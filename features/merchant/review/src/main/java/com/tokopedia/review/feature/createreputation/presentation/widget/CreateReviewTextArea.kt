package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
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
    }

    fun setListener(textAreaListener: TextAreaListener) {
        reviewCreateTextAreaExpandButton.setOnClickListener {
            textAreaListener.onExpandButtonClicked(reviewCreateTextArea.text.toString())
        }
        reviewCreateTextArea.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus) {
                    this@CreateReviewTextArea.createReviewTextAreaContainer.background = ContextCompat.getDrawable(context, R.drawable.bg_review_create_text_area_selected)
                    textAreaListener.apply {
                        trackWhenHasFocus(reviewCreateTextArea.text.isEmpty())
                        scrollToShowTextArea()
                    }
                } else {
                    this@CreateReviewTextArea.createReviewTextAreaContainer.background = ContextCompat.getDrawable(context, R.drawable.bg_review_create_text_area_default)
                }
            }
        }
    }

    fun setText(text: String) {
        reviewCreateTextArea.setText(text)
    }

    fun getText(): String {
        return reviewCreateTextArea.text.toString()
    }
}