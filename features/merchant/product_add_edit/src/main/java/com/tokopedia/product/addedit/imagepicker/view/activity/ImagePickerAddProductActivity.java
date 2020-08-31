package com.tokopedia.product.addedit.imagepicker.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.product.addedit.R;
import com.tokopedia.product.addedit.tracking.ProductAddChooseImageTracking;
import com.tokopedia.product.addedit.tracking.ProductEditChooseImageTracking;
import com.tokopedia.user.session.UserSession;
import java.util.ArrayList;

public class ImagePickerAddProductActivity extends ImagePickerActivity {

    UserSession userSession;
    private static boolean isEditProduct = false;
    private static boolean isAddProduct = false;
    public static final String IS_EDIT = "is_edit";
    public static final String IS_ADD = "is_add";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isEditProduct = getIntent().getBooleanExtra(IS_EDIT, false);
        isAddProduct = getIntent().getBooleanExtra(IS_ADD, false);
        super.onCreate(savedInstanceState);
        userSession = new UserSession(getContext());
        // change the word selanjutnya to lanjut
        TextView tvDone = findViewById(com.tokopedia.imagepicker.R.id.tv_done);
        tvDone.setText(getString(R.string.action_continue));
    }

    public static Intent getIntent(Context context, ImagePickerBuilder imagePickerBuilder, boolean isEditProduct, boolean isAddProduct) {
        Intent intent = new Intent(context, ImagePickerAddProductActivity.class);
        // https://stackoverflow.com/questions/28589509/android-e-parcel-class-not-found-when-unmarshalling-only-on-samsung-tab3
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_IMAGE_PICKER_BUILDER, imagePickerBuilder);
        intent.putExtra(EXTRA_IMAGE_PICKER_BUILDER, bundle);
        intent.putExtra(IS_EDIT, isEditProduct);
        intent.putExtra(IS_ADD, isAddProduct);
        return intent;
    }

    @Override
    public void trackOpen() {
        if (isAddProduct || !isEditProduct) {
            ProductAddChooseImageTracking.INSTANCE.trackScreen();
        }
    }

    @Override
    public void trackContinue() { }

    @Override
    public void trackBack() {
        if (isAddProduct || !isEditProduct) {
            ProductAddChooseImageTracking.INSTANCE.trackBack(userSession.getShopId());
        } else {
            ProductEditChooseImageTracking.INSTANCE.trackBack(userSession.getShopId());
        }
    }

    @Override
    protected Intent getEditorIntent(ArrayList<String> selectedImagePaths) {
        if (isAddProduct || !isEditProduct) {
            ProductAddChooseImageTracking.INSTANCE.trackContinue(userSession.getShopId());
        } else {
            ProductEditChooseImageTracking.INSTANCE.trackContinue(userSession.getShopId());
        }

        Intent targetIntent = new Intent(this, ImagePickerEditPhotoActivity.class);
        Intent origin = super.getEditorIntent(selectedImagePaths);
        targetIntent.putExtras(origin.getExtras());
        targetIntent.putExtra(IS_EDIT, isEditProduct);
        targetIntent.putExtra(IS_ADD, isAddProduct);
        return targetIntent;
    }
}
