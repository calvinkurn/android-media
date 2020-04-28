package com.tokopedia.vouchercreation.voucherlist.view.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.DownloadVoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.view.adapter.DownloadVoucherAdapter
import kotlinx.android.synthetic.main.bottomsheet_mvc_download_voucher.*

/**
 * Created By @ilhamsuaib on 28/04/20
 */

class DownloadVoucherBottomSheet(parent: ViewGroup) : BottomSheetUnify() {

    private val mAdapter by lazy { DownloadVoucherAdapter() }
    private var onDownloadClick: (List<DownloadVoucherUiModel>) -> Unit = {}

    init {
        val child = LayoutInflater.from(parent.context)
                .inflate(R.layout.bottomsheet_mvc_download_voucher, parent, false)

        setupView(child)
        setTitle(getString(R.string.mvc_select_voucher_size))
        setChild(child)
    }

    private fun setupView(child: View) = with(child) {
        rvMvcVouchers.run {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        btnMvcDownloadVoucher.setOnClickListener {
            onDownloadClick(mAdapter.items.filter { it.isSelected })
        }
    }

    fun setOnDownloadClickListener(action: (List<DownloadVoucherUiModel>) -> Unit) {
        this.onDownloadClick = action
    }

    fun show(fm: FragmentManager) {
        show(fm, DownloadVoucherBottomSheet::class.java.simpleName)
    }
}