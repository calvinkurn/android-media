package com.tokopedia.play.broadcaster.view.custom.interactive.giveaway

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewGiveawayFormBinding
import com.tokopedia.play.broadcaster.databinding.ViewPlayInteractiveTimePickerBinding
import com.tokopedia.play.broadcaster.util.extension.millisToMinutes
import com.tokopedia.play.broadcaster.util.extension.millisToRemainingSeconds
import com.tokopedia.play.broadcaster.util.extension.showErrorToaster
import com.tokopedia.play_common.util.extension.marginLp
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.updatePadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * Created by kenny.hadisaputra on 20/04/22
 */
class GiveawayFormView : ConstraintLayout {

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
    private val binding = ViewGiveawayFormBinding.inflate(
        LayoutInflater.from(context),
        this,
        true,
    )
    private val timePickerBinding = ViewPlayInteractiveTimePickerBinding.bind(binding.root)

    private val timerPickerBehavior = BottomSheetBehavior.from(timePickerBinding.clInteractiveTimePicker)

    private val offset8 = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private var mListener: Listener? = null

    private var mEligibleDurations = emptyList<Long>()
    private var mRemainingTimeInMillis: Long = Long.MAX_VALUE
    private var mSelectedDurationIdx = -1

    private var mStep = Step.AddTitle
        set(value) {
            field = value
            onStepChanged(value)
        }

    init {
        setupView()
        setupInsets()
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if(visibility == View.VISIBLE) setHeaderFocusDelayed()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setHeaderFocusDelayed()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        job.cancel()
    }

    fun setEligibleDurations(durationsInMs: List<Long>) {
        mEligibleDurations = durationsInMs
        timePickerBinding.puTimer.stringData = durationsInMs.map { formatTime(it) }.toMutableList()

        if (durationsInMs.isEmpty()) return

        timePickerBinding.puTimer.goToPosition(getSelectedDurationIndex())
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setLoading(isLoading: Boolean) {
        timePickerBinding.btnApply.isLoading = isLoading
        timePickerBinding.btnApply.isClickable = !isLoading
        timePickerBinding.ivSheetClose.isClickable = !isLoading

        val touchListener =
            if (isLoading) OnTouchListener { _, _ -> true }
            else null
        timePickerBinding.puTimer.setOnTouchListener(touchListener)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setError(throwable: Throwable) {
        showErrorToaster(
            err = throwable,
            bottomMargin = timePickerBinding.btnApply.height + offset8
        )
    }

    fun back() {
        if (isLoading()) return

        val prevStep = mStep.getPrev()
        if (prevStep != null) mStep = prevStep
        else mListener?.onExit(this)
    }

    fun next() {
        if (isLoading()) return

        val nextStep = mStep.getNext()
        if (nextStep != null) {
            mRemainingTimeInMillis = mListener?.getRemainingTimeInMillis() ?: Long.MAX_VALUE
            mStep = nextStep
        }
        else {
            mSelectedDurationIdx = timePickerBinding.puTimer.activeIndex
            mListener?.onDone(
                this,
                Data(
                    title = binding.viewGiveaway.getHeader().title,
                    durationInMs = mEligibleDurations.getOrNull(mSelectedDurationIdx) ?: DEFAULT_DURATION,
                ),
            )
        }
    }

    private fun getSelectedDurationIndex(): Int {
        return if(mSelectedDurationIdx != -1) mSelectedDurationIdx
        else mEligibleDurations.indexOf(
            TimeUnit.MINUTES.toMillis(DEFAULT_ACTIVE_MINUTE)
        ).coerceAtLeast(0)
    }

    private fun setHeaderFocusDelayed() {
        scope.launch {
            delay(SHOW_KEYBOARD_DELAY)
            if (binding.viewGiveaway.getHeader().isEditable) {
                binding.viewGiveaway.getHeader().setFocus(true)
            }
        }
    }

    private fun trimGiveawayTitle() {
        binding.viewGiveaway.getHeader().apply {
            title = title.trim()
        }
    }

    private fun isLoading() = timePickerBinding.btnApply.isLoading

    private fun onStepChanged(step: Step) {
        when (step) {
            Step.AddTitle -> {
                binding.viewGiveaway.getHeader().isEditable = true
                showDurationPicker(false)
                binding.groupActionBar.show()
            }
            Step.SetDuration -> {
                trimGiveawayTitle()

                setEligibleDurations(mEligibleDurations.filter { it < mRemainingTimeInMillis })
                binding.viewGiveaway.getHeader().isEditable = false
                showDurationPicker(true)
                binding.groupActionBar.hide()
            }
        }
    }

    private fun setupView() {
        timePickerBinding.puTimer.infiniteMode = false
        timePickerBinding.btnApply.setOnClickListener { next() }
        timePickerBinding.tvSheetTitle.text = context.getString(R.string.play_bro_interactive_set_start_duration)

        binding.viewGiveaway.showTimer(false)
        binding.tvBroGiveawayFormNext.setOnClickListener {
            mListener?.onClickContinue()
            next()
        }

        binding.viewGiveaway.getHeader().setOnTextChangedListener {
            setEnabledContinue(shouldEnable = it.isNotBlank())
        }
        binding.viewGiveaway.getHeader().maxLength = MAX_TITLE_LENGTH

        binding.icCloseGiveawayForm.setOnClickListener { back() }
        timePickerBinding.ivSheetClose.setOnClickListener {
            mListener?.onClickBackSetTimer()
            back()
        }

        setEnabledContinue(shouldEnable = false)

        onStepChanged(Step.AddTitle)
    }

    private fun setEnabledContinue(shouldEnable: Boolean) {
        binding.tvBroGiveawayFormNext.isClickable = shouldEnable
        binding.tvBroGiveawayFormNext.alpha =
            if (shouldEnable) CONTINUE_ENABLED_ALPHA
            else CONTINUE_DISABLED_ALPHA
    }

    private fun showDurationPicker(shouldShow: Boolean) {
        timerPickerBehavior.state = if (shouldShow) BottomSheetBehavior.STATE_EXPANDED
        else BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setupInsets() {
        binding.clGiveawayForm.doOnApplyWindowInsets { view, insets, padding, _ ->
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

    private enum class Step(val index: Int) {
        AddTitle(0),
        SetDuration(1);

        fun getNext() = getStepAtIndex(index + 1)

        fun getPrev() = getStepAtIndex(index - 1)

        fun getStepAtIndex(index: Int): Step? {
            return try { values[index] } catch (e: IndexOutOfBoundsException) { null }
        }

        companion object {
            private val values = values()
        }
    }

    data class Data(
        val title: String,
        val durationInMs: Long,
    )

    companion object {
        private const val SHOW_KEYBOARD_DELAY = 500L
        private const val DEFAULT_DURATION = 180000L

        private const val CONTINUE_DISABLED_ALPHA = 0.5f
        private const val CONTINUE_ENABLED_ALPHA = 1f

        private const val MAX_TITLE_LENGTH = 40
        private const val DEFAULT_ACTIVE_MINUTE = 5L
    }

    interface Listener {

        fun onExit(view: GiveawayFormView)
        fun onDone(
            view: GiveawayFormView,
            data: Data,
        )

        fun onClickBackSetTimer()
        fun onClickContinue()
        fun getRemainingTimeInMillis(): Long
    }
}