package com.tokopedia.product.addedit.imagepicker.view.activity;

import android.os.Bundle;

import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity;
import com.tokopedia.product.addedit.tracking.ProductAddEditImageTracking;
import com.tokopedia.product.addedit.tracking.ProductEditEditImageTracking;
import com.tokopedia.user.session.UserSession;


public class ImagePickerEditPhotoActivity extends ImageEditorActivity {

    UserSession userSession;
    private static boolean isEditProduct = false;
    public static final String IS_EDIT = "is_edit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isEditProduct = getIntent().getBooleanExtra(IS_EDIT, false);
        super.onCreate(savedInstanceState);
        userSession = new UserSession(getContext());
    }

    @Override
    public void trackOpen() {
        if (!isEditProduct) {
            ProductAddEditImageTracking.INSTANCE.trackScreen();
        }
    }

    @Override
    public void trackContinue() {
        if (isEditProduct) {
            ProductEditEditImageTracking.INSTANCE.trackContinue(userSession.getShopId());
        } else {
            ProductAddEditImageTracking.INSTANCE.trackEditContinue(userSession.getShopId());
        }
    }

    @Override
    public void trackBack() {
        if (isEditProduct) {
            ProductEditEditImageTracking.INSTANCE.trackBack(userSession.getShopId());
        } else {
            ProductAddEditImageTracking.INSTANCE.trackEditBack(userSession.getShopId());
        }
    }

}
