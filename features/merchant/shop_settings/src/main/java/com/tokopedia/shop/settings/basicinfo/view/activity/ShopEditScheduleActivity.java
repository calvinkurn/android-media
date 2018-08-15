package com.tokopedia.shop.settings.basicinfo.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.shop.common.constant.ShopStatusDef;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel;
import com.tokopedia.shop.settings.R;
import com.tokopedia.shop.settings.basicinfo.view.presenter.UpdateShopSettingsInfoPresenter;
import com.tokopedia.shop.settings.basicinfo.view.presenter.UpdateShopShedulePresenter;
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent;
import com.tokopedia.shop.settings.common.widget.RadioButtonLabelView;
import com.tokopedia.shop.settings.common.widget.SwitchLabelView;

import javax.inject.Inject;

public class ShopEditScheduleActivity extends BaseSimpleActivity implements UpdateShopSettingsInfoPresenter.View, UpdateShopShedulePresenter.View {

    @Inject
    UpdateShopShedulePresenter updateShopShedulePresenter;

    private ProgressDialog progressDialog;
    private ShopBasicDataModel shopBasicDataModel;

    private TextView tvSave;
    private RadioButtonLabelView labelOpen;
    private RadioButtonLabelView labelClosed;
    private View vgScheduleSwitchContent;
    private View vgCloseNowContent;
    private View vgLabelEndCloseNow;
    private TextView tvShopEndCloseNow;
    private TextView tvShopStartClose;
    private TextView tvShopEndClose;
    private TkpdHintTextInputLayout tilShopCloseNote;
    private EditText etShopCloseNote;
    private SwitchLabelView scheduleSwitch;
    //    private View vgOpen;
//    private View vgClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GraphqlClient.init(this);
        super.onCreate(savedInstanceState);

        DaggerShopSettingsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        updateShopShedulePresenter.attachView(this);

        tvSave = findViewById(R.id.tvSave);
        labelOpen = findViewById(R.id.labelOpen);
        labelClosed = findViewById(R.id.labelClose);
        scheduleSwitch = findViewById(R.id.scheduleSwitch);
        vgScheduleSwitchContent = findViewById(R.id.vgScheduleSwitchContent);
        tvShopStartClose = findViewById(R.id.tvShopStartClose);
        tvShopEndClose = findViewById(R.id.tvShopEndClose);
        tilShopCloseNote = findViewById(R.id.tilShopCloseNote);
        etShopCloseNote = findViewById(R.id.etShopCloseNote);

        labelOpen.setOnRadioButtonLabelViewListener(new RadioButtonLabelView.OnRadioButtonLabelViewListener() {
            @Override
            public void onChecked(boolean isChecked) {
                if (isChecked) {
                    labelClosed.setChecked(false);
                }
            }
        });
        labelClosed.setOnRadioButtonLabelViewListener(new RadioButtonLabelView.OnRadioButtonLabelViewListener() {
            @Override
            public void onChecked(boolean isChecked) {
                if (isChecked) {
                    labelOpen.setChecked(false);
                }
            }
        });
        scheduleSwitch.setOnSwitchLabelViewListener(new SwitchLabelView.OnSwitchLabelViewListener() {
            @Override
            public void onChecked(boolean isChecked) {
                if (isChecked) {
                    showCloseScheduleContent();
                } else {
                    hideCloseScheduleContent();
                }
            }
        });

        loadShopBasicData();
    }
//
//    private void disableScheduleSwitch(){
//        vgScheduleSwitch.setEnabled(false);
//        switchWidget.setChecked(false);
//        switchWidget.setEnabled(false);
//        hideCloseScheduleContent();
//    }
//
//    private void enableScheduleSwitch(){
//        vgScheduleSwitch.setEnabled(true);
//        switchWidget.setChecked(false);
//        switchWidget.setEnabled(true);
//        hideCloseScheduleContent();
//    }

    private void hideCloseScheduleContent(){
        vgScheduleSwitchContent.setVisibility(View.GONE);
    }

    private void showCloseScheduleContent(){
        vgScheduleSwitchContent.setVisibility(View.VISIBLE);
    }

