package com.tokopedia.affiliate.feature.createpost.view.activity;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity;
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;

import java.util.ArrayList;

/**
 * @author by yfsx on 26/09/18.
 */
public class CreatePostImageEditorActivity extends ImageEditorActivity {

    private static final String DEFAULT_RESOLUTION = "100-square";
    private static final String RESOLUTION_300 = "300";
    private static final String PARAM_ID = "id";
    private static final String PARAM_WEB_SERVICE = "web_service";
    private static final String PARAM_RESOLUTION = "param_resolution";
    private static final String DEFAULT_UPLOAD_PATH = "/upload/attachment";
    private static final String DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg";

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

}
