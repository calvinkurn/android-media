package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Header
import com.tokopedia.officialstore.official.presentation.viewmodel.ProductFlashSaleDataModel
import com.tokopedia.productcard.ProductCardFlashSaleModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DynamicChannelMixLeftViewHolder(
        view: View?,
        private val dcEventHandler: DynamicChannelEventHandler
) : AbstractViewHolder<DynamicChannelViewModel>(view), OfficialStoreFlashSaleCardListener, CoroutineScope {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.dynamic_channel_mix_left_layout
        const val OPACITY_MAX_THRESHOLD = 0.2f
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
    private var adapter: MixLeftAdapter? = null


    override fun bind(element: DynamicChannelViewModel?) {
        element?.run {
            dcEventHandler.flashSaleImpression(dynamicChannelData)
            setupHeader(dynamicChannelData.header)
            setupContent(dynamicChannelData)
        }
    }

    private fun setupHeader(header: Header?) {
        if (header != null && header.name.isNotEmpty()) {
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
                    setOnClickListener(dcEventHandler.onClickFlashSaleActionText(header.applink, header.id))
                    setTextColor(MethodChecker.getColor(itemView.context, R.color.bg_button_green_border_outline))
                }
            } else {
                headerActionText.visibility = View.GONE
            }
        } else {
            headerContainer.visibility = View.GONE
        }
    }

    private fun setupContent(channelData: Channel) {
        setupBackground(channelData)
        setupList(channelData)
    }

    private fun setupBackground(channel: Channel) {
        if (channel.banner?.backColor?.isNotEmpty() == true) {
            bannerBackground.setBackgroundColor(Color.parseColor(channel.banner.backColor))
        }
        if (channel.banner?.imageUrl?.isNotEmpty() == true) {
            image.loadImage(channel.banner.imageUrl)
        }
    }

    private fun setupList(channel: Channel) {
        image.alpha = 1f
        recyclerViewProductList.resetLayout()
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewProductList.layoutManager = layoutManager
        val typeFactoryImpl = OfficialStoreFlashSaleCardViewTypeFactoryImpl(dcEventHandler, channel)
        val productDataList = convertDataToProductData(channel)
        adapter = MixLeftAdapter(typeFactoryImpl)
        adapter?.addElement(productDataList)
        recyclerViewProductList.adapter = adapter
        launch {
            try {
                recyclerViewProductList.setHeightBasedOnProductCardMaxHeight(productDataList.map { it.productModel })
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
        recyclerViewProductList.addOnScrollListener(getParallaxEffect())
    }

    private fun getParallaxEffect(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager?.findFirstVisibleItemPosition() == 0) {
                    val firstView = layoutManager?.findViewByPosition(layoutManager?.findFirstVisibleItemPosition()!!)
                    firstView?.let { it ->
                        val distanceFromLeft = it.left
                        val translateX = distanceFromLeft * 0.1f
                        image.translationX = translateX
                        val alpha = distanceFromLeft.toFloat() / recyclerViewProductList.paddingLeft.toFloat()
                        image.alpha = alpha.takeIf { alphaValue -> alphaValue > OPACITY_MAX_THRESHOLD }
                                ?: OPACITY_MAX_THRESHOLD
                    }
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
                            ProductCardFlashSaleModel(
                                    slashedPrice = grid.slashedPrice,
                                    productName = grid.name,
                                    formattedPrice = grid.price,
                                    productImageUrl = grid.imageUrl,
                                    discountPercentage = "${grid.discountPercentage}%",
                                    stockBarLabel = grid.label,
                                    stockBarPercentage = grid.soldPercentage.toInt()
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

    override fun onFlashSaleCardImpressed(position: Int, channel: Channel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFlashSaleCardClicked(position: Int, channel: Channel, grid: Grid, applink: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
