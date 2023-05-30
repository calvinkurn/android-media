package com.tokopedia.feedplus.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.feedplus.databinding.ViewUploadFailedBinding
import com.tokopedia.feedplus.databinding.ViewUploadProgressBinding

/**
 * Created by kenny.hadisaputra on 14/03/23
 */
class UploadFailedView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = ViewUploadFailedBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    private var mListener: Listener? = null

    init {
        binding.iconClose.setOnClickListener {
            mListener?.onCloseClicked(this)
        }

        binding.layoutThumbnail.setOnClickListener {
            mListener?.onRetryClicked(this)
        }
    }

    fun setThumbnail(url: String) {
        binding.imgThumbnail.setImageUrl(url)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    interface Listener {
        fun onCloseClicked(view: UploadFailedView)
        fun onRetryClicked(view: UploadFailedView)
    }
}
