package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.picker.PickerUnify


/**
 * Created by mzennis on 05/07/21.
 */
class EngagementTimePickerViewComponent(
    container: ViewGroup,
    listener: Listener
) : ViewComponent(container, R.id.cl_time_picker_sheet) {

    private val pickerTime: PickerUnify = findViewById(R.id.picker_time)

    private val bottomSheetBehavior = BottomSheetBehavior.from(rootView)

    init {
        findViewById<TextView>(com.tokopedia.play_common.R.id.tv_sheet_title)
            .setText(R.string.play_engage_time_picker_title)

        findViewById<IconUnify>(com.tokopedia.play_common.R.id.iv_sheet_close).apply {
            setImage(IconUnify.ARROW_BACK)
            setOnClickListener {
                listener.onCloseButtonClicked(this@EngagementTimePickerViewComponent)
            }
        }

        findViewById<UnifyButton>(R.id.btn_apply).setOnClickListener {
            val selectedTime = pickerTime.activeValue.filter { it.isDigit() }.toIntOrZero()
            if (selectedTime > 0) listener.onApplyButtonClicked(this@EngagementTimePickerViewComponent, selectedTime)
            else throw IllegalStateException("this shouldn't happen: selected time is zero")
        }

        pickerTime.infiniteMode = false
    }

    fun setItems(availableTimes: List<Int>) {
        pickerTime.stringData = availableTimes.map { getString(R.string.play_engage_time_picker_value, it) }.toMutableList()
    }

    override fun show() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    interface Listener {
        fun onCloseButtonClicked(view: EngagementTimePickerViewComponent)
        fun onApplyButtonClicked(view: EngagementTimePickerViewComponent, selectedTime: Int)
    }
}