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
    val inflater: LayoutInflater = LayoutInflater.from(context)
    var shareItems: ArrayList<ShareItem> = ArrayList()

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
        val holder: ViewHolder = convertView.tag as ViewHolder
        val info: ShareItem = shareItems[position]
        imageView.loadImage(info.getIcon())
        holder.label.text = info.name
        convertView.setOnClickListener(shareItems[position].onClickListener)
        return convertView
    }

    internal inner class ViewHolder constructor(root: View) {
        val icon: ImageView = root.findViewById<View>(R.id.icon) as ImageView
        val label: Typography = root.findViewById<View>(R.id.label) as Typography
    }

}