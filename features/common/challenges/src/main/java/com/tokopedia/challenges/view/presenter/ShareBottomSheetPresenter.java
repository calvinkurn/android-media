package com.tokopedia.challenges.view.presenter;

import android.content.pm.ResolveInfo;
import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.domain.usecase.PostMapBranchUrlUseCase;
import com.tokopedia.challenges.view.share.ShareBottomSheet;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.core.util.ClipboardHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class ShareBottomSheetPresenter extends BaseDaggerPresenter<ShareBottomSheetContract.View> implements ShareBottomSheetContract.Presenter {
    PostMapBranchUrlUseCase postMapBranchUrlUseCase;
    private static final String PACKAGENAME_WHATSAPP = "com.whatsapp.ContactPicker";
    private static final String PACKAGENAME_FACEBOOK = "com.facebook.composer.shareintent.ImplicitShareIntentHandlerDefaultAlias";
    private static final String PACKAGENAME_LINE = "jp.naver.line.android.activity.selectchat.SelectChatActivityLaunchActivity";
    //private static final String PACKAGENAME_TWITTER = "com.twitter.composer.ComposerShareActivity";
    private static final String PACKAGENAME_GPLUS = "com.google.android.apps.plus.GatewayActivityAlias";
    private static final String PACKAGENAME_INSTAGRAM = "com.instagram.direct.share.handler.DirectShareHandlerActivity";

    private String[] ClassNameApplications = new String[]{PACKAGENAME_WHATSAPP, PACKAGENAME_INSTAGRAM,
            PACKAGENAME_FACEBOOK, PACKAGENAME_LINE, PACKAGENAME_GPLUS};

    @Inject
    public ShareBottomSheetPresenter(PostMapBranchUrlUseCase postMapBranchUrlUseCase) {
        this.postMapBranchUrlUseCase = postMapBranchUrlUseCase;

    }

    public void postMapBranchUrl(String id, String branchUrl, String packageName, String shareContents) {
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
                if (packageName.equalsIgnoreCase(ShareBottomSheet.KEY_COPY)) {
                    ClipboardHandler.CopyToClipboard(getView().getActivity(), branchUrl);

                } else {
                    ((ChallengesModuleRouter) ((getView().getActivity()).getApplication())).shareBranchUrlForChallenge(getView().getActivity(), packageName, branchUrl, shareContents);

                }
            }
        });


































    }

    public void createAndShareUrl(String packageName, final String url, String submissionId,String deepLink, final boolean isChallenge, String title,String og_url,String og_title,String og_image) {
         if (url.startsWith("https://tokopedia.link") || url.startsWith("http://tokopedia.link")) {
            if (packageName.equalsIgnoreCase(ShareBottomSheet.KEY_COPY)) {
                ClipboardHandler.CopyToClipboard(getView().getActivity(), url);
            } else {
                ((ChallengesModuleRouter) ((getView().getActivity()).getApplication())).shareBranchUrlForChallenge(getView().getActivity(), packageName, url, url);
            }
        } else {
            ((ChallengesModuleRouter) ((getView().getActivity()).getApplication())).generateBranchUrlForChallenge(getView().getActivity(), url, title, og_url, og_title, og_image, deepLink, new ChallengesModuleRouter.BranchLinkGenerateListener() {
                @Override
                public void onGenerateLink(String shareContents, String shareUri) {
                    if (packageName.equalsIgnoreCase(ShareBottomSheet.KEY_COPY)) {
                        ClipboardHandler.CopyToClipboard(getView().getActivity(), shareUri);
                    } else if (isChallenge) {
                        ((ChallengesModuleRouter) ((getView().getActivity()).getApplication())).shareBranchUrlForChallenge(getView().getActivity(), packageName, url, shareContents);
                    } else {
                        postMapBranchUrl(submissionId, shareUri, packageName, shareContents);
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
}
