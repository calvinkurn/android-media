package com.tokopedia.gamification.giftbox.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.giftbox.data.di.component.DaggerGiftBoxActivityComponent
import com.tokopedia.gamification.giftbox.presentation.fragments.GiftBoxDailyFragment
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

open class BaseGiftBoxActivity : AppCompatActivity() {

    private val TAG = "BaseGift"

    @Inject
    lateinit var userSession: UserSession
    private val REQUEST_CODE_LOGIN = 10
    private lateinit var fm: FrameLayout

    fun getLayout() = com.tokopedia.gamification.R.layout.activity_gift_box_daily

    open fun getDestinationFragment(): Fragment = GiftBoxDailyFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.wtf(TAG, "onCreate start")
        setContentView(getLayout())
        fm = findViewById(com.tokopedia.gamification.R.id.fm)

        val component = DaggerGiftBoxActivityComponent.builder()
                .activityContextModule(ActivityContextModule(this))
                .build()
        component.inject(this)

        if (savedInstanceState == null) {
            Log.wtf(TAG, "onCreate savedins == null")
            checkLoggedIn()
        }
        Log.wtf(TAG, "onCreate ends")
    }

    fun showGiftBoxFragment() {
        Log.wtf(TAG, "showGiftBoxFragment start")
        Log.wtf(TAG, "fm == null ->${fm == null}")
        supportFragmentManager
                .beginTransaction()
                .add(fm.id, getDestinationFragment())
                .commit()
        Log.wtf(TAG, "showGiftBoxFragment end")

    }

    fun checkLoggedIn() {
        Log.wtf(TAG, "checkLoggedIn start")
        if (userSession.isLoggedIn) {
            showGiftBoxFragment()
        } else {
            val loginIntent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
            startActivityForResult(loginIntent, REQUEST_CODE_LOGIN)
        }
        Log.wtf(TAG, "checkLoggedIn end")
    }

    fun afterLoginAttempt() {
        if (userSession.isLoggedIn) {
            showGiftBoxFragment()
        } else {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.wtf(TAG, "onActivityResult start")
        when (requestCode) {
            REQUEST_CODE_LOGIN -> {
                afterLoginAttempt()
            }
        }
        Log.wtf(TAG, "onActivityResult end")
    }
}