package com.tokopedia.dynamicfeatures

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.play.core.splitinstall.*
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dynamicfeatures.config.DFConfig
import com.tokopedia.dynamicfeatures.config.DFRemoteConfig
import com.tokopedia.dynamicfeatures.constant.CommonConstant
import com.tokopedia.dynamicfeatures.constant.ErrorConstant
import com.tokopedia.dynamicfeatures.utils.ErrorUtils
import com.tokopedia.dynamicfeatures.utils.PlayServiceUtils
import com.tokopedia.dynamicfeatures.utils.StorageUtils
import kotlinx.android.synthetic.main.activity_dynamic_feature_installer.*
import kotlinx.android.synthetic.main.fragment_df_installer.*
import kotlinx.android.synthetic.main.fragment_df_installer.button_download
import kotlinx.android.synthetic.main.fragment_df_installer.subtitle_txt
import kotlinx.android.synthetic.main.fragment_df_installer.title_txt
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

@Suppress("unused")
class DFInstallerFragment : Fragment(), CoroutineScope {

    companion object {
        private const val CONFIRMATION_REQUEST_CODE = 1
        private const val SETTING_REQUEST_CODE = 2
        const val DOWNLOAD_MODE_PAGE = "Page"
        const val TIMEOUT_ERROR_MESSAGE = "timeout"
        private const val BUNDLE_KEY_MODULE_ID = "MODULE_ID"
        private const val BUNDLE_KEY_MODULE_NAME = "MODULE_NAME"
        private const val BUNDLE_ARGUMENTS_KEY_EXTRAS = "BUNDLE_ARGUMENTS_EXTRAS"
        private const val BUNDLE_KEY_CLASS_NAME = "CLASS_NAME"
    }

