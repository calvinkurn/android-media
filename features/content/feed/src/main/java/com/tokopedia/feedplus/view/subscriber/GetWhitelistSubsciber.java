package com.tokopedia.feedplus.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.feedplus.data.pojo.WhitelistQuery;
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import rx.Subscriber;

/**
 * @author by yfsx on 20/06/18.
 */
public class GetWhitelistSubsciber extends Subscriber<GraphqlResponse> {

    private FeedPlus.View mainView;

    public GetWhitelistSubsciber(FeedPlus.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        mainView.finishLoadingProgress();
        mainView.onErrorGetWhitelist(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(GraphqlResponse response) {
        mainView.finishLoadingProgress();
        WhitelistDomain domain = mappingWhitelistDomain(response);
        if (domain == null) {
            mainView.onErrorGetWhitelist(ErrorHandler.getErrorMessage(new Throwable()));
        } else  if (!TextUtils.isEmpty(domain.getError())) {
            mainView.onErrorGetWhitelist(domain.getError());
        } else {
            mainView.onSuccessGetWhitelist(domain.isWhitelist());
        }
    }

    private WhitelistDomain mappingWhitelistDomain(GraphqlResponse response) {
        WhitelistQuery query = response.getData(WhitelistQuery.class);
        if (query == null) return null;
        else {
            WhitelistDomain domain = new WhitelistDomain();
            domain.setError(query.getWhitelist().getError());
            domain.setUrl(query.getWhitelist().getUrl());
            domain.setWhitelist(query.getWhitelist().isWhitelist());
            return domain;
        }
    }
}
