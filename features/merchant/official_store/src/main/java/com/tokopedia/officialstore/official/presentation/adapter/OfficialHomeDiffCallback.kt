package com.tokopedia.officialstore.official.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialHomeVisitable

/**
 * Created by Lukas on 27/10/20.
 */
object OfficialDiffCallback : DiffUtil.ItemCallback<Visitable<*>>(){
    override fun areItemsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return if (oldItem is OfficialHomeVisitable && newItem is OfficialHomeVisitable) {
            oldItem.visitableId() == newItem.visitableId()
        } else if (oldItem is HomeComponentVisitable && newItem is HomeComponentVisitable) {
            oldItem.visitableId() == newItem.visitableId()
        } else false
    }

    override fun areContentsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return if (oldItem is OfficialHomeVisitable && newItem is OfficialHomeVisitable) {
            oldItem.equalsWith(newItem)
        } else if (oldItem is HomeComponentVisitable && newItem is HomeComponentVisitable) {
            oldItem.equalsWith(newItem)
        } else false
    }

    override fun getChangePayload(oldItem: Visitable<*>, newItem: Visitable<*>): Any? {
        return if (oldItem is OfficialHomeVisitable && newItem is OfficialHomeVisitable) {
            oldItem.getChangePayloadFrom(newItem)
        } else if (oldItem is HomeComponentVisitable && newItem is HomeComponentVisitable) {
            oldItem.getChangePayloadFrom(newItem)
        } else false
    }
}