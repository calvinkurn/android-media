package com.tokopedia.product.manage.item.imagepicker.imagepickerbuilder;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerMultipleSelectionBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.product.manage.item.R;
import com.tokopedia.product.manage.item.imagepicker.view.activity.ImagePickerAddProductActivity;

import java.util.ArrayList;

/**
 * Created by hendry on 07/06/18.
 */

public class AddProductImagePickerBuilder {
    public static final int MAX_IMAGE_LIMIT = 5;
    public static final int INSTAGRAM_IMAGE_LIMIT = 20;

    public static ImagePickerBuilder createPrimaryNewBuilder(Context context, ArrayList<String> imageList) {
        ArrayList<Integer> placeholderDrawableRes = new ArrayList<>();
        placeholderDrawableRes.add(R.drawable.ic_utama);
        placeholderDrawableRes.add(R.drawable.ic_depan);
        placeholderDrawableRes.add(R.drawable.ic_samping);
        placeholderDrawableRes.add(R.drawable.ic_atas);
        placeholderDrawableRes.add(R.drawable.ic_detail);
        return new ImagePickerBuilder(context.getString(R.string.choose_image),
                new int[]{ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_CAMERA}, GalleryType.IMAGE_ONLY, ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.RATIO_1_1, true,
                new ImagePickerEditorBuilder(new int[]{ImageEditActionTypeDef.ACTION_BRIGHTNESS, ImageEditActionTypeDef.ACTION_CONTRAST, ImageEditActionTypeDef.ACTION_CROP, ImageEditActionTypeDef.ACTION_ROTATE},
                        false,
                        null)
                , new ImagePickerMultipleSelectionBuilder(
                imageList,
                placeholderDrawableRes,
                R.string.primary,
                MAX_IMAGE_LIMIT, true,
                ImagePickerMultipleSelectionBuilder.PreviewExtension.createPreview()));
    }

    public static ImagePickerBuilder createInstagramImportBuilder(Context context) {
        return new ImagePickerBuilder(context.getString(R.string.choose_image),
                new int[]{ImagePickerTabTypeDef.TYPE_INSTAGRAM}, GalleryType.IMAGE_ONLY, ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.RATIO_1_1, true,
                new ImagePickerEditorBuilder(new int[]{ImageEditActionTypeDef.ACTION_BRIGHTNESS, ImageEditActionTypeDef.ACTION_CONTRAST, ImageEditActionTypeDef.ACTION_CROP, ImageEditActionTypeDef.ACTION_ROTATE},
                        false,
                        null)
                , new ImagePickerMultipleSelectionBuilder(
                null,
                null,
                0,
                INSTAGRAM_IMAGE_LIMIT, true));
    }

    public static ImagePickerBuilder createVariantNewBuilder(Context context) {
        return new ImagePickerBuilder(context.getString(R.string.choose_image),
                new int[]{ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_CAMERA}, GalleryType.IMAGE_ONLY, ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.RATIO_1_1, true,
                new ImagePickerEditorBuilder(new int[]{ImageEditActionTypeDef.ACTION_BRIGHTNESS, ImageEditActionTypeDef.ACTION_CONTRAST, ImageEditActionTypeDef.ACTION_CROP, ImageEditActionTypeDef.ACTION_ROTATE},
                        false,
                        null)
                , null);
    }

    public static Intent createPickerIntentPrimary(Context context, ArrayList<String> imageList) {
        ImagePickerBuilder builder = AddProductImagePickerBuilder.createPrimaryNewBuilder(context, imageList);
        return ImagePickerActivity.getIntent(context, builder);
    }

    public static Intent createPickerIntentWithCatalog(Context context, ArrayList<String> imageList, String catalogId) {
        ImagePickerBuilder builder = AddProductImagePickerBuilder.createPrimaryNewBuilder(context, imageList);
        return ImagePickerAddProductActivity.getIntent(context, builder, catalogId);
    }

    public static Intent createPickerIntentInstagramImport(Context context) {
        ImagePickerBuilder builder = AddProductImagePickerBuilder.createInstagramImportBuilder(context);
        return ImagePickerActivity.getIntent(context, builder);
    }

    public static Intent createPickerIntentVariant(Context context) {
        ImagePickerBuilder builder = AddProductImagePickerBuilder.createVariantNewBuilder(context);
        return ImagePickerActivity.getIntent(context, builder);
    }

    public static Intent createEditorIntent(Context context, String uriOrPath) {
        return ImageEditorActivity.getIntent(context, uriOrPath, null,
                ImagePickerBuilder.DEFAULT_MIN_RESOLUTION,
                new int[]{ImageEditActionTypeDef.ACTION_BRIGHTNESS, ImageEditActionTypeDef.ACTION_CONTRAST, ImageEditActionTypeDef.ACTION_CROP, ImageEditActionTypeDef.ACTION_ROTATE},
                ImageRatioTypeDef.RATIO_1_1, false,
                ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                null);
    }
}
