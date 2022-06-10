package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.GlobalComponentMissionWidgetBinding
import com.tokopedia.home_component.decoration.MerchantVoucherDecoration
import com.tokopedia.home_component.listener.MissionWidgetComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMerchantVoucherDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSeeMorePdpDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselViewAllCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.CarouselViewAllCardViewHolder
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.getTopadsString
import com.tokopedia.home_component.viewholders.adapter.MerchantVoucherAdapter
import com.tokopedia.home_component.viewholders.adapter.MissionWidgetAdapter
import com.tokopedia.home_component.visitable.MerchantVoucherDataModel
import com.tokopedia.home_component.visitable.MissionWidgetDataModel
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class MissionWidgetViewHolder(
    itemView: View,
    val missionWidgetComponentListener: MissionWidgetComponentListener,
    private val cardInteraction: Boolean = false
) : AbstractViewHolder<MissionWidgetListDataModel>(itemView),
    CommonProductCardCarouselListener {

    companion object {
        val LAYOUT = R.layout.global_component_mission_widget
    }

    private var binding : GlobalComponentMissionWidgetBinding? by viewBinding()
    private var adapter: MissionWidgetAdapter? = null

    private fun setHeaderComponent(element: MissionWidgetListDataModel) {
        binding?.homeComponentHeaderView?.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {

            }

            override fun onChannelExpired(channelModel: ChannelModel) {}
        })
    }

    private fun setChannelDivider(element: MissionWidgetListDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun valuateRecyclerViewDecoration() {
        if (binding?.homeComponentMissionWidgetRcv?.itemDecorationCount == 0) binding?.homeComponentMissionWidgetRcv?.addItemDecoration(
            MerchantVoucherDecoration()
        )
        binding?.homeComponentMissionWidgetRcv?.layoutManager = LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    private fun mappingItem(channel: ChannelModel, visitables: MutableList<Visitable<*>>) {
        val typeFactoryImpl = CommonCarouselProductCardTypeFactoryImpl(channel, cardInteraction)
        adapter = MissionWidgetAdapter(visitables, typeFactoryImpl)
        binding?.homeComponentMissionWidgetRcv?.adapter = adapter
        binding?.homeComponentMissionWidgetRcv?.scrollToPosition(0)
    }

    private fun convertDataToMissionWidgetData(listMissionWidget: List<MissionWidgetDataModel>): MutableList<Visitable<*>> {
        val list : MutableList<Visitable<*>> = mutableListOf()
        for (element in listMissionWidget) {
            list.add(
                CarouselMissionWidgetDataModel(
                    id = element.id,
                    title = element.title,
                    subTitle = element.subTitle,
                    appLink = element.appLink,
                    url = element.url,
                    imageURL = element.imageURL
                )
            )
        }
        return list
    }

    private fun setLayoutByStatus(element: MissionWidgetListDataModel) {
        binding?.homeComponentMissionWidgetRcv?.setHasFixedSize(true)
        valuateRecyclerViewDecoration()
        val visitables = convertDataToMissionWidgetData(element.missionWidgetList)
        mappingItem(element.channelModel, visitables)
    }

    override fun bind(element: MissionWidgetListDataModel) {
        setHeaderComponent(element = element)
        setChannelDivider(element)
        setLayoutByStatus(element)
    }

    override fun onProductCardImpressed(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int
    ) {

    }

    override fun onProductCardClicked(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        applink: String
    ) {

    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {

    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {

    }
}