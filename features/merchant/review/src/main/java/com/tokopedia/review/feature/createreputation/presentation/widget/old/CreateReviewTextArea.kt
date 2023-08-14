package com.tokopedia.review.feature.createreputation.presentation.widget.old

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View.OnTouchListener
import androidx.core.content.ContextCompat
import com.tokopedia.review.R
import com.tokopedia.review.databinding.OldWidgetCreateReviewTextAreaBinding
import com.tokopedia.review.feature.createreputation.presentation.listener.TextAreaListener
import com.tokopedia.unifycomponents.BaseCustomView


class CreateReviewTextArea : BaseCustomView {

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

    private val binding = OldWidgetCreateReviewTextAreaBinding.inflate(LayoutInflater.from(context), this, true)

    override fun clearFocus() {
        binding.createReviewEditText.clearFocus()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        binding.createReviewTextAreaContainer.setBackgroundResource(R.drawable.bg_review_create_text_area_default)
        binding.createReviewEditText.apply {
            setOnTouchListener(OnTouchListener { v, event ->
                if (binding.createReviewEditText.hasFocus()) {
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
            setHintTextColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950_44
                )
            )
        }
    }

    fun setListener(textAreaListener: TextAreaListener) {
        binding.createReviewExpandButton.setOnClickListener {
            textAreaListener.onExpandButtonClicked(binding.createReviewEditText.text.toString())
        }
        binding.createReviewEditText.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.createReviewTextAreaContainer.setBackgroundResource(R.drawable.bg_review_create_text_area_selected)
                    textAreaListener.apply {
                        trackWhenHasFocus(binding.createReviewEditText.text.length)
                        scrollToShowTextArea()
                    }
                } else {
                    binding.createReviewTextAreaContainer.setBackgroundResource(R.drawable.bg_review_create_text_area_default)
                }
            }
        }
    }

    fun setText(text: String) {
        binding.createReviewEditText.setText(text)
    }

    fun getText(): String {
        return binding.createReviewEditText.text.toString()
    }

    fun isEmpty(): Boolean {
        return binding.createReviewEditText.text.isEmpty()
    }
}