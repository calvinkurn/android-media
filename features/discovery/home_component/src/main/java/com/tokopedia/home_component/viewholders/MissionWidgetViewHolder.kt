package com.tokopedia.home_component.viewholders

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.SlideTrackObject
import com.tokopedia.analytics.byteio.addHorizontalTrackListener
import com.tokopedia.home_component.analytics.TrackRecommendationMapper.MISSION_MODULE_NAME
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.home_component.databinding.GlobalComponentMissionWidgetBinding
import com.tokopedia.home_component.decoration.MissionWidgetCardItemDecoration
import com.tokopedia.home_component.decoration.MissionWidgetClearItemDecoration
import com.tokopedia.home_component.listener.MissionWidgetComponentListener
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.home_component.widget.common.carousel.CarouselListAdapter
import com.tokopedia.home_component.widget.common.carousel.CommonCarouselDiffUtilCallback
import com.tokopedia.home_component.widget.mission.MissionWidgetTypeFactory
import com.tokopedia.home_component.widget.mission.MissionWidgetTypeFactoryImpl
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
@SuppressLint("PII Data Exposure")
class MissionWidgetViewHolder(
    itemView: View,
    private val missionWidgetComponentListener: MissionWidgetComponentListener
) : AbstractViewHolder<MissionWidgetListDataModel>(itemView) {

    companion object {
        val LAYOUT = home_componentR.layout.global_component_mission_widget
    }

    private var binding: GlobalComponentMissionWidgetBinding? by viewBinding()
    private val adapter by lazy {
        CarouselListAdapter(
            MissionWidgetTypeFactoryImpl(missionWidgetComponentListener),
            CommonCarouselDiffUtilCallback()
        )
    }

    private var hasApplogScrollListener = false

    init {
        setupRecyclerView()
    }

    private fun setHeaderComponent(element: MissionWidgetListDataModel) {
        binding?.homeComponentHeaderView?.bind(element.header)
    }

    private fun setChannelDivider(element: MissionWidgetListDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelConfig = element.config,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun setupRecyclerView() {
        binding?.homeComponentMissionWidgetRcv?.itemAnimator = null
        binding?.homeComponentMissionWidgetRcv?.layoutManager = LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding?.homeComponentMissionWidgetRcv?.adapter = adapter
    }

    private fun valuateRecyclerViewDecoration(type: MissionWidgetListDataModel.Type) {
        if (binding?.homeComponentMissionWidgetRcv?.itemDecorationCount == 0) {
            val itemDecoration = if(type == MissionWidgetListDataModel.Type.CLEAR)
                MissionWidgetClearItemDecoration()
            else MissionWidgetCardItemDecoration()
            val translationY = if(type == MissionWidgetListDataModel.Type.CLEAR) 0f
            else itemView.context.resources.getDimension(home_componentR.dimen.home_component_card_compat_padding_translation_y)
            binding?.homeComponentMissionWidgetRcv?.addItemDecoration(itemDecoration)
            binding?.homeComponentMissionWidgetRcv?.translationY = translationY
        }
    }

    private fun mappingItem(visitables: List<Visitable<MissionWidgetTypeFactory>>) {
        adapter.submitList(visitables as? List<Visitable<CommonCarouselProductCardTypeFactory>>) {
            binding?.homeComponentMissionWidgetRcv?.scrollToPosition(0)
        }
    }

    private fun addHorizontalTrackListener() {
        if(hasApplogScrollListener) return
        binding?.homeComponentMissionWidgetRcv?.addHorizontalTrackListener(
            SlideTrackObject(
                moduleName = MISSION_MODULE_NAME,
                barName = MISSION_MODULE_NAME,
            )
        )
        hasApplogScrollListener = true
    }

    private fun MissionWidgetListDataModel.convertToVisitables(): List<Visitable<MissionWidgetTypeFactory>> {
        val width = missionWidgetUtil.getWidth(itemView.context)
        val titleHeight = missionWidgetUtil.findMaxTitleHeight(this, width, itemView.context)
        val subtitleHeight = missionWidgetUtil.findMaxSubtitleHeight(this, width, itemView.context)
        return missionWidgetList.mapIndexed { index, item ->
            CarouselMissionWidgetDataModel(
                data = item,
                channelId = id,
                channelName = name,
                headerName = header.name,
                withSubtitle = isWithSubtitle(),
                width = width,
                titleHeight = titleHeight,
                subtitleHeight = subtitleHeight,
                verticalPosition = verticalPosition,
                cardPosition = index,
                animateOnPress = item.animateOnPress,
                isCache = item.isCache,
                type = type,
                appLog = item.appLog,
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
                    addHorizontalTrackListener()
                    mappingItem(element.convertToVisitables())
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
        valuateRecyclerViewDecoration(element.type)
        setLayoutByStatus(element)
    }

    override fun bind(element: MissionWidgetListDataModel, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty() && (payloads[0] as? Bundle)?.getBoolean(MissionWidgetListDataModel.PAYLOAD_IS_REFRESH, false) == true) return
        bind(element)
    }
}
