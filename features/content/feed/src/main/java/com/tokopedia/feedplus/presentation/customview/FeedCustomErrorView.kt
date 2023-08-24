package com.tokopedia.feedplus.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.tokopedia.feedplus.databinding.LayoutFeedErrorBinding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedCustomErrorView : RelativeLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = LayoutFeedErrorBinding.inflate(LayoutInflater.from(context), this, true)

    /**
     * important! to set iconUnify only
     * todo: create @IconUnify annotation & linter
     */
    fun setIcon(iconUnify: Int) {
        binding.iconFeedNoContent.setImage(iconUnify)
    }

    fun setTitle(label: String) {
        if (label.isBlank()) {
            binding.tyFeedNoContentTitle.gone()
        } else {
            binding.tyFeedNoContentTitle.text = label
            binding.tyFeedNoContentTitle.visible()
        }
    }

    fun setDescription(label: String) {
        if (label.isBlank()) {
            binding.tyFeedNoContentSubtitle.gone()
        } else {
            binding.tyFeedNoContentSubtitle.text = label
            binding.tyFeedNoContentSubtitle.visible()
        }
    }

    fun setButton(label: String, onClickListener: () -> Unit) {
        binding.btnShowOtherContent.text = label
        binding.btnShowOtherContent.setOnClickListener {
            onClickListener()
        }
    }
}
