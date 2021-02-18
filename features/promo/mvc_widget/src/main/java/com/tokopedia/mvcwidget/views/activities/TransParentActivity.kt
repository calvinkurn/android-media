package com.tokopedia.mvcwidget.views.activities

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.setMargin
import com.tokopedia.mvcwidget.views.MvcDetailView
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.user.session.UserSession

class TransParentActivity : BaseActivity() {
    companion object{
        const val SHOP_ID = "shopId"
    }

    val REQUEST_CODE_LOGIN = 12
    val userSession = UserSession(this)
    lateinit var shopId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopId = intent.extras?.getString(SHOP_ID, "0") ?: "0"
        if (userSession.isLoggedIn) {
            showMvcDetailDialog()
        } else {
            val loginIntent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
            startActivityForResult(loginIntent, REQUEST_CODE_LOGIN)
        }
    }

    fun showMvcDetailDialog() {
        val bottomSheet = BottomSheetUnify()
        bottomSheet.isDragable = true
        bottomSheet.isHideable = true
        bottomSheet.showKnob = true
        bottomSheet.showCloseIcon = false
        bottomSheet.customPeekHeight = (Resources.getSystem().displayMetrics.heightPixels/2).toDp()
        bottomSheet.bottomSheet.isGestureInsetBottomIgnored = true

        bottomSheet.setTitle(getString(R.string.mvc_daftar_kupon_toko))
        val childView = MvcDetailView(this)
        bottomSheet.setChild(childView)
        bottomSheet.show(supportFragmentManager, "BottomSheet Tag")
        childView.show(shopId, false)
        bottomSheet.setShowListener {
            val titleMargin = dpToPx(16).toInt()
            bottomSheet.bottomSheetWrapper.setPadding(0, dpToPx(16).toInt(), 0, 0)
            bottomSheet.bottomSheetTitle.setMargin(titleMargin, 0, 0, 0)
        }

        bottomSheet.setOnDismissListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_LOGIN -> {
                if (userSession.isLoggedIn) {
                    showMvcDetailDialog()
                } else {
                    finish()
                }
            }
        }
    }
}