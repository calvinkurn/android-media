package com.tokopedia.core.inboxreputation.activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.tokopedia.core.R;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.inboxreputation.fragment.ImageUploadPreviewFragment;
import com.tokopedia.core.inboxreputation.fragment.InboxReputationDetailFragment;
import com.tokopedia.core.inboxreputation.fragment.InboxReputationFormFragment;
import com.tokopedia.core.inboxreputation.fragment.InboxReputationFormResponseFragment;
import com.tokopedia.core.inboxreputation.intentservice.ReviewResultReceiver;
import com.tokopedia.core.inboxreputation.listener.InboxReputationDetailView;
import com.tokopedia.core.inboxreputation.intentservice.InboxReviewIntentService;
import com.tokopedia.core.var.TkpdState;

/**
 * Created by Nisie on 21/01/16.
 */
public class InboxReputationDetailActivity extends
        BasePresenterActivity implements
        InboxReputationDetailView, InboxReputationDetailFragment.DoActionReputationListener,
        InboxReputationFormFragment.DoActionReputationListener,
        InboxReputationFormResponseFragment.DoActionReputationListener,
        ReviewResultReceiver.Receiver {

    public static final String INBOX_REPUTATION_DETAIL_FRAGMENT_TAG = "INBOX_REPUTATION_DETAIL";
    public static final String INBOX_REPUTATION_FORM_FRAGMENT_TAG = "INBOX_REPUTATION_FORM";
    public static final String INBOX_REPUTATION_FORM_RESPONSE_FRAGMENT_TAG = "INBOX_REPUTATION_FORM_RESPONSE";
    public static final String IMAGE_UPLOAD_PREVIEW_FRAGMENT_TAG = "IMAGE_UPLOAD_PREVIEW";

    public static final String NAV_EDIT_PRODUCT = "edit product";
    public static final String NAV_POST_PRODUCT = "post product";
    public static final String NAV_RESPONSE_PRODUCT = "response product";

    ReviewResultReceiver mReceiver;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment != null && fragment.getActivity() == null) {
            finish();
            startActivity(getIntent());
        } else if (fragment == null) {
            inflateFragment(getIntent().getExtras());
        }

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        mReceiver = new ReviewResultReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void inflateFragment(Bundle data) {
        switch (data.getString("nav", "")) {
            case NAV_POST_PRODUCT:
                getFragmentManager().beginTransaction()
                        .add(R.id.container, InboxReputationFormFragment.createInstance(data),
                                INBOX_REPUTATION_FORM_FRAGMENT_TAG)
                        .commit();
                break;
            case NAV_EDIT_PRODUCT:
                getFragmentManager().beginTransaction()
                        .add(R.id.container, InboxReputationFormFragment.createInstance(data),
                                INBOX_REPUTATION_FORM_FRAGMENT_TAG)
                        .commit();
                break;
            case NAV_RESPONSE_PRODUCT:
                getFragmentManager().beginTransaction()
                        .add(R.id.container, InboxReputationFormResponseFragment.createInstance(data),
                                INBOX_REPUTATION_FORM_RESPONSE_FRAGMENT_TAG)
                        .commit();
                break;
            case ImageUploadPreviewFragment.NAV_UPLOAD_IMAGE:
                getFragmentManager().beginTransaction()
                        .add(R.id.container, ImageUploadPreviewFragment.createInstance(data),
                                IMAGE_UPLOAD_PREVIEW_FRAGMENT_TAG)
                        .commit();
                break;
            default:
                getFragmentManager().beginTransaction()
                        .add(R.id.container, InboxReputationDetailFragment.createInstance(data),
                                INBOX_REPUTATION_DETAIL_FRAGMENT_TAG)
                        .commit();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TkpdState.RequestCode.CODE_OPEN_DETAIL_PRODUCT_REVIEW:
                getFragmentManager().findFragmentById(R.id.container).onActivityResult(requestCode,
                        resultCode, data);
                break;
            case ImageUploadHandler.CODE_UPLOAD_IMAGE:
                getFragmentManager().findFragmentById(R.id.container).onActivityResult(requestCode,
                        resultCode, data);
                break;
            default:
                throw new UnsupportedOperationException("Invalid Type Action");
        }
    }

    @Override
    public void postReview(Bundle param) {
        InboxReviewIntentService.startActionReview(this,
                param, mReceiver, InboxReviewIntentService.ACTION_POST_REVIEW);
    }

    @Override
    public void editReview(Bundle param) {
        InboxReviewIntentService.startActionReview(this,
                param, mReceiver, InboxReviewIntentService.ACTION_EDIT_REVIEW);
    }

    @Override
    public void postReputation(Bundle param) {
        InboxReviewIntentService.startActionReview(this,
                param, mReceiver, InboxReviewIntentService.ACTION_POST_REPUTATION);
    }

    @Override
    public void skipReview(Bundle param) {
        InboxReviewIntentService.startActionReview(this,
                param, mReceiver, InboxReviewIntentService.ACTION_SKIP_REVIEW);
    }

    @Override
    public void postResponse(Bundle param) {
        InboxReviewIntentService.startActionReview(this,
                param, mReceiver, InboxReviewIntentService.ACTION_POST_RESPONSE);
    }

    @Override
    public void deleteResponse(Bundle param) {
        InboxReviewIntentService.startActionReview(this,
                param, mReceiver, InboxReviewIntentService.ACTION_DELETE_RESPONSE);
    }

    @Override
    public void postReport(Bundle param) {
        InboxReviewIntentService.startActionReview(this,
                param, mReceiver, InboxReviewIntentService.ACTION_POST_REPORT);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        int type = resultData.getInt(InboxReviewIntentService.EXTRA_TYPE, 0);
        Fragment fragment = getFragment(type);

        if (fragment != null) {
            switch (resultCode) {
                case InboxReviewIntentService.STATUS_SUCCESS:
                    onReceiveResultSuccess(fragment, resultData);
                    break;
                case InboxReviewIntentService.STATUS_ERROR:
                    onReceiveResultError(fragment, resultData);
                    break;
            }
        }
    }

    private void onReceiveResultError(Fragment fragment, Bundle resultData) {
        int type = resultData.getInt(InboxReviewIntentService.EXTRA_TYPE, 0);
        switch (type) {
            case InboxReviewIntentService.ACTION_POST_REPUTATION:
                ((InboxReputationDetailFragment) fragment).onFailedPostReputation(resultData);
                break;
            case InboxReviewIntentService.ACTION_POST_RESPONSE:
                ((InboxReputationFormResponseFragment) fragment).onFailedPostResponse(resultData);
                break;
            case InboxReviewIntentService.ACTION_SKIP_REVIEW:
                ((InboxReputationDetailFragment) fragment).onFailedSkipReview(resultData);
                break;
            case InboxReviewIntentService.ACTION_DELETE_RESPONSE:
                ((InboxReputationDetailFragment) fragment).onFailedDeleteResponse(resultData);
                break;
            case InboxReviewIntentService.ACTION_POST_REVIEW:
                ((InboxReputationFormFragment) fragment).onFailedPostReview(resultData);
                break;
            case InboxReviewIntentService.ACTION_EDIT_REVIEW:
                ((InboxReputationFormFragment) fragment).onFailedEditReview(resultData);
                break;
            case InboxReviewIntentService.ACTION_POST_REPORT:
                ((InboxReputationDetailFragment) fragment).onFailedPostReport(resultData);
                break;
            default:
                throw new UnsupportedOperationException("Invalid Type Action");
        }
    }

    private Fragment getFragment(int type) {
        switch (type) {
            case InboxReviewIntentService.ACTION_POST_REPUTATION:
                return getFragmentManager().findFragmentByTag(INBOX_REPUTATION_DETAIL_FRAGMENT_TAG);
            case InboxReviewIntentService.ACTION_SKIP_REVIEW:
                return getFragmentManager().findFragmentByTag(INBOX_REPUTATION_DETAIL_FRAGMENT_TAG);
            case InboxReviewIntentService.ACTION_DELETE_RESPONSE:
                return getFragmentManager().findFragmentByTag(INBOX_REPUTATION_DETAIL_FRAGMENT_TAG);
            case InboxReviewIntentService.ACTION_POST_RESPONSE:
                return getFragmentManager().findFragmentByTag(INBOX_REPUTATION_FORM_RESPONSE_FRAGMENT_TAG);
            case InboxReviewIntentService.ACTION_POST_REVIEW:
                return getFragmentManager().findFragmentByTag(INBOX_REPUTATION_FORM_FRAGMENT_TAG);
            case InboxReviewIntentService.ACTION_EDIT_REVIEW:
                return getFragmentManager().findFragmentByTag(INBOX_REPUTATION_FORM_FRAGMENT_TAG);
            case InboxReviewIntentService.ACTION_POST_REPORT:
                return getFragmentManager().findFragmentByTag(INBOX_REPUTATION_DETAIL_FRAGMENT_TAG);
            default:
                throw new UnsupportedOperationException("Invalid Type Action");
        }
    }

    private void onReceiveResultSuccess(Fragment fragment, Bundle resultData) {
        int type = resultData.getInt(InboxReviewIntentService.EXTRA_TYPE, 0);
        switch (type) {
            case InboxReviewIntentService.ACTION_POST_REPUTATION:
                ((InboxReputationDetailFragment) fragment).onSuccessPostReputation(resultData);
                break;
            case InboxReviewIntentService.ACTION_POST_RESPONSE:
                ((InboxReputationFormResponseFragment) fragment).onSuccessPostResponse(resultData);
                break;
            case InboxReviewIntentService.ACTION_SKIP_REVIEW:
                ((InboxReputationDetailFragment) fragment).onSuccessSkipReview(resultData);
                break;
            case InboxReviewIntentService.ACTION_DELETE_RESPONSE:
                ((InboxReputationDetailFragment) fragment).onSuccessDeleteResponse(resultData);
                break;
            case InboxReviewIntentService.ACTION_POST_REVIEW:
                ((InboxReputationFormFragment) fragment).onSuccessPostReview(resultData);
                break;
            case InboxReviewIntentService.ACTION_EDIT_REVIEW:
                ((InboxReputationFormFragment) fragment).onSuccessEditReview(resultData);
                break;
            case InboxReviewIntentService.ACTION_POST_REPORT:
                ((InboxReputationDetailFragment) fragment).onSuccessPostReport(resultData);
                break;
            default:
                throw new UnsupportedOperationException("Invalid Type Action");
        }
    }

}
