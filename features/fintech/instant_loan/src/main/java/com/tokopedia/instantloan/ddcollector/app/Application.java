//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tokopedia.instantloan.ddcollector.app;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tokopedia.instantloan.ddcollector.BaseCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application extends BaseCollector {

    public static final String DD_APPLICATION = "application";
    public static final String PACKAGE_NAME = "package_name";
    public static final String FIRST_INSTALL_TIME = "first_install_time";
    public static final String LAST_UPDATE_TIME = "last_update_time";
    public static final String TYPE = "type";
    public static final String NAME = "name";

    private PackageManager mPackageManager;

    public Application(PackageManager packageManager) {
        this.mPackageManager = packageManager;
    }

    public String getType() {
        return DD_APPLICATION;
    }

    public Object getData() {
        List<ApplicationInfo> installedApps = this.mPackageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        if (installedApps == null) {
            return null;
        }

        List<Map<String, String>> phoneInfoList = new ArrayList<>();

        for (ApplicationInfo appInfo : installedApps) {
            try {
                PackageInfo packageInfo = this.mPackageManager.getPackageInfo(appInfo.packageName, PackageManager.GET_META_DATA);
                Map<String, String> packageInfoMap = new HashMap<>();
                packageInfoMap.put(PACKAGE_NAME, packageInfo.packageName);
                packageInfoMap.put(FIRST_INSTALL_TIME, String.valueOf(packageInfo.firstInstallTime));
                packageInfoMap.put(LAST_UPDATE_TIME, String.valueOf(packageInfo.lastUpdateTime));
                packageInfoMap.put(TYPE, String.valueOf(appInfo.flags));
                packageInfoMap.put(NAME, String.valueOf(appInfo.loadLabel(mPackageManager)));
                phoneInfoList.add(packageInfoMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return phoneInfoList;
    }
}
