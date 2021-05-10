package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
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
        this@CreateReviewTextArea.createReviewTextAreaContainer.setBackgroundResource(R.drawable.bg_review_create_text_area_default)
        createReviewEditText.apply {
            setOnTouchListener(OnTouchListener { v, event ->
                if (createReviewEditText.hasFocus()) {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                    when (event.action and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_SCROLL -> {
                            v.parent.requestDisallowInterceptTouchEvent(false)
                            return@OnTouchListener true
                        }
                    }
                }
                false
            })
            setHintTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
        }
    }

    fun setListener(textAreaListener: TextAreaListener) {
        createReviewExpandButton.setOnClickListener {
            textAreaListener.onExpandButtonClicked(createReviewEditText.text.toString())
        }
        createReviewEditText.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus) {
                    this@CreateReviewTextArea.createReviewTextAreaContainer.setBackgroundResource(R.drawable.bg_review_create_text_area_selected)
                    textAreaListener.apply {
                        trackWhenHasFocus(createReviewEditText.text.length)
                        scrollToShowTextArea()
                    }
                } else {
                    this@CreateReviewTextArea.createReviewTextAreaContainer.setBackgroundResource(R.drawable.bg_review_create_text_area_default)
                    textAreaListener.hideText()
                }
            }
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // No Op
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // No Op
                }

                override fun afterTextChanged(s: Editable?) {
                    textAreaListener.onTextChanged(s?.length ?: 0)
                }

            })
        }
    }

    fun setText(text: String) {
        createReviewEditText.setText(text)
    }

    fun getText(): String {
        return createReviewEditText.text.toString()
    }

    fun append(text: String) {
        createReviewEditText.append(text)
    }

    fun setPlaceHolder(text: String) {
        createReviewEditText.hint = text
    }
}