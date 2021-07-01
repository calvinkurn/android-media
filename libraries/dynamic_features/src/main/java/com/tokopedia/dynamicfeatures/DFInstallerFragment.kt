package com.tokopedia.dynamicfeatures

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.play.core.splitinstall.SplitInstallException
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.tokopedia.applink.RouteManager
import com.tokopedia.dynamicfeatures.utils.StorageUtils
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DFInstallerFragment : Fragment() , CoroutineScope {


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
    private lateinit var manager: SplitInstallManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manager = DFInstaller.getManager(requireContext()) ?: return
        launch {
            val moduleId = arguments?.getString("MODULE_ID").orEmpty()
            val fragmentContainerId = arguments?.getInt("FRAGMENT_CONTAINER_ID") ?: -1
            // Create request to install a feature module by name.
            val request = SplitInstallRequest.newBuilder()
                    .addModule(moduleId)
                    .build()

            if (freeInternalStorageBeforeDownload == 0L) {
                freeInternalStorageBeforeDownload = withContext(Dispatchers.IO) {
                    StorageUtils.getFreeSpaceBytes(activity?.applicationContext!!)
                }
            }

            // Load and install the requested feature module.
            startDownloadTimeStamp = System.currentTimeMillis()
            manager.startInstall(request).addOnSuccessListener {
                val test = 0
                val fragTrans = activity?.supportFragmentManager?.beginTransaction()
                val destinationFragment = activity?.supportFragmentManager?.fragmentFactory?.instantiate(ClassLoader.getSystemClassLoader(), "com.example.test_fragment_df.TestDfFragment")
                fragTrans?.replace(fragmentContainerId, destinationFragment!!)
                fragTrans?.commit()
//                if (it == 0) {
//                    onInstalled()
//                } else {
//                    sessionId = it
//                }
            }.addOnFailureListener { exception ->
                val errorCode = (exception as? SplitInstallException)?.errorCode
//                sessionId = null
//                onFailed(errorCode?.toString() ?: exception.toString())
            }
        }
    }
}