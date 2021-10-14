package com.tokopedia.recommendation_widget_common.widget.carousel

import android.content.Context
import android.graphics.Rect
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecomItemTrackingMetadata
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.presenter.RecommendationViewModel
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.TEXT_OTHER_RECOM
import com.tokopedia.recommendation_widget_common.viewutil.doSuccessOrFail
import com.tokopedia.recommendation_widget_common.viewutil.getActivityFromContext
import com.tokopedia.recommendation_widget_common.viewutil.initRecomWidgetViewModel
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_FAILED
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_LOADING
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_READY
import com.tokopedia.recommendation_widget_common.widget.header.RecommendationHeaderListener
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.CommonRecomCarouselCardTypeFactory
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.CommonRecomCarouselCardTypeFactoryImpl
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model.RecomCarouselBannerDataModel
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model.RecomCarouselSeeMoreDataModel
import com.tokopedia.recommendation_widget_common.widget.productcard.common.RecomCommonProductCardListener
import com.tokopedia.recommendation_widget_common.widget.tokonowutil.TokonowQuantityUpdater
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.layout_widget_recommendation_carousel.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by yfsx on 5/3/21.
 */

class RecommendationCarouselWidgetView : FrameLayout, RecomCommonProductCardListener, CoroutineScope, LifecycleObserver {

    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.Main

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var itemView: View
    private val itemContext: Context
    private var widgetListener: RecommendationCarouselWidgetListener? = null
    private var widgetBindPageNameListener: RecommendationCarouselWidgetBindPageNameListener? = null
    private var scrollToPosition: Int = 0
    private var carouselData: RecommendationCarouselData? = null
    private lateinit var typeFactory: CommonRecomCarouselCardTypeFactory
    private lateinit var recyclerView: RecyclerView
    private var adapter: RecommendationCarouselAdapter? = null
    private lateinit var layoutManager: LinearLayoutManager
    private var scrollListener: ((Parcelable?) -> Unit)? = null
    private var pageName: String = ""
    private var adapterPosition: Int = 0
    private var isInitialized = false
    private var isForceRefresh = false
    private var isRecomBindWithPageName = false
    private var userSession: UserSessionInterface? = null
    private lateinit var recomItemTrackingMetadata: RecomItemTrackingMetadata

    private var lifecycleOwner: LifecycleOwner? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: RecommendationViewModel by initRecomWidgetViewModel {
        context.getActivityFromContext()
    }


