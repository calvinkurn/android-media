package com.tokopedia.developer_options.presentation.activity

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Process
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.coachmark.CoachMark2.Companion.isCoachmmarkShowAllowed
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.component.Dialog
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.branchlink.domain.BranchLinkUseCase
import com.tokopedia.developer_options.presentation.adapter.DeveloperOptionAdapter
import com.tokopedia.developer_options.presentation.adapter.DeveloperOptionDiffer
import com.tokopedia.developer_options.presentation.adapter.typefactory.DeveloperOptionTypeFactoryImpl
import com.tokopedia.developer_options.presentation.di.DaggerDevOptComponent
import com.tokopedia.developer_options.presentation.di.DevOptModule
import com.tokopedia.developer_options.presentation.viewholder.AccessTokenViewHolder
import com.tokopedia.developer_options.presentation.viewholder.BranchLinkViewHolder
import com.tokopedia.developer_options.presentation.viewholder.DevOptsAuthorizationViewHolder
import com.tokopedia.developer_options.presentation.viewholder.HomeAndNavigationRevampSwitcherViewHolder
import com.tokopedia.developer_options.presentation.viewholder.LoginHelperListener
import com.tokopedia.developer_options.presentation.viewholder.ResetOnBoardingViewHolder
import com.tokopedia.developer_options.presentation.viewholder.UrlEnvironmentViewHolder
import com.tokopedia.developer_options.session.DevOptLoginSession
import com.tokopedia.encryption.security.sha256
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.translator.manager.TranslatorManager
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl.Companion.deleteInstance
import com.tokopedia.url.TokopediaUrl.Companion.init
import com.tokopedia.url.TokopediaUrl.Companion.setEnvironment
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

/**
 * @author Said Faisal on 24/11/2021
 */

