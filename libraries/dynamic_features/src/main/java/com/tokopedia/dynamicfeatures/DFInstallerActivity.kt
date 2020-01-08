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
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.play.core.splitinstall.*
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.dynamicfeatures.track.DFTracking.Companion.trackDownloadDF
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
class DFInstallerActivity : BaseSimpleActivity(), CoroutineScope {

    private lateinit var manager: SplitInstallManager

    private lateinit var progressBar: ImageView
    private lateinit var buttonDownload: UnifyButton
    private lateinit var imageView: ImageView
    private var isAutoDownload = false
    private var sessionId: Int? = null

    private lateinit var moduleName: String
    private lateinit var moduleNameTranslated: String
    private lateinit var applink: String
    private var imageUrl: String? = null
    private var moduleSize = 0L
    private var usableSpaceBeforeDownload = 0L

    private var errorList: MutableList<String> = mutableListOf()
    private var downloadTimes = 0
    private var successInstall = false
    private var job = Job()

    companion object {
        private const val EXTRA_NAME = "dfname"
        private const val EXTRA_APPLINK = "dfapplink"
        private const val EXTRA_AUTO = "dfauto"
        private const val EXTRA_IMAGE = "dfimage"
        private const val defaultImageUrl = "https://ecs7.tokopedia.net/img/android/empty_profile/drawable-xxxhdpi/product_image_48_x_48.png"
        private const val CONFIRMATION_REQUEST_CODE = 1
        private const val SETTING_REQUEST_CODE = 2
        private const val ONE_KB = 1024
        const val TAG_LOG= "DFM"
        const val PLAY_SRV_OOD= "play_ood" //tag for play service ouf of date
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
        }

