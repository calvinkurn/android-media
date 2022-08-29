package com.tokopedia.imagepicker_insta.common.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.common.ui.model.FeedAccountUiModel
import com.tokopedia.imagepicker_insta.common.ui.viewholder.FeedAccountTypeViewHolder

/**
 * Created By : Jonathan Darwin on April 13, 2022
 */
class FeedAccountTypeAdapter(
    private val listener: FeedAccountTypeViewHolder.Listener,
) : RecyclerView.Adapter<FeedAccountTypeViewHolder>() {

    private val data = mutableListOf<FeedAccountUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedAccountTypeViewHolder {
        return FeedAccountTypeViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: FeedAccountTypeViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun updateData(itemList: List<FeedAccountUiModel>) {
        data.clear()
        data.addAll(itemList)
        notifyDataSetChanged()
    }
}