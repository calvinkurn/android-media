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

class PostFilterAdapter(private val onItemClick: (PostFilterUiModel) -> Unit) : RecyclerView.Adapter<PostFilterAdapter.PostFilterViewHolder>() {

    private var filterItems = emptyList<PostFilterUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostFilterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.shc_item_post_filter, parent, false)
        return PostFilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostFilterViewHolder, position: Int) {
        holder.bind(filterItems[position]) {
            it.isSelected = true

            filterItems.forEach { filterItem ->
                if (it != filterItem) {
                    it.isSelected = false
                }
            }

            notifyDataSetChanged()
            onItemClick(it)
        }
    }

    override fun getItemCount(): Int = filterItems.size

    fun setItems(items: List<PostFilterUiModel>) {
        this.filterItems = items
    }

    class PostFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: PostFilterUiModel, onItemClick: (PostFilterUiModel) -> Unit) = with(itemView) {

            tvShcPostFilterName.text = item.name
            radShcPostFilter.isChecked = item.isSelected

            radShcPostFilter.setOnClickListener {
                onItemClick(item)
            }
            setOnClickListener {
                onItemClick(item)
            }
        }
    }
}