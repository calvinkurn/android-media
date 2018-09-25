package com.tokopedia.browse.homepage.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.browse.R;
import com.tokopedia.browse.common.data.DigitalBrowsePopularAnalyticsModel;
import com.tokopedia.browse.homepage.domain.subscriber.GetMarketplaceSubscriber;
import com.tokopedia.browse.homepage.domain.usecase.DigitalBrowseMarketplaceUseCase;
import com.tokopedia.browse.homepage.presentation.contract.DigitalBrowseMarketplaceContract;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseMarketplaceViewModel;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowsePopularBrandsViewModel;

import java.util.ArrayList;
import java.util.List;

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
                            public void onErrorGetMarketplace(Throwable throwable) {
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
    public void onDestroyView() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }

        detachView();
    }

    @Override
    public List<DigitalBrowsePopularAnalyticsModel> getPopularAnalyticsModelList(List<DigitalBrowsePopularBrandsViewModel> popularBrandsList) {
        int position = 0;
        List<DigitalBrowsePopularAnalyticsModel> popularAnalyticsModelList = new ArrayList<>();

        for (DigitalBrowsePopularBrandsViewModel item : popularBrandsList) {
            position++;
            DigitalBrowsePopularAnalyticsModel popularAnalyticsItem = new DigitalBrowsePopularAnalyticsModel();
            popularAnalyticsItem.setBannerId(item.getId());
            popularAnalyticsItem.setBrandName(item.getName());
            popularAnalyticsItem.setPosition(position);

            popularAnalyticsModelList.add(popularAnalyticsItem);
        }

        return popularAnalyticsModelList;
    }

    @Override
    public DigitalBrowsePopularAnalyticsModel getPopularAnalyticsModel(DigitalBrowsePopularBrandsViewModel viewModel, int position) {
        DigitalBrowsePopularAnalyticsModel popularAnalyticsModel = new DigitalBrowsePopularAnalyticsModel();
        popularAnalyticsModel.setBannerId(viewModel.getId());
        popularAnalyticsModel.setBrandName(viewModel.getName());
        popularAnalyticsModel.setPosition(position+1);

        return popularAnalyticsModel;
    }

    @Override
    public void onErrorGetMarketplace(Throwable throwable) {
        if (isViewAttached()) {
            if (getView().getCategoryItemCount() < 2) {
                getView().showGetDataError(throwable);
            }
        }
    }

    @Override
    public void onSuccessGetMarketplace(DigitalBrowseMarketplaceViewModel digitalBrowseMarketplaceData) {
        getView().renderData(digitalBrowseMarketplaceData);
        getView().sendPopularImpressionAnalytics(getPopularAnalyticsModelList(
                digitalBrowseMarketplaceData.getPopularBrandsList()));
    }
}
