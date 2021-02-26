package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.ui.pinnedvoucher.adapter.PinnedVoucherAdapter
import com.tokopedia.play.ui.pinnedvoucher.viewholder.PinnedVoucherViewHolder
import com.tokopedia.play.ui.productfeatured.itemdecoration.ProductFeaturedItemDecoration
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
        rvPinnedVoucherList.adapter = pinnedVoucherAdapter
        rvPinnedVoucherList.addItemDecoration(ProductFeaturedItemDecoration(rvPinnedVoucherList.context))
    }

    fun setVoucher(vouchers: List<PlayVoucherUiModel>) {
        pinnedVoucherAdapter.setItemsAndAnimateChanges(getHighlightedItems(vouchers))

        if (vouchers.isEmpty()) hide()
        else show()
    }

    fun showPlaceholder() {
        setVoucher(List(TOTAL_PLACEHOLDER) { VoucherPlaceholderUiModel })
    }

    private fun getHighlightedItems(vouchers: List<PlayVoucherUiModel>) : List<PlayVoucherUiModel> {
        return if (vouchers.isNotEmpty()) {
            if (vouchers.first() is MerchantVoucherUiModel) vouchers.filter { (it as MerchantVoucherUiModel).highlighted }
            else vouchers
        } else vouchers
    }

    interface Listener {

        fun onVoucherClicked(view: PinnedVoucherViewComponent, voucher: MerchantVoucherUiModel)
    }

    companion object {
        private const val TOTAL_PLACEHOLDER = 1
    }
}