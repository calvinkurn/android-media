package com.tokopedia.browse.homepage.presentation.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.browse.R;
import com.tokopedia.browse.homepage.domain.subscriber.GetMarketplaceSubscriber;
import com.tokopedia.browse.homepage.domain.usecase.DigitalBrowseMarketplaceUseCase;
import com.tokopedia.browse.homepage.presentation.contract.DigitalBrowseMarketplaceContract;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseMarketplaceViewModel;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by furqan on 04/09/18.
 */

public class DigitalBrowseMarketplacePresenter extends BaseDaggerPresenter<DigitalBrowseMarketplaceContract.View>
        implements DigitalBrowseMarketplaceContract.Presenter, GetMarketplaceSubscriber.MarketplaceActionListener {

    private DigitalBrowseMarketplaceUseCase digitalBrowseMarketplaceUseCase;
    private CompositeSubscription compositeSubscription;

    @Inject
    public DigitalBrowseMarketplacePresenter(DigitalBrowseMarketplaceUseCase digitalBrowseMarketplaceUseCase) {
        this.digitalBrowseMarketplaceUseCase = digitalBrowseMarketplaceUseCase;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onInit() {
        getMarketplaceDataCache();
    }

    private void getMarketplaceDataCache() {
        compositeSubscription.add(
                digitalBrowseMarketplaceUseCase.getMarketplaceDataFromCache()
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new GetMarketplaceSubscriber(new GetMarketplaceSubscriber.MarketplaceActionListener() {
                            @Override
                            public void onErrorGetMarketplace(String errorMessage) {
                                Log.e("ERROR", errorMessage);
                                getMarketplaceDataCloud();
                            }

                            @Override
                            public void onSuccessGetMarketplace(DigitalBrowseMarketplaceViewModel digitalBrowseMarketplaceData) {
                                if (digitalBrowseMarketplaceData.getRowViewModelList().size() > 0 &&
                                        digitalBrowseMarketplaceData.getPopularBrandsList().size() > 0) {
                                    getView().renderData(digitalBrowseMarketplaceData);
                                }

                                getMarketplaceDataCloud();
                            }
                        }, getView().getContext()))
        );
    }

    @Override
    public void getMarketplaceDataCloud() {
        compositeSubscription.add(
                digitalBrowseMarketplaceUseCase.createObservable(
                        GraphqlHelper.loadRawString(getView().getContext().getResources(),
                                R.raw.digital_browse_category_query),
                        GraphqlHelper.loadRawString(getView().getContext().getResources(),
                                R.raw.digital_browse_brand_query))
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new GetMarketplaceSubscriber(this, getView().getContext()))
        );
    }

    @Override
    public void onErrorGetMarketplace(String errorMessage) {
        Log.e("ERROR", errorMessage);
    }

    @Override
    public void onSuccessGetMarketplace(DigitalBrowseMarketplaceViewModel digitalBrowseMarketplaceData) {
        getView().renderData(digitalBrowseMarketplaceData);
    }
}
