package com.tokopedia.kol.feature.createpost.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.kol.R;

import java.util.ArrayList;

import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_BRIGHTNESS;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CONTRAST;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CROP;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_ROTATE;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;

/**
 * @author by yfsx on 08/06/18.
 */
public class CreatePostImagePickerActivity extends ImagePickerActivity {

    private static final int CREATE_FORM_REQUEST = 1234;

    public static Intent getInstance(Context context, String urlForm) {
        ImagePickerBuilder builder = new ImagePickerBuilder(context.getString(R.string.title_post),
                new int[]{TYPE_GALLERY, TYPE_CAMERA}, GalleryType.IMAGE_ONLY, DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.RATIO_1_1, true,
                new ImagePickerEditorBuilder(
                        new int[]{ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE},
                        false,
                        null)
                ,null);
        Intent intent = new Intent(context, CreatePostImagePickerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_IMAGE_PICKER_BUILDER, builder);
        intent.putExtra(EXTRA_IMAGE_PICKER_BUILDER, bundle);
        intent.putExtra(CreatePostActivity.FORM_URL, urlForm);
        return intent;
    }

    @Override
    protected void startEditorActivity(ArrayList<String> selectedImagePaths) {
        Intent intent = getEditorIntent(selectedImagePaths);
        startActivityForResult(intent, CREATE_FORM_REQUEST);
    }

    @Override
    protected Intent getEditorIntent(ArrayList<String> selectedImagePaths){
        String formUrl = "";
        if (getIntent().getExtras() != null) {
            formUrl = getIntent().getExtras().getString(CreatePostActivity.FORM_URL);
        } else if (getIntent().getData() != null) {
            formUrl = getIntent().getData().getLastPathSegment();
        }
        return CreatePostImageEditorActivity.getInstance(this, selectedImagePaths, imageDescriptionList,
                imagePickerBuilder.getMinResolution(), imagePickerBuilder.getImageEditActionType(),
                imagePickerBuilder.getImageRatioTypeDef(),
                imagePickerBuilder.isCirclePreview(),
                imagePickerBuilder.getMaxFileSizeInKB(),
                imagePickerBuilder.getRatioOptionList(),
                formUrl);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_FORM_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        }
    }
}
