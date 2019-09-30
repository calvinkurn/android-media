package com.tokopedia.dynamicfeatures

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.play.core.splitinstall.*
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.unifycomponents.Toaster
import java.io.File
import timber.log.Timber

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
class DFInstallerActivity : BaseSimpleActivity() {

    private lateinit var manager: SplitInstallManager

    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var progressTextPercent: TextView
    private lateinit var buttonDownload: Button
    private lateinit var imageView: ImageView
    private lateinit var progressGroup: View
    private var isAutoDownload = false
    private var sessionId: Int? = null

    private lateinit var moduleName: String
    private lateinit var moduleNameTranslated: String
    private lateinit var applink: String
    private var imageUrl: String? = null
    private var initialDownloading = false
    private var totalFreeSpaceSizeInMB = "-"

    companion object {
        private const val EXTRA_NAME = "dfname"
        private const val EXTRA_APPLINK = "dfapplink"
        private const val EXTRA_AUTO = "dfauto"
        private const val EXTRA_IMAGE = "dfimage"
        private const val defaultImageUrl = "https://ecs7.tokopedia.net/img/android/empty_profile/drawable-xxxhdpi/product_image_48_x_48.png"
        private const val CONFIRMATION_REQUEST_CODE = 1
        private const val ONE_KB = 1024
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
        totalFreeSpaceSizeInMB = getTotalFreeSpaceSizeInMB()
        if (manager.installedModules.contains(moduleName)) {
            onSuccessfulLoad(moduleName, launch = true)
        } else {
            if (isAutoDownload) {
                Timber.w("P1Download Module {launch} {$moduleName} {$totalFreeSpaceSizeInMB}")
                loadAndLaunchModule(moduleName)
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
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initializeViews() {
        progressBar = findViewById(R.id.progress_bar)
        progressBar.getProgressDrawable().setColorFilter(
            ContextCompat.getColor(this, R.color.tkpd_main_green),
            android.graphics.PorterDuff.Mode.MULTIPLY)
        progressText = findViewById(R.id.progress_text)
        progressTextPercent = findViewById(R.id.progress_text_percent)
        imageView = findViewById(R.id.image)

        progressBar.getProgressDrawable().setColorFilter(
            ContextCompat.getColor(this, R.color.tkpd_main_green),
            android.graphics.PorterDuff.Mode.MULTIPLY);
        buttonDownload = findViewById(R.id.button_download)

        buttonDownload.setOnClickListener {
            Timber.w("P1Download Module {button} {$moduleName} {$totalFreeSpaceSizeInMB}")
            loadAndLaunchModule(moduleName)
        }
        progressGroup = findViewById(R.id.progress_group)

        if (imageUrl?.isEmpty() == true) {
            imageView.visibility = View.GONE
        } else {
            ImageHandler.LoadImage(imageView, imageUrl)
            imageView.visibility = View.VISIBLE
        }
    }

    private fun loadAndLaunchModule(name: String) {
        initialDownloading = true
        displayProgress()
        progressText.text = getString(R.string.downloading_x, moduleNameTranslated)

        // Skip loading if the module already is installed. Perform success action directly.
        if (manager.installedModules.contains(name)) {
            onSuccessfulLoad(name, launch = true)
            return
        }

        // Create request to install a feature module by name.
        val request = SplitInstallRequest.newBuilder()
            .addModule(name)
            .build()

        // Load and install the requested feature module.
        manager.startInstall(request).addOnSuccessListener {
            sessionId = it
        }.addOnFailureListener { exception ->
            val errorCode = (exception as SplitInstallException).errorCode
            sessionId = null
            hideProgress()
            val message = getString(R.string.error_for_module_x, moduleName)
            showFailedMessage(message, errorCode.toString())
        }
    }

    private fun onSuccessfulLoad(moduleName: String, launch: Boolean) {
        totalFreeSpaceSizeInMB = getTotalFreeSpaceSizeInMB()
        Timber.w("P1Installed Module {$moduleName} {$totalFreeSpaceSizeInMB}")
        progressGroup.visibility = View.INVISIBLE
        if (launch && manager.installedModules.contains(moduleName)) {
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
                if (initialDownloading) {
                    initialDownloadStatus(names, state.totalBytesToDownload())
                    initialDownloading = false
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
        Timber.w("P1Failed Module {$moduleName} - {$errorCode} {$totalFreeSpaceSizeInMB}")
        val userMessage:String
        if (SplitInstallErrorCode.INSUFFICIENT_STORAGE.toString() == errorCode) {
            userMessage = getString(R.string.error_install_df_insufficient_storate)
        } else {
            userMessage = message
        }
        Toaster.showErrorWithAction(this.findViewById(android.R.id.content),
            userMessage,
            Snackbar.LENGTH_INDEFINITE,
            getString(R.string.general_label_ok), View.OnClickListener { })
    }

    private fun updateProgressMessage(message: String) {
        progressText.text = message
    }

    /** Display a loading state to the user. */
    private fun displayLoadingState(state: SplitInstallSessionState, message: String) {
        val totalBytesToDowload = state.totalBytesToDownload().toInt()
        val bytesDownloaded = state.bytesDownloaded().toInt()
        progressBar.max = totalBytesToDowload
        progressBar.progress = bytesDownloaded
        progressText.text = String.format("%.2f KB / %.2f KB",
                (bytesDownloaded.toFloat() / ONE_KB), totalBytesToDowload.toFloat() / ONE_KB)
        progressTextPercent.text = String.format("%.0f%%", bytesDownloaded.toFloat() * 100 / totalBytesToDowload)
    }

    private fun initialDownloadStatus(moduleName: String, moduleSize: Long){
        totalFreeSpaceSizeInMB = getTotalFreeSpaceSizeInMB()
        val moduleSizeinMB = String.format("%.2fMB", moduleSize.toDouble() / (ONE_KB * ONE_KB))
        Timber.w("P1Downloading Module {$moduleName} {$moduleSizeinMB:$totalFreeSpaceSizeInMB}")
    }

    private fun getTotalFreeSpaceSizeInMB() : String {
        totalFreeSpaceSizeInMB = "-"
        applicationContext?.filesDir?.absoluteFile?.toString()?.let {
            val totalSize = File(it).freeSpace.toDouble()
            totalFreeSpaceSizeInMB = String.format("%.2fMB", totalSize / (ONE_KB * ONE_KB))
        }
        return totalFreeSpaceSizeInMB
    }

    private fun displayProgress() {
        progressGroup.visibility = View.VISIBLE
        buttonDownload.visibility = View.INVISIBLE
    }

    private fun hideProgress() {
        progressGroup.visibility = View.INVISIBLE
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

}