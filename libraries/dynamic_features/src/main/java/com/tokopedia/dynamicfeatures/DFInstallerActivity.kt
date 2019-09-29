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
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.unifycomponents.Toaster
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
    private lateinit var closeButton: View
    private lateinit var imageView: ImageView
    private lateinit var progressGroup: View
    private var isAutoDownload = false
    private var sessionId: Int? = null

    private lateinit var moduleName: String
    private lateinit var moduleNameTranslated: String
    private lateinit var applink: String
    private var imageUrl: String? = null

    companion object {
        private const val EXTRA_NAME = "dfname"
        private const val EXTRA_APPLINK = "dfapplink"
        private const val EXTRA_AUTO = "dfauto"
        private const val EXTRA_IMAGE = "dfimage"
        private const val defaultImageUrl = "https://ecs7.tokopedia.net/img/android/empty_profile/drawable-xxxhdpi/product_image_48_x_48.png"
        private const val CONFIRMATION_REQUEST_CODE = 1
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
        if (moduleName.isEmpty()) {
            finish()
        }
        super.onCreate(savedInstanceState)
        if (moduleNameTranslated.isNotEmpty()) {
            setTitle(getString(R.string.installing_x, moduleNameTranslated))
        }
        setContentView(R.layout.activity_dynamic_feature_installer)
        manager = SplitInstallManagerFactory.create(this)
        initializeViews()
        if (manager.installedModules.contains(moduleName)) {
            onSuccessfulLoad(moduleName, launch = true)
        } else {
            if (isAutoDownload) {
                Timber.w("P1Download Module {$moduleName}")
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
        closeButton = findViewById<View>(R.id.close_button)

        buttonDownload.setOnClickListener {
            loadAndLaunchModule(moduleName)
        }
        closeButton.setOnClickListener {
            try {
                sessionId?.run {
                    manager.cancelInstall(this)
                }
            } catch (e: Exception) {
            } finally {
                sessionId = null
            }
            hideProgress()
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
        }.addOnFailureListener {
            sessionId = null
            hideProgress()
            val message = getString(R.string.error_for_module_x, moduleName)
            showFailedMessage(message)
        }
    }

    private fun onSuccessfulLoad(moduleName: String, launch: Boolean) {
        Timber.w("P1Installed Module {$moduleName}")
        progressGroup.visibility = View.INVISIBLE
        if (launch && manager.installedModules.contains(moduleName)) {
            RouteManager.getIntentNoFallback(this, applink)?.let {
                it.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
                intent.extras?.let { passBundle ->
                    it.putExtras(passBundle)
                }
                startActivity(it)
            }
        }
        this.finish()
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
        Timber.w("P1Failed Module {$moduleName} - {$errorCode}")
        Toaster.showErrorWithAction(this.findViewById(android.R.id.content),
            message,
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
            (bytesDownloaded.toFloat() / 1024), totalBytesToDowload.toFloat() / 1024)
        progressTextPercent.text = String.format("%.0f%%", bytesDownloaded.toFloat() * 100 / totalBytesToDowload)
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