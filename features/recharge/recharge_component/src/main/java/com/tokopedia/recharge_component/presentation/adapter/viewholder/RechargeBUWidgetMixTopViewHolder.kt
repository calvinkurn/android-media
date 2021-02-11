package com.tokopedia.recharge_component.presentation.adapter.viewholder

import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.customview.DynamicChannelHeaderView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.decoration.SimpleHorizontalLinearLayoutDecoration
import com.tokopedia.home_component.model.ChannelCtaData
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSeeMorePdpDataModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.util.GravitySnapHelper
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.home_component.viewholders.adapter.MixTopComponentAdapter
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
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.home_recharge_bu_widget_mix_top.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.*

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
        const val BU_WIDGET_TYPE_TOP = "mix-top"
    }

    lateinit var dataModel: RechargeBUWidgetDataModel

    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.Main

    override fun bind(element: RechargeBUWidgetDataModel) {
        dataModel = element
        if (dataModel.data.items.isNotEmpty()) {
            itemView.recharge_bu_content_shimmering.hide()
            isCacheData = element.isDataCache
            mappingView(element)
            setHeaderComponent(element)

            if (!isCacheData) {
                itemView.addOnImpressionListener(element) {
                    listener.onRechargeBUWidgetImpression(dataModel)
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
        listener.onRechargeBUWidgetItemClick(dataModel, position)
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        listener.onRechargeBUWidgetClickSeeAllCard(dataModel)
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {
        listener.onRechargeBUWidgetClickBanner(dataModel)
    }

    private fun mappingView(element: RechargeBUWidgetDataModel) {
        recyclerView.setHasFixedSize(true)

        valuateRecyclerViewDecoration()

        mappingHeader(element)
        mappingItem(element.data)
    }

    private fun mappingHeader(element: RechargeBUWidgetDataModel){
        with (element.data) {
            bannerTitle.text = title
            bannerTitle.visibility = if (title.isEmpty()) View.GONE else View.VISIBLE
            bannerDescription.text = option3
            bannerDescription.visibility = if (option3.isEmpty()) View.GONE else View.VISIBLE

            background.setGradientBackground(arrayListOf(option2))
            background.addOnImpressionListener(element.channel) {
                if (!isCacheData)
                    listener.onRechargeBUWidgetBannerImpression(element)
            }

            if (textlink.isNotEmpty()) bannerUnifyButton.text = textlink
            bannerUnifyButton.setOnClickListener {
                listener.onRechargeBUWidgetClickSeeAllButton(element)
            }
        }
    }

    private fun mappingItem(data: RechargePerso) {
        startSnapHelper.attachToRecyclerView(recyclerView)
        val defaultChannelModel = ChannelModel(data.title, data.title)
        val typeFactoryImpl = RechargeBUWidgetProductCardTypeFactoryImpl(defaultChannelModel)
        val visitables = mappingVisitablesFromChannel(data)
        adapter = MixTopComponentAdapter(visitables, typeFactoryImpl)
        recyclerView.adapter = adapter
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
        visitables.addAll(channelProductData)
        if (data.textlink.isNotEmpty()) {
            visitables.add(CarouselSeeMorePdpDataModel(data.applink, listener = this))
        }
        return visitables
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

    private fun setHeaderComponent(element: RechargeBUWidgetDataModel) {
        val headerView = itemView.findViewById<DynamicChannelHeaderView>(R.id.recharge_bu_widget_header_view)
        headerView.setChannel(element.channel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {

            }

            override fun onChannelExpired(channelModel: ChannelModel) {

            }
        })
    }
}