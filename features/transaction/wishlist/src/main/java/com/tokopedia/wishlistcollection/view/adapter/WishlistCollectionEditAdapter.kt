package com.tokopedia.wishlistcollection.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.WishlistCollectionEditOptionItemBinding
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionByIdResponse
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionEditFragment

class WishlistCollectionEditAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val listAccessOption = arrayListOf<GetWishlistCollectionByIdResponse.GetWishlistCollectionById.Data.AccessOptionsItem>()
    private var _actionListener: ActionListener? = null
    private var _currAccess: Int = 0

    interface ActionListener {
        fun onOptionAccessItemClicked(accessId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = WishlistCollectionEditOptionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishlistCollectionEditOptionItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WishlistCollectionEditOptionItemViewHolder -> {
                holder.bind(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return listAccessOption.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(list: List<GetWishlistCollectionByIdResponse.GetWishlistCollectionById.Data.AccessOptionsItem>) {
        listAccessOption.clear()
        listAccessOption.addAll(list)
        notifyDataSetChanged()
    }

    fun setActionListener(fragment: WishlistCollectionEditFragment?) {
        this._actionListener = fragment
    }

    fun setCurrentAccess(currAccess: Int) {
        _currAccess = currAccess
    }

    inner class WishlistCollectionEditOptionItemViewHolder(private val binding: WishlistCollectionEditOptionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.run {
                optionAccessLabel.text = listAccessOption[position].name
                optionAccessName.text = listAccessOption[position].description
                rbOptionItem.isChecked = _currAccess == listAccessOption[position].id
                root.setOnClickListener { selectOptionItem(position) }
                rbOptionItem.setOnClickListener { selectOptionItem(position) }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun selectOptionItem(position: Int) {
        _currAccess = listAccessOption[position].id
        _actionListener?.onOptionAccessItemClicked(listAccessOption[position].id)
        notifyDataSetChanged()
    }
}
