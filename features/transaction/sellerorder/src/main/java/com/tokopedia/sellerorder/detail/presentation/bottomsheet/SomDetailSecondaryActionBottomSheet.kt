package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet

class SomDetailSecondaryActionBottomSheet(
        context: Context,
        private val listener: SomBottomSheetRejectOrderAdapter.ActionListener
) : SomBottomSheet(LAYOUT, true, true, false, context.getString(R.string.som_detail_other_bottomsheet_title), context, true) {

    companion object {
        private val LAYOUT = R.layout.bottomsheet_secondary_action_list
    }

    private var secondaryActionAdapter: SomBottomSheetRejectOrderAdapter? = null

    override fun setupChildView() {
        secondaryActionAdapter = SomBottomSheetRejectOrderAdapter(listener, false)
        childViews?.findViewById<RecyclerView>(R.id.rvBottomSheetActionList)?.run {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = secondaryActionAdapter
        }
    }

    fun setActions(actions: HashMap<String, String>) {
        secondaryActionAdapter?.mapKey = actions
        secondaryActionAdapter?.notifyDataSetChanged()
    }
}