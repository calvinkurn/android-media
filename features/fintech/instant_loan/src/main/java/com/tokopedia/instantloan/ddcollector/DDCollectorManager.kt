package com.tokopedia.instantloan.ddcollector


import android.accounts.AccountManager
import android.content.Context
import android.location.LocationManager
import com.tokopedia.instantloan.ddcollector.DDConstants.Constant.REQUIRE
import com.tokopedia.instantloan.ddcollector.DDConstants.Constant.RGEX_PERMISSION_ENUM_SEPARATOR
import com.tokopedia.instantloan.ddcollector.account.Account
import com.tokopedia.instantloan.ddcollector.app.Application
import com.tokopedia.instantloan.ddcollector.bdd.BasicDeviceData
import com.tokopedia.instantloan.ddcollector.contact.Contact
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class DDCollectorManager private constructor() : PermissionResultCallback {

    private val mComponents: MutableSet<String>
    private var mDangerousComponents: List<String>? = null
    private var mContext: Context? = null
    private val mExecutorService: ExecutorService
    private var mCallback: OnDeviceDataReady? = null
    private var mPermissionUtils: PermissionUtils? = null
    private lateinit var permissionResultCallback: PermissionResultCallback

    private val defaultsComponents: Set<String>
        get() {
            val components = HashSet<String>()
            components.add(DDConstants.DDComponents.READ_CONTACTS.`val`())
            components.add(DDConstants.DDComponents.GET_ACCOUNTS.`val`())
            components.add(DDConstants.DDComponents.APP.`val`())
            components.add(DDConstants.DDComponents.BASIC_DEVICE_DATA.`val`())
            components.add(DDConstants.DDComponents.READ_PHONE_STATE.`val`())
            components.add(DDConstants.DDComponents.ACCESS_COARSE_LOCATION.`val`())
            components.add(DDConstants.DDComponents.ACCESS_FINE_LOCATION.`val`())
            return components
        }

    val dangerousPermissions: List<String>
        get() {
            val permissions = ArrayList<String>()
            for (permission in mComponents) {
                if (permission.contains(REQUIRE)) {
                    permissions.add(permission.split(RGEX_PERMISSION_ENUM_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
                }
            }
            return permissions
        }

    init {
        mExecutorService = Executors.newSingleThreadExecutor()
        mComponents = HashSet()
        mComponents.addAll(defaultsComponents)
    }

    fun init(context: Context?, permissionResultCallback: PermissionResultCallback) {
        this.mContext = context
        this.permissionResultCallback = permissionResultCallback
        mPermissionUtils = PermissionUtils(mContext!!, this)
        mDangerousComponents = dangerousPermissions
        mIsInitialized = true
    }

    fun process(callback: (OnDeviceDataReady?)) {
        if (!mIsInitialized) {
            throw IllegalStateException("Please initialized the SDK first, by calling 'init(Context)' method")
        }

        this.mCallback = callback

        if (mPermissionUtils!!.isPermissionsGranted(mDangerousComponents!!)) {
            loadDeviceData()
        } else {
            mPermissionUtils!!.checkPermission(mDangerousComponents!!, PermissionUtils.PERMISSION_REQUEST_CODE)
        }
    }

    fun loadDeviceData() {
        val callable = Callable<Map<String, Any?>> {
            val data = collectDD()

            data.let { mCallback?.callback(it) }
            data
        }

        addTaskInExecutor(callable)
    }

    private fun addTaskInExecutor(callable: Callable<Map<String, Any?>>): Future<Map<String, Any?>> {
        return mExecutorService.submit(callable)
    }

    private fun collectDD(): Map<String, Any?>? {
        try {
            val info = InfoCollectServiceImpl()
            if (mComponents.contains(DDConstants.DDComponents.APP.`val`())) {
                info.add(Application(mContext!!.packageManager))
            }

            if (mComponents.contains(DDConstants.DDComponents.GET_ACCOUNTS.`val`())) {
                info.add(Account(mContext!!.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager))
            }

            if (mComponents.contains(DDConstants.DDComponents.READ_CONTACTS.`val`())) {
                info.add(Contact(mContext!!.contentResolver))
            }

            if (mComponents.contains(DDConstants.DDComponents.BASIC_DEVICE_DATA.`val`())) {
                info.add(BasicDeviceData(mContext!!, mContext!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager))
            }

            return info.getData()
        } catch (e: Exception) {
            return null
        }

    }

    fun onRequestPermissionsResult(requestCode: Int,requiredPermissions: List<String>, permissions: Array<String>, grantResults: IntArray) {
        if(mPermissionUtils != null) {
            mPermissionUtils?.onRequestPermissionsResult(requestCode, requiredPermissions, permissions, grantResults)
        }
    }

    override fun permissionGranted(requestCode: Int) {
        loadDeviceData()
        permissionResultCallback.permissionGranted(requestCode)
    }

    override fun permissionDenied(requestCode: Int) {
        permissionResultCallback.permissionDenied(requestCode)
    }

    override fun neverAskAgain(requestCode: Int) {
        permissionResultCallback.neverAskAgain(requestCode)
    }

    companion object {
        private var sInstance: DDCollectorManager
        private var mIsInitialized: Boolean = false


        // The class is used as a singleton
        init {
            sInstance = DDCollectorManager()
        }

        fun getsInstance(): DDCollectorManager {
            return sInstance
        }
    }
}