    private var job = Job()
    private var timerJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + CoroutineExceptionHandler { _, _ -> }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_df_installer, container, false)
    }

    private var freeInternalStorageBeforeDownload = 0L
    private var startDownloadTimeStamp = 0L
    private var endDownloadTimeStamp = 0L
    private var manager: SplitInstallManager? = null
    private var progressBar: ProgressBar? = null
    private var imageView: ImageView? = null
    private var progressTextPercent: TextView? = null
    private var successInstall = false
    private var progressGroup: View? = null
    private var fragmentClassPathName = ""
    private var moduleId = ""
    private var moduleName = ""
    private var destinationFragmentExtras = Bundle()
    private var downloadTimes = 0
    private var startDownloadPercentage = -1f
    private var moduleSize = 0L
    private var sessionId: Int? = null
    private var errorList: MutableList<String> = mutableListOf()
    private var dfConfig: DFConfig? = null
    private var allowRunningServiceFromActivity: Boolean = false
    private var cancelDownloadBeforeInstallInPage: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        dfConfig = DFRemoteConfig.getConfig(requireContext())
        manager = DFInstaller.getManager(requireContext()) ?: return
        manager?.registerListener(listener)
        moduleId = arguments?.getString(BUNDLE_KEY_MODULE_ID).orEmpty()
        moduleName = arguments?.getString(BUNDLE_KEY_MODULE_NAME).orEmpty()
        destinationFragmentExtras = arguments?.getBundle(BUNDLE_ARGUMENTS_KEY_EXTRAS) ?: Bundle()
        fragmentClassPathName = arguments?.getString(BUNDLE_KEY_CLASS_NAME).orEmpty()
        allowRunningServiceFromActivity = dfConfig?.allowRunningServiceFromActivity(moduleId)
                ?: false
        cancelDownloadBeforeInstallInPage = dfConfig?.cancelDownloadBeforeInstallInPage ?: false
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

            SplitInstallSessionStatus.FAILED -> {
                onFailed(state.errorCode().toString())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val isAutoDownload = true
        val currentFragment = activity?.supportFragmentManager?.findFragmentByTag(tag)
        if (currentFragment == null) {
            when {
                DFInstaller.isInstalled(requireContext(), moduleId) -> {
                    onSuccessfulLoad()
                }
                isAutoDownload -> {
                    downloadFeature()
                }
                else -> {
                    showOnBoardingView()
                }
            }
        }
    }

    override fun onPause() {
        manager?.unregisterListener(listener)
        super.onPause()
    }

    private fun redirectToDestinationFragment() {
        activity?.supportFragmentManager?.fragmentFactory?.instantiate(
                ClassLoader.getSystemClassLoader(),
                fragmentClassPathName
        )?.let { destinationFragment ->
            val fragTrans = activity?.supportFragmentManager?.beginTransaction()
            destinationFragment.arguments = destinationFragmentExtras
            fragTrans?.replace((view?.parent as ViewGroup).id, destinationFragment, tag)
            fragTrans?.commit()
        }
    }

    /**
     * mechanism if the download Dynamic Feature is Too long, it will pop up to launch without Install
     */
    private fun addTimeout() {
        val timeout = dfConfig?.timeout ?: 0L
        if (timeout <= 0) {
            return
        }
        timerJob.cancel()
        timerJob = launch(Dispatchers.IO) {
            delay(TimeUnit.SECONDS.toMillis(timeout))
            withContext(Dispatchers.Main) {
                cancelPreviousDownload()
                //show timeoutError
                onFailed(DFInstallerActivity.TIMEOUT_ERROR_MESSAGE + "after" + timeout)
            }
        }
    }

    private fun cancelPreviousDownload() {
        try {
            if (allowRunningServiceFromActivity) {
                DFInstaller.stopInstall(activity?.applicationContext!!)
            } else {
                sessionId?.let {
                    manager?.cancelInstall(it)
                }
                sessionId = null
            }
        } catch (ignored: Exception) {

        }
    }

    private fun onSuccessfulLoad() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SplitInstallHelper.updateAppInfo(requireContext())
        }
        successInstall = DFInstaller.isInstalled(requireContext(), moduleId)
        progressGroup?.visibility = View.INVISIBLE
        if (successInstall) {
            redirectToDestinationFragment()
        }
    }

    private fun downloadFeature() {
        if (cancelDownloadBeforeInstallInPage) {
            cancelPreviousDownload()
        }
        updateInformationView(R.drawable.ic_ill_downloading, getString(R.string.dowload_on_process), getString(R.string.wording_download_waiting))
        progressGroup?.visibility = View.VISIBLE
        downloadTimes++
        loadAndLaunchModule(moduleId)
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

    private fun loadAndLaunchModule(moduleName: String) {
        // Skip loading if the module already is installed. Perform success action directly.
        if (DFInstaller.isInstalled(requireContext(), moduleName)) {
            onSuccessfulLoad()
            return
        }
        launch {
            resetDFInfo()

            // Create request to install a feature module by name.
            val request = SplitInstallRequest.newBuilder()
                    .addModule(moduleName)
                    .build()

            if (freeInternalStorageBeforeDownload == 0L) {
                freeInternalStorageBeforeDownload = withContext(Dispatchers.IO) {
                    StorageUtils.getFreeSpaceBytes(activity?.applicationContext!!)
                }
            }

            // Load and install the requested feature module.
            startDownloadTimeStamp = System.currentTimeMillis()
            manager?.startInstall(request)?.addOnSuccessListener {
                if (it == 0) {
                    onInstalled()
                } else {
                    sessionId = it
                }
            }?.addOnFailureListener { exception ->
                val errorCode = (exception as? SplitInstallException)?.errorCode
                sessionId = null
                onFailed(errorCode?.toString() ?: exception.toString())
            }
        }
        addTimeout()
    }

    private fun resetDFInfo() {
        moduleSize = 0
        startDownloadPercentage = -1f
    }

    private fun updateInformationView(imageRes: Int, title: String, subTitle: String,
                                      buttonText: String = "",
                                      onDownloadButtonClicked: () -> Unit = {}) {
        imageView?.setImageResource(imageRes)
        progressGroup?.visibility = View.INVISIBLE
        title_txt.text = title
        subtitle_txt.text = subTitle
        if (buttonText.isNotEmpty()) {
            button_download.text = buttonText
            button_download.setOnClickListener {
                onDownloadButtonClicked()
            }
            button_download.visibility = View.VISIBLE
        } else {
            button_download.visibility = View.GONE
        }
    }

    private fun initView() {
        imageView = view?.findViewById(R.id.image)
        progressTextPercent = view?.findViewById(R.id.progress_text_percent)
        progressBar = view?.findViewById(R.id.progress_bar)
        progressGroup = view?.findViewById(R.id.progress_group)
        progressBar?.progressDrawable?.setColorFilter(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500), android.graphics.PorterDuff.Mode.MULTIPLY)
    }

    /** Display a loading state to the user. */
    @SuppressLint("LogNotTimber")
    private fun displayLoadingState(state: SplitInstallSessionState) {
        val totalBytesToDowload = state.totalBytesToDownload().toInt()
        val bytesDownloaded = state.bytesDownloaded().toInt()
        progressBar?.max = totalBytesToDowload
        progressBar?.progress = bytesDownloaded
        val progressText = String.format("%.2f KB / %.2f KB",
                (bytesDownloaded.toFloat() / CommonConstant.ONE_KB), totalBytesToDowload.toFloat() / CommonConstant.ONE_KB)
        Log.i(DFInstallerActivity.DOWNLOAD_MODE_PAGE, progressText)
        val downloadProgress = bytesDownloaded.toFloat() * 100 / totalBytesToDowload
        progressTextPercent?.text = String.format("%.0f%%", downloadProgress)
        if (startDownloadPercentage < 0) {
            startDownloadPercentage = downloadProgress
        }
        button_download.visibility = View.GONE
    }

    private fun showOnBoardingView() {
        updateInformationView(R.drawable.ic_ill_onboarding,
                String.format(getString(R.string.feature_download_title), moduleName),
                String.format(getString(R.string.feature_download_subtitle), moduleName),
                getString(R.string.start_download)
        ) {
            downloadFeature()
        }
    }

    private fun showFailedMessage(errorCode: String = "") {
        val errorCodeTemp = ErrorUtils.getValidatedErrorCode(requireContext(), errorCode, freeInternalStorageBeforeDownload)
        errorList.add(errorCodeTemp)
        when (errorCodeTemp) {
            SplitInstallErrorCode.PLAY_STORE_NOT_FOUND.toString() -> {
                updateInformationView(com.tokopedia.globalerror.R.drawable.unify_globalerrors_500,
                        getString(R.string.download_error_play_store_title),
                        getString(R.string.download_error_play_store_subtitle),
                        getString(R.string.goto_playstore)
                ) { PlayServiceUtils.gotoPlayStore(requireActivity()) }
            }
            ErrorConstant.ERROR_INVALID_INSUFFICIENT_STORAGE -> updateInformationView(com.tokopedia.globalerror.R.drawable.unify_globalerrors_500,
                    getString(R.string.download_error_os_and_play_store_title),
                    getString(R.string.download_error_os_and_play_store_subtitle),
                    getString(R.string.goto_seting)
            ) { startActivityForResult(Intent(android.provider.Settings.ACTION_SETTINGS), SETTING_REQUEST_CODE) }
            SplitInstallErrorCode.INSUFFICIENT_STORAGE.toString() -> updateInformationView(R.drawable.ic_ill_insuficient_memory,
                    getString(R.string.download_error_insuficient_storage_title),
                    getString(R.string.download_error_insuficient_storage_subtitle),
                    getString(R.string.goto_seting)
            ) { startActivityForResult(Intent(android.provider.Settings.ACTION_SETTINGS), SETTING_REQUEST_CODE) }
            SplitInstallErrorCode.NETWORK_ERROR.toString() -> updateInformationView(com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection,
                    getString(R.string.download_error_connection_title),
                    getString(R.string.download_error_connection_subtitle),
                    getString(R.string.df_installer_try_again),
                    ::downloadFeature)
            /* SplitInstallErrorCode.APP_NOT_OWNED */
            "-15",
            SplitInstallErrorCode.MODULE_UNAVAILABLE.toString() -> updateInformationView(R.drawable.ic_ill_module_unavailable,
                    getString(R.string.download_error_module_unavailable_title),
                    getString(R.string.download_error_module_unavailable_subtitle),
                    getString(R.string.goto_playstore)
            ) { PlayServiceUtils.gotoPlayStore(requireActivity()) }
            else -> updateInformationView(com.tokopedia.globalerror.R.drawable.unify_globalerrors_500,
                    getString(R.string.download_error_general_title),
                    getString(R.string.download_error_general_subtitle),
                    getString(R.string.df_installer_try_again),
                    ::downloadFeature)
        }
    }

    private fun onDownload(state: SplitInstallSessionState) {
        //  In order to see this, the application has to be uploaded to the Play Store.
        displayLoadingState(state)
        if (moduleSize == 0L) {
            moduleSize = state.totalBytesToDownload()
        }
        addTimeout()
    }

    private fun onRequireUserConfirmation(state: SplitInstallSessionState) {
        manager?.startConfirmationDialogForResult(state, requireActivity(), CONFIRMATION_REQUEST_CODE)
    }

    private fun onInstalled() {
        timerJob.cancel()
        endDownloadTimeStamp = System.currentTimeMillis()
        onSuccessfulLoad()
    }

    private fun onFailed(errorString: String) {
        timerJob.cancel()
        endDownloadTimeStamp = System.currentTimeMillis()
        showFailedMessage(errorString)
    }

}