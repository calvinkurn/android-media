package com.tokopedia.play.broadcaster.view.custom.game.quiz

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
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
            binding.clLabelView.hide()
            binding.clInputView.show()
        }

        binding.icCloseInputGift.setOnClickListener {
            binding.clLabelView.show()
            binding.clInputView.hide()

            mOnRemovedListener?.invoke()
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

    fun setOnTextChangeListener(listener: (String) -> Unit) {
        mOnChangedListener = listener
    }

    fun setOnRemoveGiftListener(listener: () -> Unit) {
        mOnRemovedListener = listener
    }
}