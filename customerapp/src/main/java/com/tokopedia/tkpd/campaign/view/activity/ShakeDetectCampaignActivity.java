package com.tokopedia.tkpd.campaign.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.campaign.di.CampaignComponent;
import com.tokopedia.tkpd.campaign.di.DaggerCampaignComponent;
import com.tokopedia.tkpd.campaign.view.presenter.ShakeDetectContract;
import com.tokopedia.tkpd.campaign.view.presenter.ShakeDetectPresenter;

import javax.inject.Inject;

/**
 * Created by sandeepgoyal on 14/02/18.
 */

public class ShakeDetectCampaignActivity extends BaseSimpleActivity implements ShakeDetectContract.View,HasComponent<CampaignComponent> {


    private TkpdProgressDialog progressDialog;
    protected CampaignComponent campaignComponent;
    @Inject
    ShakeDetectPresenter presenter;


    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
        attachToPresenter();
        shakeDetect();

    }

    void attachToPresenter() {
         presenter.attachView(this);
    }

    protected void shakeDetect() {
        presenter.onShakeDetect();
    }


    @Override
    public void setContentView(int layoutResID) {
    }

    public static Intent getShakeDetectCampaignActivity(Context context) {
        Intent i = new Intent(context, ShakeDetectCampaignActivity.class);
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
        builder.setPositiveButton(getString(R.string.btn_dialog_wrong_scan),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        presenter.onRetryClick();
                    }
                }).create().show();
    }

    @Override
    public void showErrorNetwork(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode,resultCode,data);
    }
}
