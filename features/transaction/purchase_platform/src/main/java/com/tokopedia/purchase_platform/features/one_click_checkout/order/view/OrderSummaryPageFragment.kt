package com.tokopedia.purchase_platform.features.one_click_checkout.order.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.purchase_platform.R
import kotlinx.android.synthetic.main.fragment_order_summary_page.*

class OrderSummaryPageFragment : BaseDaggerFragment() {

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order_summary_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_order_detail.setOnClickListener {
            OrderPriceSummaryBottomSheet().show(this)
        }

        preference_card.setOnClickListener {
            PreferenceListBottomSheet().show(this)
        }
    }
}