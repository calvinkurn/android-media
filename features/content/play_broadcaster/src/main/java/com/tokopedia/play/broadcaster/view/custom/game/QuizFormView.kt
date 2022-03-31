package com.tokopedia.play.broadcaster.view.custom.game

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewPlayInteractiveTimePickerBinding
import com.tokopedia.play.broadcaster.databinding.ViewQuizFormBinding
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormStateUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.QuizConfigUiModel
import com.tokopedia.play.broadcaster.util.extension.millisToMinutes
import com.tokopedia.play.broadcaster.util.extension.millisToRemainingSeconds
import com.tokopedia.play_common.databinding.BottomSheetHeaderBinding
import com.tokopedia.play_common.util.extension.doOnLayout
import com.tokopedia.play_common.util.extension.marginLp
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
    private val timePickerBinding = ViewPlayInteractiveTimePickerBinding.bind(binding.root)
    private val bottomSheetHeaderBinding = BottomSheetHeaderBinding.bind(timePickerBinding.root)

    private val bottomSheetBehaviour = BottomSheetBehavior.from(timePickerBinding.clInteractiveTimePicker)

    private var mCloseListener: (() -> Unit)? = null
    private var mNextListener: (() -> Unit)? = null
    private var onTitleChangedListener: ((String) -> Unit)? = null
    private var onGiftChangedListener: ((String) -> Unit)? = null

    init {
        binding.viewGameHeader.type = GameHeaderView.Type.QUIZ
        timePickerBinding.puTimer.infiniteMode = false
        bottomSheetHeaderBinding.ivSheetClose.setImage(IconUnify.ARROW_BACK)
        bottomSheetHeaderBinding.tvSheetTitle.text = context.getString(R.string.play_bro_quiz_set_duration_title)

        binding.tvBroQuizFormNext.setOnClickListener {
            mNextListener?.invoke()
        }

        binding.icCloseQuizForm.setOnClickListener { 
            mCloseListener?.invoke()
        }

        binding.viewGameHeader.setOnTextChangedListener {
            onTitleChangedListener?.invoke(it)
        }

        bottomSheetHeaderBinding.ivSheetClose.setOnClickListener {
            mCloseListener?.invoke()
        }

        timePickerBinding.btnApply.setOnClickListener {

        }

        setupInsets()
    }

    fun setFormData(quizFormData: QuizFormDataUiModel) {
        binding.viewGameHeader.title = quizFormData.title
        /** TODO: set options */

        /** TODO: set gift */

        binding.tvBroQuizFormNext.isEnabled = quizFormData.isFormValid()
    }

    fun setFormState(quizFormState: QuizFormStateUiModel) {
        when(quizFormState) {
            QuizFormStateUiModel.Preparation -> {
                binding.groupActionBar.visibility = View.VISIBLE
                binding.viewGameHeader.isEditable = true

                bottomSheetBehaviour.state = BottomSheetBehavior.STATE_HIDDEN
            }
            QuizFormStateUiModel.SetDuration -> {
                binding.groupActionBar.visibility = View.GONE
                binding.viewGameHeader.isEditable = false


                bottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    fun setQuizConfig(quizConfig: QuizConfigUiModel) {
        /** TODO: set config here */
        binding.viewGameHeader.maxLength = quizConfig.maxTitleLength
        timePickerBinding.puTimer.stringData = quizConfig.eligibleStartTimeInMs.map { formatTime(it) }.toMutableList()
    }

    fun setOnCloseListener(listener: () -> Unit) {
        mCloseListener = listener
    }

    fun setOnNextListener(listener: () -> Unit) {
        mNextListener = listener
    }

    fun setOnTitleChangedListener(listener: (String) -> Unit) {
        onTitleChangedListener = listener
    }

    fun setOnGiftChangedListener(listener: (String) -> Unit) {
        onGiftChangedListener = listener
    }

    private fun setupInsets() {
        binding.clQuizForm.doOnApplyWindowInsets { view, insets, padding, _ ->
            view.updatePadding(
                top = insets.systemWindowInsetTop + padding.top,
                bottom = insets.systemWindowInsetBottom + padding.bottom
            )
        }

        timePickerBinding.btnApply.doOnApplyWindowInsets { view, insets, _, margin ->
            val marginLp = view.marginLp
            marginLp.bottomMargin = margin.bottom + insets.systemWindowInsetBottom
            view.layoutParams = marginLp
        }
    }

    private fun formatTime(millis: Long): String {
        val minute = millis.millisToMinutes()
        val second = millis.millisToRemainingSeconds()

        val stringBuilder = StringBuilder()
        if (minute > 0) stringBuilder.append(context.getString(R.string.play_interactive_minute, minute))
        if (second > 0) stringBuilder.append(context.getString(R.string.play_interactive_second, second))
        return stringBuilder.toString()
    }
}