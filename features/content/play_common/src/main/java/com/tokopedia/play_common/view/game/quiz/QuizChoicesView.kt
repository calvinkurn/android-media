package com.tokopedia.play_common.view.game.quiz

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play_common.R
import com.tokopedia.play_common.databinding.ItemQuizChoiceBinding
import com.tokopedia.play_common.databinding.ItemQuizChoicesAlphabetBinding
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * @author by astidhiyaa on 04/04/22
 */
class QuizChoicesView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = ItemQuizChoiceBinding.inflate(LayoutInflater.from(context))
    private val bgDrawable = MethodChecker.getDrawable(context, R.drawable.bg_quiz_choice)
    private val defaultFontColor = MethodChecker.getColor(context, unifyR.color.Unify_NN600)
    private val filledFontColor = MethodChecker.getColor(context, unifyR.color.Unify_N0)

    /***
     * if state is default = clickable, after click turn off all clickable? Need to ask
     */

    fun setView(state: PlayQuizOptionState){
        when(state) {
            /**
             * When user clicked one of the option show loader
             */
            PlayQuizOptionState.Loading -> binding.loaderQuizOption.show()
            /**
             * Initial state, bg is default and icon is alphabet - please loop through view holder or view or anything nanti
             */
            is PlayQuizOptionState.Default -> {
                binding.loaderQuizOption.hide()
                binding.quizOption.background = bgDrawable
                binding.ivQuizOption.setImageDrawable(
                    getIconOption(alphabet = state.alphabet)
                )
                binding.tvQuizQuestion.setTextColor(defaultFontColor)
            }
            /**
             * User's answer, icon is white and background is green [correct] and red [false]
             */
            is PlayQuizOptionState.Answered -> {
                binding.loaderQuizOption.hide()
                binding.tvQuizQuestion.setTextColor(filledFontColor)
                if (state.isCorrect) {
                    bgDrawable.setTint(
                        MethodChecker.getColor(
                            rootView.context,
                            unifyR.color.Unify_GN400
                        )
                    )
                    binding.ivQuizOption.setImageDrawable(
                        getIconUnifyDrawable(
                            context,
                            IconUnify.CHECK_CIRCLE,
                            unifyR.color.Unify_N0
                        )
                    )
                } else {
                    bgDrawable.setTint(
                        MethodChecker.getColor(
                            context,
                            unifyR.color.Unify_RN500
                        )
                    )
                    binding.ivQuizOption.setImageDrawable(
                        getIconUnifyDrawable(
                            context,
                            IconUnify.CLEAR,
                            unifyR.color.Unify_N0
                        )
                    )
                }
            }
            /**
             * Other choices beside user's answer, if correct = icon is green and false is red
             */
            is PlayQuizOptionState.Result -> {
                binding.quizOption.background = bgDrawable
                binding.tvQuizQuestion.setTextColor(defaultFontColor)
                binding.loaderQuizOption.hide()
                if (state.isCorrect) {
                    binding.ivQuizOption.setImageDrawable(
                        getIconUnifyDrawable(
                            context,
                            IconUnify.CHECK_CIRCLE,
                            unifyR.color.Unify_GN400
                        )
                    )
                }else{
                    binding.ivQuizOption.setImageDrawable(
                        getIconUnifyDrawable(
                            context,
                            IconUnify.CHECK_CIRCLE,
                            unifyR.color.Unify_RN500
                        )
                    )
                }
            }
        }
    }

    private fun getIconOption(alphabet: Char): Drawable{
        val alphabetView = ItemQuizChoicesAlphabetBinding.inflate(LayoutInflater.from(context))

        alphabetView.tvAlphabet.text = alphabet.toString()

        val bitmap = Bitmap.createBitmap(
            alphabetView.root.measuredWidth,
            alphabetView.root.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
         return BitmapDrawable(context.resources, bitmap)
    }
}
