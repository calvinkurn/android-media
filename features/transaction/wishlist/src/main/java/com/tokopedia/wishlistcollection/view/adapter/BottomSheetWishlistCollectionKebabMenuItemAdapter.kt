package com.tokopedia.wishlistcollection.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.BottomsheetKebabMenuWishlistCollectionItemBinding
import com.tokopedia.wishlistcollection.data.model.BottomSheetKebabActionItemData
import com.tokopedia.wishlistcollection.view.adapter.viewholder.BottomSheetWishlistCollectionKebabMenuItemViewHolder
import com.tokopedia.wishlistcollection.view.bottomsheet.listener.ActionListenerBottomSheetMenu

class BottomSheetWishlistCollectionKebabMenuItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val listAction = arrayListOf<BottomSheetKebabActionItemData>()
    private var _actionListener: ActionListenerBottomSheetMenu? = null
    var _collectionId: String = ""
    var _collectionName: String = ""
    var _collectionIndicatorTitle: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = BottomsheetKebabMenuWishlistCollectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BottomSheetWishlistCollectionKebabMenuItemViewHolder(binding, _actionListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BottomSheetWishlistCollectionKebabMenuItemViewHolder -> {
                holder.bind(listAction[position], _collectionId, _collectionName, _collectionIndicatorTitle)
            }
        }
    }

    override fun getItemCount(): Int {
        return listAction.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(list: List<BottomSheetKebabActionItemData>) {
        listAction.clear()
        listAction.addAll(list)
        notifyDataSetChanged()
    }

    fun setActionListener(actionListener: ActionListenerBottomSheetMenu?) {
        this._actionListener = actionListener
    }
}
