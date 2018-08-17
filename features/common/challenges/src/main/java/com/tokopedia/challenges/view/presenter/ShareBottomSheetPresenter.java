package com.tokopedia.challenges.view.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.domain.usecase.PostMapBranchUrlUseCase;
import com.tokopedia.challenges.view.share.ShareBottomSheet;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.core.util.ClipboardHandler;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class ShareBottomSheetPresenter extends BaseDaggerPresenter<ShareBottomSheetContract.View> implements ShareBottomSheetContract.Presenter {
    PostMapBranchUrlUseCase postMapBranchUrlUseCase;

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
                Log.d("result", typeRestResponseMap.toString());
                if (packageName.equalsIgnoreCase(ShareBottomSheet.KEY_COPY)) {
                    ClipboardHandler.CopyToClipboard(getView().getActivity(), branchUrl);

                } else {
                    ((ChallengesModuleRouter) ((getView().getActivity()).getApplication())).shareBranchUrlForChallenge(getView().getActivity(), packageName, branchUrl, shareContents);

                }
            }
        });
    }
}
