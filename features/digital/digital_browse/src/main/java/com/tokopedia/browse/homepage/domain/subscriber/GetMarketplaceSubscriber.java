package com.tokopedia.browse.homepage.domain.subscriber;

import android.content.Context;

import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseMarketplaceViewModel;

import rx.Subscriber;

/**
 * @author by furqan on 04/09/18.
 */

public class GetMarketplaceSubscriber extends Subscriber<DigitalBrowseMarketplaceViewModel> {

    private final MarketplaceActionListener marketplaceActionListener;
    private Context context;

    public GetMarketplaceSubscriber(MarketplaceActionListener marketplaceActionListener, Context context) {
        this.marketplaceActionListener = marketplaceActionListener;
        this.context = context;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        marketplaceActionListener.onErrorGetMarketplace(throwable);
    }

    @Override
    public void onNext(DigitalBrowseMarketplaceViewModel marketplaceViewModel) {
        marketplaceActionListener.onSuccessGetMarketplace(marketplaceViewModel);
    }


    public interface MarketplaceActionListener {

        void onErrorGetMarketplace(Throwable throwable);

        void onSuccessGetMarketplace(DigitalBrowseMarketplaceViewModel digitalBrowseMarketplaceData);

    }
}
