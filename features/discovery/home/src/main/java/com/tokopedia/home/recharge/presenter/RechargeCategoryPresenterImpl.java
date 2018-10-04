package com.tokopedia.home.recharge.presenter;
import android.content.Context;

import com.tokopedia.digital.widget.domain.interactor.DigitalWidgetUseCase;
import com.tokopedia.digital.widget.errorhandle.WidgetRuntimeException;
import com.tokopedia.digital.widget.view.model.category.Category;
import com.tokopedia.home.recharge.view.RechargeCategoryView;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Subscriber;

/**
 * @author kulomady 05 on 7/13/2016.
 * Modify by Nabilla Sabbaha on 8/16/2017.
 * Modified by rizkyfadillah at 10/6/17.
 */
public class RechargeCategoryPresenterImpl implements RechargeCategoryPresenter {

    private static final String TAG = RechargeCategoryPresenterImpl.class.getSimpleName();

    private Context context;
    private RechargeCategoryView view;
    private DigitalWidgetUseCase digitalWidgetUseCase;

    private List<Category> categoryList;

    public RechargeCategoryPresenterImpl(Context context,
                                         RechargeCategoryView view,
                                         DigitalWidgetUseCase digitalWidgetUseCase) {
        this.context = context;
        this.view = view;
        this.digitalWidgetUseCase = digitalWidgetUseCase;
    }

    @Override
    public void fetchDataRechargeCategory() {
        digitalWidgetUseCase.execute(RequestParams.EMPTY, new Subscriber<List<Category>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof WidgetRuntimeException) {
                    view.renderErrorMessage();
                } else {
                    view.renderErrorNetwork();
                }
            }

            @Override
            public void onNext(List<Category> categories) {
                categoryList = categories;
                finishPrepareRechargeModule();
            }
        });
    }

    private void finishPrepareRechargeModule() {
        if (context != null && view != null) {
            if (categoryList != null) {
                view.renderDataRechargeCategory(categoryList);
            } else {
                view.failedRenderDataRechargeCategory();
            }
        }
    }

    @Override
    public void onDestroy() {
        digitalWidgetUseCase.unsubscribe();
    }

}