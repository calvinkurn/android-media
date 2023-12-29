package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.DynamicChannelHeaderView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.listener.CampaignWidgetComponentListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselCampaignCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSeeMorePdpDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselViewAllCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.CarouselViewAllCardViewHolder
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.viewholders.adapter.CampaignWidgetAdapter
import com.tokopedia.home_component.visitable.CampaignWidgetDataModel

/**
 * Created by yfsx on 11/10/21.
 */
class CampaignWidgetViewHolder(
    itemView: View,
    val homeComponentListener: HomeComponentListener?,
    val campaignWidgetComponentListener: CampaignWidgetComponentListener?,
    private val parentRecycledViewPool: RecyclerView.RecycledViewPool? = null,
    private val cardInteraction: Boolean = false
) : AbstractViewHolder<CampaignWidgetDataModel>(itemView), CommonProductCardCarouselListener {

    private lateinit var adapter: CampaignWidgetAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.global_component_campaign_widget
    }

    override fun bind(element: CampaignWidgetDataModel) {
        initView()
        setHeaderComponent(element = element)
        setCarouselData(element)
        setChannelDivider(element)
    }

    override fun onProductCardImpressed(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int
    ) {
        campaignWidgetComponentListener?.onProductCardImpressed(
            channel,
            channelGrid,
            adapterPosition,
            position
        )
    }

    override fun onProductCardClicked(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        applink: String
    ) {
        campaignWidgetComponentListener?.onProductCardClicked(
            channel,
            channelGrid,
            adapterPosition,
            position,
            applink
        )
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        campaignWidgetComponentListener?.onSeeMoreCardClicked(channel, applink)
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {
    }

    private fun initView() {
        recyclerView = itemView.findViewById(R.id.recycler_view)
    }

    private fun setCarouselData(element: CampaignWidgetDataModel) {
        val dataList = mutableListOf<Visitable<*>>()
        if (element.channelModel.channelGrids.isNotEmpty()) {
            element.channelModel.channelGrids.forEach {
                dataList.add(
                    CarouselCampaignCardDataModel(
                        grid = it,
                        parentPosition = adapterPosition,
                        listener = this
                    )
                )
            }
        }
        if (element.channelModel.channelGrids.size > 1 && element.channelModel.channelHeader.applink.isNotEmpty()) {
            if (element.channelModel.channelViewAllCard.id != CarouselViewAllCardViewHolder.DEFAULT_VIEW_ALL_ID
                && element.channelModel.channelViewAllCard.contentType.isNotBlank()
                && element.channelModel.channelViewAllCard.contentType != CarouselViewAllCardViewHolder.CONTENT_DEFAULT
            ) {
                dataList.add(
                    CarouselViewAllCardDataModel(
                        element.channelModel.channelHeader.applink,
                        element.channelModel.channelViewAllCard,
                        this,
                        element.channelModel.channelBanner.imageUrl,
                        element.channelModel.channelBanner.gradientColor,
                        element.channelModel.layout
                    )
                )
            } else {
                dataList.add(
                    CarouselSeeMorePdpDataModel(
                        element.channelModel.channelHeader.applink,
                        element.channelModel.channelHeader.backImage,
                        this
                    )
                )
            }
        }
        val typeFactoryImpl = CommonCarouselProductCardTypeFactoryImpl(element.channelModel, cardInteraction)
        adapter = CampaignWidgetAdapter(dataList, typeFactoryImpl)
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
    }

    private fun setHeaderComponent(element: CampaignWidgetDataModel) {
        val header =
            itemView.findViewById<DynamicChannelHeaderView>(R.id.home_component_header_view)
        header.setChannel(element.channelModel, object :
            HeaderListener {
            override fun onSeeAllClick(link: String) {
                campaignWidgetComponentListener?.onSeeAllBannerClicked(
                    element.channelModel,
                    element.channelModel.channelHeader.applink
                )
            }

            override fun onChannelExpired(channelModel: ChannelModel) {
                homeComponentListener?.onChannelExpired(channelModel, adapterPosition, element)
            }
        })
    }

    private fun setChannelDivider(element: CampaignWidgetDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = itemView.findViewById(R.id.home_component_divider_header),
            dividerBottom = itemView.findViewById(R.id.home_component_divider_footer)
        )
    }
}