//    selectOpen(){
//
//    }

    private void onSaveButtonClicked() {
        if (isInputInvalid()) {
            return;
        }
        showSubmitLoading(getString(R.string.title_loading));
//        String tagLine = etShopSlogan.getText().toString();
//        String desc = etShopDesc.getText().toString();
//        if (!TextUtils.isEmpty(savedLocalImageUrl)) {
//            updateShopSettingsInfoPresenter.uploadShopImage(savedLocalImageUrl,tagLine, desc);
//        } else {
//            updateShopSettingsInfoPresenter.updateShopBasicData(tagLine, desc);
//        }
    }

    public void showSubmitLoading(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        if (!progressDialog.isShowing()) {
            progressDialog.setMessage(message);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void hideSubmitLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private boolean isInputInvalid() {
        boolean hasError = false;
//        String tagLine = etShopSlogan.getText().toString();
//        if (TextUtils.isEmpty(tagLine)) {
//            tilShopSlogan.setError(getString(R.string.shop_slogan_must_be_filled));
//            hasError = true;
//        }
//        String desc = etShopDesc.getText().toString();
//        if (TextUtils.isEmpty(desc)) {
//            tilShopDesc.setError(getString(R.string.shop_desc_must_be_filled));
//            hasError = true;
//        }
        return hasError;
    }

    private void loadShopBasicData() {
        updateShopShedulePresenter.getShopBasicData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (updateShopShedulePresenter != null) {
            updateShopShedulePresenter.detachView();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
//            ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
//            if (imageUrlOrPathList != null && imageUrlOrPathList.size() > 0) {
//                savedLocalImageUrl = imageUrlOrPathList.get(0);
//            }
//            needUpdatePhotoUI = true;
//        }
    }

    @Override
    public void onSuccessUpdateShopBasicData(String successMessage) {
        hideSubmitLoading();
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void onErrorUpdateShopBasicData(Throwable throwable) {
        hideSubmitLoading();
        showSnackbarErrorSubmitEdit(throwable);
    }

    @Override
    public void onSuccessUpdateShopSchedule(String successMessage) {

    }

    @Override
    public void onErrorUpdateShopSchedule(Throwable throwable) {

    }

    @Override
    public void onSuccessGetShopBasicData(ShopBasicDataModel shopBasicDataModel) {
        this.shopBasicDataModel = shopBasicDataModel;
        setUIShopSchedule(shopBasicDataModel);
        tvSave.setVisibility(View.VISIBLE);
    }

    private void setUIShopSchedule(ShopBasicDataModel shopBasicDataModel) {
        int shopStatus = shopBasicDataModel.getStatus();
        switch (shopStatus) {
            case ShopStatusDef.OPEN:
                labelOpen.setChecked(true);
                break;
            default: // closed
                labelOpen.setChecked(false);
                break;
        }
//        updatePhotoUI(shopBasicDataModel);
//        etShopSlogan.setText(shopBasicDataModel.getTagline());
//        etShopSlogan.setSelection(etShopSlogan.getText().length());
//
//        etShopDesc.setText(shopBasicDataModel.getDescription());
//        etShopDesc.setSelection(etShopDesc.getText().length());
    }

    @SuppressLint("Range")
    @Override
    public void onErrorGetShopBasicData(Throwable throwable) {
        showSnackbarErrorShopInfo(throwable);
    }

    private void showSnackbarErrorShopInfo(Throwable throwable) {
        String message = ErrorHandler.getErrorMessage(this, throwable);
        ToasterError.make(findViewById(android.R.id.content),
                message, BaseToaster.LENGTH_INDEFINITE)
                .setAction(getString(R.string.title_try_again), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadShopBasicData();
                    }
                }).show();
    }

    @Override
    public void onErrorUploadShopImage(Throwable throwable) {
        showSnackbarErrorSubmitEdit(throwable);
    }

    private void showSnackbarErrorSubmitEdit(Throwable throwable) {
        String message = ErrorHandler.getErrorMessage(this, throwable);
        ToasterError.make(findViewById(android.R.id.content),
                message, BaseToaster.LENGTH_INDEFINITE)
                .setAction(getString(R.string.title_try_again), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSaveButtonClicked();
                    }
                }).show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putString(SAVED_IMAGE_PATH, savedLocalImageUrl);
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_shop_edit_schedule;
    }

}
