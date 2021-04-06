package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import kotlinx.android.synthetic.main.bottomsheet_secondary.view.*

class SomDetailSecondaryActionBottomSheet(
        context: Context,
        private val listener: SomBottomSheetRejectOrderAdapter.ActionListener
) : SomBottomSheet(context) {

    private var adapter: SomBottomSheetRejectOrderAdapter? = null
    private var childView: View? = null

    init {
        childView = inflate(context, R.layout.bottomsheet_secondary, null).apply {
            adapter = SomBottomSheetRejectOrderAdapter(listener, false)
            rv_bottomsheet_secondary?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = this@SomDetailSecondaryActionBottomSheet.adapter
            }
            fl_btn_primary?.gone()
            tf_extra_notes?.gone()
        }
    }

    fun init(view: ViewGroup) {
        super.init(view, requireNotNull(childView), true)
    }

    fun setActions(actions: HashMap<String, String>) {
        adapter?.mapKey = actions
        adapter?.notifyDataSetChanged()
    }
}