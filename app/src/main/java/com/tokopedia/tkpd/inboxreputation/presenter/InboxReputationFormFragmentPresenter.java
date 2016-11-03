package com.tokopedia.tkpd.inboxreputation.presenter;

import android.content.Intent;

import com.tokopedia.tkpd.inboxreputation.model.ImageUpload;
import com.tokopedia.tkpd.inboxreputation.model.param.ActReviewPass;

import java.util.Map;

/**
 * Created by Nisie on 2/9/16.
 */
public interface InboxReputationFormFragmentPresenter {

    void postReview(ActReviewPass param);

    void editReview(ActReviewPass param);

    int getRatingDescription(float rating);

    void onSubmitReview();

    void onImageUploadClicked(int imagePos);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onImageClicked(int position, ImageUpload imageUpload);

    ActReviewPass getActReviewPass();

    void onDestroyView();

    void openImageGallery();

    void openCamera();
}
