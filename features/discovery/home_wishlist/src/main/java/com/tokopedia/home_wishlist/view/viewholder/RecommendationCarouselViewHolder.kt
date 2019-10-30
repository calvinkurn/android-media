package com.tokopedia.home_wishlist.view.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.*
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.base.SmartAbstractViewHolder
import com.tokopedia.home_wishlist.base.SmartExecutors
import com.tokopedia.home_wishlist.base.SmartListener
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselItemDataModel

class RecommendationCarouselViewHolder(view: View, private val appExecutors: SmartExecutors) : SmartAbstractViewHolder<RecommendationCarouselDataModel>(view) {
    private val title: TextView by lazy { view.findViewById<TextView>(R.id.title) }
    private val seeMore: TextView by lazy { view.findViewById<TextView>(R.id.see_more) }
    private val recyclerView: RecyclerView by lazy { view.findViewById<RecyclerView>(R.id.list) }
    private val disabledView: View by lazy { view.findViewById<View>(R.id.disabled_view) }
    internal val list = mutableListOf<RecommendationCarouselItemDataModel>()

    override fun bind(element: RecommendationCarouselDataModel, listener: SmartListener) {
        element.let {
            title.text = element.title
            list.addAll(element.list)
            disabledView.visibility = if(element.isBulkMode) View.VISIBLE else View.GONE
            seeMore.visibility = if(element.seeMoreAppLink.isEmpty()) View.GONE else View.VISIBLE
            seeMore.setOnClickListener { RouteManager.route(itemView.context, element.seeMoreAppLink) }
            setupRecyclerView(element, listener)
        }
    }
    private fun setupRecyclerView(dataModel: RecommendationCarouselDataModel, listener: SmartListener){
        list.clear()
        list.addAll(dataModel.list)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = object : ListAdapter<RecommendationCarouselItemDataModel, RecommendationCarouselItemViewHolder>(
                AsyncDifferConfig.Builder<RecommendationCarouselItemDataModel>(object: DiffUtil.ItemCallback<RecommendationCarouselItemDataModel>(){
                    override fun areItemsTheSame(oldItem: RecommendationCarouselItemDataModel, newItem: RecommendationCarouselItemDataModel): Boolean {
                        return oldItem.recommendationItem.productId == newItem.recommendationItem.productId
                    }

                    override fun areContentsTheSame(oldItem: RecommendationCarouselItemDataModel, newItem: RecommendationCarouselItemDataModel): Boolean {
                        return oldItem.recommendationItem.productId == newItem.recommendationItem.productId && oldItem.recommendationItem.isWishlist == newItem.recommendationItem.isWishlist
                    }

                })
                .setBackgroundThreadExecutor(appExecutors.diskIO())
                .build()
        ) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationCarouselItemViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(RecommendationCarouselItemDataModel.LAYOUT, parent, false)
                return RecommendationCarouselItemViewHolder(view, adapterPosition)
            }

            override fun getItemCount(): Int = list.size

            override fun onBindViewHolder(holder: RecommendationCarouselItemViewHolder, position: Int) {
                holder.bind(list[position], listener)
            }
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_recommendation_carousel
    }
}