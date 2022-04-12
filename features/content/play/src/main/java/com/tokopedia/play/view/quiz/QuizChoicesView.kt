package com.tokopedia.play.view.quiz

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
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

    private val tvQuestion: Typography
    private val loaderQuiz: LoaderUnify

    private val ivOption: ConstraintLayout
    private val ivAlphabet: TextView
    private val ivIcon: IconUnify

    init {
        val view = View.inflate(context, R.layout.item_quiz_choice, this)

        tvQuestion = view.findViewById(R.id.tv_quiz_question)
        loaderQuiz = view.findViewById(R.id.loader_quiz_option)

        bgDrawable = MethodChecker.getDrawable(context, commonR.drawable.bg_quiz_choice)
        defaultFontColor = MethodChecker.getColor(context, unifyR.color.Unify_NN600)
        filledFontColor = MethodChecker.getColor(context, unifyR.color.Unify_N0)

        ivOption = view.findViewById(R.id.iv_quiz_option)
        ivAlphabet = ivOption.findViewById(R.id.tv_alphabet)
        ivIcon = ivOption.findViewById(R.id.iv_icon)
    }

    /***
     * if state is default = clickable, after click turn off all clickable? Need to ask
     */

    fun setupView(item: QuizChoicesUiModel.Complete) {
        when (item.type) {
            /**
             * When user clicked one of the option show loader
             */
            PlayQuizOptionState.Loading -> loaderQuiz.show()
            /**
             * Initial state, bg is default and icon is alphabet - please loop through view holder or view or anything nanti
             */
            is PlayQuizOptionState.Default -> {
                loaderQuiz.hide()
                getIconOption(alphabet = (item.type as PlayQuizOptionState.Default).alphabet)
                tvQuestion.setTextColor(defaultFontColor)
                this.background = bgDrawable
            }
            /**
             * User's answer, icon is white and background is green [correct] and red [false]
             */
            is PlayQuizOptionState.Answered -> {
                loaderQuiz.hide()
                tvQuestion.setTextColor(filledFontColor)
                getIconOption(isCorrect = (item.type as PlayQuizOptionState.Answered).isCorrect, isAnswered = true)
                this.setBackgroundColor(
                    MethodChecker.getColor(
                        context,
                        if ((item.type as PlayQuizOptionState.Answered).isCorrect) unifyR.color.Unify_GN400 else unifyR.color.Unify_RN500
                    )
                )
            }
            /**
             * Other choices beside user's answer, if correct = icon is green and false is red
             */
            is PlayQuizOptionState.Result -> {
                tvQuestion.setTextColor(defaultFontColor)
                loaderQuiz.hide()
                getIconOption(isCorrect = (item.type as PlayQuizOptionState.Result).isCorrect)
                this.background = bgDrawable
            }
        }
        tvQuestion.text = item.question
    }

    private fun getIconOption(
        alphabet: Char? = null,
        isCorrect: Boolean? = null,
        isAnswered: Boolean = false
    ) {
        alphabet?.let {
            ivAlphabet.show()
            ivIcon.hide()
            ivAlphabet.text = alphabet.uppercase()
            ivOption.background = MethodChecker.getDrawable(context, R.drawable.bg_play_option)
        }

        isCorrect?.let {
            ivAlphabet.hide()
            ivIcon.show()

            val unifyDrawable = getIconUnifyDrawable(
                context,
                if (it) IconUnify.CHECK_CIRCLE else IconUnify.CLEAR,
                when {
                    isAnswered -> MethodChecker.getColor(context, unifyR.color.Unify_N0)
                    it -> MethodChecker.getColor(context, unifyR.color.Unify_GN400)
                    else -> MethodChecker.getColor(context, unifyR.color.Unify_RN500)
                }
            )
            ivIcon.setImageDrawable(unifyDrawable)
        }
    }

}