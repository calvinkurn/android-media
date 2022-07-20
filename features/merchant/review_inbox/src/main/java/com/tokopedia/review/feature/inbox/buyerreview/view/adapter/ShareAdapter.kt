package com.tokopedia.review.feature.inbox.buyerreview.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.feature.inbox.buyerreview.view.widgets.ShareItem
import com.tokopedia.review.inbox.R
import com.tokopedia.unifyprinciples.Typography
import java.util.*

/**
 * Created by stevenfredian on 2/21/17.
 */
class ShareAdapter constructor(context: Context?) : BaseAdapter() {
    val inflater: LayoutInflater = LayoutInflater.from(context)
    var shareItems: ArrayList<ShareItem> = ArrayList()

    fun addItem(item: ShareItem) {
        shareItems.add(item)
    }

    override fun getCount(): Int {
        return shareItems.size
    }

    override fun getItem(position: Int): ShareItem {
        return shareItems[position]
    }

    override fun getItemId(position: Int): Long {
        return shareItems[position].hashCode().toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var holder: ViewHolder? = convertView?.tag as? ViewHolder

        val view: View
        if (convertView == null) {
            view = inflater.inflate(R.layout.reputation_sheet_grid_item, parent, false)
            holder = ViewHolder(view)
            view?.tag = holder
        } else {
            view = convertView
        }

        val info = shareItems.getOrNull(position)

        if (info?.icon != null) {
            holder?.icon?.loadImage(info.icon)
        }

        holder?.label?.text = info?.name
        view.setOnClickListener(shareItems[position].onClickListener)
        return view
    }

    internal inner class ViewHolder constructor(root: View) {
        val icon: ImageView? = root.findViewById(R.id.icon)
        val label: Typography? = root.findViewById(R.id.label)
    }

}