    init {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.layout_widget_recommendation_carousel, this)
        recyclerView = view.findViewById(R.id.rv_product)
        this.itemView = view
        this.itemContext = view.context
        this.userSession = UserSession(itemContext)
    }

    companion object {
        const val NAME_CAMPAIGN_WIDGET = "Campaign-Widget"
    }

    /*
    * bind recom widget with data called from fragment
    *
     */
    fun bindRecomWithData(
        carouselData: RecommendationCarouselData = RecommendationCarouselData(),
        adapterPosition: Int = 0,
        widgetListener: RecommendationCarouselWidgetListener?,
        scrollToPosition: Int = 0
    ) {
        try {
            this.adapterPosition = adapterPosition
            this.widgetListener = widgetListener
            this.scrollToPosition = scrollToPosition
            if (!carouselData.recommendationData.recommendationItemList.isEmpty()) {
                bindWidgetWithData(carouselData)
            } else this.widgetListener?.onWidgetFail(
                carouselData.recommendationData.pageName,
                MessageErrorException("")
            )
        } catch (e: Exception) {
            this.widgetListener?.onWidgetFail(carouselData.recommendationData.pageName, e)
        }
    }

    /*
    *this function can be used for search recommendation that only provide pageName
    * bind recom widget with some important params
    * - pageName (mandatory)
    * - isTokonow (mandatory)
    * - categoryIds (optional, if want to load recom category)
    * - keyword (optional, if want to load recom from keyword search)
     */
    fun bindRecomWithPageName(
        adapterPosition: Int = 0,
        widgetBindPageNameListener: RecommendationCarouselWidgetBindPageNameListener?,
        scrollToPosition: Int = 0,
        pageName: String,
        tempHeaderName: String = TEXT_OTHER_RECOM,
        isForceRefresh: Boolean = false,
        categoryIds: List<String> = listOf(),
        keyword: String = "",
        isTokonow: Boolean = false
    ) {
        try {
            this.adapterPosition = adapterPosition
            this.widgetBindPageNameListener = widgetBindPageNameListener
            this.scrollToPosition = scrollToPosition
            this.pageName = pageName
            this.isForceRefresh = isForceRefresh
            recomItemTrackingMetadata = RecomItemTrackingMetadata()
            bindTemporaryHeader(tempHeaderName)
            bindWidgetWithPageName(
                pageName = pageName,
                isForceRefresh = isForceRefresh,
                isTokonow = isTokonow,
                keyword = keyword,
                categoryIds = categoryIds
            )
        } catch (e: Exception) {
            this.widgetBindPageNameListener?.onWidgetFail(pageName, e)
        }
    }


    /*
    * bind recom widget with some important params
    * - pageName
    * - parentProductId
    *
    * this function can be used for PDP recom
     */
    fun bindPdpRecom(
        adapterPosition: Int = 0,
        widgetBindPageNameListener: RecommendationCarouselWidgetBindPageNameListener?,
        scrollToPosition: Int = 0,
        pageName: String,
        tempHeaderName: String = TEXT_OTHER_RECOM,
        isForceRefresh: Boolean = false,
        parentProductId: String,
        isTokonow: Boolean = false
    ) {
        try {
            this.adapterPosition = adapterPosition
            this.widgetBindPageNameListener = widgetBindPageNameListener
            this.scrollToPosition = scrollToPosition
            this.pageName = pageName
            this.isForceRefresh = isForceRefresh

            bindTemporaryHeader(tempHeaderName)
            bindWidgetWithPageName(
                pageName = pageName,
                isForceRefresh = isForceRefresh,
                parentProductId = parentProductId,
                isTokonow = isTokonow
            )
        } catch (e: Exception) {
            this.widgetBindPageNameListener?.onWidgetFail(pageName, e)
        }
    }

    fun bindTemporaryHeader(tempHeaderName: String) {
        itemView.recommendation_header_view.bindData(
            RecommendationWidget(title = tempHeaderName),
            null
        )
        itemView.loadingRecom.visible()
    }

    fun getCurrentPosition(): Int {
        return if (::layoutManager.isInitialized)
            layoutManager.findFirstCompletelyVisibleItemPosition()
        else 0
    }

    fun restoreScrollState(scrollState: Parcelable?) {
        if (!::layoutManager.isInitialized) return

        itemView.post {
            layoutManager.onRestoreInstanceState(scrollState)
        }
    }

    fun setScrollListener(scrollListener: ((Parcelable?) -> Unit)?) {
        this.scrollListener = scrollListener
    }

    override fun onProductCardImpressed(
        data: RecommendationWidget,
        recomItem: RecommendationItem,
        position: Int
    ) {
        carouselData?.let {
            if (isRecomBindWithPageName) {
                widgetBindPageNameListener?.onRecomProductCardImpressed(
                    data = it,
                    recomItem = recomItem,
                    itemPosition = position,
                    adapterPosition = adapterPosition
                )
            } else {
                widgetListener?.onRecomProductCardImpressed(
                    data = it,
                    recomItem = recomItem,
                    itemPosition = position,
                    adapterPosition = adapterPosition
                )
            }
        }
    }

    override fun onProductCardClicked(
        data: RecommendationWidget,
        recomItem: RecommendationItem,
        position: Int,
        applink: String
    ) {
        carouselData?.let {
            if (isRecomBindWithPageName) {
                widgetBindPageNameListener?.onRecomProductCardClicked(
                    data = it,
                    recomItem = recomItem,
                    applink = applink,
                    itemPosition = position,
                    adapterPosition = adapterPosition
                )
            } else {
                widgetListener?.onRecomProductCardClicked(
                    data = it,
                    recomItem = recomItem,
                    applink = applink,
                    itemPosition = position,
                    adapterPosition = adapterPosition
                )
            }
        }
    }

    override fun onRecomProductCardAddToCartNonVariant(data: RecommendationWidget, recomItem: RecommendationItem, adapterPosition: Int, quantity: Int) {
        carouselData?.let {
            if (isRecomBindWithPageName) {
                viewModel?.onAtcRecomNonVariantQuantityChanged(recomItem, quantity)
            } else
                widgetListener?.onRecomProductCardAddToCartNonVariant(
                    data = it,
                    recomItem = recomItem,
                    adapterPosition = adapterPosition,
                    quantity = quantity
                )
        }
    }

    override fun onRecomProductCardAddVariantClick(data: RecommendationWidget, recomItem: RecommendationItem, adapterPosition: Int) {
        carouselData?.let {
            if (isRecomBindWithPageName) {
                widgetBindPageNameListener?.onRecomProductCardAddVariantClick(
                    data = it,
                    recomItem = recomItem,
                    adapterPosition = adapterPosition
                )
            } else
                widgetListener?.onRecomProductCardAddVariantClick(
                    data = it,
                    recomItem = recomItem,
                    adapterPosition = adapterPosition
                )
        }
    }

    override fun onSeeMoreCardClicked(data: RecommendationWidget, applink: String) {
        carouselData?.let {
            if (isRecomBindWithPageName) {
                widgetBindPageNameListener?.onSeeAllBannerClicked(data = it, applink = applink)
            } else
                widgetListener?.onSeeAllBannerClicked(data = it, applink = applink)
        }
    }

    override fun onBannerCardClicked(data: RecommendationWidget, applink: String) {
        carouselData?.let {
            if (isRecomBindWithPageName) {
                widgetBindPageNameListener?.onRecomBannerClicked(
                    data = it,
                    applink = applink,
                    adapterPosition = adapterPosition
                )
            } else
                widgetListener?.onRecomBannerClicked(
                    data = it,
                    applink = applink,
                    adapterPosition = adapterPosition
                )
        }
    }

    override fun onBannerCardImpressed(data: RecommendationWidget) {
        carouselData?.let {
            if (isRecomBindWithPageName) {
                widgetBindPageNameListener?.onRecomBannerImpressed(
                    data = it,
                    adapterPosition = adapterPosition
                )
            } else
                widgetListener?.onRecomBannerImpressed(data = it, adapterPosition = adapterPosition)
        }
    }

    private fun initVar() {
        if (isInitialized) return
        carouselData?.let {
            typeFactory = CommonRecomCarouselCardTypeFactoryImpl(it.recommendationData)
        }
        adapter = RecommendationCarouselAdapter(typeFactory)
        layoutManager = createLayoutManager()

        recyclerView.addOnScrollListener(createScrollListener())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        isInitialized = true
    }

    private fun createLayoutManager(): LinearLayoutManager {
        return object: LinearLayoutManager(itemView.context, HORIZONTAL, false) {
            override fun requestChildRectangleOnScreen(
                    parent: RecyclerView,
                    child: View,
                    rect: Rect,
                    immediate: Boolean,
                    focusedChildVisible: Boolean
            ): Boolean {
                return if ((child as? ViewGroup)?.focusedChild is CardView) {
                    false
                } else super.requestChildRectangleOnScreen(
                        parent,
                        child,
                        rect,
                        immediate,
                        focusedChildVisible
                )
            }
        }
    }

    private fun impressChannel(carouselData: RecommendationCarouselData) {
        itemView.addOnImpressionListener(carouselData) {
            if (isRecomBindWithPageName) {
                widgetBindPageNameListener?.onRecomBannerImpressed(carouselData, adapterPosition)
            } else {
                widgetListener?.onRecomBannerImpressed(carouselData, adapterPosition)
            }
        }
    }

    private fun setData(carouselData: RecommendationCarouselData) {
        val cardList = mutableListOf<Visitable<*>>()
        val layoutName = carouselData.recommendationData.layoutType
        carouselData.recommendationData.recommendationBanner?.let {
            if (it.imageUrl.isNotEmpty() && layoutName == NAME_CAMPAIGN_WIDGET) {
                cardList.add(RecomCarouselBannerDataModel(
                        applink = it.applink,
                        bannerBackgorundColor = it.backgroudColor,
                        bannerImage = it.imageUrl,
                        listener = this))
            }
        }
        val productDataList = carouselData.recommendationData.recommendationItemList.toRecomCarouselItems(listener = this)
        cardList.addAll(productDataList)
        if (cardList.size != 0) {
            if (carouselData.recommendationData.seeMoreAppLink.isNotEmpty()) {
                cardList.add(RecomCarouselSeeMoreDataModel(carouselData.recommendationData.seeMoreAppLink, listener = this))
            }

            adapter?.submitList(cardList)

            launch {
                try {
                    recyclerView.setHeightBasedOnProductCardMaxHeight(productDataList.map { it.productModel })
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }
            }
        } else {
            if (isRecomBindWithPageName) {
                widgetBindPageNameListener?.onChannelWidgetEmpty()
            } else widgetListener?.onChannelWidgetEmpty()
        }
    }

    private suspend fun RecyclerView.setHeightBasedOnProductCardMaxHeight(
            productCardModelList: List<ProductCardModel>) {
        val productCardHeight = getProductCardMaxHeight(productCardModelList)

        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = productCardHeight
        this.layoutParams = carouselLayoutParams
    }

    private suspend fun getProductCardMaxHeight(productCardModelList: List<ProductCardModel>): Int {
        val productCardWidth = itemView.context.resources.getDimensionPixelSize(com.tokopedia.productcard.R.dimen.carousel_product_card_grid_width)
        return productCardModelList.getMaxHeightForGridView(itemView.context, Dispatchers.Default, productCardWidth)
    }


    private fun setHeaderComponent(carouselData: RecommendationCarouselData) {
        itemView.recommendation_header_view.bindData(data = carouselData.recommendationData, listener = object : RecommendationHeaderListener {
            override fun onSeeAllClick(link: String) {
                if (isRecomBindWithPageName) widgetBindPageNameListener?.onSeeAllBannerClicked(
                    carouselData,
                    link
                )
                else widgetListener?.onSeeAllBannerClicked(carouselData, link)
            }

            override fun onChannelExpired(widget: RecommendationWidget) {
                if (isRecomBindWithPageName) widgetBindPageNameListener?.onChannelExpired(
                    carouselData,
                    adapterPosition
                )
                else widgetListener?.onChannelExpired(carouselData, adapterPosition)
            }
        })
    }

    private fun scrollCarousel(scrollToPosition: Int) {
        if (!::layoutManager.isInitialized) return

        itemView.post {
            layoutManager.scrollToPositionWithOffset(
                    scrollToPosition,
                    context.applicationContext.resources.getDimensionPixelOffset(R.dimen.dp_16)
            )
        }
    }

    private fun createScrollListener(): RecyclerView.OnScrollListener {
        return  object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (::layoutManager.isInitialized) {
                    val scrollState = layoutManager.onSaveInstanceState()
                    scrollListener?.invoke(scrollState)
                }
            }
        }
    }

    //get data with network call
    private fun bindWidgetWithPageName(
        pageName: String,
        isForceRefresh: Boolean,
        parentProductId: String = "",
        categoryIds: List<String> = listOf(),
        keyword: String = "",
        isTokonow: Boolean = false
    ) {
        isRecomBindWithPageName = true
        getMiniCartData()
        if (carouselData == null || isForceRefresh) {
            adapter?.clearAllElements()
            itemView.loadingRecom.visible()
            viewModel?.loadRecommendationCarousel(
                pageName = pageName,
                productIds = listOf(parentProductId),
                categoryIds = categoryIds,
                keywords = listOf(keyword),
                isTokonow = isTokonow
            )
        } else {
            itemView.loadingRecom.gone()
        }
    }

    //data alrd from fragment
    private fun bindWidgetWithData(carouselData: RecommendationCarouselData) {
        this.carouselData = carouselData
        initVar()
        doActionBasedOnRecomState(carouselData.state,
            onLoad = {
                itemView.loadingRecom.visible()
            },
            onReady = {
                itemView.loadingRecom.gone()
                impressChannel(carouselData)
                setHeaderComponent(carouselData)
                setData(carouselData)
                scrollCarousel(scrollToPosition)
            },
            onFailed = {
                itemView.loadingRecom.gone()
            }
        )
    }

    private fun doActionBasedOnRecomState(
        state: Int,
        onLoad: () -> Unit?,
        onReady: () -> Unit?,
        onFailed: () -> Unit?
    ) {
        when (state) {
            STATE_LOADING -> {
                onLoad.invoke()
            }
            STATE_READY -> {
                onReady.invoke()
            }
            STATE_FAILED -> {
                onFailed.invoke()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun startEvent(owner: LifecycleOwner) {
        this.lifecycleOwner = owner
        observeLiveData()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onWidgetResume() {
        getMiniCartData()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pauseEvent() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroyEvent() {
        lifecycleOwner?.let {
            viewModel?.getRecommendationLiveData?.removeObservers(it)
            viewModel?.errorGetRecommendation?.removeObservers(it)
            viewModel?.atcRecomTokonow?.removeObservers(it)
            viewModel?.atcRecomTokonowSendTracker?.removeObservers(it)
            viewModel?.atcRecomTokonowResetCard?.removeObservers(it)
            viewModel?.atcRecomTokonowNonLogin?.removeObservers(it)
            viewModel?.refreshMiniCartDataTriggerByPageName?.removeObservers(it)
            viewModel?.deleteCartRecomTokonowSendTracker?.removeObservers(it)
            viewModel?.minicartError?.removeObservers(it)
            viewModel?.miniCartData?.removeObservers(it)
            viewModel?.refreshUIMiniCartData?.removeObservers(it)
        }
    }

    private fun observeLiveData() {
        /*
        * observer with page name validation will only updated on designated widget
        * since live data will broadcast data to all observer available on lifecycle
        *
        * only miniCartData not validated because its expected to update all recom widgets
        * when minicart service called
         */
        lifecycleOwner?.let { owner ->
            viewModel?.getRecommendationLiveData?.observe(owner, Observer {
                this.isForceRefresh = false
                it.doSuccessOrFail({ recom ->
                    if (recom.data.pageName == pageName) {
                        if (recom.data.recommendationItemList.isNotEmpty()) {
                            bindWidgetWithData(
                                RecommendationCarouselData(
                                    recommendationData = recom.data,
                                    state = STATE_READY,
                                    isUsingWidgetViewModel = true
                                )
                            )
                        } else {
                            if (isRecomBindWithPageName) widgetBindPageNameListener?.onChannelWidgetEmpty()
                            else widgetListener?.onChannelWidgetEmpty()
                        }
                    }
                }, { throwable ->
                    if (isRecomBindWithPageName) widgetBindPageNameListener?.onWidgetFail(
                        pageName = pageName,
                        e = throwable
                    )
                    else widgetListener?.onWidgetFail(pageName = pageName, e = throwable)

                })
            })

            viewModel?.errorGetRecommendation?.observe(owner, {
                if (it.pageName == pageName) {
                    if (isRecomBindWithPageName) widgetBindPageNameListener?.onWidgetFail(
                        pageName = pageName,
                        e = it.throwable
                    )
                    else widgetListener?.onWidgetFail(pageName = pageName, e = it.throwable)
                }
            })

            //will be updated to all recom widgets on same page (expected)
            viewModel?.miniCartData?.observe(owner, Observer {
                it?.let { map ->
                    updateUiQuantity(map)
                }
            })
            viewModel?.atcRecomTokonow?.observe(owner, Observer { it ->
                if (it.recomItem.pageName == pageName) {
                    if (it.error == null) widgetBindPageNameListener?.onRecomTokonowAtcSuccess(it.message)
                    else widgetBindPageNameListener?.onRecomTokonowAtcFailed(it.error)
                }
            })
            viewModel?.atcRecomTokonowSendTracker?.observe(owner, Observer { data ->
                data.doSuccessOrFail({
                    widgetBindPageNameListener?.onRecomTokonowAtcNeedToSendTracker(
                        it.data,
                        recomItemTrackingMetadata
                    )
                }, {})
            })
            viewModel?.deleteCartRecomTokonowSendTracker?.observe(owner, Observer { data ->
                data.doSuccessOrFail({
                    widgetBindPageNameListener?.onRecomTokonowDeleteNeedToSendTracker(
                        it.data,
                        recomItemTrackingMetadata
                    )
                }, {})
            })
            viewModel?.atcRecomTokonowResetCard?.observe(owner, Observer { recomItem ->
                if (recomItem.pageName == pageName) {
                    carouselData?.let {
                        TokonowQuantityUpdater.updateCurrentQuantityRecomItem(it, recomItem)
                        setData(it)
                    }
                }
            })
            viewModel?.atcRecomTokonowNonLogin?.observe(owner, Observer { recomItem ->
                widgetBindPageNameListener?.onClickItemNonLoginState()
                if (recomItem.pageName == pageName) {
                    carouselData?.let {
                        TokonowQuantityUpdater.resetFailedRecomTokonowCard(it, recomItem)
                        setData(it)
                    }
                }
            })
            viewModel?.refreshMiniCartDataTriggerByPageName?.observe(owner, Observer {
                if (it == pageName) {
                    getMiniCartData()
                }
            })
            viewModel?.refreshUIMiniCartData?.observe(owner, {
                if (it.pageName == pageName) {
                    widgetBindPageNameListener?.onMiniCartUpdatedFromRecomWidget(it.miniCartSimplifiedData)
                }
            })
            viewModel?.minicartError?.observe(owner, Observer {
//                RecomServerLogger.logWarning(RecomServerLogger.TYPE_ERROR_GET_MINICART, it)
            })
        }
    }

    private fun updateUiQuantity(miniCart: MutableMap<String, MiniCartItem>) {
        carouselData?.let {
            TokonowQuantityUpdater.updateRecomWithMinicartData(it, miniCart)
            setData(it)
        }
    }

    private fun getMiniCartData() {
        userSession?.let {
            if (it.isLoggedIn) {
                val localAddress = ChooseAddressUtils.getLocalizingAddressData(itemContext)
                viewModel?.getMiniCart(localAddress?.shop_id ?: "", pageName)
            }
        }
    }
}