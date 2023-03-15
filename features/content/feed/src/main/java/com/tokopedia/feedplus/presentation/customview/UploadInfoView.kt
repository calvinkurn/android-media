package com.tokopedia.feedplus.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.feedplus.databinding.ViewUploadInfoBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created by kenny.hadisaputra on 15/03/23
 */
class UploadInfoView : FrameLayout {

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

    private val binding = ViewUploadInfoBinding.inflate(
        LayoutInflater.from(context),
        this,
    )

    private var mListener: Listener? = null

    init {
        binding.failed.setOnClickListener {
            mListener?.onRetryClicked(this)
        }
    }

    fun setThumbnail(thumbnailUrl: String) {
        binding.progress.setThumbnail(thumbnailUrl)
        binding.failed.setThumbnail(thumbnailUrl)
    }

    fun setProgress(progress: Int) {
        binding.progress.show()
        binding.progress.setProgress(progress)
        binding.failed.hide()
    }

    fun setFailed() {
        binding.progress.hide()
        binding.failed.show()
    }

    fun setRetryWhenFailed(onRetry: () -> Unit) {
        binding.failed.setOnClickListener { onRetry() }
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    interface Listener {
        fun onRetryClicked(view: UploadInfoView)
    }
}
