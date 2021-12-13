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
import com.tokopedia.play.ui.productsheet.adapter.MerchantVoucherAdapter
import com.tokopedia.play.ui.productsheet.itemdecoration.MerchantVoucherItemDecoration
import com.tokopedia.play.ui.productsheet.viewholder.MerchantVoucherNewViewHolder
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
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

    private var isShopCouponSheetsInitialized = false

    private val voucherAdapter =
        MerchantVoucherAdapter(object : MerchantVoucherNewViewHolder.Listener {
            override fun onCopyItemVoucherClicked(voucher: MerchantVoucherUiModel) {
                listener.onCopyVoucherCodeClicked(this@ShopCouponSheetViewComponent, voucher)
            }
        })

    private val layoutManagerVoucherList =
        object : LinearLayoutManager(rvVoucherList.context, RecyclerView.VERTICAL, false) {
            override fun onLayoutCompleted(state: RecyclerView.State?) {
                super.onLayoutCompleted(state)
                listener.onVouchersImpressed(
                    this@ShopCouponSheetViewComponent,
                    getVisibleVouchers()
                )
            }
        }

    private val voucherScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            val layoutManager = recyclerView.layoutManager
            if (newState == RecyclerView.SCROLL_STATE_SETTLING &&
                layoutManager is LinearLayoutManager
            ) {
                listener.onVoucherScrolled(
                    this@ShopCouponSheetViewComponent,
                    layoutManager.findLastVisibleItemPosition()
                )
            }
        }
    }

    init {
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->

            vBottomOverlay.layoutParams = vBottomOverlay.layoutParams.apply {
                height = insets.systemWindowInsetBottom
            }
            clContent.setPadding(clContent.paddingLeft, clContent.paddingTop, clContent.paddingRight, insets.systemWindowInsetBottom)

            insets
        }

        findViewById<ImageView>(com.tokopedia.play_common.R.id.iv_sheet_close)
            .setOnClickListener {
                listener.onCloseButtonClicked(this)
            }

        rvVoucherList.apply {
            layoutManager = layoutManagerVoucherList
            adapter = voucherAdapter
            addItemDecoration(MerchantVoucherItemDecoration(rvVoucherList.context))
        }

        rvVoucherList.addOnScrollListener(voucherScrollListener)

        tvSheetTitle.text = getString(R.string.play_title_coupon)
    }

    override fun show() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun setVoucherList(voucherList: List<MerchantVoucherUiModel>) {
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
        sendImpression()
    }

    fun getVisibleVouchers(): List<MerchantVoucherUiModel> {
        val vouchers = voucherAdapter.getItems()
        if (vouchers.isNotEmpty()) {
            val startPosition = layoutManagerVoucherList.findFirstCompletelyVisibleItemPosition()
            val endPosition = layoutManagerVoucherList.findLastCompletelyVisibleItemPosition()
            if (startPosition > -1 && endPosition < vouchers.size) {
                return vouchers
                    .slice(startPosition..endPosition)
                    .filterIsInstance<MerchantVoucherUiModel>()
            }
        }
        return emptyList()
    }

    private fun sendImpression() {
        if (isShopCouponSheetsInitialized) {
            listener.onVouchersImpressed(this@ShopCouponSheetViewComponent, getVisibleVouchers())
        }
        else isShopCouponSheetsInitialized = true

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        rvVoucherList.removeOnScrollListener(voucherScrollListener)
    }

    interface Listener {
        fun onCloseButtonClicked(view: ShopCouponSheetViewComponent)
        fun onVoucherScrolled(view: ShopCouponSheetViewComponent, lastPositionViewed: Int)
        fun onVouchersImpressed(
            view: ShopCouponSheetViewComponent,
            vouchers: List<MerchantVoucherUiModel>
        )

        fun onCopyVoucherCodeClicked(
            view: ShopCouponSheetViewComponent,
            voucher: MerchantVoucherUiModel
        )
    }
}