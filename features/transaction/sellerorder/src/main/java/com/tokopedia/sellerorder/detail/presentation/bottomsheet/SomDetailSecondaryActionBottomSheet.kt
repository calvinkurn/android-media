package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import kotlinx.android.synthetic.main.bottomsheet_secondary.view.*

class SomDetailSecondaryActionBottomSheet(
        context: Context,
        private val listener: SomBottomSheetRejectOrderAdapter.ActionListener
) : SomBottomSheet(LAYOUT, true, true, false, context.getString(R.string.som_detail_other_bottomsheet_title), context, true) {

    companion object {
        private val LAYOUT = R.layout.bottomsheet_secondary
    }

    private var secondaryActionAdapter: SomBottomSheetRejectOrderAdapter? = null

    override fun setupChildView() {
        secondaryActionAdapter = SomBottomSheetRejectOrderAdapter(listener, false)
        childViews?.run {
            rv_bottomsheet_secondary?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = secondaryActionAdapter
            }
            fl_btn_primary?.gone()
            tf_extra_notes?.gone()
        }
    }

    fun setActions(actions: HashMap<String, String>) {
        secondaryActionAdapter?.mapKey = actions
        secondaryActionAdapter?.notifyDataSetChanged()
    }
}