package com.tokopedia.tkpd.campaign.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.campaign.di.CampaignComponent;
import com.tokopedia.tkpd.campaign.di.DaggerCampaignComponent;
import com.tokopedia.tkpd.campaign.view.presenter.ShakeDetectContract;
import com.tokopedia.tkpd.campaign.view.presenter.ShakeDetectPresenter;

import android.support.annotation.NonNull;
import android.os.Build;

import javax.inject.Inject;

import butterknife.ButterKnife;

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
    private TkpdProgressDialog progressDialog;
    protected CampaignComponent campaignComponent;
    private PermissionCheckerHelper permissionCheckerHelper;

    @Inject
    ShakeDetectPresenter presenter;

    TextView shakeShakeMessage;
    public static final String KEY_LONG_SHAKE = "KEY_LONG_SHAKE_SHAKE";
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
        ButterKnife.bind(this);
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
        return R.layout.activity_shake;
    }

    void attachToPresenter() {
        presenter.attachView(this);
        presenter.setPermissionChecker(permissionCheckerHelper);
    }

    protected void shakeDetect() {
        presenter.onShakeDetect();
    }


    public static Intent getShakeDetectCampaignActivity(Context context, boolean isLongShake) {
        Intent i = new Intent(context, ShakeDetectCampaignActivity.class);
        i.putExtra(KEY_LONG_SHAKE, isLongShake);
        return i;

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
    public void showProgressDialog() {
        if (progressDialog != null) {
            progressDialog.showDialog();
        } else {
            progressDialog = new TkpdProgressDialog(getApplicationContext(), TkpdProgressDialog.NORMAL_PROGRESS);
        }
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void showErrorGetInfo(String message) {
        shakeShakeErrorMsg.setText(message);
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
    public void updateTimer(Long l) {
        shakeShakeMessage.setText(""+l);

    }

    @Override
    public boolean isLongShakeTriggered() {
        return getIntent().getBooleanExtra(KEY_LONG_SHAKE, false);
    }

    static int TOAST_LENGTH = 3000;

    @Override
    public void setSnackBarErrorMessage() {

        ToasterNormal
                .make(parent,
                        getResources().getString(R.string.shake_login_error),
                        TOAST_LENGTH)
                .setAction(getResources().getString(R.string.masuk_sekarang),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(((TkpdCoreRouter)(getApplication())).getLoginIntent(getCurrentActivity()));
                                finish();
                            }
                        })
                .show();
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
}
