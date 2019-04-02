package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;

import rx.Observable;

/**
 * @author by nisie on 8/18/17.
 */

public class GetInboxReputationUseCase extends UseCase<InboxReputationDomain> {

    protected static final String PARAM_PER_PAGE = "per_page";
    protected static final String PARAM_PAGE = "page";
    protected static final String PARAM_ROLE = "role";

    protected static final int DEFAULT_PER_PAGE = 10;
    public static final String PARAM_TIME_FILTER = "time_filter";
    public static final String PARAM_STATUS = "status";
    public static final String PARAM_SCORE_FILTER = "score_filter";

    protected static final int STATUS_UNASSESSED_REPUTATION = 1;
    protected static final int STATUS_UPDATED_REPUTATION = 2;
    protected static final int ROLE_BUYER = 1;
    protected static final int ROLE_SELLER = 2;
    protected static final int STATUS_OTHER = 3;
    protected static final String DEFAULT_TIME_FILTER = "1";
    private static final String PARAM_KEYWORD = "keyword";

    public final static String PARAM_TAB = "tab";
    public static final String PARAM_REPUTATION_ID = "reputation_id";

    protected ReputationRepository reputationRepository;

    public GetInboxReputationUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     ReputationRepository reputationRepository) {
        super(threadExecutor, postExecutionThread);
        this.reputationRepository = reputationRepository;

    }

    @Override
    public Observable<InboxReputationDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.getInboxReputationFromCloud(requestParams);
    }

    public static RequestParams getParam(int page, String keyword, String timeFilter,
                                         String scoreFilter, int tab) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_PER_PAGE, DEFAULT_PER_PAGE);
        params.putInt(PARAM_PAGE, page);
        params.putInt(PARAM_ROLE, getRole(tab));
        if (!TextUtils.isEmpty(keyword))
            params.putString(PARAM_KEYWORD, keyword);
        params.putString(PARAM_TIME_FILTER, !TextUtils.isEmpty(timeFilter) ? timeFilter :
                DEFAULT_TIME_FILTER);
        if (!TextUtils.isEmpty(scoreFilter))
            params.putString(PARAM_SCORE_FILTER, scoreFilter);
        params.putInt(PARAM_STATUS, getStatus(tab));
        params.putInt(PARAM_TAB, tab);
        return params;
    }

    public static RequestParams getSpecificReputation(String reputationId, int tab) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_PER_PAGE, DEFAULT_PER_PAGE);
        params.putInt(PARAM_PAGE, 1);
        params.putInt(PARAM_ROLE, getRole(tab));
        params.putString(PARAM_TIME_FILTER, DEFAULT_TIME_FILTER);
        params.putInt(PARAM_STATUS, getStatus(tab));
        params.putInt(PARAM_TAB, tab);
        params.putString(PARAM_REPUTATION_ID, reputationId);
        return params;
    }


    protected static int getStatus(int tab) {
        switch (tab) {
            case InboxReputationActivity.TAB_WAITING_REVIEW:
                return STATUS_UNASSESSED_REPUTATION;
            case InboxReputationActivity.TAB_MY_REVIEW:
                return STATUS_UPDATED_REPUTATION;
            case InboxReputationActivity.TAB_BUYER_REVIEW:
            default:
                return STATUS_OTHER;
        }
    }

    protected static int getRole(int tab) {
        switch (tab) {
            case InboxReputationActivity.TAB_WAITING_REVIEW:
                return ROLE_BUYER;
            case InboxReputationActivity.TAB_MY_REVIEW:
                return ROLE_BUYER;
            case InboxReputationActivity.TAB_BUYER_REVIEW:
                return ROLE_SELLER;
            default:
                return ROLE_BUYER;
        }
    }
}
