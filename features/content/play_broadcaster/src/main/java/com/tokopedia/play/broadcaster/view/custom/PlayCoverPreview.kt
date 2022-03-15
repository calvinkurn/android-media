package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewPlayCoverPreviewBinding
import com.tokopedia.play_common.view.getBitmapFromUrl
import kotlinx.coroutines.*

/**
 * Created By : Jonathan Darwin on March 14, 2022
 */
class PlayCoverPreview : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = ViewPlayCoverPreviewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private var isCoverAvailable = false

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        scope.launch {
            binding.ivCoverImageCircleDash.apply {
                setImageBitmap(
                    context.getBitmapFromUrl(context.getString(R.string.ic_play_cover_circle_dash), cacheStrategy = DiskCacheStrategy.RESOURCE)
                )
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        job.cancelChildren()
    }

    fun setCover(imageUrl: String) {
        setCover(imageUrl, false)
    }

    fun setCoverWithPlaceholder(imageUrl: String) {
        setCover(imageUrl, true)
    }

    private fun setCover(imageUrl: String, isShowPlaceholder: Boolean) {
        binding.apply {
            ivCoverImageCircleDash.visibility = if(isShowPlaceholder) View.VISIBLE else View.GONE
            ivCoverImagePlaceholder.visibility = if(isShowPlaceholder) View.VISIBLE else View.GONE
            ivCoverPreview.setImageUrl(imageUrl)
        }
        isCoverAvailable = true
    }

    fun setTitle(title: String) {
        binding.tvCoverTitle.text = title
    }

    fun setShopName(shopName: String) {
        binding.tvCoverShopName.text = shopName
    }
}