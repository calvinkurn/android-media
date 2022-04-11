package com.tokopedia.play.view.quiz

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play_common.databinding.ItemQuizChoicesAlphabetBinding
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.play_common.R as commonR
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * @author by astidhiyaa on 04/04/22
 */
class QuizChoicesView : ConstraintLayout{
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val bgDrawable: Drawable
    private val defaultFontColor: Int
    private val filledFontColor: Int

    private val tvQuestion: TextView
    private val ivOption: RelativeLayout
    private val loaderQuiz: LoaderUnify

    init {
        val view = View.inflate(context, com.tokopedia.play.R.layout.item_quiz_choice, this)

        tvQuestion = view.findViewById(commonR.id.tv_quiz_question)
        ivOption = view.findViewById(commonR.id.iv_quiz_option)
        loaderQuiz = view.findViewById(commonR.id.loader_quiz_option)

        bgDrawable = MethodChecker.getDrawable(context, commonR.drawable.bg_quiz_choice)
        defaultFontColor = MethodChecker.getColor(context, unifyR.color.Unify_NN600)
        filledFontColor = MethodChecker.getColor(context, unifyR.color.Unify_N0)
    }

    /***
     * if state is default = clickable, after click turn off all clickable? Need to ask
     */

    fun setupView(item: QuizChoicesUiModel.Complete){
        when(item.type) {
            /**
             * When user clicked one of the option show loader
             */
            PlayQuizOptionState.Loading -> loaderQuiz.show()
            /**
             * Initial state, bg is default and icon is alphabet - please loop through view holder or view or anything nanti
             */
            is PlayQuizOptionState.Default -> {
                loaderQuiz.hide()
                getIconOption(alphabet = item.type.alphabet)
                tvQuestion.setTextColor(defaultFontColor)
            }
            /**
             * User's answer, icon is white and background is green [correct] and red [false]
             */
            is PlayQuizOptionState.Answered -> {
                loaderQuiz.hide()
                tvQuestion.setTextColor(filledFontColor)
                getIconOption(isCorrect = item.type.isCorrect)
                if (item.type.isCorrect) {
                    bgDrawable.setTint(
                        MethodChecker.getColor(
                            context,
                            unifyR.color.Unify_GN400
                        )
                    )
                } else {
                    bgDrawable.setTint(
                        MethodChecker.getColor(
                            context,
                            unifyR.color.Unify_RN500
                        )
                    )
                }
            }
            /**
             * Other choices beside user's answer, if correct = icon is green and false is red
             */
            is PlayQuizOptionState.Result -> {
                tvQuestion.setTextColor(defaultFontColor)
                loaderQuiz.hide()
                getIconOption(isCorrect = item.type.isCorrect)
            }
        }

        this.background = bgDrawable
        tvQuestion.text = item.question
    }

    private fun getIconOption(alphabet: Char? = null, isCorrect: Boolean? = null) {
        if (alphabet != null){
            val alphabetView = ItemQuizChoicesAlphabetBinding.inflate(LayoutInflater.from(context))
            alphabetView.tvAlphabet.text = alphabet.toString()
            alphabetView.bgAlphabet.background = bgDrawable
            ivOption.addView(alphabetView.root)
        }else{
            isCorrect?.let {
                val unifyDrawable = getIconUnifyDrawable(
                    context,
                    if(it) IconUnify.CHECK_CIRCLE else IconUnify.CLEAR,
                    if(it) MethodChecker.getColor(context,unifyR.color.Unify_GN400)
                    else MethodChecker.getColor(context,unifyR.color.Unify_RN500)
                )
                val i = ImageView(context).apply {
                    setBackgroundDrawable(
                        unifyDrawable
                    )
                }
                ivOption.addView(i)
            }
        }
    }
}