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
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselEmptyCardDataModel
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
import kotlin.math.abs

@SuppressLint("SyntheticAccessor")
class RechargeBUWidgetMixLeftViewHolder(itemView: View,
                                        val listener: RechargeBUWidgetListener,
                                        private val parentRecycledViewPool: RecyclerView.RecycledViewPool?)
    : AbstractViewHolder<RechargeBUWidgetDataModel>(itemView), CoroutineScope, CommonProductCardCarouselListener {

    private var data: RechargePerso = RechargePerso()

    private lateinit var adapter: MixLeftAdapter

    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.Main

    private lateinit var recyclerView: RecyclerView
    private lateinit var image: ImageView
    private lateinit var loadingBackground: ImageView
    private lateinit var parallaxBackground: View
    private lateinit var parallaxView: View

    private lateinit var layoutManager: LinearLayoutManager

    private var isCacheData = false


    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_recharge_bu_widget_mix_left
        private const val FPM_MIX_LEFT = "home_mix_left"
        const val BU_WIDGET_TYPE_LEFT = "mix-left"
    }

    override fun bind(element: RechargeBUWidgetDataModel) {
        data = element.data
        if (data.items.isNotEmpty()) {
            itemView.recharge_bu_content_shimmering.hide()
            isCacheData = element.isDataCache
            initVar()
            setupBackground(data.mediaUrl, data.option2)
            setupList(data)
            setSnapEffect()
            setHeaderComponent(element)

            if (!isCacheData) {
                itemView.addOnImpressionListener(element) {
                    listener.onRechargeBUWidgetImpression(data, element.channel)
                }
            }
        } else {
            itemView.recharge_bu_content_shimmering.show()
            listener.getRechargeBUWidget(WidgetSource.findSourceByString(element.channel.widgetParam))
        }
    }

    override fun bind(element: RechargeBUWidgetDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun onProductCardImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) {
        if (!isCacheData) listener.onRechargeBUWidgetImpression(data, channelModel)
    }

    override fun onProductCardClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, applink: String) {
        listener.onRechargeBUWidgetItemClick(data, position, channelModel)
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        listener.onRechargeBUWidgetClickSeeAllCard(data, channel)
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {
        listener.onRechargeBUWidgetClickBanner(data, channel)
    }

    private fun initVar() {
        recyclerView = itemView.findViewById(R.id.rv_recharge_bu_product)
        image = itemView.findViewById(R.id.recharge_bu_parallax_image)
        parallaxBackground = itemView.findViewById(R.id.recharge_bu_parallax_background)
        parallaxView = itemView.findViewById(R.id.recharge_bu_parallax_view)
    }

    private fun setupBackground(imageUrl: String, gradientColor: String) {
        if (imageUrl.isNotEmpty()) {
//            image.addOnImpressionListener(channel) {
//                if (!isCacheData)
//                    mixLeftComponentListener?.onImageBannerImpressed(channel, adapterPosition)
//            }
            image.loadImage(imageUrl, FPM_MIX_LEFT, object : ImageHandler.ImageLoaderStateListener {
                override fun successLoad() {
                    if (gradientColor.isNotEmpty()) {
                        parallaxBackground.setGradientBackground(arrayListOf(gradientColor))
                    }
                }

                override fun failedLoad() {
                    if (gradientColor.isNotEmpty()) {
                        parallaxBackground.setGradientBackground(arrayListOf(gradientColor))
                    }
                }
            })
        } else {
        }
    }

    private fun setupList(data: RechargePerso) {
        val defaultChannelModel = ChannelModel(data.title, data.title)
        image.alpha = 1f
        recyclerView.resetLayout()
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        val typeFactoryImpl = RechargeBUWidgetProductCardTypeFactoryImpl(defaultChannelModel)
        val listData = mutableListOf<Visitable<*>>()
        listData.add(CarouselEmptyCardDataModel(defaultChannelModel, adapterPosition, this))
        val productDataList = convertDataToProductData(data)
        listData.addAll(productDataList)

//        launch {
//            try {
//                recyclerView.setHeightBasedOnProductCardMaxHeight(productDataList.map { it.productModel })
//                parentRecycledViewPool?.let { recyclerView.setRecycledViewPool(it) }
//            } catch (throwable: Throwable) {
//                throwable.printStackTrace()
//            }
//        }

//        if (channel.channelGrids.size > 1 && channel.channelHeader.applink.isNotEmpty())
//            listData.add(CarouselSeeMorePdpDataModel(channel.channelHeader.applink, channel.channelHeader.backImage, this))
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
                    data.mediaUrl,
                    "",
                    element.label1Mode,
                    element.title.toUpperCase(),
                    data.option2,
                    element.subtitle,
                    "",
                    element.label1,
                    element.label2,
                    element.label3,
                    element.applink,
                    listener = this
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
        headerView.setChannel(element.channel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {
                listener.onRechargeBUWidgetClickSeeAllButton(element.data, element.channel)
            }

            override fun onChannelExpired(channelModel: ChannelModel) {

            }
        })
    }
}
