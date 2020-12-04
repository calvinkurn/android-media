package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavVisitable
import java.lang.UnsupportedOperationException

/**
 * Created by Lukas on 20/10/20.
 */
class MainNavListAdapter(val mainNavTypeFactoryImpl: MainNavTypeFactoryImpl): ListAdapter<Visitable<*>, AbstractViewHolder<Visitable<*>>>(MainNavDiffCallback) {

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty()){
            holder.bind(getItem(position), payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        if (position < itemCount) {
            holder.bind(getItem(position))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return mainNavTypeFactoryImpl.createViewHolder(view, viewType) as AbstractViewHolder<Visitable<*>>
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position) is HomeNavVisitable) {
            return (getItem(position) as HomeNavVisitable).type(mainNavTypeFactoryImpl)
        }
        if (getItem(position) is MainNavVisitable) {
            return (getItem(position) as MainNavVisitable).type(mainNavTypeFactoryImpl)
        }
        throw UnsupportedOperationException("No view matching with viewtype")
    }
}