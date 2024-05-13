package com.tokopedia.content.product.picker.seller.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref.Key.ProductSummaryCommission
import com.tokopedia.content.common.view.fragment.LoadingDialogFragment
import com.tokopedia.content.product.picker.R
import com.tokopedia.content.product.picker.databinding.BottomSheetSellerProductSummaryBinding
import com.tokopedia.content.product.picker.seller.analytic.ContentPinnedProductAnalytic
import com.tokopedia.content.product.picker.seller.analytic.ContentProductPickerSellerAnalytic
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.content.product.picker.seller.model.exception.PinnedProductException
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductChooserEvent
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductSetupAction
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductTagSummaryUiModel
import com.tokopedia.content.product.picker.seller.util.productTagSummaryEmpty
import com.tokopedia.content.product.picker.seller.view.viewcomponent.ProductSummaryListViewComponent
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 04/02/22
 */
class ProductSummaryBottomSheet @Inject constructor(
    private val analytic: ContentProductPickerSellerAnalytic,
    private val pinnedProductAnalytic: ContentPinnedProductAnalytic,
    private val coachMarkSharedPref: ContentCoachMarkSharedPref,
) : BaseProductSetupBottomSheet(), ProductSummaryListViewComponent.Listener {

    private var mListener: Listener? = null

    private var _binding: BottomSheetSellerProductSummaryBinding? = null
    private val binding: BottomSheetSellerProductSummaryBinding
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
        pinnedProductAnalytic.onClickPinProductBottomSheet(product.id)
        viewModel.submitAction(ProductSetupAction.ClickPinProduct(product))
    }

    override fun onImpressPinnedProduct(product: ProductUiModel) {
        pinnedProductAnalytic.onImpressPinProductBottomSheet(product.id)
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

    override fun onStart() {
        super.onStart()
        analytic.viewProductSummary()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mListener = null
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetSellerProductSummaryBinding.inflate(
            LayoutInflater.from(requireContext()),
        )
        clearContentPadding = true
        setChild(binding.root)
    }

    private fun setupView() {
        binding.root.layoutParams = binding.root.layoutParams.apply {
            height = (getScreenHeight() * SCREEN_HEIGHT_DIVIDER).toInt()
        }
        setTitle(getString(R.string.product_summary_title))
        setAction(getString(R.string.product_add_more)) {
            analytic.clickAddMoreProductOnProductSetup()
            handleAddMoreProduct()
        }
        setCloseClickListener {
            analytic.clickCloseOnProductSummary()
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
                when (state.productTagSummary) {
                    ProductTagSummaryUiModel.Loading -> {
                        showLoading(true)
                        binding.globalError.visibility = View.GONE
                    }
                    else -> {
                        mListener?.onProductChanged(state.productTagSectionList)

                        setTitle(state.productCount)
                        showLoading(false)
                        binding.globalError.visibility = View.GONE
                        binding.flBtnDoneContainer.visibility = View.VISIBLE

                        productSummaryListView.setProductList(
                            productSectionList = state.productTagSectionList,
                            isEligibleForPin = viewModel.isEligibleForPin,
                            isProductNumerationShown = viewModel.isNumerationShown,
                            selectedAccount = viewModel.selectedAccount,
                        )
                        setupProductCommissionCoachMark()

                        if(state.productTagSectionList.isEmpty()) {
                            binding.globalError.productTagSummaryEmpty { handleAddMoreProduct() }
                            binding.globalError.visibility = View.VISIBLE
                            binding.flBtnDoneContainer.visibility = View.GONE
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is ProductChooserEvent.GetDataError -> {
                        toaster.showError(
                            err = event.throwable,
                            actionLabel = getString(R.string.content_product_picker_retry),
                            actionListener = { event.action?.invoke() },
                        )

                        productSummaryListView.setProductList(
                            productSectionList = emptyList(),
                            isEligibleForPin = viewModel.isEligibleForPin,
                            isProductNumerationShown = viewModel.isNumerationShown,
                            selectedAccount = viewModel.selectedAccount,
                        )
                        showLoading(false)
                    }
                    is ProductChooserEvent.DeleteProductSuccess -> {
                        toaster.showToaster(
                            message = getString(
                                R.string.product_summary_success_delete_product,
                                event.deletedProductCount
                            ),
                        )
                    }
                    is ProductChooserEvent.DeleteProductError -> {
                        toaster.showError(
                            err = event.throwable,
                            customErrMessage = getString(R.string.product_summary_fail_to_delete_product),
                            actionLabel = getString(R.string.content_product_picker_retry),
                            actionListener = { event.action?.invoke() },
                        )

                        showLoading(false)
                    }
                    is ProductChooserEvent.FailPinUnPinProduct -> {
                        if (event.isPinned) pinnedProductAnalytic.onImpressFailUnPinProductBottomSheet()
                        else pinnedProductAnalytic.onImpressFailPinProductBottomSheet()
                        if (event.throwable is PinnedProductException) {
                            pinnedProductAnalytic.onImpressColdDownPinProductSecondEvent(false)
                            toaster.showToaster(
                                message = if (event.throwable.message.isEmpty()) getString(R.string.product_pin_failed) else event.throwable.message,
                                type = Toaster.TYPE_ERROR
                            )
                        } else {
                            toaster.showError(
                                err = event.throwable
                            )
                        }
                    }
                    else -> return@collect
                }
            }
        }
    }

    private fun showLoading(isShow: Boolean) {
        if (isShow) {
            if (!isLoadingDialogVisible())
                loadingDialogFragment.show(childFragmentManager)
        } else if (loadingDialogFragment.isAdded) {
            loadingDialogFragment.dismiss()
        }
    }

    private fun isLoadingDialogVisible(): Boolean {
        return loadingDialogFragment.isVisible
    }

    private fun setTitle(productCount: Int?) {
        if (productCount != null) {
            setTitle(
                getString(
                    R.string.product_summary_title_with_count,
                    productCount,
                    viewModel.maxProduct
                )
            )
        } else {
            setTitle(getString(R.string.product_summary_title))
        }
    }

    private fun handleAddMoreProduct() {
        mListener?.onShouldAddProduct(this)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    private fun setupProductCommissionCoachMark() {
        if (coachMarkSharedPref.hasBeenShown(ProductSummaryCommission)) return
        coachMarkSharedPref.setHasBeenShown(ProductSummaryCommission)

        mListener?.onProductSummaryCommissionShown()
        productSummaryListView.getProductCommissionCoachMark { firstTextCommission ->
            val coachMark = CoachMark2(requireContext())
            val coachMarkItem = CoachMark2Item(
                anchorView = firstTextCommission,
                title = getString(R.string.product_affiliate_coach_mark_product_summary_commission_title),
                description = getString(R.string.product_affiliate_coach_mark_product_summary_commission_subtitle),
                position = CoachMark2.POSITION_BOTTOM,
            )

            viewLifecycleOwner.lifecycleScope.launch {
                delay(DELAY_COACH_MARK_DURATION)
                coachMark.isOutsideTouchable = true
                coachMark.showCoachMark(arrayListOf(coachMarkItem))
            }
        }
    }

    companion object {
        private const val TAG = "ProductSummaryBottomSheet"
        private const val SCREEN_HEIGHT_DIVIDER = 0.85f
        private const val DELAY_COACH_MARK_DURATION = 1000L

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

        fun onProductSummaryCommissionShown() {}

        fun onFinish(bottomSheet: ProductSummaryBottomSheet)
    }
}
