package com.tokopedia.sellerfeedback.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.sellerfeedback.R
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By @ilhamsuaib on 05/10/21
 */

class SettingsBottomSheet : BottomSheetUnify() {

    companion object {
        fun createInstance() = SettingsBottomSheet().apply {
            showKnob = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val childView = inflater.inflate(R.layout.bottom_sheet_feedback_settings, container, false)
        setChild(childView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}