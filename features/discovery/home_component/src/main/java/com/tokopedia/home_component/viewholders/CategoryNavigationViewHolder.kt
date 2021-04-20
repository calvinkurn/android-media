package com.tokopedia.home_component.viewholders

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.CategoryNavigationListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.visitable.CategoryNavigationDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topads.sdk.snaphelper.GravitySnapHelper
import kotlinx.android.synthetic.main.home_component_category_navigation.view.*
import kotlinx.android.synthetic.main.home_component_category_navigation_item.view.*

/**
 * Created by Lukas on 17/11/20.
 */
class CategoryNavigationViewHolder (view: View, private val listener: CategoryNavigationListener): AbstractViewHolder<CategoryNavigationDataModel> (view){
    private var categoryNavigationDataModel: CategoryNavigationDataModel? = null

    companion object{
        val LAYOUT = R.layout.home_component_category_navigation
    }

    init {
        itemView.category_recyclerview.addItemDecoration(DividerItemDecoration(itemView.context, DividerItemDecoration.HORIZONTAL))
        GravitySnapHelper(Gravity.START).attachToRecyclerView(itemView.category_recyclerview)
    }

    private val adapter = CategoryNavigationAdapter()
    override fun bind(element: CategoryNavigationDataModel) {
        categoryNavigationDataModel = element
        element.channelModel?.run {
            adapter.submitList(channelGrids)
            itemView.category_recyclerview.adapter = adapter
        }
    }

    inner class CategoryNavigationItemViewHolder (view: View): RecyclerView.ViewHolder(view) {
        fun bind(grid: ChannelGrid?){
            grid?.run {
                itemView.category_icon.loadImage(imageUrl) {
                    setPlaceHolder(R.drawable.placeholder_grey)
                }
                itemView.category_title.text = name
                categoryNavigationDataModel?.channelModel?.let { channelModel ->
                    itemView.addOnImpressionListener(grid){
                        listener.onCategoryNavigationImpress(channelModel, this, adapterPosition)
                    }
                    itemView.setOnClickListener {
                        listener.onCategoryNavigationClick(channelModel,this, adapterPosition)
                    }
                }
            }
        }
    }

    inner class CategoryNavigationAdapter : RecyclerView.Adapter<CategoryNavigationItemViewHolder>() {
        private val categoryList = mutableListOf<ChannelGrid>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryNavigationItemViewHolder {
            return CategoryNavigationItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_component_category_navigation_item, parent, false))
        }

        override fun onBindViewHolder(holder: CategoryNavigationItemViewHolder, position: Int) {
            holder.bind(categoryList.getOrNull(position))
        }

        override fun getItemCount(): Int {
            return categoryList.size
        }

        fun submitList(list: List<ChannelGrid>){
            categoryList.clear()
            categoryList.addAll(list)
        }
    }


}