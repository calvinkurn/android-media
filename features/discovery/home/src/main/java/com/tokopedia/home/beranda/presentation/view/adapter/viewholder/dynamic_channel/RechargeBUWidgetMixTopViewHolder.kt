package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.recharge_bu_widget.RechargePerso
import com.tokopedia.home.beranda.listener.RechargeBUWidgetListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.RechargeBUWidgetDataModel
import com.tokopedia.home_component.customview.DynamicChannelHeaderView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.decoration.SimpleHorizontalLinearLayoutDecoration
import com.tokopedia.home_component.model.ChannelCtaData
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselProductCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.util.GravitySnapHelper
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.home_component.viewholders.adapter.MixTopComponentAdapter
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class RechargeBUWidgetMixTopViewHolder(
        itemView: View,
        val listener: RechargeBUWidgetListener
) : AbstractViewHolder<RechargeBUWidgetDataModel>(itemView), CoroutineScope, CommonProductCardCarouselListener {
    private val bannerTitle = itemView.findViewById<Typography>(R.id.recharge_bu_widget_banner_title)
    private val bannerDescription = itemView.findViewById<Typography>(R.id.recharge_bu_widget_banner_description)
    private val bannerUnifyButton = itemView.findViewById<UnifyButton>(R.id.recharge_bu_widget_banner_button)
    private val recyclerView = itemView.findViewById<RecyclerView>(R.id.recharge_bu_widget_dc_banner_rv)
    private val startSnapHelper: GravitySnapHelper by lazy { GravitySnapHelper(Gravity.START) }
    private val background = itemView.findViewById<View>(R.id.recharge_bu_widget_background)
    private var adapter: MixTopComponentAdapter? = null
    private var isCacheData = false
    companion object{
        @LayoutRes
        val LAYOUT = R.layout.home_recharge_bu_widget_mix_top
        private const val CTA_MODE_MAIN = "main"
        private const val CTA_MODE_TRANSACTION = "transaction"
        private const val CTA_MODE_INVERTED = "inverted"
        private const val CTA_MODE_DISABLED = "disabled"
        private const val CTA_MODE_ALTERNATE = "alternate"

        private const val CTA_TYPE_FILLED = "filled"
        private const val CTA_TYPE_GHOST = "ghost"
        private const val CTA_TYPE_TEXT = "text_only"
        private const val HOME_MIX_TOP = "home_mix_top"

        const val BU_WIDGET_TYPE_TOP = "mix-top"
    }

    private var data: RechargePerso = RechargePerso()

    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.Main

    override fun bind(element: RechargeBUWidgetDataModel) {
        data = element.data
        if (data.items.isNotEmpty()) {
            isCacheData = element.isCache
            mappingView(data)

            if (!isCacheData) {
                itemView.addOnImpressionListener(element) {
                    listener.onRechargeBUWidgetImpression(data)
                }
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

    private fun mappingView(data: RechargePerso) {
        recyclerView.setHasFixedSize(true)

        valuateRecyclerViewDecoration()

        mappingHeader(data)
        mappingItem(data)

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

    private fun mappingHeader(data: RechargePerso){
        bannerTitle.text = data.title
        bannerTitle.visibility = if(data.title.isEmpty()) View.GONE else View.VISIBLE
        bannerDescription.text = data.option3
        bannerDescription.visibility = if(data.option3.isEmpty()) View.GONE else View.VISIBLE
        background.setGradientBackground(arrayListOf(data.option2))

        bannerUnifyButton.setOnClickListener {
            listener.onRechargeBUWidgetClickMore(data)
        }
    }

    private fun mappingItem(data: RechargePerso) {
        startSnapHelper.attachToRecyclerView(recyclerView)
        val defaultChannelModel = ChannelModel(data.title, data.title)
        val typeFactoryImpl = CommonCarouselProductCardTypeFactoryImpl(defaultChannelModel)
        val visitables = mappingVisitablesFromChannel(data)
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

    private fun mappingVisitablesFromChannel(data: RechargePerso): MutableList<Visitable<*>> {
        val visitables: MutableList<Visitable<*>> = mutableListOf()
        val channelProductData = convertDataToProductData(data)
        setRecyclerViewAndCardHeight(channelProductData)
        visitables.addAll(channelProductData)
        return visitables
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
                            discountPercentage = element.label1,
                            labelGroupList = listOf(ProductCardModel.LabelGroup(
                                    position = "gimmick",
                                    title = element.title.toUpperCase()
                            ))
                    ),
                    blankSpaceConfig = BlankSpaceConfig(),
                    grid = ChannelGrid(),
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

    private fun setHeaderComponent(id: String, headerTitle: String, applink: String) {
        val channelModel = ChannelModel(id, id, channelHeader = ChannelHeader(id, name = headerTitle, applink = applink))
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