package com.tokopedia.tkpd

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.applink.RouteManager
import com.tokopedia.common_compose.principles.NestButton
import com.tokopedia.common_compose.principles.NestHeader
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.user.session.UserSessionInterface

class MainActivity : AppCompatActivity() {

    val REQUEST_CODE_LOGIN = 123
    val REQUEST_CODE_LOGOUT = 456
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestAppHome()
        }
//        userSession = UserSession(this)
//
//        if (TokopediaUrl.getInstance().GQL.contains("staging")) {
//            testapp_environment?.text = "STAGING URL"
//            testapp_environment?.setBackgroundColor(Color.parseColor("#e67e22"))
//        } else {
//            testapp_environment?.text = "LIVE URL"
//            testapp_environment?.setBackgroundColor(Color.parseColor("#27ae60"))
//        }
//
//        toggle_dark_mode.isChecked = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
//        toggle_dark_mode.setOnCheckedChangeListener { _: CompoundButton?, state: Boolean ->
//            AppCompatDelegate.setDefaultNightMode(if (state) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
//        }
//
//        loginButton.setOnClickListener {
//            if (!userSession.isLoggedIn) {
//                startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
//            } else {
//                Toast.makeText(this, "Already logged in", Toast.LENGTH_SHORT).show()
//                goTo()
//            }
//        }
//
//        /* use mainapp login use case */
//        logoutButton.setOnClickListener {
//            val logoutIntent = RouteManager.getIntent(this, ApplinkConstInternalUserPlatform.LOGOUT).apply {
//                putExtra(ApplinkConstInternalUserPlatform.PARAM_IS_RETURN_HOME, false)
//            }
//            startActivityForResult(logoutIntent, REQUEST_CODE_LOGOUT)
//        }
//
//        testGqlButton.setOnClickListener { TestGqlUseCase().execute() }
//
//        devOptButton.setOnClickListener {
//            RouteManager.route(this, ApplinkConst.DEVELOPER_OPTIONS)
//        }
//
//        etAppLink.setText(getDefaultAppLink())
//
//        goToButton.setOnClickListener { goTo() }
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
        val appLink = findViewById<EditText>(R.id.etAppLink).text.toString()
        if (appLink.isNotBlank())
            RouteManager.route(this, appLink)
        else Toast.makeText(this, "Please input appLink / webLink", Toast.LENGTH_SHORT).show()
    }

    private fun getDefaultAppLink(): String {
        /*
         * Put your default applink here
         */
        return ""
    }
}

@Composable
fun TestAppHome() {

    var applink by remember { mutableStateOf("") }
    var isDarkMode by remember { mutableStateOf(false) }
    Surface {
        Column {
            NestHeader(title = "Tokopedia Test App", showBackNav = false)
            Text(
                text = "live",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Cyan),
                textAlign = TextAlign.Center,
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                }
                Spacer(modifier = Modifier.height(16.dp))
                NestButton(
                    text = "Logout",
                    modifier = Modifier.fillMaxWidth()
                ) {

                }
                Spacer(modifier = Modifier.height(16.dp))
                NestButton(
                    text = "Developer Option",
                    modifier = Modifier.fillMaxWidth()
                ) {

                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = applink,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    onValueChange = { applink = it },
                    label = {
                        Text(text = "applink")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                NestButton(
                    text = "Open",
                    modifier = Modifier.fillMaxWidth()
                ) {

                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { isDarkMode = isDarkMode.not() }
                        .padding(8.dp)
                ) {
                    Checkbox(checked = isDarkMode, onCheckedChange = null)
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
