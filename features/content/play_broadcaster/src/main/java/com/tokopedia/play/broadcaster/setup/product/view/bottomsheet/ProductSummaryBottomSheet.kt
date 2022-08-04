package com.tokopedia.play.broadcaster.setup.product.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroProductSummaryBinding
import com.tokopedia.play.broadcaster.domain.model.PinnedProductException
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserEvent
import com.tokopedia.play.broadcaster.setup.product.model.ProductSetupAction
import com.tokopedia.play.broadcaster.setup.product.model.ProductTagSummaryUiModel
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.ProductSummaryListViewComponent
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.util.extension.productTagSummaryEmpty
import com.tokopedia.play.broadcaster.view.fragment.loading.LoadingDialogFragment
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.Toaster
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

    private var _binding: BottomSheetPlayBroProductSummaryBinding? = null
    private val binding: BottomSheetPlayBroProductSummaryBinding
        get() = _binding!!

    private val loadingDialogFragment: LoadingDialogFragment
        get() = LoadingDialogFragment.get(childFragmentManager, requireActivity().classLoader)

    private val productSummaryListView by viewComponent {
        ProductSummaryListViewComponent(binding.rvProductSummaries, this)
    }

    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(binding.toasterLayout, it.viewLifecycleOwner) }
    )

    override fun onProductDeleteClicked(product: ProductUiModel) {
        analytic.clickDeleteProductOnProductSetup(productId = product.id)
        viewModel.submitAction(ProductSetupAction.DeleteSelectedProduct(product))
    }

    override fun onPinClicked(product: ProductUiModel) {
        viewModel.submitAction(ProductSetupAction.ClickPinProduct(product))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObserve()
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
            mListener?.onFinish(this)
        }

        binding.btnDone.setOnClickListener {
            analytic.clickDoneOnProductSetup()
            mListener?.onFinish(this)
        }
    }

    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.summaryUiState.withCache().collectLatest { (prevState, state) ->
                when(state.productTagSummary) {
                    is ProductTagSummaryUiModel.Loading -> {
                        showLoading(true)
                        binding.globalError.visibility = View.GONE
                    }
                    is ProductTagSummaryUiModel.Success -> {
                        mListener?.onProductChanged(state.productTagSectionList)

                        setTitle(state.productCount)
                        showLoading(false)
                        binding.globalError.visibility = View.GONE
                        binding.flBtnDoneContainer.visibility = View.VISIBLE

                        productSummaryListView.setProductList(state.productTagSectionList, viewModel.isEligibleForPin)

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
                        toaster.showError(
                            err = event.throwable,
                            actionLabel = getString(R.string.play_broadcast_try_again),
                            actionListener = { event.action?.invoke() },
                        )

                        productSummaryListView.setProductList(emptyList(), viewModel.isEligibleForPin)
                        showLoading(false)
                    }
                    is PlayBroProductChooserEvent.DeleteProductSuccess -> {
                        toaster.showToaster(
                            message = getString(R.string.play_bro_product_summary_success_delete_product, event.deletedProductCount),
                        )
                    }
                    is PlayBroProductChooserEvent.DeleteProductError -> {
                        toaster.showError(
                            err = event.throwable,
                            customErrMessage = getString(R.string.play_bro_product_summary_fail_to_delete_product),
                            actionLabel = getString(R.string.play_broadcast_try_again),
                            actionListener = { event.action?.invoke() },
                        )

                        showLoading(false)
                    }
                    is PlayBroProductChooserEvent.ShowError -> {
                        if(event.error is PinnedProductException){
                            toaster.showToaster(
                                message = if (event.error.message.isEmpty()) getString(R.string.play_bro_pin_product_failed) else event.error.message,
                                type = Toaster.TYPE_ERROR
                            )
                        }else {
                            toaster.showError(
                                err = event.error
                            )
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isShow: Boolean) {
        if(isShow) {
            if(!isLoadingDialogVisible())
                loadingDialogFragment.show(childFragmentManager)
        }
        else if(loadingDialogFragment.isAdded) {
            loadingDialogFragment.dismiss()
        }
    }

    private fun isLoadingDialogVisible(): Boolean {
        return loadingDialogFragment.isVisible
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

        fun onFinish(bottomSheet: ProductSummaryBottomSheet)
    }
}