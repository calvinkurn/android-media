package com.tokopedia.home_component.widget.card.timer

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.home_component.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifycomponents.timeFormatter
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.UnifyMotion
import java.util.*
import kotlin.math.roundToInt

/**
 * Originally from: [com.tokopedia.unifycomponents.timer.TimerUnifySingle]
 */
class CountdownTimer : ConstraintLayout {
    private var clockIcon: IconUnify? = null
    private var isFirstLoad = true
    private var isPaused: Boolean = false
    private var timerTextField: TextView? = null
    private var clockWrapper: LinearLayout? = null
    private var dayCount: Long = 0
    private var clockText: TextView? = null

    private var hourTextView: TimerTextView? = null
    private var minuteTextView: TimerTextView? = null
    private var secondTextView: TimerTextView? = null

    private var hourTextViewText: Typography? = null
    private var minuteTextViewText: Typography? = null
    private var secondTextViewText: Typography? = null

    private var colonFirstTextView: Typography? = null
    private var colonSecondTextView: Typography? = null

    private var dayTextView: Typography? = null

    private val handlerTimer = Handler(Looper.getMainLooper())

    /**
     * Flag for disabled public attribute setter on initial state
     * prevent updateUI run multiple time on initial state
     */
    private var isAttributeSetterDisabled = true

    /**
     * manipulated timer var over the time
     */
    private var tempRemainingMillis: Long = 0
    private var remainingMilliseconds: Long = 0
        set(value) {
            field = value

            if (value > 0) {
                val interval: Long = 1000
                tempRemainingMillis = value
                handlerTimer.removeCallbacksAndMessages(null)
                renderFromMillis(tempRemainingMillis)

                handlerTimer.postDelayed(object : Runnable {
                    override fun run() {
                        tempRemainingMillis -= interval
                        renderFromMillis(tempRemainingMillis)
                        onTick?.invoke(tempRemainingMillis)

                        if (tempRemainingMillis <= 0) {
                            /**
                             * execute onFinish when text animation is finish.
                             * bring more precision on user side according to visual effect.
                             * T4 is exact time that text animation duration used, please refer to TimerTextView.kt
                             * T4 = 400ms
                             */
                            Handler().postDelayed({
                                onFinish?.invoke()
                                idle()
                            }, UnifyMotion.T4)
                            handlerTimer.removeCallbacksAndMessages(null)
                        } else {
                            handlerTimer.postDelayed(this, interval)
                        }
                    }
                }, interval)
            }
        }

    var onPause: (() -> Unit)? = null
    var onResume: (() -> Unit)? = null
    var onFinish: (() -> Unit)? = null
    var onTick: ((millisUntilFinished: Long) -> Unit)? = null

    @Deprecated("not used anymore due to ContDownTimer last tick delay problem & onTick remain millis missing few millis")
    var timer: CountDownTimer? = null

    var isShowClockIcon = true
        set(value) {
            if (field == value) return
            field = value

            var leftPaddingClockWrapper = 0
            if (value) {
                clockIcon?.visibility = View.VISIBLE
                leftPaddingClockWrapper = 4.toPx()
            } else {
                clockIcon?.visibility = View.GONE
                leftPaddingClockWrapper = 6.toPx()
            }

            clockWrapper?.let {
                it.setPadding(
                    leftPaddingClockWrapper,
                    it.paddingTop,
                    it.paddingRight,
                    it.paddingBottom
                )
            }
        }

    var timerText: CharSequence? = null
        set(value) {
            field = value
            updateUI()
        }

    var timerVariant = VARIANT_MAIN
        set(value) {
            field = value
            updateUI()
        }

    var timerTextWidth = TEXT_WRAP
        set(value) {
            field = value
            updateUI()
        }

    var targetDate: Calendar? = null
        set(value) {
            field = value
            field?.add(Calendar.MINUTE, timeOffsetMinute)

            targetDateMidnight.time = value?.time
            targetDateMidnight.set(Calendar.HOUR_OF_DAY, 0)
            targetDateMidnight.set(Calendar.MINUTE, 0)
            targetDateMidnight.set(Calendar.SECOND, 0)
            targetDateMidnight.set(Calendar.MILLISECOND, 0)

            calculateTime()
            updateUI()
        }

