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
            val productId = AddOnApplinkMapper.getProductIdFromUri(dataUri)
            val pageSource = AddOnApplinkMapper.getPageSourceFromUri(dataUri)
            val cartId = AddOnApplinkMapper.getCartIdFromUri(dataUri)
            val selectedAddonIds = AddOnApplinkMapper.getSelectedAddonIdsFromUri(dataUri)
            val warehouseId = AddOnApplinkMapper.getWarehouseIdFromUri(dataUri)
            val isTokocabang = AddOnApplinkMapper.getIsTokocabangFromUri(dataUri)

            val bottomSheet = AddOnBottomSheet().apply {
                setOnDismissListener {
                    finish()
                }
            }.apply {
                setProductId(productId)
                setPageSource(pageSource)
                setCartId(cartId)
                setSelectedAddonIds(selectedAddonIds)
                setWarehouseId(warehouseId)
                setIsTokocabang(isTokocabang)
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
