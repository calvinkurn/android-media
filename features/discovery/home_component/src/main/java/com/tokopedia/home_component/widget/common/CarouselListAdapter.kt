package com.tokopedia.home_component.widget.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.home_component.util.recordCrashlytics

/**
 * Created by frenzel
 */
internal class CarouselListAdapter<T: Visitable<F>, F: AdapterTypeFactory>(
    private val typeFactory: F,
    diffUtil: DiffUtil.ItemCallback<T>,
): ListAdapter<T, AbstractViewHolder<in T>>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<in T> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<in T>, position: Int) {
        try {
            val item = getItem(position)
            holder.bind(item)
        } catch (e: Exception) {
            e.recordCrashlytics()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return try {
            if (position < 0 || position >= itemCount) {
                HideViewHolder.LAYOUT
            } else getItem(position).type(typeFactory)
        } catch (e: Exception) {
            e.recordCrashlytics()
            HideViewHolder.LAYOUT
        }
    }
}
