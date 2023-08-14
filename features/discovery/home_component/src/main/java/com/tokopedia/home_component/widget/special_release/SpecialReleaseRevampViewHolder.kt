package com.tokopedia.home_component.widget.special_release

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.HomeComponentSpecialReleaseRevampBinding
import com.tokopedia.home_component.mapper.ChannelModelMapper
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.widget.common.CarouselListAdapter
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class SpecialReleaseRevampViewHolder(
    itemView: View,
    private val specialReleaseRevampListener: SpecialReleaseRevampListener
) : AbstractViewHolder<SpecialReleaseRevampDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = com.tokopedia.home_component.R.layout.home_component_special_release_revamp
    }

    private val binding: HomeComponentSpecialReleaseRevampBinding? by viewBinding()
    private val typeFactory: CommonCarouselProductCardTypeFactory by lazy { CommonCarouselProductCardTypeFactoryImpl() }
    private val adapter: CarouselListAdapter<SpecialReleaseRevampItemDataModel, CommonCarouselProductCardTypeFactory> by lazy {
        CarouselListAdapter(typeFactory, SpecialReleaseDiffUtil())
    }

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
        val layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
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
        val items = model.channelModel.channelGrids.map {
            SpecialReleaseRevampItemDataModel(
                it,
                ChannelModelMapper.mapToProductCardModel(
                    channelGrid = it,
                    animateOnPress = CardUnify2.ANIMATE_NONE,
                    cardType = CardUnify2.TYPE_CLEAR,
                    productCardListType = ProductCardModel.ProductListType.BEST_SELLER,
                    excludeShop = true
                ),
                model.channelModel.trackingAttributionModel,
                cardInteraction = CardUnify2.ANIMATE_NONE,
                listener = specialReleaseRevampListener
            )
        }
        adapter.submitList(items)
    }
}
