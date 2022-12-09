package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.R
import com.tokopedia.unifyprinciples.R as unifyR
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

    var isCoverAvailable = false
        private set

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

    fun setInitialCover() {
        binding.ivCoverImageCircleDash.showWithCondition(true)
        binding.ivCoverImagePlaceholder.showWithCondition(true)
        binding.ivCoverPreview.setImageDrawable(ContextCompat.getDrawable(context, unifyR.color.Unify_NN400))
    }

    private fun setCover(imageUrl: String, isShowPlaceholder: Boolean) {
        binding.apply {
            ivCoverImageCircleDash.showWithCondition(isShowPlaceholder)
            ivCoverImagePlaceholder.showWithCondition(isShowPlaceholder)
            ivCoverPreview.setImageUrl(imageUrl)
        }
        isCoverAvailable = true
    }

    fun setTitle(title: String) {
        binding.tvCoverTitle.text = title
    }

    fun setAuthorName(authorName: String) {
        binding.tvCoverAuthorName.text = MethodChecker.fromHtml(authorName)
    }
}
