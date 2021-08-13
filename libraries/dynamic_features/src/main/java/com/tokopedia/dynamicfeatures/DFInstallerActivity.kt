package com.tokopedia.dynamicfeatures

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.play.core.splitinstall.*
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dynamicfeatures.config.DFConfig
import com.tokopedia.dynamicfeatures.config.DFRemoteConfig
import com.tokopedia.dynamicfeatures.constant.CommonConstant
import com.tokopedia.dynamicfeatures.constant.ErrorConstant
import com.tokopedia.dynamicfeatures.track.DFTracking.Companion.trackDownloadDF
import com.tokopedia.dynamicfeatures.utils.DFInstallerLogUtil
import com.tokopedia.dynamicfeatures.utils.ErrorUtils
import com.tokopedia.dynamicfeatures.utils.PlayServiceUtils
import com.tokopedia.dynamicfeatures.utils.StorageUtils
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.activity_dynamic_feature_installer.*
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext


/**
 * Activity that handles for installing new dynamic feature module
 *
 * APPLINK: DYNAMIC_FEATURE_INSTALL
 *
 * Example input:
 * module   : "shop_settings_sellerapp"
 * page     : applink of ShopNotesActivity
 * name     : "Pengaturan Toko" (this is for the title - user language string)
 * This activity will install the "shop_settings_sellerapp" module with progress bar,
 * and after the module is successfully installed, it will bring the user to ShopNotesActivity
 */
class DFInstallerActivity : BaseSimpleActivity(), CoroutineScope, DFInstaller.DFInstallerView {

    private lateinit var manager: SplitInstallManager

    private lateinit var progressBar: ProgressBar
    private lateinit var progressTextPercent: TextView
    private lateinit var buttonDownload: UnifyButton
    private lateinit var imageView: ImageView
    private lateinit var progressGroup: View
    private var isAutoDownload = false
    private var sessionId: Int? = null

    private var moduleName: String = ""
    private var moduleNameTranslated: String = ""
    private var deeplink: String = ""
    private var fallbackUrl: String = ""
    private var moduleSize = 0L
    private var freeInternalStorageBeforeDownload = 0L
    private var startDownloadTimeStamp = 0L
    private var endDownloadTimeStamp = 0L
    private var startDownloadPercentage = -1f

    private var errorList: MutableList<String> = mutableListOf()
    private var downloadTimes = 0
    private var successInstall = false

    private var allowRunningServiceFromActivity: Boolean = false
    private var cancelDownloadBeforeInstallInPage: Boolean = false

    private var job = Job()
    private var timerJob: Job = Job()

    private lateinit var dfConfig: DFConfig

    companion object {
        private const val EXTRA_NAME = "dfname"
        private const val EXTRA_DEEPLINK = "dfapplink"
        private const val EXTRA_FALLBACK_WEB = "dffallbackurl"
        private const val CONFIRMATION_REQUEST_CODE = 1
        private const val SETTING_REQUEST_CODE = 2
        const val DOWNLOAD_MODE_PAGE = "Page"
        const val TIMEOUT_ERROR_MESSAGE = "timeout"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            moduleName = uri.lastPathSegment ?: ""
            moduleNameTranslated = uri.getQueryParameter(EXTRA_NAME) ?: ""
            deeplink = Uri.decode(uri.getQueryParameter(EXTRA_DEEPLINK)) ?: ""
            isAutoDownload = true
            fallbackUrl = uri.getQueryParameter(EXTRA_FALLBACK_WEB) ?: ""
        }
        super.onCreate(savedInstanceState)
        dfConfig = DFRemoteConfig.getConfig(this)
        manager = DFInstaller.getManager(this) ?: return
        if (moduleName.isEmpty()) {
            finish()
            return
        }
        allowRunningServiceFromActivity = dfConfig.allowRunningServiceFromActivity(moduleName)
        cancelDownloadBeforeInstallInPage = dfConfig.cancelDownloadBeforeInstallInPage

