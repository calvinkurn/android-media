package com.tokopedia.play_common.view.quiz

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play_common.databinding.ItemQuizChoiceBinding
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import com.tokopedia.play_common.R as commonR
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * @author by astidhiyaa on 04/04/22
 */
class QuizChoicesView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val bgDrawable: Drawable
    private val defaultFontColor: Int
    private val filledFontColor: Int

    private val binding = ItemQuizChoiceBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private var mListener: Listener? = null

    init {
        binding.root.setOnClickListener {
            mListener?.onOptionClicked()
        }

        bgDrawable = MethodChecker.getDrawable(context, commonR.drawable.bg_quiz_choice)
        defaultFontColor = MethodChecker.getColor(context, unifyR.color.Unify_NN950)
        filledFontColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
    }

    /***
     * if state is default = clickable, after click turn off all clickable? Need to ask
     */

    fun setupView(item: QuizChoicesUiModel) {
        binding.llQuizOptionRight.tvRespondent.hide()
        binding.llQuizOptionRight.ivArrow.hide()
        when (item.type) {
            /**
             * Initial state, bg is default and icon is alphabet - please loop through view holder or view or anything nanti
             */
            is PlayQuizOptionState.Default -> {
                binding.tvQuizQuestion.setTextColor(defaultFontColor)
                getIconOption(alphabet = item.type.alphabet)
                getBackground(isDefault = true)
            }
            /**
             * User's answer, icon is white and background is green [correct] and red [false]
             */
            is PlayQuizOptionState.Answered -> {
                binding.tvQuizQuestion.setTextColor(filledFontColor)
                getBackground(isCorrect = item.type.isCorrect)
                getIconOption(isCorrect = item.type.isCorrect, isAnswered = true)
            }
            /**
             * Other choices beside user's answer, if correct = icon is green and false is red
             */
            is PlayQuizOptionState.Other -> {
                binding.tvQuizQuestion.setTextColor(MethodChecker.getColor(context, unifyR.color.Unify_NN600))
                getBackground(isDefault = true)
                getIconOption(isCorrect = item.type.isCorrect)
            }
            /**
             * For displaying number of participants in options, if correct = icon is green and the rest is default alphabet
             */
            is PlayQuizOptionState.Participant -> {
                binding.tvQuizQuestion.setTextColor(defaultFontColor)
                if (item.type.isCorrect) {
                    getIconOption(isCorrect = item.type.isCorrect)
                } else {
                    getIconOption(alphabet = item.type.alphabet)
                }
                getBackground(isDefault = true)
                setupResponded(item.type.count)
                binding.llQuizOptionRight.ivArrow.showWithCondition(item.type.showArrow)
            }
            else -> {
                // no op
            }
        }
        binding.tvQuizQuestion.text = item.text
        setupClickable(item.type)
    }

    private fun getIconOption(
        alphabet: Char? = null,
        isCorrect: Boolean? = null,
        isAnswered: Boolean = false
    ) {
        alphabet?.let {
            binding.ivQuizOption.tvAlphabet.show()
            binding.ivQuizOption.ivIcon.hide()
            binding.ivQuizOption.tvAlphabet.text = alphabet.uppercase()
            binding.ivQuizOption.root.background = MethodChecker.getDrawable(context, commonR.drawable.bg_play_option)
        }

        isCorrect?.let {
            binding.ivQuizOption.tvAlphabet.hide()
            binding.ivQuizOption.ivIcon.show()

            val unifyDrawable = getIconUnifyDrawable(
                context,
                if (it) IconUnify.CHECK_CIRCLE else IconUnify.CLEAR,
                when {
                    isAnswered -> MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
                    it -> MethodChecker.getColor(context, unifyR.color.Unify_GN400)
                    else -> MethodChecker.getColor(context, unifyR.color.Unify_RN500)
                }
            )
            binding.ivQuizOption.ivIcon.setImageDrawable(unifyDrawable)
            binding.ivQuizOption.root.background = null
        }
    }

    private fun getBackground(isDefault: Boolean? = null, isCorrect: Boolean? = null) {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = QUIZ_OPTION_RADIUS

        isDefault?.let {
            if (it) {
                shape.setColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
            }
            shape.setStroke(QUIZ_OPTION_STROKE_WIDTH, MethodChecker.getColor(context, commonR.color.play_dms_stroke_quiz))
        }

        isCorrect?.let {
            if (it) {
                shape.setColor(MethodChecker.getColor(context, unifyR.color.Unify_GN400))
            } else {
                shape.setColor(MethodChecker.getColor(context, unifyR.color.Unify_RN500))
            }
        }
        binding.root.background = shape
    }

    private fun setupClickable(type: PlayQuizOptionState) {
        binding.root.isClickable = when (type) {
            is PlayQuizOptionState.Default -> true
            is PlayQuizOptionState.Participant -> true
            else -> false
        }
    }

    /***
     * Use this method if you want to show loader
     */
    fun setupLoading(isLoading: Boolean) {
        binding.llQuizOptionRight.loaderQuizOption.showWithCondition(isLoading)
    }

    /***
     * Use this method if you want to show responded view
     */
    fun setupResponded(respondent: String) {
        binding.root.isClickable = true
        binding.llQuizOptionRight.tvRespondent.text = respondent
        binding.llQuizOptionRight.tvRespondent.show()
        binding.llQuizOptionRight.ivArrow.show()
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    interface Listener {
        fun onOptionClicked()
    }

    companion object {
        private const val QUIZ_OPTION_RADIUS = 20f
        private const val QUIZ_OPTION_STROKE_WIDTH = 2
    }
}
