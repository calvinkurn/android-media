package com.tokopedia.browse.categoryNavigation.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.analytics.CategoryAnalytics
import com.tokopedia.browse.categoryNavigation.data.model.hotlist.ListItem
import kotlinx.android.synthetic.main.item_category_hotlist.view.*

class HotlistAdapter(private val list: MutableList<ListItem>) : RecyclerView.Adapter<HotlistAdapter.ViewHolder>() {

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

        ImageHandler.loadImage(holder.itemView.context, holder.hotlist_image, list[position].imgSquare, R.drawable.loading_page)

        holder.hotlist_image.setOnClickListener {
            fireApplink(holder.hotlist_image.context, list[position].applink)
            CategoryAnalytics.createInstance().eventHotlistBannerClick(holder.itemView.context,list[position], position)
        }

    }

    private fun fireApplink(context: Context?, applink: String?) {
        if (applink != null) {
            RouteManager.route(context, applink)
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.adapterPosition
        if (!viewMap.containsKey(position)) {
            viewMap[position] = true
            CategoryAnalytics.createInstance().eventHotlistBannerView(holder.itemView.context,list[position], position)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val hotlist_image = view.hotlist_image

    }
}