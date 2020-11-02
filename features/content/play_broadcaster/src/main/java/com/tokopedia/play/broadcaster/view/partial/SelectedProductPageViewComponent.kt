package com.tokopedia.play.broadcaster.view.partial

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayGridTwoItemDecoration
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.ProductSelectableViewHolder
import com.tokopedia.play.broadcaster.view.adapter.ProductSelectableAdapter
import com.tokopedia.play_common.util.scroll.StopFlingScrollListener
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 28/05/20
 */
class SelectedProductPageViewComponent(
        container: ViewGroup,
        listener: Listener
) : ViewComponent(container, R.id.cl_selected_product) {

    private val clSelectedProduct: ConstraintLayout = rootView as ConstraintLayout
    private val tvSelectedProductTitle: TextView = findViewById(R.id.tv_selected_product_title)
    private val rvSelectedProduct: RecyclerView = findViewById(R.id.rv_selected_product)

    private val context: Context
        get() = clSelectedProduct.context

    private val bottomSheetBehavior = BottomSheetBehavior.from(clSelectedProduct)

    private val selectableProductAdapter = ProductSelectableAdapter(object : ProductSelectableViewHolder.Listener {
        override fun onProductSelectStateChanged(productId: Long, isSelected: Boolean) {
            listener.onProductSelectStateChanged(productId, isSelected)
        }

        override fun onProductSelectError(reason: Throwable) {

        }
    })

    init {
        rvSelectedProduct.adapter = selectableProductAdapter
        rvSelectedProduct.addItemDecoration(PlayGridTwoItemDecoration(context))
        rvSelectedProduct.addOnScrollListener(StopFlingScrollListener())

        hide()
    }

    override fun show() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun isShown(): Boolean {
        return bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED
    }

    fun setSelectedProductList(productList: List<ProductContentUiModel>) {
        updateTitle(productList.size)
        selectableProductAdapter.setItems(productList)
        selectableProductAdapter.notifyDataSetChanged()
    }

    fun onSelectedProductsUpdated(productList: List<ProductContentUiModel>) {
        updateTitle(productList.size)
    }

    private fun updateTitle(productCount: Int) {
        tvSelectedProductTitle.text = context.getString(R.string.play_selected_products, productCount)
    }

    companion object {

        private const val COLLAPSED_THRESHOLD = 0.6
    }

    interface Listener {

        fun onProductSelectStateChanged(productId: Long, isSelected: Boolean)
    }
}