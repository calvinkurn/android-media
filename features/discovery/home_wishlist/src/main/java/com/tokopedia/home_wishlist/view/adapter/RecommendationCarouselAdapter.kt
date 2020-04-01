package com.tokopedia.home_wishlist.view.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselItemDataModel
import com.tokopedia.home_wishlist.view.custom.WishlistCallback
import com.tokopedia.home_wishlist.view.ext.copy
import com.tokopedia.home_wishlist.view.listener.WishlistListener
import com.tokopedia.home_wishlist.view.viewholder.RecommendationCarouselItemViewHolder
import com.tokopedia.smart_recycler_helper.SmartExecutors

class RecommendationCarouselAdapter (
        private val wishlistListener: WishlistListener,
        appExecutors: SmartExecutors
) : ListAdapter<RecommendationCarouselItemDataModel, RecommendationCarouselItemViewHolder>(
        AsyncDifferConfig.Builder<RecommendationCarouselItemDataModel>(
                object : DiffUtil.ItemCallback<RecommendationCarouselItemDataModel>(){
                    override fun getChangePayload(oldItem: RecommendationCarouselItemDataModel, newItem: RecommendationCarouselItemDataModel): Any? {
                        val diff = Bundle()
                        if (newItem.recommendationItem.isWishlist != oldItem.recommendationItem.isWishlist) {
                            diff.putBoolean("wishlist", newItem.recommendationItem.isWishlist)
                        }
                        return if (diff.size() == 0) {
                            null
                        } else diff
                    }

                    override fun areItemsTheSame(oldItem: RecommendationCarouselItemDataModel, newItem: RecommendationCarouselItemDataModel): Boolean {
                        return oldItem.recommendationItem.productId == newItem.recommendationItem.productId
                    }

                    override fun areContentsTheSame(oldItem: RecommendationCarouselItemDataModel, newItem: RecommendationCarouselItemDataModel): Boolean {
                        return oldItem.recommendationItem.isWishlist == newItem.recommendationItem.isWishlist
                    }

                }
        )
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()
){
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

    fun updateWishlist(index: Int, isWislist: Boolean){
        val newList = list.copy()
        newList[index] = list[index].copy(recommendationItem = list[index].recommendationItem.copy(isWishlist = isWislist))
        updateList(newList)
    }
}