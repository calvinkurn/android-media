package com.tokopedia.shop.settings.basicinfo.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.design.text.watcher.AfterTextWatcher;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel;
import com.tokopedia.shop.settings.R;
import com.tokopedia.shop.settings.basicinfo.view.presenter.UpdateShopSettingsInfoPresenter;
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_BRIGHTNESS;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CONTRAST;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CROP;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_ROTATE;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;

public class ShopEditBasicInfoActivity extends BaseSimpleActivity implements UpdateShopSettingsInfoPresenter.View {

    public static final String SAVED_IMAGE_PATH = "saved_img_path";
    private static final int MAX_FILE_SIZE_IN_KB = 10240;
    private static final int REQUEST_CODE_IMAGE = 846;

    @Inject
    UpdateShopSettingsInfoPresenter updateShopSettingsInfoPresenter;

    private TextView tvSave;
    private ImageView ivLogo;
    private TextView tvBrowseFile;
    private TkpdHintTextInputLayout tilShopSlogan;
    private EditText etShopSlogan;
    private TkpdHintTextInputLayout tilShopDesc;
    private EditText etShopDesc;
    private ShopBasicDataModel shopBasicDataModel;
    private ProgressDialog progressDialog;

    private String savedLocalImageUrl;
    private boolean needUpdatePhotoUI;
    private View vgRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GraphqlClient.init(this);
        if (savedInstanceState != null) {
            savedLocalImageUrl = savedInstanceState.getString(SAVED_IMAGE_PATH);
        }
        super.onCreate(savedInstanceState);

        DaggerShopSettingsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        updateShopSettingsInfoPresenter.attachView(this);

        vgRoot = findViewById(R.id.vgRoot);
        tvSave = findViewById(R.id.tvSave);
        ivLogo = findViewById(R.id.ivLogo);
        tvBrowseFile = findViewById(R.id.tvBrowseFile);
        tilShopSlogan = findViewById(R.id.tilShopSlogan);
        etShopSlogan = findViewById(R.id.etShopSlogan);
        etShopSlogan.addTextChangedListener(new AfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                tilShopSlogan.setError(null);
            }
        });
        tilShopDesc = findViewById(R.id.tilShopDesc);
        etShopDesc = findViewById(R.id.etShopDesc);
        etShopDesc.addTextChangedListener(new AfterTextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                tilShopDesc.setError(null);
            }
        });
        View.OnClickListener OnBrowseClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        };
        ivLogo.setOnClickListener(OnBrowseClickListener);
        tvBrowseFile.setOnClickListener(OnBrowseClickListener);

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });
        vgRoot.requestFocus();

        loadShopBasicData();
    }

    private void onSaveButtonClicked(){
        if (isInputInvalid()) {
            return;
        }
        showSubmitLoading(getString(R.string.title_loading));
        String tagLine = etShopSlogan.getText().toString();
        String desc = etShopDesc.getText().toString();
        if (!TextUtils.isEmpty(savedLocalImageUrl)) {
            updateShopSettingsInfoPresenter.uploadShopImage(savedLocalImageUrl,tagLine, desc);
        } else {
            updateShopSettingsInfoPresenter.updateShopBasicData(tagLine, desc);
        }
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

    private boolean isInputInvalid(){
        boolean hasError = false;
        String tagLine = etShopSlogan.getText().toString();
        if (TextUtils.isEmpty(tagLine)) {
            tilShopSlogan.setError(getString(R.string.shop_slogan_must_be_filled));
            hasError = true;
        }
        String desc = etShopDesc.getText().toString();
        if (TextUtils.isEmpty(desc)) {
            tilShopDesc.setError(getString(R.string.shop_desc_must_be_filled));
            hasError = true;
        }
        return hasError;
    }

    private void loadShopBasicData() {
        updateShopSettingsInfoPresenter.getShopBasicData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needUpdatePhotoUI) {
            updatePhotoUI(shopBasicDataModel);
            needUpdatePhotoUI = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (updateShopSettingsInfoPresenter != null) {
            updateShopSettingsInfoPresenter.detachView();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
            if (imageUrlOrPathList != null && imageUrlOrPathList.size() > 0) {
                savedLocalImageUrl = imageUrlOrPathList.get(0);
            }
            needUpdatePhotoUI = true;
        }
    }

    private void openImagePicker() {
        ImagePickerBuilder builder = new ImagePickerBuilder(getString(R.string.choose_shop_picture),
                new int[]{TYPE_GALLERY, TYPE_CAMERA}, GalleryType.IMAGE_ONLY, MAX_FILE_SIZE_IN_KB,
                DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.RATIO_1_1, true,
                new ImagePickerEditorBuilder(
                        new int[]{ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE},
                        false,
                        null)
                ,null);
        Intent intent = ImagePickerActivity.getIntent(this, builder);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
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
    public void onSuccessGetShopBasicData(ShopBasicDataModel shopBasicDataModel) {
        this.shopBasicDataModel = shopBasicDataModel;
        setUIShopBasicData(shopBasicDataModel);
        tvSave.setVisibility(View.VISIBLE);
    }

    private void setUIShopBasicData(ShopBasicDataModel shopBasicDataModel) {
        updatePhotoUI(shopBasicDataModel);
        etShopSlogan.setText(shopBasicDataModel.getTagline());
        etShopSlogan.setSelection(etShopSlogan.getText().length());

        etShopDesc.setText(shopBasicDataModel.getDescription());
        etShopDesc.setSelection(etShopDesc.getText().length());
    }

    private void updatePhotoUI(ShopBasicDataModel shopBasicDataModel){
        if (TextUtils.isEmpty(savedLocalImageUrl)) {
            String logoUrl = shopBasicDataModel.getLogo();
            if (TextUtils.isEmpty(logoUrl)) {
                ivLogo.setImageResource(R.drawable.ic_camera_add);
            } else {
                ImageHandler.LoadImage(ivLogo, logoUrl);
            }
        } else {
            ImageHandler.LoadImage(ivLogo, savedLocalImageUrl);
        }
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
        outState.putString(SAVED_IMAGE_PATH, savedLocalImageUrl);
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_shop_edit_basic_info;
    }

}
