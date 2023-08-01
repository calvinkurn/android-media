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
import com.tokopedia.mvcwidget.FollowWidgetType
import com.tokopedia.mvcwidget.IntentManger
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.setMargin
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.trackers.MvcTracker
import com.tokopedia.mvcwidget.views.MvcDetailView
import com.tokopedia.mvcwidget.views.MvcView
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.user.session.UserSession
import timber.log.Timber

class TransParentActivity : BaseActivity() {
    var isOnResume = false
    private var childView: MvcDetailView? = null
    private var appLink: String? = null
    private var shopName: String? = null
    val mvcTracker: MvcTracker = MvcTracker()
    var mvcDataHashcode: Int = 0

    companion object {
        const val SHOP_ID = "shopId"
        const val PRODUCT_ID = "productId"
        const val MVC_SOURCE = "mvcSource"
        const val REDIRECTION_LINK = "redirectionLink"
        const val SHOP_NAME = "shopName"
        const val DATA_HASH_CODE = "dataHash"
        const val ADDITIONAL_PARAM_JSON = "additionalParamJson"

        fun getIntent(
            context: Context,
            shopId: String,
            @MvcSource source: Int,
            redirectionLink: String = "",
            shopName: String = "",
            hashCode: Int = 0,
            productId: String = "",
            additionalParamJson: String = ""
        ): Intent {
            val intent = Intent(context, TransParentActivity::class.java)
            intent.putExtra(SHOP_ID, shopId)
            intent.putExtra(PRODUCT_ID, productId)
            intent.putExtra(MVC_SOURCE, source)
            intent.putExtra(REDIRECTION_LINK, redirectionLink)
            intent.putExtra(SHOP_NAME, shopName)
            intent.putExtra(DATA_HASH_CODE, hashCode)
            intent.putExtra(ADDITIONAL_PARAM_JSON, additionalParamJson)
            return intent
        }
    }

    val REQUEST_CODE_LOGIN = 12
    var additionalParamJson: String = ""
    lateinit var userSession: UserSession
    lateinit var shopId: String
    lateinit var productId: String

    @MvcSource
    var mvcSource = MvcSource.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(this)
        handleDimming()
        shopId = intent.extras?.getString(SHOP_ID, "0") ?: "0"
        productId = intent.extras?.getString(PRODUCT_ID, "") ?: ""
        additionalParamJson = intent.extras?.getString(ADDITIONAL_PARAM_JSON, "") ?: ""
        mvcSource = intent.extras?.getInt(MVC_SOURCE, MvcSource.DEFAULT) ?: MvcSource.DEFAULT
        appLink = intent.extras?.getString(REDIRECTION_LINK, "") ?: ""
        shopName = intent.extras?.getString(SHOP_NAME, "") ?: ""
        mvcDataHashcode = intent.extras?.getInt(DATA_HASH_CODE, 0) ?: 0

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
        bottomSheet.isDragable = true
        bottomSheet.isHideable = true
        bottomSheet.showKnob = true
        bottomSheet.showCloseIcon = false
        bottomSheet.bottomSheet.isGestureInsetBottomIgnored = true
        bottomSheet.customPeekHeight = (Resources.getSystem().displayMetrics.heightPixels / 2).toDp()

        bottomSheet.setTitle(getString(R.string.mvc_daftar_kupon_toko))
        childView = MvcDetailView(this)

        if (!appLink.isNullOrEmpty()) {
            childView?.findViewById<LinearLayout>(R.id.btn_layout)?.visibility = View.VISIBLE
            childView?.findViewById<UnifyButton>(R.id.btn_continue)?.let { button ->
                if (mvcSource == MvcSource.DISCO) {
                    button.text = getString(R.string.mvc_kunjungi_toko)
                }
                button.setOnClickListener {
                    bottomSheet.dismiss()
                    shopName?.let { it1 ->
                        mvcTracker.userClickBottomSheetCTA(
                            childView?.widgetType ?: FollowWidgetType.DEFAULT,
                            it1,
                            userSession.userId
                        )
                    }
                    RouteManager.route(this, appLink)
                }
            }
        }
        bottomSheet.setChild(childView)
        bottomSheet.show(supportFragmentManager, "BottomSheet Tag")
        childView?.show(shopId, false, mvcSource, mvcTracker, productId, additionalParamJson)
        bottomSheet.setShowListener {
            val titleMargin = dpToPx(16).toInt()
            bottomSheet.bottomSheetWrapper.setPadding(0, dpToPx(16).toInt(), 0, 0)
            bottomSheet.bottomSheetTitle.setMargin(titleMargin, 0, 0, 0)
        }

        bottomSheet.setOnDismissListener {
            if (isOnResume) {
                if (childView?.bundleForDataUpdate != null) {
                    val intent = IntentManger.getJadiMemberIntent(childView?.bundleForDataUpdate!!)
                    setResult(MvcView.RESULT_CODE_OK, intent)
                }
                finish()
                mvcTracker.closeMainBottomSheet(childView?.widgetType ?: FollowWidgetType.DEFAULT, shopId, userSession.userId, mvcSource)
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
