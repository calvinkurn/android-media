package com.tokopedia.dynamicfeatures

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
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
import com.tokopedia.dynamicfeatures.utils.StorageUtils
import com.tokopedia.dynamicfeatures.utils.Utils
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.activity_dynamic_feature_installer.*
import kotlinx.coroutines.*
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

    private lateinit var moduleName: String
    private lateinit var moduleNameTranslated: String
    private lateinit var applink: String
    private var imageUrl: String? = null
    private var fallbackUrl: String = ""
    private var moduleSize = 0L
    private var freeInternalStorageBeforeDownload = 0L

    private var errorList: MutableList<String> = mutableListOf()
    private var downloadTimes = 0
    private var successInstall = false
    private var job = Job()

    private lateinit var dfConfig: DFConfig

    companion object {
        private const val EXTRA_NAME = "dfname"
        private const val EXTRA_APPLINK = "dfapplink"
        private const val EXTRA_AUTO = "dfauto"
        private const val EXTRA_IMAGE = "dfimage"
        private const val EXTRA_FALLBACK_WEB = "dffallbackurl"
        private const val defaultImageUrl = "https://ecs7.tokopedia.net/img/android/empty_profile/drawable-xxxhdpi/product_image_48_x_48.png"
        private const val CONFIRMATION_REQUEST_CODE = 1
        private const val SETTING_REQUEST_CODE = 2
        const val TAG_LOG = "Page"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            moduleName = uri.lastPathSegment ?: ""
            moduleNameTranslated = uri.getQueryParameter(EXTRA_NAME) ?: ""
            applink = Uri.decode(uri.getQueryParameter(EXTRA_APPLINK)) ?: ""
            isAutoDownload = uri.getQueryParameter(EXTRA_AUTO)?.toBoolean() ?: true
            imageUrl = uri.getQueryParameter(EXTRA_IMAGE)
            if (imageUrl.isNullOrEmpty()) {
                imageUrl = defaultImageUrl
            }
            fallbackUrl = uri.getQueryParameter(EXTRA_FALLBACK_WEB) ?: ""
        }

        super.onCreate(savedInstanceState)
        dfConfig = DFRemoteConfig.getConfig(this)
        manager = DFInstaller.getManager(this) ?: return
        if (moduleName.isEmpty()) {
            finish()
            return
        }
        if (moduleNameTranslated.isNotEmpty()) {
            setTitle(getString(R.string.installing_x, moduleNameTranslated))
        }
        setContentView(R.layout.activity_dynamic_feature_installer)
        initializeViews()
        if (manager.installedModules.contains(moduleName)) {
            onSuccessfulLoad(moduleName, launch = true)
        } else if (isAutoDownload) {
            downloadFeature()
        } else {
            showOnBoardingView()
        }
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

    private fun loadAndLaunchModule(name: String) {
        if (dfConfig.allowRunningServiceFromActivity()) {
            DFInstaller.installOnBackground(this, listOf(name), this::class.java.simpleName.toString())
            DFInstaller.attachView(this)
        } else {
            launch {
                moduleSize = 0

                // Skip loading if the module already is installed. Perform success action directly.
                if (manager.installedModules.contains(name)) {
                    onSuccessfulLoad(name, launch = true)
                    return@launch
                }

                // Create request to install a feature module by name.
                val request = SplitInstallRequest.newBuilder()
                    .addModule(name)
                    .build()

                if (freeInternalStorageBeforeDownload == 0L) {
                    freeInternalStorageBeforeDownload = withContext(Dispatchers.IO) {
                        StorageUtils.getFreeSpaceBytes(applicationContext)
                    }
                }

                // Load and install the requested feature module.
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
    }

    private fun onSuccessfulLoad(moduleName: String, launch: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SplitInstallHelper.updateAppInfo(this)
        }
        successInstall = manager.installedModules.contains(moduleName)
        progressGroup.visibility = View.INVISIBLE
        if (launch && successInstall) {
            launchAndForwardIntent(applink)
        }
        this.finish()
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
            getString(R.string.start_download)) {
            downloadFeature()
        }
    }

    private fun showFailedMessage(errorCode: String = "") {
        val errorCodeTemp = ErrorUtils.getValidatedErrorCode(this, errorCode, freeInternalStorageBeforeDownload)
        errorList.add(errorCodeTemp)
        var ctaAction: (() -> Unit)? = null
        if (fallbackUrl.isNotEmpty()) {
            val intent = RouteManager.getIntent(this, ApplinkConstInternalGlobal.WEBVIEW, fallbackUrl)
            intent?.let { it ->
                ctaAction = { ->
                    startActivity(it)
                }
            }
        }
        when (errorCodeTemp) {
            ErrorConstant.ERROR_PLAY_SERVICE_NOT_CONNECTED -> {
                updateInformationView(R.drawable.unify_globalerrors_500,
                    getString(R.string.download_error_playservice_title),
                    getString(R.string.download_error_playservice_subtitle),
                    getString(R.string.start_download), {
                    if (Utils.isPlayServiceConnected(this)) {
                        downloadFeature()
                    } else {
                        Utils.showPlayServiceErrorDialog(this)
                    }
                }, getString(R.string.continue_without_install),
                    ctaAction)
                Utils.showPlayServiceErrorDialog(this)
            }
            ErrorConstant.ERROR_PLAY_STORE_NOT_AVAILABLE -> updateInformationView(R.drawable.unify_globalerrors_500,
                getString(R.string.download_error_play_store_title),
                getString(R.string.download_error_play_store_subtitle),
                getString(R.string.goto_playstore),
                ::gotoPlayStore,
                getString(R.string.continue_without_install),
                ctaAction
            )
            ErrorConstant.ERROR_INVALID_INSUFFICIENT_STORAGE -> updateInformationView(R.drawable.unify_globalerrors_500,
                getString(R.string.download_error_os_and_play_store_title),
                getString(R.string.download_error_os_and_play_store_subtitle),
                getString(R.string.goto_seting), {
                startActivityForResult(Intent(android.provider.Settings.ACTION_SETTINGS), SETTING_REQUEST_CODE)
            }, getString(R.string.continue_without_delete_storage),
                ctaAction)
            SplitInstallErrorCode.INSUFFICIENT_STORAGE.toString() -> updateInformationView(R.drawable.ic_ill_insuficient_memory,
                getString(R.string.download_error_insuficient_storage_title),
                getString(R.string.download_error_insuficient_storage_subtitle),
                getString(R.string.goto_seting), {
                startActivityForResult(Intent(android.provider.Settings.ACTION_SETTINGS), SETTING_REQUEST_CODE)
            }, getString(R.string.continue_without_install),
                ctaAction)
            SplitInstallErrorCode.NETWORK_ERROR.toString() -> updateInformationView(R.drawable.unify_globalerrors_connection,
                getString(R.string.download_error_connection_title),
                getString(R.string.download_error_connection_subtitle),
                getString(R.string.df_installer_try_again), ::downloadFeature,
                getString(R.string.continue_without_install),
                ctaAction)
            SplitInstallErrorCode.MODULE_UNAVAILABLE.toString() -> updateInformationView(R.drawable.ic_ill_module_unavailable,
                getString(R.string.download_error_module_unavailable_title),
                getString(R.string.download_error_module_unavailable_subtitle),
                getString(R.string.goto_playstore),
                ::gotoPlayStore,
                getString(R.string.continue_without_install),
                ctaAction)
            else -> updateInformationView(R.drawable.unify_globalerrors_500,
                getString(R.string.download_error_general_title),
                getString(R.string.download_error_general_subtitle),
                getString(R.string.df_installer_try_again),
                ::downloadFeature,
                getString(R.string.continue_without_install),
                ctaAction)
        }
    }

    private fun updateInformationView(imageRes: Int, title: String, subTitle: String,
                                      buttonText: String = "",
                                      onDownloadButtonClicked: () -> (Unit) = {},
                                      ctaText: String = "",
                                      onCtaClicked: (() -> (Unit))? = null) {
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
        if (onCtaClicked != null && ctaText.isNotEmpty()) {
            button_cta.setOnClickListener {
                onCtaClicked.invoke()
            }
            button_cta.text = ctaText
            button_cta.visibility = View.VISIBLE
        } else {
            button_cta.visibility = View.GONE
        }
    }

    private fun gotoPlayStore() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (anfe: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
        }
    }

    private fun downloadFeature() {
        updateInformationView(R.drawable.ic_ill_downloading, getString(R.string.dowload_on_process), getString(R.string.wording_download_waiting))
        progressGroup.visibility = View.VISIBLE
        downloadTimes++
        loadAndLaunchModule(moduleName)
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
        Log.i(TAG_LOG, progressText)
        progressTextPercent.text = String.format("%.0f%%", bytesDownloaded.toFloat() * 100 / totalBytesToDowload)
        button_download.visibility = View.INVISIBLE
    }

    override fun onResume() {
        // works for non singleton service
        // Listener can be registered even without directly triggering a download.
        if (!dfConfig.allowRunningServiceFromActivity()) {
            manager.registerListener(listener)
        }
        super.onResume()
    }

    override fun onPause() {
        // works for non singleton service
        // Make sure to dispose of the listener once it's no longer needed.
        if (!dfConfig.allowRunningServiceFromActivity()) {
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
        if (!dfConfig.allowRunningServiceFromActivity()) {
            job.cancel()
        }
        trackDownloadDF(listOf(moduleName),
            errorList,
            false)
        DFInstallerLogUtil.logStatus(applicationContext, TAG_LOG,
            moduleName, freeInternalStorageBeforeDownload, moduleSize,
            errorList, downloadTimes, successInstall)
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
    }

    override fun onRequireUserConfirmation(state: SplitInstallSessionState) {
        manager.startConfirmationDialogForResult(state, this, CONFIRMATION_REQUEST_CODE)
    }

    override fun onInstalled() {
        onSuccessfulLoad(moduleName, true)
    }

    override fun onInstalling(state: SplitInstallSessionState) {
        updateProgressMessage(getString(R.string.installing_x, moduleNameTranslated))
    }

    override fun onFailed(errorString: String) {
        showFailedMessage(errorString)
    }
}