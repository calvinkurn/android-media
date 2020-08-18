package com.tokopedia.notifications.inApp.viewEngine

import android.app.Activity
import android.app.AlertDialog
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.notifications.R
import com.tokopedia.notifications.analytics.InAppAnalytics
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMButton
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMLayout
import com.tokopedia.notifications.inApp.viewEngine.CmInAppConstant.*
import com.tokopedia.notifications.inApp.viewEngine.adapter.ActionButtonAdapter
import com.tokopedia.unifycomponents.setImage
import java.lang.ref.WeakReference

internal open class BannerView(activity: Activity) {

    private val mActivity = WeakReference<Activity>(activity)
    private val alertDialog = AlertDialog.Builder(mActivity.get())

    private lateinit var imgBanner: ImageView
    private lateinit var btnClose: ImageView
    private lateinit var lstActionButton: RecyclerView

    private val analytics by lazy { InAppAnalytics }

    private val listener by lazy {
        CMInAppManager.getCmInAppListener()
    }

    private val dialog by lazy {
        alertDialog.create()
    }

    fun dialog(data: CMInApp) {
        alertDialog.setView(createView(data))
        alertDialog.setCancelable(false)
        dialog.show()

        // resize dialog's width with 80% of screen
        setWindowAttributes(dialog, getDisplayMetrics(mActivity.get()).first)

        // impression tracker
        analytics.impression(data)
    }

    private fun createView(data: CMInApp): View? {
        val parentView = View.inflate(
                mActivity.get(),
                R.layout.cm_layout_banner_inapp,
                null
        )

        // pre view setup
        onViewCreated(parentView, data)
        viewState(data)
        return parentView
    }

    private fun onViewCreated(container: View, data: CMInApp) {
        // casting view component
        imgBanner = container.findViewById(R.id.imgBanner)
        btnClose = container.findViewById(R.id.btnClose)
        lstActionButton = container.findViewById(R.id.lstActionButton)

        // set data view
        setBanner(data)
        setActionButton(data)
        setCloseButton(data)
        setBannerClicked(data)
    }

    private fun viewState(data: CMInApp) {
        when (data.getType()) {
            TYPE_INTERSTITIAL_IMAGE_ONLY -> {
                lstActionButton.visibility = View.GONE
            }
        }
    }

    private fun setBanner(data: CMInApp) {
        val layout = data.getCmLayout()
        imgBanner.setImage(layout.getImg(), 0f)
    }

    private fun setActionButton(data: CMInApp) {
        val layout = data.getCmLayout()

        if (layout.getButton() == null || layout.getButton().isEmpty()) {
            lstActionButton.visibility = View.GONE
            return
        }

        lstActionButton.layoutManager = when (layout.getBtnOrientation()) {
            ORIENTATION_VERTICAL -> LinearLayoutManager(mActivity.get())
            ORIENTATION_HORIZONTAL -> GridLayoutManager(
                    mActivity.get(),
                    layout.getButton().size
            )
            else -> LinearLayoutManager(mActivity.get())
        }

        val adapter = ActionButtonAdapter(data, ::onActionClicked)
        lstActionButton.setHasFixedSize(true)
        lstActionButton.adapter = adapter
    }

    private fun onActionClicked(button: CMButton, data: CMInApp) {
        trackAppLinkClick(data, button.getAppLink(), ElementType(ElementType.BUTTON))
        analytics.click(data)
        dialog?.dismiss()
    }

    private fun setCloseButton(data: CMInApp) {
        btnClose.setOnClickListener {
            dismissInteractionTracking(data)
            dialog.dismiss()
        }
    }

    private fun getBannerAppLink(data: CMInApp): String {
        with(data.getCmLayout()) {
            return if (data.type == TYPE_INTERSTITIAL && getButton().isNotEmpty()) {
                getButton().first().getAppLink()
            } else {
                getAppLink()
            }
        }
    }

    private fun setBannerClicked(data: CMInApp) {
        // prevent banner click if has more than one CTA button
        with(data.getCmLayout()) {
            if (getButton().size > 1) return
            val bannerAppLink = getBannerAppLink(data)

            imgBanner.setOnClickListener {
                trackAppLinkClick(data, bannerAppLink, ElementType(ElementType.MAIN))
                analytics.click(data)
                dialog?.dismiss()
            }
        }
    }

    private fun dismissInteractionTracking(data: CMInApp) {
        listener?.let {
            it.onCMInAppClosed(data)
            it.onCMinAppDismiss(data)
            it.onCMinAppInteraction(data)
        }
    }

    private fun trackAppLinkClick(
            data: CMInApp,
            appLink: String,
            elementType: ElementType
    ) {
        listener?.let {
            it.onCMInAppLinkClick(appLink, data, elementType)
            it.onCMinAppDismiss(data)
            it.onCMinAppInteraction(data)
        }
    }

    companion object {
        private fun getDisplayMetrics(activity: Activity?): Pair<Int, Int> {
            val displayMetrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            val displayWidth = displayMetrics.widthPixels
            val displayHeight = displayMetrics.heightPixels

            return Pair(displayWidth, displayHeight)
        }

        private fun setWindowAttributes(dialog: AlertDialog?, displayWidth: Int) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog?.window?.attributes)
            layoutParams.width = (displayWidth * 0.8f).toInt() // 80%
            dialog?.window?.attributes = layoutParams
        }

        @JvmStatic
        fun create(activity: Activity, data: CMInApp) {
            BannerView(activity).dialog(data)
        }
    }
}