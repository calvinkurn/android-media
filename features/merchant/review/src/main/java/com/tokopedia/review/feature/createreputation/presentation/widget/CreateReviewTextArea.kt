package com.tokopedia.review.feature.createreputation.presentation.widget

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.tokopedia.review.R
import com.tokopedia.review.databinding.WidgetCreateReviewTextAreaBinding
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

    private val binding =
        WidgetCreateReviewTextAreaBinding.inflate(LayoutInflater.from(context), this, true)

    override fun clearFocus() {
        binding.createReviewEditText.clearFocus()
    }

    fun requestFocusForEditText() {
        binding.createReviewEditText.apply {
            requestFocus()
            val imm: InputMethodManager? =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
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
                    com.tokopedia.unifyprinciples.R.color.Unify_N700_44
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
                    textAreaListener.hideText()
                }
            }
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
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
        binding.createReviewEditText.setText(text)
    }

    fun getText(): String {
        return binding.createReviewEditText.text.toString()
    }

    fun append(text: String) {
        binding.createReviewEditText.append(text)
    }

    fun setPlaceHolder(text: String) {
        binding.createReviewEditText.hint = text
    }

    fun getPlaceHolder(): String {
        return binding.createReviewEditText.hint.toString()
    }

    fun isEmpty(): Boolean {
        return binding.createReviewEditText.text.isEmpty()
    }
}