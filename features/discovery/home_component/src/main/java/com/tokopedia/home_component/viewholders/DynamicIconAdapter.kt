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
class DynamicIconAdapter(private val listener: DynamicIconComponentListener, private val isRevamp: Boolean = false) : RecyclerView.Adapter<DynamicIconItemViewHolder>() {
    private val iconList = mutableListOf<DynamicIconComponent.DynamicIcon>()
    private var position: Int = 0
    private var type: Int = 1
    private var isCache: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicIconItemViewHolder {
        return DynamicIconItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                if (isRevamp) DynamicIconItemViewHolder.LAYOUT_REVAMP else DynamicIconItemViewHolder.LAYOUT,
                parent,
                false
            ),
            listener,
            isRevamp
        )
    }

    override fun onBindViewHolder(holder: DynamicIconItemViewHolder, position: Int) {
        val isScrollable =
            if (isRevamp && iconList.size > DynamicIconViewHolder.SCROLLABLE_ITEM_REVAMP) true
            else if (isRevamp && iconList.size <= DynamicIconViewHolder.SCROLLABLE_ITEM_REVAMP) false
            else iconList.size > DynamicIconViewHolder.SCROLLABLE_ITEM
        iconList.getOrNull(position)?.let {
            holder
                .bind(it, isScrollable, this.position, type, isCache)
        }
    }

    override fun getItemCount(): Int {
        return iconList.size
    }

    fun setType(type: Int) {
        this.type = type
    }

    fun updatePosition(position: Int) {
        this.position = position
    }

    fun submitList(list: DynamicIconComponentDataModel) {
        iconList.clear()
        iconList.addAll(list.dynamicIconComponent.dynamicIcon)
        this.isCache = list.isCache
    }
}
