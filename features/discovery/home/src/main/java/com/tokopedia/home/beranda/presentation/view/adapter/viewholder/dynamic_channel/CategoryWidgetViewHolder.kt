package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.circular_view_pager.presentation.widgets.shimmeringImageView.ShimmeringImageView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.CategoryWidgetTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.glide.FPM_CATEGORY_WIDGET_ITEM
import com.tokopedia.home.beranda.helper.glide.loadImage
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.CategoryWidgetSpacingItemDecoration
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.home_dc_category_widget.view.*
import java.util.HashMap

class CategoryWidgetViewHolder(val view: View, val categoryListener: HomeCategoryListener) :
        DynamicChannelViewHolder(view, categoryListener) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_category_widget
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        val recyclerView = itemView.recycleList
        recyclerView.adapter = CategoryWidgetItemAdapter(channel, categoryListener, adapterPosition)
        recyclerView.layoutManager = GridLayoutManager(
                view.context,
                2,
                GridLayoutManager.HORIZONTAL,
                false)
        if (recyclerView.itemDecorationCount == 0) {
            recyclerView.addItemDecoration(CategoryWidgetSpacingItemDecoration(
                    2,
                    itemView.context.resources.getDimensionPixelOffset(R.dimen.dp_8)
            ))
        }
    }

    override fun getViewHolderClassName(): String {
        return CategoryWidgetViewHolder::class.java.toString()
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        CategoryWidgetTracking.sendCategoryWidgetSeeAllClick(channel, categoryListener.userId)
    }

    class CategoryWidgetItemAdapter(
            private val channels: DynamicHomeChannel.Channels,
            private val listener: HomeCategoryListener?,
            private val parentPosition: Int
    ): RecyclerView.Adapter<CategoryWidgetItemViewHolder>() {
        private var grids: Array<DynamicHomeChannel.Grid> = channels.grids

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryWidgetItemViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.home_dc_category_widget_item, parent, false)
            return CategoryWidgetItemViewHolder(v)
        }

        override fun getItemCount(): Int {
            return grids.size
        }

        override fun onBindViewHolder(holder: CategoryWidgetItemViewHolder, position: Int) {
            val grid = grids[position]
            holder.categoryImageView.loadImage(grid.imageUrl, FPM_CATEGORY_WIDGET_ITEM)
            holder.categoryBackground.setBackgroundColor(
                    if (grid.backColor.isNotEmpty()) Color.parseColor(grid.backColor)
                    else ContextCompat.getColor(holder.itemView.context, R.color.light_N50)
            )
            holder.categoryName.text = grid.name
            holder.itemView.setOnClickListener {
                listener?.sendEETracking(
                        CategoryWidgetTracking.getCategoryWidgetBannerClick(
                                channels.header.name,
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
        val categoryBackground: View = view.findViewById(R.id.category_background)
        val categoryName: Typography = view.findViewById(R.id.category_item_name)

        val context: Context
            get() = itemView.context
    }
}