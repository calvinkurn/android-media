package com.tokopedia.talk.feature.sellersettings.smartreply.settings.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.talk.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class TalkSmartReplySettingsStatusWidget : BaseCustomView {

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private var talkIsSmartReplyOnLabel: Label? = null
    private var talkSmartReplySettingsDescription: Typography? = null

    private fun init() {
        View.inflate(context, R.layout.widget_talk_smart_reply_settings_detail_entry_point, this)
        bindViews()
        setDescriptionText()
    }

    private fun bindViews() {
        talkIsSmartReplyOnLabel = findViewById(R.id.talkIsSmartReplyOnLabel)
        talkSmartReplySettingsDescription = findViewById(R.id.talkSmartReplySettingsDescription)
    }

    private fun setDescriptionText() {
        talkSmartReplySettingsDescription?.text = context?.let { HtmlLinkHelper(it, context.getString(R.string.smart_reply_settings_description)).spannedString }
    }

    fun setActiveLabel() {
        talkIsSmartReplyOnLabel?.apply {
            setLabel(context.getString(R.string.smart_reply_active_label))
            setLabelType(Label.GENERAL_LIGHT_GREEN)
        }
    }

    fun setInactiveLabel() {
        talkIsSmartReplyOnLabel?.apply {
            setLabel(context.getString(R.string.smart_reply_inactive_label))
            setLabelType(Label.GENERAL_LIGHT_GREY)
        }
    }


}