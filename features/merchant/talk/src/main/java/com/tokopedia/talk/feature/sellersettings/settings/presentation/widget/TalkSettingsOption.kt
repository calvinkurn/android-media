package com.tokopedia.talk.feature.sellersettings.settings.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class TalkSettingsOption : BaseCustomView {

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private var talkSettingsOptionText: Typography? = null
    private var talkSettingsOptionLabel: Label? = null
    private var talkSettingsOptionChevron: IconUnify? = null

    private fun init() {
        View.inflate(context, R.layout.widget_talk_settings_option, this)
        bindViews()
    }

    private fun init(attrs: AttributeSet) {
        View.inflate(context, R.layout.widget_talk_settings_option, this)
        bindViews()
        val styleable = context.obtainStyledAttributes(attrs, R.styleable.TalkSettingsOption, 0, 0)
        try {
            val optionText = styleable.getString(R.styleable.TalkSettingsOption_option_text) ?: ""
            setText(optionText)
        } finally {
            styleable.recycle()
        }
    }

    private fun bindViews() {
        talkSettingsOptionText = findViewById(R.id.talkSettingsOptionText)
        talkSettingsOptionLabel = findViewById(R.id.talkSettingsOptionLabel)
        talkSettingsOptionChevron = findViewById(R.id.talkSettingsOptionChevron)
    }

    private fun setText(text: String) {
        talkSettingsOptionText?.text = text
    }

    fun showLabel() {
        talkSettingsOptionLabel?.show()
    }

    fun hideLabel() {
        talkSettingsOptionLabel?.hide()
    }
}