package com.tokopedia.play.ui.variantsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.ui.productsheet.ProductSheetView
import com.tokopedia.play.view.uimodel.ProductSheetUiModel
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by jegul on 05/03/20
 */
class VariantSheetView(
        container: ViewGroup,
        listener: Listener
) : UIView(container) {

    private val view: View = LayoutInflater.from(container.context).inflate(R.layout.view_variant_sheet, container, true)
            .findViewById(R.id.cl_variant_sheet)

    private val tvSheetTitle: TextView = view.findViewById(R.id.tv_sheet_title)
    private val rvVariantList: RecyclerView = view.findViewById(R.id.rv_variant_list)
    private val btnAction: UnifyButton = view.findViewById(R.id.btn_action)

    private val bottomSheetBehavior = BottomSheetBehavior.from(view)

    init {
        view.findViewById<ImageView>(R.id.iv_close)
                .setOnClickListener {
                    listener.onCloseButtonClicked(this)
                }

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->

            v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, insets.systemWindowInsetBottom)

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
    }

    interface Listener {
        fun onCloseButtonClicked(view: VariantSheetView)
    }
}