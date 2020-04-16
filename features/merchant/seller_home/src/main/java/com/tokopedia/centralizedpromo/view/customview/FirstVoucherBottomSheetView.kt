package com.tokopedia.centralizedpromo.view.customview

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.centralizedpromo.view.FirstVoucherDataSource
import com.tokopedia.centralizedpromo.view.adapter.FirstVoucherAdapter
import com.tokopedia.sellerhome.R
import kotlinx.android.synthetic.main.centralized_promo_first_voucher_bottomsheet_layout.view.*

class FirstVoucherBottomSheetView(context: Context) : ConstraintLayout(context) {

    init {
        View.inflate(context, R.layout.centralized_promo_first_voucher_bottomsheet_layout, this)
        setupLayout()
    }

    private fun setupLayout() {
        setupAdapter()
    }

    private fun setupAdapter() {
        firstVoucherRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            val itemList = FirstVoucherDataSource.getFirstVoucherInfoItems()
            adapter = FirstVoucherAdapter(itemList)
        }
    }

}