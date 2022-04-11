package com.tokopedia.play.broadcaster.view.custom.game.quiz

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewQuizOptionBinding
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created By : Jonathan Darwin on April 04, 2022
 */
class QuizOptionView : ConstraintLayout {

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

    private val binding = ViewQuizOptionBinding.inflate(
        LayoutInflater.from(context),
        this,
        true,
    )

    private var mTextOnChangedListener: ((Int, String) -> Unit)? = null
    private var mOnCheckedListener: ((Int) -> Unit)? = null

    private val coachMark: CoachMark2 = CoachMark2(context)

    init {
        binding.flQuizOption.setOnClickListener {
            if(isEditable) mOnCheckedListener?.invoke(order)
        }

        binding.etQuizOption.apply {
            setRawInputType(InputType.TYPE_CLASS_TEXT)

            afterTextChanged {
                if(flagTriggerTextChange) {
                    updateIconChoice(it)
                    mTextOnChangedListener?.invoke(order, it)
                }
                else flagTriggerTextChange = true
            }
        }
    }

    var flagTriggerTextChange = true

    var order: Int = -1

    var textChoice: String = ""
        set(value) {
            needChange(field, value) {
                field = value

                binding.tvQuizOptionChoice.text = value
            }
        }

    var textHint: String = ""
        set(value) {
            needChange(field, value) {
                field = value

                binding.etQuizOption.hint = value
            }
        }

    var maxLength: Int = 0
        set(value) {
            needChange(field, value) {
                field = value

                binding.etQuizOption.filters = arrayOf(InputFilter.LengthFilter(maxLength))
            }
        }

    var isEditable: Boolean = true
        set(value) {
            if(field != value) {
                field = value

                binding.etQuizOption.apply {
                    isFocusable = value
                    isFocusableInTouchMode = value
                }

                setFocus(value)
            }
        }

    var isMandatory: Boolean = true

    var isCorrect: Boolean = false
        set(value) {
            needChange(field, value) {
                field = value
                binding.apply {
                    updateIconChoice()

                    root.background = ContextCompat.getDrawable(
                        context,
                        if(value) R.drawable.bg_quiz_option_selected
                        else R.drawable.bg_quiz_option
                    )

                    etQuizOption.setTextColor(
                        ContextCompat.getColor(
                            context,
                            if(value) unifyR.color.Unify_Static_White
                            else unifyR.color.Unify_NN950
                        )
                    )
                }
            }
        }

    var text: String = ""
        set(value) {
            needChange(binding.etQuizOption.text.toString(), value) {
                field = value

                flagTriggerTextChange = false
                binding.etQuizOption.setText(value)
                binding.etQuizOption.setSelection(value.length)
            }
        }

    fun setFocus(isFocus: Boolean) {
        binding.etQuizOption.apply {
            if(isFocus) {
                requestFocus()
            }
        }
    }

    fun showCoachmark(isShow: Boolean) {
        if(isShow) {
            coachMark.showCoachMark(
                arrayListOf(
                    CoachMark2Item(
                        binding.flQuizOption,
                        "",
                        context.getString(R.string.play_bro_select_quiz_option_coachmark),
                        CoachMark2.POSITION_TOP
                    )
                )
            )
        }
        else {
            coachMark.dismissCoachMark()
        }
    }

    fun setOnTextChanged(listener: (Int, String) -> Unit) {
        mTextOnChangedListener = listener
    }

    fun setOnCheckedListener(listener: (Int) -> Unit) {
        mOnCheckedListener = listener
    }

    private fun updateIconChoice(text: String = binding.etQuizOption.text.toString()) {
        binding.tvQuizOptionChoice.showWithCondition(!isCorrect && (isMandatory || text.isNotEmpty()))
        binding.icQuizOptionOptional.showWithCondition(!isCorrect && !isMandatory && text.isEmpty())
        binding.icQuizOptionChecked.showWithCondition(isCorrect)
    }

    private fun <T> needChange(prev: T, curr: T, block: () -> Unit) {
        if(prev != curr && isEditable) block()
    }
}