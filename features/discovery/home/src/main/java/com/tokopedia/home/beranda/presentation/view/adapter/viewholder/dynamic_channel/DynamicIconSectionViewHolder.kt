package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.graphics.Point
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon
import com.tokopedia.home.beranda.helper.DynamicLinkHelper
import com.tokopedia.home.beranda.helper.GravitySnapHelper
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_DYNAMIC_ICON_VIEWHOLDER
import com.tokopedia.home.beranda.helper.glide.FPM_USE_CASE_ICON
import com.tokopedia.home.beranda.helper.glide.loadMiniImage
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.DynamicIconSectionDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.CarouselDecoration
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author by errysuprayogi on 11/28/17.
 */

class DynamicIconSectionViewHolder(val view: View,
                                   val listener: HomeCategoryListener?) : AbstractViewHolder<DynamicIconSectionDataModel>(view) {

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
    override fun bind(element: DynamicIconSectionDataModel) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_DYNAMIC_ICON_VIEWHOLDER)
        adapter?.run { setSectionData(element) }
        BenchmarkHelper.endSystraceSection()
    }

    override fun bind(element: DynamicIconSectionDataModel, payloads: MutableList<Any>) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_DYNAMIC_ICON_VIEWHOLDER)
        adapter?.run { setSectionData(element) }
        BenchmarkHelper.endSystraceSection()
    }

    private class DynamicIconAdapter(
            private val context: Context,
            private val listener: HomeCategoryListener?) : RecyclerView.Adapter<DynamicIconViewHolder>() {

        var sectionViewModel = DynamicIconSectionDataModel()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicIconViewHolder {
            return DynamicIconViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_use_case_and_dynamic_icon, parent, false))
        }

        fun setSectionData(sectionDataModel: DynamicIconSectionDataModel) {
            this.sectionViewModel = sectionDataModel
            notifyDataSetChanged()
            HomeTrackingUtils.homeUsedCaseImpression(context, sectionDataModel.itemList)
        }

        override fun onBindViewHolder(holder: DynamicIconViewHolder, position: Int) {
            holder.title.text = sectionViewModel.itemList[position].name
            holder.shimmeringIcon.show()
            holder.icon.loadMiniImage(sectionViewModel.itemList[position].imageUrl, 150, 150, FPM_USE_CASE_ICON, {
                holder.shimmeringIcon.hide()
            }, onFailed = {
                holder.shimmeringIcon.show()
            })
            holder.container.setOnClickListener { view ->
                eventClickDynamicIcon(view.context, sectionViewModel.itemList[position], position)
                val link = DynamicLinkHelper.getActionLink(sectionViewModel.itemList[position])
                link?.let {
                    listener?.onSectionItemClicked(it)
                }
            }

            if(!sectionViewModel.isCache) {
                holder.itemView.addOnImpressionListener(
                        sectionViewModel.itemList[position] , OnIconImpressedListener(
                        sectionViewModel.itemList[position], listener, position
                ))

            }
        }

        private fun eventClickDynamicIcon(context: Context, homeIconItem: DynamicHomeIcon.DynamicIcon, position: Int) {
            HomePageTracking.eventEnhancedClickDynamicIconHomePage(homeIconItem, position);

            HomeTrackingUtils.homeUsedCaseClick(context,
                    homeIconItem.name, position + 1, homeIconItem.applinks)
        }

        override fun getItemCount(): Int {
            return sectionViewModel.itemList.size
        }
    }

    private class DynamicIconViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: AppCompatImageView = view.findViewById(R.id.icon)
        val shimmeringIcon: View = view.findViewById(R.id.icon_shimmering)
        val title: TextView = view.findViewById(R.id.title)
        val container: LinearLayout = view.findViewById(R.id.container)

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_dynamic_icon_section
    }

    class OnIconImpressedListener(private val homeIcon: DynamicHomeIcon.DynamicIcon,
                                  private val listener: HomeCategoryListener?,
                                  private val position: Int) : ViewHintListener {

        override fun onViewHint() {
            //don't delete, for future use for this viewholder impression
        }
    }
}
