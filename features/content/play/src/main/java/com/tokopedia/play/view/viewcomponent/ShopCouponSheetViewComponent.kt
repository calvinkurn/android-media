package com.tokopedia.play.view.viewcomponent

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.ui.productsheet.adapter.MerchantVoucherAdapter
import com.tokopedia.play.ui.productsheet.viewholder.MerchantVoucherViewHolder
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * @author by astidhiyaa on 25/11/21
 */
class ShopCouponSheetViewComponent (container: ViewGroup,
                                    private val listener: Listener
) : ViewComponent(container, R.id.cl_shop_coupon_sheet){

    private val bottomSheetBehavior = BottomSheetBehavior.from(rootView)
    private val clCouponContent: ConstraintLayout = findViewById(R.id.cl_coupon_content)
    private val vBottomOverlay: View = findViewById(R.id.v_bottom_overlay)
    private val rvVoucherList: RecyclerView = findViewById(R.id.rv_voucher_list)
    private val tvSheetTitle: TextView = findViewById(com.tokopedia.play_common.R.id.tv_sheet_title)
    private val clProductEmpty: ConstraintLayout = findViewById(R.id.cl_product_empty)

    private val voucherAdapter = MerchantVoucherAdapter(object : MerchantVoucherViewHolder.Listener {
        override fun onCopyVoucherCode(voucher: MerchantVoucherUiModel) {
        }
    })

    private val layoutManagerVoucherList = object : LinearLayoutManager(rvVoucherList.context, RecyclerView.VERTICAL, false) {
        override fun onLayoutCompleted(state: RecyclerView.State?) {
            super.onLayoutCompleted(state)
        }
    }

    init {
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { _, insets ->
            vBottomOverlay.layoutParams = vBottomOverlay.layoutParams.apply {
                height = insets.systemWindowInsetBottom
            }
            clCouponContent.setPadding(clCouponContent.paddingLeft, clCouponContent.paddingTop, clCouponContent.paddingRight, insets.systemWindowInsetBottom)

            insets
        }

        findViewById<ImageView>(com.tokopedia.play_common.R.id.iv_sheet_close)
            .setOnClickListener {
                listener.onCloseButtonClicked(this)
            }

        rvVoucherList.apply {
            layoutManager = layoutManagerVoucherList
            adapter = voucherAdapter
        }

        tvSheetTitle.text = getString(R.string.play_title_coupon)
    }

    override fun show() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun setVoucherList(voucherList: List<MerchantVoucherUiModel>){
        showContainer(voucherList.isNotEmpty(), voucherList)
    }


    private fun showContainer(shouldShow: Boolean, voucherList: List<MerchantVoucherUiModel> = listOf()){
        if (shouldShow){
            voucherAdapter.setItemsAndAnimateChanges(voucherList)
            rvVoucherList.show()
            clProductEmpty.hide()
        }else{
            rvVoucherList.hide()
            clProductEmpty.show()
        }
    }

    fun showWithHeight(height: Int) {
        if (rootView.height != height) {
            val layoutParams = rootView.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            rootView.layoutParams = layoutParams
        }
        show()
    }

    interface Listener {
        fun onCloseButtonClicked(view: ShopCouponSheetViewComponent)
    }
}