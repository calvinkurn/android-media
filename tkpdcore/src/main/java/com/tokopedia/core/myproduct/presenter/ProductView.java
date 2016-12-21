package com.tokopedia.core.myproduct.presenter;

import com.tokopedia.core.myproduct.model.SimpleTextModel;

import java.util.List;

/**
 * Created by noiz354 on 5/13/16.
 * presenter of {@link com.tokopedia.core.myproduct.ProductActivity}
 */
public interface ProductView {
    String TWITTER_DIALOG_V_4 = "TwitterDialogV4";
//    String TAG = "MNORMANSYAH";
    String IMAGE_URL = "image_url";
    String PLEASE_DISABLE_DON_T_KEEP_ACTIVITIES = "please disable don't keep activities";

    void showPopup(int type, String title, List<SimpleTextModel> simpleTextModels);
    void WarningDialog();
    void showTwitterDialog();
}
