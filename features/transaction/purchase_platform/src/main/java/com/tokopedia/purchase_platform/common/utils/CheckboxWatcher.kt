package com.tokopedia.purchase_platform.common.utils

import android.widget.CompoundButton
import com.tokopedia.purchase_platform.common.utils.NoteTextWatcher.NoteTextwatcherListener

/**
 * Created by fwidjaja on 2020-03-03.
 */
class CheckboxWatcher: CompoundButton.OnCheckedChangeListener {

    companion object {
        const val CHECKBOX_WATCHER_DEBOUNCE_TIME = 500
    }

    private var checkboxWatcherListener: CheckboxWatcherListener? = null

    fun CheckboxWatcher(checkboxWatcherListener: CheckboxWatcherListener) {
        this.checkboxWatcherListener = checkboxWatcherListener
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        checkboxWatcherListener?.onCheckboxChanged(isChecked)
    }

    interface CheckboxWatcherListener {
        fun onCheckboxChanged(isChecked: Boolean)
    }
}