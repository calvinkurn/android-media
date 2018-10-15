package com.tokopedia.browse.homepage.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.browse.R;
import com.tokopedia.browse.common.data.DigitalBrowseServiceAnalyticsModel;
import com.tokopedia.browse.homepage.domain.subscriber.GetDigitalCategorySubscriber;
import com.tokopedia.browse.homepage.domain.usecase.DigitalBrowseServiceUseCase;
import com.tokopedia.browse.homepage.presentation.contract.DigitalBrowseServiceContract;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceCategoryViewModel;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceViewModel;
import com.tokopedia.browse.homepage.presentation.model.IndexPositionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by furqan on 07/09/18.
 */

public class DigitalBrowseServicePresenter extends BaseDaggerPresenter<DigitalBrowseServiceContract.View>
        implements DigitalBrowseServiceContract.Presenter, GetDigitalCategorySubscriber.DigitalCategoryActionListener {

    private DigitalBrowseServiceUseCase digitalBrowseServiceUseCase;
    private CompositeSubscription compositeSubscription;

    @Inject
    public DigitalBrowseServicePresenter(DigitalBrowseServiceUseCase digitalBrowseServiceUseCase) {
        this.digitalBrowseServiceUseCase = digitalBrowseServiceUseCase;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onInit() {
        getCategoryDataFromCache();
    }

    @Override
    public void getDigitalCategoryCloud() {
        compositeSubscription.add(
                digitalBrowseServiceUseCase.createObservable(
                        GraphqlHelper.loadRawString(getView().getContext().getResources(),
                                R.raw.digital_browse_category_query))
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new GetDigitalCategorySubscriber(this, getView().getContext()))
        );
    }

    @Override
    public void processTabData(Map<String, IndexPositionModel> titleMap, DigitalBrowseServiceViewModel viewModel, int categoryId) {
        getView().showTab();

        String selectedTab = "";
        int selectedTabIndex = 0;

        for (DigitalBrowseServiceCategoryViewModel item : viewModel.getCategoryViewModelList()) {
            if (item.isTitle() && item.getId() == categoryId) {
                selectedTab = item.getName();
                break;
            }
        }

        List<String> titleList = new ArrayList<>();
        for (int i = 0; i < titleMap.size(); i++) {
            titleList.add("");
        }

        for (Map.Entry<String, IndexPositionModel> entry : titleMap.entrySet()) {
            titleList.set(entry.getValue().getIndexPositionInTab(), entry.getKey());

            if (entry.getKey().equals(selectedTab)) {
                selectedTabIndex = entry.getValue().getIndexPositionInTab();
            }
        }

        for (String title : titleList) {
            if (!title.equals("")) {
                getView().addTab(title);
            }
        }

        getView().renderTab(selectedTabIndex);
    }

    @Override
    public DigitalBrowseServiceAnalyticsModel getItemPositionInGroup(Map<String, IndexPositionModel> titleMap, int itemPositionInList) {
        DigitalBrowseServiceAnalyticsModel model = new DigitalBrowseServiceAnalyticsModel();
        int lastTitlePosition = 0;

        for (Map.Entry<String, IndexPositionModel> entry : titleMap.entrySet()) {
            if (lastTitlePosition <= entry.getValue().getIndexPositionInList() &&
                    entry.getValue().getIndexPositionInList() < itemPositionInList) {

                lastTitlePosition = entry.getValue().getIndexPositionInList();

                model.setHeaderName(entry.getKey());
                model.setHeaderPosition(entry.getValue().getIndexPositionInTab() + 1);
            }
        }

        model.setIconPosition(itemPositionInList - lastTitlePosition);

        return model;
    }

    @Override
    public void onErrorGetDigitalCategory(Throwable throwable) {
        if (isViewAttached()) {
            if (getView().getItemCount() < 2) {
                getView().showGetDataError(throwable);
            }
        }
    }

    @Override
    public void onSuccessGetDigitalCategory(DigitalBrowseServiceViewModel digitalBrowseServiceViewModel) {
        getView().renderData(digitalBrowseServiceViewModel);
    }

    private void getCategoryDataFromCache() {
        compositeSubscription.add(
                digitalBrowseServiceUseCase.getCategoryDataFromCache()
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new GetDigitalCategorySubscriber(new GetDigitalCategorySubscriber.DigitalCategoryActionListener() {
                            @Override
                            public void onErrorGetDigitalCategory(Throwable throwable) {
                                getDigitalCategoryCloud();
                            }

                            @Override
                            public void onSuccessGetDigitalCategory(DigitalBrowseServiceViewModel digitalBrowseServiceViewModel) {
                                if (digitalBrowseServiceViewModel != null &&
                                        digitalBrowseServiceViewModel.getCategoryViewModelList() != null &&
                                        digitalBrowseServiceViewModel.getCategoryViewModelList().size() > 0) {
                                    getView().renderData(digitalBrowseServiceViewModel);
                                }

                                getDigitalCategoryCloud();
                            }
                        }, getView().getContext()))
        );
    }

    @Override
    public void onDestroyView() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }

        detachView();
    }
}
