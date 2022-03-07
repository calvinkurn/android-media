package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import com.tokopedia.sellerorder.databinding.BottomsheetSecondaryActionListBinding

class SomDetailSecondaryActionBottomSheet(
        context: Context,
        private val listener: SomBottomSheetRejectOrderAdapter.ActionListener
) : SomBottomSheet<BottomsheetSecondaryActionListBinding>(LAYOUT, true, true, false, false, false, context.getString(R.string.som_detail_other_bottomsheet_title), context, true) {

    companion object {
        private val LAYOUT = R.layout.bottomsheet_secondary_action_list
    }

    private var secondaryActionAdapter: SomBottomSheetRejectOrderAdapter? = null

    override fun bind(view: View): BottomsheetSecondaryActionListBinding {
        return BottomsheetSecondaryActionListBinding.bind(view)
    }

    override fun setupChildView() {
        secondaryActionAdapter = SomBottomSheetRejectOrderAdapter(listener, false)
        binding?.rvBottomSheetActionList?.run {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = secondaryActionAdapter
        }
    }

    fun setActions(actions: HashMap<String, String>) {
        secondaryActionAdapter?.mapKey = actions
        secondaryActionAdapter?.notifyDataSetChanged()
    }
}