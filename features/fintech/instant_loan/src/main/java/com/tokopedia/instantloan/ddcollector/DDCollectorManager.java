package com.tokopedia.instantloan.ddcollector;


import android.accounts.AccountManager;
import android.content.Context;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.instantloan.ddcollector.account.Account;
import com.tokopedia.instantloan.ddcollector.app.Application;
import com.tokopedia.instantloan.ddcollector.bdd.BasicDeviceData;
import com.tokopedia.instantloan.ddcollector.call.Call;
import com.tokopedia.instantloan.ddcollector.contact.Contact;
import com.tokopedia.instantloan.ddcollector.sms.Sms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DDCollectorManager implements PermissionResultCallback {

    private Set<String> mComponents;
    private List<String> mDangerousComponents;
    private Context mContext;
    private ExecutorService mExecutorService;
    private OnDeviceDataReady mCallback;
    private PermissionUtils mPermissionUtils;
    private static DDCollectorManager sInstance;
    private static boolean mIsInitialized;
    private PermissionResultCallback permissionResultCallback;


    // The class is used as a singleton
    static {
        sInstance = new DDCollectorManager();
    }

    public static DDCollectorManager getsInstance() {
        return sInstance;
    }

    private DDCollectorManager() {
        mExecutorService = Executors.newSingleThreadExecutor();
        mComponents = new HashSet<>();
        mComponents.addAll(getDefaultsComponents());
    }

    public void init(Context context, PermissionResultCallback permissionResultCallback) {
        this.mContext = context;
        this.permissionResultCallback = permissionResultCallback;
        mPermissionUtils = new PermissionUtils(mContext, this);
        mDangerousComponents = getDangerousPermissions();
        mIsInitialized = true;
    }

    public void process(@NonNull OnDeviceDataReady callback) {
        if (!mIsInitialized) {
            throw new IllegalStateException("Please initialized the SDK first, by calling 'init(Context)' method");
        }

        this.mCallback = callback;

        if (mPermissionUtils.isPermissionsGranted(mDangerousComponents)) {
            loadDeviceData();
        } else {
            mPermissionUtils.checkPermission(mDangerousComponents, PermissionUtils.PERMISSION_REQUEST_CODE);
        }
    }

    private void loadDeviceData() {
        Callable<Map<String, Object>> callable = new Callable<Map<String, Object>>() {
            public Map<String, Object> call() throws Exception {
                Map<String, Object> data = collectDD();
                if (mCallback != null) {
                    mCallback.callback(data);
                }
                return data;
            }
        };

        addTaskInExecutor(callable);
    }

    private Future<Map<String, Object>> addTaskInExecutor(Callable<Map<String, Object>> callable) {
        return mExecutorService.submit(callable);
    }

    @Nullable
    private Map<String, Object> collectDD() {
        try {
            InfoCollectService info = new InfoCollectServiceImpl();
            if (mComponents.contains(DDConstants.DDComponents.APP.val())) {
                info.add(new Application(mContext.getPackageManager()));

            }

            if (mComponents.contains(DDConstants.DDComponents.GET_ACCOUNTS.val())) {
                info.add(new Account((AccountManager) mContext.getSystemService(Context.ACCOUNT_SERVICE)));
            }

            if (mComponents.contains(DDConstants.DDComponents.READ_CONTACTS.val())) {
                info.add(new Contact(mContext.getContentResolver()));
            }

            if (mComponents.contains(DDConstants.DDComponents.READ_CALL_LOG.val())) {
                info.add(new Call(mContext.getContentResolver()));
            }

            if (mComponents.contains(DDConstants.DDComponents.READ_SMS.val())) {
                info.add(new Sms(mContext.getContentResolver()));
            }

            if (mComponents.contains(DDConstants.DDComponents.BASIC_DEVICE_DATA.val())) {
                info.add(new BasicDeviceData(mContext, (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE)));
            }

            return info.getData();
        } catch (Exception e) {
            return null;
        }
    }

    public void addComponents(@NonNull Set<String> components) {
        mComponents.addAll(components);
    }

    public void addComponents(@NonNull String component) {
        mComponents.add(component);
    }

    private Set<String> getDefaultsComponents() {
        Set<String> components = new HashSet<>();
        components.add(DDConstants.DDComponents.READ_SMS.val());
        components.add(DDConstants.DDComponents.READ_CONTACTS.val());
        components.add(DDConstants.DDComponents.READ_CALL_LOG.val());
        components.add(DDConstants.DDComponents.GET_ACCOUNTS.val());
        components.add(DDConstants.DDComponents.APP.val());
        components.add(DDConstants.DDComponents.BASIC_DEVICE_DATA.val());
        components.add(DDConstants.DDComponents.READ_PHONE_STATE.val());
        components.add(DDConstants.DDComponents.ACCESS_COARSE_LOCATION.val());
        components.add(DDConstants.DDComponents.ACCESS_FINE_LOCATION.val());
        return components;
    }

    private List<String> getDangerousPermissions() {
        List<String> permissions = new ArrayList<>();
        for (String permission : mComponents) {
            if (permission.contains(DDConstants.REQUIRE)) {
                permissions.add(permission.split(DDConstants.RGEX_PERMISSION_ENUM_SEPARATOR)[1]);
            }
        }
        return permissions;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void permissionGranted(int requestCode) {
        loadDeviceData();
        permissionResultCallback.permissionGranted(requestCode);
    }

    @Override
    public void permissionDenied(int requestCode) {
        permissionResultCallback.permissionDenied(requestCode);
    }

    @Override
    public void neverAskAgain(int requestCode) {
        permissionResultCallback.neverAskAgain(requestCode);
    }
}
