package com.tokopedia.recharge_component.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.customview.DynamicChannelHeaderView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselEmptyCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSeeMorePdpDataModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.util.GravitySnapHelper
import com.tokopedia.home_component.util.ImageHandler
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.home_component.viewholders.adapter.MixLeftAdapter
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.recharge_component.R
import com.tokopedia.recharge_component.listener.RechargeBUWidgetListener
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.recharge_component.model.RechargeBUWidgetProductCardModel
import com.tokopedia.recharge_component.model.RechargePerso
import com.tokopedia.recharge_component.model.WidgetSource
import com.tokopedia.recharge_component.presentation.adapter.RechargeBUWidgetProductCardTypeFactoryImpl
import kotlinx.android.synthetic.main.home_recharge_bu_widget_mix_left.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.*
import kotlin.math.abs

@SuppressLint("SyntheticAccessor")
class RechargeBUWidgetMixLeftViewHolder(itemView: View,
                                        val listener: RechargeBUWidgetListener)
    : AbstractViewHolder<RechargeBUWidgetDataModel>(itemView), CoroutineScope, CommonProductCardCarouselListener {

    lateinit var dataModel: RechargeBUWidgetDataModel

    private lateinit var adapter: MixLeftAdapter

    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.Main

    private lateinit var recyclerView: RecyclerView
    private lateinit var image: ImageView
    private lateinit var parallaxBackground: View
    private lateinit var parallaxView: View

    private lateinit var layoutManager: LinearLayoutManager

    private var isCacheData = false


    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_recharge_bu_widget_mix_left
        const val BU_WIDGET_TYPE_LEFT = "mix-left"
    }

    override fun bind(element: RechargeBUWidgetDataModel) {
        dataModel = element
        if (element.data.items.isNotEmpty()) {
            itemView.recharge_bu_content_shimmering.hide()
            isCacheData = element.isDataCache
            initVar()
            setupBackground(element)
            setupList(element)
            setSnapEffect()
            setHeaderComponent(element)

            if (!isCacheData) {
                itemView.addOnImpressionListener(element) {
                    listener.onRechargeBUWidgetImpression(element)
                }
            }
        } else {
            itemView.recharge_bu_content_shimmering.show()
            val source = element.channel.widgetParam.removePrefix("?section=")
            listener.getRechargeBUWidget(WidgetSource.findSourceByString(source))
        }
    }

    override fun bind(element: RechargeBUWidgetDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun onProductCardImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) {

    }

    override fun onProductCardClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, applink: String) {
        // Decrement position to account for empty product card
        listener.onRechargeBUWidgetItemClick(dataModel, position - 1)
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        listener.onRechargeBUWidgetClickSeeAllCard(dataModel)
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {
        listener.onRechargeBUWidgetClickBanner(dataModel)
    }

    private fun initVar() {
        recyclerView = itemView.findViewById(R.id.rv_recharge_bu_product)
        image = itemView.findViewById(R.id.recharge_bu_parallax_image)
        parallaxBackground = itemView.findViewById(R.id.recharge_bu_parallax_background)
        parallaxView = itemView.findViewById(R.id.recharge_bu_parallax_view)
    }

    private fun setupBackground(element: RechargeBUWidgetDataModel) {
        val imageUrl = element.data.mediaUrl
        if (imageUrl.isNotEmpty()) {
            val gradientColor = element.data.option2
            image.addOnImpressionListener(element.channel) {
                if (!isCacheData)
                    listener.onRechargeBUWidgetBannerImpression(dataModel)
            }
            ImageHandler.LoadImage(image, imageUrl)
            if (gradientColor.isNotEmpty()) {
                parallaxBackground.setGradientBackground(arrayListOf(gradientColor))
            }
        }
    }

    private fun setupList(element: RechargeBUWidgetDataModel) {
        val rechargePerso = element.data
        val channel = element.channel.copy(rechargePerso.title, rechargePerso.title)
        image.alpha = 1f
        recyclerView.resetLayout()
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        val typeFactoryImpl = RechargeBUWidgetProductCardTypeFactoryImpl(channel)
        val listData = mutableListOf<Visitable<*>>()
        listData.add(CarouselEmptyCardDataModel(channel, adapterPosition, this, rechargePerso.bannerApplink))
        val productDataList = convertDataToProductData(rechargePerso)
        listData.addAll(productDataList)
        if (rechargePerso.textlink.isNotEmpty()) {
            listData.add(CarouselSeeMorePdpDataModel(rechargePerso.applink, listener = this))
        }
        adapter = MixLeftAdapter(listData, typeFactoryImpl)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(getParallaxEffect())
    }

    private fun setSnapEffect() {
        val snapHelper: SnapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(recyclerView)
    }

    private fun getParallaxEffect(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    val firstView = layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition())
                    firstView?.let {
                        val distanceFromLeft = it.left
                        val translateX = distanceFromLeft * 0.2f
                        parallaxView.translationX = translateX

                        if (distanceFromLeft <= 0) {
                            val itemSize = it.width.toFloat()
                            val alpha = (abs(distanceFromLeft).toFloat() / itemSize * 0.80f)
                            image.alpha = 1 - alpha
                        }
                    }
                }
            }
        }
    }

    private fun convertDataToProductData(data: RechargePerso): List<RechargeBUWidgetProductCardModel> {
        val list: MutableList<RechargeBUWidgetProductCardModel> = mutableListOf()
        for (element in data.items) {
            list.add(RechargeBUWidgetProductCardModel(
                    element.mediaUrl,
                    element.backgroundColor,
                    element.mediaUrlType,
                    element.title.toUpperCase(Locale.getDefault()),
                    data.option2,
                    element.subtitle,
                    element.label2,
                    element.label1,
                    element.label3,
                    element.applink,
                    this
            ))
        }
        return list
    }

    private fun RecyclerView.resetLayout() {
        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = RecyclerView.LayoutParams.WRAP_CONTENT
        this.layoutParams = carouselLayoutParams
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

    private fun setHeaderComponent(element: RechargeBUWidgetDataModel) {
        val headerView = itemView.findViewById<DynamicChannelHeaderView>(R.id.recharge_bu_widget_header_view)
        val channel = element.channel.copy(
                channelHeader = ChannelHeader(name = element.data.title, applink = element.data.applink)
        )
        headerView.setChannel(channel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {
                listener.onRechargeBUWidgetClickSeeAllButton(element)
            }

            override fun onChannelExpired(channelModel: ChannelModel) {

            }
        })
    }
}
