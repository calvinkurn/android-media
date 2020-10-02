package com.tokopedia.play.broadcaster.view.partial

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.transition.ScaleTransition
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by jegul on 29/05/20
 */
class BottomActionViewComponent(
        container: ViewGroup,
        private val listener: Listener
) : ViewComponent(container, R.id.bottom_sheet_action) {

    private val clInventory: ConstraintLayout = findViewById(R.id.cl_inventory)
    private val ivInventory: ImageView = findViewById(R.id.iv_inventory)
    private val btnAction: UnifyButton = findViewById(R.id.btn_action)
    private val tvBadgeCount: TextView = findViewById(R.id.tv_badge_count)
    private val flInventoryClickArea: FrameLayout = findViewById(R.id.fl_inventory_click_area)

    init {
        btnAction.setOnClickListener {
            if (!btnAction.isLoading) listener.onNextButtonClicked()
        }
    }

    fun setupBottomActionWithProducts(productList: List<ProductContentUiModel>) {
        onBottomActionTransition()
        if (productList.isEmpty()) {
            ivInventory.setImageResource(R.drawable.ic_play_inventory_disabled)
            ivInventory.isClickable = false
            btnAction.isEnabled = false
            tvBadgeCount.gone()
            flInventoryClickArea.setOnClickListener {  }
        } else {
            ivInventory.setImageResource(R.drawable.ic_play_inventory)
            ivInventory.isClickable = true
            btnAction.isEnabled = true
            tvBadgeCount.visible()
            tvBadgeCount.text = productList.size.toString()
            flInventoryClickArea.setOnClickListener { listener.onInventoryIconClicked() }
        }
    }

    fun setLoading(isLoading: Boolean) {
        btnAction.isLoading = isLoading
    }

    fun getButtonView(): View = btnAction

    private fun onBottomActionTransition() {
        TransitionManager.beginDelayedTransition(
                rootView as ViewGroup,
                ScaleTransition()
                        .addTarget(tvBadgeCount)
                        .setDuration(300)
        )
    }

    interface Listener {
        fun onInventoryIconClicked()
        fun onNextButtonClicked()
    }
}