package com.tokopedia.play.broadcaster.view.partial

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.ProductUiModel
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by jegul on 29/05/20
 */
class BottomActionPartialView(
        container: ViewGroup,
        listener: Listener
) {

    private val rootView: View = container.findViewById(R.id.bottom_sheet_action)
    private val ivInventory: ImageView = container.findViewById(R.id.iv_inventory)
    private val btnAction: UnifyButton = container.findViewById(R.id.btn_action)
    private val tvBadgeCount: TextView = container.findViewById(R.id.tv_badge_count)

    init {
        ivInventory.setOnClickListener { listener.onInventoryIconClicked() }
    }

    fun setupBottomActionWithProducts(productList: List<ProductUiModel>) {
        if (productList.isEmpty()) {
            ivInventory.setImageResource(R.drawable.ic_play_inventory_disabled)
            ivInventory.isClickable = false
            btnAction.isEnabled = false
            tvBadgeCount.gone()
        } else {
            ivInventory.setImageResource(R.drawable.ic_play_inventory)
            ivInventory.isClickable = true
            btnAction.isEnabled = true
            tvBadgeCount.visible()
            tvBadgeCount.text = productList.size.toString()
        }
    }

    fun show() {
        rootView.visible()
    }

    fun hide() {
        rootView.gone()
    }

    interface Listener {

        fun onInventoryIconClicked()
    }
}