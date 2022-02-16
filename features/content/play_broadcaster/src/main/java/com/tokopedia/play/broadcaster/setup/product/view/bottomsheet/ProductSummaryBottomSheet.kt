package com.tokopedia.play.broadcaster.setup.product.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroProductSummaryBinding
import com.tokopedia.play.broadcaster.setup.product.model.ProductSetupAction
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserEvent
import com.tokopedia.play.broadcaster.setup.product.model.ProductTagSummaryUiModel
import com.tokopedia.play.broadcaster.setup.product.view.ProductSetupFragment
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.ProductSummaryListViewComponent
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.util.extension.productTagSummaryEmpty
import com.tokopedia.play.broadcaster.util.extension.showErrorToaster
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 04/02/22
 */
class ProductSummaryBottomSheet @Inject constructor(
    private val analytic: PlayBroadcastAnalytic,
) : BaseProductSetupBottomSheet(), ProductSummaryListViewComponent.Listener {

    private var mListener: Listener? = null

    private val container: ProductSetupFragment?
        get() = parentFragment as? ProductSetupFragment

    private var _binding: BottomSheetPlayBroProductSummaryBinding? = null
    private val binding: BottomSheetPlayBroProductSummaryBinding
        get() = _binding!!

    private val productSummaryListView by viewComponent {
        ProductSummaryListViewComponent(binding.rvProductSummaries, this)
    }

    override fun onProductDeleteClicked(product: ProductUiModel) {
        analytic.clickDeleteProductOnProductSetup(productId = product.id)
        viewModel.submitAction(ProductSetupAction.DeleteSelectedProduct(product))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObserve()

        viewModel.submitAction(ProductSetupAction.LoadProductSummary)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mListener = null
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
            analytic.clickAddMoreProductOnProductSetup()
            handleAddMoreProduct()
        }
        setCloseClickListener {
            dismiss()
            container?.removeFragment()
        }

        binding.btnDone.setOnClickListener {
            analytic.clickDoneOnProductSetup()
            dismiss()
            container?.removeFragment()
        }
    }

    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.summaryUiState.withCache().collectLatest { (prevState, state) ->
                when(state.productTagSummary) {
                    is ProductTagSummaryUiModel.Loading -> {
                        binding.ivLoading.visibility = View.VISIBLE
                        binding.globalError.visibility = View.GONE
                    }
                    is ProductTagSummaryUiModel.LoadingWithPlaceholder -> {
                        binding.ivLoading.visibility = View.GONE
                        binding.globalError.visibility = View.GONE

                        productSummaryListView.setLoading()
                    }
                    is ProductTagSummaryUiModel.Success -> {
                        mListener?.onProductChanged(state.productTagSectionList)

                        setTitle(state.productCount)
                        binding.ivLoading.visibility = View.GONE
                        binding.globalError.visibility = View.GONE
                        binding.flBtnDoneContainer.visibility = View.VISIBLE

                        productSummaryListView.setProductList(state.productTagSectionList)

                        if(state.productTagSectionList.isEmpty()) {
                            binding.globalError.productTagSummaryEmpty { handleAddMoreProduct() }
                            binding.globalError.visibility = View.VISIBLE
                            binding.flBtnDoneContainer.visibility = View.GONE
                        }
                    }
                    else -> {}
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when(event) {
                    is PlayBroProductChooserEvent.GetDataError -> {
                        view?.rootView?.showErrorToaster(
                            err = event.throwable,
                            actionLabel = getString(R.string.play_broadcast_try_again),
                            actionListener = { event.action?.invoke() },
                        )

                        productSummaryListView.setProductList(emptyList())
                        binding.ivLoading.visibility = View.GONE
                    }
                    is PlayBroProductChooserEvent.DeleteProductSuccess -> {
                        view?.rootView?.showToaster(
                            message = getString(R.string.play_bro_product_summary_success_delete_product, event.deletedProductCount),
                        )
                    }
                    is PlayBroProductChooserEvent.DeleteProductError -> {
                        view?.rootView?.showErrorToaster(
                            err = event.throwable,
                            customErrMessage = getString(R.string.play_bro_product_summary_fail_to_delete_product),
                            actionLabel = getString(R.string.play_broadcast_try_again),
                            actionListener = { event.action?.invoke() },
                        )

                        binding.ivLoading.visibility = View.GONE
                    }
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
        mListener?.onShouldAddProduct(this)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
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

    interface Listener {

        fun onProductChanged(productTagSectionList: List<ProductTagSectionUiModel>)

        fun onShouldAddProduct(bottomSheet: ProductSummaryBottomSheet)
    }
}