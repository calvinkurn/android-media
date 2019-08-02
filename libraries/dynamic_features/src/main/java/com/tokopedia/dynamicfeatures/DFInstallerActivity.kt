package com.tokopedia.dynamicfeatures

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.*
import com.tokopedia.dynamicfeatures.R
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.google.android.play.core.tasks.Task
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.unifycomponents.Toaster

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
class DFInstallerActivity : BaseActivity() {
    private lateinit var manager: SplitInstallManager

    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var progressTextPercent: TextView
    private lateinit var buttonDownload: Button
    private lateinit var closeButton: View
    private lateinit var imageView: ImageView
    private lateinit var progressGroup: View
    private var isAutoDownload = false
    private var name: String? = null
    private var task: Task<Int>? = null

    private lateinit var moduleName: String
    private lateinit var moduleNameTranslated: String
    private lateinit var applink: String
    private var imageUrl: String = ""

    companion object {
        private const val EXTRA_NAME = "name"
        private const val EXTRA_APPLINK = "applink"
        private const val EXTRA_AUTO = "auto"
        private const val EXTRA_IMAGE = "image"
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
            imageUrl = uri.getQueryParameter(EXTRA_IMAGE) ?: defaultImageUrl
        }
        if (moduleName.isEmpty()) {
            finish()
        }
        super.onCreate(savedInstanceState)
        if (moduleNameTranslated.isNotEmpty()) {
            setTitle(getString(R.string.installing_x, moduleNameTranslated))
        }
        setContentView(R.layout.activity_dynamic_feature_installer)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.getProgressDrawable().setColorFilter(
            ContextCompat.getColor(this, R.color.tkpd_main_green),
            android.graphics.PorterDuff.Mode.MULTIPLY);
        manager = SplitInstallManagerFactory.create(this)
        initializeViews()
        if (manager.installedModules.contains(name)) {
            onSuccessfulLoad(name ?: "", launch = true)
        } else {
            if (isAutoDownload) {
                loadAndLaunchModule(name ?: "")
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
        progressText = findViewById(R.id.progress_text)
        progressTextPercent = findViewById(R.id.progress_text_percent)
        imageView = findViewById(R.id.image)

        progressBar.getProgressDrawable().setColorFilter(
            ContextCompat.getColor(this, R.color.tkpd_main_green),
            android.graphics.PorterDuff.Mode.MULTIPLY);
        buttonDownload = findViewById(R.id.button_download)
        closeButton = findViewById<View>(R.id.close_button)

        buttonDownload.setOnClickListener {
            loadAndLaunchModule(name ?: "")
        }
        closeButton.setOnClickListener {
            task?.run {
                manager.cancelInstall(this.result)
            }
            hideProgress()
        }
        progressGroup = findViewById(R.id.progress_group)

        if (imageUrl.isEmpty()) {
            imageView.visibility = View.GONE
        } else {
            ImageHandler.LoadImage(imageView, imageUrl)
            imageView.visibility = View.VISIBLE
        }
    }

    private fun loadAndLaunchModule(name: String) {
        displayProgress()
        progressText.text = getString(R.string.downloading_x, name)

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
        task = manager.startInstall(request)
    }

    private fun onSuccessfulLoad(moduleName: String, launch: Boolean) {
        hideProgress()
        if (launch && manager.installedModules.contains(name)) {
            RouteManager.getIntentNoFallback(this, applink)?.let {
                it.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT;
                startActivity(it)
            }
        }
        this.finish()
    }

    /** Listener used to handle changes in state for install requests. */
    private val listener = SplitInstallStateUpdatedListener { state ->
        val multiInstall = state.moduleNames().size > 1

        val names = state.moduleNames().joinToString(" - ")

        when (state.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> {
                //  In order to see this, the application has to be uploaded to the Play Store.
                displayLoadingState(state, getString(R.string.downloading_x, names))
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
                    getString(R.string.installing_x, names)
                )
            }
            SplitInstallSessionStatus.FAILED -> {
                val message = getString(R.string.error_for_module, state.moduleNames(), state.errorCode())
                Toaster.showErrorWithAction(this.findViewById(android.R.id.content),
                    message,
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.general_label_ok), View.OnClickListener { })
                hideProgress()
            }
        }
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


}