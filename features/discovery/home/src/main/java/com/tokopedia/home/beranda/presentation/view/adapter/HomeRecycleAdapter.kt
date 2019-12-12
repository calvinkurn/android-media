package com.tokopedia.home.beranda.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.dynamicbanner.entity.PlayCardHome
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ReviewViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeolocationPromptViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.RetryModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.PlayCardViewHolder
import com.tokopedia.home.beranda.presentation.view.helper.TokopediaPlayerHelper
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedViewModel
import timber.log.Timber
import java.util.*

class HomeRecycleAdapter(private val adapterTypeFactory: HomeAdapterFactory, visitables: List<Visitable<*>>) : BaseAdapter<HomeAdapterFactory>(adapterTypeFactory, visitables), LifecycleObserver, View.OnClickListener{

   companion object{
       //without ticker
       val POSITION_GEOLOCATION_WITHOUT_TICKER = 3
       val POSITION_HEADER_WITHOUT_TICKER = 1

       //with ticker
       val POSITION_GEOLOCATION_WITH_TICKER = 4
       val POSITION_HEADER_WITH_TICKER = 2

       val POSITION_UNDEFINED = -1
       private var mRecyclerView: RecyclerView? = null
       private var isFirstItemPlayed = false
       private var currentSelected = -1

       private var mLayoutManager: LinearLayoutManager? = null

       private val retryModel: RetryModel = RetryModel()
   }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return adapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        holder.bind(visitables[position])
        //check if visitable is homerecommendation, we will set newData = false after bind
        //because newData = true will force viewholder to recreate tab and viewpager
        if (visitables[position] is HomeRecommendationFeedViewModel) {
            (visitables[position] as HomeRecommendationFeedViewModel).isNewData = false
        }
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int, payloads: List<Any>) {
        if (payloads.isNotEmpty()) holder.bind(visitables[position], payloads) else super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemViewType(position: Int): Int {
        return visitables[position].type(adapterTypeFactory)
    }

    override fun getItemCount(): Int {
        return visitables.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
        mLayoutManager = mRecyclerView?.layoutManager as LinearLayoutManager
        mRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    val firstVisible = layoutManager?.findFirstVisibleItemPosition() ?: -1
                    val lastVisitable = layoutManager?.findLastVisibleItemPosition() ?: -1
                    visitables.subList(firstVisible, lastVisitable).withIndex().filter { (index, visitable) ->  visitable is PlayCardViewModel }.map{(index, _) ->
                        if (index != currentSelected && visitables[index] is PlayCardViewModel) {
                            onSelectedItemChanged(index)
                        }
                        index
                    }.also {
                        if(it.isEmpty()) onSelectedItemChanged(-1)
                    }
                }
            }
        })
    }

    fun getItem(pos: Int): Visitable<*>? {
        return visitables[pos]
    }

    fun getItems(): List<Visitable<*>?> {
        return visitables
    }

    fun clearItems() {
        visitables.clear()
    }

    fun showRetry() {
        if (visitables.contains(retryModel)) {
            return
        }
        val positionStart = itemCount
        visitables.add(retryModel)
        notifyItemRangeInserted(positionStart, 1)
    }

    fun removeRetry() {
        val index = visitables.indexOf(retryModel)
        visitables.remove(retryModel)
        notifyItemRemoved(index)
    }

    //mapping another visitable to visitables from home_query
    fun setItems(visitables: List<Visitable<*>>) {
        this.visitables = visitables
        notifyDataSetChanged()
    }

    fun hasReview(): Int {
        for (i in visitables.indices) {
            if (visitables[i] is ReviewViewModel) {
                return i
            }
        }
        return -1
    }

    fun updateReviewItem(suggestedProductReview: SuggestedProductReview?) {
        if (visitables[hasReview()] is ReviewViewModel && hasReview() != -1) {
            (visitables[hasReview()] as ReviewViewModel).suggestedProductReview = suggestedProductReview!!
            notifyItemChanged(hasReview())
        }
    }

    fun updateHomeQueryItems(newVisitable: List<Visitable<*>?>?) {
        clearItems()
        visitables = newVisitable
        notifyDataSetChanged()
    }

    fun removeGeolocationViewModel() {
        val removedPosition = removeGeolocation()
        if (removedPosition != -1) {
            notifyItemRemoved(removedPosition)
        }
    }

    fun removeReviewViewModel() {
        val reviewPosition = removeReview()
        if (reviewPosition != -1) {
            notifyItemRemoved(reviewPosition)
        }
    }

    fun setHomeHeaderViewModel(homeHeaderViewModel: HeaderViewModel) {
        val changedPosition = setHomeHeader(homeHeaderViewModel)
        if (changedPosition != POSITION_UNDEFINED) {
            notifyItemChanged(changedPosition)
        }
    }

    fun addHomeHeaderViewModel(homeHeaderViewModel: HeaderViewModel) {
        val addedPosition = addHomeHeader(homeHeaderViewModel)
        if (addedPosition != POSITION_UNDEFINED) {
            notifyItemInserted(addedPosition)
        }
    }

    fun setGeolocationViewModel(geolocationViewModel: GeolocationPromptViewModel) {
        val addedPosition = setGeolocation(geolocationViewModel)
        notifyItemInserted(addedPosition)
    }

    private fun hasTicker(): Boolean {
        return getItems().size > 1 && getItems()[1] is TickerViewModel
    }

    private fun hasHomeHeaderViewModel(): Int {
        return if (visitables != null && visitables.size > 0) {
            if (visitables.size > POSITION_HEADER_WITHOUT_TICKER &&
                    visitables[POSITION_HEADER_WITHOUT_TICKER] is HeaderViewModel) {
                POSITION_HEADER_WITHOUT_TICKER
            } else if (visitables.size > POSITION_HEADER_WITH_TICKER &&
                    visitables[POSITION_HEADER_WITH_TICKER] is HeaderViewModel) {
                POSITION_HEADER_WITH_TICKER
            } else {
                POSITION_UNDEFINED
            }
        } else POSITION_UNDEFINED
    }

    private fun hasGeolocationViewModel(): Int {
        return if (visitables != null && visitables.size > 0) {
            if (visitables.size > POSITION_GEOLOCATION_WITHOUT_TICKER &&
                    visitables[POSITION_GEOLOCATION_WITHOUT_TICKER] is GeolocationPromptViewModel) {
                POSITION_GEOLOCATION_WITHOUT_TICKER
            } else if (visitables.size > POSITION_GEOLOCATION_WITH_TICKER &&
                    visitables[POSITION_GEOLOCATION_WITH_TICKER] is GeolocationPromptViewModel) {
                POSITION_GEOLOCATION_WITH_TICKER
            } else {
                POSITION_UNDEFINED
            }
        } else POSITION_UNDEFINED
    }

    private fun removeGeolocation(): Int {
        for (i in visitables.indices) {
            if (visitables[i] is GeolocationPromptViewModel) {
                visitables.removeAt(i)
                return i
            }
        }
        return POSITION_UNDEFINED
    }

    private fun removeReview(): Int {
        for (i in visitables.indices) {
            if (visitables[i] is ReviewViewModel) {
                visitables.removeAt(i)
                return i
            }
        }
        return POSITION_UNDEFINED
    }

    private fun setGeolocation(geolocationPromptViewModel: GeolocationPromptViewModel): Int {
        when (hasGeolocationViewModel()) {
            POSITION_GEOLOCATION_WITH_TICKER -> {
                visitables[POSITION_GEOLOCATION_WITH_TICKER] = geolocationPromptViewModel
                return POSITION_GEOLOCATION_WITH_TICKER
            }
            POSITION_GEOLOCATION_WITHOUT_TICKER -> {
                visitables[POSITION_GEOLOCATION_WITHOUT_TICKER] = geolocationPromptViewModel
                return POSITION_GEOLOCATION_WITHOUT_TICKER
            }
            POSITION_UNDEFINED -> {
                return if (hasTicker()) {
                    visitables.add(
                            POSITION_GEOLOCATION_WITH_TICKER,
                            geolocationPromptViewModel
                    )
                    POSITION_HEADER_WITH_TICKER
                } else {
                    visitables.add(
                            POSITION_GEOLOCATION_WITHOUT_TICKER,
                            geolocationPromptViewModel
                    )
                    POSITION_GEOLOCATION_WITHOUT_TICKER
                }
            }
        }
        return POSITION_UNDEFINED
    }

    //update and return updated position
    private fun setHomeHeader(homeHeaderViewModel: HeaderViewModel): Int {
        when (hasHomeHeaderViewModel()) {
            POSITION_HEADER_WITH_TICKER -> {
                visitables[POSITION_HEADER_WITH_TICKER] = homeHeaderViewModel
                return POSITION_HEADER_WITH_TICKER
            }
            POSITION_HEADER_WITHOUT_TICKER -> {
                visitables[POSITION_HEADER_WITHOUT_TICKER] = homeHeaderViewModel
                return POSITION_HEADER_WITHOUT_TICKER
            }
        }
        return POSITION_UNDEFINED
    }

    private fun addHomeHeader(homeHeaderViewModel: HeaderViewModel): Int {
        return if (hasTicker()) {
            visitables.add(POSITION_HEADER_WITH_TICKER, homeHeaderViewModel)
            POSITION_HEADER_WITH_TICKER
        } else {
            visitables.add(POSITION_HEADER_WITHOUT_TICKER, homeHeaderViewModel)
            POSITION_HEADER_WITHOUT_TICKER
        }
    }

    fun getRecommendationFeedSectionPosition(): Int {
        return visitables.size - 1
    }

    fun isRetryShown(): Boolean {
        return visitables.contains(retryModel)
    }

    fun setPlayData(playContentBanner: PlayCardHome?, adapterPosition: Int) {
        if (visitables[adapterPosition] is PlayCardViewModel) {
            (visitables[adapterPosition] as PlayCardViewModel).setPlayCardHome(playContentBanner!!)
        }
        notifyItemChanged(adapterPosition)
    }

    private fun onSelectedItemChanged(newSelected: Int) {
        if(newSelected != -1) {
            if(currentSelected != -1) {
//                changeAlphaToVisible(currentSelected, false)
                pausePlayerByPosition(currentSelected)
//                blockPlayerByPosition(currentSelected)
            }
            //---------
//            changeAlphaToVisible(newSelected, true)
            prepareAndPlayByPosition(newSelected)
//            unBlockPlayerByPosition(newSelected)
        }
        currentSelected = newSelected
    }


    private fun unBlockPlayerByPosition(newSelected: Int) {
        val viewHolder: AbstractViewHolder<out Visitable<*>>?  = getViewHolder(newSelected)
        if (viewHolder != null && viewHolder is PlayCardViewHolder) {
//            viewHolder.helper?.playerUnBlock()
        }
    }

    private fun prepareAndPlayByPosition(position: Int) {
        val newPlayer: TokopediaPlayerHelper? = getExoPlayerByPosition(position)
        if (newPlayer != null) {
            newPlayer.preparePlayer()
            newPlayer.playerPlay()
        }
    }

    private fun blockPlayerByPosition(position: Int) {
        val viewHolder: AbstractViewHolder<out Visitable<*>>?  = getViewHolder(position)
        if (viewHolder != null && viewHolder is PlayCardViewHolder) {
            viewHolder.helper?.playerBlock()
        }
    }

    private fun pausePlayerByPosition(position: Int) {
        getExoPlayerByPosition(position)?.playerPause()
    }

    private fun changeAlphaToVisible(position: Int, isVisible: Boolean) {
        val viewHolder: AbstractViewHolder<out Visitable<*>>?  = getViewHolder(position)
        if (viewHolder != null && viewHolder is PlayCardViewHolder) {
            viewHolder.itemView.alpha = if (isVisible) 1.0f else 0.2f
        }
    }

    private fun getExoPlayerByPosition(firstVisible: Int): TokopediaPlayerHelper? {
        val holder: AbstractViewHolder<out Visitable<*>>? = getViewHolder(firstVisible)
        return if (holder != null && holder is PlayCardViewHolder) {
            holder.helper
        } else {
            null
        }
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
        if(holder is PlayCardViewHolder) {
            holder.createHelper()
            if (!isFirstItemPlayed && currentSelected == -1) {
                isFirstItemPlayed = true
                currentSelected = holder.adapterPosition
                holder.helper?.preparePlayer()
                holder.helper?.playerPlay()
//                holder.helper?.playerUnBlock()
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewDetachedFromWindow(holder)
        if(holder is PlayCardViewHolder) {
            holder.releaseExoPlayerCalled()
        }
    }

    override fun onClick(v: View?) {

    }


    private fun getViewHolder(position: Int): AbstractViewHolder<out Visitable<*>>? {
        return mRecyclerView?.findViewHolderForAdapterPosition(position) as AbstractViewHolder<out Visitable<*>>?
    }


    private fun getAllExoPlayers(): ArrayList<TokopediaPlayerHelper> {
        val list: ArrayList<TokopediaPlayerHelper> = ArrayList()
        for (i in visitables.indices) {
            val exoPlayerHelper: TokopediaPlayerHelper? = getExoPlayerByPosition(i)
            if (exoPlayerHelper != null) {
                list.add(exoPlayerHelper)
            }
        }
        return list
    }

    // Activity LifeCycle
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected fun onStart() {
        Timber.tag(HomeRecycleAdapter::class.java.name).i("onActivityStart")
        for (exoPlayerHelper in getAllExoPlayers()) {
            exoPlayerHelper.onActivityStart()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected fun onResume() {
        Timber.tag(HomeRecycleAdapter::class.java.name).i("onActivityResume")
        for (exoPlayerHelper in getAllExoPlayers()) {
            exoPlayerHelper.onActivityResume()
        }
        val newPlayer: TokopediaPlayerHelper? = getExoPlayerByPosition(currentSelected)
        if (newPlayer != null) {
            newPlayer.preparePlayer()
            newPlayer.playerPlay()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected fun onPause() {
        Timber.tag(HomeRecycleAdapter::class.java.name).i("onActivityPause")
        for (exoPlayerHelper in getAllExoPlayers()) {
            exoPlayerHelper.onActivityPause()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected fun onStop() {
        Timber.tag(HomeRecycleAdapter::class.java.name).i("onActivityStop")
        for (exoPlayerHelper in getAllExoPlayers()) {
            exoPlayerHelper.onActivityStop()
        }
    }

}