package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveDurationInfoUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.picker.PickerUnify


/**
 * Created by mzennis on 05/07/21.
 */
class InteractiveTimePickerViewComponent(
    container: ViewGroup,
    listener: Listener
) : ViewComponent(container, R.id.cl_time_picker_sheet) {

    private val pickerTime: PickerUnify = findViewById(R.id.picker_time)

    private val bottomSheetBehavior = BottomSheetBehavior.from(rootView)

    private val availableDuration = mutableListOf<InteractiveDurationInfoUiModel>()

    init {
        findViewById<TextView>(com.tokopedia.play_common.R.id.tv_sheet_title)
            .setText(R.string.play_interactive_time_picker_title)

        findViewById<IconUnify>(com.tokopedia.play_common.R.id.iv_sheet_close).apply {
            setImage(IconUnify.ARROW_BACK)
            setOnClickListener {
                listener.onCloseButtonClicked(this@InteractiveTimePickerViewComponent)
            }
        }

        findViewById<UnifyButton>(R.id.btn_apply).setOnClickListener {
            val selectedDuration = availableDuration.firstOrNull { it.formatted == pickerTime.activeValue }
            if (selectedDuration != null) listener.onApplyButtonClicked(this@InteractiveTimePickerViewComponent, selectedDuration)
        }

        pickerTime.infiniteMode = false
        pickerTime.onValueChanged = { value, _ ->
            val selectedDuration = availableDuration.firstOrNull { it.formatted == value }
            if (selectedDuration != null) listener.onValuePickerChanged(this@InteractiveTimePickerViewComponent, selectedDuration)
        }
    }

    fun setActiveDuration(duration: InteractiveDurationInfoUiModel) {
        val activePosition = pickerTime.stringData.indexOf(duration.formatted)
        if (activePosition >= 0) pickerTime.goToPosition(activePosition)
    }

    fun setItems(durations: List<InteractiveDurationInfoUiModel>) {
        availableDuration.clear()
        availableDuration.addAll(durations)
        pickerTime.stringData = durations.map { it.formatted }.toMutableList()
    }

    override fun show() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    interface Listener {
        fun onCloseButtonClicked(view: InteractiveTimePickerViewComponent)
        fun onValuePickerChanged(view: InteractiveTimePickerViewComponent, selectedDuration: InteractiveDurationInfoUiModel)
        fun onApplyButtonClicked(view: InteractiveTimePickerViewComponent, selectedDuration: InteractiveDurationInfoUiModel)
    }
}