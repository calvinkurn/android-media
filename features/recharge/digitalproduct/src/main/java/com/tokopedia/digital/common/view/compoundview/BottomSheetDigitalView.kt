package com.tokopedia.digital.common.view.compoundview

import android.os.Bundle
import android.view.View
import com.tokopedia.unifycomponents.BottomSheetUnify

class BottomSheetDigitalView : BottomSheetUnify() {
    private lateinit var childView: View
    private lateinit var listener: ActionListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBottomSheet()
    }

    private fun initBottomSheet() {
        showCloseIcon = false
        showKnob = true
        isDragable = true
        isHideable = true
        arguments?.let {
            setTitle(it.getString(TITLE) ?: "")
        }
        setAction(getString(R.string.telco_reset_filter)) {
            if (adapter.totalChecked > 0) resetFilter()
        }

        childView = View.inflate(requireContext(), R.layout.bottom_sheet_telco_filter, null)
        setChild(childView)
    }

    interface ActionListener {
        fun onTelcoFilterSaved(keysFilter: ArrayList<String>, valuesFilter: String)
        fun getFilterSelected(): ArrayList<String>
        fun resetFilter()
    }
}