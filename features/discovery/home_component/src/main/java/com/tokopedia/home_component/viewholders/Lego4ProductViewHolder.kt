package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.GlobalDcLego4ProductBinding
import com.tokopedia.home_component.listener.LegoProductListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.mapper.ChannelModelMapper
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.viewholders.adapter.Lego4ProductAdapter
import com.tokopedia.home_component.visitable.LegoProductCardDataModel
import com.tokopedia.home_component.visitable.Lego4ProductDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class Lego4ProductViewHolder(itemView: View,
                             private val legoProductListener: LegoProductListener,
                             private val homeComponentListener: HomeComponentListener,
                             val parentRecyclerViewPool: RecyclerView.RecycledViewPool? = null,
                             private val cardInteraction: Boolean = false
): AbstractViewHolder<Lego4ProductDataModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.global_dc_lego_4_product
        private const val SPAN_COUNT = 2
        private const val LEGO_4_PRODUCT_SIZE = 4
    }
    private val binding: GlobalDcLego4ProductBinding? by viewBinding()
    private val layoutManager: GridLayoutManager by lazy { GridLayoutManager(itemView.context, SPAN_COUNT) }

    private var isCacheData = false

    override fun bind(element: Lego4ProductDataModel) {
        isCacheData = element.isCache
        setHeaderComponent(element)
        setChannelDivider(element)
        initView(element)
    }

    override fun bind(element: Lego4ProductDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setHeaderComponent(element: Lego4ProductDataModel) {
        binding?.homeComponentHeaderView?.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {
                legoProductListener.onSeeAllClicked(element.channelModel, adapterPosition)
            }

            override fun onChannelExpired(channelModel: ChannelModel) {
                homeComponentListener.onChannelExpired(channelModel, adapterPosition, element)
            }
        })
    }

    private fun setChannelDivider(element: Lego4ProductDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun initView(element: Lego4ProductDataModel) {
        initRV()
        initItems(element)
        if (!isCacheData) {
            itemView.addOnImpressionListener(element.channelModel) {
                legoProductListener.onChannelImpressed(element.channelModel, element.channelModel.verticalPosition)
            }
        }
    }

    private fun initRV() {
        parentRecyclerViewPool?.let { binding?.homeComponentLego4ProductRv?.setRecycledViewPool(parentRecyclerViewPool) }
        binding?.homeComponentLego4ProductRv?.layoutManager = layoutManager
    }

    private fun initItems(element: Lego4ProductDataModel) {
        val adapter = Lego4ProductAdapter(element.channelModel, convertDataToProductData(element.channelModel))
        binding?.homeComponentLego4ProductRv?.adapter = adapter
    }

    private fun convertDataToProductData(channel: ChannelModel): List<LegoProductCardDataModel> {
        val list = mutableListOf<LegoProductCardDataModel>()
        channel.channelGrids.forEachIndexed { index, element ->
            if(index >= LEGO_4_PRODUCT_SIZE) {
                return list
            }
            list.add(
                LegoProductCardDataModel(
                    ChannelModelMapper.mapToProductCardModel(element, cardInteraction),
                    blankSpaceConfig = BlankSpaceConfig(),
                    grid = element,
                    applink = element.applink,
                    listener = legoProductListener,
                )
            )
        }
        return list
    }
}
