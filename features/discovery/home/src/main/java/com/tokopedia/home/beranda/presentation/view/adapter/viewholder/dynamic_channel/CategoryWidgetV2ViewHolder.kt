package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.CategoryWidgetTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.glide.FPM_CATEGORY_WIDGET_ITEM
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.CategoryWidgetV2SpacingItemDecoration
import com.tokopedia.home.beranda.presentation.view.helper.HomeChannelWidgetUtil
import com.tokopedia.home_component.util.loadImageWithoutPlaceholder
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifyprinciples.Typography
import java.util.HashMap

/**
 * created by Dhaba
 */
class CategoryWidgetV2ViewHolder (val view: View, private val categoryListener: HomeCategoryListener) :
        DynamicChannelViewHolder(view, categoryListener) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_category_widget_v2
        private const val TOTAL_SPAN_RECYCLER = 2
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.recycleList)
        if (!channel.isCache) {
            itemView.addOnImpressionListener(channel) {
                categoryListener.putEEToIris(
                        CategoryWidgetTracking.getCategoryWidgetBannerImpression(
                                channel.grids.toList(),
                                categoryListener.userId,
                                true,
                                channel) as HashMap<String, Any>
                )
            }
        }
        recyclerView.adapter = CategoryWidgetV2ItemAdapter(channel, categoryListener)
        recyclerView.layoutManager = GridLayoutManager(
                view.context,
                TOTAL_SPAN_RECYCLER,
                GridLayoutManager.HORIZONTAL,
                false)

        if (recyclerView.itemDecorationCount == 0) {
            recyclerView.addItemDecoration(CategoryWidgetV2SpacingItemDecoration(8f.toDpInt()))
        }
        setChannelDivider(channel)
    }

    override fun getViewHolderClassName(): String {
        return CategoryWidgetV2ViewHolder::class.java.toString()
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        CategoryWidgetTracking.sendCategoryWidgetSeeAllClick(channel, categoryListener.userId)
    }

    private fun setChannelDivider(channel: DynamicHomeChannel.Channels) {
        HomeChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = channel,
            dividerTop = itemView.findViewById(R.id.home_component_divider_header),
            dividerBottom = itemView.findViewById(R.id.home_component_divider_footer)
        )
    }

    class CategoryWidgetV2ItemAdapter(
        private val channels: DynamicHomeChannel.Channels,
        private val listener: HomeCategoryListener?
    ): RecyclerView.Adapter<CategoryWidgetItemViewHolder>() {
        private var grids: Array<DynamicHomeChannel.Grid> = channels.grids

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryWidgetItemViewHolder {
            val layout = R.layout.home_dc_category_widget_v2_item
            val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            return CategoryWidgetItemViewHolder(v)
        }

        override fun getItemCount(): Int {
            return grids.size
        }

        override fun onBindViewHolder(holder: CategoryWidgetItemViewHolder, position: Int) {
            val grid = grids[position]
            holder.categoryImageView.loadImageWithoutPlaceholder(grid.imageUrl, FPM_CATEGORY_WIDGET_ITEM)
            holder.categoryName.text = grid.name
            holder.itemView.setOnClickListener {
                listener?.sendEETracking(
                        CategoryWidgetTracking.getCategoryWidgetBannerClick(
                                channels.id,
                                listener.userId,
                                (position+1).toString(),
                                grid,
                                channels
                        ) as HashMap<String, Any>
                )
                listener?.onSectionItemClicked(grid.applink)
            }
        }
    }

    class CategoryWidgetItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val categoryImageView: ImageView = view.findViewById(R.id.category_image)
        val categoryName: Typography = view.findViewById(R.id.category_item_name)
        val categoryContainer: View = view.findViewById(R.id.card_container)

        val context: Context
            get() = itemView.context
    }
}