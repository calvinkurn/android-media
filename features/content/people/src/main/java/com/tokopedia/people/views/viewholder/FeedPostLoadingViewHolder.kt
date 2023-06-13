package com.tokopedia.people.views.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.people.databinding.UpItemLoadingBinding

/**
 * created by fachrizalmrsln on 22/11/22
 **/
class FeedPostLoadingViewHolder(
    binding: UpItemLoadingBinding,
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup) = FeedPostLoadingViewHolder(
            UpItemLoadingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }
}
