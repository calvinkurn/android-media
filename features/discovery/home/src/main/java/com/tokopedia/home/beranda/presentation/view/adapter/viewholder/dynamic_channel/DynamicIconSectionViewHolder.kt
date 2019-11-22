package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.graphics.Point
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.helper.DynamicLinkHelper
import com.tokopedia.home.beranda.helper.GravitySnapHelper
import com.tokopedia.home.beranda.helper.HomeImageHandler
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.CarouselDecoration
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.DynamicIconSectionViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.HomeIconItem
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener

/**
 * @author by errysuprayogi on 11/28/17.
 */

class DynamicIconSectionViewHolder(val view: View,
                                   val listener: HomeCategoryListener) : AbstractViewHolder<DynamicIconSectionViewModel>(view) {

    private var adapter: DynamicIconAdapter? = null
    private val startSnapHelper: GravitySnapHelper by lazy { GravitySnapHelper(Gravity.START, true) }

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
            recyclerView.addItemDecoration(CarouselDecoration(
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
        recyclerView.setHasFixedSize(true)
        startSnapHelper.attachToRecyclerView(recyclerView)
        recyclerView.clearOnScrollListeners()
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
//            ImageHandler.loadImageThumbs(holder.context, holder.icon, sectionViewModel.itemList[position].icon)
            HomeImageHandler.loadImage(holder.context, holder.icon, sectionViewModel.itemList[position].icon)
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

        private fun eventClickDynamicIcon(context: Context, homeIconItem: HomeIconItem, position: Int) {
            HomePageTracking.eventEnhancedClickDynamicIconHomePage(context, homeIconItem, position);

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

    class OnIconImpressedListener(private val homeIconItem: HomeIconItem,
                                  private val listener: HomeCategoryListener,
                                  private val position: Int) : ViewHintListener {

        override fun onViewHint() {
            //don't delete, for future use for this viewholder impression
        }
    }
}
