package com.tokopedia.feedplus.view.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory
import com.tokopedia.feedplus.view.util.EndlessScrollRecycleListener
import com.tokopedia.feedplus.view.viewmodel.EmptyFeedBeforeLoginModel
import com.tokopedia.feedplus.view.viewmodel.RetryModel

import java.util.ArrayList

/**
 * @author by nisie on 5/15/17.
 */

class FeedPlusAdapter(private val typeFactory: FeedPlusTypeFactory) : RecyclerView.Adapter<AbstractViewHolder<*>>() {

    private var list: MutableList<Visitable<*>> = mutableListOf()
    private val emptyModel: EmptyModel
    private val emptyFeedBeforeLoginModel: EmptyFeedBeforeLoginModel
    private val loadingMoreModel: LoadingMoreModel
    private val retryModel: RetryModel
    private var unsetListener: Boolean = false
    private var loadListener: OnLoadListener? = null
    private var recyclerView: RecyclerView? = null
    var itemTreshold = 5

    private val endlessScrollListener = object : EndlessScrollRecycleListener() {
        override fun onLoadMore(page: Int, totalItemsCount: Int) {
            if (isLoading)
                return
            if (loadListener != null && !unsetListener && list.size > itemTreshold) {
                showLoading()
                loadListener!!.onLoad(totalItemsCount)
            }
        }

        override fun onScroll(lastVisiblePosition: Int) {
            if (loadListener is OnScrollListener)
                (loadListener as OnScrollListener).onScroll(lastVisiblePosition)
        }
    }

    val isLoading: Boolean
        get() = this.list.contains(loadingMoreModel)

    init {
        this.emptyModel = EmptyModel()
        this.loadingMoreModel = LoadingMoreModel()
        this.retryModel = RetryModel()
        this.emptyFeedBeforeLoginModel = EmptyFeedBeforeLoginModel()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        holder.bind(list[position])
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int,
                                  payloads: List<Any>) {
        if (!payloads.isEmpty()) {
            holder.bind(list!![position], payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list!![position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    private fun add(visitable: Visitable<*>) {
        val position = this.list!!.size
        if (this.list!!.add(visitable)) {
            notifyItemInserted(position)
        }
    }

    private fun remove(visitable: Visitable<*>) {
        val position = this.list!!.indexOf(visitable)
        if (this.list!!.remove(visitable)) {
            notifyItemRemoved(position)
        }
    }

    fun setList(list: MutableList<Visitable<*>>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun addList(list: List<Visitable<*>>) {
        val positionStart = itemCount
        if (this.list!!.addAll(list)) {
            notifyItemRangeInserted(positionStart, list.size)
        }
    }

    fun clearData() {
        this.list!!.clear()
        notifyDataSetChanged()
    }

    fun showEmpty() {
        add(emptyModel)
    }

    fun removeEmpty() {
        remove(emptyModel)
    }

    fun showRetry() {
        add(retryModel)
    }

    fun removeRetry() {
        remove(retryModel)
    }

    fun showLoading() {
        val removePosition = this.list!!.indexOf(loadingMoreModel)
        if (removePosition != -1) remove(loadingMoreModel)
        add(loadingMoreModel)
    }

    fun removeLoading() {
        remove(loadingMoreModel)
    }

    fun getlist(): List<Visitable<*>>? {
        return list
    }

    fun addItem(item: Visitable<*>) {
        add(item)
    }

    fun showUserNotLogin() {
        add(emptyFeedBeforeLoginModel)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        this.recyclerView!!.itemAnimator = null
        setEndlessScrollListener()
    }

    override fun onViewRecycled(holder: AbstractViewHolder<*>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    fun setOnLoadListener(loadListener: OnLoadListener) {
        this.loadListener = loadListener
    }

    fun setEndlessScrollListener() {
        unsetListener = false
        recyclerView!!.addOnScrollListener(endlessScrollListener)
    }

    fun unsetEndlessScrollListener() {
        unsetListener = true
        recyclerView!!.removeOnScrollListener(endlessScrollListener)
    }

    interface OnLoadListener {
        fun onLoad(totalCount: Int)

    }

    interface OnScrollListener : OnLoadListener {
        fun onScroll(lastVisiblePosition: Int)

    }
}
