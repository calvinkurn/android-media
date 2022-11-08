package com.tokopedia.tkpd

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common_compose.principles.NestButton
import com.tokopedia.common_compose.principles.NestHeader
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class MainActivity : AppCompatActivity() {

    val REQUEST_CODE_LOGIN = 123
    val REQUEST_CODE_LOGOUT = 456
    lateinit var userSession: UserSessionInterface

    private val applink = mutableStateOf(getDefaultAppLink())
    private val darkMode = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(this)
        darkMode.value = getDarkModeStatus()
        delegate.applyDayNight()
        setContent {
            NestTheme {
                val applinkStr by remember { applink }
                val darkModeBool by remember { darkMode }
                TestAppHome(
                    Model(applinkStr, getLiveStatus(), darkModeBool),
                    {
                        val newState = darkMode.value.not()
                        setDarkModeAndRecreate(newState)
                        darkMode.value = newState
                    },
                    { this.applink.value = it },
                    {
                        when (it) {
                            HomeDestination.LOGIN -> handleNavigationLogin()
                            HomeDestination.LOGOUT -> handleNavigationLogout()
                            HomeDestination.DEVELOPER_OPTION -> gotoDeveloperOptions()
                            HomeDestination.APPLINK -> goTo()
                        }
                    })
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

    private fun getLiveStatus(): String {
        return if (TokopediaUrl.getInstance().GQL.contains("staging")) {
            "STAGING URL"
        } else {
            "LIVE URL"
        }
    }

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
        }
    }

    private fun setLoginStatus() {
        if (userSession.isLoggedIn) {
            val identity =
                if (userSession.email.isNotEmpty()) userSession.email else userSession.phoneNumber
            findViewById<TextView>(R.id.loginButton)?.run {
                text = "Logged in as:\n${identity}"
                visibility = View.VISIBLE
            }
        } else {
            findViewById<TextView>(R.id.loginButton)?.run {
                text = "Login"
                visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        setLoginStatus()
    }

    private fun goTo() {
        /* @example: open groupchat module;
         * startActivity(PlayActivity.getCallingIntent(this, "668", true))
         * or, you can use route like this:
         * RouteManager.route(this, ApplinkConstInternalMarketplace.SHOP_SETTINGS)
         * LEAVE THIS EMPTY AS DEFAULT!!
         * */
        if (applink.value.isNotBlank()) RouteManager.route(this, applink.value)
        else Toast.makeText(this, "Please input appLink / webLink", Toast.LENGTH_SHORT).show()
    }

    private fun getDefaultAppLink(): String {
        /*
         * Put your default applink here
         */
        return ""
    }

    data class Model(
        val applink: String = "",
        val urlState: String = "live",
        val isDarkModeChecked: Boolean = false
    )
}

sealed interface HomeDestination {
    object LOGIN : HomeDestination
    object LOGOUT : HomeDestination
    object DEVELOPER_OPTION : HomeDestination
    object APPLINK : HomeDestination
}

@SuppressLint("UnsupportedDarkModeColor")
@Composable
fun TestAppHome(
    model: MainActivity.Model = MainActivity.Model(),
    onDarkModeChanged: () -> Unit = {},
    onApplinkChanged: (String) -> Unit = {},
    onNavigateTo: (HomeDestination) -> Unit = {}
) {
    Surface {
        Column {
            NestHeader(title = "Tokopedia Test App", showBackNav = false)
            val urlBgColor =
                (if (model.urlState.contains("live", true)) "#27ae60" else "#e67e22").toColorInt()
            Text(
                text = model.urlState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(Color(urlBgColor)),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onPrimary,
                style = NestTheme.typography.heading5
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 72.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                NestButton(
                    text = "Login",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateTo(HomeDestination.LOGIN) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                NestButton(
                    text = "Logout",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateTo(HomeDestination.LOGOUT) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                NestButton(
                    text = "Developer Option",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateTo(HomeDestination.DEVELOPER_OPTION) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = model.applink,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    onValueChange = onApplinkChanged,
                    textStyle = NestTheme.typography.body3,
                    label = {
                        Text(text = "applink")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                NestButton(
                    text = "Open",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateTo(HomeDestination.APPLINK) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onDarkModeChanged() }
                        .padding(8.dp)
                ) {
                    Checkbox(checked = model.isDarkModeChecked, onCheckedChange = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Force Dark Mode")
                }
            }
        }
    }
}

@Preview
@Composable
fun TestAppHomePreview() {
    NestTheme {
        TestAppHome()
    }
}
