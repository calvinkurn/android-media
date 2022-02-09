package com.tokopedia.play.broadcaster.setup.product.view.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroProductSummaryBinding
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductSummaryAction
import com.tokopedia.play.broadcaster.setup.product.model.ProductTagSummaryUiModel
import com.tokopedia.play.broadcaster.setup.product.view.ProductSetupFragment
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.ProductSummaryListViewComponent
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.util.extension.productEtalaseEmpty
import com.tokopedia.play.broadcaster.util.extension.productTagSummaryEmpty
import com.tokopedia.play.broadcaster.util.extension.showErrorToaster
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.processNextEventInCurrentThread
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 04/02/22
 */
class ProductSummaryBottomSheet @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val dialogCustomizer: PlayBroadcastDialogCustomizer,
) : BottomSheetUnify(), ProductSummaryListViewComponent.Listener {

    private lateinit var viewModel: PlayBroProductSetupViewModel

    private var _binding: BottomSheetPlayBroProductSummaryBinding? = null
    private val binding: BottomSheetPlayBroProductSummaryBinding
        get() = _binding!!

    private val productSummaryListView by viewComponent {
        ProductSummaryListViewComponent(binding.rvProductSummaries, this)
    }

    @ExperimentalStdlibApi
    override fun onProductDeleteClicked(product: ProductUiModel) {
        viewModel.submitAction(PlayBroProductSummaryAction.DeleteProduct(product))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            dialogCustomizer.customize(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireParentFragment(), viewModelFactory)
            .get(PlayBroProductSetupViewModel::class.java)
        setupBottomSheet()
    }

    /** TODO: gonna delete this later */
    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObserve()

        viewModel.submitAction(PlayBroProductSummaryAction.LoadProductSummary)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetPlayBroProductSummaryBinding.inflate(
            LayoutInflater.from(requireContext()),
        )
        clearContentPadding = true
        setChild(binding.root)
    }

    private fun setupView() {
        binding.root.layoutParams = binding.root.layoutParams.apply {
            height = (getScreenHeight() * 0.85f).toInt()
        }
        setTitle(getString(R.string.play_bro_product_summary_title))
        setAction(getString(R.string.play_bro_product_add_more)) {
            handleAddMoreProduct()
        }

        binding.btnDone.setOnClickListener {
            dismiss()
        }
    }

    @ExperimentalStdlibApi
    /** TODO: gonna remove this annotation later */
    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest { (prevState, state) ->
                when(state.productTagSummary) {
                    is ProductTagSummaryUiModel.Loading -> {
                        binding.ivLoading.visibility = View.VISIBLE
                        binding.globalError.visibility = View.GONE
                        binding.flBtnDoneContainer.visibility = View.GONE
                    }
                    is ProductTagSummaryUiModel.LoadingWithPlaceholder -> {
                        setTitle(null)
                        binding.ivLoading.visibility = View.GONE
                        binding.globalError.visibility = View.GONE
                        binding.flBtnDoneContainer.visibility = View.GONE

                        productSummaryListView.setLoading()
                    }
                    is ProductTagSummaryUiModel.Success -> {
                        setTitle(state.productTagSummary.productCount)
                        binding.ivLoading.visibility = View.GONE
                        binding.globalError.visibility = View.GONE
                        binding.flBtnDoneContainer.visibility = View.VISIBLE

                        productSummaryListView.setProductList(state.productTagSummary.sections)
                    }
                    is ProductTagSummaryUiModel.Empty -> {
                        setTitle(0)
                        productSummaryListView.setProductList(emptyList())

                        binding.globalError.productTagSummaryEmpty { handleAddMoreProduct() }
                        binding.globalError.visibility = View.VISIBLE
                        binding.flBtnDoneContainer.visibility = View.GONE
                    }
                    is ProductTagSummaryUiModel.Error -> {
                        view?.showErrorToaster(
                            err = state.productTagSummary.throwable,
                            customErrMessage = state.productTagSummary.throwable.localizedMessage
                                ?: getString(R.string.play_broadcaster_default_error),
                            actionLabel = getString(R.string.play_broadcast_try_again),
                            actionListener = { viewModel.submitAction(PlayBroProductSummaryAction.LoadProductSummary) }
                        )

                        setTitle(null)
                        productSummaryListView.setProductList(emptyList())

                        binding.ivLoading.visibility = View.GONE
                        binding.flBtnDoneContainer.visibility = View.GONE
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setTitle(productCount: Int?) {
        if(productCount != null) {
            setTitle(getString(R.string.play_bro_product_summary_title_with_count, productCount, viewModel.maxProduct))
        }
        else {
            setTitle(getString(R.string.play_bro_product_summary_title))
        }
    }

    private fun handleAddMoreProduct() {
        dismiss()
        (parentFragment as? ProductSetupFragment)
            ?.openProductChooser()
    }

    companion object {
        private const val TAG = "ProductSummaryBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): ProductSummaryBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ProductSummaryBottomSheet
            return if (oldInstance != null) oldInstance
            else {
                val fragmentFactory = fragmentManager.fragmentFactory
                fragmentFactory.instantiate(
                    classLoader,
                    ProductSummaryBottomSheet::class.java.name
                ) as ProductSummaryBottomSheet
            }
        }
    }
}