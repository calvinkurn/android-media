package com.tokopedia.play.broadcaster.view.custom.game.quiz

import android.content.Context
import android.text.InputFilter
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

    private var mTextOnChangedListener: ((String) -> Unit)? = null

    init {
        binding.etQuizOption.afterTextChanged {
            mTextOnChangedListener?.invoke(it)
        }
    }

    var textChoice: String = ""
        set(value) {
            field = value

            binding.tvQuizOptionChoice.text = value
        }

    var textHint: String = ""
        set(value) {
            field = value

            binding.etQuizOption.hint = value
        }

    var maxLength: Int = 0
        set(value) {
            if(field != value) {
                field = value

                binding.etQuizOption.filters = arrayOf(InputFilter.LengthFilter(maxLength))
            }
        }

    var isEditable: Boolean = true
        set(value) {
            field = value

            binding.etQuizOption.apply {
                isFocusable = value
                isFocusableInTouchMode = value
                isEnabled = value
            }
        }

    var isCorrect: Boolean = false
        set(value) {
            if(field != value) {
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
            field = value
            binding.etQuizOption.setText(value)
        }

    fun setOnTextChanged(listener: (String) -> Unit) {
        mTextOnChangedListener = listener
    }
}