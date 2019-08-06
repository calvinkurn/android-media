package com.tokopedia.hotel.common.presentation.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.hotel.R
import kotlinx.android.synthetic.main.widget_info_text_view.view.*

/**
 * @author by resakemal on 29/04/19
 */

class InfoTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
        BaseCustomView(context, attrs, defStyleAttr) {

    var descriptionLineCount = INFO_DESC_DEFAULT_LINE_COUNT
    var truncateDescription = true

    var infoViewListener: InfoViewListener? = null
    set(value) {
        field = value
        infoViewListener?.let { listener -> info_more.setOnClickListener{ listener.onMoreClicked() } }
    }

    init {
        View.inflate(context, R.layout.widget_info_text_view, this)

        attrs?.let {
            val styledAttributes = context.obtainStyledAttributes(it, R.styleable.InfoTextView)
            try {
                info_title.text = styledAttributes.getString(R.styleable.InfoTextView_titleText) ?: ""
                info_desc.text = styledAttributes.getString(R.styleable.InfoTextView_descriptionText) ?: ""
                truncateDescription = styledAttributes.getBoolean(R.styleable.InfoTextView_truncateDescription, false)

                if (truncateDescription) {
                    info_desc.post {
                        if (info_desc.lineCount > descriptionLineCount) {
                            info_desc.ellipsize = TextUtils.TruncateAt.END
                            info_desc.maxLines = descriptionLineCount
                            info_more.visibility = View.VISIBLE
                        }
                    }
                } else {
                    resetMaxLineCount()
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
        info_title.text = title
    }

    fun setDescription(desc: CharSequence) {
        info_desc.text = desc
    }

    fun setTitleAndDescription(title: CharSequence, desc: CharSequence) {
        setTitle(title)
        setDescription(desc)
    }

    fun buildView() {
        visibility = View.VISIBLE

        if (truncateDescription) {
            info_desc.post {
                if (info_desc.lineCount > descriptionLineCount) {
                    info_desc.ellipsize = TextUtils.TruncateAt.END
                    info_desc.maxLines = descriptionLineCount
                    info_more.visibility = View.VISIBLE
                }
            }
        }
    }

    fun resetMaxLineCount() {
        info_desc.ellipsize = null
        info_desc.maxLines = Integer.MAX_VALUE
        info_more.visibility = View.GONE
    }

    companion object {
        const val INFO_DESC_DEFAULT_LINE_COUNT = 3
    }
}
