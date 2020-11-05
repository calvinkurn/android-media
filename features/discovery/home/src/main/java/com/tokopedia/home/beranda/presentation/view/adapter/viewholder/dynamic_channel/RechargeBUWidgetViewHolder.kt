package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

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
import com.tokopedia.home.beranda.domain.model.recharge_bu_widget.RechargePerso
import com.tokopedia.home.beranda.listener.RechargeBUWidgetListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.RechargeBUWidgetDataModel
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.DynamicChannelHeaderView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselEmptyCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselProductCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
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
import com.tokopedia.productcard.v2.BlankSpaceConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * @author by yoasfs on 2020-03-05
 */

@SuppressLint("SyntheticAccessor")
class RechargeBUWidgetViewHolder(itemView: View,
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
        val LAYOUT = R.layout.home_component_recharge_bu_widget
        private const val FPM_MIX_LEFT = "home_mix_left"
    }

    override fun bind(element: RechargeBUWidgetDataModel) {
        data = element.data
        if (data.items.isNotEmpty()) {
            isCacheData = element.isCache
            initVar()
            setupBackground(data.mediaUrl, data.option2)
            setupList(data)
            setSnapEffect()
            setHeaderComponent(element.visitableId(), data.title, data.applink)

            itemView.addOnImpressionListener(element) {
                if (!isCacheData) listener.onRechargeBUWidgetImpression(data)
            }
        } else {
            listener.getRechargeBUWidget(element.source)
        }
    }

    override fun bind(element: RechargeBUWidgetDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun onProductCardImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) {
        if (!isCacheData) listener.onRechargeBUWidgetImpression(data)
    }

    override fun onProductCardClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, applink: String) {
        listener.onRechargeBUWidgetItemClick(data.items[position])
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {

    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {

    }

    private fun initVar() {
        recyclerView = itemView.findViewById(R.id.rv_recharge_bu_product)
        image = itemView.findViewById(R.id.recharge_bu_parallax_image)
        loadingBackground = itemView.findViewById(R.id.recharge_bu_background_loader)
        parallaxBackground = itemView.findViewById(R.id.recharge_bu_parallax_background)
        parallaxView = itemView.findViewById(R.id.recharge_bu_parallax_view)
    }

    private fun setupBackground(imageUrl: String, gradientColor: String) {
        if (imageUrl.isNotEmpty()) {
            loadingBackground.show()
//            image.addOnImpressionListener(channel) {
//                if (!isCacheData)
//                    mixLeftComponentListener?.onImageBannerImpressed(channel, adapterPosition)
//            }
            image.loadImage(imageUrl, FPM_MIX_LEFT, object : ImageHandler.ImageLoaderStateListener {
                override fun successLoad() {
                    if (gradientColor.isNotEmpty()) {
                        parallaxBackground.setGradientBackground(arrayListOf(gradientColor))
                    }
                    loadingBackground.hide()
                }

                override fun failedLoad() {
                    if (gradientColor.isNotEmpty()) {
                        parallaxBackground.setGradientBackground(arrayListOf(gradientColor))
                    }
                    loadingBackground.hide()
                }
            })
        } else {
            loadingBackground.hide()
        }
    }

    private fun setupList(data: RechargePerso) {
        val defaultChannelModel = ChannelModel(data.title, data.title)
        image.alpha = 1f
        recyclerView.resetLayout()
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        val typeFactoryImpl = CommonCarouselProductCardTypeFactoryImpl(defaultChannelModel)
        val listData = mutableListOf<Visitable<*>>()
        listData.add(CarouselEmptyCardDataModel(defaultChannelModel, adapterPosition, this))
        val productDataList = convertDataToProductData(data)
        listData.addAll(productDataList)

        launch {
            try {
                recyclerView.setHeightBasedOnProductCardMaxHeight(productDataList.map { it.productModel })
                parentRecycledViewPool?.let { recyclerView.setRecycledViewPool(it) }
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }

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

    private fun convertDataToProductData(data: RechargePerso): List<CarouselProductCardDataModel> {
        val list: MutableList<CarouselProductCardDataModel> = mutableListOf()
        for (element in data.items) {
            list.add(CarouselProductCardDataModel(
                    ProductCardModel(
                            slashedPrice = element.label2,
                            productName = element.subtitle,
                            formattedPrice = element.label3,
                            productImageUrl = element.mediaUrl,
                            discountPercentage = element.label1
                    ),
                    blankSpaceConfig = BlankSpaceConfig(),
                    grid = ChannelGrid(),
                    applink = element.applink,
                    listener = this,
                    componentName = FPM_MIX_LEFT
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

    private fun setHeaderComponent(id: String, headerTitle: String, applink: String) {
        val channelModel = ChannelModel(id, id, channelHeader = ChannelHeader(name = headerTitle, applink = applink))
        val headerView = itemView.findViewById<DynamicChannelHeaderView>(R.id.recharge_bu_header_view)
        headerView.setChannel(channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {
                listener.onRechargeBUWidgetClickMore(data)
            }

            override fun onChannelExpired(channelModel: ChannelModel) {

            }
        })
    }
}
