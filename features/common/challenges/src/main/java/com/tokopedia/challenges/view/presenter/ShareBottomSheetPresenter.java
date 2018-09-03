package com.tokopedia.challenges.view.presenter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.domain.usecase.PostMapBranchUrlUseCase;
import com.tokopedia.challenges.view.share.ShareBottomSheet;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.imagepicker.common.util.ImageUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ShareBottomSheetPresenter extends BaseDaggerPresenter<ShareBottomSheetContract.View> implements ShareBottomSheetContract.Presenter {
    PostMapBranchUrlUseCase postMapBranchUrlUseCase;
    private static final String PACKAGENAME_WHATSAPP = "com.whatsapp.ContactPicker";
    private static final String PACKAGENAME_FACEBOOK = "com.facebook.composer.shareintent.ImplicitShareIntentHandlerDefaultAlias";
    private static final String PACKAGENAME_LINE = "jp.naver.line.android.activity.selectchat.SelectChatActivityLaunchActivity";
    //private static final String PACKAGENAME_TWITTER = "com.twitter.composer.ComposerShareActivity";
    private static final String PACKAGENAME_GPLUS = "com.google.android.apps.plus.GatewayActivityAlias";
    private static final String PACKAGENAME_INSTAGRAM_DIRECT = "com.instagram.direct.share.handler.DirectShareHandlerActivity";
    private static final String PACKAGENAME_INSTAGRAM = "com.instagram.android";

    private String[] ClassNameApplications = new String[]{PACKAGENAME_WHATSAPP, PACKAGENAME_INSTAGRAM, PACKAGENAME_INSTAGRAM_DIRECT,
            PACKAGENAME_FACEBOOK, PACKAGENAME_LINE, PACKAGENAME_GPLUS};
    public static final String POST_SHARE_TEXT = "Saya telah mengikuti %s di Tokopedia, bantu saya menang dengan share & like post Cek: %s";
    public static final String CHALLENGE_SHARE_TEXT = "Ikutan %s di Tokopedia Challenge bisa menang berbagai hadiah seru! Cek daftar Challenge yang bisa kamu ikuti di %s";

    @Inject
    public ShareBottomSheetPresenter(PostMapBranchUrlUseCase postMapBranchUrlUseCase) {
        this.postMapBranchUrlUseCase = postMapBranchUrlUseCase;

    }

    public void postMapBranchUrl(String id, String branchUrl, String packageName, String title, boolean isChallenge) {
        postMapBranchUrlUseCase.setRequestParams(id, branchUrl);
        postMapBranchUrlUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                shareLink(isChallenge, branchUrl, title, packageName);
            }
        });
    }

    public void createAndShareUrl(String packageName, final String url, String submissionId, String deepLink, final boolean isChallenge, String title, String og_url, String og_title, String og_image) {

        if (url.startsWith("https://tokopedia.link") || url.startsWith("http://tokopedia.link")) {
            shareLink(isChallenge, url, title, packageName);
        } else {
            getView().showProgress("Please wait");
            ((ChallengesModuleRouter) ((getView().getActivity()).getApplication())).generateBranchUrlForChallenge(getView().getActivity(), url, title, og_url, og_title, og_image, deepLink, new ChallengesModuleRouter.BranchLinkGenerateListener() {
                @Override
                public void onGenerateLink(String shareContents, String shareUri) {
                    getView().hideProgress();
                    getView().setNewUrl(shareUri);
                    if (isChallenge) {
                        shareLink(isChallenge, shareUri, title, packageName);
                    } else {
                        postMapBranchUrl(submissionId, shareUri, packageName, title, isChallenge);
                    }
                }
            });
        }
    }

    public List<ResolveInfo> validate(List<ResolveInfo> resolvedActivities) {
        List<ResolveInfo> showApplications = new ArrayList<>();
        for (ResolveInfo resolveInfo : resolvedActivities) {
            if (Arrays.asList(ClassNameApplications)
                    .contains(resolveInfo.activityInfo.name)) {
                showApplications.add(resolveInfo);
            }
        }
        return showApplications;
    }

    private String getShareContents(boolean isChallenge, String url, String challengeTitle) {
        if (isChallenge) {
            return String.format(CHALLENGE_SHARE_TEXT, challengeTitle, url);
        } else {
            return String.format(POST_SHARE_TEXT, challengeTitle, url);
        }
    }

    private void copyToClipboard(String contents) {
        ClipboardManager clipboard = (ClipboardManager) getView().getActivity().getSystemService(Activity.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Tokopedia", contents);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getView().getActivity(), R.string.copy_to_clipboard_bhahasa, Toast.LENGTH_LONG).show();
    }

    private void shareLink(boolean isChallenge, String branchUrl, String title, String packageName) {
        if (packageName.equalsIgnoreCase(ShareBottomSheet.KEY_COPY)) {
            copyToClipboard(getShareContents(isChallenge, branchUrl, title));
        } else if (packageName.equalsIgnoreCase(ShareBottomSheet.KEY_OTHER)) {
            Intent intent = getIntent(title, getShareContents(isChallenge, branchUrl, title));
            getView().getActivity().startActivity(Intent.createChooser(intent, getView().getActivity().getString(R.string.other)));
        }
        else if (PACKAGENAME_INSTAGRAM.equalsIgnoreCase(packageName)) {
            convertHttpPathToLocalPath(title, getShareContents(isChallenge, branchUrl, title), "https://images.indi.com/s3/indi-upload-ap-southeast-1/Image/26f9ed21a1356e8c125cf0116b872198.jpg");
        }
        else {
            ((ChallengesModuleRouter) ((getView().getActivity()).getApplication())).shareBranchUrlForChallenge(getView().getActivity(), packageName, branchUrl, getShareContents(isChallenge, branchUrl, title));
        }
    }

    private Intent getIntent(String title, String contains) {
        final Intent mIntent = new Intent(Intent.ACTION_SEND);
        mIntent.setType("text/plain");
        mIntent.putExtra(Intent.EXTRA_TITLE, title);
        mIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        mIntent.putExtra(Intent.EXTRA_TEXT, contains);
        return mIntent;
    }

    private void createInstagramIntent(String title, String contains, File media) {
        String type = "image/*";
        //String filename = "/myVideo.mp4";
        //mediaPath = Environment.getExternalStorageDirectory() + filename;

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        // File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TITLE, title);
        share.putExtra(Intent.EXTRA_SUBJECT, title);
        share.putExtra(Intent.EXTRA_TEXT, contains);
        share.setPackage(PACKAGENAME_INSTAGRAM);
        getView().getActivity().startActivity(Intent.createChooser(share, "Share"));
    }

    private void convertHttpPathToLocalPath(String title, String contains, String mediaUrl) {
         downloadObservable(mediaUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(File file) {
                        getView().hideProgress();
                        if (file != null)
                            createInstagramIntent(title, contains, file);
                    }
                });
    }

    @NonNull
    private Observable<File> downloadObservable(String mediaUrl) {
        getView().showProgress("please wait");
        return Observable.just(mediaUrl)
                .map(new Func1<String, File>() {
                    @Override
                    public File call(String url) {
                        if (URLUtil.isNetworkUrl(url)) {
                            if (!isViewAttached()) {
                                return null;
                            }
                            FutureTarget<File> future = Glide.with(getView().getActivity())
                                    .load(url)
                                    .downloadOnly(ImageUtils.DEF_WIDTH, ImageUtils.DEF_HEIGHT);
                            try {
                                return future.get();
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e.getMessage());
                            }
                        } else {
                            return new File(url);
                        }
                    }
                });
    }
}
