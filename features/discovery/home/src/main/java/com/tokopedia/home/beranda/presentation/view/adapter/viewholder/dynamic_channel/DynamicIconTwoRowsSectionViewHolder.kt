package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon
import com.tokopedia.home.beranda.helper.DynamicLinkHelper
import com.tokopedia.home.beranda.helper.glide.FPM_USE_CASE_ICON
import com.tokopedia.home.beranda.helper.glide.loadMiniImage
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.DynamicIconSectionDataModel
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author by errysuprayogi on 11/28/17.
 */

class DynamicIconTwoRowsSectionViewHolder(val view: View,
                                          val listener: HomeCategoryListener) : AbstractViewHolder<DynamicIconSectionDataModel>(view) {

    private var adapter: DynamicIconAdapter? = null

    init {
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.list)
        adapter = DynamicIconAdapter(itemView.context, listener)


        recyclerView.adapter = adapter
        val gridLayoutManager = GridLayoutManager(itemView.context,
                5, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.clearOnScrollListeners()
    }
    override fun bind(element: DynamicIconSectionDataModel) {
        adapter?.run { setSectionData(element) }
    }

    override fun bind(element: DynamicIconSectionDataModel, payloads: MutableList<Any>) {
        adapter?.run { setSectionData(element) }
    }

    private class DynamicIconAdapter(
            private val context: Context,
            private val listener: HomeCategoryListener) : RecyclerView.Adapter<DynamicIconViewHolder>() {

        var sectionViewModel = DynamicIconSectionDataModel()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicIconViewHolder {
            return DynamicIconViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_dynamic_icon_two_row, parent, false))
        }

        fun setSectionData(sectionDataModel: DynamicIconSectionDataModel) {
            this.sectionViewModel = sectionDataModel
            notifyDataSetChanged()
            HomeTrackingUtils.homeUsedCaseImpression(context, sectionDataModel.itemList)
        }

        override fun onBindViewHolder(holder: DynamicIconViewHolder, position: Int) {
            holder.title.text = sectionViewModel.itemList[position].name
            holder.icon.loadMiniImage(sectionViewModel.itemList[position].imageUrl, 150, 150, FPM_USE_CASE_ICON, {
                holder.shimmeringIcon.hide()
            }, onFailed = {
                holder.shimmeringIcon.show()
            })
            holder.container.setOnClickListener { view ->
                eventClickDynamicIcon(view.context, sectionViewModel.itemList[position], position)
                listener.onSectionItemClicked(DynamicLinkHelper.getActionLink(sectionViewModel.itemList[position]))
            }

            if(!sectionViewModel.isCache) {
                holder.itemView.addOnImpressionListener(
                        sectionViewModel.itemList[position], OnIconImpressedListener(
                        sectionViewModel.itemList[position], listener, position
                )
                )
            }
        }

        private fun eventClickDynamicIcon(context: Context, homeIconItem: DynamicHomeIcon.DynamicIcon, position: Int) {
            HomePageTracking.eventEnhancedClickDynamicIconHomePage(homeIconItem, position);

            HomeTrackingUtils.homeUsedCaseClick(context,
                    homeIconItem.name, position + 1, homeIconItem.applinks)
        }

        override fun getItemCount(): Int {
            return if(sectionViewModel.itemList.size > 10) 10 else sectionViewModel.itemList.size
        }
    }

    private class DynamicIconViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val icon: AppCompatImageView = view.findViewById(R.id.icon)
        val shimmeringIcon: View = view.findViewById(R.id.icon_shimmering)
        val title: TextView = view.findViewById(R.id.title)
        val container: LinearLayout = view.findViewById(R.id.container)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_dynamic_icon_two_rows_section
    }

    class OnIconImpressedListener(private val homeIconItem: DynamicHomeIcon.DynamicIcon,
                                  private val listener: HomeCategoryListener,
                                  private val position: Int) : ViewHintListener {

        override fun onViewHint() {
            //don't delete, for future use for this viewholder impression
        }
    }
}
