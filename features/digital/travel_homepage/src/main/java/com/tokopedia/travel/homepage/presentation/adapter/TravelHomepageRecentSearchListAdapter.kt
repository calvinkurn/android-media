package com.tokopedia.travel.homepage.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageRecentSearchModel
import kotlinx.android.synthetic.main.travel_homepage_travel_section_list_item.view.*

/**
 * @author by jessica on 2019-08-09
 */

class TravelHomepageRecentSearchListAdapter(private var list: List<TravelHomepageRecentSearchModel.Item>,
                                            var listener: RecentSearchViewHolder.OnItemClickListener?):
        RecyclerView.Adapter<TravelHomepageRecentSearchListAdapter.RecentSearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecentSearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(RecentSearchViewHolder.LAYOUT, parent, false)
        return RecentSearchViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        holder.bind(list.get(position), position, listener)
    }


    class RecentSearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(recentSearch: TravelHomepageRecentSearchModel.Item, position: Int, listener: OnItemClickListener?) {
            with(itemView) {
                image.loadImage(recentSearch.imageUrl)
                title.text = recentSearch.title
                subtitle.text = recentSearch.subtitle
                prefix.text = recentSearch.prefix
            }
            if (listener != null) itemView.setOnClickListener { listener.onItemClick(recentSearch, position) }
        }

        companion object {
            val LAYOUT = R.layout.travel_homepage_travel_section_list_item
        }

        interface OnItemClickListener {
            fun onItemClick(category: TravelHomepageRecentSearchModel.Item, position: Int)
        }
    }
}