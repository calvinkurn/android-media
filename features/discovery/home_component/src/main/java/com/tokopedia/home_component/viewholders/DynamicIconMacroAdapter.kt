package com.tokopedia.home_component.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.listener.DynamicIconComponentListener
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel

/**
 * Created by dhaba
 */
class DynamicIconMacroAdapter(private val listener: DynamicIconComponentListener) : RecyclerView.Adapter<DynamicIconMacroItemViewHolder>() {
    private val categoryList = mutableListOf<DynamicIconComponent.DynamicIcon>()
    private var position: Int = 0
    private var type: Int = 1
    private var isCache: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicIconMacroItemViewHolder {
        return DynamicIconMacroItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                DynamicIconViewHolder.DynamicIconItemViewHolder.LAYOUT_EXPERIMENT,
                parent,
                false
            ),
            listener
        )
    }

    override fun onBindViewHolder(holder: DynamicIconMacroItemViewHolder, position: Int) {
        categoryList.getOrNull(position)?.let { holder.bind(it, categoryList.size > DynamicIconViewHolder.SCROLLABLE_ITEM, this.position, type, isCache) }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun setType(type: Int) {
        this.type = type
    }

    fun updatePosition(position: Int) {
        this.position = position
    }

    fun submitList(list: DynamicIconComponentDataModel) {
        categoryList.clear()
        categoryList.addAll(list.dynamicIconComponent.dynamicIcon)
        this.isCache = list.isCache
    }
}
