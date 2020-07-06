package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.presentation.viewmodel.ProductFlashSaleDataModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DynamicChannelMixLeftViewHolder(
        view: View?,
        private val dcEventHandler: DynamicChannelEventHandler
) : AbstractViewHolder<DynamicChannelViewModel>(view), CoroutineScope, TransparentProductFlashSaleClickListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.dynamic_channel_mix_left_layout
    }

    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.Main

    private val headerContainer = itemView.findViewById<ConstraintLayout>(R.id.dc_header_main_container)
    private val headerTitle = itemView.findViewById<Typography>(R.id.dc_header_title)
    private val headerCountDown = itemView.findViewById<CountDownView>(R.id.dc_header_count_down)
    private val headerActionText = itemView.findViewById<AppCompatTextView>(R.id.dc_header_action_text)
    private val recyclerViewProductList = itemView.findViewById<RecyclerView>(R.id.rv_product)
    private val image = itemView.findViewById<ImageView>(R.id.parallax_image)
    private val bannerBackground = itemView.findViewById<View>(R.id.banner_background)
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: MixWidgetAdapter? = null


    override fun bind(element: DynamicChannelViewModel?) {
        element?.run {
            dcEventHandler.onMixLeftBannerImpressed(dynamicChannelData, 1)
            setupHeader(dynamicChannelData)
            setupContent(dynamicChannelData)
        }
    }

    override fun onClickTransparentItem() {
        image.performClick()
    }

    private fun setupHeader(channel: Channel) {
        channel.header?.let { header ->
            if (header.name.isNotEmpty()) {
                headerContainer.visibility = View.VISIBLE
                headerTitle.text = header.name

                if (header.expiredTime.isNotEmpty()) {
                    val expiredTime = OfficialStoreDateHelper.getExpiredTime(header.expiredTime)

                    headerCountDown.setup(
                            OfficialStoreDateHelper.getServerTimeOffset(header.serverTime),
                            expiredTime,
                            dcEventHandler
                    )
                    headerCountDown.visibility = View.VISIBLE
                } else {
                    headerCountDown.visibility = View.GONE
                }

                if (header.applink.isNotEmpty()) {
                    headerActionText.apply {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            dcEventHandler.onMixFlashSaleSeeAllClicked(channel, header.applink)
                        }
                    }
                } else {
                    headerActionText.visibility = View.GONE
                }
            } else {
                headerContainer.visibility = View.GONE
            }
        }
    }

    private fun setupContent(channelData: Channel) {
        setupBackground(channelData)
        setupList(channelData)
    }

    private fun setupBackground(channel: Channel) {
        channel.banner?.let{ banner ->
            setGradientBackground(bannerBackground, banner.gradientColor)
            image.loadImage(banner.imageUrl)
            image.setOnClickListener { dcEventHandler.onClickMixLeftBannerImage(channel, 1) }
        }
    }

    private fun setupList(channel: Channel) {
        image.alpha = 1f
        recyclerViewProductList.resetLayout()
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewProductList.layoutManager = layoutManager
        val typeFactoryImpl = OfficialStoreFlashSaleCardViewTypeFactoryImpl(dcEventHandler, this, channel)
        val productDataList = convertDataToProductData(channel)
        adapter = MixWidgetAdapter(typeFactoryImpl)
        adapter?.addElement(EmptyModel())
        adapter?.addElement(productDataList)
        recyclerViewProductList.adapter = adapter
        recyclerViewProductList.addOnScrollListener(getParallaxEffect())
        launch {
            try {
                recyclerViewProductList.setHeightBasedOnProductCardMaxHeight(productDataList.map { it.productModel })
                val imageLayoutParams = image.layoutParams
                val recyclerViewLayoutParams = recyclerViewProductList.layoutParams as ViewGroup.MarginLayoutParams
                imageLayoutParams.height = recyclerViewLayoutParams.height + recyclerViewLayoutParams.topMargin + recyclerViewLayoutParams.bottomMargin
            }
            catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    private suspend fun View.setHeightBasedOnProductCardMaxHeight(
            productCardModelList: List<ProductCardModel>
    ) {
        val productCardHeight = getProductCardMaxHeight(productCardModelList)

        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = productCardHeight
        this.layoutParams = carouselLayoutParams
    }

    private suspend fun getProductCardMaxHeight(productCardModelList: List<ProductCardModel>): Int {
            val productCardWidth = itemView.context.resources.getDimensionPixelSize(R.dimen.product_card_carousel_item_width)
            return productCardModelList.getMaxHeightForGridView(itemView.context, Dispatchers.Default, productCardWidth)
    }

    private fun getParallaxEffect(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager?.findFirstVisibleItemPosition().orZero() <= 1 && layoutManager?.findLastVisibleItemPosition().orZero() >= 1) {
                    val firstTransparentView = layoutManager?.findViewByPosition(0)
                    val firstNonTransparentView = layoutManager?.findViewByPosition(1)
                    firstNonTransparentView?.let { it ->
                        val distanceFromLeft = it.left
                        val translateX = (firstTransparentView?.width.orZero() - distanceFromLeft) * -0.1f
                        image.translationX = translateX
                        val alpha = distanceFromLeft.toFloat() / (firstTransparentView?.width.orZero()).toFloat()
                        image.alpha = alpha
                    }
                }else{
                    image.alpha = 0f
                }
            }
        }
    }

    private fun convertDataToProductData(channel: Channel): List<ProductFlashSaleDataModel> {
        return channel.grids?.let { listGridData ->
            val list: MutableList<ProductFlashSaleDataModel> = mutableListOf()
            listGridData.onEach { gridData ->
                gridData?.let { grid ->
                    list.add(ProductFlashSaleDataModel(
                            ProductCardModel(
                                    slashedPrice = grid.slashedPrice,
                                    productName = grid.name,
                                    formattedPrice = grid.price,
                                    productImageUrl = grid.imageUrl,
                                    discountPercentage = grid.discount,
                                    freeOngkir = ProductCardModel.FreeOngkir(grid.freeOngkir?.isActive
                                            ?: false, grid.freeOngkir?.imageUrl ?: ""),
                                    labelGroupList = grid.labelGroup.map {
                                        ProductCardModel.LabelGroup(
                                                position = it.position,
                                                title = it.title,
                                                type = it.type
                                        )
                                    },
                                    hasThreeDots = false
                            ),
                            gridData,
                            grid.applink
                    ))
                }
            }
            return list
        } ?: mutableListOf()
    }

    private fun RecyclerView.resetLayout() {
        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = RecyclerView.LayoutParams.WRAP_CONTENT
        this.layoutParams = carouselLayoutParams
    }

    private fun setGradientBackground(view: View, colorArray: MutableList<String>) {
        if (colorArray.size > 1) {
            val colors = IntArray(colorArray.size)
            for (i in 0 until colorArray.size) {
                colors[i] = Color.parseColor(colorArray[i])
            }
            val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
            gradient.cornerRadius = 0f
            view.background = gradient
        } else {
            view.setBackgroundColor(Color.parseColor(colorArray.getOrNull(0) ?: "#ffffff"))
        }
    }
}
