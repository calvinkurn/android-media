package com.tokopedia.kol.feature.createpost.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;

import com.tokopedia.kol.R;
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
public class ImageUploadActivity extends ImagePickerActivity {

    public static Intent getInstance(Context context) {
        ImagePickerBuilder builder = new ImagePickerBuilder(getString(R.string.title_post),
                new int[]{TYPE_GALLERY, TYPE_CAMERA}, GalleryType.IMAGE_ONLY, DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                DEFAULT_MIN_RESOLUTION, 1, 1, true,
                new ImagePickerEditorBuilder(new int[]{ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE},
                        false)
                , null);
        return ImagePickerActivity.getIntent(context, builder);
    }
}
