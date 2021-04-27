package com.tokopedia.mvcwidget.views.activities

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.mvcwidget.MvcSource
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.Tracker
import com.tokopedia.mvcwidget.setMargin
import com.tokopedia.mvcwidget.views.MvcDetailView
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.user.session.UserSession
import timber.log.Timber

class TransParentActivity : BaseActivity() {
    var isOnResume = false

    companion object {
        const val SHOP_ID = "shopId"
        const val MVC_SOURCE = "mvcSource"

        fun getIntent(context: Context, shopId: String, @MvcSource source: Int): Intent {
            val intent = Intent(context, TransParentActivity::class.java)
            intent.putExtra(SHOP_ID, shopId)
            intent.putExtra(MVC_SOURCE, source)
            return intent
        }

    }

    val REQUEST_CODE_LOGIN = 12
    lateinit var userSession : UserSession
    lateinit var shopId: String
    @MvcSource
    var mvcSource = MvcSource.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(this)
        handleDimming()
        shopId = intent.extras?.getString(SHOP_ID, "0") ?: "0"
        mvcSource = intent.extras?.getInt(MVC_SOURCE, MvcSource.DEFAULT) ?: MvcSource.DEFAULT
        if (userSession.isLoggedIn) {
            showMvcDetailDialog()
        } else {
            val loginIntent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
            startActivityForResult(loginIntent, REQUEST_CODE_LOGIN)
        }
    }

    fun handleDimming(){
        try{
            window.setDimAmount(0f)
        }catch (th:Throwable){
            Timber.e(th)
        }
    }

    fun showMvcDetailDialog() {
        val bottomSheet = BottomSheetUnify()
        bottomSheet.isDragable = false
        bottomSheet.isHideable = true
        bottomSheet.showKnob = true
        bottomSheet.showCloseIcon = false
        bottomSheet.isFullpage = true
        bottomSheet.bottomSheet.isGestureInsetBottomIgnored = true

        bottomSheet.setTitle(getString(R.string.mvc_daftar_kupon_toko))
        val childView = MvcDetailView(this)
        bottomSheet.setChild(childView)
        bottomSheet.show(supportFragmentManager, "BottomSheet Tag")
        childView.show(shopId, false, mvcSource)
        bottomSheet.setShowListener {
            val titleMargin = dpToPx(16).toInt()
            bottomSheet.bottomSheetWrapper.setPadding(0, dpToPx(16).toInt(), 0, 0)
            bottomSheet.bottomSheetTitle.setMargin(titleMargin, 0, 0, 0)
        }

        bottomSheet.setOnDismissListener {
            if (isOnResume) {
                finish()
                Tracker.closeMainBottomSheet(shopId, UserSession(this).userId, mvcSource)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        isOnResume = true
    }

    override fun onStop() {
        super.onStop()
        isOnResume = false
    }

    override fun onPause() {
        super.onPause()
        isOnResume = false
    }

    override fun onDestroy() {
        super.onDestroy()
        isOnResume = false
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