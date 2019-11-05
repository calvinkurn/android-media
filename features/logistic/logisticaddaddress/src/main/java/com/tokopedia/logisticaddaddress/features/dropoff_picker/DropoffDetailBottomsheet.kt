package com.tokopedia.logisticaddaddress.features.dropoff_picker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.logisticaddaddress.R


class DropoffDetailBottomsheet : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottomsheet_dropoff_detail, container, false)
    }

    override fun onStart() {
        super.onStart()

        val window = dialog.window
        val windowParams = window!!.attributes
        windowParams.dimAmount = 0.10f;
        windowParams.flags = windowParams.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window.attributes = windowParams
    }

    companion object {
        fun newInstance(): DropoffDetailBottomsheet = DropoffDetailBottomsheet().apply {
            arguments = Bundle()
        }
    }
}