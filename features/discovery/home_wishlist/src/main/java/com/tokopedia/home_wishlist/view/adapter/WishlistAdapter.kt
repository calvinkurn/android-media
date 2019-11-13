package com.tokopedia.home_wishlist.view.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.home_wishlist.model.datamodel.*
import com.tokopedia.home_wishlist.view.listener.WishlistListener
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartExecutors
import com.tokopedia.smart_recycler_helper.SmartRecyclerAdapter

/**
 * A Class of WishlistAdapter.
 *
 * This class for handling adapter recommendation page
 *
 * @property adapterTypeFactory the type factory for recommendationPage
 */
class WishlistAdapter(
        smartExecutors: SmartExecutors,
        private val adapterTypeFactory: WishlistTypeFactoryImpl,
        private val wishlistListener: WishlistListener
) : SmartRecyclerAdapter<WishlistDataModel, WishlistTypeFactoryImpl>(
        appExecutors = smartExecutors,
        adapterTypeFactory = adapterTypeFactory,
        diffCallback = object : DiffUtil.ItemCallback<WishlistDataModel>(){
            override fun getChangePayload(oldItem: WishlistDataModel, newItem: WishlistDataModel): Any? {
                if(oldItem is WishlistItemDataModel && newItem is WishlistItemDataModel){
                    val bundle = Bundle()
                    if(oldItem.isOnChecked != newItem.isOnChecked){
                        bundle.putBoolean("isOnChecked", newItem.isOnChecked)
                    }

                    if(oldItem.isOnAddToCartProgress != newItem.isOnAddToCartProgress){
                        bundle.putBoolean("isOnAddToCartProgress", newItem.isOnAddToCartProgress)
                    }

                    if(oldItem.isOnBulkRemoveProgress != newItem.isOnBulkRemoveProgress){
                        bundle.putBoolean("isOnBulkRemoveProgress", newItem.isOnBulkRemoveProgress)
                    }
                    return bundle
                }else if(oldItem is RecommendationCarouselDataModel && newItem is RecommendationCarouselDataModel){
                    val bundle = Bundle()
                    if(oldItem.isOnBulkRemoveProgress != newItem.isOnBulkRemoveProgress){
                        bundle.putBoolean("isOnBulkRemoveProgress", newItem.isOnBulkRemoveProgress)
                    }

                    val differentList = oldItem.list.zip(newItem.list).find{ (list1, list2) -> list1.recommendationItem.isWishlist != list2.recommendationItem.isWishlist}
                    if(differentList != null){
                        val index = newItem.list.indexOf(differentList.second)
                        bundle.putInt("updateList", index)
                        bundle.putBoolean("updateIsWishlist", differentList.second.recommendationItem.isWishlist)
                    }
                    return bundle
                }else if(oldItem is RecommendationItemDataModel && newItem is RecommendationItemDataModel){
                    val bundle = Bundle()
                    if(oldItem.recommendationItem.isWishlist != newItem.recommendationItem.isWishlist){
                        bundle.putBoolean("wishlist", newItem.recommendationItem.isWishlist)
                    }
                    return bundle
                }

                return null
            }

            override fun areItemsTheSame(oldItem: WishlistDataModel, newItem: WishlistDataModel): Boolean {
                return oldItem.getUniqueIdentity() == newItem.getUniqueIdentity() || (oldItem.hashCode() == newItem.hashCode() && oldItem::class == newItem::class)
            }

            override fun areContentsTheSame(oldItem: WishlistDataModel, newItem: WishlistDataModel): Boolean {
                return oldItem.equalsDataModel(newItem)
            }
        }
) {
    /**
     * This override void from [BaseListAdapter]
     * It handling binding viewHolder
     * @param holder the viewHolder on bind
     * @param position the position of the viewHolder
     */
    override fun bind(holder: SmartAbstractViewHolder<Visitable<*>>, item: WishlistDataModel) {
        val layout = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        when(item){
            is WishlistItemDataModel,
            is LoadMoreDataModel,
            is RecommendationCarouselDataModel,
            is LoadingDataModel,
            is EmptyWishlistDataModel,
            is EmptySearchWishlistDataModel,
            is ErrorWishlistDataModel,
            is RecommendationTitleDataModel-> layout.isFullSpan = true
        }
        holder.bind(item, wishlistListener)
    }

    override fun bind(holder: SmartAbstractViewHolder<Visitable<*>>, item: WishlistDataModel, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty()){
            holder.bind(item, wishlistListener, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type(adapterTypeFactory)
    }
}