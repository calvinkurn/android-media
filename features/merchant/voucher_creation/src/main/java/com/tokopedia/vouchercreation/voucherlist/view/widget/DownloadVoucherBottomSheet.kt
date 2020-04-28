package com.tokopedia.vouchercreation.voucherlist.view.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R

/**
 * Created By @ilhamsuaib on 28/04/20
 */

class DownloadVoucherBottomSheet(parent: ViewGroup) : BottomSheetUnify() {

    init {
        val child = LayoutInflater.from(parent.context)
                .inflate(R.layout.bottomsheet_mvc_download_voucher, parent, false)

        setupView(child)
        setTitle(getString(R.string.mvc_select_voucher_size))
        setChild(child)
    }

    private fun setupView(child: View) {

    }

    fun setOnDownloadClickListener(action: () -> Unit) {

    }

    fun show(fm: FragmentManager) {

    }
}