package com.tokopedia.play.broadcaster.view.custom.preparation

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroPreparationCoverFormBinding
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.getBitmapFromUrl
import com.tokopedia.play_common.view.updateMargins
import kotlinx.coroutines.*

/**
 * Created By : Jonathan Darwin on January 26, 2022
 */
class CoverFormView : ConstraintLayout {
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

    private val binding = ViewPlayBroPreparationCoverFormBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private var mListener: Listener? = null

    private var isCoverAvailable = false

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    init {
        binding.icCloseCoverForm.setOnClickListener { mListener?.onCloseCoverForm() }
        binding.clCoverFormPreview.setOnClickListener {
            mListener?.onClickCoverPreview(isCoverAvailable)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding.icCloseCoverForm.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as MarginLayoutParams
            val newTopMargin = margin.top + insets.systemWindowInsetTop
            if (marginLayoutParams.topMargin != newTopMargin) {
                marginLayoutParams.updateMargins(top = newTopMargin)
                v.parent.requestLayout()
            }
        }

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
        mListener = null
        job.cancelChildren()
    }

    fun setCover(imageUrl: String) {
        binding.apply {
            ivCoverImageCircleDash.visibility = View.GONE
            ivCoverImagePlaceholder.visibility = View.GONE
            ivCoverFormPreview.setImageUrl(imageUrl)
        }
        isCoverAvailable = true
    }

    fun setTitle(title: String) {
        binding.tvCoverFormTitle.text = title
    }

    fun setShopName(shopName: String) {
        binding.tvCoverFormShopName.text = shopName
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    interface Listener {
        fun onCloseCoverForm()
        fun onClickCoverPreview(isEditCover: Boolean)
    }
}