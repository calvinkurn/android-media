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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.globalerror.showUnifyError
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.presenter.RecomWidgetViewModel
import com.tokopedia.recommendation_widget_common.viewutil.doSuccessOrFail
import com.tokopedia.recommendation_widget_common.viewutil.initRecomWidgetViewModel
import com.tokopedia.recommendation_widget_common.viewutil.toDpInt
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_FAILED
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_LOADING
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_READY
import com.tokopedia.recommendation_widget_common.widget.carousel.chips.RecomChipsAdapter
import com.tokopedia.recommendation_widget_common.widget.header.RecommendationHeaderListener
import com.tokopedia.recommendation_widget_common.widget.header.RecommendationHeaderView
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.CommonRecomCarouselCardTypeFactory
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.CommonRecomCarouselCardTypeFactoryImpl
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model.RecomCarouselBannerDataModel
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model.RecomCarouselSeeMoreDataModel
import com.tokopedia.recommendation_widget_common.widget.productcard.common.RecomCommonProductCardListener
import com.tokopedia.recommendation_widget_common.widget.tokonowutil.TokonowQuantityUpdater
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
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
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var itemView: View
    private val itemContext: Context
    private var basicListener: RecomCarouselWidgetBasicListener? = null
    private var tokonowListener: RecommendationCarouselTokonowListener? = null
    private var tokonowPageNameListener: RecommendationCarouselTokonowPageNameListener? = null
    private var basicChipListener: RecomCarouselChipListener? = null
    private var carouselData: RecommendationCarouselData? = null
    private var headerView: RecommendationHeaderView? = null
    private var loadingView: View? = null
    private lateinit var typeFactory: CommonRecomCarouselCardTypeFactory
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvChips: RecyclerView
    private var adapter: RecommendationCarouselAdapter? = null
    private var chipsAdapter: RecomChipsAdapter? = null
    private lateinit var layoutManager: LinearLayoutManager
    private var scrollListener: ((Parcelable?) -> Unit)? = null
    private var userSession: UserSessionInterface? = null

    private var lifecycleOwner: LifecycleOwner? = null
    private var widgetMetadata: RecomWidgetMetadata = RecomWidgetMetadata()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: RecomWidgetViewModel? by initRecomWidgetViewModel()

    init {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.layout_widget_recommendation_carousel, this)
        recyclerView = view.findViewById(R.id.rv_product)
        headerView = view.findViewById(R.id.recommendation_header_view)
        loadingView = view.findViewById(R.id.loadingRecom)
        rvChips = view.findViewById(R.id.rv_chips)
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
    fun bind(
        carouselData: RecommendationCarouselData = RecommendationCarouselData(),
        adapterPosition: Int = 0,
        basicListener: RecomCarouselWidgetBasicListener?,
        tokonowListener: RecommendationCarouselTokonowListener?,
        chipListener: RecomCarouselChipListener? = null,
        scrollToPosition: Int = 0
    ) {
        try {
            widgetMetadata = widgetMetadata.copy(
                adapterPosition = adapterPosition,
                scrollToPosition = scrollToPosition
            )
            this.basicListener = basicListener
            this.tokonowListener = tokonowListener
            this.basicChipListener = chipListener
            if (carouselData.recommendationData.recommendationItemList.isNotEmpty()) {
                bindWidgetWithData(carouselData)
            } else this.basicListener?.onWidgetFail(
                carouselData.recommendationData.pageName,
                MessageErrorException("")
            )
        } catch (e: Exception) {
            this.basicListener?.onWidgetFail(carouselData.recommendationData.pageName, e)
        }
    }

    /*
    *this function can be used for search recommendation that provide pageName
    * bind recom widget with some important params
    * - pageName (mandatory)
    * - isTokonow (mandatory)
    * - categoryIds (optional, if want to load recom category)
    * - keyword (optional, if want to load recom from keyword search)
    * - parentProductId, if call recom from PDP
     */
    fun bind(
        adapterPosition: Int = 0,
        basicListener: RecomCarouselWidgetBasicListener?,
        tokonowPageNameListener: RecommendationCarouselTokonowPageNameListener?,
        scrollToPosition: Int = 0,
        pageName: String,
        tempHeaderName: String = context.getString(R.string.text_other_recom),
        isForceRefresh: Boolean = false,
        categoryIds: List<String> = listOf(),
        keyword: String = "",
        productIds: List<String> = listOf(),
        isTokonow: Boolean = false
    ) {
        try {
            widgetMetadata = widgetMetadata.copy(
                adapterPosition = adapterPosition,
                scrollToPosition = scrollToPosition,
                pageName = pageName,
                isForceRefresh = isForceRefresh,
                isRecomBindWithPageName = true,
                productIds = productIds,
                categoryIds = categoryIds,
                keyword = keyword
            )
            this.basicListener = basicListener
            this.tokonowPageNameListener = tokonowPageNameListener
            bindTemporaryHeader(tempHeaderName)
            bindWidgetWithPageName(
                pageName = pageName,
                isForceRefresh = isForceRefresh,
                isTokonow = isTokonow,
                keyword = keyword,
                categoryIds = categoryIds,
                productIds = productIds
            )
        } catch (e: Exception) {
            this.basicListener?.onWidgetFail(pageName, e)
        }
    }

    fun bindTemporaryHeader(tempHeaderName: String) {
        headerView?.bindData(
            RecommendationWidget(title = tempHeaderName),
            null
        )
        recyclerView.gone()
        loadingView?.visible()
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
            basicListener?.onRecomProductCardImpressed(
                data = it,
                recomItem = recomItem,
                itemPosition = position,
                adapterPosition = widgetMetadata.adapterPosition
            )
        }
    }

    override fun onProductCardClicked(
        data: RecommendationWidget,
        recomItem: RecommendationItem,
        position: Int,
        applink: String
    ) {
        carouselData?.let {
            basicListener?.onRecomProductCardClicked(
                data = it,
                recomItem = recomItem,
                applink = applink,
                itemPosition = position,
                adapterPosition = widgetMetadata.adapterPosition
            )
        }
    }

    override fun onRecomProductCardAddToCartNonVariant(data: RecommendationWidget, recomItem: RecommendationItem, adapterPosition: Int, quantity: Int) {
        carouselData?.let {
            if (widgetMetadata.isRecomBindWithPageName) {
                viewModel?.onAtcRecomNonVariantQuantityChanged(recomItem, quantity)
            } else
                tokonowListener?.onRecomProductCardAddToCartNonVariant(
                    data = it,
                    recomItem = recomItem,
                    adapterPosition = adapterPosition,
                    quantity = quantity
                )
        }
    }

    override fun onRecomProductCardAddVariantClick(data: RecommendationWidget, recomItem: RecommendationItem, adapterPosition: Int) {
        carouselData?.let {
            tokonowListener?.onRecomProductCardAddVariantClick(
                data = it,
                recomItem = recomItem,
                adapterPosition = adapterPosition
            )
            tokonowPageNameListener?.onRecomProductCardAddVariantClick(
                data = it,
                recomItem = recomItem,
                adapterPosition = adapterPosition
            )
        }
    }

    override fun onSeeMoreCardClicked(data: RecommendationWidget, applink: String) {
        carouselData?.let {
            basicListener?.onSeeAllBannerClicked(data = it, applink = applink)
        }
    }

    override fun onBannerCardClicked(data: RecommendationWidget, applink: String) {
        carouselData?.let {
            basicListener?.onRecomBannerClicked(
                data = it,
                applink = applink,
                adapterPosition = widgetMetadata.adapterPosition
            )
        }
    }

    override fun onBannerCardImpressed(data: RecommendationWidget) {
        carouselData?.let {
            basicListener?.onRecomBannerImpressed(
                data = it,
                adapterPosition = widgetMetadata.adapterPosition
            )
        }
    }

    private fun initVar() {
        if (widgetMetadata.isInitialized) return
        carouselData?.let {
            typeFactory = CommonRecomCarouselCardTypeFactoryImpl(it.recommendationData)
        }
        adapter = RecommendationCarouselAdapter(typeFactory)
        layoutManager = createLayoutManager()

        recyclerView.addOnScrollListener(createScrollListener())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        widgetMetadata.isInitialized = true
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
            basicListener?.onRecomBannerImpressed(carouselData, widgetMetadata.adapterPosition)
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
                    recyclerView.visible()
                    loadingView?.gone()
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }
            }
        } else {
            basicListener?.onChannelWidgetEmpty()
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
        headerView?.bindData(data = carouselData.recommendationData, listener = object : RecommendationHeaderListener {
            override fun onSeeAllClick(link: String) {
                basicListener?.onSeeAllBannerClicked(carouselData, link)
            }

            override fun onChannelExpired(widget: RecommendationWidget) {
                basicListener?.onChannelExpired(carouselData, widgetMetadata.adapterPosition)
            }
        })
    }

    private fun scrollCarousel(scrollToPosition: Int) {
        if (!::layoutManager.isInitialized) return

        itemView.post {
            layoutManager.scrollToPositionWithOffset(
                    scrollToPosition,
                    16f.toDpInt()
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
        productIds: List<String> = listOf(),
        categoryIds: List<String> = listOf(),
        keyword: String = "",
        isTokonow: Boolean = false
    ) {
        getMiniCartData()
        if (carouselData == null || isForceRefresh) {
            adapter?.clearAllElements()
            recyclerView.gone()
            loadingView?.visible()
            viewModel?.loadRecommendationCarousel(
                pageName = pageName,
                productIds = productIds,
                categoryIds = categoryIds,
                keywords = listOf(keyword),
                isTokonow = isTokonow
            )
        } else {
            recyclerView.visible()
            loadingView?.gone()
        }
    }

    //data alrd from fragment
    private fun bindWidgetWithData(carouselData: RecommendationCarouselData) {
        this.carouselData = carouselData
        initVar()
        initChips()
        doActionBasedOnRecomState(carouselData.state,
            onLoad = {
                recyclerView.gone()
                loadingView?.visible()
            },
            onReady = {
                recyclerView.visible()
                loadingView?.gone()
                impressChannel(carouselData)
                setHeaderComponent(carouselData)
                setData(carouselData)
                recyclerView.show()
                scrollCarousel(widgetMetadata.scrollToPosition)
            },
            onFailed = {
                recyclerView.gone()
                loadingView?.gone()
            }
        )
    }

    private fun rebindWidgetWithNewFilterData(carouselData: RecommendationCarouselData?) {
        carouselData?.let {
            this.carouselData = carouselData
            setData(carouselData)
            scrollCarousel(0)
            impressChannel(carouselData)
        }
    }

    private fun initChips() {
        if (chipsAdapter == null && carouselData?.filterData?.isNotEmpty() == true) {
            chipsAdapter = RecomChipsAdapter(object : RecomChipsAdapter.RecomChipsListener {
                override fun onFilterAnnotationClicked(
                    annotationChip: AnnotationChip,
                    position: Int
                ) {
                    handleChipsClick(annotationChip, position)
                }
            })
            rvChips.adapter = chipsAdapter
            chipsAdapter?.submitList(carouselData?.filterData ?: listOf())
            rvChips.show()
        } else {
            rvChips.hide()
        }
    }

    private fun handleChipsClick(annotationChip: AnnotationChip,
                                 position: Int) {
        chipsAdapter?.submitList(
            carouselData?.filterData?.map {
                it.copy(
                    recommendationFilterChip = it.recommendationFilterChip.copy(
                        isActivated =
                        annotationChip.recommendationFilterChip.name == it.recommendationFilterChip.name
                                && !annotationChip.recommendationFilterChip.isActivated
                    )
                )
            } ?: listOf()
        )
        if (widgetMetadata.pageName.isEmpty()) {
            //throw to outer listener
                    basicChipListener?.onChipClicked(
                        annotationChip.copy(
                            recommendationFilterChip = annotationChip.recommendationFilterChip.copy(
                                isActivated = !annotationChip.recommendationFilterChip.isActivated)),
                        position
                    )
        } else {
            //call recom viewmodel
                carouselData?.let {
                    viewModel?.loadRecomBySelectedChips(
                        widgetMetadata,
                        it.filterData,
                        annotationChip
                    )
                }
        }
        loadingView?.show()
        recyclerView.invisible()
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
//        getMiniCartData()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pauseEvent() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroyEvent() {

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
                widgetMetadata.isForceRefresh = false
                if (it.pageName == widgetMetadata.pageName) {
                    if (it.recommendationItemList.isNotEmpty()) {
                        bindWidgetWithData(
                            RecommendationCarouselData(
                                recommendationData = it,
                                filterData = mapToAnnotateChip(it),
                                state = STATE_READY,
                                isUsingWidgetViewModel = true
                            )
                        )
                    } else {
                        basicListener?.onChannelWidgetEmpty()
                    }
                }
            })

            viewModel?.errorGetRecommendation?.observe(owner, {
                if (it.pageName == widgetMetadata.pageName) {
                    basicListener?.onWidgetFail(
                        pageName = widgetMetadata.pageName,
                        e = it.throwable
                    )
                }
            })

            //will be updated to all recom widgets on same page (expected)
            viewModel?.miniCartData?.observe(owner, Observer {
                it?.let { map ->
                    updateUiQuantity(map)
                }
            })
            viewModel?.atcRecomTokonow?.observe(owner, Observer { it ->
                if (it.recomItem.pageName == widgetMetadata.pageName) {
                    if (it.error == null) tokonowPageNameListener?.onRecomTokonowAtcSuccess(it.message)
                    else tokonowPageNameListener?.onRecomTokonowAtcFailed(it.error)
                }
            })
            viewModel?.atcRecomTokonowSendTracker?.observe(owner, Observer { data ->
                data.doSuccessOrFail({
                    tokonowPageNameListener?.onRecomTokonowAtcNeedToSendTracker(
                        it.data
                    )
                }, {})
            })
            viewModel?.deleteCartRecomTokonowSendTracker?.observe(owner, Observer { data ->
                data.doSuccessOrFail({
                    tokonowPageNameListener?.onRecomTokonowDeleteNeedToSendTracker(
                        it.data
                    )
                }, {})
            })
            viewModel?.atcRecomTokonowResetCard?.observe(owner, Observer { recomItem ->
                if (recomItem.pageName == widgetMetadata.pageName) {
                    carouselData?.let {
                        TokonowQuantityUpdater.updateCurrentQuantityRecomItem(it, recomItem)
                        setData(it)
                    }
                }
            })
            viewModel?.atcRecomTokonowNonLogin?.observe(owner, Observer { recomItem ->
                tokonowPageNameListener?.onClickItemNonLoginState()
                if (recomItem.pageName == widgetMetadata.pageName) {
                    carouselData?.let {
                        TokonowQuantityUpdater.resetFailedRecomTokonowCard(it, recomItem)
                        setData(it)
                    }
                }
            })
            viewModel?.refreshMiniCartDataTriggerByPageName?.observe(owner, Observer {
                if (it == widgetMetadata.pageName) {
                    getMiniCartData()
                }
            })
            viewModel?.refreshUIMiniCartData?.observe(owner, {
                if (it.pageName == widgetMetadata.pageName) {
                    tokonowPageNameListener?.onMiniCartUpdatedFromRecomWidget(it.miniCartSimplifiedData)
                }
            })
            viewModel?.minicartError?.observe(owner, Observer {
            })
            viewModel?.recomFilterResultData?.observe(owner, Observer { result ->
                if (result.pageName == widgetMetadata.pageName) {
                    if (result.isSuccess) {
                        result.recomWidgetData?.let {
                            rebindWidgetWithNewFilterData(
                                carouselData?.copy(
                                    recommendationData = it,
                                    filterData = result.filterList
                                )
                            )
                        }
                    } else {
                        basicListener?.onShowError(
                            pageName = result.pageName,
                            e = result.throwable ?: MessageErrorException(context?.getString(R.string.failed_to_load)))
                        recyclerView.visible()
                        loadingView?.gone()
                    }
                }
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
                viewModel?.getMiniCart(localAddress?.shop_id ?: "", widgetMetadata.pageName)
            }
        }
    }

    private fun mapToAnnotateChip(data: RecommendationWidget): List<AnnotationChip> {
        return data.recommendationFilterChips.map {
            AnnotationChip(it)
        }
    }

    data class RecomWidgetMetadata(
        val scrollToPosition: Int = 0,
        val pageName: String = "",
        val adapterPosition: Int = 0,
        var isInitialized: Boolean = false,
        var isForceRefresh: Boolean = false,
        val isRecomBindWithPageName: Boolean = false,
        val productIds: List<String> = listOf(),
        val categoryIds: List<String> = listOf(),
        val keyword: String = "",
        val isTokonow: Boolean = false,
        val queryParam: String = ""
    ) {
    }
}