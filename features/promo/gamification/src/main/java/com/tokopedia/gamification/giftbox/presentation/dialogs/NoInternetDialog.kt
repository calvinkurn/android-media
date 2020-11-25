package com.tokopedia.gamification.giftbox.presentation.dialogs

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.analytics.GtmEvents
import com.tokopedia.gamification.giftbox.analytics.GtmGiftTapTap
import com.tokopedia.gamification.giftbox.presentation.activities.GiftBoxDailyActivity
import com.tokopedia.gamification.giftbox.presentation.activities.GiftBoxTapTapActivity
import com.tokopedia.user.session.UserSession

class NoInternetDialog {

    lateinit var llParent: LinearLayout
    lateinit var btnRetry: View
    lateinit var btnSettings: View
    lateinit var imageClose: View
    lateinit var closeAbleDialog: CloseableBottomSheetDialog

    fun showDialog(context: Context) {
        val resId = com.tokopedia.gamification.R.layout.dialog_gami_no_internet
        val v = LayoutInflater.from(context).inflate(resId, null, false)
        closeAbleDialog = CloseableBottomSheetDialog.createInstanceRounded(context)
        closeAbleDialog.setContentView(v)


        llParent = v.findViewById(R.id.llParent)
        btnRetry = v.findViewById(R.id.btnRetry)
        btnSettings = v.findViewById(R.id.btnSettings)
        imageClose = v.findViewById(R.id.imageClose)

        hideDivider()
        setClicks(context)
        closeAbleDialog.show()
        expandBottomSheet()
    }

    fun hideDivider() {
        if (llParent.parent?.parent is ViewGroup) {
            val viewGroup = (llParent.parent?.parent as ViewGroup)
            val headerRounded = viewGroup.findViewById<View>(com.tokopedia.design.R.id.header_rounded)
            if (headerRounded != null) {
                headerRounded.visibility = View.GONE
            }

            val fmContainer = viewGroup.findViewById<View>(com.tokopedia.design.R.id.container)
            if (fmContainer != null) {
                fmContainer.setBackgroundResource(com.tokopedia.design.R.drawable.header_bottom_sheet_rounded_white)
            }
        }
    }

    fun setClicks(context: Context) {
        btnSettings.setOnClickListener {
            try {
                val userSession = UserSession(it.context)
                when(context){
                    is GiftBoxDailyActivity-> GtmEvents.clickSettingsButton(userSession.userId)
                    is GiftBoxTapTapActivity-> GtmGiftTapTap.clickSettingsButton(userSession.userId)
                }

                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                context.startActivity(intent)
            } catch (ex: Exception) {
                //Do nothing
            }
        }

        imageClose.setOnClickListener {
            val userSession = UserSession(it.context)
            GtmEvents.clickExitButton(userSession.userId)
            closeAbleDialog.dismiss()
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