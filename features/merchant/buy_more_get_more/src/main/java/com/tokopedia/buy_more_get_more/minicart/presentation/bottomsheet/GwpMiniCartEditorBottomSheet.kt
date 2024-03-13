package com.tokopedia.buy_more_get_more.minicart.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.bmsm_widget.domain.entity.MainProduct
import com.tokopedia.bmsm_widget.domain.entity.PageSource
import com.tokopedia.bmsm_widget.domain.entity.TierGifts
import com.tokopedia.bmsm_widget.presentation.bottomsheet.GiftListBottomSheet
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.BottomSheetGwpMiniCartEditorBinding
import com.tokopedia.buy_more_get_more.minicart.analytics.BmgmMiniCartTracker
import com.tokopedia.buy_more_get_more.minicart.common.di.DaggerBmgmComponent
import com.tokopedia.buy_more_get_more.minicart.common.utils.MiniCartUtils
import com.tokopedia.buy_more_get_more.minicart.common.utils.logger.NonFatalIssueLogger
import com.tokopedia.buy_more_get_more.minicart.domain.model.MiniCartParam
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.GwpMiniCartEditorAdapter
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.diffutil.MiniCartDiffUtilCallback
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmState
import com.tokopedia.buy_more_get_more.minicart.presentation.model.GwpMiniCartEditorVisitable
import com.tokopedia.buy_more_get_more.minicart.presentation.model.effect.MiniCartEditorEffect
import com.tokopedia.buy_more_get_more.minicart.presentation.model.event.MiniCartEditorEvent
import com.tokopedia.buy_more_get_more.minicart.presentation.model.state.MiniCartEditorState
import com.tokopedia.buy_more_get_more.minicart.presentation.viewmodel.MiniCartEditorViewModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.v2.MiniCartV2Widget
import com.tokopedia.minicart.v2.MiniCartV2WidgetListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 04/12/23.
 */

class GwpMiniCartEditorBottomSheet : BottomSheetUnify(), GwpMiniCartEditorAdapter.Listener {

