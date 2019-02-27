package com.tokopedia.affiliate.feature.createpost.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.affiliate.R;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerMultipleSelectionBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;

import java.util.ArrayList;

import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef
        .ACTION_BRIGHTNESS;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CONTRAST;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CROP;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_ROTATE;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
        .DEFAULT_MAX_IMAGE_SIZE_IN_KB;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
        .DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;

/**
 * @author by yfsx on 26/09/18.
 */
public class CreatePostImagePickerActivity extends ImagePickerActivity {

    public static Intent getInstance(Context context, ArrayList<String> selectedImageList,
                                     int maxImage) {
        ImagePickerBuilder builder = new ImagePickerBuilder(
                context.getString(R.string.title_af_choose_photo),
                new int[]{TYPE_GALLERY, TYPE_CAMERA},
                GalleryType.IMAGE_ONLY,
                DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                DEFAULT_MIN_RESOLUTION,
                ImageRatioTypeDef.RATIO_1_1,
                true,
                new ImagePickerEditorBuilder(
                        new int[]{ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE},
                        false,
                        null)
                , new ImagePickerMultipleSelectionBuilder(
                selectedImageList,
                null,
                0,
                maxImage));
        Intent intent = new Intent(context, CreatePostImagePickerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_IMAGE_PICKER_BUILDER, builder);
        intent.putExtra(EXTRA_IMAGE_PICKER_BUILDER, bundle);
        return intent;
    }

    @Override
    protected void startEditorActivity(ArrayList<String> selectedImagePaths) {
        Intent intent = getEditorIntent(selectedImagePaths);
        startActivityForResult(intent, REQUEST_CODE_EDITOR);
    }

    @Override
    protected Intent getEditorIntent(ArrayList<String> selectedImagePaths) {
        return CreatePostImageEditorActivity.getInstance(this, selectedImagePaths,
                imageDescriptionList,
                imagePickerBuilder.getMinResolution(), imagePickerBuilder.getImageEditActionType(),
                imagePickerBuilder.getImageRatioTypeDef(),
                imagePickerBuilder.isCirclePreview(),
                imagePickerBuilder.getMaxFileSizeInKB(),
                imagePickerBuilder.getRatioOptionList());
    }

    //TODO milhamj
    @Override
    protected void onDoneClicked() {
        super.onDoneClicked();
    }
}
