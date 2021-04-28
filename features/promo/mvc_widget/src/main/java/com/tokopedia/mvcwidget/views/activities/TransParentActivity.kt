package com.tokopedia.mvcwidget.views.activities

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
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
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.user.session.UserSession
import timber.log.Timber

class TransParentActivity : BaseActivity() {
    var isOnResume = false
    var isShowButton = false
    private var childView: MvcDetailView? = null
    private var appLink : String?=null

    companion object {
        const val SHOP_ID = "shopId"
        const val MVC_SOURCE = "mvcSource"
        const val IS_BUTTON_SHOW = "isButtonShow"
        const val REDIRECTION_LINK = "redirectionLink"

        fun getIntent(context: Context, shopId: String, @MvcSource source: Int, showButton: Boolean = false, redirectionLink: String = ""): Intent {
            val intent = Intent(context, TransParentActivity::class.java)
            intent.putExtra(SHOP_ID, shopId)
            intent.putExtra(MVC_SOURCE, source)
            intent.putExtra(IS_BUTTON_SHOW, showButton)
            intent.putExtra(REDIRECTION_LINK, redirectionLink)

            return intent
        }

    }

    val REQUEST_CODE_LOGIN = 12
    lateinit var userSession: UserSession
    lateinit var shopId: String

    @MvcSource
    var mvcSource = MvcSource.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(this)
        handleDimming()
        shopId = intent.extras?.getString(SHOP_ID, "0") ?: "0"
        mvcSource = intent.extras?.getInt(MVC_SOURCE, MvcSource.DEFAULT) ?: MvcSource.DEFAULT
        isShowButton = intent.extras?.getBoolean(IS_BUTTON_SHOW, false) ?: false
        appLink = intent.extras?.getString(REDIRECTION_LINK, "") ?: ""


        if (userSession.isLoggedIn) {
            showMvcDetailDialog()
        } else {
            val loginIntent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
            startActivityForResult(loginIntent, REQUEST_CODE_LOGIN)
        }
    }

    fun handleDimming() {
        try {
            window.setDimAmount(0f)
        } catch (th: Throwable) {
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
        childView = MvcDetailView(this)

        if (isShowButton) {
            childView?.findViewById<LinearLayout>(R.id.btn_layout)?.visibility = View.VISIBLE
            childView?.findViewById<UnifyButton>(R.id.btn_continue)?.setOnClickListener {
                bottomSheet.dismiss()
                RouteManager.route(this,appLink)
            }
        }
        bottomSheet.setChild(childView)
        bottomSheet.show(supportFragmentManager, "BottomSheet Tag")
        childView?.show(shopId, false, mvcSource)
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

    fun setTokoButtonClickListener(onClickListener: View.OnClickListener?) {
        childView?.findViewById<UnifyButton>(R.id.btn_continue)?.setOnClickListener(onClickListener)
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