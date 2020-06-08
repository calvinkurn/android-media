package com.tokopedia.product.addedit.imagepicker.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity;
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.user.session.UserSession;


public class SizechartPickerEditPhotoActivity extends ImageEditorActivity {

    UserSession userSession;

    public static Intent createIntent(Context context, String uriOrPath) {
        return ImageEditorActivity.getIntent(context, uriOrPath,
                null,
                ImagePickerBuilder.DEFAULT_MIN_RESOLUTION,
                new int[]{ImageEditActionTypeDef.ACTION_BRIGHTNESS,
                        ImageEditActionTypeDef.ACTION_CONTRAST,
                        ImageEditActionTypeDef.ACTION_CROP,
                        ImageEditActionTypeDef.ACTION_ROTATE},
                ImageRatioTypeDef.RATIO_1_1, false,
                ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSession = new UserSession(getContext());
    }

    @Override
    public void trackOpen() {
        // TODO faisalramd implement tracker
    }

    @Override
    public void trackContinue() {
        // TODO faisalramd implement tracker
    }

    @Override
    public void trackBack() {
        // TODO faisalramd implement tracker
    }
}
