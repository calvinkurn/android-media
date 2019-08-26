package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.graphics.Point
import android.support.annotation.LayoutRes
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.helper.DynamicLinkHelper
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.DynamicIconDecoration
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicIconSectionViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HomeIconItem
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils

/**
 * @author by errysuprayogi on 11/28/17.
 */

class DynamicIconSectionViewHolder(val view: View,
                                   val listener: HomeCategoryListener) : AbstractViewHolder<DynamicIconSectionViewModel>(view) {

    private var adapter: DynamicIconAdapter? = null

    init {
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.list)
        adapter = DynamicIconAdapter(itemView.context, listener)

        /**
         * get max width of device
         * for dynamic icon decoration
         */
        val windowManager = itemView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x

        if (recyclerView.itemDecorationCount == 0) {
            recyclerView.addItemDecoration(DynamicIconDecoration(
                    itemView.context.resources.getDimensionPixelSize(R.dimen.dp_16),
                    width,
                    5,
                    itemView.context.resources.getDimensionPixelOffset(
                            R.dimen.use_case_and_dynamic_icon_size
                    )
            ))
        }
        recyclerView.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(itemView.context,
                LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.clearOnScrollListeners()
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    listener.onDynamicIconScrollStart()
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    listener.onDynamicIconScrollEnd()
                }
            }
        })
    }
    override fun bind(element: DynamicIconSectionViewModel) {
        adapter?.run { setSectionData(element) }
    }

    private class DynamicIconAdapter(
            private val context: Context,
            private val listener: HomeCategoryListener) : RecyclerView.Adapter<DynamicIconViewHolder>() {

        var sectionViewModel = DynamicIconSectionViewModel()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicIconViewHolder {
            return DynamicIconViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_use_case_and_dynamic_icon, parent, false))
        }

        fun setSectionData(sectionViewModel: DynamicIconSectionViewModel) {
            this.sectionViewModel = sectionViewModel
            notifyDataSetChanged()
            HomeTrackingUtils.homeUsedCaseImpression(context, sectionViewModel.itemList)
        }

        override fun onBindViewHolder(holder: DynamicIconViewHolder, position: Int) {
            holder.title.text = sectionViewModel.itemList[position].title
            ImageHandler.loadImageThumbs(holder.context, holder.icon, sectionViewModel.itemList[position].icon)
            holder.container.setOnClickListener { view ->
                eventClickDynamicIcon(view.context, sectionViewModel.itemList[position], position)
                listener.onSectionItemClicked(DynamicLinkHelper.getActionLink(sectionViewModel.itemList[position]))
            }
        }

        private fun eventClickDynamicIcon(context: Context, homeIconItem: HomeIconItem, position: Int) {
            HomePageTracking.eventEnhancedClickDynamicIconHomePage(context,
                    homeIconItem.getEnhanceClickDynamicIconHomePage(position))

            HomeTrackingUtils.homeUsedCaseClick(context,
                    homeIconItem.title, position + 1, homeIconItem.applink)
        }

        override fun getItemCount(): Int {
            return sectionViewModel.itemList.size
        }
    }

    private class DynamicIconViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val icon: AppCompatImageView = view.findViewById(R.id.icon)
        val title: TextView = view.findViewById(R.id.title)
        val container: LinearLayout = view.findViewById(R.id.container)
        val context: Context
            get() = view.context

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_dynamic_icon_section
    }
}
