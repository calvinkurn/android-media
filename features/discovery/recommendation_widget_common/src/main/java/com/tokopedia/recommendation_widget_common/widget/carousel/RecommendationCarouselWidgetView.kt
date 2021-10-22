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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_FAILED
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_LOADING
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_READY
import com.tokopedia.recommendation_widget_common.widget.header.RecommendationHeaderListener
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.CommonRecomCarouselCardTypeFactory
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.CommonRecomCarouselCardTypeFactoryImpl
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model.RecomCarouselBannerDataModel
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model.RecomCarouselSeeMoreDataModel
import com.tokopedia.recommendation_widget_common.widget.productcard.common.RecomCommonProductCardListener
import kotlinx.android.synthetic.main.layout_widget_recommendation_carousel.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Created by yfsx on 5/3/21.
 */

class RecommendationCarouselWidgetView : FrameLayout, RecomCommonProductCardListener, CoroutineScope {

    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.Main

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var itemView: View
    private val itemContext: Context
    private var widgetListener: RecommendationCarouselWidgetListener? = null
    private lateinit var carouselData: RecommendationCarouselData
    private lateinit var typeFactory: CommonRecomCarouselCardTypeFactory
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecommendationCarouselAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var scrollListener: ((Parcelable?) -> Unit)? = null
    private var adapterPosition: Int = 0
    private var isInitialized = false

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_widget_recommendation_carousel, this)
        recyclerView = view.findViewById(R.id.rv_product)
        this.itemView = view
        this.itemContext = view.context
    }

    companion object {
        const val NAME_CAMPAIGN_WIDGET = "Campaign-Widget"
    }

    fun bind(
            carouselData: RecommendationCarouselData,
            adapterPosition: Int = 0,
            widgetListener: RecommendationCarouselWidgetListener?,
            scrollToPosition: Int = 0,
    ) {
        this.carouselData = carouselData
        this.widgetListener = widgetListener
        this.adapterPosition = adapterPosition
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

    fun bindTemporaryHeader(tempHeaderName: String) {
        itemView.recommendation_header_view.bindData(RecommendationWidget(title = tempHeaderName), null)
        itemView.loadingRecom.visible()
    }

    override fun onProductCardImpressed(data: RecommendationWidget, recomItem: RecommendationItem, position: Int) {
        widgetListener?.onRecomProductCardImpressed(data = carouselData, recomItem = recomItem, itemPosition = position, adapterPosition = adapterPosition)
    }

    override fun onProductCardClicked(data: RecommendationWidget, recomItem: RecommendationItem, position: Int, applink: String) {
        widgetListener?.onRecomProductCardClicked(data = carouselData, recomItem = recomItem, applink = applink, itemPosition = position, adapterPosition = adapterPosition)
    }

    override fun onRecomProductCardAddToCartNonVariant(data: RecommendationWidget, recomItem: RecommendationItem, adapterPosition: Int, quantity: Int) {
        widgetListener?.onRecomProductCardAddToCartNonVariant(data = carouselData, recomItem = recomItem, adapterPosition = adapterPosition, quantity = quantity)
    }

    override fun onRecomProductCardAddVariantClick(data: RecommendationWidget, recomItem: RecommendationItem, adapterPosition: Int) {
        widgetListener?.onRecomProductCardAddVariantClick(data = carouselData, recomItem = recomItem, adapterPosition = adapterPosition)
    }

    override fun onSeeMoreCardClicked(data: RecommendationWidget, applink: String) {
        widgetListener?.onSeeAllBannerClicked(data = carouselData, applink = applink)
    }

    override fun onBannerCardClicked(data: RecommendationWidget, applink: String) {
        widgetListener?.onRecomBannerClicked(data = carouselData, applink = applink, adapterPosition = adapterPosition)
    }

    override fun onBannerCardImpressed(data: RecommendationWidget) {
        widgetListener?.onRecomBannerImpressed(data = carouselData, adapterPosition = adapterPosition)
    }

    private fun initVar() {
        if (isInitialized) return

        typeFactory = CommonRecomCarouselCardTypeFactoryImpl(carouselData.recommendationData)
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
            widgetListener?.onRecomBannerImpressed(carouselData, adapterPosition)
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

            adapter.submitList(cardList)

            launch {
                try {
                    recyclerView.setHeightBasedOnProductCardMaxHeight(productDataList.map { it.productModel })
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }
            }
        } else {
            widgetListener?.onChannelWidgetEmpty()
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
                widgetListener?.onSeeAllBannerClicked(carouselData, link)
            }

            override fun onChannelExpired(widget: RecommendationWidget) {
                widgetListener?.onChannelExpired(carouselData, adapterPosition)
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

    private fun doActionBasedOnRecomState(state: Int, onLoad: () -> Unit?, onReady: () -> Unit?, onFailed: () -> Unit?) {
        when (carouselData.state) {
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
}