package com.tokopedia.feedplus.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.feedplus.databinding.ViewUploadProgressBinding

/**
 * Created by kenny.hadisaputra on 14/03/23
 */
class UploadProgressView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = ViewUploadProgressBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    fun setThumbnail(url: String) {
        binding.imgThumbnail.setImageUrl(url)
    }

    fun setProgress(progress: Int, isSmooth: Boolean = true) {
        binding.pbUploading.setValue(progress, isSmooth)
    }

    fun resetProgress() {
        binding.pbUploading.setValue(0, false)
    }
}
