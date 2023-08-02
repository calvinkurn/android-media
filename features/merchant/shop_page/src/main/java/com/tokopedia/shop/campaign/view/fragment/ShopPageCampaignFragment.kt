package com.tokopedia.shop.campaign.view.fragment

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.widget.extension.stepScrollToPositionWithDelay
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.ext.hasSuccessfulTranscodedChannel
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopCampaignTabTracker
import com.tokopedia.shop.analytic.ShopPageCampaignTrackingMapper
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.shop.campaign.domain.entity.ShopCampaignRedeemPromoVoucherResult
import com.tokopedia.shop.campaign.util.mapper.ShopPageCampaignMapper
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignTabAdapter
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignTabAdapterTypeFactory
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignDisplaySliderBannerHighlightViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignVoucherSliderItemViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignVoucherSliderMoreItemViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignVoucherSliderViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.WidgetConfigListener
import com.tokopedia.shop.campaign.view.bottomsheet.ExclusiveLaunchVoucherListBottomSheet
import com.tokopedia.shop.campaign.view.bottomsheet.VoucherDetailBottomSheet
import com.tokopedia.shop.campaign.view.listener.ShopCampaignCarouselProductListener
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.campaign.view.listener.ShopCampaignPlayWidgetListener
import com.tokopedia.shop.campaign.view.model.ShopCampaignWidgetCarouselProductUiModel
import com.tokopedia.shop.campaign.view.model.ShopWidgetDisplaySliderBannerHighlightUiModel
import com.tokopedia.shop.campaign.view.viewmodel.ShopCampaignViewModel
import com.tokopedia.shop.common.CropTopImageByHeightPercentageTransformation
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant
import com.tokopedia.shop.common.data.mapper.ShopPageWidgetMapper
import com.tokopedia.shop.common.data.model.ShopPageWidgetUiModel
import com.tokopedia.shop.common.extension.showToaster
import com.tokopedia.shop.common.extension.showToasterError
import com.tokopedia.shop.common.util.ShopPageExceptionHandler
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.databinding.FragmentShopPageCampaignBinding
import com.tokopedia.shop.home.WidgetName
import com.tokopedia.shop.home.WidgetType
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.fragment.ShopPageHomeFragment
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel
import com.tokopedia.shop.home.view.model.CheckCampaignNotifyMeUiModel
import com.tokopedia.shop.home.view.model.GetCampaignNotifyMeUiModel
import com.tokopedia.shop.home.view.model.NotifyMeAction
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.ShopPageLayoutUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerTimerUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetVoucherSliderUiModel
import com.tokopedia.shop.home.view.model.StatusCampaign
import com.tokopedia.shop.home.view.viewmodel.ShopHomeViewModel
import com.tokopedia.shop.pageheader.presentation.fragment.InterfaceShopPageHeader
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageHeaderFragment
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageHeaderFragmentV2
import com.tokopedia.shop.pageheader.util.ShopPageHeaderTabName
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min

