package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.CategoryWidgetTracking
import com.tokopedia.home.beranda.helper.glide.FPM_CATEGORY_WIDGET_ITEM
import com.tokopedia.home.beranda.helper.glide.loadImageRounded
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.CategoryWidgetSpacingItemDecoration
import com.tokopedia.home.databinding.HomeDcCategoryWidgetBinding
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.home_component.visitable.CategoryWidgetDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*

class CategoryWidgetViewHolder(val view: View, private val categoryListener: HomeCategoryListener) :
    AbstractViewHolder<CategoryWidgetDataModel>(view) {

    private var binding: HomeDcCategoryWidgetBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_category_widget
        private const val CATEGORY_WIDGET_SPAN_COUNT = 2
    }

    override fun bind(element: CategoryWidgetDataModel) {
        val recyclerView = binding?.recycleList
        val channel = element.channelModel
        if (!element.isCache) {
            itemView.addOnImpressionListener(channel) {
                categoryListener.putEEToIris(
                    CategoryWidgetTracking.getCategoryWidgetBannerImpression(
                        channel.channelGrids.toList(),
                        categoryListener.userId,
                        true,
                        channel
                    ) as HashMap<String, Any>
                )
            }
        }
        recyclerView?.adapter = CategoryWidgetItemAdapter(channel, categoryListener, channel.verticalPosition)
        recyclerView?.layoutManager = GridLayoutManager(
            view.context,
            CATEGORY_WIDGET_SPAN_COUNT,
            GridLayoutManager.HORIZONTAL,
            false
        )

        if (recyclerView?.itemDecorationCount == 0) {
            val dimens = 0f.toDpInt()
            recyclerView?.addItemDecoration(
                CategoryWidgetSpacingItemDecoration(
                    CATEGORY_WIDGET_SPAN_COUNT,
                    dimens
                )
            )
        }
        setChannelHeader(channel)
        setChannelDivider(channel)
    }

    private fun setChannelHeader(channel: ChannelModel) {
        binding?.homeComponentHeaderView?.setChannel(
            channel,
            object : HeaderListener {
                override fun onSeeAllClick(link: String) {
                    CategoryWidgetTracking.sendCategoryWidgetSeeAllClick(channel, categoryListener.userId)
                }
            }
        )
    }

    private fun setChannelDivider(channel: ChannelModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = channel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    class CategoryWidgetItemAdapter(
        private val channels: ChannelModel,
        private val listener: HomeCategoryListener?,
        private val parentPosition: Int
    ) : RecyclerView.Adapter<CategoryWidgetItemViewHolder>() {
        private var grids: List<ChannelGrid> = channels.channelGrids

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryWidgetItemViewHolder {
            val layout = R.layout.home_dc_category_widget_item
            val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            return CategoryWidgetItemViewHolder(v)
        }

        override fun getItemCount(): Int {
            return grids.size
        }

        override fun onBindViewHolder(holder: CategoryWidgetItemViewHolder, position: Int) {
            val grid = grids[position]
            holder.categoryImageView.loadImageRounded(grid.imageUrl, 16, FPM_CATEGORY_WIDGET_ITEM)
            holder.categoryName.text = grid.name
            holder.itemView.setOnClickListener {
                listener?.sendEETracking(
                    CategoryWidgetTracking.getCategoryWidgetBannerClick(
                        listener.userId,
                        (position + 1).toString(),
                        grid,
                        channels
                    ) as HashMap<String, Any>
                )
                listener?.onSectionItemClicked(grid.applink)
            }
        }
    }

    class CategoryWidgetItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryImageView: ImageView = view.findViewById(R.id.category_image)
        val categoryName: Typography = view.findViewById(R.id.category_item_name)

        val context: Context
            get() = itemView.context
    }
}
