package com.tokopedia.instantloan.ddcollector.app

import android.content.pm.PackageManager
import com.tokopedia.instantloan.ddcollector.BaseCollector
import java.util.*

class Application(private val mPackageManager: PackageManager) : BaseCollector() {

    override fun getType(): String {
        return DD_APPLICATION
    }

    override fun getData(): Any {
        val installedApps = this.mPackageManager.getInstalledApplications(PackageManager.GET_META_DATA)
                ?: return ""

        val phoneInfoList = ArrayList<Map<String, String>>()

        for (appInfo in installedApps) {
            try {
                val packageInfo = this.mPackageManager.getPackageInfo(appInfo.packageName, PackageManager.GET_META_DATA)
                val packageInfoMap = HashMap<String, String>()
                packageInfoMap[PACKAGE_NAME] = packageInfo.packageName
                packageInfoMap[FIRST_INSTALL_TIME] = packageInfo.firstInstallTime.toString()
                packageInfoMap[LAST_UPDATE_TIME] = packageInfo.lastUpdateTime.toString()
                packageInfoMap[TYPE] = appInfo.flags.toString()
                packageInfoMap[NAME] = appInfo.loadLabel(mPackageManager).toString()
                phoneInfoList.add(packageInfoMap)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        return phoneInfoList
    }

    companion object {

        val DD_APPLICATION = "application"
        val PACKAGE_NAME = "package_name"
        val FIRST_INSTALL_TIME = "first_install_time"
        val LAST_UPDATE_TIME = "last_update_time"
        val TYPE = "type"
        val NAME = "name"
    }
}