    companion object {

        private const val TAG = "GwpMiniCartEditorBottomSheet"

        fun newInstance(): GwpMiniCartEditorBottomSheet {
            return GwpMiniCartEditorBottomSheet().apply {
                showCloseIcon = true
                isSkipCollapseState = true
                clearContentPadding = true
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MiniCartEditorViewModel::class.java]
    }

    private var offerEndDate = ""
    private var param = MiniCartParam()
    private var binding: BottomSheetGwpMiniCartEditorBinding? = null
    private var adapter: GwpMiniCartEditorAdapter? = null
    private var miniCartV2WidgetListener: MiniCartV2WidgetListener? = null
    private val impressionHolder by lazy { ImpressHolder() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        viewModel.setEvent(MiniCartEditorEvent.FetchData(param))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetGwpMiniCartEditorBinding.inflate(inflater, container, false).apply {
            setChild(root)
        }
        adapter = GwpMiniCartEditorAdapter(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        binding = null
        adapter = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeIfOfferEnded()
        setTitle(view.context.getString(R.string.bmgm_mini_cart_bmgm_bottom_sheet_title))

        setupView()
        collectData()
        collectEffect()
        collectSetCheckListState()
    }

    override fun onCartItemQuantityChanged(
        product: BmgmMiniCartVisitable.ProductUiModel,
        newQty: Int
    ) {
        viewModel.setEvent(MiniCartEditorEvent.AdjustQuantity(product, newQty))
    }

    override fun onCartItemQuantityDeleted(product: BmgmMiniCartVisitable.ProductUiModel) {
        viewModel.setEvent(MiniCartEditorEvent.DeleteCart(product))
    }

    override fun onReloadClicked() {
        viewModel.setEvent(MiniCartEditorEvent.FetchData(param))
    }

    override fun onGiftCtaClicked() {
        val data = viewModel.cartData.value.data
        val warehouseId = param.warehouseIds.firstOrNull() ?: return
        val selectedOfferId = data.offerId
        val tiersGift = data.tiers.filterIsInstance<BmgmMiniCartVisitable.GwpGiftWidgetUiModel>()
            .flatMap { it.productList }.groupBy { it.tierId }
        val selectedTierId = tiersGift.keys.firstOrNull().orZero()
        val shopId = param.shopIds.firstOrNull().orZero().toString()
        val userId = viewModel.getUserId()
        val mainProducts = data.products.map { MainProduct(it.productId.toLongOrZero(), it.productQuantity) }

        val bottomSheet = GiftListBottomSheet.newInstance(
            offerId = selectedOfferId,
            warehouseId = warehouseId,
            tierGifts = tiersGift.map {
                TierGifts(
                    tierId = it.key,
                    gifts = it.value.map { benefit ->
                        TierGifts.GiftProduct(benefit.id.toLongOrZero(), benefit.qty)
                    }
                )
            },
            pageSource = PageSource.OFFER_LANDING_PAGE,
            autoSelectTierChipByTierId = selectedTierId,
            shopId = shopId,
            mainProducts = mainProducts
        )
        if (childFragmentManager.isStateSaved || bottomSheet.isAdded) return
        bottomSheet.show(childFragmentManager, bottomSheet.tag)

        BmgmMiniCartTracker.sendClickHadiahEntryEvent(
            offerId = selectedOfferId.toString(),
            warehouseId = warehouseId.toString(),
            shopId = shopId,
            userId = userId
        )
    }

    fun setParameter(param: MiniCartParam, offerEndDate: String) {
        this.offerEndDate = offerEndDate
        this.param = param
    }

    fun show(fm: FragmentManager) {
        if (fm.isStateSaved || isAdded || isVisible) return

        show(fm, TAG)
    }

    private fun closeIfOfferEnded() {
        val isOfferEnded = MiniCartUtils.checkIsOfferEnded(offerEndDate)
        if (isOfferEnded) {
            dismiss()
            activity?.finish()
        }
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.cartData.collect {
                when (it.state) {
                    MiniCartEditorState.State.LOADING -> setOnLoading()
                    MiniCartEditorState.State.PARTIALLY_LOADING -> showPartiallyLoading(it.data)
                    MiniCartEditorState.State.ERROR -> setOnError()
                    MiniCartEditorState.State.SUCCESS -> setOnSuccess(it.data)
                }
            }
        }
    }

    private fun showPartiallyLoading(data: BmgmMiniCartDataUiModel) {
        updateList(data.tiers)
        binding?.miniCartViewGwp?.showLoading()
    }

    private fun collectEffect() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.effect.collect {
                when (it) {
                    is MiniCartEditorEffect.OnRemoveFailed -> showErrorToaster(it.throwable)
                    is MiniCartEditorEffect.DismissBottomSheet -> dismiss()
                }
            }
        }
    }

    private fun collectSetCheckListState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.setCheckListState.collectLatest {
                when (it) {
                    is BmgmState.Loading -> binding?.miniCartViewGwp?.showLoading()
                    is BmgmState.Success -> openCartPage()
                    is BmgmState.Error -> {
                        sendLogger(it.t)
                        openCartPage()
                    }

                    else -> {} /* no-op */
                }
            }
        }
    }

    private fun sendLogger(t: Throwable) {
        NonFatalIssueLogger.logToCrashlytics(t, this::class.java.canonicalName)
    }

    private fun openCartPage() {
        binding?.miniCartViewGwp?.dismissLoading()
        dismiss()
        RouteManager.route(context, ApplinkConst.CART)
    }

    private fun showErrorToaster(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, throwable)
        if (message.isNotBlank()) {
            val anchor = view?.rootView ?: return
            Toaster.build(anchor, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
        }
    }

    private fun setOnSuccess(data: BmgmMiniCartDataUiModel) {
        val isEmpty = data.tiers.filterIsInstance<BmgmMiniCartVisitable.ProductUiModel>().isEmpty()
        if (isEmpty) {
            dismiss()
            return
        }

        updateList(data.tiers)
        showSummary(data)
        sendAnalyticImpressionBottomSheet(data)
        sendAnalyticCloseClicked(data)
    }

    private fun sendAnalyticCloseClicked(data: BmgmMiniCartDataUiModel) {
        bottomSheetClose.setOnClickListener {
            val warehouseId = param.warehouseIds.firstOrNull()?.toString().orEmpty()
            val shopId = param.shopIds.firstOrNull()?.toString().orEmpty()
            val userSession = viewModel.getUserId()
            BmgmMiniCartTracker.sendClickCloseMinicartEvent(
                offerId = data.offerId.toString(),
                warehouseId = warehouseId,
                shopId = shopId,
                userId = userSession
            )
            dismiss()
        }
    }

    private fun sendAnalyticImpressionBottomSheet(data: BmgmMiniCartDataUiModel) {
        binding?.root?.addOnImpressionListener(impressionHolder) {
            val warehouseId = param.warehouseIds.firstOrNull()?.toString().orEmpty()
            val shopId = param.shopIds.firstOrNull()?.toString().orEmpty()
            val userSession = viewModel.getUserId()
            BmgmMiniCartTracker.sendImpressionMinicartEvent(
                offerId = data.offerId.toString(),
                warehouseId = warehouseId,
                userId = shopId,
                shopId = userSession
            )
        }
    }

    private fun showSummary(data: BmgmMiniCartDataUiModel) {
        binding?.miniCartViewGwp?.run {
            dismissLoading()
            visible()
            refresh(
                MiniCartSimplifiedData(
                    isShowMiniCartWidget = data.tiers.isNotEmpty(),
                    miniCartWidgetData = MiniCartWidgetData(
                        totalProductOriginalPrice = data.priceBeforeBenefit,
                        totalProductPrice = data.finalPrice
                    )
                )
            )
        }
    }

    private fun setOnError() {
        binding?.miniCartViewGwp?.dismissLoading()
        binding?.miniCartViewGwp?.gone()
        updateList(listOf(GwpMiniCartEditorVisitable.MiniCartEditorErrorState))
    }

    private fun setOnLoading() {
        updateList(listOf(GwpMiniCartEditorVisitable.MiniCartEditorLoadingState))
        binding?.miniCartViewGwp?.visible()
        binding?.miniCartViewGwp?.showLoading()
    }

    private fun initInjector() {
        val applicationContext = context?.applicationContext ?: return
        DaggerBmgmComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    private fun setupView() {
        binding?.run {
            rvMiniCartEditor.layoutManager = LinearLayoutManager(root.context)
            rvMiniCartEditor.adapter = adapter

            initMiniCart()
        }
    }

    private fun initMiniCart() {
        val listener = miniCartV2WidgetListener ?: createMiniCartV2Listener()
        binding?.miniCartViewGwp?.initialize(
            MiniCartV2Widget.MiniCartV2WidgetConfig(
                showTopShadow = false,
                showChevron = false,
                showOriginalTotalPrice = false,
                overridePrimaryButtonAction = true,
                overridePrimaryButtonWording = context?.getString(R.string.bmsm_check_cart)
                    .orEmpty(),
                page = MiniCartAnalytics.Page.BMSM_OLP_PAGE
            ),
            listener
        )
        binding?.miniCartViewGwp?.showLoading()
    }

    private fun updateList(items: List<BmgmMiniCartVisitable>) {
        if (items.isEmpty()) {
            dismiss()
            return
        }

        val adapter = adapter ?: return
        val oldList = adapter.data.toList()
        val diffCallback = MiniCartDiffUtilCallback(oldList, items)
        val diffUtil = DiffUtil.calculateDiff(diffCallback)
        adapter.data.clear()
        adapter.data.addAll(items)
        diffUtil.dispatchUpdatesTo(adapter)
    }

    private fun createMiniCartV2Listener(): MiniCartV2WidgetListener {
        val listener = object : MiniCartV2WidgetListener() {
            override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
            }

            override fun onPrimaryButtonClickListener() {
                setCartListCheckboxState()
                sendAnalyticCheckCart()
            }
        }
        miniCartV2WidgetListener = listener
        return listener
    }

    private fun sendAnalyticCheckCart() {
        val offerId = param.offerIds.firstOrNull()?.toString().orEmpty()
        val warehouseId = param.warehouseIds.firstOrNull()?.toString().orEmpty()
        val shopId = param.shopIds.firstOrNull()?.toString().orEmpty()

        BmgmMiniCartTracker.sendClickCekKeranjangEvent(
            offerId = offerId,
            warehouseId = warehouseId,
            userId = shopId,
            shopId = viewModel.getUserId()
        )
    }

    private fun setCartListCheckboxState() {
        val isOfferEnded = MiniCartUtils.checkIsOfferEnded(offerEndDate)
        if (isOfferEnded) {
            dismiss()
        } else {
            viewModel.setEvent(MiniCartEditorEvent.SetCartListCheckboxState)
        }
    }
}
