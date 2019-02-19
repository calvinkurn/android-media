package com.tokopedia.home.beranda.data.mapper;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.home.BuildConfig;
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedGqlResponse;
import com.tokopedia.home.beranda.domain.gql.feed.Product;
import com.tokopedia.home.beranda.domain.gql.feed.RecommendationProduct;
import com.tokopedia.home.beranda.presentation.view.subscriber.GetHomeFeedsSubscriber;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedListModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel;
import com.tokopedia.kotlin.util.ContainNullException;
import com.tokopedia.kotlin.util.NullCheckerKt;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by henrypriyono on 12/29/17.
 */

public class CheckNullMapper implements Func1<GraphqlResponse, GraphqlResponse> {

    @Override
    public GraphqlResponse call(GraphqlResponse graphqlResponse) {
        NullCheckerKt.isContainNull(graphqlResponse, errorMessage -> {
            String message = String.format("Found %s in %s",
                    errorMessage, GetHomeFeedsSubscriber.class.getSimpleName());
            ContainNullException exception = new ContainNullException(message);
            if (!BuildConfig.DEBUG) {
                Crashlytics.logException(exception);
            }
            throw exception;
        });
        return graphqlResponse;
    }
}