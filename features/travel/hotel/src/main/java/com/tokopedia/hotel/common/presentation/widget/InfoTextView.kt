package com.tokopedia.hotel.common.presentation.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.WidgetInfoTextViewBinding
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BaseCustomView

/**
 * @author by resakemal on 29/04/19
 */

class InfoTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
        BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = WidgetInfoTextViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var descriptionLineCount = INFO_DESC_DEFAULT_LINE_COUNT
    var truncateDescription = true

    var infoViewListener: InfoViewListener? = null
    set(value) {
        field = value
        infoViewListener?.let { listener -> binding.infoMore.setOnClickListener{ listener.onMoreClicked() } }
    }

    init {
        View.inflate(context, R.layout.widget_info_text_view, this)

        attrs?.let {
            val styledAttributes = context.obtainStyledAttributes(it, R.styleable.InfoTextView)
            try {
                with(binding) {
                    infoTitle.text = styledAttributes.getString(R.styleable.InfoTextView_titleText) ?: ""
                    infoDesc.text = styledAttributes.getString(R.styleable.InfoTextView_descriptionText) ?: ""
                    truncateDescription = styledAttributes.getBoolean(R.styleable.InfoTextView_truncateDescription, false)

                    if (truncateDescription) {
                        infoDesc.post {
                            if (infoDesc.lineCount > descriptionLineCount) {
                                infoDesc.ellipsize = TextUtils.TruncateAt.END
                                infoDesc.maxLines = descriptionLineCount
                                infoMore.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        resetMaxLineCount()
                    }
                }
            } finally {
                styledAttributes.recycle()
            }
        }

        infoViewListener = object : InfoViewListener {
            override fun onMoreClicked() {
                resetMaxLineCount()
                invalidate()
            }
        }
    }

    interface InfoViewListener {
        fun onMoreClicked()
    }

    fun setTitle(title: CharSequence) {
        binding.infoTitle.text = title
    }

    fun setDescription(desc: CharSequence) {
        binding.infoDesc.text = desc
    }

    fun setTitleAndDescription(title: CharSequence, desc: CharSequence) {
        setTitle(title)
        setDescription(desc)
    }

    fun buildView() {
        visibility = View.VISIBLE

        if (truncateDescription) {
            with(binding) {
                infoDesc.post {
                    if (infoDesc.lineCount > descriptionLineCount) {
                        infoDesc.ellipsize = TextUtils.TruncateAt.END
                        infoDesc.maxLines = descriptionLineCount
                        infoMore.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    fun resetMaxLineCount() {
        with(binding) {
            infoDesc.ellipsize = null
            infoDesc.maxLines = Integer.MAX_VALUE
            infoMore.visibility = View.GONE
        }
    }

    fun setInfoTitleTextSize(titleSize: Float) {
        binding.infoTitle.setTextSize(
            TypedValue.COMPLEX_UNIT_SP,
            titleSize
        )
    }

    fun setInfoContainerMargin(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        binding.infoContainer.setMargin(left, top, right, bottom)
    }

    fun setInfoMoreVisible() {
        binding.infoMore.show()
    }

    fun setInfoDescription(text: String) {
        binding.infoDesc.text = text
    }

    companion object {
        const val INFO_DESC_DEFAULT_LINE_COUNT = 3
    }
}
