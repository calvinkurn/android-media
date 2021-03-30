package com.tokopedia.analyticsdebugger.cassava.validator.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R

class FileListingAdapter : RecyclerView.Adapter<FileListingAdapter.ItemViewHolder>() {

    private val mItems: MutableList<String> = mutableListOf()
    private var callback: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_test, parent, false)
        return ItemViewHolder(view).apply {
            itemView.setOnClickListener { callback?.invoke(mItems[adapterPosition]) }
        }
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.tv_item).text =
                mItems[position].removePrefix(ValidatorListFragment.TRACKER_ROOT_PATH)
    }

    fun setOnItemClickListener(callback: (String) -> Unit) {
        this.callback = callback
    }

    fun setItems(list: List<String>) {
        val result = DiffUtil.calculateDiff(getDiffCallback(mItems, list))
        with(mItems) {
            clear()
            addAll(list)
        }
        result.dispatchUpdatesTo(this)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

private fun getDiffCallback(old: List<String>, newList: List<String>) = object : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition] == newList[newItemPosition]
    }
}