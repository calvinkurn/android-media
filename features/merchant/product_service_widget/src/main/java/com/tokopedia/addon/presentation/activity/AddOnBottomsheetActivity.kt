package com.tokopedia.addon.presentation.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.addon.presentation.bottomsheet.AddOnBottomSheet
import com.tokopedia.addon.presentation.uimodel.AddOnApplinkMapper
import com.tokopedia.applink.RouteManager
import com.tokopedia.product_service_widget.R

class AddOnBottomsheetActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gifting_bottomsheet)
        setOrientation()
        parseApplinkData()
    }

    private fun parseApplinkData() {
        RouteManager.getIntent(this, intent.data.toString()).data?.let { dataUri ->
            val pageSource = AddOnApplinkMapper.getPageSourceFromUri(dataUri)
            val cartId = AddOnApplinkMapper.getCartIdFromUri(dataUri)
            val selectedAddonIds = AddOnApplinkMapper.getSelectedAddonIdsFromUri(dataUri)
            val deselectedAddonIds = AddOnApplinkMapper.getDeselectedAddonIdsFromUri(dataUri)
            val atcSource = AddOnApplinkMapper.getAtcSourceFromUri(dataUri)
            val addOnParam = AddOnApplinkMapper.getAddOnWidgetParamFromUri(dataUri)

            val bottomSheet = AddOnBottomSheet().apply {
                setOnDismissListener {
                    finish()
                }
            }.apply {
                setAddOnWidgetParam(addOnParam)
                setPageSource(pageSource)
                setCartId(cartId)
                setSelectedAddonIds(selectedAddonIds)
                setDeselectedAddonIds(deselectedAddonIds)
                setAtcSource(atcSource)
            }
            bottomSheet.show(supportFragmentManager, "")
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun setOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
