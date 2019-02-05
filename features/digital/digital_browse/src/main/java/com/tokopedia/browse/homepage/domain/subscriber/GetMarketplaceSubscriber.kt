package com.tokopedia.browse.homepage.domain.subscriber

import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseMarketplaceViewModel
import rx.Subscriber

/**
 * @author by furqan on 04/09/18.
 */

class GetMarketplaceSubscriber(private val marketplaceActionListener: MarketplaceActionListener) : Subscriber<DigitalBrowseMarketplaceViewModel>() {

    override fun onCompleted() {

    }

    override fun onError(throwable: Throwable) {
        marketplaceActionListener.onErrorGetMarketplace(throwable)
    }

    override fun onNext(marketplaceViewModel: DigitalBrowseMarketplaceViewModel) {
        marketplaceActionListener.onSuccessGetMarketplace(marketplaceViewModel)
    }


    interface MarketplaceActionListener {

        fun onErrorGetMarketplace(throwable: Throwable)

        fun onSuccessGetMarketplace(digitalBrowseMarketplaceData: DigitalBrowseMarketplaceViewModel)

    }
}
