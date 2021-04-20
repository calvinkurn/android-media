package com.tokopedia.home_component.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.DynamicIconComponentListener
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import kotlinx.android.synthetic.main.home_component_dynamic_icon.view.*
import kotlinx.android.synthetic.main.home_component_dynamic_icon_item.view.*

/**
 * Created by Lukas on 1/8/21.
 */
class DynamicIconViewHolder (itemView: View, private val listener: DynamicIconComponentListener): AbstractViewHolder<DynamicIconComponentDataModel>(itemView){
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_dynamic_icon
        
        
        private const val SCROLLABLE_ITEM = 5
    }

    private val adapter = DynamicIconAdapter(listener)

    override fun bind(element: DynamicIconComponentDataModel) {
        setupDynamicIcon(element.type, element.dynamicIconComponent.dynamicIcon)
        setupImpression(element)
    }

    private fun setupDynamicIcon(type: Int, dynamicIcons: List<DynamicIconComponent.DynamicIcon>){
        if (dynamicIcons.isNotEmpty()) {
            adapter.submitList(dynamicIcons)
            adapter.updatePosition(adapterPosition)
            adapter.setType(type)
            itemView.dynamic_icon_recycler_view.adapter = adapter
            setupLayoutManager(
                    isScrollItem = dynamicIcons.size > SCROLLABLE_ITEM,
                    spanCount = dynamicIcons.size
            )
        }
    }

    private fun setupImpression(element: DynamicIconComponentDataModel){
        itemView.addOnImpressionListener(element){
            if(!element.isCache) listener.onImpressIcon(element.dynamicIconComponent.dynamicIcon, adapterPosition, element.type)
        }
    }

    private fun setupLayoutManager(isScrollItem: Boolean, spanCount: Int){
        if(isScrollItem){
            itemView.dynamic_icon_recycler_view.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        } else {
            itemView.dynamic_icon_recycler_view.layoutManager = GridLayoutManager(itemView.context, spanCount, GridLayoutManager.VERTICAL, false)
        }
    }

    internal inner class DynamicIconAdapter (private val listener: DynamicIconComponentListener) : RecyclerView.Adapter<DynamicIconItemViewHolder>() {
        private val categoryList = mutableListOf<DynamicIconComponent.DynamicIcon>()
        private var position: Int = 0
        private var type: Int = 1
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicIconItemViewHolder {
            return DynamicIconItemViewHolder(LayoutInflater.from(parent.context).inflate(DynamicIconItemViewHolder.LAYOUT, parent, false), listener)
        }

        override fun onBindViewHolder(holder: DynamicIconItemViewHolder, position: Int) {
            categoryList.getOrNull(position)?.let { holder.bind(it, categoryList.size > SCROLLABLE_ITEM, this.position, type) }
        }

        override fun getItemCount(): Int {
            return categoryList.size
        }

        fun setType(type: Int){
            this.type = type
        }

        fun updatePosition(position: Int){
            this.position = position
        }

        fun submitList(list: List<DynamicIconComponent.DynamicIcon>){
            categoryList.clear()
            categoryList.addAll(list)
        }
    }

    internal class DynamicIconItemViewHolder(itemView: View, private val listener: DynamicIconComponentListener): RecyclerView.ViewHolder(itemView){
        companion object{
            @LayoutRes
            val LAYOUT = R.layout.home_component_dynamic_icon_item
        }

        fun bind(item: DynamicIconComponent.DynamicIcon, isScrollable: Boolean, parentPosition: Int, type: Int){
            itemView.dynamic_icon_typography.text = item.name
            itemView.dynamic_icon_image_view.loadImage(item.imageUrl)
            itemView.dynamic_icon_container.layoutParams = ViewGroup.LayoutParams(
                    if(isScrollable) ViewGroup.LayoutParams.WRAP_CONTENT else ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
            itemView.dynamic_icon_typography.maxLines = if(item.withBackground) 2 else 1
            itemView.dynamic_icon_background.visibility = if(item.withBackground) View.VISIBLE else View.GONE
            itemView.setOnClickListener {
                listener.onClickIcon(item, parentPosition,adapterPosition, type)
            }
        }
    }

}