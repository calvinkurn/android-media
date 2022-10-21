package com.tokopedia.wishlistcollection.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.WishlistCollectionEditOptionItemBinding
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionByIdResponse
import com.tokopedia.wishlistcollection.view.adapter.viewholder.WishlistCollectionEditOptionItemViewHolder
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionEditFragment

class WishlistCollectionEditAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val listAccessOption = arrayListOf<GetWishlistCollectionByIdResponse.GetWishlistCollectionById.Data.AccessOptionsItem>()
    private var _actionListener: ActionListener? = null

    interface ActionListener {
        fun onOptionAccessItemClicked(accessId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = WishlistCollectionEditOptionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishlistCollectionEditOptionItemViewHolder(binding, _actionListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WishlistCollectionEditOptionItemViewHolder -> {
                holder.bind(listAccessOption[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return listAccessOption.size
    }

    fun addList(list: List<GetWishlistCollectionByIdResponse.GetWishlistCollectionById.Data.AccessOptionsItem>) {
        listAccessOption.clear()
        listAccessOption.addAll(list)
        notifyDataSetChanged()
    }

    fun setActionListener(fragment: WishlistCollectionEditFragment?) {
        this._actionListener = fragment
    }
}
