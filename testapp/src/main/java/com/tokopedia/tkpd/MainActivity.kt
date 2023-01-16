package com.tokopedia.tkpd

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.sellerorder.list.presentation.activities.SomListActivity
import com.tokopedia.tkpd.testgql.TestGqlUseCase
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.main_testapp.*

class MainActivity : AppCompatActivity() {

    val REQUEST_CODE_LOGIN = 123
    val REQUEST_CODE_LOGOUT = 456
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_testapp)
        userSession = UserSession(this)

        if (TokopediaUrl.getInstance().GQL.contains("staging")) {
            testapp_environment?.text = "STAGING URL"
            testapp_environment?.setBackgroundColor(Color.parseColor("#e67e22"))
        } else {
            testapp_environment?.text = "LIVE URL"
            testapp_environment?.setBackgroundColor(Color.parseColor("#27ae60"))
        }

        toggle_dark_mode.isChecked = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        toggle_dark_mode.setOnCheckedChangeListener { _: CompoundButton?, state: Boolean ->
            AppCompatDelegate.setDefaultNightMode(if (state) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        }

        loginButton.setOnClickListener {
            if (!userSession.isLoggedIn) {
                startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
            } else {
                Toast.makeText(this, "Already logged in", Toast.LENGTH_SHORT).show()
                goTo()
            }
        }

        /* use mainapp login use case */
        logoutButton.setOnClickListener {
            val logoutIntent = RouteManager.getIntent(this, ApplinkConstInternalUserPlatform.LOGOUT).apply {
                putExtra(ApplinkConstInternalUserPlatform.PARAM_IS_RETURN_HOME, false)
            }
            startActivityForResult(logoutIntent, REQUEST_CODE_LOGOUT)
        }

        testGqlButton.setOnClickListener { TestGqlUseCase().execute() }

        devOptButton.setOnClickListener {
            RouteManager.route(this, ApplinkConst.DEVELOPER_OPTIONS)
        }

        etAppLink.setText(getDefaultAppLink())

        goToButton.setOnClickListener { goTo() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        setLoginStatus()
        when (requestCode) {
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

    private fun setLoginStatus() {
        if (userSession.isLoggedIn) {
            val identity = if (userSession.email.isNotEmpty()) userSession.email else userSession.phoneNumber
            loginButton?.text = "Logged in as:\n$identity"
            logoutButton.visibility = View.VISIBLE
        } else {
            loginButton?.text = "Login"
            logoutButton.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        setLoginStatus()
    }

    private fun goTo() {
        /* @example: open groupchat module;
         * startActivity(PlayActivity.getCallingIntent(this, "668", true))
         * or, you can use route like this:
         * RouteManager.route(this, ApplinkConstInternalMarketplace.SHOP_SETTINGS)
         * LEAVE THIS EMPTY AS DEFAULT!!
         * */
        val appLink = SomListActivity.createIntent(this)
        startActivity(appLink)
    }

    private fun getDefaultAppLink(): String {
        /*
         * Put your default applink here
         */
        return ""
    }
}
