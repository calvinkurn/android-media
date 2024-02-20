package com.tokopedia.feedplus.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedplus.databinding.ItemFeedPostImageBinding
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.getBitmapFromUrl
import com.tokopedia.play_common.util.blur.ImageBlurUtil
import kotlinx.coroutines.withContext
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.unifycomponents.R as unifycomponentsR

/**
 * Created By : Muhammad Furqan on 02/03/23
 */
class FeedPostImageAdapter(
    val data: List<String>,
    private val lifecycleOwner: LifecycleOwner,
    private val dispatcher: CoroutineDispatchers
) :
    RecyclerView.Adapter<FeedPostImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemFeedPostImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            lifecycleOwner,
            dispatcher
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(
        private val binding: ItemFeedPostImageBinding,
        private val lifecycleOwner: LifecycleOwner,
        private val dispatcher: CoroutineDispatchers
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private val imageBlurUtil by lazyThreadSafetyNone {
            ImageBlurUtil(binding.root.context)
        }

        init {
            lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_DESTROY) imageBlurUtil.close()
                }
            })
        }

        fun bind(url: String) {
            lifecycleOwner.lifecycleScope.launchCatchError(block = {
                val bitmap = withContext(dispatcher.io) {
                    url.getBitmapFromUrl(binding.root.context)
                }

                bitmap?.let {
                    imageBlurUtil.blurredView(
                        src = it,
                        view = binding.bgImgFeedPost,
                        repeatCount = BLUR_REPETITION
                    )

                    binding.bgImgFeedPost.alpha = BG_ALPHA
                    binding.imgFeedPost.setImageBitmap(bitmap)
                }
            }) {
                binding.bgImgFeedPost.setBackgroundColor(MethodChecker.getColor(binding.root.context, unifyprinciplesR.color.Unify_Static_Black))
                binding.imgFeedPost.setImageResource(unifycomponentsR.drawable.ic_broken_image)
            }
        }

        companion object {
            private const val BG_ALPHA = 0.8f
            private const val BLUR_REPETITION = 8
        }
    }
}
