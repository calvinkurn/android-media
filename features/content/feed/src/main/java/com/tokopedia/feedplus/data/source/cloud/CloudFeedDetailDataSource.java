package com.tokopedia.feedplus.data.source.cloud;

import android.content.Context;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.tkpdfeed.feeds.FeedDetail;
import com.tokopedia.feedplus.data.mapper.FeedDetailListMapper;
import com.tokopedia.feedplus.domain.model.feeddetail.DataFeedDetailDomain;
import com.tokopedia.feedplus.domain.usecase.GetFeedsDetailUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * @author by nisie on 5/24/17.
 */

public class CloudFeedDetailDataSource {

    private static final int LIMIT_DETAIL = 30;
    private ApolloClient apolloClient;
    private Context context;
    private FeedDetailListMapper feedDetailListMapper;

    public CloudFeedDetailDataSource(Context context,
                                     ApolloClient apolloClient,
                                     FeedDetailListMapper feedDetailListMapper) {

        this.apolloClient = apolloClient;
        this.context = context;
        this.feedDetailListMapper = feedDetailListMapper;
    }

    public Observable<List<DataFeedDetailDomain>> getFeedsDetailList(RequestParams requestParams) {
        ApolloWatcher<FeedDetail.Data> apolloWatcher = apolloClient.newCall(
                FeedDetail.builder()
                        .detailID(getDetailId(requestParams))
                        .userID(Integer.parseInt(
                                requestParams.getString(GetFeedsDetailUseCase
                                        .PARAM_USER_ID, "0")))
                        .limitDetail(LIMIT_DETAIL)
                        .pageDetail(getPaging(requestParams))
                        .build()).watcher();

        return RxApollo.from(apolloWatcher).map(feedDetailListMapper);
    }

    private int getPaging(RequestParams requestParams) {
        return requestParams.getInt(GetFeedsDetailUseCase.PARAM_PAGE, 1);
    }

    private String getDetailId(RequestParams requestParams) {
        return requestParams.getString(GetFeedsDetailUseCase.PARAM_DETAIL_ID, "");
    }


}
