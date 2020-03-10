package com.tokopedia.play.ui.productsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.ui.productsheet.adapter.MerchantVoucherAdapter
import com.tokopedia.play.ui.productsheet.adapter.ProductLineAdapter
import com.tokopedia.play.ui.productsheet.itemdecoration.MerchantVoucherItemDecoration
import com.tokopedia.play.ui.productsheet.itemdecoration.ProductLineItemDecoration
import com.tokopedia.play.ui.productsheet.viewholder.ProductLineViewHolder
import com.tokopedia.play.view.uimodel.ProductSheetUiModel

/**
 * Created by jegul on 02/03/20
 */
class ProductSheetView(
        container: ViewGroup,
        listener: Listener
) : UIView(container) {

    private val view: View = LayoutInflater.from(container.context).inflate(R.layout.view_product_sheet, container, true)
            .findViewById(R.id.cl_product_sheet)

    private val clProductContent: ConstraintLayout = view.findViewById(R.id.cl_product_content)
    private val tvSheetTitle: TextView = view.findViewById(R.id.tv_sheet_title)
    private val rvProductList: RecyclerView = view.findViewById(R.id.rv_product_list)
    private val rvVoucherList: RecyclerView = view.findViewById(R.id.rv_voucher_list)
    private val vBottomOverlay: View = view.findViewById(R.id.v_bottom_overlay)

    private val productLineAdapter = ProductLineAdapter(object : ProductLineViewHolder.Listener {
        override fun onBuyProduct(productId: String) {
            listener.onBuyButtonClicked(this@ProductSheetView, productId)
        }

        override fun onAtcProduct(productId: String) {
            listener.onAtcButtonClicked(this@ProductSheetView, productId)
        }
    })
    private val voucherAdapter = MerchantVoucherAdapter()

    private val bottomSheetBehavior = BottomSheetBehavior.from(view)

    init {
        view.findViewById<ImageView>(R.id.iv_close)
                .setOnClickListener {
                    listener.onCloseButtonClicked(this)
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

            vBottomOverlay.layoutParams = vBottomOverlay.layoutParams.apply {
                height = insets.systemWindowInsetBottom
            }
            clProductContent.setPadding(clProductContent.paddingLeft, clProductContent.paddingTop, clProductContent.paddingRight, insets.systemWindowInsetBottom)

            insets
        }
    }

    override val containerId: Int = view.id

    override fun show() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    internal fun setStateHidden() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    internal fun showWithHeight(height: Int) {
        if (view.height != height) {
            val layoutParams = view.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            view.layoutParams = layoutParams
        }

        show()
    }

    internal fun setProductSheet(model: ProductSheetUiModel) {
        tvSheetTitle.text = model.title
        voucherAdapter.setItemsAndAnimateChanges(model.voucherList)
        productLineAdapter.setItemsAndAnimateChanges(model.productList)
    }

    interface Listener {
        fun onCloseButtonClicked(view: ProductSheetView)
        fun onBuyButtonClicked(view: ProductSheetView, productId: String)
        fun onAtcButtonClicked(view: ProductSheetView, productId: String)
    }
}