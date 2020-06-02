package com.tokopedia.vouchercreation.voucherlist.view.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R

class SuccessCreateBottomSheet(private val parent: ViewGroup): BottomSheetUnify() {

    companion object {
        val TAG: String = SuccessCreateBottomSheet::class.java.simpleName

        @JvmStatic
        fun createInstance(parent: ViewGroup): SuccessCreateBottomSheet {
            return SuccessCreateBottomSheet(parent)
        }
    }

    init {
        val child = LayoutInflater.from(parent.context)
                .inflate(R.layout.bottomsheet_mvc_success_create, parent, false)
        setChild(child)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    fun show(fm: FragmentManager) {
        showNow(fm, this::class.java.simpleName)
    }

}