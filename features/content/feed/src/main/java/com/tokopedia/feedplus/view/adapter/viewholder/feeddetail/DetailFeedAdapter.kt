package com.tokopedia.feedplus.view.adapter.viewholder.feeddetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactory
import java.util.*

/**
 * @author by nisie on 5/18/17.
 */
class DetailFeedAdapter(typeFactory: FeedPlusDetailTypeFactory) : RecyclerView.Adapter<AbstractViewHolder<*>>() {
    private val list: MutableList<Visitable<*>>
    private val emptyModel: EmptyModel
    private val loadingModel: LoadingModel
    private val loadingMoreModel: LoadingMoreModel
    private val typeFactory: FeedPlusDetailTypeFactory
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        (holder as AbstractViewHolder<Visitable<*>>).bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return (list[position] as Visitable<FeedPlusDetailTypeFactory>).type(typeFactory)
    }

    fun addList(list: MutableList<ProductFeedDetailViewModelNew>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun add(item: Visitable<*>) {
        val position = itemCount
        if (list.add(item)) {
            notifyItemInserted(position)
        }
    }

    fun remove(item: Visitable<*>?) {
        val position = list.indexOf(item)
        if (list.remove(item)) {
            notifyItemRemoved(position)
        }
    }

    fun showEmpty() {
        add(emptyModel)
    }

    fun dismissEmpty() {
        remove(emptyModel)
    }

    fun showLoading() {
        add(loadingModel)
    }

    fun dismissLoading() {
        remove(loadingModel)
    }

    fun showLoadingMore() {
        add(loadingMoreModel)
    }

    fun dismissLoadingMore() {
        remove(loadingMoreModel)
    }

    val isLoading: Boolean
        get() = list.contains(loadingModel)

    fun getList(): List<Visitable<*>> {
        return list
    }

    init {
        list = ArrayList()
        this.typeFactory = typeFactory
        emptyModel = EmptyModel()
        loadingModel = LoadingModel()
        loadingMoreModel = LoadingMoreModel()
    }
}