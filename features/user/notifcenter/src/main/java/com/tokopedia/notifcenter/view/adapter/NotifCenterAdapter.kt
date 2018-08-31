package com.tokopedia.notifcenter.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.view.adapter.typefactory.NotifCenterTypeFactory
import javax.inject.Inject

/**
 * @author by milhamj on 30/08/18.
 */

class NotifCenterAdapter @Inject constructor()
    : RecyclerView.Adapter<AbstractViewHolder<Visitable<*>>>() {
    private val list : ArrayList<Visitable<*>> = ArrayList()
    private val loadingMoreModel: LoadingMoreModel = LoadingMoreModel()

    lateinit var typeFactory : NotifCenterTypeFactory

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : AbstractViewHolder<Visitable<*>>
            = parent.context
            .let { LayoutInflater.from(it).inflate(viewType, parent, false) }
            .let { typeFactory.createViewHolder(it, viewType) }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        holder.bind(list[position])
    }

    fun setList(list : List<Visitable<*>>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun addAll(list : List<Visitable<*>>) {
        val position = this.list.size
        this.list.addAll(list)
        notifyItemRangeInserted(position, list.size)
    }

    fun addItem(visitable : Visitable<*>) {
        val position = this.list.size
        if (this.list.add(visitable)) {
            notifyItemInserted(position)
        }
    }

    fun removeItem(visitable : Visitable<*>) {
        val position = this.list.indexOf(visitable)
        if (this.list.remove(visitable)) {
            notifyItemRemoved(position)
        }
    }

    fun clearData() {
        this.list.clear()
        notifyDataSetChanged()
    }

    fun showLoading() {
        addItem(loadingMoreModel)
    }

    fun removeLoading() {
        removeItem(loadingMoreModel)
    }

    fun isLoading() =  this.list.contains(loadingMoreModel)
}
