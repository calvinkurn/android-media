package com.tokopedia.core.share.presenter;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnPublishListener;
import com.tkpd.library.utils.ConnectionDetector;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.util.ClipboardHandler;
import com.tokopedia.core.util.ShareSocmedHandler;
import com.tokopedia.core.var.TkpdState;

import java.util.List;

/**
 * Created by Angga.Prasetiyo on 11/12/2015.
 */
public class ProductSharePresenterImpl implements ProductSharePresenter {
    private static final String TAG = ProductSharePresenterImpl.class.getSimpleName();

    private final Activity activity;

    public ProductSharePresenterImpl(BasePresenterFragment baseFragment) {
        this.activity = baseFragment.getActivity();
    }

    @Override
    public void shareBBM(ShareData data) {
        UnifyTracking.eventShare(
                data.getSource() != null ? data.getSource() : "",
                AppEventTracking.SOCIAL_MEDIA.BBM
        );
        data.setSource(AppEventTracking.SOCIAL_MEDIA.BBM);
        ShareSocmedHandler.ShareSpecific(activity, TkpdState.PackageName.BlackBerry,
                TkpdState.PackageName.TYPE_TEXT, data.getTextContent(), data.renderShareUri(), null, null);
    }

    @Override
    public void shareFb(final SimpleFacebook simpleFacebook, final ShareData data) {
        UnifyTracking.eventShare( data.getSource() != null ? data.getSource() : "",
                AppEventTracking.SOCIAL_MEDIA.FACEBOOK
        );
        data.setSource(AppEventTracking.SOCIAL_MEDIA.FACEBOOK);
        ConnectionDetector detector =  new ConnectionDetector(this.activity);
        if (detector.isConnectingToInternet()){
            simpleFacebook.publish(new Feed.Builder()
                    .setMessage(data.getUri())
                    .setName(data.getName())
                    .setCaption(data.getPrice())
                    .setDescription(data.getDescription())
                    .setPicture(data.getImgUri())
                    .setLink(data.renderShareUri())
                    .build(), true, new OnPublishListener() {
                @Override
                public void onFail(String reason) {
                    loginFacebook(simpleFacebook);
                }

                @Override
                public void onException(Throwable throwable) {
                    throwable.printStackTrace();
                }

                @Override
                public void onThinking() {
                    Log.d("varis tag", "on Thingking");
                }

                @Override
                public void onComplete(String id) {
                    Log.d("varis tag", "on Complete");
                }
            });
        } else {
            NetworkErrorHelper.showSnackbar(this.activity);
        }
    }


    @Override
    public void shareTwitter(ShareData data) {
        UnifyTracking.eventShare( data.getSource() != null ? data.getSource() : "",
                AppEventTracking.SOCIAL_MEDIA.TWITTER
        );
        data.setSource(AppEventTracking.SOCIAL_MEDIA.TWITTER);
        if (data.getImgUri()!=null){
            ShareSocmedHandler.ShareSpecificUri(activity, TkpdState.PackageName.Twitter,
                    TkpdState.PackageName.TYPE_IMAGE, data.getTextContent(),
                    data.renderShareUri(), data.getImgUri(), TkpdState.PackageName
                            .TWITTER_DEFAULT + "url=" + data.getUri() + "&text=" + data.getName());
        } else {
            ShareSocmedHandler.ShareSpecific(activity, TkpdState.PackageName.Twitter,
                    TkpdState.PackageName.TYPE_TEXT, data.getTextContent(), data.renderShareUri(), null, null);
        }

    }

    @Override
    public void shareWhatsApp(ShareData data) {
        UnifyTracking.eventShare( data.getSource() != null ? data.getSource() : "",
                AppEventTracking.SOCIAL_MEDIA.WHATSHAPP
        );
        data.setSource(AppEventTracking.SOCIAL_MEDIA.WHATSHAPP);
        ShareSocmedHandler.ShareSpecific(activity, TkpdState.PackageName.Whatsapp,
                TkpdState.PackageName.TYPE_TEXT, data.getTextContent(), data.renderShareUri(), null, null);

    }

