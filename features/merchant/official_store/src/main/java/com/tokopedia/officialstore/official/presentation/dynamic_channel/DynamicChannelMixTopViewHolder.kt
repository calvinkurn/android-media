package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.officialstore.DynamicChannelIdentifiers.CTA_MODE_ALTERNATE
import com.tokopedia.officialstore.DynamicChannelIdentifiers.CTA_MODE_DISABLED
import com.tokopedia.officialstore.DynamicChannelIdentifiers.CTA_MODE_INVERTED
import com.tokopedia.officialstore.DynamicChannelIdentifiers.CTA_MODE_MAIN
import com.tokopedia.officialstore.DynamicChannelIdentifiers.CTA_MODE_TRANSACTION
import com.tokopedia.officialstore.DynamicChannelIdentifiers.CTA_TYPE_FILLED
import com.tokopedia.officialstore.DynamicChannelIdentifiers.CTA_TYPE_GHOST
import com.tokopedia.officialstore.DynamicChannelIdentifiers.CTA_TYPE_TEXT
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.dynamic_channel.*
import com.tokopedia.officialstore.official.presentation.viewmodel.ProductFlashSaleDataModel
import com.tokopedia.productcard.ProductCardFlashSaleModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DynamicChannelMixTopViewHolder(
        view: View?,
        private val dcEventHandler: DynamicChannelEventHandler
) : AbstractViewHolder<DynamicChannelViewModel>(view), OfficialStoreFlashSaleCardListener, CoroutineScope {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.dynamic_channel_mix_top_layout
    }

    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.Main
    private val headerContainer = itemView.findViewById<ConstraintLayout>(R.id.dc_header_main_container)
    private val headerTitle = itemView.findViewById<Typography>(R.id.dc_header_title)
    private val headerCountDown = itemView.findViewById<CountDownView>(R.id.dc_header_count_down)
    private val headerActionText = itemView.findViewById<AppCompatTextView>(R.id.dc_header_action_text)
    private val recyclerViewProductList = itemView.findViewById<RecyclerView>(R.id.dc_banner_rv)
    private val bannerBackground = itemView.findViewById<View>(R.id.banner_background)
    private val bannerTitle = itemView.findViewById<Typography>(R.id.banner_title)
    private val bannerDescription = itemView.findViewById<Typography>(R.id.banner_description)
    private val bannerUnifyButton = itemView.findViewById<UnifyButton>(R.id.banner_button)
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
        setupBanner(channelData)
        setupList(channelData)
    }

    private fun setupBanner(channel: Channel) {
        channel.banner?.let{banner ->
            val ctaData = banner.cta
            var textColor = ContextCompat.getColor(bannerTitle.context, R.color.Neutral_N50)
            var backColor: Int = ContextCompat.getColor(bannerTitle.context, R.color.Neutral_N50)
            if(banner.textColor.isNotEmpty()){
                try {
                    textColor = Color.parseColor(banner.textColor)
                } catch (e: IllegalArgumentException) { }
            }

            if(banner.backColor.isNotEmpty()) {
                try {
                    backColor = Color.parseColor(banner.backColor)
                } catch (e: IllegalArgumentException) { }
            }

            bannerTitle.text = banner.title
            bannerTitle.visibility = if(banner.title.isEmpty()) View.GONE else View.VISIBLE
            bannerDescription.text = banner.description
            bannerDescription.visibility = if(banner.description.isEmpty()) View.GONE else View.VISIBLE
            bannerBackground.setBackgroundColor(backColor)
            bannerTitle.setTextColor(textColor)
            bannerDescription.setTextColor(textColor)
            ctaData?.let{
                setupBannerUnifyButton(banner)
            }
            itemView.setOnClickListener {
                dcEventHandler.onClickMixTopBannerItem(banner.applink)
            }
        }
    }

    private fun setupBannerUnifyButton(banner: Banner) {
        banner.cta?.let { cta ->
            bannerUnifyButton.setOnClickListener {
//                dcEventHandler.onClickMixTopBannerButton()
            }
            //set false first to prevent unexpected behavior
            bannerUnifyButton.isInverse = false
            if (cta.text.isEmpty()) {
                bannerUnifyButton.visibility = View.GONE
                return
            }
            var mode = CTA_MODE_MAIN
            var type = CTA_TYPE_FILLED

            if (cta.mode.isNotEmpty()) mode = cta.mode

            if (cta.text.isNotEmpty()) type = cta.type

            when (type) {
                CTA_TYPE_FILLED -> bannerUnifyButton.buttonVariant = UnifyButton.Variant.FILLED
                CTA_TYPE_GHOST -> bannerUnifyButton.buttonVariant = UnifyButton.Variant.GHOST
                CTA_TYPE_TEXT -> bannerUnifyButton.buttonVariant = UnifyButton.Variant.TEXT_ONLY
            }

            when (mode) {
                CTA_MODE_MAIN -> bannerUnifyButton.buttonType = UnifyButton.Type.MAIN
                CTA_MODE_TRANSACTION -> bannerUnifyButton.buttonType = UnifyButton.Type.TRANSACTION
                CTA_MODE_ALTERNATE -> bannerUnifyButton.buttonType = UnifyButton.Type.ALTERNATE
                CTA_MODE_DISABLED -> bannerUnifyButton.isEnabled = false
                CTA_MODE_INVERTED -> bannerUnifyButton.isInverse = true
            }

            bannerUnifyButton.text = cta.text
        }
    }

    private fun setupList(channel: Channel) {
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

    private fun copyCoupon(view: View, cta: Cta) {
        val clipboard = view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(getString(R.string.os_coupon_code_label), cta.couponCode)
        clipboard.primaryClip = clipData
        Toaster.make(view.parent as ViewGroup,
                getString(R.string.os_toaster_coupon_copied),
                Snackbar.LENGTH_LONG)
    }

    override fun onFlashSaleCardImpressed(position: Int, channel: Channel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFlashSaleCardClicked(position: Int, channel: Channel, grid: Grid, applink: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
