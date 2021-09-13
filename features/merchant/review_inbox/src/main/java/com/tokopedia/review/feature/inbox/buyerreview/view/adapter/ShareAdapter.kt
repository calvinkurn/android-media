package com.tokopedia.review.feature.inbox.buyerreview.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.tokopedia.review.feature.inbox.buyerreview.view.widgets.ShareItem
import com.tokopedia.review.inbox.R
import com.tokopedia.unifyprinciples.Typography
import java.util.*

/**
 * Created by stevenfredian on 2/21/17.
 */
class ShareAdapter constructor(context: Context?) : BaseAdapter() {
    val inflater: LayoutInflater
    var shareItems: ArrayList<ShareItem>
    fun addItem(item: ShareItem) {
        shareItems.add(item)
    }

    override fun getCount(): Int {
        return shareItems.size
    }

    override fun getItem(position: Int): ShareItem {
        return shareItems.get(position)
    }

    override fun getItemId(position: Int): Long {
        return shareItems.get(position).hashCode().toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView: View = convertView
        val holder: ViewHolder
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.reputation_sheet_grid_item, parent, false)
            holder = ViewHolder(convertView)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val info: ShareItem = shareItems.get(position)
        if (info.getIcon() != null) {
            imageView.loadImage(info.getIcon())
        } else {
//                loadIcon();
        }
        holder.label.setText(info.getName())
        convertView.setOnClickListener(shareItems.get(position).getOnClickListener())
        return convertView
    }

    internal inner class ViewHolder constructor(root: View) {
        val icon: ImageView
        val label: Typography

        init {
            icon = root.findViewById<View>(R.id.icon) as ImageView
            label = root.findViewById<View>(R.id.label) as Typography
        }
    }

    init {
        inflater = LayoutInflater.from(context)
        shareItems = ArrayList()
    }
}