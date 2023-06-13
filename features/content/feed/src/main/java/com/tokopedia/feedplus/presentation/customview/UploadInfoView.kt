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

    private val binding = ViewUploadInfoBinding.inflate(
        LayoutInflater.from(context),
        this,
    )

    private var mListener: Listener? = null

    init {
        binding.failed.setListener(object : UploadFailedView.Listener {
            override fun onCloseClicked(view: UploadFailedView) {
                mListener?.onCloseWhenFailedClicked(this@UploadInfoView)
            }

            override fun onRetryClicked(view: UploadFailedView) {
                mListener?.onRetryClicked(this@UploadInfoView)
            }
        })
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
        binding.progress.resetProgress()
        binding.progress.hide()
        binding.failed.show()
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    interface Listener {
        fun onRetryClicked(view: UploadInfoView)
        fun onCloseWhenFailedClicked(view: UploadInfoView)
    }
}
