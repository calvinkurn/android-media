package com.tokopedia.challenges.view.presenter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.domain.usecase.PostMapBranchUrlUseCase;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.share.ShareBottomSheet;
import com.tokopedia.challenges.view.share.ShareInstagramBottomSheet;
import com.tokopedia.challenges.view.utils.ChallengesCacheHandler;
import com.tokopedia.challenges.view.utils.Utils;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */

public class ShareBottomSheetPresenter extends BaseDaggerPresenter<ShareBottomSheetContract.View> implements ShareBottomSheetContract.Presenter {

    PostMapBranchUrlUseCase postMapBranchUrlUseCase;
    private static final String PACKAGENAME_WHATSAPP = "com.whatsapp";
    private static final String PACKAGENAME_FACEBOOK = "com.facebook.katana";
    private static final String PACKAGENAME_LINE = "jp.naver.line.android";
    private static final String PACKAGENAME_GPLUS = "com.google.android.apps.plus";
    private static final String PACKAGENAME_INSTAGRAM = "com.instagram.android";
    private static final String BRANCH_LINK_FORMAT_HTTPS = "https://tokopedia.link";
    private static final String BRANCH_LINK_FORMAT_HTTP = "http://tokopedia.link";

    private String[] ClassNameApplications = new String[]{PACKAGENAME_WHATSAPP, PACKAGENAME_INSTAGRAM,
            PACKAGENAME_FACEBOOK, PACKAGENAME_LINE, PACKAGENAME_GPLUS};

    @Inject
    public ShareBottomSheetPresenter(PostMapBranchUrlUseCase postMapBranchUrlUseCase) {
        this.postMapBranchUrlUseCase = postMapBranchUrlUseCase;

    }