class ShopPageCampaignFragment :
    ShopPageHomeFragment(),
    WidgetConfigListener,
    ShopHomeListener,
    ShopCampaignInterface,
    ShopCampaignDisplaySliderBannerHighlightViewHolder.Listener,
    ShopCampaignCarouselProductListener,
    ShopCampaignVoucherSliderViewHolder.Listener,
    ShopCampaignVoucherSliderItemViewHolder.Listener,
    ShopCampaignVoucherSliderMoreItemViewHolder.Listener,
    ShopCampaignPlayWidgetListener {

    companion object {
        private const val KEY_SHOP_ID = "SHOP_ID"
        private const val LOAD_WIDGET_ITEM_PER_PAGE = 3
        private const val LIST_WIDGET_LAYOUT_START_INDEX = 0
        private const val HOME_TAB_APP_LINK_LAST_SEGMENT = "home"
        private const val QUERY_PARAM_TAB = "tab"
        private const val PATTERN_CROP_TOP_PERCENTAGE = 0.25

        fun createInstance(shopId: String): ShopPageCampaignFragment {
            val bundle = Bundle()
            bundle.putString(KEY_SHOP_ID, shopId)
            return ShopPageCampaignFragment().apply {
                arguments = bundle
            }
        }
    }

    private val viewBindingCampaignTab: FragmentShopPageCampaignBinding? by viewBinding()
    private var listCampaignTabBackgroundColor: List<String> = listOf()
    private var listPatternImage: List<String> = listOf()
    private var recyclerViewTopPadding = 0
    private var isClickToScrollToTop = false
    private var latestCompletelyVisibleItemIndex = -1
    private var textColor: String = ""
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private var viewModelCampaign: ShopCampaignViewModel? = null
    private var isDarkTheme: Boolean = false

    private val shopCampaignTabAdapter: ShopCampaignTabAdapter
        get() = adapter as ShopCampaignTabAdapter
    private val shopCampaignTabAdapterTypeFactory by lazy {
        ShopCampaignTabAdapterTypeFactory(
            shopHomeDisplayWidgetListener = this,
            shopCampaignDisplayBannerTimerWidgetListener = this,
            shopCampaignCarouselProductListener = this,
            playWidgetCoordinator = playWidgetCoordinator,
            shopPlayWidgetListener = this,
            shopCampaignInterface = this,
            sliderBannerHighlightListener = this,
            shopCampaignVoucherSliderListener = this,
            shopCampaignVoucherSliderItemListener = this,
            shopCampaignVoucherSliderMoreItemListener = this
        )
    }

    private var globalErrorShopPage: GlobalError? = null
    private var viewBackgroundColor: View? = null

    @Inject
    lateinit var shopCampaignTabTracker: ShopCampaignTabTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelCampaign =
            ViewModelProviders.of(this, viewModelFactory).get(ShopCampaignViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_page_campaign, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeVoucherSliderWidgetData()
        observeRedeemResult()
        observeCampaignWidgetListVisitable()
        observeLatestShopCampaignWidgetLayoutData()
        observeUpdatedBannerTimerUiModelData()
        observeShopReminderButtonStatusSharedVieModel()
    }

    private fun observeShopReminderButtonStatusSharedVieModel() {
        shopReminderButtonStatusSharedVieModel?.sharedBannerTimerUiModel?.observe(viewLifecycleOwner) {bannerTimerUiModel ->
            if (!shopCampaignTabAdapter.isLoading && getSelectedFragment() != this) {
                bannerTimerUiModel?.let {
                    viewModelCampaign?.updateBannerTimerWidgetUiModel(
                        shopCampaignTabAdapter.getNewVisitableItems().toMutableList(),
                        it
                    )
                }
            }
        }
    }
    private fun observeUpdatedBannerTimerUiModelData() {
        viewModelCampaign?.updatedBannerTimerUiModelData?.observe(viewLifecycleOwner) {bannerTimerUiModel ->
            bannerTimerUiModel?.let {
                shopReminderButtonStatusSharedVieModel?.updateSharedBannerTimerData(it)
            }
        }
    }

    private fun observeLatestShopCampaignWidgetLayoutData() {
        viewModelCampaign?.latestShopCampaignWidgetLayoutData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    getRecyclerView(view)?.visible()
                    setShopLayoutData(it.data)
                }
                is Fail -> {
                    onErrorGetLatestShopCampaignWidgetLayoutData(it.throwable)
                }
            }
        }
    }

    private fun onErrorGetLatestShopCampaignWidgetLayoutData(throwable: Throwable) {
        globalErrorShopPage?.visible()
        if (throwable is MessageErrorException) {
            globalErrorShopPage?.setType(GlobalError.SERVER_ERROR)
        } else {
            globalErrorShopPage?.setType(GlobalError.NO_CONNECTION)
        }
        globalErrorShopPage?.errorSecondaryAction?.show()
        globalErrorShopPage?.setOnClickListener {
            globalErrorShopPage?.errorSecondaryAction?.hide()
            getLatestShopCampaignWidgetLayoutData()
        }
        getRecyclerView(view)?.hide()
    }

    private fun observeCampaignWidgetListVisitable() {
        viewModelCampaign?.campaignWidgetListVisitable?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    shopCampaignTabAdapter.submitList(it.data)
                }
                is Fail -> {}
            }
        }
    }

    private fun observeRedeemResult() {
        viewModelCampaign?.redeemResult?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    handleRedeemVoucherSuccess(it.data)
                }

                is Fail -> {
                    showToasterError(view ?: return@observe, it.throwable)
                }
            }
        }
    }

    private fun observeVoucherSliderWidgetData() {
        viewModelCampaign?.voucherSliderWidgetData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    viewModelCampaign?.updateVoucherSliderWidgetData(
                        shopCampaignTabAdapter.getNewVisitableItems(),
                        it.data
                    )
                }
                else -> {
                    shopCampaignTabAdapter.getVoucherSliderUiModel()?.copy(
                        isError = true
                    )?.let { voucherSliderUiModel ->
                        viewModelCampaign?.updateVoucherSliderWidgetData(
                            shopCampaignTabAdapter.getNewVisitableItems(),
                            voucherSliderUiModel
                        )
                    }
                }
            }
        }
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }

    override fun loadInitialData() {
        shopCampaignTabAdapter.showLoading()
        getRecyclerView(view)?.visible()
        recyclerViewTopPadding = getRecyclerView(view)?.paddingTop ?: 0
        globalErrorShopPage?.hide()
        checkShopLayout()
    }

    override fun checkShopLayout() {
        shopPageHomeLayoutUiModel.let {
            if (it != null) {
                setShopLayoutData(it)
                setWidgetLayoutPlaceholder()
            } else {
                getLatestShopCampaignWidgetLayoutData()
            }
        }
    }

    override fun setWidgetLayoutPlaceholder() {
        val shopHomeWidgetContentData =
            ShopPageCampaignMapper.mapShopCampaignWidgetLayoutToListShopCampaignWidget(
                listWidgetLayout,
                shopId
            )
        shopCampaignTabAdapter.setCampaignLayoutData(shopHomeWidgetContentData)
    }

    override fun setShopLayoutData(dataWidgetLayoutUiModel: ShopPageLayoutUiModel) {
        listWidgetLayout = dataWidgetLayoutUiModel.listWidgetLayout.toMutableList()
        val shopCampaignWidgetContentData =
            ShopPageCampaignMapper.mapShopCampaignWidgetLayoutToListShopCampaignWidget(
                dataWidgetLayoutUiModel.listWidgetLayout,
                shopId
            )
        shopCampaignTabAdapter.setCampaignLayoutData(shopCampaignWidgetContentData)
    }

    override fun initView() {
        globalErrorShopPage = viewBindingCampaignTab?.globalError
        viewBackgroundColor = viewBindingCampaignTab?.viewBgColor
    }

    override fun observeShopHomeWidgetContentData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModelCampaign?.shopCampaignWidgetContentData?.collect {
                when (it) {
                    is Success -> {
                        onSuccessGetShopHomeWidgetContentData(it.data)
                        renderCampaignTabBackgroundColor()
                        renderPatternBackground()
                    }

                    is Fail -> {
                        val throwable = it.throwable
                        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
                        if (!ShopUtil.isExceptionIgnored(throwable)) {
                            ShopUtil.logShopPageP2BuyerFlowAlerting(
                                tag = ShopPageLoggerConstant.Tag.SHOP_PAGE_BUYER_FLOW_TAG,
                                functionName = this@ShopPageCampaignFragment::observeShopHomeWidgetContentData.name,
                                liveDataName = ShopHomeViewModel::shopHomeWidgetContentData.name,
                                userId = userId,
                                shopId = shopId,
                                shopName = shopName,
                                errorMessage = errorMessage,
                                stackTrace = Log.getStackTraceString(throwable),
                                errType = ShopPageLoggerConstant.Tag.SHOP_PAGE_HOME_TAB_BUYER_FLOW_TAG
                            )
                        }
                        showErrorToast(errorMessage)
                    }
                }
            }
            viewModelCampaign?.shopCampaignHomeWidgetContentDataError?.collect {
                shopCampaignTabAdapter.removeShopHomeWidget(it)
            }
        }
    }

    private fun renderPatternBackground() {
        viewBindingCampaignTab?.imageBackgroundPattern?.apply {
            if (drawable == null) {
                val patternUrl = listPatternImage.getOrNull(Int.ZERO).orEmpty()
                shouldShowWithAction(patternUrl.isNotEmpty()) {}
                Glide.with(context)
                    .load(listPatternImage.getOrNull(Int.ZERO))
                    .apply(RequestOptions().override(Target.SIZE_ORIGINAL))
                    .override(Target.SIZE_ORIGINAL)
                    .transform(
                        CropTopImageByHeightPercentageTransformation(
                            PATTERN_CROP_TOP_PERCENTAGE
                        )
                    )
                    .into(this)
            }
        }
    }

    override fun observePlayWidget() {
        viewModel?.playWidgetObservable?.observe(viewLifecycleOwner) { carouselPlayWidgetUiModel ->
            shopPlayWidgetAnalytic.widgetId = carouselPlayWidgetUiModel?.widgetId.orEmpty()
            shopCampaignTabAdapter.updatePlayWidget(carouselPlayWidgetUiModel?.playWidgetState)

            val widget = carouselPlayWidgetUiModel?.playWidgetState

            if (widget?.model?.hasSuccessfulTranscodedChannel == true) showWidgetTranscodeSuccessToaster()

            val parent = getRealParentFragment()
            if (parent is InterfaceShopPageHeader) {
                val recyclerView = getRecyclerView(view)

                if (parent.isNewlyBroadcastSaved() == true) {
                    parent.clearIsNewlyBroadcastSaved()
                    recyclerView?.addOneTimeGlobalLayoutListener {
                        viewScope.launch {
                            parent.collapseAppBar()
                            val widgetPosition =
                                shopCampaignTabAdapter.list.orEmpty()
                                    .indexOfFirst { it is CarouselPlayWidgetUiModel }
                            val finalPosition = min(
                                ShopUtil.getActualPositionFromIndex(widgetPosition),
                                shopCampaignTabAdapter.itemCount.orZero()
                            )
                            recyclerView.stepScrollToPositionWithDelay(
                                finalPosition,
                                PLAY_WIDGET_NEWLY_BROADCAST_SCROLL_DELAY
                            )
                        }
                    }
                }
            }

            carouselPlayWidgetUiModel?.actionEvent?.getContentIfNotHandled()?.let {
                when (it) {
                    is CarouselPlayWidgetUiModel.Action.Delete -> showWidgetDeletedToaster()
                    is CarouselPlayWidgetUiModel.Action.DeleteFailed -> showWidgetDeleteFailedToaster(
                        it.channelId,
                        it.reason
                    )

                    else -> {
                    }
                }
            }
        }
    }

    override fun onSuccessGetShopHomeWidgetContentData(mapWidgetContentData: Map<Pair<String, String>, Visitable<*>?>) {
        shopCampaignTabAdapter.updateShopCampaignWidgetContentData(mapWidgetContentData)
        checkBannerTimerWidgetRemindMeStatus(mapWidgetContentData.values.toList())
    }

    private fun checkBannerTimerWidgetRemindMeStatus(listWidgetContentData: List<Visitable<*>?>) {
        if (isLogin) {
            listWidgetContentData
                .filterIsInstance<ShopWidgetDisplayBannerTimerUiModel>()
                .firstOrNull().let { bannerTimerWidget ->
                    if(bannerTimerWidget?.data?.status == StatusCampaign.UPCOMING) {
                        viewModel?.getBannerTimerRemindMeStatus(bannerTimerWidget.getCampaignId())
                    }
                }
        }
    }

    override fun getWidgetTextColor(): Int {
        return parseColor(textColor)
    }

    private fun renderCampaignTabBackgroundColor() {
        viewBackgroundColor?.apply {
            if (listCampaignTabBackgroundColor.isNotEmpty()) {
                show()
                setBackgroundColor(
                    parseColor(
                        listCampaignTabBackgroundColor.firstOrNull().orEmpty()
                    )
                )
            } else {
                hide()
            }
        }
    }

    private fun parseColor(colorHex: String): Int {
        return try {
            Color.parseColor(colorHex)
        } catch (e: Throwable) {
            0
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, AdapterTypeFactory> {
        return ShopCampaignTabAdapter(shopCampaignTabAdapterTypeFactory)
    }

    override fun getAdapterTypeFactory(): AdapterTypeFactory {
        return shopCampaignTabAdapterTypeFactory
    }

    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        activity?.run {
            DaggerShopPageHomeComponent
                .builder()
                .shopPageHomeModule(ShopPageHomeModule())
                .shopComponent(ShopComponentHelper().getComponent(application, this))
                .build()
                .inject(this@ShopPageCampaignFragment)
        }
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object :
            DataEndlessScrollListener(staggeredGridLayoutManager, shopCampaignTabAdapter) {

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)
                val layoutManager =
                    (getRecyclerView(view)?.layoutManager as? StaggeredGridLayoutManager)
                val firstCompletelyVisibleItemPosition =
                    layoutManager?.findFirstCompletelyVisibleItemPositions(null)?.getOrNull(0)
                        .orZero()
                if (firstCompletelyVisibleItemPosition == 0 && isClickToScrollToTop) {
                    isClickToScrollToTop = false
                    if(ShopUtil.isEnableShopPageReImagined()){
                        (getRealParentFragment() as? ShopPageHeaderFragmentV2)?.expandHeader()
                    } else {
                        (getRealParentFragment() as? ShopPageHeaderFragment)?.expandHeader()
                    }
                }
                if (firstCompletelyVisibleItemPosition != latestCompletelyVisibleItemIndex) {
                    hideScrollToTopButton()
                }
                latestCompletelyVisibleItemIndex = firstCompletelyVisibleItemPosition
                val lastCompletelyVisibleItemPosition =
                    layoutManager?.findLastCompletelyVisibleItemPositions(
                        null
                    )?.getOrNull(0).orZero()
                val firstVisibleItemPosition = layoutManager?.findFirstVisibleItemPositions(
                    null
                )?.getOrNull(0).orZero()
                checkLoadNextShopCampaignWidgetContentData(
                    lastCompletelyVisibleItemPosition,
                    firstVisibleItemPosition
                )
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, state: Int) {
                super.onScrollStateChanged(recyclerView, state)
                if (state == SCROLL_STATE_IDLE) {
                    val firstCompletelyVisibleItemPosition =
                        (getRecyclerView(view)?.layoutManager as? StaggeredGridLayoutManager)?.findFirstCompletelyVisibleItemPositions(
                            null
                        )?.getOrNull(0).orZero()
                    if (firstCompletelyVisibleItemPosition > 0) {
                        showScrollToTopButton()
                    }
                }
            }

            override fun onLoadMore(page: Int, totalItemsCount: Int) {}
        }
    }

    private fun checkLoadNextShopCampaignWidgetContentData(
        lastVisibleItemPosition: Int,
        firstVisibleItemPosition: Int
    ) {
        val shouldLoadLastVisibleItem =
            shopCampaignTabAdapter.isLoadNextCampaignWidgetData(lastVisibleItemPosition)
        val shouldLoadFirstVisibleItem =
            shopCampaignTabAdapter.isLoadNextCampaignWidgetData(firstVisibleItemPosition)
        if (shouldLoadLastVisibleItem || shouldLoadFirstVisibleItem) {
            val position = if (shouldLoadLastVisibleItem) {
                lastVisibleItemPosition
            } else {
                firstVisibleItemPosition
            }
            val listWidgetLayoutToLoad = getListWidgetLayoutToLoad(position)
            shopCampaignTabAdapter.updateShopCampaignWidgetStateToLoading(listWidgetLayoutToLoad)
            checkLoadVoucherSliderData(listWidgetLayoutToLoad)
            checkLoadPlayWidgetData(listWidgetLayoutToLoad)
            getWidgetContentData(listWidgetLayoutToLoad)

            listWidgetLayoutToLoad.clear()
        }
    }

    private fun checkLoadPlayWidgetData(listWidgetLayoutToLoad: MutableList<ShopPageWidgetUiModel>) {
        listWidgetLayoutToLoad.firstOrNull {
            isWidgetPlay(it)
        }?.let {
            listWidgetLayoutToLoad.remove(it)
            getPlayWidgetData()
        }
    }

    override fun getPlayWidgetData() {
        shopCampaignTabAdapter.getPlayWidgetUiModel()?.let {
            val playWidgetType = ShopPageWidgetMapper.mapToPlayWidgetTypeExclusiveLaunch(
                shopId,
                it.getCampaignId()
            )
            viewModel?.getPlayWidget(it, playWidgetType)
        }
    }

    private fun checkLoadVoucherSliderData(listWidgetLayoutToLoad: MutableList<ShopPageWidgetUiModel>) {
        listWidgetLayoutToLoad.firstOrNull {
            isWidgetVoucherSlider(it)
        }?.let {
            listWidgetLayoutToLoad.remove(it)
            getVoucherSliderData()
        }
    }

    private fun getVoucherSliderData() {
        shopCampaignTabAdapter.getVoucherSliderUiModel()?.let {
            viewModelCampaign?.getVoucherSliderData(it)
        }
    }

    private fun isWidgetVoucherSlider(uiModel: ShopPageWidgetUiModel): Boolean {
        return uiModel.widgetType == WidgetType.VOUCHER_SLIDER && uiModel.widgetName == WidgetName.VOUCHER
    }

    private fun getWidgetContentData(listWidgetLayoutToLoad: MutableList<ShopPageWidgetUiModel>) {
        if (listWidgetLayoutToLoad.isNotEmpty()) {
            val widgetUserAddressLocalData = ShopUtil.getShopPageWidgetUserAddressLocalData(context)
                ?: LocalCacheModel()
            viewModelCampaign?.getWidgetContentData(
                listWidgetLayoutToLoad.toList(),
                shopId,
                widgetUserAddressLocalData,
            )
        }
    }

    private fun getListWidgetLayoutToLoad(lastCompletelyVisibleItemPosition: Int): MutableList<ShopPageWidgetUiModel> {
        return if (listWidgetLayout.isNotEmpty()) {
            if (shopCampaignTabAdapter.isLoadFirstWidgetContentData()) {
                listWidgetLayout.subList(
                    LIST_WIDGET_LAYOUT_START_INDEX,
                    ShopUtil.getActualPositionFromIndex(lastCompletelyVisibleItemPosition)
                )
            } else {
                val toIndex = LOAD_WIDGET_ITEM_PER_PAGE.takeIf { it <= listWidgetLayout.size }
                    ?: listWidgetLayout.size
                listWidgetLayout.subList(LIST_WIDGET_LAYOUT_START_INDEX, toIndex)
            }
        } else {
            mutableListOf()
        }
    }

    override fun loadData(page: Int) {}

    override fun scrollToTop() {
        isClickToScrollToTop = true
        getRecyclerView(view)?.scrollToPosition(0)
    }

    override fun isShowScrollToTopButton(): Boolean {
        return latestCompletelyVisibleItemIndex > 0
    }

    private fun hideScrollToTopButton() {
        if(ShopUtil.isEnableShopPageReImagined()){
            (getRealParentFragment() as? ShopPageHeaderFragmentV2)?.hideScrollToTopButton()
        } else {
            (getRealParentFragment() as? ShopPageHeaderFragment)?.hideScrollToTopButton()
        }
    }

    private fun showScrollToTopButton() {
        if(ShopUtil.isEnableShopPageReImagined()){
            (getRealParentFragment() as? ShopPageHeaderFragmentV2)?.showScrollToTopButton()
        } else {
            (getRealParentFragment() as? ShopPageHeaderFragment)?.showScrollToTopButton()
        }
    }

    fun setCampaignTabListBackgroundColor(listBackgroundColor: List<String>) {
        this.listCampaignTabBackgroundColor = listBackgroundColor
    }

    override fun isCampaignTabDarkMode(): Boolean {
        return isDarkTheme
    }

    override fun resumeSliderBannerAutoScroll() {
        shopCampaignTabAdapter.resumeSliderBannerAutoScroll()
    }

    override fun pauseSliderBannerAutoScroll() {
        shopCampaignTabAdapter.pauseSliderBannerAutoScroll()
    }

    override fun onProductImageClicked(
        widgetUiModel: ShopWidgetDisplaySliderBannerHighlightUiModel,
        productData: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData,
        position: Int
    ) {
        sendClickProductSliderBannerHighlight(widgetUiModel, productData, position)
        RouteManager.route(context, productData.appLink)
    }

    private fun sendClickProductSliderBannerHighlight(
        widgetUiModel: ShopWidgetDisplaySliderBannerHighlightUiModel,
        productData: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData,
        position: Int
    ) {
        shopCampaignTabTracker.sendClickProductSliderBannerHighlight(
            ShopPageCampaignTrackingMapper.mapToProductSliderBannerHighlightTrackerDataModel(
                widgetUiModel.widgetId,
                getSelectedTabName(),
                position,
                productData.imageUrl,
                productData.appLink,
                shopId,
                userId
            )
        )
    }

    override fun onProductImageImpression(
        widgetUiModel: ShopWidgetDisplaySliderBannerHighlightUiModel,
        productData: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData,
        position: Int
    ) {
        sendImpressionProductSliderBannerHighlight(widgetUiModel, productData, position)
    }

    private fun sendImpressionProductSliderBannerHighlight(
        widgetUiModel: ShopWidgetDisplaySliderBannerHighlightUiModel,
        productData: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData,
        position: Int
    ) {
        shopCampaignTabTracker.sendImpressionProductSliderBannerHighlight(
            ShopPageCampaignTrackingMapper.mapToProductSliderBannerHighlightTrackerDataModel(
                widgetUiModel.widgetId,
                getSelectedTabName(),
                position,
                productData.imageUrl,
                productData.appLink,
                shopId,
                userId
            )
        )
    }

    override fun onWidgetSliderBannerHighlightImpression(
        uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel,
        bindingAdapterPosition: Int
    ) {
    }

    override fun onWidgetSliderBannerHighlightCtaClicked(
        widgetUiModel: ShopWidgetDisplaySliderBannerHighlightUiModel
    ) {
        sendClickCtaSliderBannerHighlight(widgetUiModel)
        checkShouldSelectHomeTab(widgetUiModel.header.ctaLink)
    }

    private fun sendClickCtaSliderBannerHighlight(widgetUiModel: ShopWidgetDisplaySliderBannerHighlightUiModel) {
        shopCampaignTabTracker.sendClickCtaSliderBannerHighlight(
            ShopPageCampaignTrackingMapper.mapToClickCtaSliderBannerHighlightTrackerDataModel(
                widgetUiModel.widgetId,
                shopId,
                userId
            )
        )
    }

    private fun checkShouldSelectHomeTab(appLink: String) {
        val tabValue = Uri.parse(appLink).getQueryParameter(QUERY_PARAM_TAB).orEmpty()
        if (tabValue == ShopPageHeaderTabName.HOME) {
            if(ShopUtil.isEnableShopPageReImagined()){
                (getRealParentFragment() as? ShopPageHeaderFragmentV2)?.selectShopTab(ShopPageHeaderTabName.HOME)
            } else {
                (getRealParentFragment() as? ShopPageHeaderFragment)?.selectShopTab(ShopPageHeaderTabName.HOME)
            }
        }
    }

    override fun onCampaignCarouselProductItemClicked(
        parentPosition: Int,
        itemPosition: Int,
        carouselProductWidgetUiModel: ShopCampaignWidgetCarouselProductUiModel?,
        productUiModel: ShopHomeProductUiModel?
    ) {
        sendClickProductHighlightCarouselProductItemTracker(
            itemPosition,
            carouselProductWidgetUiModel,
            productUiModel
        )
        goToPDP(productUiModel?.productUrl.orEmpty())
    }

    private fun sendClickProductHighlightCarouselProductItemTracker(
        itemPosition: Int,
        carouselProductWidgetUiModel: ShopCampaignWidgetCarouselProductUiModel?,
        productUiModel: ShopHomeProductUiModel?
    ) {
        shopCampaignTabTracker.clickProductHighlightCarouselProductItem(
            ShopPageCampaignTrackingMapper.mapToShopHomeCampaignWidgetProductTrackerModel(
                shopId,
                userId,
                carouselProductWidgetUiModel?.getCampaignId().orEmpty(),
                carouselProductWidgetUiModel?.widgetId.orEmpty(),
                getSelectedTabName(),
                itemPosition,
                productUiModel?.id.orEmpty(),
                productUiModel?.name.orEmpty(),
                productUiModel?.displayedPrice.orEmpty()
            )
        )
    }

    override fun onCampaignCarouselProductItemImpression(
        parentPosition: Int,
        itemPosition: Int,
        carouselProductWidgetUiModel: ShopCampaignWidgetCarouselProductUiModel?,
        productUiModel: ShopHomeProductUiModel?
    ) {
        sendImpressionProductHighlightCarouselProductItemTracker(
            itemPosition,
            carouselProductWidgetUiModel,
            productUiModel
        )
    }

    private fun sendImpressionProductHighlightCarouselProductItemTracker(
        itemPosition: Int,
        carouselProductWidgetUiModel: ShopCampaignWidgetCarouselProductUiModel?,
        productUiModel: ShopHomeProductUiModel?
    ) {
        shopCampaignTabTracker.impressionProductHighlightCarouselProductItem(
            ShopPageCampaignTrackingMapper.mapToShopHomeCampaignWidgetProductTrackerModel(
                shopId,
                userId,
                carouselProductWidgetUiModel?.getCampaignId().orEmpty(),
                carouselProductWidgetUiModel?.widgetId.orEmpty(),
                getSelectedTabName(),
                itemPosition,
                productUiModel?.id.orEmpty(),
                productUiModel?.name.orEmpty(),
                productUiModel?.displayedPrice.orEmpty()
            )
        )
    }

    override fun onCtaClicked(carouselProductWidgetUiModel: ShopCampaignWidgetCarouselProductUiModel?) {
        sendClickCtaHeaderTitle(carouselProductWidgetUiModel?.widgetId.orEmpty())
        context?.let {
            RouteManager.route(it, carouselProductWidgetUiModel?.header?.ctaLink)
        }
    }

    private fun sendClickCtaHeaderTitle(widgetId: String) {
        shopCampaignTabTracker.clickCtaHeaderTitle(
            ShopPageCampaignTrackingMapper.mapToShopCampaignWidgetHeaderTitleTrackerDataModel(
                widgetId, shopId, userId
            )
        )
    }

    override fun onCampaignCarouselProductWidgetImpression(
        position: Int,
        carouselProductWidgetUiModel: ShopCampaignWidgetCarouselProductUiModel
    ) {
        sendImpressionWidgetHeaderTitle(
            carouselProductWidgetUiModel.header,
            carouselProductWidgetUiModel.widgetId
        )
    }

    private fun sendImpressionWidgetHeaderTitle(
        header: BaseShopHomeWidgetUiModel.Header,
        widgetId: String
    ) {
        if(header.title.isNotEmpty()){
            shopCampaignTabTracker.impressionWidgetHeaderTitle(
                ShopPageCampaignTrackingMapper.mapToShopCampaignWidgetHeaderTitleTrackerDataModel(
                    widgetId, shopId, userId
                )
            )
        }
    }

    override fun onCampaignVoucherSliderItemImpression(
        model: ExclusiveLaunchVoucher,
        position: Int
    ) {
    }

    override fun onCampaignVoucherSliderItemClick(
        parentUiModel: ShopWidgetVoucherSliderUiModel,
        model: ExclusiveLaunchVoucher,
        position: Int
    ) {
        sendClickVoucherSliderItemTracker(parentUiModel)
        showVoucherDetailBottomSheet(
            model.slug,
            model.couponCode,
            "",
            parentUiModel.widgetId
        )
    }

    private fun sendClickVoucherSliderItemTracker(
        parentUiModel: ShopWidgetVoucherSliderUiModel
    ) {
        shopCampaignTabTracker.clickVoucherSliderItem(
            ShopPageCampaignTrackingMapper.mapToClickVoucherSliderItemTrackerDataModel(
                parentUiModel.widgetId,
                shopId,
                userId
            )
        )
    }

    override fun onCampaignVoucherSliderItemCtaClaimClick(
        parentUiModel: ShopWidgetVoucherSliderUiModel,
        model: ExclusiveLaunchVoucher,
        position: Int
    ) {
        if (isLogin) {
            if(viewModelCampaign?.isLoadingRedeemVoucher() == false) {
                sendClickCtaVoucherSliderItemTracker(parentUiModel, model)
                redeemCampaignVoucherSlider(parentUiModel, model)
            }
        } else {
            redirectToLoginPage()
        }
    }

    private fun sendClickCtaVoucherSliderItemTracker(
        parentUiModel: ShopWidgetVoucherSliderUiModel,
        model: ExclusiveLaunchVoucher
    ) {
        shopCampaignTabTracker.clickCtaVoucherSliderItem(
            ShopPageCampaignTrackingMapper.mapToClickCtaVoucherSliderItemTrackerDataModel(
                model.buttonStr,
                parentUiModel.widgetId,
                shopId,
                userId
            )
        )
    }

    private fun redeemCampaignVoucherSlider(
        parentUiModel: ShopWidgetVoucherSliderUiModel,
        model: ExclusiveLaunchVoucher
    ) {
        viewModelCampaign?.redeemCampaignVoucherSlider(parentUiModel, model)
    }

    override fun onCampaignVoucherSliderMoreItemClick(
        listCategorySlug: List<String>,
        parentUiModel: ShopWidgetVoucherSliderUiModel?
    ) {
        sendClickSeeMoreVoucherTracker(parentUiModel)
        showVoucherListBottomSheet(listCategorySlug)
    }

    private fun sendClickSeeMoreVoucherTracker(parentUiModel: ShopWidgetVoucherSliderUiModel?) {
        shopCampaignTabTracker.clickSeeMoreVoucherSlider(
            ShopPageCampaignTrackingMapper.mapToClickSeeMoreVoucherSliderTrackerDataModel(
                parentUiModel?.widgetId.orEmpty(),
                shopId,
                userId
            )
        )
    }

    private fun showVoucherListBottomSheet(listCategorySlug: List<String>) {
        val bottomSheet = ExclusiveLaunchVoucherListBottomSheet.newInstance(
            shopId = shopId,
            useDarkBackground = isCampaignTabDarkMode(),
            voucherSlugs = listCategorySlug,
            campaignId = "", //TODO: replace with real id
            widgetId = "" //TODO: replace with real id
        )
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun showVoucherDetailBottomSheet(
        slug: String,
        couponCode: String,
        campaignId: String,
        widgetId: String
    ) {
        if (!isAdded) return

        val bottomSheet = VoucherDetailBottomSheet.newInstance(
            shopId = shopId,
            voucherSlug = slug,
            promoVoucherCode = couponCode,
            campaignId = campaignId,
            widgetId = widgetId
        ).apply {
            setOnVoucherRedeemSuccess { redeemResult ->
                handleRedeemVoucherSuccess(ShopPageCampaignMapper.mapToShopCampaignRedeemPromoVoucherResult(
                    slug,
                    couponCode,
                    campaignId,
                    widgetId,
                    redeemResult
                ))
            }
            setOnVoucherUseSuccess { showUseVoucherSuccess() }
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun showUseVoucherSuccess() {
        val toasterMessage = context?.getString(R.string.shop_page_use_voucher_success).orEmpty()
        showToaster(
            message = toasterMessage ,
            view = view ?: return,
            ctaText = "",
            onCtaClicked = {}
        )
    }
    private fun handleRedeemVoucherSuccess(shopCampaignRedeemPromo: ShopCampaignRedeemPromoVoucherResult) {
        if (shopCampaignRedeemPromo.redeemResult.redeemMessage.isNotEmpty()) {
            val toasterMessage = context?.getString(R.string.shop_campaign_tab_voucher_redeem_success_message).orEmpty()
            val toasterCta = context?.getString(R.string.see_label).orEmpty()
            showToaster(
                toasterMessage,
                view ?: return,
                toasterCta,
                {
                    showVoucherDetailBottomSheet(
                        shopCampaignRedeemPromo.slug,
                        shopCampaignRedeemPromo.couponCode,
                        shopCampaignRedeemPromo.campaignId,
                        shopCampaignRedeemPromo.widgetId
                    )
                }
            )
            getVoucherSliderData()
        }
    }

    override fun onSuccessGetYouTubeData(widgetId: String, data: YoutubeVideoDetailModel) {
        shopCampaignTabAdapter.setHomeYouTubeData(widgetId, data)
    }

    override fun onFailedGetYouTubeData(widgetId: String, throwable: Throwable) {
        ShopPageExceptionHandler.logExceptionToCrashlytics(
            ShopPageExceptionHandler.ERROR_WHEN_GET_YOUTUBE_DATA,
            throwable
        )
        shopCampaignTabAdapter.setHomeYouTubeData(widgetId, YoutubeVideoDetailModel())
    }

    override fun onDisplayBannerTimerClicked(
        position: Int,
        uiModel: ShopWidgetDisplayBannerTimerUiModel
    ) {
        RouteManager.route(context, uiModel.header.ctaLink)
    }

    override fun onImpressionDisplayBannerTimerWidget(
        position: Int,
        uiModel: ShopWidgetDisplayBannerTimerUiModel
    ) {
        sendImpressionShopHomeBannerTimerCampaignTabTracker(uiModel)
    }

    private fun sendImpressionShopHomeBannerTimerCampaignTabTracker(
        uiModel: ShopWidgetDisplayBannerTimerUiModel
    ) {
        shopCampaignTabTracker.sendImpressionShopBannerTimerCampaignTracker(
            ShopPageCampaignTrackingMapper.mapToShopCampaignBannerTimerTrackerDataModel(
                uiModel.getCampaignId(),
                uiModel.widgetId,
                shopId,
                userId
            )
        )
    }

    override fun onTimerFinished(uiModel: ShopWidgetDisplayBannerTimerUiModel) {
        shopCampaignTabAdapter.removeWidget(uiModel)
        endlessRecyclerViewScrollListener.resetState()
        if(getSelectedFragment() == this){
            refreshShopHeader()
        }
    }

    private fun getLatestShopCampaignWidgetLayoutData() {
        globalErrorShopPage?.hide()
        shopCampaignTabAdapter.showLoading()
        scrollToTop()
        viewModelCampaign?.getLatestShopCampaignWidgetLayoutData(
            shopId,
            extParam,
            ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel(),
            "CampaignTab"
        )
    }

    override fun onClickRemindMe(position: Int, uiModel: ShopWidgetDisplayBannerTimerUiModel) {
        viewModel?.let {
            if (it.isLogin) {
                viewModelCampaign?.showBannerTimerRemindMeLoading(
                    shopCampaignTabAdapter.getNewVisitableItems()
                )
                handleBannerTimerClickRemindMe(uiModel)
                sendClickRemindMeShopCampaignBannerTimerTracker(uiModel)
            } else {
                redirectToLoginPage()
            }
        }
    }

    private fun sendClickRemindMeShopCampaignBannerTimerTracker(
        uiModel: ShopWidgetDisplayBannerTimerUiModel
    ) {
        shopCampaignTabTracker.sendClickRemindMeShopCampaignBannerTimerTracker(
            ShopPageCampaignTrackingMapper.mapToShopCampaignBannerTimerTrackerDataModel(
                uiModel.getCampaignId(),
                uiModel.widgetId,
                shopId,
                userId
            )
        )
    }

    override fun onSuccessCheckBannerTimerNotifyMe(data: CheckCampaignNotifyMeUiModel) {
        val isRegisterCampaign = data.action.equals(
            NotifyMeAction.REGISTER.action,
            ignoreCase = true
        )
        viewModelCampaign?.toggleBannerTimerRemindMe(
            shopCampaignTabAdapter.getNewVisitableItems(),
            isRegisterCampaign,
            true
        )
        view?.let {
            Toaster.build(
                it,
                data.message,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(R.string.shop_string_ok)
            ).show()
        }
    }

    override fun onFailCheckBannerTimerNotifyMe(errorMessage: String) {
        view?.let {
            Toaster.build(
                it,
                errorMessage,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(R.string.shop_string_ok)
            ).show()
        }
        viewModelCampaign?.toggleBannerTimerRemindMe(
            shopCampaignTabAdapter.getNewVisitableItems(),
            isRemindMe = false,
            isClickRemindMe = false
        )
    }

    override fun onSuccessGetBannerTimerRemindMeStatusData(data: GetCampaignNotifyMeUiModel) {
        viewModelCampaign?.toggleBannerTimerRemindMe(
            shopCampaignTabAdapter.getNewVisitableItems(),
            isRemindMe = data.isAvailable,
            isClickRemindMe = false
        )
    }

    override fun onDisplayWidgetImpression(model: ShopHomeDisplayWidgetUiModel, position: Int) {
        sendImpressionWidgetHeaderTitle(
            model.header,
            model.widgetId
        )
        sendImpressionWidgetDisplayTracker(
            model,
            position
        )
    }

    private fun sendImpressionWidgetDisplayTracker(model: ShopHomeDisplayWidgetUiModel, position: Int) {
        shopCampaignTabTracker.impressionWidgetDisplay(
            ShopPageCampaignTrackingMapper.mapToShopCampaignWidgetDisplayTrackerDataModel(
                model.widgetId,
                model.name,
                position,
                shopId,
                userId,
                model.header.title
            )
        )
    }

    override fun onDisplayItemClicked(
        displayWidgetUiModel: ShopHomeDisplayWidgetUiModel?,
        displayWidgetItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem,
        parentPosition: Int,
        adapterPosition: Int
    ) {
        sendClickWidgetDisplayTracker(
            displayWidgetUiModel,
            parentPosition
        )
        context?.let {
            if (displayWidgetItem.appLink.isNotEmpty()) {
                RouteManager.route(it, displayWidgetItem.appLink)
            }
        }
    }

    private fun sendClickWidgetDisplayTracker(
        displayWidgetUiModel: ShopHomeDisplayWidgetUiModel?,
        parentPosition: Int
    ) {
        displayWidgetUiModel?.let {
            shopCampaignTabTracker.clickWidgetDisplay(
                ShopPageCampaignTrackingMapper.mapToShopCampaignWidgetDisplayTrackerDataModel(
                    it.widgetId,
                    it.name,
                    parentPosition,
                    shopId,
                    userId,
                    it.header.title
                )
            )
        }
    }

    fun setIsDarkTheme(isDarkTheme: Boolean) {
        this.isDarkTheme = isDarkTheme
    }

    override fun getListBackgroundColor(): List<String> {
        return listCampaignTabBackgroundColor
    }

    fun setListPatternImage(listPatternImage: List<String>) {
        this.listPatternImage = listPatternImage
    }

    override fun onPlayWidgetImpression(model: CarouselPlayWidgetUiModel, position: Int) {
    }

    override fun onImpressionVoucherSliderWidget(
        model: ShopWidgetVoucherSliderUiModel,
        position: Int
    ) {
        sendImpressionVoucherSliderWidget(model)
    }

    private fun sendImpressionVoucherSliderWidget(model: ShopWidgetVoucherSliderUiModel) {
        shopCampaignTabTracker.sendImpressionShopVoucherSliderCampaignTracker(
            ShopPageCampaignTrackingMapper.mapToShopCampaignVoucherSliderTrackerDataModel(
                model.widgetId,
                shopId,
                userId
            )
        )
    }

    override fun onImpressionPlayWidget(widgetModel: CarouselPlayWidgetUiModel, position: Int) {
        sendImpressionWidgetHeaderTitle(
            widgetModel.header,
            widgetModel.widgetId
        )
    }
    override fun onPlayWidgetItemImpression(
        widgetModel: CarouselPlayWidgetUiModel,
        channelModel: PlayWidgetChannelUiModel,
        position: Int
    ) {
        sendPlayWidgetItemImpression(widgetModel, channelModel, position)
    }

    private fun sendPlayWidgetItemImpression(
        widgetModel: CarouselPlayWidgetUiModel,
        channelModel: PlayWidgetChannelUiModel,
        position: Int
    ) {
        shopCampaignTabTracker.impressionPlayWidgetItem(
            ShopPageCampaignTrackingMapper.mapToShopCampaignPlayWidgetItemTrackerDataModel(
                widgetModel.getCampaignId(),
                widgetModel.widgetId,
                channelModel.channelId,
                getSelectedTabName(),
                position,
                shopId,
                userId
            )
        )
    }

    override fun onPlayWidgetItemClick(
        widgetModel: CarouselPlayWidgetUiModel,
        channelModel: PlayWidgetChannelUiModel,
        position: Int
    ) {
        sendPlayWidgetItemClick(widgetModel, channelModel, position)
    }

    private fun sendPlayWidgetItemClick(
        widgetModel: CarouselPlayWidgetUiModel,
        channelModel: PlayWidgetChannelUiModel,
        position: Int
    ) {
        shopCampaignTabTracker.clickPlayWidgetItem(
            ShopPageCampaignTrackingMapper.mapToShopCampaignPlayWidgetItemTrackerDataModel(
                widgetModel.getCampaignId(),
                widgetModel.widgetId,
                channelModel.channelId,
                getSelectedTabName(),
                position,
                shopId,
                userId
            )
        )
    }

    private fun getRealParentFragment(): Fragment? {
        return if (ShopUtil.isEnableShopPageReImagined()) {
            parentFragment?.parentFragment
        } else {
            parentFragment
        }
    }
}
