package com.tokopedia.home.beranda.presentation.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.PlayCardViewHolder
import com.tokopedia.home.beranda.presentation.view.helper.HomePlayWidgetHelper
import java.util.*

class HomeRecycleAdapter(asyncDifferConfig: AsyncDifferConfig<HomeVisitable>, private val adapterTypeFactory: HomeAdapterFactory, visitables: List<Visitable<*>>) :
        HomeBaseAdapter<HomeAdapterFactory>(asyncDifferConfig, adapterTypeFactory, visitables), LifecycleObserver{

   private var mRecyclerView: RecyclerView? = null
   private var currentSelected = -1
   private var mLayoutManager: LinearLayoutManager? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return adapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return (getItem(position) as Visitable<HomeAdapterFactory>).type(adapterTypeFactory)
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
//                    val firstIndexVisible = mLayoutManager?.findFirstVisibleItemPosition() ?: -1
//                    val lastIndexVisible = mLayoutManager?.findLastVisibleItemPosition() ?: -1
//                    val positions = getPositionPlay().filter { it in firstIndexVisible..lastIndexVisible }

                    // check if the view is completely visible on first item
//                    if (firstIndexVisible != -1 &&
//                            positions.isNotEmpty() &&
//                            (positions.first() != currentSelected || currentSelected == -1) && //check if we missing currentSelected
//                            getExoPlayerByPosition(positions.first())?.isPlayerPlaying() == false &&
//                            (getViewHolder(positions.first()) as PlayCardViewHolder).wantsToPlay()
//                    ) {
//                        onSelectedItemChanged(positions.first())
//                    }

//                    if(positions.isEmpty()) onSelectedItemChanged(-1)
                }
            }
        })
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
        val newPlayer: HomePlayWidgetHelper? = getExoPlayerByPosition(position)
        newPlayer?.preparePlayer()
    }

    private fun pausePlayerByPosition(position: Int) {
        getExoPlayerByPosition(position)?.playerPause()
    }

    private fun getExoPlayerByPosition(firstVisible: Int): HomePlayWidgetHelper? {
        val holder: AbstractViewHolder<out Visitable<*>>? = getViewHolder(firstVisible)
        return if (holder != null && holder is PlayCardViewHolder) {
            holder.getHelper()
        } else {
            null
        }
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
        if(holder is PlayCardViewHolder) {
            holder.onViewAttach()
        }
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewDetachedFromWindow(holder)
        if(holder is PlayCardViewHolder) {
            holder.onViewDetach()
        }
    }

    private fun getViewHolder(position: Int): AbstractViewHolder<out Visitable<*>>? {
        return mRecyclerView?.findViewHolderForAdapterPosition(position) as AbstractViewHolder<out Visitable<*>>?
    }

    private fun getPositionPlay(): List<Int>{
        val list = mutableListOf<Int>()
        for (i in currentList.indices) {
            if(getItem(i) is PlayCardViewModel) list.add(i)
        }
        return list
    }

    private fun getAllExoPlayers(): ArrayList<HomePlayWidgetHelper> {
        val list: ArrayList<HomePlayWidgetHelper> = ArrayList()
        for (i in currentList.indices) {
            val exoPlayerHelper: HomePlayWidgetHelper? = getExoPlayerByPosition(i)
            if (exoPlayerHelper != null) {
                list.add(exoPlayerHelper)
            }
        }
        return list
    }

    // Activity LifeCycle

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        val positions = getPositionPlay()
        if(positions.isNotEmpty() && getViewHolder(positions.first()) is PlayCardViewHolder){
            currentSelected = positions.first()
            (getViewHolder(currentSelected) as PlayCardViewHolder).resume()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        val positions = getPositionPlay()
        if(positions.isNotEmpty() && getViewHolder(positions.first()) is PlayCardViewHolder){
            currentSelected = positions.first()
            (getViewHolder(currentSelected) as PlayCardViewHolder).pause()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        for (exoPlayerHelper in getAllExoPlayers()) {
            exoPlayerHelper.onActivityStop()
        }
    }
}