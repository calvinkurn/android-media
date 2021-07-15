package com.tokopedia.play.broadcaster.view.partial

import android.content.Context
import android.text.InputFilter
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveConfigUiModel
import com.tokopedia.play.broadcaster.util.extension.millisToMinutes
import com.tokopedia.play.broadcaster.util.extension.millisToRemainingSeconds
import com.tokopedia.play.broadcaster.view.custom.PlayBroadcastEditText
import com.tokopedia.play_common.util.extension.marginLp
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.picker.PickerUnify
import com.tokopedia.unifyprinciples.Typography
import java.util.concurrent.TimeUnit
import com.tokopedia.play_common.R as commonR


/**
 * Created by mzennis on 06/07/21.
 */
class BroadcastInteractiveSetupViewComponent(
    container: ViewGroup,
    private val listener: Listener
) : ViewComponent(container, R.id.cl_interactive_setup) {

    private val containerSetup = findViewById<ConstraintLayout>(R.id.cl_container_setup)
    private val titleLbl = findViewById<Typography>(R.id.tv_title)
    private val descLbl = findViewById<Typography>(R.id.tv_desc)
    private val editTextTitle = findViewById<PlayBroadcastEditText>(R.id.et_interactive_title)
    private val activeTimerLbl = findViewById<Typography>(R.id.tv_timer)
    private val containerTimePicker = findViewById<ConstraintLayout>(R.id.cl_interactive_time_picker)
    private val timePicker = findViewById<PickerUnify>(R.id.pu_timer)
    private val btnApply = findViewById<UnifyButton>(R.id.btn_apply)

    private val bottomSheetBehavior = BottomSheetBehavior.from(containerTimePicker)

    private val defaultSelectedDuration = TimeUnit.MINUTES.toMillis(DEFAULT_DURATION_IN_MINUTE)

    private lateinit var mConfig: InteractiveConfigUiModel

    private val title: String
        get() = editTextTitle.text.toString()

    init {
        initView()
        showPickerSheet(false)

        /**
         * Default value
         */
        setActiveTitle("${getString(R.string.play_interactive_title_default)} ")
        setFocusOnEditTextTitle()

        containerSetup.doOnApplyWindowInsets { v, insets, padding, _ ->
            v.updatePadding(top = padding.top + insets.systemWindowInsetTop)
        }

        btnApply.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLp = v.marginLp
            marginLp.bottomMargin = margin.bottom + insets.systemWindowInsetBottom
            v.layoutParams = marginLp
        }
    }

    private fun initView() {
        rootView.setOnClickListener {
            showKeyboard(false)
            hide()
        }
        findViewById<UnifyButton>(R.id.btn_cancel).setOnClickListener {
            showKeyboard(false)
            hide()
        }
        editTextTitle.apply {
            setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT
                    && title.length in MIN_LENGTH_CHAR..MAX_LENGTH_CHAR) {
                    showSetupDurationLayout()
                    return@OnEditorActionListener true
                }
                false
            })
            filters = arrayOf(InputFilter.LengthFilter(MAX_LENGTH_CHAR))
            setSelection(title.length)
        }
        activeTimerLbl.setOnClickListener {
            showSetupDurationLayout()
        }
        initTimePicker()
    }

    private fun initTimePicker() {
        findViewById<TextView>(commonR.id.tv_sheet_title)
            .setText(R.string.play_interactive_time_picker_title)

        findViewById<IconUnify>(commonR.id.iv_sheet_close).apply {
            setImage(IconUnify.ARROW_BACK)
            setOnClickListener {
                showSetupTitleLayout()
            }
        }

        btnApply.setOnClickListener {
            val selectedDuration = mConfig.availableStartTimeInMs[timePicker.activeIndex]
            listener.onApplyButtonClicked(this@BroadcastInteractiveSetupViewComponent, title, selectedDuration)
        }

        timePicker.infiniteMode = false
        timePicker.onValueChanged = { _, index ->
            val selectedDuration = mConfig.availableStartTimeInMs[index]
            setLabelSelectedDuration(selectedDuration)
        }
    }

    fun setLoading(isLoading: Boolean) {
        btnApply.isLoading = isLoading
    }

    fun setConfig(config: InteractiveConfigUiModel) {
        mConfig = config
        setupLabelGuideline(false)
    }

    fun setAvailableStartTimes(durationInMs: List<Long>) {
        if (!durationInMs.isNullOrEmpty()) mConfig = mConfig.copy(availableStartTimeInMs = durationInMs)
        timePicker.stringData = mConfig.availableStartTimeInMs.map { formatTime(it) }.toMutableList()

        if (mConfig.availableStartTimeInMs.contains(defaultSelectedDuration)) {
            setSelectedDuration(defaultSelectedDuration)
        } else setSelectedDuration(mConfig.availableStartTimeInMs.first())
    }

    fun interceptBackPressed(): Boolean {
        return when {
            isPickerSheetVisible() -> {
                showSetupTitleLayout()
                true
            }
            isShown() -> {
                hide()
                true
            }
            else -> false
        }
    }

    override fun show() {
        super.show()
        if (!isPickerSheetVisible()) setFocusOnEditTextTitle()
    }

    private fun setActiveTitle(title: String) {
        editTextTitle.setText(title)
    }

    private fun setupLabelGuideline(showPickerSheet: Boolean) {
        if (showPickerSheet) {
            titleLbl.text = mConfig.timeGuidelineHeader
            descLbl.text = mConfig.timeGuidelineDetail
        } else {
            titleLbl.text = mConfig.nameGuidelineHeader
            descLbl.text = mConfig.nameGuidelineDetail
        }
    }

    private fun setSelectedDuration(durationInMs: Long) {
        val activePosition = mConfig.availableStartTimeInMs.indexOf(durationInMs)
        if (activePosition in 0 until timePicker.stringData.size) {
            timePicker.goToPosition(activePosition)
        } else {
            if (timePicker.stringData.size > 0) timePicker.goToPosition(0)
        }
        if (!isPickerSheetVisible()) setLabelSelectedDuration(durationInMs)
    }

    private fun setLabelSelectedDuration(durationInMs: Long) {
        activeTimerLbl.text = getString(
            R.string.play_interactive_selected_duration_format,
            durationInMs.millisToMinutes(),
            durationInMs.millisToRemainingSeconds()
        )
    }

    private fun formatTime(millis: Long): String {
        val minute = millis.millisToMinutes()
        val second = millis.millisToRemainingSeconds()

        val stringBuilder = StringBuilder()
        if (minute > 0) stringBuilder.append(getString(R.string.play_interactive_minute, minute))
        if (second > 0) stringBuilder.append(getString(R.string.play_interactive_second, second))
        return stringBuilder.toString()
    }

    private fun setFocusOnEditTextTitle() {
        showKeyboard(true)
        editTextTitle.apply {
            isFocusable = true
            isFocusableInTouchMode = true
        }
        editTextTitle.requestFocus()
        editTextTitle.setSelection(title.length)
    }

    private fun showKeyboard(shouldShow: Boolean) {
        val imm = editTextTitle.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (shouldShow) imm.showSoftInput(editTextTitle, InputMethodManager.SHOW_IMPLICIT)
        else imm.hideSoftInputFromWindow(editTextTitle.windowToken, 0)
    }

    private fun isPickerSheetVisible(): Boolean {
        return bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED
    }

    private fun showSetupTitleLayout() {
        setFocusOnEditTextTitle()
        showPickerSheet(false)
        setupLabelGuideline(false)
    }

    private fun showSetupDurationLayout() {
        showKeyboard(false)
        showPickerSheet(true)
        setupLabelGuideline(true)
    }

    private fun showPickerSheet(shouldShow: Boolean) {
        bottomSheetBehavior.state = if (shouldShow) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_HIDDEN
    }

    interface Listener {
        fun onApplyButtonClicked(view: BroadcastInteractiveSetupViewComponent, title: String, durationInMs: Long)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        ViewCompat.setOnApplyWindowInsetsListener(rootView, null)
        ViewCompat.setOnApplyWindowInsetsListener(btnApply, null)
    }

    companion object {
        private const val MIN_LENGTH_CHAR = 3
        private const val MAX_LENGTH_CHAR = 25

        private const val DEFAULT_DURATION_IN_MINUTE: Long = 3
    }
}