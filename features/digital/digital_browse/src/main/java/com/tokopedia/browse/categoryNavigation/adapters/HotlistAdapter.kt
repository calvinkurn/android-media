package com.tokopedia.browse.categoryNavigation.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.analytics.CategoryAnalytics
import com.tokopedia.browse.categoryNavigation.data.model.hotlist.ListItem
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.android.synthetic.main.item_category_hotlist.view.*

class HotlistAdapter(private val list: MutableList<ListItem>,
                     private val trackingQueue: TrackingQueue?) : RecyclerView.Adapter<HotlistAdapter.ViewHolder>() {

    val viewMap = HashMap<Int, Boolean>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotlistAdapter.ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_category_hotlist, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return if (list.size > 2) {
            2
        } else {
            list.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.addOnImpressionListener(
                list[position], object : ViewHintListener {
            override fun onViewHint() {
                if (!viewMap.containsKey(position)) {
                    viewMap[position] = true
                    trackingQueue?.let {
                        CategoryAnalytics.createInstance().eventHotlistBannerView(it, list[position], position)
                    }
                }
            }
        })

        ImageHandler.loadImage(holder.itemView.context, holder.hotlist_image, list[position].imgSquare, R.drawable.loading_page)

        holder.hotlist_image.setOnClickListener {
            fireApplink(holder.hotlist_image.context, list[position].applink)
            CategoryAnalytics.createInstance().eventHotlistBannerClick(list[position], position)
        }

    }

    private fun fireApplink(context: Context?, applink: String?) {
        if (applink != null) {
            RouteManager.route(context, applink)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val hotlist_image = view.hotlist_image

    }
}