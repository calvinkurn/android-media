package com.tokopedia.home_wishlist.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.home_wishlist.base.SmartExecutors
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselItemDataModel
import com.tokopedia.home_wishlist.view.listener.WishlistListener
import com.tokopedia.home_wishlist.view.viewholder.RecommendationCarouselItemViewHolder
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable

class RecommendationCarouselAdapter (
        private val wishlistListener: WishlistListener
) : RecyclerView.Adapter<RecommendationCarouselItemViewHolder>()
//        AsyncDifferConfig.Builder<RecommendationCarouselItemDataModel>(
//                object : DiffUtil.ItemCallback<RecommendationCarouselItemDataModel>(){
//                    override fun getChangePayload(oldItem: RecommendationCarouselItemDataModel, newItem: RecommendationCarouselItemDataModel): Any? {
//                        val diff = Bundle()
//                        if (newItem.isWishlist != oldItem.isWishlist) {
//                            diff.putBoolean("wishlist", newItem.isWishlist)
//                        }
//                        return if (diff.size() == 0) {
//                            null
//                        } else diff
//                    }
//
//                    override fun areItemsTheSame(oldItem: RecommendationCarouselItemDataModel, newItem: RecommendationCarouselItemDataModel): Boolean {
//                        return oldItem.recommendationItem.productId == newItem.recommendationItem.productId
//                    }
//
//                    override fun areContentsTheSame(oldItem: RecommendationCarouselItemDataModel, newItem: RecommendationCarouselItemDataModel): Boolean {
//                        return oldItem.isWishlist == newItem.isWishlist
//                    }
//
//                }
//        )
//        .setBackgroundThreadExecutor(smartExecutors.diskIO())
//        .build()
//)
{
    private var list = ArrayList<RecommendationCarouselItemDataModel>()

    override fun onBindViewHolder(holder: RecommendationCarouselItemViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        } else {
            val o = payloads[0] as Bundle
            for (key in o.keySet()) {
                if (key == "wishlist") {
                    holder.updateWishlist(o.getBoolean(key))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecommendationCarouselItemViewHolder, position: Int) {
        holder.bind(list[position], wishlistListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationCarouselItemViewHolder {
        val view = onCreateViewItem(parent, viewType)
        return RecommendationCarouselItemViewHolder(view)
    }

    private fun onCreateViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    }

    override fun getItemViewType(position: Int): Int {
        return RecommendationCarouselItemViewHolder.LAYOUT
    }

    fun updateList(newList: List<RecommendationCarouselItemDataModel>) {
        val diffResult = DiffUtil.calculateDiff(WishlistCallback(list, newList))

        list.clear()
        list.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }
}