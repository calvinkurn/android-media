package com.tokopedia.feedplus.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.shimmer.ShimmerUiModel
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory
import com.tokopedia.feedplus.view.util.EndlessScrollRecycleListener
import com.tokopedia.feedplus.view.util.FeedDiffUtilCallback
import com.tokopedia.feedplus.view.viewmodel.RetryModel

/**
 * @author by nisie on 5/15/17.
 */

class FeedPlusAdapter(private val typeFactory: FeedPlusTypeFactory, val loadListener: OnLoadListener) : RecyclerView.Adapter<AbstractViewHolder<Visitable<*>>>() {

    private var list: MutableList<Visitable<*>> = mutableListOf()
    private val emptyModel: EmptyModel = EmptyModel()
    private val loadingMoreModel: LoadingMoreModel = LoadingMoreModel()
    private val retryModel: RetryModel = RetryModel()
    private var unsetListener: Boolean = false
    private var recyclerView: RecyclerView? = null
    var itemTreshold = 5

    private val endlessScrollListener = object : EndlessScrollRecycleListener() {
        override fun onLoadMore(page: Int, totalItemsCount: Int) {
            if (isLoading)
                return
            if (!unsetListener && list.size > itemTreshold) {
                showLoading()
                loadListener.onLoad(totalItemsCount)
            }
        }

        override fun onScroll(lastVisiblePosition: Int) {
            if (loadListener is OnScrollListener)
                loadListener.onScroll(lastVisiblePosition)
        }
    }

    val isLoading: Boolean
        get() = this.list.contains(loadingMoreModel)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>>  {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)

        @Suppress("UNCHECKED_CAST")
        return typeFactory.createViewHolder(view, viewType) as AbstractViewHolder<Visitable<*>>
    }

    override fun getItemViewType(position: Int): Int {
        @Suppress("UNCHECKED_CAST")
        val data = list[position] as Visitable<FeedPlusTypeFactory>

        return data.type(typeFactory)
    }
    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>> , position: Int) {
        holder.bind(list[position])
    }

    override fun onBindViewHolder(holder:  AbstractViewHolder<Visitable<*>> , position: Int,
                                  payloads: List<Any>) {
        if (!payloads.isEmpty()) {
            holder.bind(list[position], payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun add(visitable: Visitable<*>) {
       addList(mutableListOf(visitable))
    }

    fun addList(newLists: List<Visitable<*>>) {
        val newList: MutableList<Visitable<*>> = mutableListOf()
        newList.addAll(list)
        newList.addAll(newLists)
        updateList(newList)
    }

    private fun remove(visitable: Visitable<*>) {
        val newList: MutableList<Visitable<*>> = mutableListOf()
        newList.addAll(list)
        newList.remove(visitable)
        updateList(newList)

    }

    fun clearData() {
        updateList(mutableListOf())
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
        val removePosition = this.list.indexOf(loadingMoreModel)
        if (removePosition != -1) remove(loadingMoreModel)
        add(loadingMoreModel)
    }

    fun removeLoading() {
        remove(loadingMoreModel)
    }

    fun getlist(): MutableList<Visitable<*>> {
        return list
    }

    fun getList(): ArrayList<Visitable<*>> {
        return ArrayList(list)
    }

    fun addItem(item: Visitable<*>) {
        add(item)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        this.recyclerView!!.itemAnimator = null
        setEndlessScrollListener()
    }

    override fun onViewRecycled(holder:  AbstractViewHolder<Visitable<*>> ) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    fun setEndlessScrollListener() {
        unsetListener = false
        recyclerView!!.addOnScrollListener(endlessScrollListener)
    }

    fun unsetEndlessScrollListener() {
        unsetListener = true
        recyclerView!!.removeOnScrollListener(endlessScrollListener)
    }

    fun updateList(newList: List<Visitable<*>>) {
        val diffResult = DiffUtil.calculateDiff(FeedDiffUtilCallback(list, newList))

        list.clear()
        list.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

    fun removePlayWidget() {
        val playCarousel = list.firstOrNull { it is CarouselPlayCardViewModel }
        if (playCarousel != null) remove(playCarousel)
    }

    fun updatePlayWidget(newModel: CarouselPlayCardViewModel) {
        val newList = list.map {
            if (it is CarouselPlayCardViewModel) newModel
            else it
        }
        updateList(newList)
    }

    fun showShimmer() {
        val shimmerItems: ArrayList<ShimmerUiModel> = ArrayList()
        repeat(5) { shimmerItems.add(ShimmerUiModel()) }
        updateList(shimmerItems)
    }

    fun removeShimmer(){
        clearData()
    }

    interface OnLoadListener {
        fun onLoad(totalCount: Int)

    }

    interface OnScrollListener : OnLoadListener {
        fun onScroll(lastVisiblePosition: Int)
    }
}
