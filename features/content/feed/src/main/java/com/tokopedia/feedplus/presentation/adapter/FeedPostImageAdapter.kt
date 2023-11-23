package com.tokopedia.feedplus.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.feedplus.databinding.ItemFeedPostImageBinding
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play_common.util.blur.ImageBlurUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created By : Muhammad Furqan on 02/03/23
 */
class FeedPostImageAdapter(val data: List<String>, private val lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<FeedPostImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemFeedPostImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            lifecycleOwner,
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(
        private val binding: ItemFeedPostImageBinding,
        private val lifecycleOwner: LifecycleOwner,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private val imageBlurUtil by lazyThreadSafetyNone {
            ImageBlurUtil(binding.root.context)
        }

        fun bind(url: String) {
            lifecycleOwner.lifecycleScope.launch {
                val bitmap = withContext(Dispatchers.IO) {
                    Glide.with(binding.root.context).asBitmap()
                        .load(url)
                        .submit()
                        .get()
                }
                imageBlurUtil.blurredView(src = bitmap, view = binding.bgImgFeedPost, repeatCount = 8)
                binding.bgImgFeedPost.alpha = BG_ALPHA
                binding.imgFeedPost.setImageBitmap(bitmap)
            }
        }

        companion object {
            private const val BG_ALPHA = 0.8f
        }
    }
}
