package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTrackingV2
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.GravitySnapHelper
import com.tokopedia.home.beranda.helper.glide.loadImage
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mixleft.model.MixLeftAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.EmptyDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.FlashSaleDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.listener.FlashSaleCardListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.typeFactory.FlashSaleCardViewTypeFactoryImpl
import com.tokopedia.home.util.setGradientBackground
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.ProductCardFlashSaleModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.productcard.v2.BlankSpaceConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * @author by yoasfs on 2020-03-05
 */
class MixLeftViewHolder (itemView: View, val homeCategoryListener: HomeCategoryListener,
                         private val parentRecycledViewPool: RecyclerView.RecycledViewPool)
    : DynamicChannelViewHolder(itemView, homeCategoryListener), CoroutineScope, FlashSaleCardListener {

    private lateinit var adapter: MixLeftAdapter

    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.Main

    private val recyclerView: RecyclerView = itemView.findViewById(R.id.rv_product)
    private val image: ImageView = itemView.findViewById(R.id.parallax_image)
    private val loadingBackground: ImageView = itemView.findViewById(R.id.background_loader)
    private val parallaxBackground: View = itemView.findViewById(R.id.parallax_background)
    private val parallaxView: View = itemView.findViewById(R.id.parallax_view)

    private lateinit var layoutManager: LinearLayoutManager


    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_mix_left
        private const val FPM_MIX_LEFT = "home_mix_left"
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        setupBackground(channel)
        setupList(channel)
        setSnapEffect()
    }

    override fun getViewHolderClassName(): String {
        return MixLeftViewHolder::class.java.simpleName
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        this.onBannerSeeMoreClicked(applink, channel)
    }

    override fun onBannerSeeMoreClicked(applink: String, channel: DynamicHomeChannel.Channels) {
        HomePageTrackingV2.MixLeft.sendMixLeftClickLoadMore(channel)
    }

    override fun onFlashSaleCardImpressed(position: Int, channel: DynamicHomeChannel.Channels) {

    }

    override fun onFlashSaleCardClicked(position: Int, channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, applink: String) {
        RouteManager.route(itemView.context, applink)
        HomePageTrackingV2.MixLeft.sendMixLeftProductClick(channel, grid, position - 1)
    }

    private fun setupBackground(channel: DynamicHomeChannel.Channels) {
        if (channel.banner.imageUrl.isNotEmpty()) {
            loadingBackground.show()
            image.loadImage(channel.banner.imageUrl, FPM_MIX_LEFT, object : ImageHandler.ImageLoaderStateListener{
                override fun successLoad() {
                    parallaxBackground.setGradientBackground(channel.banner.gradientColor)
                    loadingBackground.hide()
                }

                override fun failedLoad() {
                    parallaxBackground.setGradientBackground(channel.banner.gradientColor)
                    loadingBackground.hide()
                }
            })
        } else {
            loadingBackground.hide()
        }
    }

    private fun setupList(channel: DynamicHomeChannel.Channels) {
        image.alpha = 1f
        recyclerView.resetLayout()
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        val typeFactoryImpl = FlashSaleCardViewTypeFactoryImpl(channel)
        val listData = mutableListOf<Visitable<*>>()
        listData.add(EmptyDataModel())
        val productDataList = convertDataToProductData(channel)
        listData.addAll(productDataList)

        adapter = MixLeftAdapter(listData,typeFactoryImpl)
        recyclerView.adapter = adapter
        launch {
            try {
                recyclerView.setHeightBasedOnProductCardMaxHeight(productDataList.map {it.productModel})
                parentRecycledViewPool.let {recyclerView.setRecycledViewPool(it) }
            }
            catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
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
                            val alpha = (Math.abs(distanceFromLeft).toFloat() / itemSize * 0.80f)
                            image.alpha = 1 - alpha
                        }
                    }
                }
            }
        }
    }

    private fun convertDataToProductData(channel: DynamicHomeChannel.Channels): List<FlashSaleDataModel> {
        val list :MutableList<FlashSaleDataModel> = mutableListOf()
        for (element in channel.grids) {
            list.add(FlashSaleDataModel(
                    ProductCardFlashSaleModel(
                            slashedPrice = element.slashedPrice,
                            productName = element.name,
                            formattedPrice = element.price,
                            productImageUrl = element.imageUrl,
                            discountPercentage = element.discount,
                            pdpViewCount = element.productViewCountFormatted,
                            stockBarLabel = element.label,
                            isTopAds = element.isTopads,
                            stockBarPercentage = element.soldPercentage,
                            labelGroupList = element.labelGroup.map {
                                ProductCardFlashSaleModel.LabelGroup(
                                        position = it.position,
                                        title = it.title,
                                        type = it.type
                                )
                            },
                            isOutOfStock = element.isOutOfStock
                    ),
                    blankSpaceConfig = BlankSpaceConfig(),
                    grid = element,
                    applink = element.applink,
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
            productCardModelList: List<ProductCardFlashSaleModel>) {
        val productCardHeight = getProductCardMaxHeight(productCardModelList)

        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = productCardHeight
        this.layoutParams = carouselLayoutParams
    }

    private suspend fun getProductCardMaxHeight(productCardModelList: List<ProductCardFlashSaleModel>): Int {
        val productCardWidth = itemView.context.resources.getDimensionPixelSize(com.tokopedia.productcard.R.dimen.product_card_flashsale_width)
        return productCardModelList.getMaxHeightForGridView(itemView.context, Dispatchers.Default, productCardWidth)
    }

}
