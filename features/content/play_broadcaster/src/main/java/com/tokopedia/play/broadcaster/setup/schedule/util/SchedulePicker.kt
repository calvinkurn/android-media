package com.tokopedia.play.broadcaster.setup.schedule.util

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.util.extension.globalVisibleRect
import com.tokopedia.unifyprinciples.Typography
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by kenny.hadisaputra on 24/03/22
 */
data class SchedulePickerState(
    val hasSchedule: Boolean,
    val isLoading: Boolean,
)

class SchedulePicker(fragment: Fragment) {

    private val weakFragment = WeakReference(fragment)

    private val toasterViewId = View.generateViewId()
    private var mState = SchedulePickerState(hasSchedule = false, isLoading =  false)

    fun updateState(fn: (oldState: SchedulePickerState) -> SchedulePickerState) {
        mState = fn(mState)
        getDatePicker()?.invalidate()
    }

    fun dismiss() {
        getDatePicker()?.dismiss()
    }

    fun show(
        minDate: Calendar,
        maxDate: Calendar,
        selectedDate: Calendar,
        listener: Listener,
    ) {
        val context = weakFragment.get()?.context ?: return
        val fragmentManager = weakFragment.get()?.childFragmentManager ?: return

        getDatePicker() ?: DateTimePickerUnify(
            context = context,
            minDate = minDate,
            maxDate = maxDate,
            defaultDate = selectedDate,
            type = DateTimePickerUnify.TYPE_DATETIMEPICKER,
        ).apply {
            setInfoVisible(true)
            setInfo(
                context.getString(R.string.play_bro_schedule_info)
            )
            setTitle(
                context.getString(R.string.play_bro_schedule_label)
            )
            setAction(
                context.getString(R.string.play_bro_schedule_delete)
            ) {
                if (it.isEnabled) listener.onDeleteSchedule(this@SchedulePicker)
            }

            /**
             * Should be set here because of BottomSheetUnify behavior
             * that does not allow changing close click listener after shown
             */
            setCloseClickListener {
                if (!mState.isLoading) dismiss()
            }

            setShowListener {
                datePickerButton.text = context.getString(
                    R.string.play_label_save
                )

                datePickerButton.setOnClickListener {
                    listener.onSaveSchedule(this@SchedulePicker, getDate().time)
                }

                /**
                 * ALERT... ALERT...
                 * This is a hacky way to show toaster inside DateTimePickerUnify
                 * If somehow you read this and there's a change in date time picker,
                 * please don't forget to test and adjust it accordingly
                 *
                 * TODO("Use a different class for date & time picker")
                 */
                if (dateTimePicker.findViewById<View>(toasterViewId) == null) {
                    dateTimePicker.addView(
                        CoordinatorLayout(dateTimePicker.context).apply {
                            id = toasterViewId
                            val lParams = FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT,
                            )
                            lParams.setMargins(
                                0,
                                0,
                                0,
                                datePickerButton.height
                            )
                            lParams.gravity = Gravity.BOTTOM
                            layoutParams = lParams
                        }
                    )
                }

                invalidate()
            }
        }.show(fragmentManager, TAG)
    }

    fun getToasterContainer(): View? {
        return getDatePicker()?.dateTimePicker?.findViewById(toasterViewId)
    }

    private fun DateTimePickerUnify.invalidate() {
        setActionEnabled(!mState.isLoading && mState.hasSchedule)
        setLoading(mState.isLoading)
    }

    private fun DateTimePickerUnify.setActionEnabled(isEnabled: Boolean) {
        val action = bottomSheetAction as? Typography ?: return
        action.isEnabled = isEnabled
    }

    private fun DateTimePickerUnify.setLoading(isLoading: Boolean) {
        datePickerButton.isLoading = isLoading
        datePickerButton.isEnabled = !isLoading

        isCancelable = !isLoading
        overlayClickDismiss = !isLoading
    }

    private fun getDatePicker(): DateTimePickerUnify? {
        val fragmentManager = weakFragment.get()?.childFragmentManager
        return fragmentManager?.findFragmentByTag(
            TAG
        ) as? DateTimePickerUnify ?: return null
    }

    companion object {
        private const val TAG = "schedule_picker"
    }

    interface Listener {
        fun onDeleteSchedule(wrapper: SchedulePicker)
        fun onSaveSchedule(wrapper: SchedulePicker, date: Date)
    }
}