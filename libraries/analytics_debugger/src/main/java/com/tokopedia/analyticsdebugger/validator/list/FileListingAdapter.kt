package com.tokopedia.analyticsdebugger.validator.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R

class FileListingAdapter : RecyclerView.Adapter<FileListingAdapter.ItemViewHolder>() {

    private val mItems: MutableList<String> = mutableListOf()
    private var callback: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_test, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val tv = holder.itemView.findViewById<TextView>(R.id.tv_item)
        tv.text = mItems[position]
        holder.itemView.setOnClickListener { callback?.invoke(mItems[position]) }
    }

    fun setOnItemClickListener(callback: (String) -> Unit) {
        this.callback = callback
    }

    fun setItems(list: List<String>) {
        with(mItems) {
            clear()
            addAll(list)
            notifyDataSetChanged()
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}