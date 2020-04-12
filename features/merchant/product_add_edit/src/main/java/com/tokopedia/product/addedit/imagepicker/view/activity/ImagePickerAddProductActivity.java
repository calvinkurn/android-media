package com.tokopedia.product.addedit.imagepicker.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel;
import com.tokopedia.product.addedit.tracking.ProductAddChooseImageTracking;
import com.tokopedia.product.addedit.tracking.ProductEditChooseImageTracking;
import com.tokopedia.user.session.UserSession;
import java.util.ArrayList;
import static com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.REQUEST_CODE_DETAIL;

public class ImagePickerAddProductActivity extends ImagePickerActivity {

    UserSession userSession;
    public static final String IS_EDIT = "is_edit";
    public static final String IS_DRAFT = "is_draft";
    public static final String PRODUCT_INPUT_MODEL = "product_input_model";
    private static boolean isEditProduct = false;
    private static boolean isDraftProduct = false;
    private static ProductInputModel productInputModel = new ProductInputModel();

    public static Intent getIntent(Context context, ImagePickerBuilder imagePickerBuilder, boolean isEditProduct, boolean isDraftProduct, ProductInputModel productInputModel) {
        Intent intent = new Intent(context, ImagePickerAddProductActivity.class);
        // https://stackoverflow.com/questions/28589509/android-e-parcel-class-not-found-when-unmarshalling-only-on-samsung-tab3
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_IMAGE_PICKER_BUILDER, imagePickerBuilder);
        intent.putExtra(EXTRA_IMAGE_PICKER_BUILDER, bundle);
        intent.putExtra(IS_EDIT, isEditProduct);
        intent.putExtra(IS_DRAFT,isDraftProduct);
        intent.putExtra(PRODUCT_INPUT_MODEL, productInputModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isEditProduct = getIntent().getBooleanExtra(IS_EDIT, false);
        isDraftProduct = getIntent().getBooleanExtra(IS_DRAFT, false);
        productInputModel = getIntent().getParcelableExtra(PRODUCT_INPUT_MODEL);
        super.onCreate(savedInstanceState);
        userSession = new UserSession(getContext());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null && requestCode == REQUEST_CODE_DETAIL) {
            setResult(Activity.RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void trackOpen() {
        if (!isEditProduct) {
            ProductAddChooseImageTracking.INSTANCE.trackScreen();
        }
    }

    @Override
    public void trackContinue() {
        if (isEditProduct) {
            ProductEditChooseImageTracking.INSTANCE.trackContinue(userSession.getShopId());
        } else {
            ProductAddChooseImageTracking.INSTANCE.trackContinue(userSession.getShopId());
        }
    }

    @Override
    public void trackBack() {
        if (isEditProduct) {
            ProductEditChooseImageTracking.INSTANCE.trackBack(userSession.getShopId());
        } else {
            ProductAddChooseImageTracking.INSTANCE.trackBack(userSession.getShopId());
        }
    }

    @Override
    protected Intent getEditorIntent(ArrayList<String> selectedImagePaths) {
        Intent targetIntent = new Intent(this, ImagePickerEditPhotoActivity.class);
        Intent origin = super.getEditorIntent(selectedImagePaths);
        targetIntent.putExtras(origin.getExtras());
        return targetIntent;
    }

    @Override
    protected void startEditorActivity(ArrayList<String> selectedImagePaths) {
        Intent intent = getEditorIntent(selectedImagePaths);
        intent.putExtra(IS_EDIT, isEditProduct);
        intent.putExtra(IS_DRAFT,isDraftProduct);
        intent.putExtra(PRODUCT_INPUT_MODEL, productInputModel);
        if (isEditProduct || isDraftProduct) {
            startActivityForResult(intent, REQUEST_CODE_EDITOR);
        } else {
            startActivityForResult(intent, REQUEST_CODE_DETAIL);
        }
    }
}
