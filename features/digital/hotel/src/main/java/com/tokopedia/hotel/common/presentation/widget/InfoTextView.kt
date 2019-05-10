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

class InfoTextView: BaseCustomView {

    var infoTitle: CharSequence = ""
    var infoDescription: CharSequence = ""
    var descriptionLineCount = INFO_DESC_DEFAULT_LINE_COUNT
    var truncateDescription = true

    var infoViewListener: InfoViewListener? = null

    interface InfoViewListener {
        fun onMoreClicked()
    }

    constructor(context: Context) : super(context) { init() }
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) { init() }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) { init() }

    fun init() {
        View.inflate(context, R.layout.widget_info_text_view, this)
        infoViewListener = object : InfoViewListener {
            override fun onMoreClicked() {
                resetMaxLineCount()
                invalidate()
            }
        }
    }

    fun buildView() {
        visibility = View.VISIBLE
        info_title.setText(infoTitle)

        info_desc.setText(infoDescription)
        if (truncateDescription) {
            info_desc.post {
                if (info_desc.lineCount > descriptionLineCount) {
                    info_desc.setEllipsize(TextUtils.TruncateAt.END)
                    info_desc.setMaxLines(descriptionLineCount)
                    info_more.visibility = View.VISIBLE
                }
            }
        }

        infoViewListener?.let { listener -> info_more.setOnClickListener{ listener.onMoreClicked() } }
    }

    fun resetMaxLineCount() {
        info_desc.setEllipsize(null)
        info_desc.setMaxLines(Integer.MAX_VALUE)
        info_more.visibility = View.GONE
    }

    companion object {
        val INFO_DESC_DEFAULT_LINE_COUNT = 3
    }
}
