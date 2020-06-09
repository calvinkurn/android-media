package com.tokopedia.centralizedpromo.view.fragment

import android.content.Context
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.centralizedpromo.view.FirstVoucherDataSource
import com.tokopedia.centralizedpromo.view.adapter.FirstVoucherAdapter
import com.tokopedia.sellerhome.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.centralized_promo_first_voucher_bottomsheet_layout.*

class FirstVoucherBottomSheetFragment : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(context: Context) = FirstVoucherBottomSheetFragment().apply {
            val view = View.inflate(context, R.layout.centralized_promo_first_voucher_bottomsheet_layout, null)
            setChild(view)
            setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isAdded) {
            initView()
        }
    }

    private fun initView() {
        firstVoucherRecyclerView?.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            val itemList = FirstVoucherDataSource.getFirstVoucherInfoItems()
            adapter = FirstVoucherAdapter(itemList)
        }

        firstVoucherButton?.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalSellerapp.CREATE_VOUCHER)
            this.dismiss()
        }
    }
}