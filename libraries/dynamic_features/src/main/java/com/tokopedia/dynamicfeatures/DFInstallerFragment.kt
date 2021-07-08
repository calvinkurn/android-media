package com.tokopedia.dynamicfeatures

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.google.android.play.core.splitinstall.*
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dynamicfeatures.utils.StorageUtils
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import kotlinx.android.synthetic.main.activity_dynamic_feature_installer.*
import kotlinx.android.synthetic.main.fragment_df_installer.*
import kotlinx.android.synthetic.main.fragment_df_installer.image
import kotlinx.android.synthetic.main.fragment_df_installer.title_txt
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DFInstallerFragment : Fragment() , CoroutineScope, DFInstaller.DFInstallerView {


    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + CoroutineExceptionHandler { _, _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
    private var successInstall = false
    private var progressGroup: View? = null
    private var fragmentClassPathName = ""
    private var moduleId = ""
    private var downloadTimes = 0
    private var startDownloadPercentage = -1f
    private var moduleSize = 0L
    private var sessionId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

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

    override fun onResume() {
        super.onResume()
        manager = DFInstaller.getManager(requireContext()) ?: return
        manager?.registerListener(listener)
        moduleId = arguments?.getString("MODULE_ID").orEmpty()
        fragmentClassPathName = arguments?.getString("CLASS_PATH_NAME").orEmpty()
        val isAutoDownload = true
        if (DFInstaller.isInstalled(requireContext(), moduleId)) {
            onSuccessfulLoad()
        } else if (isAutoDownload) {
            downloadFeature()
//            logDownloadPage();
        } else {
//            showOnBoardingView()
        }
    }

    override fun onPause() {
        manager?.unregisterListener(listener)
        super.onPause()
    }

    private fun redirectToDestinationFragment(){
        val fragTrans = activity?.supportFragmentManager?.beginTransaction()
        val destinationFragment = activity?.supportFragmentManager?.fragmentFactory?.instantiate(
                ClassLoader.getSystemClassLoader(),
                fragmentClassPathName
        )
        fragTrans?.replace((view?.parent as ViewGroup).id, destinationFragment!!, tag)
        fragTrans?.commit()
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
//        if (cancelDownloadBeforeInstallInPage) {
//            cancelPreviousDownload()
//        }
        updateInformationView(R.drawable.ic_ill_downloading, getString(R.string.dowload_on_process), getString(R.string.wording_download_waiting))
        progressGroup?.visibility = View.VISIBLE
        downloadTimes++
        loadAndLaunchModule(moduleId)
    }

    private fun loadAndLaunchModule(moduleName: String) {
        // Skip loading if the module already is installed. Perform success action directly.
        if (DFInstaller.isInstalled(requireContext(), moduleName)) {
            onSuccessfulLoad()
            return
        }
        if (false) {
//            DFInstaller.installOnBackground(this, listOf(moduleName), this::class.java.simpleName.toString())
//            DFInstaller.attachView(this)
//            DFInstaller.deeplink = deeplink
//            DFInstaller.fallbackUrl = fallbackUrl
        } else {
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
//                    onFailed(errorCode?.toString() ?: exception.toString())
                }
            }
        }
//        addTimeout()
    }

    private fun resetDFInfo() {
        moduleSize = 0
        startDownloadPercentage = -1f
    }

    private fun updateInformationView(imageRes: Int, title: String, subTitle: String,
                                      buttonText: String = "",
                                      onDownloadButtonClicked: () -> (Unit) = {},
                                      ctaText: String = "") {
        imageView?.setImageResource(imageRes)
        progressGroup?.visibility = View.INVISIBLE
//        title_txt.text = title
//        subtitle_txt.text = subTitle
//        if (buttonText.isNotEmpty()) {
//            button_download.text = buttonText
//            button_download.setOnClickListener {
//                onDownloadButtonClicked()
//            }
//            button_download.visibility = View.VISIBLE
//        } else {
//            button_download.visibility = View.INVISIBLE
//        }
//        if (fallbackUrl.isNotEmpty() && ctaText.isNotEmpty()) {
//            button_cta.visibility = View.VISIBLE
//            button_cta.text = ctaText
//            button_cta.setOnClickListener { _ ->
//                RouteManager.getIntent(this, ApplinkConstInternalGlobal.WEBVIEW, fallbackUrl)?.let { it ->
//                    ServerLogger.log(Priority.P1, "DFM_FALLBACK",
//                            mapOf("type" to "click",
//                                    "mod_name" to moduleName,
//                                    "deeplink" to deeplink,
//                                    "url" to fallbackUrl
//                            ))
//                    startActivity(it)
//                    finish()
//                }
//            }
//        } else {
//            button_cta.visibility = View.GONE
//        }
    }

    private fun initView() {
        imageView = view?.findViewById(R.id.image)
        progressBar = view?.findViewById(R.id.progress_bar)
        progressGroup = view?.findViewById(R.id.progress_group)
        progressBar?.progressDrawable?.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.tkpd_main_green),
                android.graphics.PorterDuff.Mode.MULTIPLY)
    }

    override fun onDownload(state: SplitInstallSessionState) {
//  In order to see this, the application has to be uploaded to the Play Store.
//        displayLoadingState(state, getString(R.string.downloading_x, moduleNameTranslated))
//        if (moduleSize == 0L) {
//            moduleSize = state.totalBytesToDownload()
//        }
//        addTimeout()
    }

    override fun onRequireUserConfirmation(state: SplitInstallSessionState) {
    }

    override fun onInstalled() {
//        timerJob.cancel()
        endDownloadTimeStamp = System.currentTimeMillis()
        onSuccessfulLoad()
    }

    override fun onInstalling(state: SplitInstallSessionState) {
    }

    override fun onFailed(errorString: String) {
    }

    override fun getModuleNameView(): String {
        return ""
    }

    override fun getDeeplink(): String {
        return  ""
    }

    override fun getFallbackUrl(): String {
       return ""
    }
}