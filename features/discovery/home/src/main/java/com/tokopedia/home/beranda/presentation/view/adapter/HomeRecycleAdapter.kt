package com.tokopedia.home.beranda.presentation.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ReviewViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeolocationPromptViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.RetryModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.PlayCardViewHolder
import com.tokopedia.home.beranda.presentation.view.helper.TokopediaPlayerHelper
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedViewModel
import timber.log.Timber
import java.util.*

class HomeRecycleAdapter(private val adapterTypeFactory: HomeAdapterFactory, visitables: List<Visitable<*>>) : BaseAdapter<HomeAdapterFactory>(adapterTypeFactory, visitables), LifecycleObserver{

   companion object{
       //without ticker
       const val POSITION_GEOLOCATION_WITHOUT_TICKER = 3
       const val POSITION_HEADER_WITHOUT_TICKER = 1

       //with ticker
       const val POSITION_GEOLOCATION_WITH_TICKER = 4
       const val POSITION_HEADER_WITH_TICKER = 2

       const val POSITION_UNDEFINED = -1

   }
   private var mRecyclerView: RecyclerView? = null
   private var currentSelected = -1
   private var isFirstItemPlayed = false
   private var mLayoutManager: LinearLayoutManager? = null
   private val retryModel: RetryModel = RetryModel()
   private val listPlay = mutableMapOf<Visitable<*>, Int>()

    init{
        Timber.plant(Timber.DebugTree())
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
            @SuppressLint("SyntheticAccessor")
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //find first index visible on screen
                    val firstIndexVisible = mLayoutManager?.findFirstVisibleItemPosition() ?: -1
                    val lastIndexVisible = mLayoutManager?.findLastVisibleItemPosition() ?: -1
                    val positions = getPositionPlay().filter { it in firstIndexVisible..lastIndexVisible }

                    // check if the view is completely visible on first item
                    if (firstIndexVisible != -1 &&
                            positions.isNotEmpty() &&
                            positions.first() != currentSelected &&
                            (getViewHolder(positions.first()) as PlayCardViewHolder).wantsToPlay()
                    ) {
                        onSelectedItemChanged(positions.first())
                    } else {
                        //if not any visible play, will reset
                        onSelectedItemChanged(-1)
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
        clearExoPlayer()
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

    fun getRecommendationFeedSectionPosition(): Int {
        return visitables.size - 1
    }

    private fun clearExoPlayer(){
        currentSelected = -1
        listPlay.clear()
    }

    fun setPlayData(playContentBanner: PlayChannel?, adapterPosition: Int) {
        if (visitables[adapterPosition] is PlayCardViewModel) {
            (visitables[adapterPosition] as PlayCardViewModel).setPlayCardHome(playContentBanner!!)
        }
        notifyItemChanged(adapterPosition)
    }

    private fun onSelectedItemChanged(newSelected: Int) {
        if(currentSelected != -1) {
            pausePlayerByPosition(currentSelected)
        }
        if(newSelected != -1) {
            prepareAndPlayByPosition(newSelected)
        }
        currentSelected = newSelected
    }

    private fun prepareAndPlayByPosition(position: Int) {
        val newPlayer: TokopediaPlayerHelper? = getExoPlayerByPosition(position)
        if (newPlayer != null) {
            newPlayer.preparePlayer()
            newPlayer.playerPlay()
        }
    }

    private fun pausePlayerByPosition(position: Int) {
        getExoPlayerByPosition(position)?.playerPause()
    }

    private fun getExoPlayerByPosition(firstVisible: Int): TokopediaPlayerHelper? {
        val holder: AbstractViewHolder<out Visitable<*>>? = getViewHolder(firstVisible)
        return if (holder != null && holder is PlayCardViewHolder) {
            holder.getHelper()
        } else {
            null
        }
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewDetachedFromWindow(holder)
        if(holder is PlayCardViewHolder) {
            holder.getHelper()?.playerPause()
        }
    }

    private fun getViewHolder(position: Int): AbstractViewHolder<out Visitable<*>>? {
        return mRecyclerView?.findViewHolderForAdapterPosition(position) as AbstractViewHolder<out Visitable<*>>?
    }

    private fun getPositionPlay(): List<Int>{
        val list = mutableListOf<Int>()
        for (i in visitables.indices) {
            if(visitables[i] is PlayCardViewModel) list.add(i)
        }
        return list
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

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected fun onStop() {
        Timber.tag(HomeRecycleAdapter::class.java.name).i("onActivityDestroy")
        for (exoPlayerHelper in getAllExoPlayers()) {
            exoPlayerHelper.onActivityStop()
        }
    }
}