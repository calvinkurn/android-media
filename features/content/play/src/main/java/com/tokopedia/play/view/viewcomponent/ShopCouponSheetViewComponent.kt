package com.tokopedia.play.view.viewcomponent

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.R
import com.tokopedia.play.ui.promosheet.adapter.MerchantVoucherAdapter
import com.tokopedia.play.ui.promosheet.itemdecoration.MerchantVoucherItemDecoration
import com.tokopedia.play.ui.promosheet.viewholder.MerchantVoucherNewViewHolder
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * @author by astidhiyaa on 25/11/21
 */
class ShopCouponSheetViewComponent(
    container: ViewGroup,
    private val listener: Listener
) : ViewComponent(container, R.id.cl_shop_coupon_sheet) {

    private val bottomSheetBehavior = BottomSheetBehavior.from(rootView)
    private val rvVoucherList: RecyclerView = findViewById(R.id.rv_voucher_list)
    private val tvSheetTitle: TextView = findViewById(com.tokopedia.play_common.R.id.tv_sheet_title)
    private val clVoucherEmpty: ConstraintLayout = findViewById(R.id.cl_product_empty)
    private val clContent: ConstraintLayout = findViewById(R.id.cl_coupon_content)
    private val vBottomOverlay: View = findViewById(R.id.v_bottom_overlay)

    private val voucherAdapter =
        MerchantVoucherAdapter(object : MerchantVoucherNewViewHolder.Listener {
            override fun onCopyItemVoucherClicked(voucher: PlayVoucherUiModel.Merchant) {
                listener.onCopyVoucherCodeClicked(this@ShopCouponSheetViewComponent, voucher)
            }

            override fun onVoucherItemClicked(voucher: PlayVoucherUiModel.Merchant) {
                listener.onVoucherItemClicked(this@ShopCouponSheetViewComponent, voucher)
            }
        })

    private val impressedVoucher
            get() = voucherAdapter.getItems().filterIsInstance<PlayVoucherUiModel.Merchant>()

    private val voucherScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            val layoutManager = recyclerView.layoutManager
            if (newState == RecyclerView.SCROLL_STATE_IDLE && layoutManager is LinearLayoutManager) {
                val index = layoutManagerVoucher.findFirstCompletelyVisibleItemPosition()
                listener.onVouchersImpressed(
                    this@ShopCouponSheetViewComponent,
                    impressedVoucher.getOrNull(index)?.id.orEmpty()
                )
            }
        }
    }

    private val layoutManagerVoucher = object : LinearLayoutManager(rvVoucherList.context, RecyclerView.VERTICAL, false) {
        override fun onLayoutCompleted(state: RecyclerView.State?) {
            super.onLayoutCompleted(state)
            val index = findFirstCompletelyVisibleItemPosition()
            listener.onVouchersImpressed(
                this@ShopCouponSheetViewComponent,
                impressedVoucher.getOrNull(index)?.id.orEmpty()
            )
        }
    }

    init {
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->

            vBottomOverlay.layoutParams = vBottomOverlay.layoutParams.apply {
                height = insets.systemWindowInsetBottom
            }
            clContent.setPadding(
                clContent.paddingLeft,
                clContent.paddingTop,
                clContent.paddingRight,
                insets.systemWindowInsetBottom
            )

            insets
        }

        findViewById<ImageView>(com.tokopedia.play_common.R.id.iv_sheet_close)
            .setOnClickListener {
                listener.onCloseButtonClicked(this)
            }

        rvVoucherList.apply {
            layoutManager = layoutManagerVoucher
            adapter = voucherAdapter
            addItemDecoration(MerchantVoucherItemDecoration(rvVoucherList.context))
            addOnScrollListener(voucherScrollListener)
        }

        tvSheetTitle.text = getString(R.string.play_title_coupon)
    }

    override fun show() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun setVoucherList(voucherList: List<PlayVoucherUiModel>) {
        rvVoucherList.shouldShowWithAction(voucherList.isNotEmpty()) {
            voucherAdapter.setItemsAndAnimateChanges(voucherList)
        }
        clVoucherEmpty.showWithCondition(voucherList.isEmpty())
    }

    fun showWithHeight(height: Int) {
        if (rootView.height != height) {
            val layoutParams = rootView.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            rootView.layoutParams = layoutParams
        }
        show()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        rvVoucherList.removeOnScrollListener(voucherScrollListener)
    }

    interface Listener {
        fun onCloseButtonClicked(view: ShopCouponSheetViewComponent)
        fun onVouchersImpressed(
            view: ShopCouponSheetViewComponent,
            voucherId: String
        )

        fun onCopyVoucherCodeClicked(
            view: ShopCouponSheetViewComponent,
            voucher: PlayVoucherUiModel.Merchant
        )

        fun onVoucherItemClicked(
            view: ShopCouponSheetViewComponent,
            voucher: PlayVoucherUiModel.Merchant
        )
    }
}
