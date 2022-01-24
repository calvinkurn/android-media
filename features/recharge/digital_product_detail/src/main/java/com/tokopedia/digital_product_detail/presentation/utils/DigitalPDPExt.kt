package com.tokopedia.digital_product_detail.presentation.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.Menu
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital_product_detail.presentation.activity.DigitalPDPPulsaActivity
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.unifyprinciples.R
import java.lang.ref.WeakReference
import kotlin.math.abs

private const val TOOLBAR_ICON_SIZE = 64

fun AppBarLayout.setupDynamicAppBar(
    isErrorMessageShown: () -> Boolean,
    isInputEmpty: () -> Boolean,
    onCollapseAppBar: () -> Unit,
    onExpandAppBar: () -> Unit
) {
    addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
        var lastOffset = -1
        var lastIsCollapsed = false

        override fun onOffsetChanged(p0: AppBarLayout?, verticalOffSet: Int) {
            if (lastOffset == verticalOffSet) return

            lastOffset = verticalOffSet
            if (abs(verticalOffSet) >= totalScrollRange && !lastIsCollapsed) {
                if (!isErrorMessageShown() && !isInputEmpty()) {
                    //Collapsed
                    lastIsCollapsed = true
                    onCollapseAppBar()
                }
            } else if (verticalOffSet == 0 && lastIsCollapsed) {
                //Expanded
                lastIsCollapsed = false
                onExpandAppBar()
            }
        }
    })
}

fun Menu.setupOrderListIcon(activityRef: WeakReference<Activity>) {
    activityRef.get()?.let { activity ->
        activity.menuInflater.inflate(com.tokopedia.digital_product_detail.R.menu.menu_pdp, this)
        val iconUnify = getIconUnifyDrawable(
            activity,
            IconUnify.LIST_TRANSACTION,
            ContextCompat.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
        )
        iconUnify?.toBitmap()?.let {
            this.getItem(0).setOnMenuItemClickListener {
                RouteManager.route(activity, ApplinkConst.DIGITAL_ORDER)
                true
            }
            this.getItem(0).icon = BitmapDrawable(
                activity.resources,
                Bitmap.createScaledBitmap(it, TOOLBAR_ICON_SIZE, TOOLBAR_ICON_SIZE, true))
        }
    }
}