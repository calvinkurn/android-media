package com.tokopedia.gamification.giftbox.presentation.dialogs

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.analytics.GtmEvents
import com.tokopedia.gamification.giftbox.analytics.GtmGiftTapTap
import com.tokopedia.gamification.giftbox.presentation.activities.GiftBoxDailyActivity
import com.tokopedia.gamification.giftbox.presentation.activities.GiftBoxTapTapActivity
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSession

class NoInternetDialog {

    lateinit var llParent: LinearLayout
    lateinit var btnRetry: View
    lateinit var btnSettings: View
    lateinit var imageClose: View
    var bottomSheet: BottomSheetUnify?=null

    fun showDialog(context: Context) {
        if(context is AppCompatActivity){
            val resId = com.tokopedia.gamification.R.layout.dialog_gami_no_internet
            val v = LayoutInflater.from(context).inflate(resId, null, false)
            bottomSheet = BottomSheetUnify()
            bottomSheet?.isDragable = false
            bottomSheet?.showCloseIcon = false
            bottomSheet?.showHeader = false
            bottomSheet?.setChild(v)

            llParent = v.findViewById(R.id.llParent)
            btnRetry = v.findViewById(R.id.btnRetry)
            btnSettings = v.findViewById(R.id.btnSettings)
            imageClose = v.findViewById(R.id.imageClose)

            setClicks(context)
            bottomSheet?.show(context.supportFragmentManager,"no_internet")
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
            bottomSheet?.dismiss()
        }


    }
}