        super.onCreate(savedInstanceState)
        manager = SplitInstallManagerFactory.create(this)
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
        } else {
            if (isAutoDownload) {
                downloadFeature()
            } else {
                hideProgress()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CONFIRMATION_REQUEST_CODE) {
            // Handle the user's decision. For example, if the user selects "Cancel",
            // you may want to disable certain functionality that depends on the module.
            if (resultCode == Activity.RESULT_CANCELED) {
                hideProgress()
            }
        } else if (requestCode == SETTING_REQUEST_CODE) {
            downloadFeature()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initializeViews() {
        progressBar = findViewById(R.id.iv_loading)
        ImageHandler.loadGif(progressBar, R.drawable.ic_loading_indeterminate, -1)

        imageView = findViewById(R.id.image)

        buttonDownload = findViewById(R.id.button_download)

        buttonDownload.setOnClickListener {
            downloadFeature()
        }
        title_txt.setText(String.format(getString(R.string.feature_download_title), moduleNameTranslated))
        subtitle_txt.setText(String.format(getString(R.string.feature_download_subtitle), moduleNameTranslated))
    }

    private fun loadAndLaunchModule(name: String) {
        launch {
            moduleSize = 0
            displayProgress()

            // Skip loading if the module already is installed. Perform success action directly.
            if (manager.installedModules.contains(name)) {
                onSuccessfulLoad(name, launch = true)
                return@launch
            }

            // Create request to install a feature module by name.
            val request = SplitInstallRequest.newBuilder()
                .addModule(name)
                .build()

            if (usableSpaceBeforeDownload == 0L) {
                usableSpaceBeforeDownload = withContext(Dispatchers.IO) {
                    DFInstallerLogUtil.getFreeSpaceBytes(applicationContext)
                }
            }

            // Load and install the requested feature module.
            manager.startInstall(request).addOnSuccessListener {
                if (it == 0) {
                    onSuccessfulLoad(moduleName, true)
                } else {
                    sessionId = it
                }
            }.addOnFailureListener { exception ->
                val errorCode = (exception as? SplitInstallException)?.errorCode
                sessionId = null
                hideProgress()
                val message = getString(R.string.error_for_module_x, moduleName)
                showFailedMessage(message, errorCode?.toString() ?: exception.toString())
            }
        }
    }

    private fun onSuccessfulLoad(moduleName: String, launch: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SplitInstallHelper.updateAppInfo(this)
        }
        successInstall = manager.installedModules.contains(moduleName)
        progressBar.visibility = View.INVISIBLE
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
        val multiInstall = state.moduleNames().size > 1

        val names = state.moduleNames().joinToString(" - ")

        when (state.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> {
                //  In order to see this, the application has to be uploaded to the Play Store.
                displayLoadingState(state, getString(R.string.downloading_x, moduleNameTranslated))
                if (moduleSize == 0L) {
                    moduleSize = state.totalBytesToDownload()
                }
            }
            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                /*
                  This may occur when attempting to download a sufficiently large module.

                  In order to see this, the application has to be uploaded to the Play Store.
                  Then features can be requested until the confirmation path is triggered.
                 */
                manager.startConfirmationDialogForResult(state, this, CONFIRMATION_REQUEST_CODE)
            }
            SplitInstallSessionStatus.INSTALLED -> {
                onSuccessfulLoad(names, launch = !multiInstall)
            }

            SplitInstallSessionStatus.INSTALLING -> {
                updateProgressMessage(
                    getString(R.string.installing_x, moduleNameTranslated)
                )
            }
            SplitInstallSessionStatus.FAILED -> {
                val message = getString(R.string.error_for_module, state.moduleNames(), state.errorCode())
                showFailedMessage(message, state.errorCode().toString())
                hideProgress()
            }
        }
    }

    private fun showFailedMessage(message: String, errorCode: String = "") {
        errorList.add(errorCode)
        button_download.visibility = View.VISIBLE
        if (SplitInstallErrorCode.INSUFFICIENT_STORAGE.toString() == errorCode) {
            image.setImageResource(R.drawable.ic_ill_insuficient_memory)
            title_txt.setText(getString(R.string.download_error_insuficient_memory_title))
            subtitle_txt.setText(String.format(getString(R.string.download_error_insuficient_memory_subtitle), (moduleSize.toFloat() / ONE_KB)))
            button_download.setText(getString(R.string.goto_seting))
            button_download.setOnClickListener {
                startActivityForResult(Intent(android.provider.Settings.ACTION_SETTINGS), SETTING_REQUEST_CODE)
            }
        } else if (SplitInstallErrorCode.NETWORK_ERROR.toString() == errorCode) {
            image.setImageResource(R.drawable.ic_ill_no_connection)
            title_txt.setText(getString(R.string.download_error_connection_title))
            subtitle_txt.setText(getString(R.string.download_error_connection_subtitle))
            button_download.setText(getString(R.string.try_again))
            button_download.setOnClickListener {
                downloadFeature()
            }
        } else if (SplitInstallErrorCode.MODULE_UNAVAILABLE.toString() == errorCode) {
            image.setImageResource(R.drawable.ic_ill_module_unavailable)
            title_txt.setText(getString(R.string.download_error_module_unavailable_title))
            subtitle_txt.setText(getString(R.string.download_error_module_unavailable_subtitle))
            button_download.setText(getString(R.string.goto_playstore))
            button_download.setOnClickListener {
                gotoPlayStore()
            }
        } else {
            val isPlayServiceUptoDate = checkPlayServiceUptoDate()
            if (isPlayServiceUptoDate) {
                showGeneralError()
            } else {
                // show log play service is not up-to-date
                val lastIndex = errorList.size - 1
                val lastItem = errorList[lastIndex]
                errorList[lastIndex] = "$lastItem $PLAY_SRV_OOD"
                image.setImageResource(R.drawable.ic_ill_general_error)
                title_txt.setText(getString(R.string.download_error_playservice_title))
                subtitle_txt.setText(getString(R.string.download_error_playservice_subtitle))
                button_download.setOnClickListener {
                    val hasBeenUpdated = checkPlayServiceUptoDate()
                    if (hasBeenUpdated) {
                        downloadFeature()
                    }
                }
            }
        }
    }

    fun showGeneralError(){
        image.setImageResource(R.drawable.ic_ill_general_error)
        title_txt.setText(getString(R.string.download_error_general_title))
        subtitle_txt.setText(getString(R.string.download_error_general_subtitle))
        button_download.setOnClickListener {
            downloadFeature()
        }
    }

    private fun checkPlayServiceUptoDate():Boolean{
        // this code should be on main thread.
        val googleAPI = GoogleApiAvailability.getInstance()
        val result = googleAPI.isGooglePlayServicesAvailable(this)
        return if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, 9000).show()
            }
            false
        } else {
            true
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
        title_txt.setText(getString(R.string.dowload_on_process))
        subtitle_txt.setText(getString(R.string.wording_download_waiting))
        image.setImageResource(R.drawable.ic_ill_downloading)
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
        val progressText = String.format("%.2f KB / %.2f KB",
            (bytesDownloaded.toFloat() / ONE_KB), totalBytesToDowload.toFloat() / ONE_KB)
        Log.i(TAG_LOG, progressText)
        button_download.visibility = View.INVISIBLE
    }

    private fun displayProgress() {
        progressBar.visibility = View.VISIBLE
        buttonDownload.visibility = View.INVISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.INVISIBLE
        buttonDownload.visibility = View.VISIBLE
    }

    override fun onResume() {
        // Listener can be registered even without directly triggering a download.
        manager.registerListener(listener)
        super.onResume()
    }

    override fun onPause() {
        // Make sure to dispose of the listener once it's no longer needed.
        manager.unregisterListener(listener)
        super.onPause()
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        val applicationContext = this.applicationContext
        trackDownloadDF(listOf(moduleName),
            errorList,
            false)
        DFInstallerLogUtil.logStatus(applicationContext, TAG_LOG,
            moduleName, usableSpaceBeforeDownload, moduleSize,
            errorList, downloadTimes, successInstall)
        job.cancel()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + CoroutineExceptionHandler { _, _ -> }

}