package com.tokopedia.home_component.viewholders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.DynamicChannelHeaderView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.util.FPM_CATEGORY_WIDGET_ITEM
import com.tokopedia.home_component.decoration.CategoryWidgetV2SpacingItemDecoration
import com.tokopedia.home_component.listener.CategoryWidgetV2Listener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.loadImageWithoutPlaceholder
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.home_component.visitable.CategoryWidgetV2DataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifyprinciples.Typography

/**
 * created by Dhaba
 */
class CategoryWidgetV2ViewHolder (val view: View,
                                  private val categoryListener: CategoryWidgetV2Listener?,
                                  private val cardInteraction: Boolean = false
) : AbstractViewHolder<CategoryWidgetV2DataModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_category_widget_v2
        private const val TOTAL_SPAN_RECYCLER = 2
        private const val IMAGE_CORNER = 0
        private const val CARD_CORNER_RADIUS = 9f
    }

    private lateinit var homeComponentHeader: DynamicChannelHeaderView

    override fun bind(element: CategoryWidgetV2DataModel) {
        setHeaderComponent(element)
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.recycleList)
        if (!element.isCache) {
            itemView.addOnImpressionListener(element.channelModel) {
                categoryListener?.onImpressCategoryWidget(element.channelModel)
            }
        }
        recyclerView.adapter = CategoryWidgetV2ItemAdapter(element, categoryListener, cardInteraction)
        recyclerView.layoutManager = GridLayoutManager(
            view.context,
            TOTAL_SPAN_RECYCLER,
            GridLayoutManager.HORIZONTAL,
            false)

        if (recyclerView.itemDecorationCount == 0) {
            recyclerView.addItemDecoration(CategoryWidgetV2SpacingItemDecoration(8f.toDpInt()))
        }
        setChannelDivider(element)
    }

    private fun setHeaderComponent(element: CategoryWidgetV2DataModel) {
        homeComponentHeader = itemView.findViewById(R.id.home_component_header_view)
        homeComponentHeader?.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {
                categoryListener?.onSeeAllCategoryWidget(element.channelModel)
            }
            override fun onChannelExpired(channelModel: ChannelModel) {}
        })
    }

    private fun setChannelDivider(element: CategoryWidgetV2DataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = itemView.findViewById(R.id.home_component_divider_header),
            dividerBottom = itemView.findViewById(R.id.home_component_divider_footer)
        )
    }

    class CategoryWidgetV2ItemAdapter(
        private val element: CategoryWidgetV2DataModel,
        private val categoryListener: CategoryWidgetV2Listener?,
        private val cardInteraction: Boolean = false
    ): RecyclerView.Adapter<CategoryWidgetItemViewHolder>() {
        private var grids: Array<ChannelGrid> = element.channelModel.channelGrids.toTypedArray()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryWidgetItemViewHolder {
            val layout = R.layout.home_component_category_widget_v2_item
            val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            val viewHolder = CategoryWidgetItemViewHolder(v)
            viewHolder.cardUnify.apply {
                radius = CARD_CORNER_RADIUS.dpToPx()
                cardType = CardUnify2.TYPE_BORDER
                animateOnPress = if(cardInteraction) CardUnify2.ANIMATE_OVERLAY_BOUNCE else CardUnify2.ANIMATE_OVERLAY
            }
            return viewHolder
        }

        override fun getItemCount(): Int {
            return grids.size
        }

        override fun onBindViewHolder(holder: CategoryWidgetItemViewHolder, position: Int) {
            val grid = grids[position]
            holder.categoryImageView.apply {
                cornerRadius = IMAGE_CORNER
                loadImageWithoutPlaceholder(grid.imageUrl, FPM_CATEGORY_WIDGET_ITEM)
            }
            holder.categoryName.text = grid.name
            holder.itemView.setOnClickListener {
                categoryListener?.onClickCategoryWidget(element.channelModel, grid, position)
            }
        }
    }

    class CategoryWidgetItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val cardUnify: CardUnify2 = view.findViewById(R.id.item_cat_card)
        val categoryImageView: ImageUnify = view.findViewById(R.id.category_image)
        val categoryName: Typography = view.findViewById(R.id.category_item_name)

        val context: Context
            get() = itemView.context
    }
}