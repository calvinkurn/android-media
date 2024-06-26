package com.tokopedia.home.beranda.presentation.view.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ErrorStateAtfModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ErrorStateChannelOneModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.EmptyBlankViewHolder
import com.tokopedia.home.beranda.presentation.view.helper.HomeThematicUtil
import com.tokopedia.home_component.viewholders.BannerRevampViewHolder
import com.tokopedia.home_component.viewholders.SpecialReleaseViewHolder
import com.tokopedia.home_component.visitable.BannerRevampDataModel
import com.tokopedia.home_component.visitable.SpecialReleaseDataModel

class HomeRecycleAdapter(asyncDifferConfig: AsyncDifferConfig<Visitable<*>>, private val adapterTypeFactory: HomeAdapterFactory, visitables: List<Visitable<*>>) :
        HomeBaseAdapter<HomeAdapterFactory>(asyncDifferConfig, adapterTypeFactory, visitables) {

   private var mRecyclerView: RecyclerView? = null
   private var mLayoutManager: LinearLayoutManager? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        return try {
            val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            adapterTypeFactory.createViewHolder(view, viewType)
        } catch (e: Exception) {
            EmptyBlankViewHolder(parent)
        }
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

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
        if(holder is BannerRevampViewHolder) {
            holder.resumeIndicator()
        }
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewDetachedFromWindow(holder)
        if(holder is BannerRevampViewHolder) {
            holder.pauseIndicator()
        }
    }

    private fun getViewHolder(classType: Class<*>): AbstractViewHolder<out Visitable<*>>? {
        val index = currentList.indexOfFirst { it::class.java == classType }
        return mRecyclerView?.findViewHolderForAdapterPosition(index) as? AbstractViewHolder<out Visitable<*>>
    }

    private fun onResumeSpecialRelease() {
        if(itemCount > 0){
            for (i in 0..(mRecyclerView?.childCount?:0)) {
                val childView = mRecyclerView?.getChildAt(i)
                childView?.let {
                    val holder = mRecyclerView?.getChildViewHolder(childView)
                    holder?.let {
                        if (it is SpecialReleaseViewHolder) {
                            val viewholderPosition = it.adapterPosition
                            notifyItemChanged(viewholderPosition, Bundle().apply {
                                putBoolean(SpecialReleaseDataModel.SPECIAL_RELEASE_TIMER_BIND, true)
                            })
                        }
                    }
                }
            }
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

    fun updateThematicTextColor() {
        currentList.indexOfFirst { it is HomeHeaderDataModel }.let {
            notifyItemChanged(it, bundleOf(HomeThematicUtil.PAYLOAD_APPLY_THEMATIC_COLOR to true))
        }
    }

    override fun onBindViewHolder(
        holder: AbstractViewHolder<out Visitable<*>>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        super.onBindViewHolder(holder, position)
    }

    fun onPause() {
        (getViewHolder(BannerRevampDataModel::class.java) as? BannerRevampViewHolder)?.pauseIndicator()
    }

    fun onResume() {
        (getViewHolder(BannerRevampDataModel::class.java) as? BannerRevampViewHolder)?.resumeIndicator()
        onResumeSpecialRelease()
    }
}
