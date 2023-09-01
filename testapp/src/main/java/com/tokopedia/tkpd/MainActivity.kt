package com.tokopedia.tkpd

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class MainActivity : BaseActivity() {

    val REQUEST_CODE_LOGIN = 123
    val REQUEST_CODE_LOGOUT = 456
    val REQUEST_CODE_DEVELOPER_OPTIONS = 789
    lateinit var userSession: UserSessionInterface

    private val model = mutableStateOf(
        Model(getDefaultAppLink(), false, "Login")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(this)
        model.value = model.value.copy(isDarkModeChecked = getDarkModeStatus())
        setContent {
            NestTheme {
                var modelState by remember { model }
                HomeScreen(
                    model = modelState,
                    onDarkModeChanged = {
                        val newState = modelState.isDarkModeChecked.not()
                        setDarkModeAndRecreate(newState)
                        modelState = modelState.copy(isDarkModeChecked = newState)
                    },
                    onApplinkChanged = { modelState = modelState.copy(applink = it) },
                    onNavigateTo = {
                        when (it) {
                            HomeDestination.LOGIN -> handleNavigationLogin()
                            HomeDestination.LOGOUT -> handleNavigationLogout()
                            HomeDestination.DEVELOPER_OPTION -> gotoDeveloperOptions()
                            HomeDestination.APPLINK -> goTo()
                        }
                    }
                )
            }
        }
    }

    private fun gotoDeveloperOptions() {
        RouteManager.route(this, ApplinkConst.DEVELOPER_OPTIONS)
    }

    private fun setDarkModeAndRecreate(active: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (active) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        startActivity(Intent(this, this.javaClass))
    }

    private fun getDarkModeStatus(): Boolean =
        resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    private fun handleNavigationLogin() {
        if (!userSession.isLoggedIn) {
            startActivityForResult(
                RouteManager.getIntent(this, ApplinkConst.LOGIN),
                REQUEST_CODE_LOGIN
            )
        } else {
            Toast.makeText(this, "Already logged in", Toast.LENGTH_SHORT).show()
            goTo()
        }
    }

    private fun handleNavigationLogout() {
        val logoutIntent =
            RouteManager.getIntent(this, ApplinkConstInternalUserPlatform.LOGOUT).apply {
                putExtra(ApplinkConstInternalUserPlatform.PARAM_IS_RETURN_HOME, false)
            }
        startActivityForResult(logoutIntent, REQUEST_CODE_LOGOUT)
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
            REQUEST_CODE_DEVELOPER_OPTIONS -> {
                if (userSession.isLoggedIn) {
                    goTo()
                }
            }
        }
    }

    private fun setLoginStatus() {
        if (userSession.isLoggedIn) {
            val identity =
                if (userSession.email.isNotEmpty()) userSession.email else userSession.phoneNumber
            model.value = model.value.copy(isLoggedIn = true, loginText = "Logged in as:\n$identity")
        } else {
            model.value = model.value.copy(isLoggedIn = false, loginText = "Login")
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
        if (model.value.applink.isNotBlank()) {
            RouteManager.route(this, model.value.applink)
        } else {
            Toast.makeText(this, "Please input appLink / webLink", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getDefaultAppLink(): String {
        /*
         * Put your default applink here
         */
        return ""
    }

    data class Model(
        val applink: String = "",
        val isDarkModeChecked: Boolean = false,
        val loginText: String = "Login",
        val isLoggedIn: Boolean = false
    )

    sealed interface HomeDestination {
        object LOGIN : HomeDestination
        object LOGOUT : HomeDestination
        object DEVELOPER_OPTION : HomeDestination
        object APPLINK : HomeDestination
    }
}
