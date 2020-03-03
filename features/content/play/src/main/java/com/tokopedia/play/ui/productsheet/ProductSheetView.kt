package com.tokopedia.play.ui.productsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.ui.productsheet.adapter.ProductSheetAdapter
import com.tokopedia.play.view.uimodel.ProductSheetUiModel

/**
 * Created by jegul on 02/03/20
 */
class ProductSheetView(container: ViewGroup) : UIView(container) {

    private val view: View = LayoutInflater.from(container.context).inflate(R.layout.view_product_sheet, container, true)
            .findViewById(R.id.cl_product_sheet)

    private val tvSheetTitle: TextView = view.findViewById(R.id.tv_sheet_title)
    private val rvDiscountProduct: RecyclerView = view.findViewById(R.id.rv_discount_product)

    private val productSheetAdapter = ProductSheetAdapter()

    private val bottomSheetBehavior = BottomSheetBehavior.from(view)

    private val maxHeight : Int
            get() = (container.height * PERCENT_PRODUCT_SHEET_HEIGHT).toInt()

    init {
        view.findViewById<ImageView>(R.id.iv_close)
                .setOnClickListener {
                    hide()
                }

        rvDiscountProduct.apply {
            layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            adapter = productSheetAdapter
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
        productSheetAdapter.setItemsAndAnimateChanges(model.contentList)
    }

    companion object {
        private const val PERCENT_PRODUCT_SHEET_HEIGHT = 0.6
    }
}