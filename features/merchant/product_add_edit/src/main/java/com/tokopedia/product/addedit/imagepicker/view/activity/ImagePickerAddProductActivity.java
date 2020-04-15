package com.tokopedia.product.addedit.imagepicker.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.product.addedit.tracking.ProductAddChooseImageTracking;
import com.tokopedia.product.addedit.tracking.ProductEditChooseImageTracking;
import com.tokopedia.user.session.UserSession;
import java.util.ArrayList;

public class ImagePickerAddProductActivity extends ImagePickerActivity {

    UserSession userSession;
    private static boolean isEditProduct = false;
    public static final String IS_EDIT = "is_edit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isEditProduct = getIntent().getBooleanExtra(IS_EDIT, false);
        super.onCreate(savedInstanceState);
        userSession = new UserSession(getContext());
    }

    public static Intent getIntent(Context context, ImagePickerBuilder imagePickerBuilder, boolean isEditProduct) {
        Intent intent = new Intent(context, ImagePickerAddProductActivity.class);
        // https://stackoverflow.com/questions/28589509/android-e-parcel-class-not-found-when-unmarshalling-only-on-samsung-tab3
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_IMAGE_PICKER_BUILDER, imagePickerBuilder);
        intent.putExtra(EXTRA_IMAGE_PICKER_BUILDER, bundle);
        intent.putExtra(IS_EDIT, isEditProduct);
        return intent;
    }

    @Override
    public void trackOpen() {
        if (!isEditProduct) {
            ProductAddChooseImageTracking.INSTANCE.trackScreen();
        }
    }

    @Override
    public void trackContinue() { }

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
        if (isEditProduct) {
            ProductEditChooseImageTracking.INSTANCE.trackContinue(userSession.getShopId());
        } else {
            ProductAddChooseImageTracking.INSTANCE.trackContinue(userSession.getShopId());
        }

        Intent targetIntent = new Intent(this, ImagePickerEditPhotoActivity.class);
        Intent origin = super.getEditorIntent(selectedImagePaths);
        targetIntent.putExtras(origin.getExtras());
        targetIntent.putExtra(IS_EDIT, isEditProduct);
        return targetIntent;
    }
}
