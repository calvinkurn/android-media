package com.tokopedia.purchase_platform.features.one_click_checkout.order.view

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.view.PreferenceListAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_preference_list.view.*

class PreferenceListBottomSheet {

    fun show(view: OrderSummaryPageFragment) {
        view.fragmentManager?.let {
            BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                setTitle("Pengiriman dan pembayaran")
                val child = View.inflate(view.context, R.layout.bottomsheet_preference_list, null)
                setupChild(child)
                view.view?.height?.div(2)?.let { height ->
                    customPeekHeight = height
                }
                setChild(child)
                show(it, null)
            }
        }
    }

    private fun setupChild(child: View) {
        val rvPreferenceList = child.rv_preference_list
        rvPreferenceList.layoutManager = LinearLayoutManager(child.context, LinearLayoutManager.VERTICAL, false)
        rvPreferenceList.adapter = PreferenceListAdapter().apply {
            submitList(listOf(Preference(), Preference()))
        }
    }
}