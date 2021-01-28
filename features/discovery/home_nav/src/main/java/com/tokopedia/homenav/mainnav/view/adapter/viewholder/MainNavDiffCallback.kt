package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.mainnav.view.datamodel.MainNavVisitable

/**
 * Created by Lukas on 20/10/20.
 */

object MainNavDiffCallback : DiffUtil.ItemCallback<Visitable<*>>() {
    override fun areItemsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        if (oldItem is HomeNavVisitable && newItem is HomeNavVisitable) {
            return oldItem.id() == newItem.id()
        }

        if (oldItem is MainNavVisitable && newItem is MainNavVisitable) {
            return oldItem.id() == newItem.id()
        }

        return false
    }

    override fun areContentsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        if (oldItem is HomeNavVisitable && newItem is HomeNavVisitable) {
            return oldItem.isContentTheSame(newItem)
        }

        if (oldItem is MainNavVisitable && newItem is MainNavVisitable) {
            return oldItem.isContentTheSame(newItem)
        }

        return false
    }
}