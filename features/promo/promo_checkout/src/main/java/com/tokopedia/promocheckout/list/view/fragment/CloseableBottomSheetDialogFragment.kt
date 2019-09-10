package com.tokopedia.promocheckout.list.view.fragment

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.detail.view.fragment.CheckoutCatalogDetailFragment

class CloseableBottomSheetDialogFragment : BottomSheetDialogFragment() {
    companion object{
        fun newInstance() = CloseableBottomSheetDialogFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.promo_coupon_detail_bottomsheet, container, false)
        return view
    }
}
