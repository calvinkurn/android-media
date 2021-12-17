package com.tokopedia.review.feature.reviewreply.view.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.review.R
import com.tokopedia.review.databinding.WidgetReplyTextboxBinding
import com.tokopedia.unifycomponents.BaseCustomView

class ReviewReplyTextBox : BaseCustomView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = WidgetReplyTextboxBinding.inflate(LayoutInflater.from(context), this, true)

    fun setReplyAction() {
        binding.replyEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.replySendButton.apply {
                    if (count.isMoreThanZero()) {
                        isEnabled = true
                        setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_cta_send_active
                            )
                        )
                    } else {
                        isEnabled = false
                        setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_cta_send
                            )
                        )
                    }
                }
            }
        })
    }

    fun clickAddTemplate(listener: () -> Unit) {
        binding.btnAddTemplate.setOnClickListener {
            listener.invoke()
        }
    }

    fun getSendButton(): AppCompatImageView{
        return binding.replySendButton
    }

    fun getText(): String {
        return binding.replyEditText.text.toString()
    }

    fun getEditText(): EditText {
        return binding.replyEditText
    }

    fun clearEditText() {
        binding.replyEditText.text?.clear()
    }

    fun getTemplatesRecyclerView(): RecyclerView {
        return binding.listTemplate
    }

    fun getAddTemplateArea(): LinearLayout {
        return binding.addTemplateArea
    }

    fun getAddTemplateButton(): AppCompatImageView {
        return binding.btnAddTemplate
    }
}