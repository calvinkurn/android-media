package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity
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
import com.tokopedia.home.beranda.helper.glide.loadImageRounded
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.CategoryWidgetSpacingItemDecoration
import com.tokopedia.home.util.ViewUtils
import com.tokopedia.home_component.HomeComponentRollenceController
import com.tokopedia.home_component.util.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.home_dc_category_widget.view.*
import java.util.*

class CategoryWidgetViewHolder(val view: View, private val categoryListener: HomeCategoryListener) :
        DynamicChannelViewHolder(view, categoryListener) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_category_widget
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        val recyclerView = itemView.recycleList
        if (!channel.isCache) {
            itemView.addOnImpressionListener(channel) {
                categoryListener.putEEToIris(
                        CategoryWidgetTracking.getCategoryWidgetBannerImpression(
                                channel.grids.toList(),
                                categoryListener.userId,
                                true,
                                channel) as HashMap<String, Any>)
            }
        }
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
            var layout = 0
            HomeComponentRollenceController.checkCategoryWidgetRollenceType(
                    isTypeControl = {
                        layout = R.layout.home_dc_category_widget_item
                    },
                    isTypeTextInside = {
                        layout = R.layout.home_dc_category_widget_item_text_inside
                    },
                    isTypeTextBox = {
                        layout = R.layout.home_dc_category_widget_item_text_box
                    }
            )
            val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            return CategoryWidgetItemViewHolder(v)
        }

        override fun getItemCount(): Int {
            return grids.size
        }

        override fun onBindViewHolder(holder: CategoryWidgetItemViewHolder, position: Int) {
            val grid = grids[position]
            HomeComponentRollenceController.checkCategoryWidgetRollenceType(
                    isTypeControl = {
                        holder.categoryImageView.loadImageRounded(grid.imageUrl, 16, FPM_CATEGORY_WIDGET_ITEM)
                    },
                    isTypeTextInside = {
                        holder.categoryImageView.loadImageRounded(grid.imageUrl, 16, FPM_CATEGORY_WIDGET_ITEM)
                    },
                    isTypeTextBox = {
                        holder.categoryImageView.loadImageWithoutPlaceholder(grid.imageUrl, FPM_CATEGORY_WIDGET_ITEM)
                    }
            )
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

        private fun getCardShadow(view: View): Drawable {
            return ViewUtils.generateBackgroundWithShadow(view, R.color.Unify_N0, R.dimen.dp_8, com.tokopedia.unifyprinciples.R.color.Unify_N400_32, R.dimen.dp_2, Gravity.CENTER)
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