package com.tokopedia.vouchercreation.product.voucherlist.view.widget.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.product.voucherlist.view.adapter.CouponStatusFilterAdapter
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherStatus

class CouponStatusFilterBotomSheet(
    private val onStatusSelected: (couponName: String, couponStatus: String) -> Unit
): BottomSheetUnify() {

    init {
        clearContentPadding = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        setTitle(getString(R.string.mvc_coupon_filter_status_title))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val contentView = View.inflate(context, R.layout.bottomsheet_mvc_coupon_status_filter, null)
        val rvCouponStatus: RecyclerView? = contentView?.findViewById(R.id.rvCouponStatus)
        val adapter = CouponStatusFilterAdapter(::onCouponStatusClicked)
        val adapterItems = mutableListOf(
            Pair(getString(R.string.mvc_coupon_status_active), VoucherStatus.NOT_STARTED_AND_ONGOING),
            Pair(getString(R.string.mvc_coupon_status_inactive), VoucherStatus.HISTORY)
        )

        adapter.setItems(adapterItems)
        rvCouponStatus?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvCouponStatus?.adapter = adapter
        setChild(contentView)
    }

    private fun onCouponStatusClicked(couponName: String, @VoucherStatus couponStatus: String) {
        onStatusSelected(couponName, couponStatus)
        dismiss()
    }
}