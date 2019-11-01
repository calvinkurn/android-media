package com.tokopedia.home_wishlist.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_wishlist.base.SmartAbstractViewHolder
import com.tokopedia.home_wishlist.base.SmartExecutors
import com.tokopedia.home_wishlist.base.SmartRecyclerAdapter
import com.tokopedia.home_wishlist.model.datamodel.WishlistDataModel
import com.tokopedia.home_wishlist.view.listener.WishlistListener

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
            override fun areItemsTheSame(oldItem: WishlistDataModel, newItem: WishlistDataModel): Boolean {
                return oldItem::class == newItem::class || oldItem.getUniqueIdentity() == newItem.getUniqueIdentity() || oldItem.hashCode() == newItem.hashCode()
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
        holder.bind(item, wishlistListener)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type(adapterTypeFactory)
    }
}