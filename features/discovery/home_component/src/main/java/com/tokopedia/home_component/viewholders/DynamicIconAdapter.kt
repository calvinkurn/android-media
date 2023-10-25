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
class DynamicIconAdapter(private val listener: DynamicIconComponentListener) : RecyclerView.Adapter<DynamicIconBigItemViewHolder>() {
    private val iconList = mutableListOf<DynamicIconComponent.DynamicIcon>()
    private var position: Int = 0
    private var isCache: Boolean = false
    private var isScrollable = false
    private var type: DynamicIconComponentDataModel.Type = DynamicIconComponentDataModel.Type.BIG

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicIconBigItemViewHolder {
        val layout = if(this.type == DynamicIconComponentDataModel.Type.SMALL) {
            DynamicIconSmallItemViewHolder.LAYOUT
        } else {
            DynamicIconBigItemViewHolder.LAYOUT
        }
        return DynamicIconBigItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layout,
                parent,
                false
            ),
            listener,
        )
    }

    override fun onBindViewHolder(holder: DynamicIconBigItemViewHolder, position: Int) {
        iconList.getOrNull(position)?.let {
            holder.bind(it, isScrollable, this.position, isCache)
        }
    }

    override fun getItemCount(): Int {
        return iconList.size
    }

    fun updatePosition(position: Int) {
        this.position = position
    }

    fun submitList(list: DynamicIconComponentDataModel) {
        this.isCache = list.isCache
        this.isScrollable = list.dynamicIconComponent.dynamicIcon.size > list.scrollableItemThreshold
        this.type = list.type
        iconList.clear()
        iconList.addAll(list.dynamicIconComponent.dynamicIcon)
    }
}
