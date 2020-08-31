package com.tokopedia.tkpd

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chuckerteam.chucker.api.Chucker
import com.tokopedia.application.MyApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalHome
import com.tokopedia.applink.internal.ApplinkConstInternalTestApp
import com.tokopedia.tkpd.helper.logout
import com.tokopedia.tkpd.network.DataSource
import com.tokopedia.tkpd.testgql.TestGqlUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.main_testapp.*

class MainActivity : AppCompatActivity() {

    val REQUEST_CODE_LOGIN = 123
    val REQUEST_CODE_LOGOUT = 456
    lateinit var userSession : UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_testapp)

        userSession = UserSession(this)

        if (userSession.deviceId.isNullOrEmpty()) {
            userSession.deviceId = DataSource.MOCK_DEVICE_ID
        }

        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            if (!userSession.isLoggedIn()) {
                startActivityForResult(RouteManager.getIntent(this, ApplinkConstInternalTestApp.LOGIN), REQUEST_CODE_LOGIN)
            } else {
                Toast.makeText(this, "Already logged in", Toast.LENGTH_SHORT).show()
                goTo()
            }
        }

        //still use old testapp logout process,
        // because real logout module still contains core_legacy
        // that will dramatically slows down compile time if included
        logoutButton.setOnClickListener {
            logout(application as MyApplication)
        }

        testGqlButton.setOnClickListener {
            TestGqlUseCase().execute()
        }

        devOptButton.setOnClickListener {
            RouteManager.route(this, ApplinkConst.DEVELOPER_OPTIONS)
        }

        val button = findViewById<Button>(R.id.button)
        val chuckButton = findViewById<Button>(R.id.chuckButton)

        chuckButton.setOnClickListener { startActivity(Chucker.getLaunchIntent(this, Chucker.SCREEN_HTTP)) }
        button.setOnClickListener {
            goTo()
        }


        useOldPageButton.setOnClickListener {
            startActivity(Intent(this, OldMainActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            REQUEST_CODE_LOGIN -> {
                if (userSession.isLoggedIn) {
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                    goTo()
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
            REQUEST_CODE_LOGOUT -> {
                if (!userSession.isLoggedIn) {
                    Toast.makeText(this, "Logout Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Logout Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun goTo() {
        /* @example: open groupchat module;
         * startActivity(PlayActivity.getCallingIntent(this, "668", true))
         * or, you can use route like this:
         * RouteManager.route(this, ApplinkConstInternalMarketplace.SHOP_SETTINGS) */
    }
}
