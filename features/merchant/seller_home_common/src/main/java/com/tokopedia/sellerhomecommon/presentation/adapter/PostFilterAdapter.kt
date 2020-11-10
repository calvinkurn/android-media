package com.tokopedia.sellerhomecommon.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.PostFilterUiModel
import kotlinx.android.synthetic.main.shc_item_post_filter.view.*

/**
 * Created By @ilhamsuaib on 06/11/20
 */

class PostFilterAdapter(
        private val filterItems: List<PostFilterUiModel>,
        private val listener: Listener
) : RecyclerView.Adapter<PostFilterAdapter.PostFilterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostFilterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.shc_item_post_filter, parent, false)
        return PostFilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostFilterViewHolder, position: Int) {
        holder.bind(filterItems[position], listener)
    }

    override fun getItemCount(): Int = filterItems.size

    fun getItems() = filterItems

    class PostFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: PostFilterUiModel, listener: Listener) = with(itemView) {

            tvShcPostFilterName.text = item.name
            radShcPostFilter.isChecked = item.isSelected

            radShcPostFilter.setOnClickListener {
                listener.onItemClick(item)
            }
            setOnClickListener {
                listener.onItemClick(item)
            }
        }
    }

    interface Listener {
        fun onItemClick(item: PostFilterUiModel)
    }
}