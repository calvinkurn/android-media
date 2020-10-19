package com.tokopedia.campaign.shake.landing.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.applink.internal.ApplinkConstInternalPromo;
import com.tokopedia.campaign.shake.landing.R;
import com.tokopedia.campaign.shake.landing.di.CampaignComponent;
import com.tokopedia.campaign.shake.landing.di.DaggerCampaignComponent;
import com.tokopedia.campaign.shake.landing.view.presenter.ShakeDetectContract;
import com.tokopedia.campaign.shake.landing.view.presenter.ShakeDetectPresenter;
import com.tokopedia.utils.permission.PermissionCheckerHelper;
import com.tokopedia.unifycomponents.Toaster;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by sandeepgoyal on 14/02/18.
 */

public class ShakeDetectCampaignActivity extends BaseSimpleActivity implements ShakeDetectContract.View, HasComponent<CampaignComponent> {

    public static String SCREEN_NAME = "ShakeDetectCampaignActivity";
    private View shakeShakeMessageButton;
    private View parent;
    private View cancelButton;
    private View cancelBtn;
    private View disableShakeShake;
    private View layoutshakeShakeErrorMsg;
    private View btnShakeClose;
    private TextView shakeShakeErrorMsg;
    private View btnTurnOff;
    protected CampaignComponent campaignComponent;
    private PermissionCheckerHelper permissionCheckerHelper;

    @Inject
    ShakeDetectPresenter presenter;

    TextView shakeShakeMessage;
    private View cancelBtn1;
    private View cancelBtn2;

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionCheckerHelper = new PermissionCheckerHelper();
        shakeShakeMessage = (TextView) findViewById(R.id.shake_shake_message);
        shakeShakeMessageButton =  findViewById(R.id.shake_shake_message_button);
        cancelButton = findViewById(R.id.cancel_button);
        parent = findViewById(R.id.parent);
        cancelBtn = findViewById(R.id.cancel_btn);
        cancelBtn1 = findViewById(R.id.cancel_btn2);
        cancelBtn2 = findViewById(R.id.cancel_btn3);
        disableShakeShake = findViewById(R.id.disable_shake_shake_button);
        btnTurnOff = findViewById(R.id.btn_turn_off);
        shakeShakeErrorMsg = findViewById(R.id.shake_shake_msg);
        layoutshakeShakeErrorMsg = findViewById(R.id.layout_shake_error);
        btnShakeClose = findViewById(R.id.btn_shake_close);

        initInjector();
        attachToPresenter();
        cancelButton.setOnClickListener(cancelListener);
        cancelBtn.setOnClickListener(cancelListener);
        cancelBtn1.setOnClickListener(cancelListener);
        cancelBtn2.setOnClickListener(cancelListener);
        btnShakeClose.setOnClickListener(cancelListener);
        shakeDetect();
        btnTurnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDisableShakeShake();
            }
        });


    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shakeDetect();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.shake_landing_activity_shake;
    }

    void attachToPresenter() {
        presenter.attachView(this);
        presenter.setPermissionChecker(permissionCheckerHelper);
    }

    protected void shakeDetect() {
        presenter.onShakeDetect();
    }

    @Override
    public CampaignComponent getComponent() {
        if (campaignComponent == null) initInjector();
        return campaignComponent;
    }

    protected void initInjector() {
        campaignComponent = DaggerCampaignComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
        campaignComponent.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void showErrorGetInfo() {
        shakeShakeErrorMsg.setText(getString(R.string.shake_landing_shake_default_error));
        layoutshakeShakeErrorMsg.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.GONE);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    public void showErrorNetwork(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean isLongShakeTriggered() {
        Uri uri = getIntent().getData();

        if (uri != null) {
            List<String> paths = UriUtil.destructureUri(ApplinkConstInternalPromo.PROMO_CAMPAIGN_SHAKE_LANDING, uri);
            if (!paths.isEmpty()) {
                boolean isLongShake = Boolean.parseBoolean(paths.get(0));
                return isLongShake;
            }
        }
        return false;
    }

    static int TOAST_LENGTH = 3000;

    @Override
    public void setSnackBarErrorMessage() {
        Toaster.INSTANCE.make(parent, getResources().getString(R.string.shake_landing_shake_login_error), TOAST_LENGTH, Toaster.TYPE_ERROR,
                getResources().getString(R.string.shake_landing_masuk_sekarang), v->{
                    RouteManager.route(ShakeDetectCampaignActivity.this, ApplinkConst.LOGIN);
                    finish();
                });
    }

    @Override
    public void setInvisibleCounter() {
        shakeShakeMessageButton.setVisibility(View.GONE);
    }

    @Override
    public void showDisableShakeShakeVisible() {
        disableShakeShake.setVisibility(View.VISIBLE);
    }

    public void makeInvisibleShakeShakeDisableView() {
        disableShakeShake.setVisibility(View.GONE);
    }

    @Override
    public void setCancelButtonVisible() {
        cancelButton.setVisibility(View.VISIBLE);
    }

    @Override
    public Activity getCurrentActivity() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public String getScreenName() {
        return SCREEN_NAME;
    }
    View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.onCancelClick();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper.onRequestPermissionsResult(ShakeDetectCampaignActivity.this,
                    requestCode, permissions,
                    grantResults);
        }
    }

    @Override
    public void goToGeneralSetting() {
        Intent intent = RouteManager.getIntent(this, ApplinkConstInternalGlobal.GENERAL_SETTING);
        startActivity(intent);
        finish();
    }
}
