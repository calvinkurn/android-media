package com.tokopedia.play.ui.productsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.ui.productsheet.adapter.MerchantVoucherAdapter
import com.tokopedia.play.ui.productsheet.adapter.ProductLineAdapter
import com.tokopedia.play.ui.productsheet.itemdecoration.MerchantVoucherItemDecoration
import com.tokopedia.play.ui.productsheet.itemdecoration.ProductLineItemDecoration
import com.tokopedia.play.view.uimodel.ProductSheetUiModel

/**
 * Created by jegul on 02/03/20
 */
class ProductSheetView(container: ViewGroup) : UIView(container) {

    private val view: View = LayoutInflater.from(container.context).inflate(R.layout.view_product_sheet, container, true)
            .findViewById(R.id.cl_product_sheet)

    private val tvSheetTitle: TextView = view.findViewById(R.id.tv_sheet_title)
    private val rvProductList: RecyclerView = view.findViewById(R.id.rv_product_list)
    private val rvVoucherList: RecyclerView = view.findViewById(R.id.rv_voucher_list)

    private val productLineAdapter = ProductLineAdapter()
    private val voucherAdapter = MerchantVoucherAdapter()

    private val bottomSheetBehavior = BottomSheetBehavior.from(view)

    private val maxHeight : Int
            get() = (container.height * PERCENT_PRODUCT_SHEET_HEIGHT).toInt()

    init {
        view.findViewById<ImageView>(R.id.iv_close)
                .setOnClickListener {
                    hide()
                }

        rvProductList.apply {
            layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            adapter = productLineAdapter
            addItemDecoration(ProductLineItemDecoration(view.context))
        }

        rvVoucherList.apply {
            layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
            adapter = voucherAdapter
            addItemDecoration(MerchantVoucherItemDecoration(view.context))
        }

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->

            v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, insets.systemWindowInsetBottom)

            insets
        }
    }

    override val containerId: Int = view.id

    override fun show() {
        if (view.height != maxHeight) {
            val layoutParams = view.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = maxHeight
            view.layoutParams = layoutParams
        }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    internal fun setProductSheet(model: ProductSheetUiModel) {
        tvSheetTitle.text = model.title
        voucherAdapter.setItemsAndAnimateChanges(model.voucherList)
        productLineAdapter.setItemsAndAnimateChanges(model.productList)
    }

    companion object {
        private const val PERCENT_PRODUCT_SHEET_HEIGHT = 0.6
    }
}