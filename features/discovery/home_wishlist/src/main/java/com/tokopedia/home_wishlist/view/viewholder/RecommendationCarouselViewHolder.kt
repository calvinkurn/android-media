package com.tokopedia.home_wishlist.view.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselItemDataModel

class RecommendationCarouselViewHolder(view: View) : AbstractViewHolder<RecommendationCarouselDataModel>(view) {
    private val title: TextView by lazy { view.findViewById<TextView>(R.id.title) }
    private val seeMore: TextView by lazy { view.findViewById<TextView>(R.id.see_more) }
    private val recyclerView: RecyclerView by lazy { view.findViewById<RecyclerView>(R.id.list) }
    internal val list = mutableListOf<RecommendationCarouselItemDataModel>()

    override fun bind(element: RecommendationCarouselDataModel?) {
        element?.let {
            title.text = element.title
            list.addAll(element.list)
            seeMore.visibility = if(element.seeMoreAppLink.isEmpty()) View.GONE else View.VISIBLE
            seeMore.setOnClickListener { RouteManager.route(itemView.context, element.seeMoreAppLink) }
            setupRecyclerView(element)
        }
    }
    private fun setupRecyclerView(dataModel: RecommendationCarouselDataModel){
        list.clear()
        list.addAll(dataModel.list)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = object : RecyclerView.Adapter<RecommendationCarouselItemViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationCarouselItemViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(RecommendationCarouselItemDataModel.LAYOUT, parent, false)
                return RecommendationCarouselItemViewHolder(view)
            }

            override fun getItemCount(): Int = list.size

            override fun onBindViewHolder(holder: RecommendationCarouselItemViewHolder, position: Int) {
                holder.bind(list[position])
            }
        }
    }

    fun updateWishlist(position: Int, isWishlist: Boolean){
        list[position].recommendationItem.isWishlist = isWishlist
        recyclerView.adapter?.notifyItemChanged(position)
    }

    companion object{
        val LAYOUT = R.layout.layout_recommendation_carousel
    }
}