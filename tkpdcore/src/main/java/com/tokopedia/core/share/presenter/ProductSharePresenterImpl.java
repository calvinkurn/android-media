package com.tokopedia.core.share.presenter;

import android.app.Activity;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.tkpd.library.utils.ConnectionDetector;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.share.listener.ShareView;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.core.util.ClipboardHandler;
import com.tokopedia.core.util.ShareSocmedHandler;
import com.tokopedia.core.var.TkpdState;

/**
 * Created by Angga.Prasetiyo on 11/12/2015.
 */
public class ProductSharePresenterImpl implements ProductSharePresenter {
    private static final String TAG = ProductSharePresenterImpl.class.getSimpleName();

    private final Activity activity;
    // private final ProductShareFragment fragment;
    private static final String FACEBOOK = "well";
    LocalCacheHandler facebookCache;
    private ShareView view;

    public ProductSharePresenterImpl(ShareView view) {
        this.activity = view.getActivity();
        // this.fragment = (ProductShareFragment) baseFragment;
        this.view = view;
        facebookCache = new LocalCacheHandler(activity, FACEBOOK);
    }

    @Override
    public void shareBBM(ShareData data) {
        if (data.getType().equals(ShareData.CATEGORY_TYPE)) {
            shareCategory(data, AppEventTracking.SOCIAL_MEDIA.BBM);
        } else {
            UnifyTracking.eventShare(
                    AppEventTracking.SOCIAL_MEDIA.BBM
            );
        }
        data.setSource(AppEventTracking.SOCIAL_MEDIA.BBM);
        ShareSocmedHandler.ShareSpecific(data, activity, TkpdState.PackageName.BlackBerry,
                TkpdState.PackageName.TYPE_TEXT, null, null);
    }

    @Override
    public void shareFb(final ShareData data) {
        if (data.getType().equals(ShareData.CATEGORY_TYPE)) {
            shareCategory(data, AppEventTracking.SOCIAL_MEDIA.FACEBOOK);
        } else {
            UnifyTracking.eventShare(
                    AppEventTracking.SOCIAL_MEDIA.FACEBOOK
            );
        }
        data.setSource(AppEventTracking.SOCIAL_MEDIA.FACEBOOK);
        ConnectionDetector detector = new ConnectionDetector(this.activity);
        boolean expired = facebookCache.isExpired();

        if (detector.isConnectingToInternet()) {
            if (expired) {
                LoginManager.getInstance().logOut();
            }
            BranchSdkUtils.generateBranchLink(data, activity, new BranchSdkUtils.GenerateShareContents() {
                @Override
                public void onCreateShareContents(String shareContents, String shareUri, String branchUrl) {
                    view.showDialogShareFb(branchUrl);
                }
            });

        } else {
            NetworkErrorHelper.showSnackbar(this.activity);
        }
    }


    @Override
    public void shareTwitter(ShareData data) {
        if (data.getType().equals(ShareData.CATEGORY_TYPE)) {
            shareCategory(data, AppEventTracking.SOCIAL_MEDIA.TWITTER);
        } else {
            UnifyTracking.eventShare(
                    AppEventTracking.SOCIAL_MEDIA.TWITTER
            );
        }
        data.setSource(AppEventTracking.SOCIAL_MEDIA.TWITTER);
        if (data.getImgUri() != null) {
            ShareSocmedHandler.ShareSpecificUri(data, activity, TkpdState.PackageName.Twitter,
                    TkpdState.PackageName.TYPE_IMAGE, data.getImgUri(), TkpdState.PackageName
                            .TWITTER_DEFAULT + "url=" + data.getUri() + "&text=" + data.getName());
        } else {
            ShareSocmedHandler.ShareSpecific(data, activity, TkpdState.PackageName.Twitter,
                    TkpdState.PackageName.TYPE_TEXT, null, null);
        }

    }

    @Override
    public void shareWhatsApp(ShareData data) {
        if (data.getType().equals(ShareData.CATEGORY_TYPE)) {
            shareCategory(data, AppEventTracking.SOCIAL_MEDIA.WHATSHAPP);
        } else {
            UnifyTracking.eventShare(
                    AppEventTracking.SOCIAL_MEDIA.WHATSHAPP
            );
        }
        data.setSource(AppEventTracking.SOCIAL_MEDIA.WHATSHAPP);
        ShareSocmedHandler.ShareSpecific(data, activity, TkpdState.PackageName.Whatsapp,
                TkpdState.PackageName.TYPE_TEXT, null, null);

    }

