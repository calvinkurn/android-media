package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.GlobalComponentFeaturedBrandBinding
import com.tokopedia.home_component.decoration.GridSpacingItemDecoration
import com.tokopedia.home_component.listener.FeaturedBrandListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.DynamicChannelTabletConfiguration
import com.tokopedia.home_component.viewholders.adapter.FeaturedBrandAdapter
import com.tokopedia.home_component.visitable.FeaturedBrandDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

/**
 * @author by yoasfs on 31/05/21
 */
class FeaturedBrandViewHolder (itemView: View,
                               val homeComponentListener: HomeComponentListener?,
                               val featuredBrandListener: FeaturedBrandListener?,
                               val parentRecyclerViewPool: RecyclerView.RecycledViewPool? = null): AbstractViewHolder<FeaturedBrandDataModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.global_component_featured_brand
        private const val FEATURED_BRAND_SPACING = 10
    }
    private var binding: GlobalComponentFeaturedBrandBinding? by viewBinding()
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var adapter: FeaturedBrandAdapter

    private var isCacheData = false

    override fun bind(element: FeaturedBrandDataModel) {
        isCacheData = element.isCache
        setHeaderComponent(element)
        initView(element)
    }

    override fun bind(element: FeaturedBrandDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun initView(element: FeaturedBrandDataModel) {
        initRV()
        initItems(element)
        if (!isCacheData) {
            itemView.addOnImpressionListener(element.channelModel) {
                featuredBrandListener?.onChannelLegoImpressed(element.channelModel, adapterPosition)
            }
        }
    }

    private fun initRV() {
        recyclerView = itemView.findViewById(R.id.recycleList)
        layoutManager = GridLayoutManager(itemView.context, DynamicChannelTabletConfiguration.getSpanCountFor2x2(itemView.context))
        parentRecyclerViewPool?.let { recyclerView.setRecycledViewPool(parentRecyclerViewPool) }
        recyclerView.layoutManager = layoutManager
    }

    private fun initItems(element: FeaturedBrandDataModel) {
        adapter = FeaturedBrandAdapter(featuredBrandListener, adapterPosition, isCacheData)
        adapter.addData(element)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        if (recyclerView.itemDecorationCount == 0) recyclerView.addItemDecoration(
                GridSpacingItemDecoration(DynamicChannelTabletConfiguration.getSpanCountFor2x2(itemView.context), DynamicChannelTabletConfiguration.getSpacingSpaceFor2x2(itemView.context), false))
    }

    private fun setHeaderComponent(element: FeaturedBrandDataModel) {
        binding?.homeComponentHeaderView?.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {
                featuredBrandListener?.onSeeAllClicked(element.channelModel, adapterPosition, link)
            }

            override fun onChannelExpired(channelModel: ChannelModel) {
                homeComponentListener?.onChannelExpired(channelModel, adapterPosition, element)
            }
        })
    }
}