package com.tokopedia.talk.feature.write.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.talk.R
import com.tokopedia.talk.databinding.WidgetTalkWriteCategoryDetailsBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.HtmlLinkHelper

class TalkWriteCategoryDetails : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private val binding = WidgetTalkWriteCategoryDetailsBinding.bind(this)

    private fun init() {
        View.inflate(context, R.layout.widget_talk_write_category_details, this)
    }

    fun setContent(content: String) {
        if (content.isEmpty()) {
            return
        }
        binding.writeCategoryDetailTitle.text = HtmlLinkHelper(context, content).spannedString
    }
}