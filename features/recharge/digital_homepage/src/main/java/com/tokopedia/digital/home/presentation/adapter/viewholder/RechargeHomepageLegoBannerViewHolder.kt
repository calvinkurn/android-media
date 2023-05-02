package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeLegoBannerBinding
import com.tokopedia.home_component.HomeComponentRollenceController
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.decoration.GridSpacingItemDecoration
import com.tokopedia.home_component.decoration.clearDecorations
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.DynamicChannelLayout
import com.tokopedia.home_component.util.DynamicChannelTabletConfiguration
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.home_component.R as resHome

/**
 * created by @bayazidnasir on 11/2/2022
 */

class RechargeHomepageLegoBannerViewHolder(
    val view: View,
    val listener: DynamicLegoBannerListener,
    val homeListener: HomeComponentListener
) : AbstractViewHolder<DynamicLegoBannerDataModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_lego_banner

        private const val SPAN_COUNT_2 = 2
        private const val SPAN_COUNT_3 = 3
        private const val SPAN_SPACING_0 = 0
        private const val SPAN_SPACING_ROUNDED = 8f
        private const val SPAN_SPACING_BLEEDING = 10
    }

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun bind(element: DynamicLegoBannerDataModel) {
        val binding = ViewRechargeHomeLegoBannerBinding.bind(itemView)

        val isCacheData = element.isCache
        val isLego24UsingRollenceVariant = false

        setHeader(binding, element)
        setGrid(binding, element, isCacheData, isLego24UsingRollenceVariant)
    }

    private fun setHeader(
        binding: ViewRechargeHomeLegoBannerBinding,
        element: DynamicLegoBannerDataModel
    ) {
        binding.homeComponentHeaderView.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {
                when (element.channelModel.channelConfig.layout) {
                    DynamicChannelLayout.LAYOUT_6_IMAGE -> listener.onSeeAllSixImage(
                        element.channelModel,
                        adapterPosition
                    )
                    DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE -> listener.onSeeAllFourImage(
                        element.channelModel,
                        adapterPosition
                    )
                    DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE -> listener.onSeeAllThreemage(
                        element.channelModel,
                        adapterPosition
                    )
                    DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE -> listener.onSeeAllTwoImage(
                        element.channelModel,
                        adapterPosition
                    )
                }
            }

            override fun onChannelExpired(channelModel: ChannelModel) {
                homeListener.onChannelExpired(channelModel, adapterPosition, element)
            }
        })
    }

    private fun setGrid(
        binding: ViewRechargeHomeLegoBannerBinding,
        element: DynamicLegoBannerDataModel,
        isCacheData: Boolean,
        isLegoRolleceVariant: Boolean
    ) {
        if (element.channelModel.channelGrids.isNotEmpty()) {
            val defaultSpanCount = getRecyclerviewDefaultSpanCount(element)
            setViewportImpression(element)
            with(binding.recycleList) {
                setRecycledViewPool(viewPool)
                setHasFixedSize(true)
                if (itemDecorationCount == 0) {
                    addItemDecoration(
                        GridSpacingItemDecoration(
                            defaultSpanCount,
                            SPAN_SPACING_0,
                            true
                        )
                    )
                }
                layoutManager = GridLayoutManager(
                    itemView.context,
                    defaultSpanCount,
                    GridLayoutManager.VERTICAL, false
                )
                adapter = DynamicLegoBannerViewHolder.LegoItemAdapter(
                    listener,
                    element.channelModel,
                    adapterPosition + 1,
                    isCacheData,
                    isLegoRolleceVariant
                )

                clearDecorations()
                var marginValue = 0

                //setup lego banner grid
                if (element.channelModel.channelConfig.layout == DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE && isLegoRolleceVariant) {
                    if (itemDecorationCount == 0) {
                        addItemDecoration(
                            GridSpacingItemDecoration(
                                DynamicChannelTabletConfiguration.getSpanCountFor2x2(itemView.context),
                                SPAN_SPACING_ROUNDED.toDpInt(),
                                false
                            )
                        )
                    }
                    marginValue = getDimens(resHome.dimen.home_component_margin_default)
                } else if (element.channelModel.channelConfig.layout == DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE && isLegoRolleceVariant) {
                    if (itemDecorationCount == 0) {
                        addItemDecoration(
                            GridSpacingItemDecoration(
                                SPAN_COUNT_2,
                                SPAN_SPACING_ROUNDED.toDpInt(),
                                false
                            )
                        )
                    }
                    marginValue = getDimens(resHome.dimen.home_component_margin_default)
                } else if (element.channelModel.channelConfig.layout == DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE
                    || element.channelModel.channelConfig.layout == DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE
                ) {
                    if (itemDecorationCount == 0) {
                        addItemDecoration(
                            GridSpacingItemDecoration(
                                DynamicChannelTabletConfiguration.getSpacingSpaceFor2x2(itemView.context),
                                SPAN_SPACING_BLEEDING,
                                false
                            )
                        )
                        marginValue = 0
                    }
                }

                val marginBottom: Int = marginValue
                val marginLayoutParams = (layoutParams as FrameLayout.LayoutParams).also {
                    it.leftMargin = marginValue
                    it.rightMargin = marginValue
                }
                layoutParams = marginLayoutParams
                setPadding(0, 0, 0, marginBottom)
            }
        } else {
            listener.getDynamicLegoBannerData(element.channelModel)
        }
    }

    private fun getDimens(@DimenRes res: Int): Int = itemView.resources.getDimension(res).toInt()

    private fun getRecyclerviewDefaultSpanCount(element: DynamicLegoBannerDataModel): Int {
        return when (element.channelModel.channelConfig.layout) {
            DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE -> DynamicChannelTabletConfiguration.getSpacingSpaceFor2x2(
                itemView.context
            )
            DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE -> SPAN_COUNT_2
            else -> SPAN_COUNT_3
        }
    }

    private fun setViewportImpression(element: DynamicLegoBannerDataModel) {
        itemView.addOnImpressionListener(element.channelModel) {
            when (element.channelModel.channelConfig.layout) {
                DynamicChannelLayout.LAYOUT_6_IMAGE -> {
                    listener.onChannelImpressionSixImage(element.channelModel, adapterPosition)
                }
                DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE -> {
                    listener.onChannelImpressionThreeImage(element.channelModel, adapterPosition)
                }
                DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE -> {
                    listener.onChannelImpressionFourImage(element.channelModel, adapterPosition)
                }
                DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE -> {
                    listener.onChannelImpressionTwoImage(element.channelModel, adapterPosition)
                }
            }
        }
    }
}
