package com.tokopedia.gamification.giftbox.presentation.activities

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.giftbox.data.di.component.DaggerGiftBoxActivityComponent
import com.tokopedia.gamification.giftbox.presentation.fragments.GiftBoxDailyFragment
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

open class BaseGiftBoxActivity : BaseActivity() {

    @Inject
    lateinit var userSession: UserSession
    private val REQUEST_CODE_LOGIN = 10
    private lateinit var fm: FrameLayout
    private val requiredPermissions: Array<String>
        get() = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)


    fun getLayout() = com.tokopedia.gamification.R.layout.activity_gift_box_daily

    open fun getDestinationFragment(): Fragment = GiftBoxDailyFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        fm = findViewById(com.tokopedia.gamification.R.id.fmGiftRoot)

        val component = DaggerGiftBoxActivityComponent.builder()
                .activityContextModule(ActivityContextModule(this))
                .build()
        component.inject(this)

        ActivityCompat.requestPermissions(this, requiredPermissions, 1001)

        if (savedInstanceState == null) {
            checkLoggedIn()
        }
    }

    fun showGiftBoxFragment() {
        supportFragmentManager
                .beginTransaction()
                .add(fm.id, getDestinationFragment())
                .commit()

    }

    fun checkLoggedIn() {
        if (userSession.isLoggedIn) {
            showGiftBoxFragment()
        } else {
            val loginIntent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
            startActivityForResult(loginIntent, REQUEST_CODE_LOGIN)
        }
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
        when (requestCode) {
            REQUEST_CODE_LOGIN -> {
                afterLoginAttempt()
            }
        }
    }
}