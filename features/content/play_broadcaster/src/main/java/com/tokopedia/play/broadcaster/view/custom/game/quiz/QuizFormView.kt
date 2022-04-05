package com.tokopedia.play.broadcaster.view.custom.game.quiz

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewPlayInteractiveTimePickerBinding
import com.tokopedia.play.broadcaster.databinding.ViewQuizFormBinding
import com.tokopedia.play.broadcaster.ui.itemdecoration.QuizOptionItemDecoration
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormStateUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.QuizConfigUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.game.QuizOptionViewHolder
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import com.tokopedia.play.broadcaster.util.extension.millisToMinutes
import com.tokopedia.play.broadcaster.util.extension.millisToRemainingSeconds
import com.tokopedia.play.broadcaster.util.extension.showErrorToaster
import com.tokopedia.play.broadcaster.view.adapter.QuizOptionAdapter
import com.tokopedia.play_common.databinding.BottomSheetHeaderBinding
import com.tokopedia.play_common.util.extension.marginLp
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.game.GameHeaderView
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
    private val bottomSheetHeaderBinding = BottomSheetHeaderBinding.bind(timePickerBinding.root)

    private val bottomSheetBehaviour = BottomSheetBehavior.from(timePickerBinding.clInteractiveTimePicker)

    val offset8 = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)

    private val eventBus = EventBus<Event>()

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val adapter = QuizOptionAdapter(object : QuizOptionViewHolder.Listener {
        override fun onOptionChecked(order: Int) {
//            eventBus.emit(Event.SelectQuizOption(order))
        }

        override fun onTextChanged(order: Int, text: String) {
//            eventBus.emit(Event.OptionChanged(order, text))
        }
    })

    private var quizConfig: QuizConfigUiModel = QuizConfigUiModel.empty()
        set(value) {
            field = value

            /** TODO: set config here */
            binding.viewGameHeader.maxLength = quizConfig.maxTitleLength
            binding.viewQuizGift.maxLength = quizConfig.maxRewardLength
            timePickerBinding.puTimer.stringData = quizConfig.eligibleStartTimeInMs.map { formatTime(it) }.toMutableList()
        }

    private var mQuizFormData: QuizFormDataUiModel = QuizFormDataUiModel()

    init {
        binding.rvQuizOption.addItemDecoration(QuizOptionItemDecoration(context))
        binding.rvQuizOption.adapter = adapter

        binding.viewGameHeader.type = GameHeaderView.Type.QUIZ
        timePickerBinding.puTimer.infiniteMode = false
        bottomSheetHeaderBinding.ivSheetClose.setImage(IconUnify.ARROW_BACK)
        bottomSheetHeaderBinding.tvSheetTitle.text = context.getString(R.string.play_bro_quiz_set_duration_title)

        binding.tvBroQuizFormNext.setOnClickListener {
            eventBus.emit(Event.SaveQuizData(mQuizFormData))
            eventBus.emit(Event.Next)
        }

        binding.icCloseQuizForm.setOnClickListener {
            eventBus.emit(Event.Back)
        }

        binding.viewGameHeader.setOnTextChangedListener {
            setFormData(mQuizFormData.copy(title = it))
        }

        binding.viewQuizGift.setOnTextChangeListener {
            setFormData(mQuizFormData.copy(gift = it))
        }

        binding.viewQuizGift.setOnRemoveGiftListener {
            setFormData(mQuizFormData.copy(gift = ""))
        }

        bottomSheetHeaderBinding.ivSheetClose.setOnClickListener {
            eventBus.emit(Event.Back)
        }

        timePickerBinding.puTimer.onValueChanged = { _, index ->
            val selectedDuration = quizConfig.availableStartTimeInMs[index]
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

    fun setFormData(quizFormData: QuizFormDataUiModel) {
        if(mQuizFormData != quizFormData) {
            mQuizFormData = quizFormData

            /** Set Quiz Title */
            binding.viewGameHeader.title = quizFormData.title

            /** TODO: set options */
            adapter.setItemsAndAnimateChanges(quizFormData.options)

            /** Set Gift */
            binding.viewQuizGift.gift = quizFormData.gift

            /** Set Quiz Duration */
            val idx = quizConfig.eligibleStartTimeInMs.indexOf(quizFormData.duration)
            if(timePickerBinding.puTimer.activeIndex != idx) {
                timePickerBinding.puTimer.apply {
                    if(idx != -1) goToPosition(idx)
                    else if(quizConfig.eligibleStartTimeInMs.isNotEmpty()) goToPosition(0)
                }
            }

            /** Validate Form */
            binding.tvBroQuizFormNext.isEnabled = quizFormData.isFormValid()
        }
    }

    fun setFormState(quizFormState: QuizFormStateUiModel) {
        when(quizFormState) {
            QuizFormStateUiModel.Preparation -> {
                binding.groupActionBar.visibility = View.VISIBLE
                binding.viewGameHeader.isEditable = true
                binding.viewQuizGift.isEditable = true

                bottomSheetBehaviour.state = BottomSheetBehavior.STATE_HIDDEN
            }
            is QuizFormStateUiModel.SetDuration -> {
                binding.groupActionBar.visibility = View.GONE
                binding.viewGameHeader.isEditable = false
                binding.viewQuizGift.isEditable = false

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
        if (minute > 0) stringBuilder.append(context.getString(R.string.play_interactive_minute, minute))
        if (second > 0) stringBuilder.append(context.getString(R.string.play_interactive_second, second))
        return stringBuilder.toString()
    }

    sealed class Event {
        object Back: Event()
        object Next: Event()
        data class TitleChanged(val title: String): Event() /** TODO: not used anymore */
        data class OptionChanged(val order: Int, val text: String): Event() /** TODO: not used anymore */
        data class SelectQuizOption(val order: Int): Event() /** TODO: not used anymore */
        data class GiftChanged(val gift: String): Event() /** TODO: not used anymore */
        data class SaveQuizData(val quizFormData: QuizFormDataUiModel): Event()
        data class SelectDuration(val duration: Long): Event()
        object Submit: Event()
    }

    companion object {
        private const val SHOW_KEYBOARD_DELAY = 500L
    }
}