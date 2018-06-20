package com.tokopedia.feedplus.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain;
import com.tokopedia.feedplus.view.listener.FeedPlus;

import rx.Subscriber;

/**
 * @author by yfsx on 20/06/18.
 */
public class GetWhitelistSubsciber extends Subscriber<WhitelistDomain> {

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
    public void onNext(WhitelistDomain whitelistDomain) {
        mainView.finishLoadingProgress();
        mainView.onSuccessGetWhitelist(whitelistDomain.getContent().isWhitelist());
    }
}
