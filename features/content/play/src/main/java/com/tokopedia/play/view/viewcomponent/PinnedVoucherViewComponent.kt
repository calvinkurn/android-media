package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
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
    private val pinnedVoucherListener = object: RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) sendImpression()
        }
    }

    init {
        rvPinnedVoucherList.adapter = pinnedVoucherAdapter
        rvPinnedVoucherList.addItemDecoration(ProductFeaturedItemDecoration(rvPinnedVoucherList.context))
        rvPinnedVoucherList.addOnScrollListener(pinnedVoucherListener)
    }

    fun setVoucher(vouchers: List<PlayVoucherUiModel>) {
        val highlightedItems = getHighlightedItems(vouchers)
        pinnedVoucherAdapter.setItemsAndAnimateChanges(highlightedItems)

        if (highlightedItems.isEmpty()) hide()
        else {
            show()
            rvPinnedVoucherList.addOneTimeGlobalLayoutListener { sendImpression() }
        }
    }

    fun showIfNotEmpty() {
        if (pinnedVoucherAdapter.itemCount != 0
                && pinnedVoucherAdapter.getItem(0) !is VoucherPlaceholderUiModel) show()
        else hide()
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

    private fun sendImpression() {
        val layoutManager = rvPinnedVoucherList.layoutManager
        if (layoutManager !is LinearLayoutManager) return

        val startPosition = layoutManager.findFirstVisibleItemPosition()
        val endPosition = layoutManager.findLastVisibleItemPosition()
        if (startPosition < 0 || endPosition > pinnedVoucherAdapter.itemCount) return

        val voucherImpressed = pinnedVoucherAdapter.getItems()
                .slice(startPosition..endPosition)
                .filterIsInstance<MerchantVoucherUiModel>()
        listener.onVoucherImpressed(this@PinnedVoucherViewComponent, voucherImpressed)
    }

    interface Listener {

        fun onVoucherImpressed(view: PinnedVoucherViewComponent, vouchers: List<MerchantVoucherUiModel>)
        fun onVoucherClicked(view: PinnedVoucherViewComponent, voucher: MerchantVoucherUiModel)
    }

    companion object {
        private const val TOTAL_PLACEHOLDER = 1
    }
}