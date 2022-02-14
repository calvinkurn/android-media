package com.tokopedia.home_component.viewholders

import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.HomeComponentSpecialReleaseBinding
import com.tokopedia.home_component.decoration.SimpleHorizontalLinearLayoutDecoration
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.SpecialReleaseComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSpecialReleaseDataModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.GravitySnapHelper
import com.tokopedia.home_component.viewholders.adapter.SpecialReleaseAdapter
import com.tokopedia.home_component.visitable.SpecialReleaseDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

class SpecialReleaseViewHolder(
        itemView: View,
        val homeComponentListener: HomeComponentListener?,
        val specialReleaseComponentListener: SpecialReleaseComponentListener?
) : AbstractViewHolder<SpecialReleaseDataModel>(itemView), CommonProductCardCarouselListener {
    private var binding: HomeComponentSpecialReleaseBinding? by viewBinding()
    private val startSnapHelper: GravitySnapHelper by lazy { GravitySnapHelper(Gravity.START) }
    private var adapter: SpecialReleaseAdapter? = null
    private var isCacheData = false

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.home_component_special_release
    }

    override fun bind(element: SpecialReleaseDataModel) {
        isCacheData = element.isCache
        mappingView(element.channelModel)
        setHeaderComponent(element = element)
        setChannelDivider(element = element)
        if (!isCacheData) {
            itemView.addOnImpressionListener(element.channelModel) {
                specialReleaseComponentListener?.onSpecialReleaseChannelImpressed(element.channelModel, adapterPosition)
            }
        }
    }

    override fun bind(element: SpecialReleaseDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setChannelDivider(element: SpecialReleaseDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun mappingView(channel: ChannelModel) {
        valuateRecyclerViewDecoration()
        mappingItem(channel, channel.channelGrids.map {
            CarouselSpecialReleaseDataModel(it, adapterPosition, this)
        }.toMutableList()
        )
    }

    private fun mappingItem(channel: ChannelModel, visitables: MutableList<Visitable<*>>) {
        startSnapHelper.attachToRecyclerView(binding?.homeComponentSpecialReleaseRv)
        val typeFactoryImpl = CommonCarouselProductCardTypeFactoryImpl(channel)
        adapter = SpecialReleaseAdapter(visitables, typeFactoryImpl)
        binding?.homeComponentSpecialReleaseRv?.adapter = adapter
    }

    private fun valuateRecyclerViewDecoration() {
        if (binding?.homeComponentSpecialReleaseRv?.itemDecorationCount == 0) binding?.homeComponentSpecialReleaseRv?.addItemDecoration(SimpleHorizontalLinearLayoutDecoration())
        binding?.homeComponentSpecialReleaseRv?.layoutManager = LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
        )
        /**
         * Attach startSnapHelper to recyclerView
         */
        startSnapHelper.attachToRecyclerView(binding?.homeComponentSpecialReleaseRv)
    }

    private fun setHeaderComponent(element: SpecialReleaseDataModel) {
        binding?.homeComponentHeaderView?.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {
                specialReleaseComponentListener?.onSpecialReleaseItemSeeAllClicked(element.channelModel, link)
            }

            override fun onChannelExpired(channelModel: ChannelModel) {
                homeComponentListener?.onChannelExpired(channelModel, adapterPosition, element)
            }
        })
    }

    override fun onProductCardImpressed(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int
    ) {
        specialReleaseComponentListener?.onSpecialReleaseItemImpressed(
            grid = channelGrid,
            channelModel = channel,
            position = position
        )
    }

    override fun onProductCardClicked(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        applink: String
    ) {
        specialReleaseComponentListener?.onSpecialReleaseItemClicked(
            grid = channelGrid,
            channelModel = channel,
            position = position
        )
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {}
    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {}
}