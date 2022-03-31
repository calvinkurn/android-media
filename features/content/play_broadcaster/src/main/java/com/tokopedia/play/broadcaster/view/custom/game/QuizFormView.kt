package com.tokopedia.play.broadcaster.view.custom.game

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.play.broadcaster.databinding.ViewQuizFormBinding
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormStateUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.QuizConfigUiModel
import com.tokopedia.play_common.util.extension.doOnLayout
import com.tokopedia.play_common.util.extension.showKeyboard
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.game.GameHeaderView
import com.tokopedia.play_common.view.updatePadding

/**
 * Created By : Jonathan Darwin on March 30, 2022
 */
class QuizFormView : ConstraintLayout {

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

    private val binding = ViewQuizFormBinding.inflate(
        LayoutInflater.from(context),
        this,
        true,
    )

    private var mCloseListener: (() -> Unit)? = null
    private var mNextListener: (() -> Unit)? = null

    init {
        binding.viewGameHeader.type = GameHeaderView.Type.QUIZ

        binding.tvBroQuizFormNext.setOnClickListener {
            mNextListener?.invoke()
        }

        binding.icCloseQuizForm.setOnClickListener { 
            mCloseListener?.invoke()
        }

        setupInsets()
    }

    fun setFormData(quizFormData: QuizFormDataUiModel) {
        binding.viewGameHeader.title = quizFormData.title
        /** TODO: set options */

        /** TODO: set gift */
    }

    fun setFormState(quizFormState: QuizFormStateUiModel) {
        when(quizFormState) {
            QuizFormStateUiModel.Preparation -> {
                binding.viewGameHeader.apply {
                    isEditable = true
                    focus()
                }
            }
            QuizFormStateUiModel.SetDuration -> {
                binding.viewGameHeader.isEditable = false
                /** TODO: hide actionbar */

                /** TODO: show bottomsheet */
            }
        }
    }

    fun setOnCloseListener(listener: () -> Unit) {
        mCloseListener = listener
    }

    fun setOnNextListener(listener: () -> Unit) {
        mNextListener = listener
    }

    fun setQuizConfig(quizConfig: QuizConfigUiModel) {
        /** TODO: set config here */
        binding.viewGameHeader.maxLength = quizConfig.maxTitleLength
    }

    private fun setupInsets() {
        binding.root.doOnApplyWindowInsets { view, insets, padding, _ ->
            view.updatePadding(
                top = insets.systemWindowInsetTop + padding.top,
                bottom = insets.systemWindowInsetBottom + padding.bottom
            )
        }
    }
}