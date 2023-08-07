package com.tokopedia.home_component.viewholders

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.GlobalComponentMissionWidgetBinding
import com.tokopedia.home_component.decoration.MissionWidgetItemDecoration
import com.tokopedia.home_component.listener.MissionWidgetComponentListener
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.util.MissionWidgetUtil
import com.tokopedia.home_component.viewholders.adapter.MissionWidgetAdapter
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
@SuppressLint("PII Data Exposure")
class MissionWidgetViewHolder(
    itemView: View,
    private val missionWidgetComponentListener: MissionWidgetComponentListener,
    private val cardInteraction: Boolean = false
) : AbstractViewHolder<MissionWidgetListDataModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.global_component_mission_widget
    }

    private var binding: GlobalComponentMissionWidgetBinding? by viewBinding()
    private var adapter: MissionWidgetAdapter? = null

    private fun setHeaderComponent(element: MissionWidgetListDataModel) {
        binding?.homeComponentHeaderView?.bind(element.header)
    }

    private fun setChannelDivider(element: MissionWidgetListDataModel) {
//        ChannelWidgetUtil.validateHomeComponentDivider(
//            channelModel = element.channelModel,
//            dividerTop = binding?.homeComponentDividerHeader,
//            dividerBottom = binding?.homeComponentDividerFooter
//        )
    }

    private fun valuateRecyclerViewDecoration() {
        if (binding?.homeComponentMissionWidgetRcv?.itemDecorationCount == 0) {
            binding?.homeComponentMissionWidgetRcv?.addItemDecoration(
                MissionWidgetItemDecoration()
            )
        }
        binding?.homeComponentMissionWidgetRcv?.layoutManager = LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    private fun mappingItem(visitables: List<Visitable<*>>) {
        val typeFactoryImpl = CommonCarouselProductCardTypeFactoryImpl()
        adapter = MissionWidgetAdapter(visitables, typeFactoryImpl)
        binding?.homeComponentMissionWidgetRcv?.adapter = adapter
        binding?.homeComponentMissionWidgetRcv?.scrollToPosition(0)
    }

    private fun convertDataToMissionWidgetData(element: MissionWidgetListDataModel): List<Visitable<*>> {
        val subtitleHeight = MissionWidgetUtil.findMaxHeightSubtitleText(
            element.missionWidgetList,
            itemView.context
        )
        return element.missionWidgetList.mapIndexed { index, item ->
            CarouselMissionWidgetDataModel(
                data = item,
                channelId = element.id,
                channelName = element.name,
                headerName = element.header.name,
                subtitleHeight = subtitleHeight,
                missionWidgetComponentListener = missionWidgetComponentListener,
                verticalPosition = element.verticalPosition,
                cardPosition = index
            )
        }
    }

    private fun setLayoutByStatus(element: MissionWidgetListDataModel) {
        if (element.isShowMissionWidget()) {
            binding?.homeComponentHeaderView?.show()
            binding?.root?.show()
            itemView.show()
            binding?.homeComponentDividerHeader?.show()
            binding?.containerMissionWidgetItem?.show()
            binding?.refreshMissionWidget?.refreshBtn?.setOnClickListener {
                missionWidgetComponentListener.refreshMissionWidget(element)
            }
            setHeaderComponent(element = element)
            setChannelDivider(element)
            when (element.status) {
                MissionWidgetListDataModel.STATUS_LOADING -> {
                    binding?.homeComponentHeaderView?.gone()
                    binding?.refreshMissionWidget?.gone()
                    binding?.homeComponentMissionWidgetRcv?.gone()
                    binding?.shimmeringMissionWidget?.show()
                }
                MissionWidgetListDataModel.STATUS_ERROR -> {
                    binding?.refreshMissionWidget?.show()
                    binding?.homeComponentMissionWidgetRcv?.gone()
                    binding?.shimmeringMissionWidget?.gone()
                    binding?.homeComponentHeaderView?.show()
                }
                else -> {
                    binding?.refreshMissionWidget?.gone()
                    binding?.homeComponentMissionWidgetRcv?.show()
                    binding?.shimmeringMissionWidget?.gone()
                    binding?.homeComponentHeaderView?.show()
                    binding?.homeComponentMissionWidgetRcv?.setHasFixedSize(true)
                    valuateRecyclerViewDecoration()
                    val visitables = convertDataToMissionWidgetData(element)
                    mappingItem(visitables)
                }
            }
        } else {
            binding?.refreshMissionWidget?.gone()
            binding?.homeComponentMissionWidgetRcv?.gone()
            binding?.shimmeringMissionWidget?.gone()
            binding?.homeComponentHeaderView?.gone()
            binding?.homeComponentHeaderView?.gone()
            binding?.homeComponentDividerHeader?.gone()
            binding?.containerMissionWidgetItem?.gone()
            binding?.root?.gone()
            itemView.gone()
        }
    }

    override fun bind(element: MissionWidgetListDataModel) {
        setLayoutByStatus(element)
    }

    override fun bind(element: MissionWidgetListDataModel, payloads: MutableList<Any>) {
        bind(element)
    }
}
