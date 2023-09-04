package com.tokopedia.home_component.widget.common.carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.util.recordCrashlytics

/**
 * Created by frenzel
 */
internal class CarouselListAdapter<T: Visitable<in CommonCarouselProductCardTypeFactory>>(
    private val typeFactory: CommonCarouselProductCardTypeFactory,
    diffUtil: DiffUtil.ItemCallback<T>,
): ListAdapter<T, AbstractViewHolder<*>>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        try {
            val item = getItem(position)
            (holder as AbstractViewHolder<in T>).bind(item)
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
