package com.tokopedia.challenges.view.presenter;

import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
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
    public final static String TAG = "url";
    private List<String> brands;
    GetSubmissionChallengesUseCase getSubmissionChallengesUseCase;
    private RequestParams searchParams = RequestParams.create();
    private int pageStart = 0;
    private int pageSize=10;
    private String sortType=Utils.QUERY_PARAM_KEY_SORT_RECENT;
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

    public void setSortType(String sortType){
        this.sortType=sortType;
    }

    public void setChallengeId(String challengeId){
        this.challengeId=challengeId;
    }

    public void loadMoreItems(boolean showProgress) {
        isLoading = true;
        setNextPageParams();
        if(showProgress)
            getView().showProgressBar();
        getSubmissionChallengesUseCase.setRequestParams(searchParams);
        getSubmissionChallengesUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                isLoading = false;
                getView().hideProgressBar();
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                if (getView() == null) {
                    return;
                }
                RestResponse res1 = typeRestResponseMap.get(SubmissionResponse.class);
                SubmissionResponse submissionResponse = res1.getData();
                isLoading = false;
                getView().removeFooter();
                if (submissionResponse != null) {
                    getView().addSubmissionToCards(submissionResponse.getSubmissionResults());
                    if (submissionResponse.getSubmissionResults() != null)
                        pageStart += submissionResponse.getSubmissionResults().size();
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
                loadMoreItems(false);
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
