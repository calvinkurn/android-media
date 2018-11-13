package com.tokopedia.challenges.view.presenter;

import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.challenges.domain.usecase.GetSubmissionChallengesUseCase;
import com.tokopedia.challenges.view.contractor.AllSubmissionContract;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class AllSubmissionPresenter extends BaseDaggerPresenter<AllSubmissionContract.View>
        implements AllSubmissionContract.Presenter {

    private boolean isLoading;
    private boolean isLastPage;
    private GetSubmissionChallengesUseCase getSubmissionChallengesUseCase;
    private RequestParams searchParams = RequestParams.create();
    private int pageStart = 0;
    private int pageSize = 20;
    private int totalItems = 20;
    private String sortType = Utils.QUERY_PARAM_KEY_SORT_RECENT;
    private String challengeId;


    @Inject
    public AllSubmissionPresenter(GetSubmissionChallengesUseCase getSubmissionChallengesUseCase) {
        this.getSubmissionChallengesUseCase = getSubmissionChallengesUseCase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {
        getSubmissionChallengesUseCase.unsubscribe();
    }


    @Override
    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager) {
        checkIfToLoad(layoutManager);
    }

    public void setPageStart(int start) {
        this.pageStart = start;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    @Override
    public void loadMoreItems(boolean showProgress) {
        isLoading = true;
        setNextPageParams();
        if (showProgress)
            getView().showProgressBar();
        getSubmissionChallengesUseCase.execute(searchParams, new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() == null) return;
                isLoading = false;
                getView().hideProgressBar();
                e.printStackTrace();
                getView().hideProgressBar();
                if (pageStart > 0)
                    NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            loadMoreItems(true);
                        }
                    });
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                if (getView() == null) return;

                RestResponse res1 = typeRestResponseMap.get(SubmissionResponse.class);
                SubmissionResponse submissionResponse = res1.getData();
                if (submissionResponse != null) {
                    totalItems = submissionResponse.getFound();
                }
                isLoading = false;
                getView().removeFooter();
                if (submissionResponse != null && submissionResponse.getSubmissionResults() != null
                        && submissionResponse.getSubmissionResults().size() > 0) {
                    if (pageStart == 0) {
                        getView().clearList();
                    }
                    pageStart += submissionResponse.getSubmissionResults().size();
                    getView().addSubmissionToCards(submissionResponse.getSubmissionResults());
                } else {
                    isLastPage = true;
                }
                getView().hideProgressBar();
                checkIfToLoad(getView().getLayoutManager());
            }
        });
    }

    private void checkIfToLoad(LinearLayoutManager layoutManager) {
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                if (pageStart + pageSize <= totalItems)
                    loadMoreItems(false);
                else
                    getView().removeFooter();

            } else {
                getView().addFooter();
            }
        }
    }

    private void setNextPageParams() {
        searchParams.putString(Utils.QUERY_PARAM_CHALLENGE_ID, challengeId);
        searchParams.putInt(Utils.QUERY_PARAM_KEY_START, pageStart);
        searchParams.putInt(Utils.QUERY_PARAM_KEY_SIZE, pageSize);
        searchParams.putString(Utils.QUERY_PARAM_KEY_SORT, sortType);
    }


}
