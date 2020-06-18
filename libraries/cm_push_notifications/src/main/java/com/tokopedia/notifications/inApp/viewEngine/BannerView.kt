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
import com.tokopedia.applink.RouteManager
import com.tokopedia.notifications.R
import com.tokopedia.notifications.inApp.CMInAppManager
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

    private val dialog by lazy {
        alertDialog.create()
    }

    fun dialog(data: CMInApp) {
        alertDialog.setView(createView(data))
        alertDialog.setCancelable(false)
        dialog.show()

        // resize dialog's width with 80% of screen
        setWindowAttributes(dialog, getDisplayMetrics(mActivity.get()).first)
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
    }

    private fun viewState(data: CMInApp) {
        when (data.getType()) {
            TYPE_FULL_SCREEN_IMAGE_ONLY -> {
                fullScreenImageOnly(data)
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

    private fun onActionClicked(data: CMInApp) {
        dialog?.dismiss()
        dismissInteractionTracking(data)
    }

    private fun setCloseButton(data: CMInApp) {
        btnClose.setOnClickListener {
            dialog.dismiss()
            dismissInteractionTracking(data)
        }
    }

    private fun dismissInteractionTracking(data: CMInApp) {
        CMInAppManager.getCmInAppListener().apply {
            if (this != null) {
                onCMInAppClosed(data)
                onCMinAppDismiss(data)
                onCMinAppInteraction(data)
            }
        }
    }

    private fun fullScreenImageOnly(data: CMInApp) {
        lstActionButton.visibility = View.GONE

        imgBanner.setOnClickListener {
            dismissInteractionTracking(data)

            RouteManager.route(
                    mActivity.get(),
                    data.getCmLayout().getAppLink()
            )
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