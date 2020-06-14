package com.tokopedia.notifications.inApp.viewEngine

import android.app.Activity
import android.view.View
import android.widget.ImageView
import com.tokopedia.applink.RouteManager
import com.tokopedia.notifications.R
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMButton
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMLayout
import com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant.TYPE_FULL_SCREEN_IMAGE_ONLY
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.setImage
import java.lang.ref.WeakReference

internal open class BannerView(activity: Activity) {

    private val mActivity = WeakReference<Activity>(activity)

    private lateinit var imgBanner: ImageView
    private lateinit var btnClose: ImageView
    private lateinit var btnAction: UnifyButton

    fun createView(data: CMInApp): View? {
        val parentView = View.inflate(
                mActivity.get(),
                R.layout.cm_layout_banner_inapp,
                null
        )
        onViewCreated(parentView, data)
        viewState(data)
        return parentView
    }

    private fun onViewCreated(container: View, data: CMInApp) {
        // casting view component
        imgBanner = container.findViewById(R.id.imgBanner)
        btnClose = container.findViewById(R.id.btnClose)
        btnAction = container.findViewById(R.id.btnAction)

        // set data view
        val layout = data.getCmLayout()
        val action = layout.getButton().first()
        setBanner(layout, action)
        setActionButton(action)
        setCloseButton(data)
    }

    private fun viewState(data: CMInApp) {
        when (data.getType()) {
            TYPE_FULL_SCREEN_IMAGE_ONLY -> {
                fullScreenImageOnly()
            }
        }
    }

    private fun setBanner(layout: CMLayout, action: CMButton) {
        imgBanner.setImage(layout.getImg(), 0f)
        imgBanner.setOnClickListener {
            RouteManager.route(mActivity.get(), action.getAppLink())
        }
    }

    private fun setActionButton(action: CMButton) {
        btnAction.text = action.getTxt()
        btnAction.tag = action.getAppLink()
    }

    private fun setCloseButton(data: CMInApp) {
        btnClose.setOnClickListener {
            CMInAppManager.getCmInAppListener().apply {
                if (this != null) {
                    onCMInAppClosed(data)
                    onCMinAppDismiss()
                    onCMinAppInteraction(data)
                }
            }
        }
    }

    private fun fullScreenImageOnly() {
        btnAction.visibility = View.GONE
    }

}