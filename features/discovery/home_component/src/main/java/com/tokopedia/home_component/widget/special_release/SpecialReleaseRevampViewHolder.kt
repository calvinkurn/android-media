package com.tokopedia.home_component.widget.special_release

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.HomeComponentSpecialReleaseRevampBinding
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.NpaLinearLayoutManager
import com.tokopedia.home_component.widget.common.carousel.CarouselListAdapter
import com.tokopedia.home_component.widget.common.carousel.CommonCarouselDiffUtilCallback
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.home_component.R as home_componentR

/**
 * Created by frenzel
 */
class SpecialReleaseRevampViewHolder(
    itemView: View,
    private val specialReleaseRevampListener: SpecialReleaseRevampListener
) : AbstractViewHolder<SpecialReleaseRevampDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = home_componentR.layout.home_component_special_release_revamp
    }

    private val binding: HomeComponentSpecialReleaseRevampBinding? by viewBinding()
    private val typeFactory: CommonCarouselProductCardTypeFactory by lazy { SpecialReleaseRevampItemTypeFactoryImpl(specialReleaseRevampListener) }
    private val adapter by lazy { CarouselListAdapter(typeFactory, CommonCarouselDiffUtilCallback()) }

    override fun bind(element: SpecialReleaseRevampDataModel) {
        binding?.let {
            initAdapter()
            setData(element)
            setHeaderComponent(element.channelModel)
            setChannelDivider(element.channelModel)
        }
    }

    override fun bind(element: SpecialReleaseRevampDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun initAdapter() {
        val layoutManager = NpaLinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL)
        binding?.homeComponentSpecialReleaseRv?.apply {
            setLayoutManager(layoutManager)
            if(itemDecorationCount == 0) {
                addItemDecoration(SpecialReleaseRevampItemDecoration)
            }
            this.adapter = this@SpecialReleaseRevampViewHolder.adapter
        }
    }

    private fun setChannelDivider(channelModel: ChannelModel) {
        binding?.let {
            ChannelWidgetUtil.validateHomeComponentDivider(
                channelModel = channelModel,
                dividerTop = it.homeComponentDividerHeader,
                dividerBottom = it.homeComponentDividerFooter
            )
        }
    }

    private fun setHeaderComponent(channelModel: ChannelModel) {
        binding?.homeComponentHeaderView?.setChannel(
            channelModel,
            object: HeaderListener {
                override fun onSeeAllClick(link: String) {
                    specialReleaseRevampListener.onSeeAllClick(channelModel.trackingAttributionModel, link)
                }
            }
        )
    }

    private fun setData(model: SpecialReleaseRevampDataModel) {
        adapter.submitList(model.specialReleaseItems as? List<Visitable<CommonCarouselProductCardTypeFactory>>)
    }
}
