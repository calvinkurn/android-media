package com.tokopedia.play.broadcaster.view.custom.game.quiz

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewPlayInteractiveTimePickerBinding
import com.tokopedia.play.broadcaster.databinding.ViewQuizFormBinding
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormStateUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.QuizConfigUiModel
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import com.tokopedia.play.broadcaster.util.extension.millisToMinutes
import com.tokopedia.play.broadcaster.util.extension.millisToRemainingSeconds
import com.tokopedia.play.broadcaster.util.extension.showErrorToaster
import com.tokopedia.play_common.util.extension.marginLp
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.game.GameHeaderView
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.play_common.view.updatePadding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

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

    /** Binding */
    private val binding = ViewQuizFormBinding.inflate(
        LayoutInflater.from(context),
        this,
        true,
    )
    private val timePickerBinding = ViewPlayInteractiveTimePickerBinding.bind(binding.root)

    private val bottomSheetBehaviour = BottomSheetBehavior.from(timePickerBinding.clInteractiveTimePicker)

    private val offset8 = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
    private val offset16 = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

    private val eventBus = EventBus<Event>()

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private var quizConfig: QuizConfigUiModel = QuizConfigUiModel.empty()
        set(value) {
            if(field != value) {
                field = value

                binding.viewGameHeader.maxLength = value.maxTitleLength
                timePickerBinding.puTimer.stringData = quizConfig.eligibleStartTimeInMs.map { formatTime(it) }.toMutableList()
            }
        }

    private val optionListView: MutableList<QuizOptionView> = mutableListOf()

    init {
        binding.viewGameHeader.type = GameHeaderView.Type.QUIZ
        timePickerBinding.puTimer.infiniteMode = false
        timePickerBinding.tvSheetTitle.text = context.getString(R.string.play_bro_quiz_set_duration_title)

        binding.tvBroQuizFormNext.setOnClickListener {
            eventBus.emit(Event.Next)
        }

        binding.icCloseQuizForm.setOnClickListener {
            eventBus.emit(Event.Close)
            eventBus.emit(Event.Back)
        }

        binding.viewGameHeader.setOnTextChangedListener {
            eventBus.emit(Event.TitleChanged(it))
        }

        timePickerBinding.ivSheetClose.setOnClickListener {
            eventBus.emit(Event.BackSelectDuration)
            eventBus.emit(Event.Back)
        }

        timePickerBinding.puTimer.onValueChanged = { _, index ->
            val selectedDuration = quizConfig.availableStartTimeInMs.getOrNull(index) ?: DEFAULT_DURATION
            eventBus.emit(Event.SelectDuration(selectedDuration))
        }

        timePickerBinding.btnApply.setOnClickListener {
            eventBus.emit(Event.Submit)
        }

        setupInsets()
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if(visibility == View.VISIBLE) {
            scope.launch {
                delay(SHOW_KEYBOARD_DELAY)
                binding.viewGameHeader.setFocus(true)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        job.cancel()
    }

    fun setFormData(quizFormData: QuizFormDataUiModel, needToUpdateUI: Boolean = true) {
        if(needToUpdateUI) {
            /** Update Quiz Title */
            binding.viewGameHeader.title = quizFormData.title

            /** Update Quiz Option */
            val options = quizFormData.options
            if(options.size > optionListView.size) {
                /** Add Field */
                for(i in optionListView.size until options.size) {
                    val newOptionView = createNewOptionView(options[i])
                    optionListView.add(newOptionView)
                    binding.llOptionContainer.addView(newOptionView)

                    val marginLayoutParams = newOptionView.layoutParams as MarginLayoutParams
                    marginLayoutParams.updateMargins(bottom = offset16)
                }
            }
            else if(options.size < optionListView.size) {
                /** Remove Field */
                for(i in options.size until optionListView.size) {
                    val removedOptionView = optionListView.removeAt(i)
                    binding.llOptionContainer.removeView(removedOptionView)
                }
            }

            options.zip(optionListView).forEach { (option, optionView) ->
                bindOptionData(optionView, option)
            }

            /** Update Quiz Duration */
            val idx = quizConfig.eligibleStartTimeInMs.indexOf(quizFormData.durationInMs)
            if(timePickerBinding.puTimer.activeIndex != idx) {
                timePickerBinding.puTimer.apply {
                    if(idx != -1) goToPosition(idx)
                    else if(quizConfig.eligibleStartTimeInMs.isNotEmpty()) goToPosition(0)
                }
            }
        }

        /** Validate Form */
        setEnabledContinue(quizFormData.isFormValid())
    }

    private fun setEnabledContinue(shouldEnable: Boolean) {
        binding.tvBroQuizFormNext.isClickable = shouldEnable
        binding.tvBroQuizFormNext.alpha =
            if (shouldEnable) CONTINUE_ENABLED_ALPHA
            else CONTINUE_DISABLED_ALPHA
    }

    fun setFormState(quizFormState: QuizFormStateUiModel) {
        when(quizFormState) {
            QuizFormStateUiModel.Preparation -> {
                binding.groupActionBar.visibility = View.VISIBLE
                binding.viewGameHeader.isEditable = true

                bottomSheetBehaviour.state = BottomSheetBehavior.STATE_HIDDEN
            }
            is QuizFormStateUiModel.SetDuration -> {
                binding.groupActionBar.visibility = View.GONE
                binding.viewGameHeader.isEditable = false

                bottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED

                timePickerBinding.btnApply.apply {
                    isLoading = quizFormState.isLoading
                    isEnabled = !quizFormState.isLoading
                }
            }
        }
    }

    fun listen(): Flow<Event> {
        return eventBus.subscribe()
    }

    fun applyQuizConfig(quizConfig: QuizConfigUiModel) {
        this.quizConfig = quizConfig
    }

    fun setError(throwable: Throwable) {
        showErrorToaster(
            err = throwable,
            bottomMargin = timePickerBinding.btnApply.height + offset8
        )
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
        if (minute > 0) stringBuilder.append(context.getString(R.string.play_interactive_minute, minute)).append(" ")
        if (second > 0) stringBuilder.append(context.getString(R.string.play_interactive_second, second))
        return stringBuilder.toString()
    }

    private fun createNewOptionView(option: QuizFormDataUiModel.Option): QuizOptionView {
        return bindOptionData(QuizOptionView(context), option).apply {
            setOnCheckedListener { eventBus.emit(Event.SelectQuizOption(option.order)) }
            setOnTextChanged { order, text -> eventBus.emit(Event.OptionChanged(order, text)) }
        }
    }

    private fun bindOptionData(optionView: QuizOptionView, option: QuizFormDataUiModel.Option): QuizOptionView {
        return optionView.apply {
            /** Open editable to rebind data */
            isEditable = true

            order = option.order
            text = option.text
            textChoice = option.getTextChoice()
            textHint = if(option.isMandatory) context.getString(R.string.play_bro_quiz_hint_text, option.order + 1)
                        else context.getString(R.string.play_bro_quiz_hint_add_new_option)
            maxLength = quizConfig.maxChoiceLength
            isMandatory = option.isMandatory
            isCorrect = option.isSelected

            setFocus(option.isFocus)

            showCoachmark(option.isShowCoachmark)

            isEditable = option.isEditable
        }
    }

    sealed interface Event {
        object Back: Event
        object Next: Event
        data class TitleChanged(val title: String): Event
        data class OptionChanged(val order: Int, val text: String): Event
        data class SelectQuizOption(val order: Int): Event
        data class SaveQuizData(val quizFormData: QuizFormDataUiModel): Event
        data class SelectDuration(val duration: Long): Event
        object Submit: Event
        object Close : Event
        object BackSelectDuration : Event
    }

    companion object {
        private const val SHOW_KEYBOARD_DELAY = 500L
        private const val CONTINUE_DISABLED_ALPHA = 0.5f
        private const val CONTINUE_ENABLED_ALPHA = 1f
        private const val DEFAULT_DURATION = 180000L
    }
}
