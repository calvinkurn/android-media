package com.tokopedia.gamification.giftbox.presentation.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.data.di.component.DaggerGiftBoxActivityComponent
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

abstract class BaseGiftBoxActivity : AppCompatActivity() {

    @Inject
    lateinit var userSession: UserSession
    private val REQUEST_CODE_LOGIN = 10

    abstract fun getLayout(): Int
    abstract fun getDestinationFragment(): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())

        val component = DaggerGiftBoxActivityComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
        component.inject(this)


        if (savedInstanceState == null) {
            checkLoggedIn()
        }
    }

    fun showGiftBoxFragment() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fm, getDestinationFragment())
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