package com.tokopedia.play.broadcaster.view.custom.game.quiz

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewQuizOptionBinding
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel

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

    init {
        binding.etQuizOption.apply {
            setRawInputType(InputType.TYPE_CLASS_TEXT)

            afterTextChanged {
                mTextOnChangedListener?.invoke(order, it)
            }
        }
    }

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
            needChange(field, value) {
                field = value

                binding.etQuizOption.apply {
                    isFocusable = value
                    isFocusableInTouchMode = value
                    isEnabled = value
                }
            }
        }

    var isCorrect: Boolean = false
        set(value) {
            needChange(field, value) {
                field = value
                binding.apply {
                    tvQuizOptionChoice.showWithCondition(!value)
                    icQuizOptionChecked.showWithCondition(value)

                    root.background = ContextCompat.getDrawable(
                        context,
                        if(value) R.drawable.bg_quiz_option_selected
                        else R.drawable.bg_quiz_option
                    )

                    etQuizOption.setTextColor(
                        ContextCompat.getColor(
                            context,
                            if(value) R.color.Unify_Static_White
                            else R.color.Unify_NN950
                        )
                    )
                }
            }
        }

    var text: String = ""
        set(value) {
            needChange(binding.etQuizOption.text.toString(), value) {
                field = value
                binding.etQuizOption.setText(value)
                binding.etQuizOption.setSelection(value.length)
            }
        }

    fun setOnTextChanged(listener: (Int, String) -> Unit) {
        mTextOnChangedListener = listener
    }

    private fun <T> needChange(prev: T, curr: T, block: () -> Unit) {
        if(prev != curr) block()
    }
}