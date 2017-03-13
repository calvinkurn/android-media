package com.tokopedia.core.inboxreputation.presenter;

import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.share.widget.ShareDialog;
import com.tokopedia.core.inboxreputation.fragment.InboxReputationFormFragment;
import com.tokopedia.core.inboxreputation.model.ImageUpload;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetailItem;
import com.tokopedia.core.inboxreputation.model.param.ActReviewPass;

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

    void doFacebookLogin(InboxReputationFormFragment inboxReputationFormFragment, CallbackManager callbackManager);

    void prepareDialogShareFb(InboxReputationFormFragment fragment, ShareDialog shareDialog, CallbackManager callbackManager, InboxReputationDetailItem inboxReputationDetail, String stringDomain, String contentDescription, Intent intent);
}