    @Override
    public void shareLine(ShareData data) {
        if (data.getType().equals(ShareData.CATEGORY_TYPE)) {
            shareCategory(data, AppEventTracking.SOCIAL_MEDIA.LINE);
        } else {
            UnifyTracking.eventShare(
                    AppEventTracking.SOCIAL_MEDIA.LINE
            );
        }
        data.setSource(AppEventTracking.SOCIAL_MEDIA.LINE);
        ShareSocmedHandler.ShareSpecific(data, activity, TkpdState.PackageName.Line,
                TkpdState.PackageName.TYPE_TEXT, null, null);

    }

    @Override
    public void sharePinterest(ShareData data) {
        if (data.getType().equals(ShareData.CATEGORY_TYPE)) {
            shareCategory(data, AppEventTracking.SOCIAL_MEDIA.PINTEREST);
        } else {
            UnifyTracking.eventShare(
                    AppEventTracking.SOCIAL_MEDIA.PINTEREST
            );
        }
        data.setSource(AppEventTracking.SOCIAL_MEDIA.PINTEREST);
        if (data.getImgUri() != null) {
            ShareSocmedHandler.ShareSpecificUri(data, activity, TkpdState.PackageName.Pinterest,
                    TkpdState.PackageName.TYPE_TEXT, data.getImgUri(), null);
        } else {
            ShareSocmedHandler.ShareSpecific(data, activity, TkpdState.PackageName.Pinterest,
                    TkpdState.PackageName.TYPE_TEXT, null, null);
        }
    }

    @Override
    public void shareMore(ShareData data) {
        if (data.getType().equals(ShareData.CATEGORY_TYPE)) {
            shareCategory(data, AppEventTracking.SOCIAL_MEDIA.OTHER);
        } else {
            UnifyTracking.eventShare(
                    AppEventTracking.SOCIAL_MEDIA.OTHER
            );
        }
        data.setSource(AppEventTracking.SOCIAL_MEDIA.OTHER);
        if (data.getImgUri() != null) {
            ShareSocmedHandler.ShareIntentImageUri(data, activity, null,
                    data.getImgUri());
        } else {
            ShareSocmedHandler.ShareIntentImageUri(data, activity, null,
                    data.getImgUri());
        }
    }

    @Override
    public void shareInstagram(ShareData data) {
        if (data.getType().equals(ShareData.CATEGORY_TYPE)) {
            shareCategory(data, AppEventTracking.SOCIAL_MEDIA.INSTAGRAM);
        } else {
            UnifyTracking.eventShare(
                    AppEventTracking.SOCIAL_MEDIA.INSTAGRAM
            );
        }
        data.setSource(AppEventTracking.SOCIAL_MEDIA.INSTAGRAM);
        if (data.getImgUri() != null) {
            ShareSocmedHandler.ShareSpecificUri(data, activity, TkpdState.PackageName.Instagram,
                    TkpdState.PackageName.TYPE_IMAGE,
                    data.getImgUri(), null);
        } else {
            ShareSocmedHandler.ShareSpecific(data, activity, TkpdState.PackageName.Instagram,
                    TkpdState.PackageName.TYPE_TEXT, null, null);
        }
    }

    @Override
    public void shareGPlus(ShareData data) {
        if (data.getType().equals(ShareData.CATEGORY_TYPE)) {
            shareCategory(data, AppEventTracking.SOCIAL_MEDIA.GOOGLE_PLUS);
        } else {
            UnifyTracking.eventShare(
                    AppEventTracking.SOCIAL_MEDIA.GOOGLE_PLUS
            );
        }
        data.setSource(AppEventTracking.SOCIAL_MEDIA.GOOGLE_PLUS);
        ShareSocmedHandler.ShareSpecific(data, activity, TkpdState.PackageName.Gplus,
                TkpdState.PackageName.TYPE_IMAGE,
                null, null);
    }

    @Override
    public void shareCopy(final ShareData data) {

        data.setSource("Copy");
        BranchSdkUtils.generateBranchLink(data, activity, new BranchSdkUtils.GenerateShareContents() {
            @Override
            public void onCreateShareContents(String shareContents, String shareUri, String branchUrl) {
                ClipboardHandler.CopyToClipboard(activity, shareUri);
            }
        });

        Toast.makeText(activity, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setFacebookCache() {
        facebookCache.setExpire(3600);
    }

    @Override
    public void shareCategory(ShareData data, String media) {
        String[] shareParam = data.getSplittedDescription(",");
        if (shareParam.length == 2) {
            UnifyTracking.eventShareCategory(shareParam[0], shareParam[1] + "-" + media);
        }
    }
}
