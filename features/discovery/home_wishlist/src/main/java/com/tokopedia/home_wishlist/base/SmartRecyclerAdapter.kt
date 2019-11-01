package com.tokopedia.home_wishlist.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactoryImpl

abstract class SmartRecyclerAdapter<T, V : WishlistTypeFactoryImpl>(
        appExecutors: SmartExecutors,
        private val adapterTypeFactory: V,
        diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, SmartAbstractViewHolder<*>>(
        AsyncDifferConfig.Builder<T>(diffCallback)
                .setBackgroundThreadExecutor(appExecutors.diskIO())
                .build()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartAbstractViewHolder<*> {
        val view = onCreateViewItem(parent, viewType)
        return adapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: SmartAbstractViewHolder<*>, position: Int, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty()){
            bind(holder as SmartAbstractViewHolder<Visitable<*>>, getItem(position), payloads)
        }else{
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    private fun onCreateViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    }

    override fun onBindViewHolder(holder: SmartAbstractViewHolder<*>, position: Int) {
        bind(holder as SmartAbstractViewHolder<Visitable<*>>, getItem(position))
    }

    protected abstract fun bind(visitable: SmartAbstractViewHolder<Visitable<*>>, item: T)

    protected abstract fun bind(visitable: SmartAbstractViewHolder<Visitable<*>>, item: T, payloads: MutableList<Any>)


    abstract override fun getItemViewType(position: Int): Int
}