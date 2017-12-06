package com.tokopedia.tkpd.thankyou.data.source;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.tkpdfeed.feeds.GetKolComments;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetKolCommentsUseCase;

import rx.Observable;

/**
 * Created by okasurya on 12/5/17.
 */

public class MarketplaceTrackerCloudSource extends ThanksTrackerCloudSource {
    private ApolloClient apolloClient;

    public MarketplaceTrackerCloudSource(RequestParams requestParams) {
        super(requestParams);
    }

    @Override
    public Observable<String> sendAnalytics() {
//        ApolloWatcher<MarkeplacePaymentData.Data> apolloWatcher = apolloClient.newCall(
//                GetKolComments.builder()
//                        .idPost(requestParams.getInt(GetKolCommentsUseCase.PARAM_ID, 0))
//                        .cursor(requestParams.getString(GetKolCommentsUseCase.PARAM_CURSOR, ""))
//                        .limit(requestParams.getInt(GetKolCommentsUseCase.PARAM_LIMIT,
//                                GetKolCommentsUseCase.DEFAULT_LIMIT))
//                        .build()).watcher();
//        return RxApollo.from(apolloWatcher).map(new F);
        return null;
    }
}