    @Override
    public void shareLine(ShareData data) {
        UnifyTracking.eventShare( data.getSource() != null ? data.getSource() : "",
                AppEventTracking.SOCIAL_MEDIA.LINE
        );
        data.setSource(AppEventTracking.SOCIAL_MEDIA.LINE);
        if (data.getImgUri() != null){
            ShareSocmedHandler.ShareSpecificUri(activity, TkpdState.PackageName.Line,
                    TkpdState.PackageName.TYPE_TEXT, data.getTextContent(), data.renderShareUri(),
                    data.getImgUri(), null);
        } else {
            ShareSocmedHandler.ShareSpecific(activity, TkpdState.PackageName.Line,
                    TkpdState.PackageName.TYPE_TEXT, data.getTextContent(), data.renderShareUri(), null, null);
        }
    }

    @Override
    public void sharePinterest(ShareData data) {
        UnifyTracking.eventShare( data.getSource() != null ? data.getSource() : "",
                AppEventTracking.SOCIAL_MEDIA.PINTEREST
        );
        data.setSource(AppEventTracking.SOCIAL_MEDIA.PINTEREST);
        if (data.getImgUri() != null){
            ShareSocmedHandler.ShareSpecificUri(activity, TkpdState.PackageName.Pinterest,
                    TkpdState.PackageName.TYPE_TEXT, data.getTextContent(), data.renderShareUri(),
                    data.getImgUri(), null);
        } else {
            ShareSocmedHandler.ShareSpecific(activity, TkpdState.PackageName.Pinterest,
                    TkpdState.PackageName.TYPE_TEXT, data.getTextContent(), data.renderShareUri(), null, null);
        }
    }

    @Override
    public void shareMore(ShareData data) {
        UnifyTracking.eventShare( data.getSource() != null ? data.getSource() : "",
                AppEventTracking.SOCIAL_MEDIA.OTHER
        );
        data.setSource(AppEventTracking.SOCIAL_MEDIA.OTHER);
        if (data.getImgUri()!= null){
            ShareSocmedHandler.ShareIntentImageUri(activity, null, data.getTextContent(), data.renderShareUri(),
                    data.getImgUri());
        } else {
            ShareSocmedHandler.ShareIntentImageUri(activity, null, data.getTextContent(), data.renderShareUri(),
                    data.getImgUri());
        }
    }

    @Override
    public void shareInstagram(ShareData data) {
        UnifyTracking.eventShare( data.getSource() != null ? data.getSource() : "",
                AppEventTracking.SOCIAL_MEDIA.INSTAGRAM
        );
        data.setSource(AppEventTracking.SOCIAL_MEDIA.INSTAGRAM);
        if(data.getImgUri()!= null){
            ShareSocmedHandler.ShareSpecificUri(activity, TkpdState.PackageName.Instagram,
                    TkpdState.PackageName.TYPE_IMAGE, data.getTextContent(), data.renderShareUri(),
                    data.getImgUri(), null);
        } else {
            ShareSocmedHandler.ShareSpecific(activity, TkpdState.PackageName.Instagram,
                    TkpdState.PackageName.TYPE_TEXT, data.getTextContent(), data.renderShareUri(), null, null);
        }
    }

    @Override
    public void shareGPlus(ShareData data) {
        UnifyTracking.eventShare( data.getSource() != null ? data.getSource() : "",
                AppEventTracking.SOCIAL_MEDIA.GOOGLE_PLUS
        );
        data.setSource(AppEventTracking.SOCIAL_MEDIA.GOOGLE_PLUS);
        ShareSocmedHandler.ShareSpecific(activity, TkpdState.PackageName.Gplus,
                TkpdState.PackageName.TYPE_IMAGE, data.getTextContent(), data.renderShareUri(),
                null, null);
    }

    @Override
    public void shareCopy(ShareData data) {
        data.setSource("Copy");
        ClipboardHandler.CopyToClipboard(activity, data.renderShareUri());
        Toast.makeText(activity, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    private void loginFacebook(SimpleFacebook simpleFacebook) {
        simpleFacebook.login(new OnLoginListener() {
            @Override
            public void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onException(Throwable throwable) {

            }

            @Override
            public void onFail(String s) {

            }
        });
    }
}
