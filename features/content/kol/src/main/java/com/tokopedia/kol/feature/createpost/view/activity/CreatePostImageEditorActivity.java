package com.tokopedia.kol.feature.createpost.view.activity;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.imagepicker.common.util.ImageUtils;
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity;
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;

import java.util.ArrayList;

import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.RESULT_IMAGE_DESCRIPTION_LIST;

/**
 * @author by yfsx on 20/06/18.
 */
public class CreatePostImageEditorActivity extends ImageEditorActivity {

    private static final int CREATE_FORM_REQUEST = 1234;

    public static Intent getInstance(Context context, ArrayList<String> imageUrls,
                                   ArrayList<String> imageDescription,
                                   int minResolution,
                                   @ImageEditActionTypeDef int[] imageEditActionType,
                                   ImageRatioTypeDef defaultRatio,
                                   boolean isCirclePreview,
                                   long maxFileSize,
                                   ArrayList<ImageRatioTypeDef> ratioOptionList) {
        Intent intent = new Intent(context, CreatePostImageEditorActivity.class);
        intent.putExtra(EXTRA_IMAGE_URLS, imageUrls);
        intent.putExtra(EXTRA_IMAGE_DESCRIPTION_LIST, imageDescription);
        intent.putExtra(EXTRA_MIN_RESOLUTION, minResolution);
        intent.putExtra(EXTRA_EDIT_ACTION_TYPE, imageEditActionType);
        intent.putExtra(EXTRA_RATIO, defaultRatio);
        intent.putExtra(EXTRA_IS_CIRCLE_PREVIEW, isCirclePreview);
        intent.putExtra(EXTRA_MAX_FILE_SIZE, maxFileSize);
        intent.putExtra(EXTRA_RATIO_OPTION_LIST, ratioOptionList);
        return intent;
    }

    protected void onFinishEditingImage(ArrayList<String> imageUrlOrPathList) {
        ImageUtils.deleteCacheFolder(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE);
        ImageUtils.deleteCacheFolder(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA);
        Intent intent = getFinishIntent(imageUrlOrPathList);
        startActivityForResult(intent, CREATE_FORM_REQUEST);
    }

    protected Intent getFinishIntent(ArrayList<String> imageUrlOrPathList){
        Intent intent = CreatePostActivity.getInstanceWebView(this,"");
        intent.putStringArrayListExtra(PICKER_RESULT_PATHS, imageUrlOrPathList);
        intent.putStringArrayListExtra(RESULT_PREVIOUS_IMAGE, extraImageUrls);
        intent.putStringArrayListExtra(RESULT_IMAGE_DESCRIPTION_LIST, imageDescriptionList);
        intent.putExtra(RESULT_IS_EDITTED, isEdittedList);
        return intent;
    }
}
