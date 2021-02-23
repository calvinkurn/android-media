package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.ui.pinned.voucher.adapter.PinnedVoucherAdapter
import com.tokopedia.play.ui.pinned.voucher.viewholder.PinnedVoucherViewHolder
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.play.view.uimodel.VoucherPlaceholderUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent


/**
 * Created by mzennis on 22/02/21.
 */
class PinnedVoucherViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        private val listener: Listener
) : ViewComponent(container, idRes) {

    private val rvPinnedVoucherList: RecyclerView = findViewById(R.id.rv_pinned_voucher_list)

    private val pinnedVoucherAdapter = PinnedVoucherAdapter(object : PinnedVoucherViewHolder.Listener {
        override fun onVoucherClicked(voucher: MerchantVoucherUiModel) {
            listener.onVoucherClicked(this@PinnedVoucherViewComponent, voucher)
        }
    })

    init {
        rvPinnedVoucherList.apply {
            layoutManager = LinearLayoutManager(rvPinnedVoucherList.context, RecyclerView.HORIZONTAL, false)
            adapter = pinnedVoucherAdapter
        }
    }

    fun setVoucher(vouchers: List<PlayVoucherUiModel>) {
        pinnedVoucherAdapter.setItemsAndAnimateChanges(vouchers)

        if (vouchers.isEmpty()) rvPinnedVoucherList.hide()
        else rvPinnedVoucherList.show()
    }

    fun showPlaceholder() {
        setVoucher(
                List(1) { VoucherPlaceholderUiModel }
        )
    }

    interface Listener {

        fun onVoucherClicked(view: PinnedVoucherViewComponent, voucher: MerchantVoucherUiModel)
    }
}