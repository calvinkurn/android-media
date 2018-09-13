package com.tokopedia.browse.homepage.presentation.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.browse.R;
import com.tokopedia.browse.homepage.domain.subscriber.GetDigitalCategorySubscriber;
import com.tokopedia.browse.homepage.domain.usecase.DigitalBrowseServiceUseCase;
import com.tokopedia.browse.homepage.presentation.contract.DigitalBrowseServiceContract;
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
        getDigitalCategoryCloud();
    }

    @Override
    public void getDigitalCategoryCloud() {
        compositeSubscription.add(
                digitalBrowseServiceUseCase.createObservable(
                        GraphqlHelper.loadRawString(getView().getContext().getResources(),
                                R.raw.digital_browser_brand_query))
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new GetDigitalCategorySubscriber(this, getView().getContext()))
        );
    }

    @Override
    public void processTabData(Map<String, IndexPositionModel> titleMap) {
        getView().showTab();

        List<String> titleList = new ArrayList<>();
        for (int i = 0; i < titleMap.size(); i++) {
            titleList.add("");
        }

        for (Map.Entry<String, IndexPositionModel> entry : titleMap.entrySet()) {
            titleList.set(entry.getValue().getIndexPositionInTab(), entry.getKey());
        }

        for (String title : titleList) {
            if (!title.equals("")) {
                getView().addTab(title);
            }
        }

        getView().renderTab();
    }

    @Override
    public void onErrorGetDigitalCategory(String errorMessage) {
        Log.e("Error", errorMessage);
    }

    @Override
    public void onSuccessGetDigitalCategory(DigitalBrowseServiceViewModel digitalBrowseServiceViewModel) {
        getView().renderData(digitalBrowseServiceViewModel);
    }
}
