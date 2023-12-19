package com.tokopedia.shareexperience.ui.adapter.viewholder.image

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shareexperience.databinding.ShareexperienceImageItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class ShareExImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val binding: ShareexperienceImageItemBinding? by viewBinding()

    fun bind(element: String) {
        binding?.shareexIvImageItem?.loadImage(element)
    }
}
