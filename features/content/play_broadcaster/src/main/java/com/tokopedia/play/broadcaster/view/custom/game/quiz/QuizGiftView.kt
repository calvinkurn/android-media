package com.tokopedia.play.broadcaster.view.custom.game.quiz

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.databinding.ViewQuizGiftBinding

/**
 * Created By : Jonathan Darwin on April 01, 2022
 */
class QuizGiftView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = ViewQuizGiftBinding.inflate(
        LayoutInflater.from(context),
        this,
        true,
    )

    private var mOnChangedListener: ((String) -> Unit)? = null
    private var mOnRemovedListener: (() -> Unit)? = null

    init {
        binding.clLabelView.setOnClickListener {
            isEditable {
                binding.clLabelView.hide()
                binding.clInputView.show()

                setFocus(true)
            }
        }

        binding.icCloseInputGift.setOnClickListener {
            isEditable {
                binding.clLabelView.show()
                binding.clInputView.hide()

                mOnRemovedListener?.invoke()
            }
        }

        binding.etBroQuizGift.afterTextChanged {
            mOnChangedListener?.invoke(it)
        }
    }

    var maxLength: Int = 0
        set(value) {
            if(field != value) {
                field = value

                binding.etBroQuizGift.filters = arrayOf(InputFilter.LengthFilter(maxLength))
            }
        }

    var gift: String = ""
        set(value) {
            field = value

            if(binding.etBroQuizGift.text.toString() != field) {
                binding.etBroQuizGift.setText(value)
                binding.etBroQuizGift.setSelection(value.length)
            }
        }

    var isEditable: Boolean = true
        set(value) {
            field = value

            binding.etBroQuizGift.apply {
                isFocusable = value
                isFocusableInTouchMode = value
                isEnabled = value
            }
        }

    fun setOnTextChangeListener(listener: (String) -> Unit) {
        mOnChangedListener = listener
    }

    fun setOnRemoveGiftListener(listener: () -> Unit) {
        mOnRemovedListener = listener
    }

    private fun isEditable(fn: () -> Unit) {
        if(isEditable) fn.invoke()
    }

    private fun setFocus(isFocus: Boolean) {
        binding.etBroQuizGift.apply {
            if(isFocus) requestFocus()
            else clearFocus()
        }

        showKeyboard(isFocus)
    }

    private fun showKeyboard(isShow: Boolean) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (isShow) imm.showSoftInput(binding.etBroQuizGift, InputMethodManager.SHOW_IMPLICIT)
        else imm.hideSoftInputFromWindow(binding.etBroQuizGift.windowToken, 0)
    }
}