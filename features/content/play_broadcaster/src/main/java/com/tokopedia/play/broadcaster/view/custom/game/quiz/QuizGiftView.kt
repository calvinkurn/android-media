package com.tokopedia.play.broadcaster.view.custom.game.quiz

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewQuizGiftBinding
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play_common.util.extension.showKeyboard

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
    private val bindingGift = binding.layoutQuizGift
    private val bindingGiftInput = binding.layoutQuizGiftInput

    private var mOnChangedListener: ((String) -> Unit)? = null
    private var mOnClickListener: (() -> Unit)? = null
    private var mOnClickCloseGiftListener: (() -> Unit)? = null

    private val coachMark: CoachMark2 = CoachMark2(context)

    init {
        bindingGift.root.setOnClickListener {
            isEditable {
                bindingGift.root.hide()
                bindingGiftInput.root.show()
                if(isShowCoachmark) {
                    isShowCoachmark = false
                    showCoachmark()
                }
                setFocus(true)
            }
            mOnClickListener?.invoke()
        }

        bindingGiftInput.icCloseInputGift.setOnClickListener {
            isEditable {
                bindingGift.root.show()
                bindingGiftInput.root.hide()
                bindingGiftInput.etBroQuizGift.setText("")
            }
            bindingGiftInput.etBroQuizGift.showKeyboard(false)
            mOnClickCloseGiftListener?.invoke()
        }
        bindingGiftInput.etBroQuizGift.afterTextChanged {
            mOnChangedListener?.invoke(it)
        }
    }

    var maxLength: Int = 0
        set(value) {
            if(field != value) {
                field = value

                bindingGiftInput.etBroQuizGift.filters = arrayOf(InputFilter.LengthFilter(value))
            }
        }

    var gift: String = ""
        set(value) {
            bindingGiftInput.etBroQuizGift.setText(value)
            bindingGiftInput.etBroQuizGift.setSelection(value.length)
        }

    var isEditable: Boolean = true
        set(value) {
            field = value

            bindingGiftInput.etBroQuizGift.apply {
                isFocusable = value
                isFocusableInTouchMode = value
                isEnabled = value
            }

            bindingGiftInput.icCloseInputGift.showWithCondition(isEditable)
            adjustFieldSize(value)
        }

    var isShowCoachmark: Boolean = true

    fun setOnTextChangeListener(listener: (String) -> Unit) {
        mOnChangedListener = listener
    }

    fun setOnClickListener(listener: () -> Unit) {
        mOnClickListener = listener
    }

    fun hideGiftTextFieldIfEmpty() {
        bindingGiftInput.etBroQuizGift.apply {
            if(text.toString().isBlank()) {
                bindingGift.root.show()
                bindingGiftInput.root.hide()
            }
        }
    }

    private fun adjustFieldSize(isEditable: Boolean) {
        bindingGiftInput.etBroQuizGift.apply {
            layoutParams = layoutParams.apply {
                width = if(isEditable) MATCH_CONSTRAINT else WRAP_CONTENT
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
        bindingGiftInput.etBroQuizGift.apply {
            if(isFocus) requestFocus()
            else clearFocus()

            showKeyboard(isFocus)
        }
    }

    fun setOnCloseGiftClickListener(listener: () -> Unit) {
        mOnClickCloseGiftListener = listener
    }
}