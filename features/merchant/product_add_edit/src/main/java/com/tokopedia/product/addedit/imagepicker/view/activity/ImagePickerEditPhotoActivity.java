package com.tokopedia.product.addedit.imagepicker.view.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity;
import com.tokopedia.product.addedit.R;
import com.tokopedia.product.addedit.tracking.ProductAddEditImageTracking;
import com.tokopedia.product.addedit.tracking.ProductEditEditImageTracking;
import com.tokopedia.user.session.UserSession;


public class ImagePickerEditPhotoActivity extends ImageEditorActivity {

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

    @Override
    public void trackOpen() {
        if (isAddProduct || !isEditProduct) {
            ProductAddEditImageTracking.INSTANCE.trackScreen();
        }
    }

    @Override
    public void trackContinue() {
        if (isAddProduct || !isEditProduct) {
            ProductAddEditImageTracking.INSTANCE.trackEditContinue(userSession.getShopId());
        } else {
            ProductEditEditImageTracking.INSTANCE.trackContinue(userSession.getShopId());
        }
    }

    @Override
    public void trackBack() {
        if (isAddProduct || !isEditProduct) {
            ProductAddEditImageTracking.INSTANCE.trackEditBack(userSession.getShopId());
        } else {
            ProductEditEditImageTracking.INSTANCE.trackBack(userSession.getShopId());
        }
    }
}