    private void postMapBranchUrl(String id, String branchUrl, String packageName, String title, boolean isChallenge) {
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
                if (getView() == null || getView().getActivity() == null) return;

                shareLink(isChallenge, branchUrl, title, packageName);
            }
        });
    }

    @Override
    public void createAndShareChallenge(String packageName, String name) {
        Result challengeItem = getView().getChallengeItem();
        if (challengeItem == null) {
            return;
        }
        String url = null;
        if (challengeItem.getSharing() != null && challengeItem.getSharing().getMetaTags() != null) {
            url = challengeItem.getSharing().getMetaTags().getOgUrl();
        }

        if (TextUtils.isEmpty(url)) {
            url = Utils.getApplinkPathForBranch(ChallengesUrl.AppLink.CHALLENGES_DETAILS, challengeItem.getId());
        }
        if (url.startsWith(BRANCH_LINK_FORMAT_HTTPS) || url.startsWith(BRANCH_LINK_FORMAT_HTTP)) {
            shareLink(true, url, challengeItem.getTitle(), packageName);
        } else {
            getView().showProgress(getView().getActivity().getString(R.string.ch_please_wait));
            ((ChallengesModuleRouter) ((getView().getActivity()).getApplication())).generateBranchUrlForChallenge(getView().getActivity(), url, challengeItem.getTitle(), name, challengeItem.getSharing().getMetaTags().getOgUrl(), challengeItem.getSharing().getMetaTags().getOgTitle(), challengeItem.getSharing().getMetaTags().getOgDescription(), challengeItem.getSharing().getMetaTags().getOgImage(), Utils.getApplinkPathForBranch(ChallengesUrl.AppLink.CHALLENGES_DETAILS, challengeItem.getId()), new ChallengesModuleRouter.BranchLinkGenerateListener() {
                @Override
                public void onGenerateLink(String shareContents, String shareUri) {
                    getView().hideProgress();
                    shareLink(true, shareUri, challengeItem.getTitle(), packageName);
                }
            });
        }
    }

    @Override
    public void createAndShareSubmission(String packageName, String name) {
        SubmissionResult submissionItem = getView().getSubmissionItem();
        if (submissionItem == null) {
            return;
        }
        String url = submissionItem.getSharing().getMetaTags().getOgUrl();
        if (TextUtils.isEmpty(url)) {
            url = Utils.getApplinkPathForBranch(ChallengesUrl.AppLink.SUBMISSION_DETAILS, submissionItem.getId());
        }
        if (url.startsWith(BRANCH_LINK_FORMAT_HTTPS) || url.startsWith(BRANCH_LINK_FORMAT_HTTP)) {
            shareLink(false, url, submissionItem.getTitle(), packageName);
        } else {
            getView().showProgress(getView().getActivity().getString(R.string.ch_please_wait));
            ((ChallengesModuleRouter) ((getView().getActivity()).getApplication())).generateBranchUrlForChallenge(getView().getActivity(), url, submissionItem.getTitle(), name, submissionItem.getSharing().getMetaTags().getOgUrl(), submissionItem.getSharing().getMetaTags().getOgTitle(), submissionItem.getSharing().getMetaTags().getOgDescription(), submissionItem.getSharing().getMetaTags().getOgImage(), Utils.getApplinkPathForBranch(ChallengesUrl.AppLink.SUBMISSION_DETAILS, submissionItem.getId()), new ChallengesModuleRouter.BranchLinkGenerateListener() {
                @Override
                public void onGenerateLink(String shareContents, String shareUri) {
                    getView().hideProgress();
                    postMapBranchUrl(submissionItem.getId(), shareUri, packageName, submissionItem.getTitle(), false);
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

    public List<ResolveInfo> appInstalledOrNot() {
        List<ResolveInfo> showApplications = new ArrayList<>();
        PackageManager pm = getView().getActivity().getPackageManager();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        // mainIntent.setType("image/*");
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(shareIntent, 0); // returns all applications which can listen to the SEND Intent
        if (resolveInfos != null && !resolveInfos.isEmpty()) {
            for (ResolveInfo info : resolveInfos) {
                if (Arrays.asList(ClassNameApplications)
                        .contains(info.activityInfo.packageName)) {
                    showApplications.add(info);
                }
            }
        }


        return showApplications;
    }

    private String getShareContents(boolean isChallenge, String url, String challengeTitle) {
        if (isChallenge) {
            return String.format(getView().getActivity().getString(R.string.ch_challenge_share_text), challengeTitle, url);
        } else {
            return String.format(getView().getActivity().getString(R.string.ch_post_share_text), challengeTitle, url);
        }
    }

    private void copyToClipboard(String contents) {
        ClipboardManager clipboard = (ClipboardManager) getView().getActivity().getSystemService(Activity.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Tokopedia", contents);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getView().getActivity(), R.string.ch_copy_to_clipboard_bhahasa, Toast.LENGTH_LONG).show();
    }

    private void shareLink(boolean isChallenge, String branchUrl, String title, String packageName) {
        if (packageName.equalsIgnoreCase(ShareBottomSheet.KEY_COPY)) {
            copyToClipboard(getShareContents(isChallenge, branchUrl, title));
        } else if (packageName.equalsIgnoreCase(ShareBottomSheet.KEY_OTHER)) {
            Intent intent = getIntent(title, getShareContents(isChallenge, branchUrl, title));
            getView().getActivity().startActivity(Intent.createChooser(intent, getView().getActivity().getString(R.string.ch_other)));
        } else if (PACKAGENAME_INSTAGRAM.equalsIgnoreCase(packageName)) {
            shareToInstagram(title, getShareContents(isChallenge, branchUrl, title), isChallenge);
        } else {
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

    private void createInstagramIntent(String title, String contains, File media, boolean isVideo) {
        ShareInstagramBottomSheet shareInstagramBottomSheet = new ShareInstagramBottomSheet();

        if (getView().getChallengeItem() != null) {
            shareInstagramBottomSheet.setData(getView().getChallengeItem(), title, contains, media, isVideo);
        } else if (getView().getSubmissionItem() != null) {
            shareInstagramBottomSheet.setData(getView().getSubmissionItem(), title, contains, media, isVideo);
        }


        shareInstagramBottomSheet.show(((AppCompatActivity) getView().getActivity()).getSupportFragmentManager(), ShareInstagramBottomSheet.class.getCanonicalName());

    }

    private void convertHttpPathToLocalPath(String title, String contains, String mediaUrl, boolean isVideo) {
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
                        if (getView() == null || getView().getActivity() == null) return;
                        getView().hideProgress();
                        if (file != null)
                            createInstagramIntent(title, contains, file, isVideo);
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
                            if (getView() == null || getView().getActivity() == null) {
                                return null;
                            }
                            FutureTarget<File> future = Glide.with(getView().getActivity())
                                    .load(url)
                                    .downloadOnly(ImageUtils.DEF_WIDTH, ImageUtils.DEF_HEIGHT);
                            try {
                                return future.get();
                            } catch (InterruptedException | ExecutionException e) {
                                getView().hideProgress();
                                e.printStackTrace();
                                throw new RuntimeException(e.getMessage());
                            }
                        } else {
                            return new File(url);
                        }
                    }
                });
    }

    private void shareToInstagram(String title, String shareContents, boolean isChallenge) {

        String mediaUrl = null;
        boolean isVideo = false;
        if (isChallenge) {
            mediaUrl = getView().getChallengeItem().getThumbnailUrl();
            isVideo = false;

        } else {
            String videoPath = ChallengesCacheHandler.getLocalVideoPath(getView().getActivity(), getView().getSubmissionItem().getId());
            if (!TextUtils.isEmpty(videoPath)) {
                File file = new File(videoPath);
                if (file.exists()) {
                    if (Utils.isImage(videoPath)) {
                        isVideo = false;
                    } else {
                        isVideo = true;
                    }
                    createInstagramIntent(title, shareContents, file, isVideo);
                    return;
                }
            } else if (getView().getSubmissionItem().getMedia().get(0).getMediaType().equalsIgnoreCase("Image")) {
                if (getView().getSubmissionItem().getSharing() != null && getView().getSubmissionItem().getSharing().getMetaTags() != null) {
                    mediaUrl = getView().getSubmissionItem().getSharing().getMetaTags().getOgImage();
                }
            } else if (getView().getSubmissionItem().getMedia() != null && getView().getSubmissionItem().getMedia().get(0).getVideo() != null && getView().getSubmissionItem().getMedia().get(0).getVideo().getSources() != null) {
                mediaUrl = getView().getSubmissionItem().getMedia().get(0).getVideo().getSources().get(1).getSource();
                isVideo = true;
            }
        }

        if (mediaUrl != null) {
            convertHttpPathToLocalPath(title, shareContents, mediaUrl, isVideo);
        }
    }

    @Override
    public boolean getParticipatedStatus(SubmissionResult submissionResult) {
        if (submissionResult != null)
            return (submissionResult.getMe() != null && submissionResult.getUser() != null && submissionResult.getMe().getId() != null && submissionResult.getUser().getId() != null && submissionResult.getMe().getId().equalsIgnoreCase(submissionResult.getUser().getId()));
        else
            return false;
    }
}