class DeveloperOptionActivity :
    BaseActivity(),
    DevOptsAuthorizationViewHolder.DevOptsAuthorizationListener {

    companion object {
        private const val CACHE_FREE_RETURN = "CACHE_FREE_RETURN"
        private const val API_KEY_TRANSLATOR = ""
        private const val RV_DEFAULT_POSITION = 0
        private const val RV_CACHE_SIZE = 20
        private const val LOGIN_HELPER_REQUEST_CODE = 789

        const val SHOW_AND_COPY_APPLINK_TOGGLE_NAME = "show_and_copy_applink_toggle_name"
        const val SHOW_AND_COPY_APPLINK_TOGGLE_KEY = "show_and_copy_applink_toggle_key"
        const val SHOW_AND_COPY_APPLINK_TOGGLE_DEFAULT_VALUE = false
        const val LEAK_CANARY_TOGGLE_SP_NAME = "mainapp_leakcanary_toggle"
        const val LEAK_CANARY_TOGGLE_KEY = "key_leakcanary_toggle"
        const val LEAK_CANARY_TOGGLE_KEY_SELLER = "key_leakcanary_toggle_seller"
        const val STRICT_MODE_LEAK_PUBLISHER_TOGGLE_KEY = "key_strict_mode_leak_publisher_toggle"
        const val STRICT_MODE_LEAK_PUBLISHER_TOGGLE_KEY_SELLER =
            "key_strict_mode_leak_publisher_toggle_seller"
        const val LEAK_CANARY_DEFAULT_TOGGLE = true
        const val STRICT_MODE_LEAK_PUBLISHER_DEFAULT_TOGGLE = false
        const val REMOTE_CONFIG_PREFIX = "remote_config_prefix"
        const val SHARED_PREF_FILE = "shared_pref_file"
        const val STAGING = "staging"
        const val LIVE = "live"
        const val CHANGEURL = "changeurl"
        const val URI_HOME_MACROBENCHMARK = "home-macrobenchmark"
        const val URI_COACHMARK = "coachmark"
        const val URI_COACHMARK_DISABLE = "disable"
        const val KEY_FIRST_VIEW_NAVIGATION = "KEY_FIRST_VIEW_NAVIGATION"
        const val KEY_FIRST_VIEW_NAVIGATION_ONBOARDING = "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING"
        const val KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1 =
            "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1"
        const val KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2 =
            "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2"
        const val KEY_P1_DONE_AS_NON_LOGIN = "KEY_P1_DONE_AS_NON_LOGIN"
        const val PREF_KEY_HOME_COACHMARK = "PREF_KEY_HOME_COACHMARK"
        const val PREF_KEY_HOME_COACHMARK_NAV = "PREF_KEY_HOME_COACHMARK_NAV"
        const val PREF_KEY_HOME_COACHMARK_INBOX = "PREF_KEY_HOME_COACHMARK_INBOX"
        const val PREF_KEY_HOME_COACHMARK_BALANCE = "PREF_KEY_HOME_COACHMARK_BALANCE"
        const val PREFERENCE_NAME = "coahmark_choose_address"
        const val EXTRA_IS_COACHMARK = "EXTRA_IS_COACHMARK"
        const val DEPRECATED_API_SWITCHER_TOASTER_SP_NAME = "deprecated_switcher_toggle"
        const val DEPRECATED_API_SWITCHER_TOASTER_KEY = "deprecated_switcher_key"
        const val PREF_KEY_FPI_MONITORING_POPUP = "fpi_monitoring_popup"
    }

    private var userSession: UserSession? = null
    private var rvDeveloperOption: RecyclerView? = null
    private var sbDeveloperOption: SearchBarUnify? = null
    private val remoteConfig by lazy { FirebaseRemoteConfigImpl(this) }
    private val loginSession by lazy { DevOptLoginSession(this) }
    private val gson by lazy { GsonBuilder().disableHtmlEscaping().create() }

    @Inject
    lateinit var branchLinkUseCase: BranchLinkUseCase

    private val adapter by lazy {
        DeveloperOptionAdapter(
            typeFactory = DeveloperOptionTypeFactoryImpl(
                accessTokenListener = clickAccessTokenBtn(),
                resetOnBoardingListener = clickResetOnBoarding(),
                urlEnvironmentListener = selectUrlEnvironment(),
                homeAndNavigationRevampListener = homeAndNavigationListener(),
                loginHelperListener = loginHelperListener(),
                authorizeListener = this,
                branchListener = getBranchListener()
            ),
            differ = DeveloperOptionDiffer()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkDebuggingModeOrNot()
        inject()
        autoAuthorized()
    }

    private fun autoAuthorized() {
        lifecycleScope.launch(Dispatchers.Default) {
            val url = URL("https://docs-android.tokopedia.net/api/dev-opt-pass")
            var connection: HttpURLConnection? = null
            val result = try {
                /* starting http request */
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 3000
                connection.connect()

                /* reading the response */
                val rd: BufferedReader = if (connection.responseCode != 200) {
                    BufferedReader(InputStreamReader(connection.errorStream))
                } else {
                    BufferedReader(InputStreamReader(connection.inputStream))
                }
                val content = StringBuilder()
                var line: String?
                while (rd.readLine().also { line = it } != null) {
                    content.append(line).append('\n')
                }
                content.toString()
            } catch (e: IOException) {
                e.printStackTrace()
                ""
            } finally {
                connection?.disconnect()
            }
            yield()
            if (result.isEmpty()) {
                return@launch
            }
            withContext(Dispatchers.Main) {
                onSubmitDevOptsPassword(
                    result
                        .replace("\n", "")
                        .sha256(),
                    true
                )
            }
        }
    }

    override fun getScreenName(): String = getString(R.string.screen_name)

    private fun inject() {
        DaggerDevOptComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .devOptModule(DevOptModule()).build().inject(this)
    }

    private fun checkDebuggingModeOrNot() {
        if (GlobalConfig.isAllowDebuggingTools()) {
            userSession = UserSession(this)
            setIntentData()
        } else {
            finish()
        }
    }

    private fun setIntentData() {
        val intent = intent
        var uri: Uri? = null
        var isChangeUrlApplink = false
        var isCoachmarkApplink = false
        var isHomeMacrobenchmarkApplink = false
        if (intent != null) {
            uri = intent.data
            if (uri != null) {
                isChangeUrlApplink = uri.pathSegments.size == 3 && uri.pathSegments[1] == CHANGEURL
                isCoachmarkApplink =
                    uri.pathSegments.size == 3 && uri.pathSegments[1] == URI_COACHMARK
                isHomeMacrobenchmarkApplink =
                    uri.pathSegments.size == 3 && uri.pathSegments[1] == URI_HOME_MACROBENCHMARK
            }
        }
        when {
            isChangeUrlApplink -> uri?.apply { handleUri(this) }
            isHomeMacrobenchmarkApplink -> userSession?.apply { handleHomeMacrobenchmarkUri(this) }
            isCoachmarkApplink -> uri?.apply { handleCoachmarkUri(this) }
            else -> {
                setContentView(R.layout.activity_developer_option)
                setRecyclerView()
                setSearchBar()
                initTranslator()
            }
        }
    }

    private fun setSearchBar() {
        sbDeveloperOption = findViewById(R.id.sbDeveloperOption)
        sbDeveloperOption?.searchBarTextField?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.let {
                    adapter.searchItem(it)
                    rvDeveloperOption?.scrollToPosition(RV_DEFAULT_POSITION)
                }
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) { /* no need to implement */
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) { /* no need to implement */
            }
        })
    }

    private fun setRecyclerView() {
        rvDeveloperOption = findViewById(R.id.rvDeveloperOption)
        rvDeveloperOption?.apply {
            adapter = this@DeveloperOptionActivity.adapter
            layoutManager = LinearLayoutManager(context)
            setItemViewCacheSize(RV_CACHE_SIZE)
        }

        val loggedIn = loginSession.isLoggedIn()
        clearSessionIfNotLoggedIn(loggedIn)

        adapter.setValueIsAuthorized(loggedIn)
        adapter.initializeList()
        adapter.setDefaultItem()
    }

    private fun handleUri(uri: Uri) {
        if (uri.lastPathSegment?.startsWith(STAGING) == true) {
            setEnvironment(this, Env.STAGING)
        } else if (uri.lastPathSegment?.startsWith(LIVE) == true) {
            setEnvironment(this, Env.LIVE)
        }
        deleteInstance()
        init(this)
        userSession?.logoutSession()
        Handler().postDelayed({ restart(this) }, 500)
    }

    /**
     * Call to restart the application process using the specified intents.
     *
     *
     * Behavior of the current process after invoking this method is undefined.
     */
    private fun restart(context: Context) {
        val intent = RouteManager
            .getIntent(context, ApplinkConst.HOME)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        if (context is Activity) {
            context.finish()
        }
        Process.killProcess(Process.myPid())
    }

    private fun handleHomeMacrobenchmarkUri(userSession: UserSessionInterface) {
        isCoachmmarkShowAllowed = false
        val sharedPrefs = getSharedPreferences(KEY_FIRST_VIEW_NAVIGATION, MODE_PRIVATE)
        val editor = sharedPrefs.edit()
            .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING, false)
            .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1, false)
            .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2, false)
            .putBoolean(KEY_P1_DONE_AS_NON_LOGIN, true)
        editor.apply()
        userSession.setFirstTimeUserOnboarding(false)
    }

    private fun handleCoachmarkUri(uri: Uri) {
        if (uri.lastPathSegment?.startsWith(URI_COACHMARK_DISABLE) == true) {
            isCoachmmarkShowAllowed = false
            val sharedPrefs = getSharedPreferences(KEY_FIRST_VIEW_NAVIGATION, MODE_PRIVATE)
            val editor = sharedPrefs.edit()
                .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING, false)
                .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1, false)
                .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2, false)
                .putBoolean(KEY_P1_DONE_AS_NON_LOGIN, true)
            editor.apply()
        }
        finish()
    }

    private fun initTranslator() {
        TranslatorManager.Companion.init(this.application, API_KEY_TRANSLATOR)
    }

    private fun clickAccessTokenBtn() = object : AccessTokenViewHolder.AccessTokenListener {
        override fun onClickAccessTokenBtn() {
            userSession?.accessToken?.apply {
                val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", this)
                clipboard.setPrimaryClip(clip)
            }
        }

        override fun getAccessToken(): String = userSession?.accessToken.orEmpty()
    }

    private fun clickResetOnBoarding() =
        object : ResetOnBoardingViewHolder.ResetOnBoardingListener {
            override fun onClickOnBoardingBtn() {
                userSession?.isFirstTimeUser = true
                val sharedPref = getSharedPreferences(CACHE_FREE_RETURN, MODE_PRIVATE)
                val editor = sharedPref.edit().clear()
                editor.apply()
                Toast.makeText(
                    this@DeveloperOptionActivity,
                    getString(R.string.reset_onboarding),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun homeAndNavigationListener() =
        object : HomeAndNavigationRevampSwitcherViewHolder.HomeAndNavigationRevampListener {
            override fun onClickSkipOnBoardingBtn() {
                userSession?.setFirstTimeUserOnboarding(false)
                Toast.makeText(
                    this@DeveloperOptionActivity,
                    getString(com.tokopedia.developer_options.R.string.skip_onboarding_user_session),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun selectUrlEnvironment() = object : UrlEnvironmentViewHolder.UrlEnvironmentListener {
        override fun onLogOutUserSession() {
            userSession?.logoutSession()
        }
    }

    private fun loginHelperListener() = object : LoginHelperListener {
        override fun routeToLoginHelperActivity() {
            val loginHelperIntent = RouteManager.getIntent(
                this@DeveloperOptionActivity,
                ApplinkConstInternalGlobal.LOGIN_HELPER
            )
            startActivityForResult(loginHelperIntent, LOGIN_HELPER_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOGIN_HELPER_REQUEST_CODE -> {
                this.setResult(Activity.RESULT_OK)
                this.finish()
            }
        }
    }

    private fun clearSessionIfNotLoggedIn(loggedIn: Boolean) {
        if (!loggedIn) loginSession.clear()
    }

    private fun showToaster(message: String) {
        KeyboardHandler.hideSoftKeyboard(this)
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun getBranchListener(): BranchLinkViewHolder.BranchListener {
        return object : BranchLinkViewHolder.BranchListener {
            override fun onClick(link: String) {
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val response = branchLinkUseCase(link)
                        val prettyJson = gson.toJson(response)
                        withContext(Dispatchers.Main) {
                            openResultBranch(prettyJson)
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@DeveloperOptionActivity, e.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun openResultBranch(result: String) {
        val dialog = Dialog(this, Dialog.Type.LONG_PROMINANCE)
        dialog.setTitle("Result branch")
        dialog.setDesc(result)
        dialog.setDescMovementMethod()
        dialog.setBtnCancel("Close")
        dialog.setOnCancelClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    class DeveloperOptionException(message: String?) : RuntimeException(message)

    override fun onSubmitDevOptsPassword(password: String, isAuto: Boolean) {
        val serverPassword = remoteConfig.getString(RemoteConfigKey.DEV_OPTS_AUTHORIZATION, "")
        if (password == serverPassword) {
            loginSession.setLoginSession(password)
            adapter.setValueIsAuthorized(true)
            adapter.initializeList()
            adapter.setDefaultItem()
            showToaster("You are authorized !!")
        } else {
            if (!isAuto) {
                showToaster("Wrong password !! Please ask Android representative")
            }
        }
    }
}
