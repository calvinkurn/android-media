package com.tokopedia.feedplus.presentation.adapter

import android.graphics.Bitmap
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
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
                blurImage(bitmap)
            }
        }

        private suspend fun blurImage(bitmap: Bitmap) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                binding.bgImgFeedPost.setImageBitmap(bitmap)
                binding.bgImgFeedPost.setRenderEffect(
                    RenderEffect.createBlurEffect(
                        DEFAULT_NEW_RADIUS,
                        DEFAULT_NEW_RADIUS,
                        Shader.TileMode.CLAMP
                    )
                )
            } else {
                var resultBlur: Bitmap = bitmap
                    for (i in 1..8) {
                        resultBlur = imageBlurUtil.blurImage(resultBlur, radius = DEFAULT_OLD_RADIUS)
                    }
                binding.bgImgFeedPost.setImageBitmap(resultBlur)
            }
            binding.imgFeedPost.setImageBitmap(bitmap)
            binding.bgImgFeedPost.alpha = BG_ALPHA
        }

        companion object {
            private const val BG_ALPHA = 0.8f
            private const val DEFAULT_OLD_RADIUS = 25f
            private const val DEFAULT_NEW_RADIUS = DEFAULT_OLD_RADIUS * 8
        }
    }
}
