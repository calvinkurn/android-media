package com.tokopedia.home.beranda.domain.interactor;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.BuildConfig;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.data.mapper.HomeFeedMapper;
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedContentGqlResponse;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedListModel;
import com.tokopedia.kotlin.util.ContainNullException;
import com.tokopedia.kotlin.util.NullCheckerKt;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class GetHomeFeedUseCase extends UseCase<HomeFeedListModel> {
    public static final String PARAM_RECOM_ID = "recomID";
    public static final String PARAM_COUNT = "count";
    public static final String PARAM_PAGE = "page";

    private Context context;
    private GraphqlUseCase graphqlUseCase;
    private HomeFeedMapper homeFeedMapper;

    public GetHomeFeedUseCase(Context context,
                              GraphqlUseCase graphqlUseCase,
                              HomeFeedMapper homeFeedMapper) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
        this.homeFeedMapper = homeFeedMapper;
    }

    @Override
    public Observable<HomeFeedListModel> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
        R.raw.gql_home_feed), HomeFeedContentGqlResponse.class, requestParams.getParameters());

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map(homeFeedMapper)
                .map(checkForNull());
    }

    private Func1<HomeFeedListModel, HomeFeedListModel> checkForNull() {
        return responseMap -> {
            NullCheckerKt.isContainNull(responseMap, errorMessage -> {
                String message = String.format("Found %s in %s", errorMessage, GetHomeFeedUseCase.class.getSimpleName());
                ContainNullException exception = new ContainNullException(message);
                if (!BuildConfig.DEBUG) {
                    Crashlytics.logException(exception);
                }
                throw exception;
            });
            return responseMap;
        };
    }

    public RequestParams getHomeFeedParam(int recomId, int count, int page) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_RECOM_ID, recomId);
        params.putInt(PARAM_COUNT, count);
        params.putInt(PARAM_PAGE, page);
        return params;
    }
}
