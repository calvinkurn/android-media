package com.tokopedia.home_component.viewholders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.DynamicChannelHeaderView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.decoration.CategoryWidgetV2SpacingItemDecoration
import com.tokopedia.home_component.listener.CategoryWidgetV2Listener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.FPM_CATEGORY_WIDGET_ITEM
import com.tokopedia.home_component.util.loadImageWithoutPlaceholder
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.home_component.visitable.CategoryWidgetV2DataModel
import com.tokopedia.home_component.visitable.MerchantVoucherDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifyprinciples.Typography

class CategoryWidgetV2ViewHolder(val view: View, private val categoryListener: CategoryWidgetV2Listener?): AbstractViewHolder<CategoryWidgetV2DataModel>(view)  {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_category_widget_v2
        private const val TOTAL_SPAN_RECYCLER = 2
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var adapter: CategoryWidgetV2ItemAdapter
    private lateinit var homeComponentHeader: DynamicChannelHeaderView

    override fun bind(element: CategoryWidgetV2DataModel) {
        setHeaderComponent(element)
        initView(element)
    }

    private fun initView(element: CategoryWidgetV2DataModel) {
        initRV(element)
        initItems(element)
    }

    private fun initRV(element: CategoryWidgetV2DataModel){
        if (!element.isCache) {
            itemView.addOnImpressionListener(element.channelModel){
                categoryListener?.onImpressCategoryWidget(element.channelModel)
            }
        }
        recyclerView = view.findViewById(R.id.recycleList)
        layoutManager = GridLayoutManager(view.context,
            TOTAL_SPAN_RECYCLER,
            GridLayoutManager.HORIZONTAL,
            false)
        recyclerView.layoutManager = layoutManager
    }

    private fun initItems(element: CategoryWidgetV2DataModel) {
        adapter = CategoryWidgetV2ItemAdapter(element, categoryListener)
        recyclerView.adapter = adapter
        if (recyclerView.itemDecorationCount == 0) {
            recyclerView.addItemDecoration(CategoryWidgetV2SpacingItemDecoration(8f.toDpInt()))
        }
        setChannelDivider(element)
    }

    private fun setChannelDivider(element: CategoryWidgetV2DataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = itemView.findViewById(R.id.home_component_divider_header),
            dividerBottom = itemView.findViewById(R.id.home_component_divider_footer)
        )
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

    class CategoryWidgetV2ItemAdapter(
        private val element: CategoryWidgetV2DataModel,
        private val categoryListener: CategoryWidgetV2Listener?
    ): RecyclerView.Adapter<CategoryWidgetItemViewHolder>() {
        private var grids: Array<ChannelGrid> = element.channelModel.channelGrids.toTypedArray()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryWidgetItemViewHolder {
            val layout = R.layout.home_component_category_widget_v2_item
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
                categoryListener?.onClickCategoryWidget(element.channelModel, grid, position)
            }
        }
    }

    class CategoryWidgetItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val categoryImageView: ImageView = view.findViewById(R.id.category_image)
        val categoryName: Typography = view.findViewById(R.id.category_item_name)

        val context: Context
            get() = itemView.context
    }


}