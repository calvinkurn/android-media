package com.tokopedia.tokopedianow.recipebookmark.persentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowTagBinding
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.TagUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.viewholder.TagViewHolder
import com.tokopedia.tokopedianow.recipebookmark.persentation.viewholder.TagViewHolder.TagListener

class TagAdapter(
    private var itemList: List<TagUiModel>,
    private val listener: TagListener? = null
) : RecyclerView.Adapter<TagViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        return TagViewHolder(
            ItemTokopedianowTagBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener
        )
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = itemList.size
}