    /**
     * a variable to help counts day between target and current date ignoring time
     */
    private var targetDateMidnight = Calendar.getInstance()

    /**
     * timeOffset on minute format
     */
    private var timeOffsetMinute = 0

    /**
     * Set timeOffset before you set targetDate & re-set targetDate if timeOffset is re-assigned.
     *
     * offset value that represent additional hours for targetDate on float format.
     *
     * 1f = 1 hour
     *
     * 0.5f = 30 minute
     *
     * 0.21f = 12,6 minute (will be round to 13 minute)
     */
    var timeOffset = 0f
        set(value) {
            field = value

            timeOffsetMinute = (field * 60).roundToInt()
        }

    constructor(context: Context) : super(context) {
        initWittAttrs(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initWittAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initWittAttrs(context, attrs)
    }

    private val clockWrapperLayoutChangeListener = object : OnLayoutChangeListener {
        override fun onLayoutChange(
            view: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            val isHeightSame = (top - bottom) == (oldTop - oldBottom)
            if (isHeightSame) return

            val bg = view.background
            if (bg is GradientDrawable) {
                bg.cornerRadius = getColorWrapperBackgroundCornerRadius()
            }
        }
    }

    init {
        /**
         * Prevent timer not showing issues on Oppo device running on Android 7.1.1
         * - Use simple typography without animation for problematic Oppo device
         */
        View.inflate(context, R.layout.widget_small_product_card_timer, this)
        hourTextView = findViewById(R.id.timer_unify_hour_text)
        minuteTextView = findViewById(R.id.timer_unify_minute_text)
        secondTextView = findViewById(R.id.timer_unify_second_text)

        timerTextField = findViewById(R.id.timer_unify_text)
        clockWrapper = findViewById(R.id.timer_unify_clock_wrapper)
        clockText = findViewById(R.id.timer_unify_clock_text)
        clockIcon = findViewById(R.id.timer_unify_clock_icon)

        colonFirstTextView = findViewById(R.id.timer_unify_colon_first_text)
        colonSecondTextView = findViewById(R.id.timer_unify_colon_second_text)

        dayTextView = findViewById(R.id.timer_unify_day_text)
    }

    private fun renderFromMillis(millisUntilFinished: Long) {
        val seconds = (millisUntilFinished / 1000) % 60
        val minutes = (millisUntilFinished / (1000 * 60) % 60)
        val hours = (millisUntilFinished / (1000 * 60 * 60))
        val days = (millisUntilFinished / (1000 * 60 * 60 * 24))

        dayCount = days

        val secondText = timeFormatter(seconds.toInt())
        val minuteText = timeFormatter(minutes.toInt())
        val hourText = hours.toString()

        if (days < 1) {
            // static days text
            dayTextView?.visibility = View.GONE

            // animation-related number digits
            clockText?.text = ""
            hourTextView?.setText(hourText)
            minuteTextView?.setText(minuteText)
            secondTextView?.setText(secondText)

            hourTextViewText?.setText(hourText)
            minuteTextViewText?.setText(minuteText)
            secondTextViewText?.setText(secondText)

            hourTextView?.visibility = View.VISIBLE
            minuteTextView?.visibility = View.VISIBLE
            secondTextView?.visibility = View.VISIBLE

            hourTextViewText?.visibility = View.VISIBLE
            minuteTextViewText?.visibility = View.VISIBLE
            secondTextViewText?.visibility = View.VISIBLE

            colonFirstTextView?.visibility = View.VISIBLE
            colonSecondTextView?.visibility = View.VISIBLE
        } else {
            // static days text
            val timerTitle = context.getString(R.string.count_down_timer_day_title, days)
            dayTextView?.setText(timerTitle)
            dayTextView?.visibility = View.VISIBLE

            // animation-related number digits
            clockText?.text = ""
            secondTextView?.visibility = View.GONE
            secondTextViewText?.visibility = View.GONE
            hourTextView?.visibility = View.GONE
            minuteTextView?.visibility = View.GONE
            hourTextViewText?.visibility = View.GONE
            minuteTextViewText?.visibility = View.GONE
            colonFirstTextView?.visibility = View.GONE
            colonSecondTextView?.visibility = View.GONE
        }
    }

    private fun updateUI() {
        if (isAttributeSetterDisabled) return
        setBackgroundResource(R.drawable.home_component_countdown_timer_bg)
        setTimerWidth()
        configTimerText()

        /**
         * post for get clock wrapper height for radius
         */
        post {
            setTimerWrapBackgroundColor()
            setTimerTextColor()

            invalidate()
            requestLayout()
        }
    }

    private fun initWittAttrs(context: Context, attrs: AttributeSet? = null) {
        /**
         * adjustment margin bottom, OpenSauceOne have different height & need adjustment
         */
        if(Typography.isFontTypeOpenSauceOne){
            val additionalBottomSize = 1.85f.dpToPx().toInt()
            (colonFirstTextView?.layoutParams as LinearLayout.LayoutParams).setMargins(0,0,0, additionalBottomSize)
            (colonSecondTextView?.layoutParams as LinearLayout.LayoutParams).setMargins(0,0,0, additionalBottomSize)
        }

        hourTextView?.setType(Typography.BODY_3)
        minuteTextView?.setType(Typography.BODY_3)
        secondTextView?.setType(Typography.BODY_3)

        hourTextViewText?.setType(Typography.BODY_3)
        minuteTextViewText?.setType(Typography.BODY_3)
        secondTextViewText?.setType(Typography.BODY_3)

        isAttributeSetterDisabled = false
        updateUI()
    }

    private fun setTimerWrapBackgroundColor() {
        var colorRef = if (timerTextWidth == TEXT_WRAP) {
            when (timerVariant) {
                VARIANT_ALTERNATE -> {
                    R.color.dms_countdown_timer_alternate_state_background
                }
                VARIANT_INFORMATIVE -> {
                    R.color.dms_countdown_timer_informative_state_background
                }
                VARIANT_GENERAL -> {
                    R.color.dms_countdown_timer_general_state_background
                }
                VARIANT_INFORMATIVE_ALTERNATE -> {
                    R.color.dms_countdown_timer_informative_alternate_state_background
                }
                else -> {
                    /**
                     * state 1 = below 5 days | state 2 = 5 days and above
                     */
                    when {
                        dayCount > 4 && (timerText == "" || timerText == null) -> {
                            R.color.dms_countdown_timer_main_state_2_background
                        }
                        else -> {
                            R.color.dms_countdown_timer_main_state_1_background
                        }
                    }
                }
            }
        } else null

        colorRef?.let {
            val clockWrapperBackground = getColorClockWrapperBackground(
                ContextCompat.getColor(context, it)
            )

            clockWrapper?.background = clockWrapperBackground
        }
    }

    private fun setTimerTextColor() {
        when (timerVariant) {
            VARIANT_ALTERNATE -> {
                when {
                    dayCount > 4 && (timerText == "" || timerText == null) -> {
                        clockIcon?.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.dms_countdown_timer_alternate_state_2_text_color
                            )
                        )
                        configTimerTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.dms_countdown_timer_alternate_state_2_text_color
                            )
                        )
                    }
                    else -> {
                        clockIcon?.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.dms_countdown_timer_alternate_state_1_text_color
                            )
                        )
                        configTimerTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.dms_countdown_timer_alternate_state_1_text_color
                            )
                        )
                    }
                }
                if (timerTextWidth == TEXT_WRAP) {
                    timerTextField?.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.dms_countdown_timer_alternate_text_wrap_color
                        )
                    )
                } else {
                    timerTextField?.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.dms_countdown_timer_alternate_text_full_color
                        )
                    )
                }
            }
            VARIANT_INFORMATIVE -> {
                clockIcon?.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.dms_countdown_timer_informative_state_text_color
                    )
                )
                configTimerTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.dms_countdown_timer_informative_state_text_color
                    )
                )
                timerTextField?.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.dms_countdown_timer_informative_text_wrap_full_color
                    )
                )
            }
            VARIANT_GENERAL -> {
                configTimerTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.dms_countdown_timer_general_state_text_color
                    )
                )

                if (timerTextWidth == TEXT_WRAP) {
                    timerTextField?.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.dms_countdown_timer_general_text_wrap_color
                        )
                    )
                } else {
                    timerTextField?.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.dms_countdown_timer_general_text_full_color
                        )
                    )
                }
            }
            VARIANT_INFORMATIVE_ALTERNATE -> {
                clockIcon?.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.dms_countdown_timer_main_state_text_color
                    )
                )
                timerTextField?.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.dms_countdown_timer_main_state_text_color
                    )
                )
                configTimerTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.dms_countdown_timer_main_state_text_color
                    )
                )
            }
            else -> {
                configTimerTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.dms_countdown_timer_main_state_text_color
                    )
                )
                if (timerTextWidth == TEXT_WRAP) {
                    timerTextField?.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.dms_countdown_timer_main_text_wrap_color
                        )
                    )
                } else {
                    timerTextField?.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.dms_countdown_timer_main_text_full_color
                        )
                    )
                }
            }
        }
    }

    private fun configTimerText() {
        if ((timerText != "" && timerText != null)) {
            val maxTextLength = if (timerTextWidth == TEXT_FULL) 28 else 25
            if (timerText?.length!! > maxTextLength) {
                val ellipsizedText = timerText?.substring(0, maxTextLength) + "..."
                timerTextField?.text = ellipsizedText
            } else {
                timerTextField?.text = timerText
            }
            timerTextField?.visibility = View.VISIBLE
        } else {
            timerTextField?.visibility = View.GONE
        }
    }

    private fun setTimerWidth() {
        if (timerTextWidth == TEXT_FULL || timerTextWidth == TEXT_FULL_RADIUS) {
            timerTextField?.layoutParams?.width = 0
        } else {
            timerTextField?.layoutParams?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        when (timerTextWidth) {
            TEXT_FULL -> {
                setPadding(16.toPx(), 4.toPx(), 8.toPx(), 4.toPx())
            }
            TEXT_FULL_RADIUS -> {
                setPadding(12.toPx(), 4.toPx(), 4.toPx(), 4.toPx())
            }
            else -> {
                setPadding(0, 0, 0, 0)
            }
        }
    }

    private fun calculateTime() {
        targetDate?.run {
            var today = Calendar.getInstance()
            remainingMilliseconds = this.time.time - today.time.time
        }
    }

    private fun configTimerTextColor(color: Int) {
        clockIcon?.setColorFilter(color)
        clockText?.setTextColor(color)
        hourTextView?.setTextColor(color)
        minuteTextView?.setTextColor(color)
        secondTextView?.setTextColor(color)
        hourTextViewText?.setTextColor(color)
        minuteTextViewText?.setTextColor(color)
        secondTextViewText?.setTextColor(color)
        colonFirstTextView?.setTextColor(color)
        colonSecondTextView?.setTextColor(color)
    }

    fun idle() {
        if (timerTextWidth == TEXT_WRAP && timerVariant != VARIANT_INFORMATIVE_ALTERNATE) {
            val clockWrapperBackground = getColorClockWrapperBackground(
                ContextCompat.getColor(context, R.color.dms_countdown_timer_idle_background)
            )

            clockWrapper?.background = clockWrapperBackground

            timerTextField?.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.dms_countdown_timer_idle_text_color
                )
            )
            configTimerTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.dms_countdown_timer_idle_text_color
                )
            )
            clockIcon?.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.dms_countdown_timer_idle_clock_color
                )
            )
        }
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val newDrawableState = super.onCreateDrawableState(extraSpace + 1)

        when {
            /**
             * alternate drawable state
             */
            timerVariant == VARIANT_ALTERNATE && timerTextWidth == TEXT_FULL_RADIUS -> {
                View.mergeDrawableStates(
                    newDrawableState,
                    IntArray(1)
                )
            }
            timerVariant == VARIANT_ALTERNATE && timerTextWidth == TEXT_FULL -> {
                View.mergeDrawableStates(
                    newDrawableState,
                    IntArray(1)
                )
            }
            /**
             * informative drawable state
             */
            timerVariant == VARIANT_INFORMATIVE && timerTextWidth == TEXT_FULL_RADIUS -> {
                View.mergeDrawableStates(
                    newDrawableState,
                    IntArray(1)
                )
            }
            timerVariant == VARIANT_INFORMATIVE && timerTextWidth == TEXT_FULL -> {
                View.mergeDrawableStates(
                    newDrawableState,
                    IntArray(1)
                )
            }
            /**
             * alternate drawable state
             */
            timerVariant == VARIANT_INFORMATIVE_ALTERNATE && timerTextWidth == TEXT_FULL_RADIUS -> {
                View.mergeDrawableStates(
                    newDrawableState,
                    IntArray(1)
                )
            }
            timerVariant == VARIANT_INFORMATIVE_ALTERNATE && timerTextWidth == TEXT_FULL -> {
                View.mergeDrawableStates(
                    newDrawableState,
                    IntArray(1)
                )
            }
            /**
             * main drawable state
             */
            timerVariant == VARIANT_MAIN && timerTextWidth == TEXT_FULL -> {
                View.mergeDrawableStates(
                    newDrawableState,
                    IntArray(1)
                )
            }
            timerVariant == VARIANT_MAIN && timerTextWidth == TEXT_FULL_RADIUS -> {
                View.mergeDrawableStates(
                    newDrawableState,
                    IntArray(1)
                )
            }
            /**
             * general drawable state
             */
            timerVariant == VARIANT_GENERAL && timerTextWidth == TEXT_FULL -> {
                View.mergeDrawableStates(
                    newDrawableState,
                    IntArray(1)
                )
            }
            timerVariant == VARIANT_GENERAL && timerTextWidth == TEXT_FULL_RADIUS -> {
                View.mergeDrawableStates(
                    newDrawableState,
                    IntArray(1)
                )
            }
        }
        return newDrawableState
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        handlerTimer.removeCallbacksAndMessages(null)
        clockWrapper?.removeOnLayoutChangeListener(clockWrapperLayoutChangeListener)
    }

    override fun onAttachedToWindow() {
        /**
         * prevent calculate time run twice on initial state and produce fraction result.
         * twice calculation will bring difference on millis scale, the diff is small such 10-20 millis.
         * but the difference bring fraction to the calculation and break the visual
         */
        if (isFirstLoad) {
            isFirstLoad = false
        } else {
            calculateTime()
        }
        updateUI()

        super.onAttachedToWindow()

        clockWrapper?.removeOnLayoutChangeListener(clockWrapperLayoutChangeListener)
        clockWrapper?.addOnLayoutChangeListener(clockWrapperLayoutChangeListener)
    }

    fun pause() {
        isPaused = true
        onPause?.invoke()
        handlerTimer.removeCallbacksAndMessages(null)
    }

    fun resume() {
        if (isPaused) {
            val newTarget = Calendar.getInstance()
            newTarget.add(Calendar.MILLISECOND, tempRemainingMillis.toInt())

            targetDate = newTarget
            onResume?.invoke()
            isPaused = false
        }
    }

    fun addTrigger(date: Calendar, action: () -> Unit) {
        val today = Calendar.getInstance()
        if (date.timeInMillis - today.timeInMillis > -1) {
            val triggerInMillis = date.timeInMillis - today.timeInMillis
            Handler().postDelayed({
                action.invoke()
            }, triggerInMillis)
        }
    }

    private fun getColorClockWrapperBackground(@ColorInt color: Int): GradientDrawable {
        /**
         * radius = 1/2 height
         * else 10dp following figma
         */

        val clockWrapperBackground = GradientDrawable()
        clockWrapperBackground.setColor(color)
        clockWrapperBackground.cornerRadius = getColorWrapperBackgroundCornerRadius()

        return clockWrapperBackground
    }

    private fun getColorWrapperBackgroundCornerRadius(): Float {
        return (clockWrapper?.height ?: 20.toPx()) / 2f
    }

    companion object {
        const val VARIANT_MAIN = 0
        const val VARIANT_INFORMATIVE = 1
        const val VARIANT_ALTERNATE = 2
        const val VARIANT_GENERAL = 3
        const val VARIANT_INFORMATIVE_ALTERNATE = 4

        const val TEXT_WRAP = 0
        const val TEXT_FULL_RADIUS = 1
        const val TEXT_FULL = 2
    }
}
