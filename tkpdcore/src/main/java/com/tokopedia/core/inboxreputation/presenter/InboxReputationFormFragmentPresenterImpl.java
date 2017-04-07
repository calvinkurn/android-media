package com.tokopedia.core.inboxreputation.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.inboxreputation.activity.InboxReputationDetailActivity;
import com.tokopedia.core.inboxreputation.fragment.ImageUploadPreviewFragment;
import com.tokopedia.core.inboxreputation.fragment.InboxReputationFormFragment;
import com.tokopedia.core.inboxreputation.intentservice.InboxReviewIntentService;
import com.tokopedia.core.inboxreputation.interactor.ActReputationRetrofitInteractor;
import com.tokopedia.core.inboxreputation.interactor.ActReputationRetrofitInteractorImpl;
import com.tokopedia.core.inboxreputation.interactor.CacheInboxReputationInteractor;
import com.tokopedia.core.inboxreputation.interactor.CacheInboxReputationInteractorImpl;
import com.tokopedia.core.inboxreputation.listener.InboxReputationFormView;
import com.tokopedia.core.inboxreputation.model.ImageUpload;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetailItem;
import com.tokopedia.core.inboxreputation.model.param.ActReviewPass;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.var.FacebookContainer;

/**
 * Created by Nisie on 2/9/16.
 */
