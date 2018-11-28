package com.tokopedia.challenges.view.presenter;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.challenges.domain.usecase.GetMySubmissionsListUseCase;
import com.tokopedia.challenges.domain.usecase.PostSubmissionLikeUseCase;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */

public class MySubmissionsHomePresenter extends BaseDaggerPresenter<MySubmissionsBaseContract.View> implements MySubmissionsBaseContract.Presenter {

    private GetMySubmissionsListUseCase getMySubmissionsListUseCase;
    private PostSubmissionLikeUseCase postSubmissionLikeUseCase;
    private boolean isLoading;
    private boolean isLastPage;
    private int pageStart = 0;
    private int pageSize = 30;
    private int totalItems = 30;

    @Inject
    public MySubmissionsHomePresenter(GetMySubmissionsListUseCase getMySubmissionsListUseCase, PostSubmissionLikeUseCase postSubmissionLikeUseCase) {
        this.getMySubmissionsListUseCase = getMySubmissionsListUseCase;
        this.postSubmissionLikeUseCase = postSubmissionLikeUseCase;
    }

    @Override
    public void getMySubmissionsList(Boolean isFirst) {
        if (isFirst) {
            getView().showProgressBarView();
        }
        isLoading = true;
        getMySubmissionsListUseCase.setRequestParams(0, pageSize);
        getMySubmissionsListUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(!isViewAttached()) return;
                isLoading = false;
                getView().removeProgressBarView();
                getView().showErrorNetwork(
                        ErrorHandler.getErrorMessage(getView().getActivity(), e));
                e.printStackTrace();
            }

            @Override
            public void onNext(Map<Type, RestResponse> restResponse) {
                if(!isViewAttached()) return;
                getView().removeProgressBarView();
                RestResponse res1 = restResponse.get(SubmissionResponse.class);
                SubmissionResponse mainDataObject = res1.getData();
                isLoading = false;
                getView().removeFooter();
                if (mainDataObject != null) {
                    totalItems = mainDataObject.getFound();
                }
                if (mainDataObject != null && mainDataObject.getSubmissionResults() != null && mainDataObject.getSubmissionResults().size() > 0) {
                    //pageStart += mainDataObject.getSubmissionResults().size();
                    getView().setSubmissionsDataToUI(mainDataObject.getSubmissionResults());
                } else {
                    isLastPage = true;
                    //if (pageStart == 0)
                    getView().renderEmptyList();
                }
                //checkIfToLoad(getView().getLayoutManager());
            }
        });
    }

    @Override
    public void setSubmissionLike(final SubmissionResult result) {

        RequestParams requestParams = RequestParams.create();
        if (result.getMe() != null)
            requestParams.putBoolean(PostSubmissionLikeUseCase.IS_LIKED, !result.getMe().isLiked());
        if (!TextUtils.isEmpty(result.getId()))
            requestParams.putString(Utils.QUERY_PARAM_SUBMISSION_ID, result.getId());
        postSubmissionLikeUseCase.execute(requestParams,new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
            }
        });

    }

    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager) {
        checkIfToLoad(layoutManager);
    }

    private void checkIfToLoad(LinearLayoutManager layoutManager) {
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                if (pageStart + pageSize <= totalItems)
                    getMySubmissionsList(false);
                else
                    getView().removeFooter();
            } else {
                getView().addFooter();
            }
        }
    }

    @Override
    public void onDestroy() {
        detachView();
    }
}
