package com.tokopedia.mvcwidget.views.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.setMargin
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.trackers.MvcTracker
import com.tokopedia.mvcwidget.views.MvcDetailView
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.user.session.UserSession

/**
 * Created By : Muhammad Furqan on 15/08/23
 */
class MvcDetailBottomSheet : BottomSheetUnify() {

    private val mvcTracker: MvcTracker = MvcTracker()

    private var applink: String? = null
    private var shopName: String? = null
    private var userSession: UserSession? = null
    private var shopId: String = ""
    private var productId: String = ""
    private var additionalParamJson: String = ""

    private var isOnResume: Boolean = false

    @MvcSource
    private var mvcSource = MvcSource.DEFAULT

    init {
        isDragable = true
        isHideable = true
        showKnob = true
        showCloseIcon = false
        bottomSheet.isGestureInsetBottomIgnored = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        customPeekHeight = (getScreenHeight() / 2).toDp()
        setTitle(getString(R.string.mvc_daftar_kupon_toko))
        context?.let {
            userSession = UserSession(it)
            val childView = MvcDetailView(it)

            if (!applink.isNullOrEmpty()) {
                childView.findViewById<LinearLayout>(R.id.btn_layout)?.visibility = View.VISIBLE
                childView.findViewById<UnifyButton>(R.id.btn_continue)?.let { button ->
                    if (mvcSource == MvcSource.DISCO) {
                        button.text = getString(R.string.mvc_kunjungi_toko)
                    }
                    button.setOnClickListener { _ ->
                        shopName?.let { mShopName ->
                            mvcTracker.userClickBottomSheetCTA(
                                childView.widgetType,
                                mShopName,
                                userSession?.userId ?: ""
                            )
                        }
                        RouteManager.route(it, applink)
                        dismiss()
                    }
                }
            }

            setOnDismissListener {
                if (isOnResume) {
                    mvcTracker.closeMainBottomSheet(
                        childView.widgetType,
                        shopId,
                        userSession?.userId ?: "",
                        mvcSource
                    )
                }
            }

            setChild(childView)
            childView.show(shopId, false, mvcSource, mvcTracker, productId, additionalParamJson)
        }

        setShowListener {
            val titleMargin = dpToPx(16).toInt()
            bottomSheetWrapper.setPadding(0, dpToPx(16).toInt(), 0, 0)
            bottomSheetTitle.setMargin(titleMargin, 0, 0, 0)
        }

        return super.onCreateView(inflater, container, savedInstanceState)
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

    fun show(
        shopId: String,
        @MvcSource source: Int,
        redirectionLink: String = "",
        shopName: String = "",
        productId: String = "",
        additionalParamJson: String = "",
        manager: FragmentManager,
        tag: String = "MvcDetailBottomSheet"
    ) {
        this.shopId = shopId
        this.mvcSource = source
        this.applink = redirectionLink
        this.shopName = shopName
        this.productId = productId
        this.additionalParamJson = additionalParamJson

        show(manager, tag)
    }
}
