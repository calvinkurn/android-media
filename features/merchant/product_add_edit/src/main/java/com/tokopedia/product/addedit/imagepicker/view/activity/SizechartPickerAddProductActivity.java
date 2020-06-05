package com.tokopedia.product.addedit.imagepicker.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.product.addedit.R;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder;

import java.util.ArrayList;

public class SizechartPickerAddProductActivity extends ImagePickerActivity {

    UserSession userSession;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, SizechartPickerAddProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_IMAGE_PICKER_BUILDER, createSizechartImagePickerBuilder(context));
        intent.putExtra(EXTRA_IMAGE_PICKER_BUILDER, bundle);
        return intent;
    }

    private static ImagePickerBuilder createSizechartImagePickerBuilder(Context context)  {
        ImagePickerEditorBuilder imagePickerEditorBuilder = new ImagePickerEditorBuilder(
                new int[]{ImageEditActionTypeDef.ACTION_BRIGHTNESS,
                        ImageEditActionTypeDef.ACTION_CONTRAST,
                        ImageEditActionTypeDef.ACTION_CROP,
                        ImageEditActionTypeDef.ACTION_ROTATE},
                false,
                null);
        return new ImagePickerBuilder(context.getString(R.string.choose_image),
                new int[]{ImagePickerTabTypeDef.TYPE_GALLERY,
                        ImagePickerTabTypeDef.TYPE_CAMERA,
                        ImagePickerTabTypeDef.TYPE_INSTAGRAM},
                GalleryType.IMAGE_ONLY,
                ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                ImagePickerBuilder.DEFAULT_MIN_RESOLUTION,
                ImageRatioTypeDef.RATIO_1_1,
                true,
                imagePickerEditorBuilder,
                null);
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

    @Override
    protected Intent getEditorIntent(ArrayList<String> selectedImagePaths) {
        Intent targetIntent = new Intent(this, SizechartPickerEditPhotoActivity.class);
        Intent origin = super.getEditorIntent(selectedImagePaths);
        if (origin.getExtras() != null) {
            targetIntent.putExtras(origin.getExtras());
        }
        return targetIntent;
    }
}
