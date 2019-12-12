package com.tokopedia.discovery.find.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.adapters.QuickFilterAdapter
import com.tokopedia.discovery.find.data.model.RelatedLinkData
import com.tokopedia.unifyprinciples.Typography

class FindRelatedLinkAdapter(var context: Context, private var relatedLinkList: ArrayList<RelatedLinkData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_RELATED_LINK = 0
        const val SHIMMER_LAYOUT_COUNT = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_RELATED_LINK -> {
                val v = LayoutInflater.from(parent.context).inflate(RelatedLinkViewHolder.LAYOUT, parent, false)
                RelatedLinkViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(QuickFilterAdapter.ShimmerViewHolder.Layout, parent, false)
                return QuickFilterAdapter.ShimmerViewHolder(v)
            }
        }
    }

    class RelatedLinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object{
            val LAYOUT = R.layout.related_link_rv_item
        }

        val relatedLink: Typography = itemView.findViewById(R.id.related_link)
    }

    override fun getItemCount(): Int {
        return if (relatedLinkList.size <= 0) {
            SHIMMER_LAYOUT_COUNT
        } else {
            relatedLinkList.size
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_RELATED_LINK -> setRelatedLinkData(holder as RelatedLinkViewHolder, position)
            else -> {
            }
        }
    }

    private fun setRelatedLinkData(viewHolder: RelatedLinkViewHolder, position: Int) {
        val relatedLink = relatedLinkList[position]
        viewHolder.relatedLink.text = relatedLink.text
        viewHolder.relatedLink.setOnClickListener{
            RouteManager.route(context, relatedLink.url)
        }
    }

}