        if (moduleNameTranslated.isNotEmpty()) {
            title = getString(R.string.installing_x, moduleNameTranslated)
        }
        setContentView(R.layout.activity_dynamic_feature_installer)
        initializeViews()
        if (DFInstaller.isInstalled(this, moduleName)) {
            onSuccessfulLoad(moduleName, launch = true)
        } else if (isAutoDownload) {
            downloadFeature()
            logDownloadPage();
        } else {
            showOnBoardingView()
        }
    }

    private fun logDownloadPage() {
        ServerLogger.log(Priority.P1, "DFM_DOWNLOAD_PAGE",
                mapOf("type" to "download",
                        "mod_name" to moduleName,
                        "dl_service" to allowRunningServiceFromActivity.toString(),
                        "deeplink" to deeplink,
                        "fallback_url" to fallbackUrl
                ))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CONFIRMATION_REQUEST_CODE) {
            // Handle the user's decision. For example, if the user selects "Cancel",
            // you may want to disable certain functionality that depends on the module.
            if (resultCode == Activity.RESULT_CANCELED) {
                showOnBoardingView()
            }
        } else if (requestCode == SETTING_REQUEST_CODE) {
            downloadFeature()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initializeViews() {
        progressBar = findViewById(R.id.progress_bar)
        progressTextPercent = findViewById(R.id.progress_text_percent)
        imageView = findViewById(R.id.image)
        buttonDownload = findViewById(R.id.button_download)
        progressGroup = findViewById(R.id.progress_group)

        progressBar.progressDrawable.setColorFilter(
                ContextCompat.getColor(this, R.color.tkpd_main_green),
                android.graphics.PorterDuff.Mode.MULTIPLY)
    }

    private fun loadAndLaunchModule(moduleName: String) {
        // Skip loading if the module already is installed. Perform success action directly.
        if (DFInstaller.isInstalled(this@DFInstallerActivity, moduleName)) {
            onSuccessfulLoad(moduleName, launch = true)
            return
        }
        if (allowRunningServiceFromActivity) {
            DFInstaller.installOnBackground(this, listOf(moduleName), this::class.java.simpleName.toString())
            DFInstaller.attachView(this)
            DFInstaller.deeplink = deeplink
            DFInstaller.fallbackUrl = fallbackUrl
        } else {
            launch {
                resetDFInfo()

                // Create request to install a feature module by name.
                val request = SplitInstallRequest.newBuilder()
                        .addModule(moduleName)
                        .build()

                if (freeInternalStorageBeforeDownload == 0L) {
                    freeInternalStorageBeforeDownload = withContext(Dispatchers.IO) {
                        StorageUtils.getFreeSpaceBytes(applicationContext)
                    }
                }

                // Load and install the requested feature module.
                startDownloadTimeStamp = System.currentTimeMillis()
                manager.startInstall(request).addOnSuccessListener {
                    if (it == 0) {
                        onInstalled()
                    } else {
                        sessionId = it
                    }
                }.addOnFailureListener { exception ->
                    val errorCode = (exception as? SplitInstallException)?.errorCode
                    sessionId = null
                    onFailed(errorCode?.toString() ?: exception.toString())
                }
            }
        }
        addTimeout()
    }

    /**
     * mechanism if the download Dynamic Feature is Too long, it will pop up to launch without Install
     */
    private fun addTimeout() {
        val timeout = dfConfig.timeout
        if (timeout <= 0) {
            return
        }
        timerJob.cancel()
        timerJob = launch(Dispatchers.IO) {
            delay(TimeUnit.SECONDS.toMillis(timeout))
            withContext(Dispatchers.Main) {
                cancelPreviousDownload()
                //show timeoutError
                onFailed(TIMEOUT_ERROR_MESSAGE + "after" + timeout)
            }
        }
    }

    private fun resetDFInfo() {
        moduleSize = 0
        startDownloadPercentage = -1f
    }

    private fun onSuccessfulLoad(moduleName: String, launch: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SplitInstallHelper.updateAppInfo(this)
        }
        successInstall = DFInstaller.isInstalled(this, moduleName)
        progressGroup.visibility = View.INVISIBLE
        if (launch && successInstall) {
            launchAndForwardIntent(deeplink)
        }
        finish()
    }

    private fun launchAndForwardIntent(applink: String) {
        RouteManager.getIntentNoFallback(this, applink)?.let {
            it.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
            intent.extras?.let { passBundle ->
                it.putExtras(passBundle)
            }
            startActivity(it)
        }
    }

    /** Listener used to handle changes in state for install requests. */
    private val listener = SplitInstallStateUpdatedListener { state ->
        if (state.sessionId() != sessionId) {
            return@SplitInstallStateUpdatedListener
        }
        when (state.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> {
                onDownload(state)
            }
            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                /*
                  This may occur when attempting to download a sufficiently large module.

                  In order to see this, the application has to be uploaded to the Play Store.
                  Then features can be requested until the confirmation path is triggered.
                 */
                onRequireUserConfirmation(state)
            }
            SplitInstallSessionStatus.INSTALLED -> {
                onInstalled()
            }

            SplitInstallSessionStatus.INSTALLING -> {
                onInstalling(state)
            }
            SplitInstallSessionStatus.FAILED -> {
                onFailed(state.errorCode().toString())
            }
        }
    }

    private fun showOnBoardingView() {
        updateInformationView(R.drawable.ic_ill_onboarding,
                String.format(getString(R.string.feature_download_title), moduleNameTranslated),
                String.format(getString(R.string.feature_download_subtitle), moduleNameTranslated),
                getString(R.string.start_download),
                {
                    downloadFeature()
                    logDownloadPage()
                }
        )
    }

    private fun showFailedMessage(errorCode: String = "") {
        val errorCodeTemp = ErrorUtils.getValidatedErrorCode(this, errorCode, freeInternalStorageBeforeDownload)
        errorList.add(errorCodeTemp)
        when (errorCodeTemp) {
            SplitInstallErrorCode.PLAY_STORE_NOT_FOUND.toString() -> {
                updateInformationView(com.tokopedia.globalerror.R.drawable.unify_globalerrors_500,
                        getString(R.string.download_error_play_store_title),
                        getString(R.string.download_error_play_store_subtitle),
                        getString(R.string.goto_playstore),
                        { PlayServiceUtils.gotoPlayStore(this) },
                        getString(R.string.continue_without_install))
            }
            ErrorConstant.ERROR_INVALID_INSUFFICIENT_STORAGE -> updateInformationView(com.tokopedia.globalerror.R.drawable.unify_globalerrors_500,
                    getString(R.string.download_error_os_and_play_store_title),
                    getString(R.string.download_error_os_and_play_store_subtitle),
                    getString(R.string.goto_seting),
                    { startActivityForResult(Intent(android.provider.Settings.ACTION_SETTINGS), SETTING_REQUEST_CODE) },
                    getString(R.string.continue_without_delete_storage))
            SplitInstallErrorCode.INSUFFICIENT_STORAGE.toString() -> updateInformationView(R.drawable.ic_ill_insuficient_memory,
                    getString(R.string.download_error_insuficient_storage_title),
                    getString(R.string.download_error_insuficient_storage_subtitle),
                    getString(R.string.goto_seting),
                    { startActivityForResult(Intent(android.provider.Settings.ACTION_SETTINGS), SETTING_REQUEST_CODE) },
                    getString(R.string.continue_without_install))
            SplitInstallErrorCode.NETWORK_ERROR.toString() -> updateInformationView(com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection,
                    getString(R.string.download_error_connection_title),
                    getString(R.string.download_error_connection_subtitle),
                    getString(R.string.df_installer_try_again),
                    ::downloadFeature,
                    getString(R.string.continue_without_install))
            /* SplitInstallErrorCode.APP_NOT_OWNED */
            "-15",
            SplitInstallErrorCode.MODULE_UNAVAILABLE.toString() -> updateInformationView(R.drawable.ic_ill_module_unavailable,
                    getString(R.string.download_error_module_unavailable_title),
                    getString(R.string.download_error_module_unavailable_subtitle),
                    getString(R.string.goto_playstore),
                    { PlayServiceUtils.gotoPlayStore(this) },
                    getString(R.string.continue_without_install))
            else -> updateInformationView(com.tokopedia.globalerror.R.drawable.unify_globalerrors_500,
                    getString(R.string.download_error_general_title),
                    getString(R.string.download_error_general_subtitle),
                    getString(R.string.df_installer_try_again),
                    ::downloadFeature,
                    getString(R.string.continue_without_install))
        }
    }

    private fun updateInformationView(imageRes: Int, title: String, subTitle: String,
                                      buttonText: String = "",
                                      onDownloadButtonClicked: () -> (Unit) = {},
                                      ctaText: String = "") {
        image.setImageResource(imageRes)
        progressGroup.visibility = View.INVISIBLE
        title_txt.text = title
        subtitle_txt.text = subTitle
        if (buttonText.isNotEmpty()) {
            button_download.text = buttonText
            button_download.setOnClickListener {
                onDownloadButtonClicked()
            }
            button_download.visibility = View.VISIBLE
        } else {
            button_download.visibility = View.INVISIBLE
        }
        if (fallbackUrl.isNotEmpty() && ctaText.isNotEmpty()) {
            button_cta.visibility = View.VISIBLE
            button_cta.text = ctaText
            button_cta.setOnClickListener { _ ->
                RouteManager.getIntent(this, ApplinkConstInternalGlobal.WEBVIEW, fallbackUrl)?.let { it ->
                    ServerLogger.log(Priority.P1, "DFM_FALLBACK",
                            mapOf("type" to "click",
                                    "mod_name" to moduleName,
                                    "deeplink" to deeplink,
                                    "url" to fallbackUrl
                            ))
                    startActivity(it)
                    finish()
                }
            }
        } else {
            button_cta.visibility = View.GONE
        }
    }

    private fun downloadFeature() {
        if (cancelDownloadBeforeInstallInPage) {
            cancelPreviousDownload()
        }
        updateInformationView(R.drawable.ic_ill_downloading, getString(R.string.dowload_on_process), getString(R.string.wording_download_waiting))
        progressGroup.visibility = View.VISIBLE
        downloadTimes++
        loadAndLaunchModule(moduleName)
    }

    private fun cancelPreviousDownload() {
        try {
            if (allowRunningServiceFromActivity) {
                DFInstaller.stopInstall(applicationContext)
            } else {
                sessionId?.let {
                    manager.cancelInstall(it)
                }
                sessionId = null
            }
        } catch (ignored: Exception) {

        }
    }

    private fun updateProgressMessage(message: String) {
        //no-op, no progress shown in UI
    }

    /** Display a loading state to the user. */
    @SuppressLint("LogNotTimber")
    private fun displayLoadingState(state: SplitInstallSessionState, message: String) {
        val totalBytesToDowload = state.totalBytesToDownload().toInt()
        val bytesDownloaded = state.bytesDownloaded().toInt()
        progressBar.max = totalBytesToDowload
        progressBar.progress = bytesDownloaded
        val progressText = String.format("%.2f KB / %.2f KB",
                (bytesDownloaded.toFloat() / CommonConstant.ONE_KB), totalBytesToDowload.toFloat() / CommonConstant.ONE_KB)
        Log.i(DOWNLOAD_MODE_PAGE, progressText)
        val downloadProgress = bytesDownloaded.toFloat() * 100 / totalBytesToDowload
        progressTextPercent.text = String.format("%.0f%%", downloadProgress)
        if (startDownloadPercentage < 0) {
            startDownloadPercentage = downloadProgress
        }
        button_download.visibility = View.INVISIBLE
    }

    override fun onResume() {
        // works for non singleton service
        // Listener can be registered even without directly triggering a download.
        if (!allowRunningServiceFromActivity) {
            manager.registerListener(listener)
        }
        super.onResume()
    }

    override fun onPause() {
        // works for non singleton service
        // Make sure to dispose of the listener once it's no longer needed.
        if (!allowRunningServiceFromActivity) {
            manager.unregisterListener(listener)
        }
        super.onPause()
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        val applicationContext = this.applicationContext
        if (!allowRunningServiceFromActivity) {
            trackDownloadDF(listOf(moduleName),
                    errorList,
                    false)
            DFInstallerLogUtil.logStatus(applicationContext, CommonConstant.DFM_TAG, DOWNLOAD_MODE_PAGE,
                    moduleName, freeInternalStorageBeforeDownload, moduleSize,
                    errorList, downloadTimes, successInstall,
                    startDownloadTimeStamp, endDownloadTimeStamp,
                    startDownloadPercentage,
                    allowRunningServiceFromActivity, deeplink, fallbackUrl)
            job.cancel()
        }
        timerJob.cancel()
        DFInstaller.clearRef()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + CoroutineExceptionHandler { _, _ -> }

    override fun onDownload(state: SplitInstallSessionState) {
        //  In order to see this, the application has to be uploaded to the Play Store.
        displayLoadingState(state, getString(R.string.downloading_x, moduleNameTranslated))
        if (moduleSize == 0L) {
            moduleSize = state.totalBytesToDownload()
        }
        addTimeout()
    }

    override fun onRequireUserConfirmation(state: SplitInstallSessionState) {
        manager.startConfirmationDialogForResult(state, this, CONFIRMATION_REQUEST_CODE)
    }

    override fun onInstalled() {
        timerJob.cancel()
        endDownloadTimeStamp = System.currentTimeMillis()
        onSuccessfulLoad(moduleName, true)
    }

    override fun onInstalling(state: SplitInstallSessionState) {
        updateProgressMessage(getString(R.string.installing_x, moduleNameTranslated))
    }

    override fun onFailed(errorString: String) {
        timerJob.cancel()
        endDownloadTimeStamp = System.currentTimeMillis()
        showFailedMessage(errorString)
    }

    override fun getModuleNameView(): String {
        return moduleName
    }

    override fun getDeeplink(): String {
        return deeplink
    }

    override fun getFallbackUrl(): String {
        return fallbackUrl
    }
}