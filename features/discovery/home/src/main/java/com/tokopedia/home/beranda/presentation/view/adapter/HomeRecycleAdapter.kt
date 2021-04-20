package com.tokopedia.home.beranda.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ErrorStateAtfModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ErrorStateChannelOneModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.BannerViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.PlayCardViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.EmptyBlankViewHolder
import com.tokopedia.home.beranda.presentation.view.helper.HomePlayWidgetHelper
import java.util.*

class HomeRecycleAdapter(asyncDifferConfig: AsyncDifferConfig<Visitable<*>>, private val adapterTypeFactory: HomeAdapterFactory, visitables: List<Visitable<*>>) :
        HomeBaseAdapter<HomeAdapterFactory>(asyncDifferConfig, adapterTypeFactory, visitables){

   private var mRecyclerView: RecyclerView? = null
   private var currentSelected = -1
   private var mLayoutManager: LinearLayoutManager? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return adapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item != null) (getItem(position) as Visitable<HomeAdapterFactory>).type(adapterTypeFactory)
        else EmptyBlankViewHolder.LAYOUT
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
        mLayoutManager = mRecyclerView?.layoutManager as LinearLayoutManager
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
        } else if(holder is BannerViewHolder){
            holder.onPause()
        }
    }

    private fun getViewHolder(position: Int): AbstractViewHolder<out Visitable<*>>? {
        return mRecyclerView?.findViewHolderForAdapterPosition(position) as AbstractViewHolder<out Visitable<*>>?
    }

    private fun getPositionPlay(): List<Int>{
        val list = mutableListOf<Int>()
        for (i in currentList.indices) {
            if(getItem(i) is PlayCardDataModel) list.add(i)
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

    fun onResumeBanner() {
        if(itemCount > 0){
            (getViewHolder(0) as? BannerViewHolder)?.onResume()
        }
    }

    fun onResumePlayWidget(){
        val positions = getPositionPlay()
        if(positions.isNotEmpty()){
            currentSelected = positions.first()
            (getViewHolder(currentSelected) as? PlayCardViewHolder)?.resume()
        }
    }

    fun onPauseBanner() {
        if(itemCount > 0){
            (getViewHolder(0) as? BannerViewHolder)?.onPause()
        }
    }

    fun onPausePlayWidget(shouldPausePlay: Boolean){
        val positions = getPositionPlay()
        if(positions.isNotEmpty()){
            currentSelected = positions.first()
            (getViewHolder(currentSelected) as? PlayCardViewHolder)?.pause(shouldPausePlay)
        }
    }

    fun onDestroy() {
        for (exoPlayerHelper in getAllExoPlayers()) {
            exoPlayerHelper.onActivityDestroy()
        }
    }

    fun resetImpressionHomeBanner() {
        if(itemCount > 0){
            (getViewHolder(0) as? BannerViewHolder)?.resetImpression()
        }
    }

    fun resetChannelErrorState() {
        currentList.indexOfFirst { it is ErrorStateChannelOneModel }.let { position ->
            if (position == -1) return@let
            notifyItemChanged(position)
        }
    }

    fun resetAtfErrorState() {
        currentList.indexOfFirst { it is ErrorStateAtfModel }.let { position ->
            if (position == -1) return@let
            notifyItemChanged(position)
        }
    }
}