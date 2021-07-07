package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.R
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

    private var availableDuration = listOf<Long>()

    init {
        findViewById<TextView>(com.tokopedia.play_common.R.id.tv_sheet_title)
            .setText(R.string.play_interactive_time_picker_title)

        findViewById<IconUnify>(com.tokopedia.play_common.R.id.iv_sheet_close).apply {
            setImage(IconUnify.ARROW_BACK)
            setOnClickListener {
                listener.onBackButtonClicked(this@InteractiveTimePickerViewComponent)
            }
        }

        findViewById<UnifyButton>(R.id.btn_apply).setOnClickListener {
            val selectedDuration = availableDuration[pickerTime.activeIndex]
            listener.onApplyButtonClicked(this@InteractiveTimePickerViewComponent, selectedDuration)
        }

        pickerTime.infiniteMode = false
        pickerTime.onItemClickListener = { _, index ->
            val selectedDuration = availableDuration[index]
            listener.onValuePickerChanged(this@InteractiveTimePickerViewComponent, selectedDuration)
        }
    }

    fun setActiveDuration(duration: Long) {
        val activePosition = availableDuration.indexOf(duration)
        if (activePosition in 0 until pickerTime.stringData.size) {
            pickerTime.goToPosition(activePosition)
        } else {
            if (pickerTime.stringData.size > 0) pickerTime.goToPosition(0)
        }
    }

    fun setAvailableDuration(durations: List<Long>) {
        availableDuration = durations
        pickerTime.stringData = availableDuration.map { it.toString() }.toMutableList() // TODO: format
    }

    override fun show() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    interface Listener {
        fun onBackButtonClicked(view: InteractiveTimePickerViewComponent)
        fun onValuePickerChanged(view: InteractiveTimePickerViewComponent, selectedDuration: Long)
        fun onApplyButtonClicked(view: InteractiveTimePickerViewComponent, selectedDuration: Long)
    }
}