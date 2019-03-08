package com.tokopedia.kyc.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.DaggerKYCComponent;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.model.ConfirmRequestDataContainer;
import com.tokopedia.kyc.view.fragment.FragmentUpgradeToOvo;
import com.tokopedia.kyc.view.interfaces.ActivityListener;
import com.tokopedia.kyc.view.interfaces.LoaderUiListener;

import java.util.ArrayList;
import java.util.List;

public class StartUpgradeToOvoActivity extends BaseSimpleActivity implements
        HasComponent<KYCComponent>, ActivityListener, LoaderUiListener {

    private KYCComponent KYCComponent = null;
    private ConfirmRequestDataContainer confirmRequestDataContainer;
    private ProgressDialog loading;
    private List<String> permissionsToRequest;
    private boolean isPermissionGotDenied;
    protected static final int REQUEST_CAMERA_PERMISSIONS = 932;
    private int retryCount = 3;

    @DeepLink(Constants.AppLinks.OVOUPGRADE)
    public static Intent getCallingStartUpgradeToOvo(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, StartUpgradeToOvoActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }

    @Override
    protected Fragment getNewFragment() {
        return FragmentUpgradeToOvo.newInstance();
    }

    @Override
    public KYCComponent getComponent() {
        if (KYCComponent == null) {
            initInjector();
        }
        return KYCComponent;
    }

    private void initInjector() {
        KYCComponent = DaggerKYCComponent.builder().baseAppComponent(
                ((BaseMainApplication)getApplicationContext()).getBaseAppComponent()).build();
    }

    @Override
    public String getScreenName() {
        return Constants.Values.UPGRADE_OVO_SCR;
    }

    @Override
    public void setHeaderTitle(String title) {
        updateTitle(title);
    }

    @Override
    public void addReplaceFragment(BaseDaggerFragment baseDaggerFragment, boolean replace, String tag) {
        retryCount = 3;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(replace) {
            fragmentTransaction.replace(R.id.parent_view, baseDaggerFragment, tag);
        }
        else {
            fragmentTransaction.add(R.id.parent_view, baseDaggerFragment, tag);
        }
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void showHideActionbar(boolean show){
        if(show) getSupportActionBar().show();
        else getSupportActionBar().hide();
    }

    @Override
    public ConfirmRequestDataContainer getDataContatainer() {
        if(confirmRequestDataContainer == null){
            confirmRequestDataContainer = new ConfirmRequestDataContainer();
        }
        return confirmRequestDataContainer;
    }

    @Override
    public boolean isRetryValid() {
        if(retryCount > 0){
            retryCount--;
            return true;
        }
        return false;
    }

    @Override
    public void hideProgressDialog(){
        if(loading != null)
            loading.dismiss();
    }

    @Override
    public void showProgressDialog() {
        if(loading == null) loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.title_loading));
        loading.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionsToRequest != null && grantResults.length == permissionsToRequest.size()) {
            int grantCount = 0;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    isPermissionGotDenied = true;
                    break;
                }
                grantCount++;
            }
            if (grantCount == grantResults.length) {
                isPermissionGotDenied = false;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPermissionGotDenied) {
            finish();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            String[] permissions;
            permissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            permissionsToRequest = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }
            if (!permissionsToRequest.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CAMERA_PERMISSIONS);
            }
        }
    }
}
