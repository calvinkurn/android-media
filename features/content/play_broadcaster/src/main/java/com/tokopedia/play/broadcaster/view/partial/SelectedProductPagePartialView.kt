package com.tokopedia.play.broadcaster.view.partial

import android.content.Context
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayGridTwoItemDecoration
import com.tokopedia.play.broadcaster.ui.viewholder.ProductSelectableViewHolder
import com.tokopedia.play.broadcaster.view.adapter.ProductSelectableAdapter
import com.tokopedia.play.broadcaster.view.uimodel.ProductUiModel

/**
 * Created by jegul on 28/05/20
 */
class SelectedProductPagePartialView(
        private val container: ViewGroup,
        listener: Listener
) {

    val isShown: Boolean
        get() = bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED

    private val clSelectedProduct: ConstraintLayout = container.findViewById(R.id.cl_selected_product)
    private val vDragArea: View = container.findViewById(R.id.v_drag_area)
    private val tvSelectedProductTitle: TextView = container.findViewById(R.id.tv_selected_product_title)
    private val rvSelectedProduct: RecyclerView = container.findViewById(R.id.rv_selected_product)
    private val parentCoordinator = clSelectedProduct.parent as View

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


        vDragArea.setOnTouchListener(object : View.OnTouchListener {

            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                println("DragTouch: ${MotionEvent.actionToString(event.action)}")
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        parentCoordinator.parent.requestDisallowInterceptTouchEvent(true)
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val currentY = event.rawY.toInt()
                        bottomSheetBehavior.peekHeight = container.height - currentY
                    }
                    MotionEvent.ACTION_UP -> {
                        parentCoordinator.parent.requestDisallowInterceptTouchEvent(false)
                        if (bottomSheetBehavior.peekHeight >= 0.6 * parentCoordinator.height) show()
                        else hide()
                    }
                }

                return false
            }
        })

        hide()
    }

    fun show() {
        bottomSheetBehavior.peekHeight = parentCoordinator.height
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun setSelectedProductList(productList: List<ProductUiModel>) {
        updateTitle(productList.size)
        selectableProductAdapter.setItems(productList)
        selectableProductAdapter.notifyDataSetChanged()
    }

    fun onSelectedProductsUpdated(productList: List<ProductUiModel>) {
        updateTitle(productList.size)
    }

    private fun updateTitle(productCount: Int) {
        tvSelectedProductTitle.text = context.getString(R.string.play_selected_products, productCount)
    }

    interface Listener {

        fun onProductSelectStateChanged(productId: Long, isSelected: Boolean)
    }
}