public class InboxReputationFormFragmentPresenterImpl
        implements InboxReputationFormFragmentPresenter {

    public static final String EXTRA_REVIEW_ID = "review_id";

    InboxReputationFormView viewListener;
    ActReputationRetrofitInteractor actReputationRetrofitInteractor;
    ImageUploadHandler imageUploadHandler;
    CacheInboxReputationInteractor cacheInboxReputationInteractor;
    InboxReputationFormFragment.DoActionReputationListener listener;

    public InboxReputationFormFragmentPresenterImpl(InboxReputationFormFragment viewListener) {
        this.viewListener = viewListener;
        this.actReputationRetrofitInteractor = new ActReputationRetrofitInteractorImpl();
        this.imageUploadHandler = ImageUploadHandler.createInstance(viewListener);
        this.cacheInboxReputationInteractor = new CacheInboxReputationInteractorImpl();
        this.listener = (InboxReputationFormFragment.DoActionReputationListener) viewListener.getActivity();
    }

    @Override
    public void postReview(final ActReviewPass param) {

        viewListener.showLoading();

        Bundle bundle = new Bundle();
        bundle.putParcelable(InboxReviewIntentService.PARAM_POST_REVIEW,
                param);
        listener.postReview(bundle);

    }

    @Override
    public void onImageUploadClicked(int imagePos) {
        cacheInboxReputationInteractor.setInboxReputationFormCache(viewListener.getInboxReputationDetail().getReviewId(), getActReviewPass());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ImageUploadHandler.REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK || resultCode == GalleryBrowser.RESULT_CODE) {
                Intent intent = new Intent(viewListener.getActivity(), InboxReputationDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("nav", ImageUploadPreviewFragment.NAV_UPLOAD_IMAGE);
                bundle.putString(EXTRA_REVIEW_ID, viewListener.getInboxReputationDetail().getReviewId());

                switch (resultCode) {
                    case GalleryBrowser.RESULT_CODE:
                        bundle.putString(ImageUploadHandler.FILELOC, data.getStringExtra(ImageGallery.EXTRA_URL));
                        break;
                    case Activity.RESULT_OK:
                        bundle.putString(ImageUploadHandler.FILELOC, imageUploadHandler.getCameraFileloc());
                        break;
                    default:
                        break;
                }
                intent.putExtras(bundle);
                viewListener.getActivity().startActivityForResult(intent,
                        ImageUploadHandler.CODE_UPLOAD_IMAGE);
            }
        } else if (requestCode == ImageUploadHandler.CODE_UPLOAD_IMAGE) {
            cacheInboxReputationInteractor.getInboxReputationFormCache(new CacheInboxReputationInteractor.GetInboxReputationFormCacheListener() {
                @Override
                public void onSuccess(ActReviewPass review) {
                    viewListener.setFormFromCache(review);
                }

                @Override
                public void onError(Throwable e) {

                }
            });

        }

    }

    @Override
    public void onImageClicked(int position, ImageUpload imageUpload) {
        cacheInboxReputationInteractor.setInboxReputationFormCache(viewListener.getInboxReputationDetail().getReviewId(), getActReviewPass());

        Intent intent = new Intent(viewListener.getActivity(), InboxReputationDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("nav", ImageUploadPreviewFragment.NAV_UPLOAD_IMAGE);
        bundle.putBoolean("is_update", true);
        bundle.putString(EXTRA_REVIEW_ID, viewListener.getInboxReputationDetail().getReviewId());
        bundle.putInt("position", position);
        intent.putExtras(bundle);
        viewListener.getActivity().startActivityForResult(intent,
                ImageUploadHandler.CODE_UPLOAD_IMAGE);
    }

    @Override
    public int getRatingDescription(float rating) {
        switch (String.valueOf(rating)) {
            case "1.0":
                return R.string.rating_title_1;
            case "2.0":
                return R.string.rating_title_2;
            case "3.0":
                return R.string.rating_title_3;
            case "4.0":
                return R.string.rating_title_4;
            case "5.0":
                return R.string.rating_title_5;
            default:
                return R.string.rating_title_0;
        }
    }

    @Override
    public void onSubmitReview() {
        viewListener.removeError();
        TrackingUtils.eventLoca(viewListener.getActivity().getString(R.string.event_submit_review));
        if (isValidForm()) {
            viewListener.setActionsEnabled(false);
            switch (viewListener.getActivity().getIntent().getExtras().getString("nav", "")) {
                case InboxReputationDetailActivity.NAV_EDIT_PRODUCT:
                    editReview(getActReviewPass());
                    break;
                default:
                    postReview(getActReviewPass());
                    break;
            }
            int accuracy = (int) Float.parseFloat(viewListener.getAccuracyRating());
            int quality = (int) Float.parseFloat(viewListener.getQualityRating());
            UnifyTracking.eventLocaGoodReview(accuracy, quality);
        }
    }

    @Override
    public ActReviewPass getActReviewPass() {
        ActReviewPass param = new ActReviewPass();
        param.setReviewId(viewListener.getInboxReputationDetail().getReviewId());
        param.setAccuracyRate(viewListener.getAccuracyRating());
        param.setProductId(viewListener.getInboxReputationDetail().getProductId());
        param.setQualityRate(viewListener.getQualityRating());
        param.setReputationId(viewListener.getInboxReputation().getReputationId());
        param.setReviewMessage(viewListener.getMessageReview());
        param.setShopId(viewListener.getInboxReputationDetail().getShopId());
        param.setImageUploads(viewListener.getAdapter().getList());
        param.setToken(viewListener.getToken());
        param.setDeletedImageUploads(viewListener.getAdapter().getDeletedList());
        return param;

    }

    @Override
    public void onDestroyView() {
        actReputationRetrofitInteractor.unSubscribeObservable();
    }

    @Override
    public void openImageGallery() {
        imageUploadHandler.actionImagePicker();
    }

    @Override
    public void openCamera() {
        imageUploadHandler.actionCamera();
    }

    @Override
    public void doFacebookLogin(InboxReputationFormFragment fragment, CallbackManager callbackManager) {
        LoginManager.getInstance().logInWithPublishPermissions(fragment, FacebookContainer.writePermissions);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
            }

            @Override
            public void onCancel() {
                viewListener.unTickCheckBox();
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException e) {
                viewListener.unTickCheckBox();
                if(e instanceof FacebookAuthorizationException){
                    LoginManager.getInstance().logOut();
                }
            }
        });
    }

    @Override
    public void prepareDialogShareFb(InboxReputationFormFragment fragment, ShareDialog shareDialog
            , CallbackManager callbackManager, InboxReputationDetailItem inboxReputationDetail
            , String stringDomain, String contentDescription, final Intent intent) {
        final ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle(inboxReputationDetail.getProductName())
                .setImageUrl(Uri.parse(inboxReputationDetail.getProductImageUrl()))
                .setContentUrl(Uri.parse(stringDomain+inboxReputationDetail.getProductUri()))
                .setContentDescription(contentDescription)
                .build();

        LoginManager.getInstance().logInWithPublishPermissions(fragment, FacebookContainer.writePermissions);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                ShareApi.share(linkContent, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        viewListener.onSuccessSharingFacebook(intent);
                    }
                    @Override
                    public void onCancel() {
                        viewListener.onCancelSharingFacebook(intent);
                    }
                    @Override
                    public void onError(FacebookException error) {
                        Log.i("facebook", "onError: "+error);
                        viewListener.onErrorSharingFacebook(intent);
                    }
                });
            }

            @Override
            public void onCancel() {
                LoginManager.getInstance().logOut();
                viewListener.onCancelSharingFacebook(intent);
            }

            @Override
            public void onError(FacebookException e) {
                if(e instanceof FacebookAuthorizationException){
                    LoginManager.getInstance().logOut();
                }
                viewListener.onErrorSharingFacebook(intent);
            }
        });
    }


    private boolean isValidForm() {
        boolean isValid = true;

        if (isMessageEmpty()) {
            viewListener.showMessageReviewError(R.string.error_review_message_empty);
            isValid = false;
        } else if (isMessageInvalid()) {
            viewListener.showMessageReviewError(R.string.error_review_message);
            isValid = false;
        }

        if (isRatingQualityEmpty()) {
            viewListener.showRatingQualityError();
            isValid = false;
        }

        if (isRatingAccuracyEmpty()) {
            viewListener.showRatingAccuracyError();
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void editReview(final ActReviewPass param) {
        viewListener.showLoading();

        Bundle bundle = new Bundle();
        bundle.putParcelable(InboxReviewIntentService.PARAM_EDIT_REVIEW,
                param);
        listener.editReview(bundle);

    }


    private boolean isRatingQualityEmpty() {
        return viewListener.getQualityRating().equals("0.0");
    }

    private boolean isRatingAccuracyEmpty() {
        return viewListener.getAccuracyRating().equals("0.0");
    }

    private boolean isMessageEmpty() {
        return viewListener.getMessageReview().trim().length() == 0;
    }

    private boolean isMessageInvalid() {
        return viewListener.getMessageReview().length() < 30;
    }


}
