package com.tokopedia.tkpd.campaign.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.campaign.di.CampaignComponent;
import com.tokopedia.tkpd.campaign.di.DaggerCampaignComponent;
import com.tokopedia.tkpd.campaign.view.presenter.ShakeDetectContract;
import com.tokopedia.tkpd.campaign.view.presenter.ShakeDetectPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sandeepgoyal on 14/02/18.
 */

public class ShakeDetectCampaignActivity extends BaseSimpleActivity implements ShakeDetectContract.View, HasComponent<CampaignComponent> {



    public static String SCREEN_NAME = "ShakeDetectCampaignActivity";
    View shakeShakeMessageButton;
    View cancelButton;
    View cancelBtn;
    private TkpdProgressDialog progressDialog;
    protected CampaignComponent campaignComponent;

    @Inject
    ShakeDetectPresenter presenter;

    TextView shakeShakeMessage;
    public static final String KEY_LONG_SHAKE = "KEY_LONG_SHAKE_SHAKE";

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shakeShakeMessage = (TextView) findViewById(R.id.shake_shake_message);
        shakeShakeMessageButton =  findViewById(R.id.shake_shake_message_button);
        cancelButton = findViewById(R.id.cancel_button);
        cancelBtn = findViewById(R.id.cancel_btn);
        initInjector();
        attachToPresenter();
        ButterKnife.bind(this);
        cancelButton.setOnClickListener(cancelListener);
        cancelBtn.setOnClickListener(cancelListener);
        shakeDetect();


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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_dialog_wrong_scan));
        builder.setMessage(message);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                presenter.onRetryClick();
            }
        });
        builder.setPositiveButton(getString(R.string.btn_dialog_wrong_scan),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).create().show();
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

    @Override
    public void setInvisibleCounter() {
        shakeShakeMessageButton.setVisibility(View.GONE);
    }

    @Override
    public void setCancelButtonVisible() {
        cancelButton.setVisibility(View.VISIBLE);
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

}
