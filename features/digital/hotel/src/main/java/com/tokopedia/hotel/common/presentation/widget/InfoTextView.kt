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

    var descriptionLineCount = INFO_DESC_DEFAULT_LINE_COUNT
    var truncateDescription = true

    var infoViewListener: InfoViewListener? = null

    constructor(context: Context) : super(context) { init() }
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) { init() }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr) { init() }


    fun init() {
        View.inflate(context, R.layout.widget_info_text_view, this)
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

    fun setTitleAndDescription(title: CharSequence, desc: CharSequence) {
        info_title.text = title
        info_desc.text = desc
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

        infoViewListener.let { listener -> info_more.setOnClickListener{ listener?.onMoreClicked() } }
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
