package com.tokopedia.home_component.viewholders

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.decoration.SimpleHorizontalLinearLayoutDecoration
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.MixTopComponentListener
import com.tokopedia.home_component.mapper.ChannelModelMapper
import com.tokopedia.home_component.model.ChannelCtaData
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselProductCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSeeMorePdpDataModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.GravitySnapHelper
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.home_component.viewholders.adapter.MixTopComponentAdapter
import com.tokopedia.home_component.visitable.MixTopDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.global_dc_mix_left.view.home_component_divider_footer
import kotlinx.android.synthetic.main.global_dc_mix_left.view.home_component_divider_header
import kotlinx.android.synthetic.main.global_dc_mix_left.view.home_component_header_view
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MixTopComponentViewHolder(
        itemView: View,
        val homeComponentListener: HomeComponentListener?,
        val mixTopComponentListener: MixTopComponentListener?
) : AbstractViewHolder<MixTopDataModel>(itemView), CoroutineScope, CommonProductCardCarouselListener {
    private val bannerTitle = itemView.findViewById<Typography>(R.id.banner_title)
    private val bannerDescription = itemView.findViewById<Typography>(R.id.banner_description)
    private val bannerUnifyButton = itemView.findViewById<UnifyButton>(R.id.banner_button)
    private val recyclerView = itemView.findViewById<RecyclerView>(R.id.dc_banner_rv)
    private val startSnapHelper: GravitySnapHelper by lazy { GravitySnapHelper(Gravity.START) }
    private val background = itemView.findViewById<View>(R.id.background)
    private var adapter: MixTopComponentAdapter? = null
    private var isCacheData = false
    companion object{
        @LayoutRes
        val LAYOUT = R.layout.global_dc_mix_top
        private const val CTA_MODE_MAIN = "main"
        private const val CTA_MODE_TRANSACTION = "transaction"
        private const val CTA_MODE_INVERTED = "inverted"
        private const val CTA_MODE_DISABLED = "disabled"
        private const val CTA_MODE_ALTERNATE = "alternate"

        private const val CTA_TYPE_FILLED = "filled"
        private const val CTA_TYPE_GHOST = "ghost"
        private const val CTA_TYPE_TEXT = "text_only"
        private const val HOME_MIX_TOP = "home_mix_top"
    }


    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.Main

    override fun bind(element: MixTopDataModel) {
        isCacheData = element.isCache
        mappingView(element.channelModel)
        setHeaderComponent(element = element)
        setChannelDivider(element = element)
        if (!isCacheData) {
            itemView.addOnImpressionListener(element.channelModel) {
                mixTopComponentListener?.onMixTopImpressed(element.channelModel, adapterPosition)
            }
        }
    }

    override fun bind(element: MixTopDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun onProductCardImpressed(channel: ChannelModel, channelGrid: ChannelGrid, position: Int) {
        if (!isCacheData)
            mixTopComponentListener?.onProductCardImpressed(channel, channelGrid, adapterPosition, position)
    }

    override fun onProductCardClicked(channel: ChannelModel, channelGrid: ChannelGrid, position: Int, applink: String) {
        mixTopComponentListener?.onProductCardClicked(channel, channelGrid, adapterPosition, position, applink)
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        mixTopComponentListener?.onSeeMoreCardClicked(channel,applink)
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {

    }

    private fun setChannelDivider(element: MixTopDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = itemView.home_component_divider_header,
            dividerBottom = itemView.home_component_divider_footer
        )
    }

    private fun mappingView(channel: ChannelModel) {
        val visitables: MutableList<Visitable<*>> = mappingVisitablesFromChannel(channel)
        recyclerView.setHasFixedSize(true)

        valuateRecyclerViewDecoration()

        mappingHeader(channel)
        mappingCtaButton(channel.channelBanner.cta)
        mappingItem(channel, visitables)

    }

    private fun setRecyclerViewAndCardHeight(productDataList: List<CarouselProductCardDataModel>) {
        launch {
            try {
                recyclerView.setHeightBasedOnProductCardMaxHeight(productDataList.map {it.productModel})
            }
            catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    private fun mappingHeader(channel: ChannelModel){
        val bannerItem = channel.channelBanner
        val ctaData = channel.channelBanner.cta
        var textColor = ContextCompat.getColor(bannerTitle.context, R.color.Unify_N50)
        if(bannerItem.textColor.isNotEmpty()){
            try {
                textColor = Color.parseColor(bannerItem.textColor)
            } catch (e: IllegalArgumentException) { }
        }

        bannerTitle.text = bannerItem.title
        bannerTitle.visibility = if(bannerItem.title.isEmpty()) View.GONE else View.VISIBLE
        bannerDescription.text = bannerItem.description
        bannerDescription.visibility = if(bannerItem.description.isEmpty()) View.GONE else View.VISIBLE
        background.setGradientBackground(bannerItem.gradientColor)
        bannerTitle.setTextColor(textColor)
        bannerDescription.setTextColor(textColor)

        bannerUnifyButton.setOnClickListener {
            mixTopComponentListener?.onMixtopButtonClicked(channel)
            if (ctaData.couponCode.isEmpty()) {
                mixTopComponentListener?.onSectionItemClicked(bannerItem.applink)
            } else {
                copyCoupon(itemView, ctaData)
            }
        }

        itemView.setOnClickListener {
            mixTopComponentListener?.onSectionItemClicked(bannerItem.applink)
        }

        background.setOnClickListener {
            mixTopComponentListener?.onBackgroundClicked(channel)
        }
    }

    private fun mappingItem(channel: ChannelModel, visitables: MutableList<Visitable<*>>) {
        startSnapHelper.attachToRecyclerView(recyclerView)
        val typeFactoryImpl = CommonCarouselProductCardTypeFactoryImpl(channel)
        adapter = MixTopComponentAdapter(visitables, typeFactoryImpl)
        recyclerView.adapter = adapter
    }

    private fun mappingCtaButton(cta: ChannelCtaData) {

        //set false first to prevent unexpected behavior
        bannerUnifyButton.isInverse = false

        if (cta.text.isEmpty()) {
            bannerUnifyButton.visibility = View.GONE
        }else {
            bannerUnifyButton.visibility = View.VISIBLE
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

    private fun copyCoupon(view: View, cta: ChannelCtaData) {
        val clipboard = view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Coupon Code", cta.couponCode)
        clipboard.setPrimaryClip(clipData)

        Toaster.build(view.parent as ViewGroup,
                getString(R.string.discovery_home_toaster_coupon_copied),
                Snackbar.LENGTH_LONG).show()
    }

    private fun valuateRecyclerViewDecoration() {
        if (recyclerView.itemDecorationCount == 0) recyclerView.addItemDecoration(SimpleHorizontalLinearLayoutDecoration())
        recyclerView.layoutManager = LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
        )
        /**
         * Attach startSnapHelper to recyclerView
         */
        startSnapHelper.attachToRecyclerView(recyclerView)
    }

    private fun mappingVisitablesFromChannel(channel: ChannelModel): MutableList<Visitable<*>> {
        val visitables: MutableList<Visitable<*>> = mutableListOf()
        val channelProductData = convertDataToProductData(channel)
        setRecyclerViewAndCardHeight(channelProductData)
        visitables.addAll(channelProductData)
        if(channel.channelGrids.size > 1 && channel.channelHeader.applink.isNotEmpty())
            visitables.add(CarouselSeeMorePdpDataModel(channel.channelHeader.applink, channel.channelHeader.backImage, this))
        return visitables
    }

    private fun convertDataToProductData(channel: ChannelModel): List<CarouselProductCardDataModel> {
        val list :MutableList<CarouselProductCardDataModel> = mutableListOf()
        for (element in channel.channelGrids) {
            list.add(CarouselProductCardDataModel(
                    ChannelModelMapper.mapToProductCardModel(element),
                    blankSpaceConfig = BlankSpaceConfig(),
                    grid = element,
                    applink = element.applink,
                    listener = this,
                    componentName = HOME_MIX_TOP
            ))
        }
        return list
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

    private fun setHeaderComponent(element: MixTopDataModel) {
        itemView.home_component_header_view.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {
                mixTopComponentListener?.onSeeAllBannerClicked(element.channelModel, element.channelModel.channelHeader.applink)
            }

            override fun onChannelExpired(channelModel: ChannelModel) {
                homeComponentListener?.onChannelExpired(channelModel, adapterPosition, element)
            }
        })
    }
}