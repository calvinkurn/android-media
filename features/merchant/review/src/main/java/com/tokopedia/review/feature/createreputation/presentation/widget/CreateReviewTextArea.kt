package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.presentation.listener.TextAreaListener
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_create_review_text_area.view.*

class CreateReviewTextArea : BaseCustomView {

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.widget_create_review_text_area, this)
        createReviewEditText.setHintTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_32))
    }

    fun setListener(textAreaListener: TextAreaListener) {
        createReviewExpandButton.setOnClickListener {
            textAreaListener.onExpandButtonClicked(createReviewEditText.text.toString())
        }
        createReviewEditText.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus) {
                    this@CreateReviewTextArea.createReviewTextAreaContainer.background = ContextCompat.getDrawable(context, R.drawable.bg_review_create_text_area_selected)
                    textAreaListener.apply {
                        trackWhenHasFocus(createReviewEditText.text.isBlank())
                        scrollToShowTextArea()
                    }
                } else {
                    this@CreateReviewTextArea.createReviewTextAreaContainer.background = ContextCompat.getDrawable(context, R.drawable.bg_review_create_text_area_default)
                }
            }
        }
    }

    fun setText(text: String) {
        createReviewEditText.setText(text)
    }

    fun getText(): String {
        return createReviewEditText.text.toString()
    }
}