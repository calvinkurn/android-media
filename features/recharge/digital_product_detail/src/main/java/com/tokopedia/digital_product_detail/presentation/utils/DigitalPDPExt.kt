package com.tokopedia.digital_product_detail.presentation.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.Menu
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital_product_detail.presentation.listener.DigitalHistoryIconListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import java.lang.ref.WeakReference
import kotlin.math.abs

private const val TOOLBAR_ICON_SIZE = 64

fun NestedScrollView.setupDynamicScrollListener(
    isErrorMessageShown: () -> Boolean,
    isInputEmpty: () -> Boolean,
    onCollapseAppBar: () -> Unit,
    onExpandAppBar: () -> Unit
) {
    setOnScrollChangeListener(object: NestedScrollView.OnScrollChangeListener {
        var lastIsCollapsed = false

        override fun onScrollChange(
            v: NestedScrollView,
            scrollX: Int,
            scrollY: Int,
            oldScrollX: Int,
            oldScrollY: Int
        ) {
            if (scrollY == 0 && lastIsCollapsed) {
                lastIsCollapsed = false
                onExpandAppBar()
            }

            if (scrollY != 0 && scrollY > oldScrollY && !lastIsCollapsed) {
                lastIsCollapsed = true
                if (!isErrorMessageShown() && !isInputEmpty()) {
                    onCollapseAppBar()
                }
            }
        }
    })
}

fun Menu.setupOrderListIcon(activityRef: WeakReference<Activity>, listener: DigitalHistoryIconListener?) {
    activityRef.get()?.let { activity ->
        activity.menuInflater.inflate(com.tokopedia.digital_product_detail.R.menu.menu_pdp, this)
        val iconUnify = getIconUnifyDrawable(
            activity,
            IconUnify.LIST_TRANSACTION,
            ContextCompat.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
        )
        iconUnify?.toBitmap()?.let {
            this.getItem(0).setOnMenuItemClickListener {
                listener?.onClickDigitalIconHistory()
                RouteManager.route(activity, ApplinkConst.DIGITAL_ORDER)
                true
            }
            this.getItem(0).icon = BitmapDrawable(
                activity.resources,
                Bitmap.createScaledBitmap(it, TOOLBAR_ICON_SIZE, TOOLBAR_ICON_SIZE, true))
        }
    }
}

fun SortFilterItem.toggle() {
    type = if (type == ChipsUnify.TYPE_NORMAL) {
        ChipsUnify.TYPE_SELECTED
    } else {
        ChipsUnify.TYPE_NORMAL
    }
}
