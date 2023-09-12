package com.tokopedia.minicart.bmgm.presentation.bottomsheet

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.bmgm.analytics.BmgmMiniCartTracker
import com.tokopedia.minicart.bmgm.common.di.DaggerBmgmComponent
import com.tokopedia.minicart.bmgm.common.utils.MiniCartUtils
import com.tokopedia.minicart.bmgm.common.utils.logger.NonFatalIssueLogger
import com.tokopedia.minicart.bmgm.presentation.adapter.BmgmMiniCartDetailAdapter
import com.tokopedia.minicart.bmgm.presentation.adapter.itemdecoration.BmgmMiniCartDetailItemDecoration
import com.tokopedia.minicart.bmgm.presentation.model.BmgmState
import com.tokopedia.minicart.bmgm.presentation.model.MiniCartDetailUiModel
import com.tokopedia.minicart.bmgm.presentation.viewmodel.BmgmMiniCartDetailViewModel
import com.tokopedia.minicart.databinding.BottomSheetBmgmMiniCartDetailBinding
import com.tokopedia.minicart.databinding.ViewBmgmMiniCartSubTotalBinding
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import dagger.Lazy
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartDetailBottomSheet : BottomSheetUnify() {

    companion object {

        const val TAG = "BmgmCartBottomSheet"

        fun getInstance(fm: FragmentManager): BmgmMiniCartDetailBottomSheet {
            return (fm.findFragmentByTag(TAG) as? BmgmMiniCartDetailBottomSheet)
                ?: BmgmMiniCartDetailBottomSheet().apply {
                    showCloseIcon = true
                    showKnob = false
                    isDragable = true
                    clearContentPadding = true
                    isHideable = true
                    isSkipCollapseState = true
                }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: Lazy<UserSessionInterface>

    private var binding: BottomSheetBmgmMiniCartDetailBinding? = null
    private var footerBinding: ViewBmgmMiniCartSubTotalBinding? = null
    private val listAdapter = BmgmMiniCartDetailAdapter()
    private val viewModel by lazy {
        ViewModelProvider(
            this, viewModelFactory
        )[BmgmMiniCartDetailViewModel::class.java]
    }
    private var cartData: BmgmCommonDataModel? = null
    private val impressHolder = ImpressHolder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetBmgmMiniCartDetailBinding.inflate(inflater).apply {
            footerBinding = ViewBmgmMiniCartSubTotalBinding.bind(root)
            setChild(root)
            setTitle(getString(R.string.mini_cart_bmgm_bottom_sheet_title))
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProductList()
        observeCartList()
        observeCartCheckedListState()
    }

    private fun observeCartList() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.cartData.collect {
                when (it) {
                    is BmgmState.Success -> setOnCartDataSuccess(it.data)
                    is BmgmState.Error -> {
                        sendLogger(it.t)
                        setOnError()
                    }

                    else -> {/* no-op */
                    }
                }
            }
        }
        viewModel.getCartData()
    }

    private fun setOnCartDataSuccess(data: BmgmCommonDataModel) {
        this.cartData = data
        showProducts(data)
        setupCartEntryPoint()
        setOnCloseClicked()
        sendImpressionTracker()
    }

    private fun observeCartCheckedListState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.setCheckListState.collectLatest {
                when (it) {
                    is BmgmState.Loading -> showLoadingButton()
                    is BmgmState.Success -> openCartPage()
                    is BmgmState.Error -> {
                        sendLogger(it.t)
                        openCartPage()
                    }

                    else -> {/* no-op */
                    }
                }
            }
        }
    }

    private fun sendLogger(t: Throwable) {
        NonFatalIssueLogger.logToCrashlytics(t, this::class.java.canonicalName)
    }

    private fun openCartPage() {
        dismissLoadingButton()
        dismiss()
        RouteManager.route(context, ApplinkConst.CART)
    }

    private fun sendClickCheckCartEvent() {
        cartData?.let { data ->
            BmgmMiniCartTracker.sendClickCekKeranjangEvent(
                offerId = data.offerId.toString(),
                warehouseId = data.warehouseId.toString(),
                userId = data.shopId,
                shopId = userSession.get().userId
            )
        }
    }

    private fun showLoadingButton() {
        footerBinding?.btnBmgmOpenCart?.isLoading = true
    }

    private fun dismissLoadingButton() {
        footerBinding?.btnBmgmOpenCart?.isLoading = false
    }

    private fun setupProductList() {
        binding?.rvBmgmMiniCartBottomSheet?.run {
            if (itemDecorationCount == Int.ZERO) {
                addItemDecoration(BmgmMiniCartDetailItemDecoration())
            }
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }
    }

    private fun showProducts(data: BmgmCommonDataModel) {
        val productList = getDiscountedProductList(data.tiersApplied, data.hasReachMaxDiscount)
        listAdapter.clearAllElements()
        listAdapter.addElement(productList)
    }

    private fun sendImpressionTracker() {
        cartData?.let { data ->
            binding?.root?.addOnImpressionListener(impressHolder) {
                BmgmMiniCartTracker.sendImpressionMinicartEvent(
                    offerId = data.offerId.toString(),
                    warehouseId = data.warehouseId.toString(),
                    userId = data.shopId,
                    shopId = userSession.get().userId
                )
            }
        }
    }

    private fun setOnCloseClicked() {
        cartData?.let { data ->
            super.setCloseClickListener {
                BmgmMiniCartTracker.sendClickCloseMinicartEvent(
                    offerId = data.offerId.toString(),
                    warehouseId = data.warehouseId.toString(),
                    shopId = data.shopId,
                    userId = userSession.get().userId
                )
                dismiss()
            }
        }
    }

    private fun setupCartEntryPoint() {
        cartData?.let { model ->
            if (!model.showMiniCartFooter) {
                binding?.containerSubTotal?.gone()
                return
            }
            binding?.containerSubTotal?.visible()

            footerBinding?.run {
                tvBmgmFinalPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    model.finalPrice, false
                )
                val showCrossedPrice = model.finalPrice != model.priceBeforeBenefit
                if (showCrossedPrice) {
                    tvBmgmPriceBeforeDiscount.visible()
                    tvBmgmPriceBeforeDiscount.text =
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            model.priceBeforeBenefit, false
                        )
                    tvBmgmPriceBeforeDiscount.paintFlags =
                        tvBmgmPriceBeforeDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    tvBmgmPriceBeforeDiscount.gone()
                }

                btnBmgmOpenCart.isEnabled = true
                btnBmgmOpenCart.setOnClickListener {
                    setCartListCheckboxState(model)
                    sendClickCheckCartEvent()
                }
            }
        }
    }

    private fun setCartListCheckboxState(model: BmgmCommonDataModel) {
        context?.let {
            val isOfferEnded = MiniCartUtils.checkIsOfferEnded(model.offerEndDate)
            if (!isOfferEnded) {
                viewModel.setCartListCheckboxState(getCartIds(model.tiersApplied))
            } else {
                val appLink = UriUtil.buildUri(
                    uriPattern = ApplinkConst.BUY_MORE_GET_MORE_OLP, model.offerId.toString()
                )
                RouteManager.route(it, appLink)
                activity?.finish()
            }
        }
    }

    private fun setOnError() {
        activity?.finish()
    }

    private fun getCartIds(tiersApplied: List<BmgmCommonDataModel.TierModel>): List<String> {
        return tiersApplied.map { it.products }.flatten().map { it.cartId }
    }

    private fun getDiscountedProductList(
        tiers: List<BmgmCommonDataModel.TierModel>, hasReachMaxDiscount: Boolean
    ): List<MiniCartDetailUiModel> {
        val items = mutableListOf<MiniCartDetailUiModel>()
        tiers.forEach { tier ->
            val isDiscountedTier = !tier.isNonDiscountProducts()
            if (isDiscountedTier) {
                items.add(getDiscountSectionText(tier, isDiscountedTier = true))
                items.addAll(mapToProductList(tier.products, isDiscountedTier = true))
            } else {
                items.add(getDiscountSectionText(tier, !hasReachMaxDiscount))
                items.addAll(mapToProductList(tier.products, !hasReachMaxDiscount))
            }
        }
        return items.toList()
    }

    private fun mapToProductList(
        products: List<BmgmCommonDataModel.ProductModel>, isDiscountedTier: Boolean
    ): List<MiniCartDetailUiModel.Product> {
        return products.mapIndexed { i, product ->
            val isFirstItem = i == Int.ZERO
            val isLastItem = i == products.size.minus(Int.ONE)
            return@mapIndexed MiniCartDetailUiModel.Product(
                showVerticalDivider = isDiscountedTier,
                product = product,
                showTopSpace = !isFirstItem,
                showBottomSpace = !isLastItem
            )
        }
    }

    private fun getDiscountSectionText(
        model: BmgmCommonDataModel.TierModel, isDiscountedTier: Boolean
    ): MiniCartDetailUiModel {
        return MiniCartDetailUiModel.Section(
            sectionText = model.tierMessage, isDiscountSection = isDiscountedTier
        )
    }

    private fun initInjector() {
        context?.let {
            DaggerBmgmComponent.builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
        }
    }
}