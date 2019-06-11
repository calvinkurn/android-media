package com.tokopedia.browse.categoryNavigation.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.tokopedia.applink.RouteManager
import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.data.model.hotlist.ListItem
import kotlinx.android.synthetic.main.item_category_hotlist.view.*

class HotlistAdapter(private val list: MutableList<ListItem>) : RecyclerView.Adapter<HotlistAdapter.ViewHolder>() {

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


        Glide.with(holder.itemView.context)
                .load(list[position].imgSquare)
                .placeholder(R.drawable.loading_page)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .centerCrop()
                .into(holder.hotlist_image)

        holder.hotlist_image.setOnClickListener {
            fireApplink(holder.hotlist_image.context, list[position].applink)
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