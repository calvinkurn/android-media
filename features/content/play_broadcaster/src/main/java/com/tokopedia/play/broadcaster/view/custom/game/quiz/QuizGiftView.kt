package com.tokopedia.play.broadcaster.view.custom.game.quiz

import android.content.Context
import android.graphics.Typeface
import android.text.InputFilter
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewQuizGiftBinding
import kotlinx.android.synthetic.main.view_quiz_gift.view.*
import kotlin.math.max

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

    private val coachMark: CoachMark2 = CoachMark2(context)

    init {
        binding.clLabelView.setOnClickListener {
            isEditable {
                binding.clLabelView.hide()
                binding.clInputView.show()
                if(isShowCoachmark) {
                    isShowCoachmark = false
                    showCoachmark()
                }
                setFocus(true)
            }
        }

        binding.icCloseInputGift.setOnClickListener {
            isEditable {
                binding.clLabelView.show()
                binding.clInputView.hide()
                binding.etBroQuizGift.setText("")
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

                binding.etBroQuizGift.filters = arrayOf(InputFilter.LengthFilter(value))
            }
        }

    var gift: String = ""
        set(value) {
            binding.etBroQuizGift.setText(value)
            binding.etBroQuizGift.setSelection(value.length)
        }

    var isEditable: Boolean = true
        set(value) {
            field = value

            binding.etBroQuizGift.apply {
                isFocusable = value
                isFocusableInTouchMode = value
                isEnabled = value
            }

            binding.icCloseInputGift.showWithCondition(isEditable)
            adjustFieldSize(value)
        }

    var isShowCoachmark: Boolean = true

    fun setOnTextChangeListener(listener: (String) -> Unit) {
        mOnChangedListener = listener
    }

    fun hideGiftTextFieldIfEmpty() {
        binding.etBroQuizGift.apply {
            if(text.toString().isEmpty()) {
                binding.clLabelView.show()
                binding.clInputView.hide()
            }
        }
    }

    private fun adjustFieldSize(isEditable: Boolean) {
        binding.etBroQuizGift.apply {
            layoutParams = layoutParams.apply {
                width = if(isEditable) 0 else WRAP_CONTENT
            }
            hint = if(isEditable) context.getString(R.string.play_bro_quiz_gift_hint) else ""
        }
    }

    fun hideCoackmark() {
        isShowCoachmark = false
        coachMark.dismissCoachMark()
    }

    private fun showCoachmark() {
        coachMark.isDismissed = false
        coachMark.showCoachMark(
            arrayListOf(
                CoachMark2Item(
                    this,
                    "",
                    context.getString(R.string.play_bro_quiz_prize_coachmark),
                    CoachMark2.POSITION_TOP
                )
            )
        )
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

    companion object {
        private const val MAX_TEXT_THRESHOLD = 15
    }
}