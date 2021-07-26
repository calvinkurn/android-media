package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.official.presentation.widget.CountDownView
import com.tokopedia.officialstore.DynamicChannelIdentifiers.CTA_MODE_ALTERNATE
import com.tokopedia.officialstore.DynamicChannelIdentifiers.CTA_MODE_DISABLED
import com.tokopedia.officialstore.DynamicChannelIdentifiers.CTA_MODE_INVERTED
import com.tokopedia.officialstore.DynamicChannelIdentifiers.CTA_MODE_MAIN
import com.tokopedia.officialstore.DynamicChannelIdentifiers.CTA_MODE_TRANSACTION
import com.tokopedia.officialstore.DynamicChannelIdentifiers.CTA_TYPE_FILLED
import com.tokopedia.officialstore.DynamicChannelIdentifiers.CTA_TYPE_GHOST
import com.tokopedia.officialstore.DynamicChannelIdentifiers.CTA_TYPE_TEXT
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.OfficialStoreChannel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Banner
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.presentation.viewmodel.ProductFlashSaleDataModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DynamicChannelMixTopViewHolder(
        view: View?,
        private val dcEventHandler: DynamicChannelEventHandler
) : AbstractViewHolder<DynamicChannelDataModel>(view), CoroutineScope {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.dynamic_channel_mix_top_layout
    }

    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.IO
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
    private var adapter: MixWidgetAdapter? = null


    override fun bind(element: DynamicChannelDataModel?) {
        element?.run {
            setupHeader(dynamicChannelData.channel)
            setupContent(dynamicChannelData)
        }
    }

    private fun setupHeader(channel: Channel) {
        channel.header?.let{header ->
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
                            dcEventHandler.onMixFlashSaleSeeAllClicked( channel,header.applink)
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

    private fun setupContent(dynamicChannelData: OfficialStoreChannel) {
        setupBanner(dynamicChannelData.channel)
        setupList(dynamicChannelData)
    }

    private fun setupBanner(channel: Channel) {
        channel.banner?.let{banner ->
            val ctaData = banner.cta
            var textColor = ContextCompat.getColor(bannerTitle.context, R.color.Unify_N50)
            if(banner.textColor.isNotEmpty()){
                try {
                    textColor = Color.parseColor(banner.textColor)
                } catch (e: IllegalArgumentException) { }
            }

            bannerTitle.text = banner.title
            bannerTitle.visibility = if(banner.title.isEmpty()) View.GONE else View.VISIBLE
            bannerDescription.text = banner.description
            bannerDescription.visibility = if(banner.description.isEmpty()) View.GONE else View.VISIBLE
            setGradientBackground(bannerBackground, banner.gradientColor)
            bannerTitle.setTextColor(textColor)
            bannerDescription.setTextColor(textColor)
            ctaData?.let{
                setupBannerUnifyButton(channel, banner)
            }
            itemView.setOnClickListener {
                dcEventHandler.onClickMixTopBannerItem(banner.applink)
            }
        }
    }

    private fun setupBannerUnifyButton(channel: Channel,banner: Banner) {
        banner.cta?.let { cta ->
            bannerUnifyButton.setOnClickListener {
                dcEventHandler.onClickMixTopBannerCtaButton(
                        cta,
                        channel.id,
                        banner.applink
                )
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

    private fun setupList(dynamicChannelData: OfficialStoreChannel) {
        val channel = dynamicChannelData.channel
        recyclerViewProductList.resetLayout()
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewProductList.layoutManager = layoutManager
        val typeFactoryImpl = OfficialStoreFlashSaleCardViewTypeFactoryImpl(dcEventHandler, null, channel)
        val productDataList = convertDataToProductData(dynamicChannelData)
        if(adapter == null){
            adapter = MixWidgetAdapter(typeFactoryImpl)
            recyclerViewProductList.adapter = adapter
        }
        adapter?.clearAllElements()
        adapter?.addElement(productDataList)
        launch {
            try {
                recyclerViewProductList.setHeightBasedOnProductCardMaxHeight(dynamicChannelData)
            }
            catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    private fun View.setHeightBasedOnProductCardMaxHeight(officialStoreChannel: OfficialStoreChannel) {
        val productCardHeight = officialStoreChannel.height

        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = productCardHeight
        this.layoutParams = carouselLayoutParams
    }

    private suspend fun getProductCardMaxHeight(productCardModelList: List<ProductCardModel>): Int {
        val productCardWidth = itemView.context.resources.getDimensionPixelSize(R.dimen.product_card_carousel_item_width)
        return productCardModelList.getMaxHeightForGridView(itemView.context, Dispatchers.IO, productCardWidth)
    }

    private fun convertDataToProductData(officialStoreChannel: OfficialStoreChannel): List<ProductFlashSaleDataModel> {
        return officialStoreChannel.productCardModels.withIndex().map {
            ProductFlashSaleDataModel(
                    it.value,
                    officialStoreChannel.channel.grids[it.index],
                    officialStoreChannel.channel.grids[it.index].applink
            )
        }
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
            val defaultColorString = "#${Integer.toHexString(ContextCompat.getColor(itemView.context, R.color.Unify_N0))}"
            view.setBackgroundColor(Color.parseColor(colorArray.getOrNull(0) ?: defaultColorString))
        }
    }
}
