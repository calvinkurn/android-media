package com.tokopedia.vouchercreation.create.view.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.adapter.MerchantVoucherTargetAdapter
import com.tokopedia.vouchercreation.create.view.uimodel.VoucherTargetUiModel
import kotlinx.android.synthetic.main.mvc_voucher_target_widget.view.*

class VoucherTargetViewHolder(itemView: View) : AbstractViewHolder<VoucherTargetUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.mvc_voucher_target_widget
    }

    override fun bind(element: VoucherTargetUiModel) {
        itemView.run {
            voucherTargetWidgetRecyclerView?.layoutManager = LinearLayoutManager(context)
            voucherTargetWidgetRecyclerView?.adapter = MerchantVoucherTargetAdapter(::onRequestNotify)
        }
    }

    private fun onRequestNotify() {
        itemView.voucherTargetWidgetRecyclerView?.run {
            post {
                adapter?.notifyDataSetChanged()
            }
        }
    }
}