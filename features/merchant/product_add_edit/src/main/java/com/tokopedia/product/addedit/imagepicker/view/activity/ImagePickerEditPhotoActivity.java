package com.tokopedia.product.addedit.imagepicker.view.activity;

import android.content.Intent;
import android.os.Bundle;
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity;
import com.tokopedia.product.addedit.tracking.ProductAddEditImageTracking;
import com.tokopedia.product.addedit.tracking.ProductEditEditImageTracking;
import com.tokopedia.user.session.UserSession;
import static com.tokopedia.product.addedit.imagepicker.view.activity.ImagePickerAddProductActivity.IS_DRAFT;
import static com.tokopedia.product.addedit.imagepicker.view.activity.ImagePickerAddProductActivity.IS_EDIT;

public class ImagePickerEditPhotoActivity extends ImageEditorActivity {

    UserSession userSession;
    private static boolean isEditProduct = false;
    private static boolean isDraftProduct = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        isEditProduct = intent.getBooleanExtra(IS_EDIT, false);
        isDraftProduct = intent.getBooleanExtra(IS_DRAFT, false);
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
            ProductAddEditImageTracking.INSTANCE.trackContinue(userSession.getShopId());
        }
    }

    @Override
    public void trackBack() {
        if (isEditProduct) {
            ProductEditEditImageTracking.INSTANCE.trackBack(userSession.getShopId());
        } else {
            ProductAddEditImageTracking.INSTANCE.trackBack(userSession.getShopId());
        }
    }
}
