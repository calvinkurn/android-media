package com.tokopedia.play.broadcaster.setup.schedule.util

import androidx.fragment.app.Fragment
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.play.broadcaster.R
import com.tokopedia.unifyprinciples.Typography
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by kenny.hadisaputra on 24/03/22
 */
class SchedulePicker(fragment: Fragment) {

    private val weakFragment = WeakReference(fragment)

    private var mHasSchedule: Boolean = false
    private var mIsLoading: Boolean = false

    fun setLoading(isLoading: Boolean) {
        mIsLoading = isLoading

        getDatePicker()?.invalidate()
    }

    fun setHasSchedule(hasSchedule: Boolean) {
        mHasSchedule = hasSchedule

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

        val datePicker = getDatePicker() ?: DateTimePickerUnify(
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
                if (!mIsLoading) dismiss()
            }

            setShowListener {
                datePickerButton.text = context.getString(
                    R.string.play_label_save
                )

                datePickerButton.setOnClickListener {
                    listener.onSaveSchedule(this@SchedulePicker, getDate().time)
                }

                invalidate()
            }
        }.show(fragmentManager, TAG)
    }

    private fun DateTimePickerUnify.invalidate() {
        setActionEnabled(!mIsLoading && mHasSchedule)
        setLoading(mIsLoading)
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