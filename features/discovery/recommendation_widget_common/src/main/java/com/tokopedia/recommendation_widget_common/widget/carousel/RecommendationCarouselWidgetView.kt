package com.tokopedia.recommendation_widget_common.widget.carousel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
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
    private var adapterPosition: Int = 0

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_widget_recommendation_carousel, this)
        recyclerView = view.findViewById(R.id.rv_product)
        this.itemView = view
        this.itemContext = view.context
    }

    fun bind(carouselData: RecommendationCarouselData, adapterPosition: Int = 0, widgetListener: RecommendationCarouselWidgetListener?) {
        this.carouselData = carouselData
        this.widgetListener = widgetListener
        this.adapterPosition = adapterPosition
        initVar()
        impressChannel(carouselData)
        setHeaderComponent(carouselData)
        setData(carouselData)
    }

    override fun onProductCardImpressed(data: RecommendationWidget, recomItem: RecommendationItem, position: Int) {
        widgetListener?.onRecomProductCardImpressed(data = carouselData, recomItem = recomItem, itemPosition = position, adapterPosition = adapterPosition)
    }

    override fun onProductCardClicked(data: RecommendationWidget, recomItem: RecommendationItem, position: Int, applink: String) {
        widgetListener?.onRecomProductCardClicked(data = carouselData, recomItem = recomItem, applink = applink, itemPosition = position, adapterPosition = adapterPosition)
    }

    override fun onSeeMoreCardClicked(data: RecommendationWidget, applink: String) {
        widgetListener?.onSeeAllBannerClicked(data = carouselData, applink = applink)
    }

    override fun onBannerCardClicked(data: RecommendationWidget, applink: String) {
        widgetListener?.onRecomBannerClicked(data = carouselData, applink = applink, adapterPosition = adapterPosition)
    }

    override fun onBannerCardImpressed(data: RecommendationWidget, applink: String) {
        widgetListener?.onRecomBannerImpressed(data = carouselData, adapterPosition = adapterPosition)
    }

    private fun initVar() {
        typeFactory = CommonRecomCarouselCardTypeFactoryImpl(carouselData.recommendationData)
    }


    private fun impressChannel(carouselData: RecommendationCarouselData) {
        itemView.addOnImpressionListener(carouselData) {
            widgetListener?.onRecomBannerImpressed(carouselData, adapterPosition)
        }
    }

    private fun setData(carouselData: RecommendationCarouselData) {
        val cardList = mutableListOf<Visitable<*>>()
        carouselData.recommendationData.recommendationBanner?.let {
            cardList.add(RecomCarouselBannerDataModel(
                    applink = it.applink,
                    bannerBackgorundColor = it.backgroudColor,
                    bannerImage = it.imageUrl,
                    listener = this))
        }
        val productDataList = carouselData.recommendationData.recommendationItemList.toRecomCarouselItems(listener = this)
        cardList.addAll(productDataList)
        cardList.add(RecomCarouselSeeMoreDataModel())
        adapter = RecommendationCarouselAdapter(cardList, typeFactory)

        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        launch {
            try {
                recyclerView.setHeightBasedOnProductCardMaxHeight(productDataList.map {it.productModel})
            }
            catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
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
        val productCardWidth = itemView.context.resources.getDimensionPixelSize(com.tokopedia.productcard.R.dimen.product_card_flashsale_width)
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

}