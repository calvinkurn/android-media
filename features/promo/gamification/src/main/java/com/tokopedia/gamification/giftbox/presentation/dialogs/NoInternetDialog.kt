package com.tokopedia.gamification.giftbox.presentation.dialogs

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.gamification.R

class NoInternetDialog {

    lateinit var llParent: LinearLayout
    lateinit var btnRetry: View
    lateinit var btnSettings: View
    lateinit var closeAbleDialog: CloseableBottomSheetDialog

    fun showDialog(context: Context) {
        val resId = R.layout.dialog_gami_no_internet
        val v = LayoutInflater.from(context).inflate(resId, null, false)
        closeAbleDialog = CloseableBottomSheetDialog.createInstanceCloseableRounded(context, {})
        closeAbleDialog.setContentView(v)

        llParent = v.findViewById(R.id.llParent)
        btnRetry = v.findViewById(R.id.btnRetry)
        btnSettings = v.findViewById(R.id.btnSettings)

        setCustomCloseIcon()
        setClicks(context)
        closeAbleDialog.show()
        expandBottomSheet()
    }

    fun setCustomCloseIcon() {
        if (llParent.parent is ViewGroup && llParent.parent.parent is ViewGroup) {
            val imageClose = (llParent.parent.parent as ViewGroup).findViewById<ImageView>(com.tokopedia.design.R.id.close_button_rounded)
            imageClose.setImageResource(R.drawable.ic_close_default)
        }
    }

    fun setClicks(context: Context) {
        btnSettings.setOnClickListener {
            try {
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                context.startActivity(intent)
            } catch (ex: Exception) {
                //Do nothing
            }
        }
    }

    private fun expandBottomSheet() {
        var bottomSheetFmContainer: ViewGroup? = null
        var bottomSheetCoordinatorLayout: CoordinatorLayout? = null
        var child: ViewGroup = llParent
        var parent: ViewParent = llParent.parent

        while (!(parent is CoordinatorLayout)) {
            if (parent is ViewGroup) {
                child = parent
            }
            parent = parent.parent
        }
        bottomSheetFmContainer = child
        bottomSheetCoordinatorLayout = parent
        if (bottomSheetCoordinatorLayout != null && bottomSheetFmContainer is FrameLayout) {
            BottomSheetBehavior.from(bottomSheetFmContainer).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
}