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
    private val bgDrawable = MethodChecker.getDrawable(rootView.context, R.drawable.bg_quiz_choice)

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
            }
            /**
             * User's answer, if icon is white and background is green [correct] and red [false]
             */
            is PlayQuizOptionState.Answered -> {
                binding.loaderQuizOption.hide()
                if (state.isCorrect) {
                    bgDrawable.setTint(
                        MethodChecker.getColor(
                            rootView.context,
                            unifyR.color.Unify_G500
                        )
                    )
                    binding.ivQuizOption.setImageDrawable(
                        getIconUnifyDrawable(
                            context,
                            IconUnify.CHECK_CIRCLE
                        )
                    )
                } else {
                    bgDrawable.setTint(
                        MethodChecker.getColor(
                            rootView.context,
                            unifyR.color.Unify_R500
                        )
                    )
                    binding.ivQuizOption.setImageDrawable(
                        getIconUnifyDrawable(
                            context,
                            IconUnify.CLEAR
                        )
                    )
                }
            }
            /**
             * Other choices beside user's answer, if correct = icon is green and false is red
             */
            is PlayQuizOptionState.Result -> {
                binding.quizOption.background = bgDrawable
                binding.loaderQuizOption.hide()
                if (state.isCorrect) {
                    bgDrawable.setTint(
                        MethodChecker.getColor(
                            rootView.context,
                            unifyR.color.Unify_G500
                        )
                    )
                    binding.ivQuizOption.setImageDrawable(
                        getIconUnifyDrawable(
                            context,
                            IconUnify.CHECK_CIRCLE
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
