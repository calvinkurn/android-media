package com.tokopedia.digital.product.presenter;

import com.tokopedia.digital.product.listener.IOperatorChooserView;
import com.tokopedia.digital.widget.interactor.IDigitalWidgetInteractor;
import com.tokopedia.digital.widget.model.operator.Operator;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Rizky on 12/19/17.
 */

public class OperatorChooserPresenter implements IOperatorChooserPresenter {

    private IDigitalWidgetInteractor widgetInteractor;
    private IOperatorChooserView view;

    public OperatorChooserPresenter(IOperatorChooserView view, IDigitalWidgetInteractor widgetInteractor) {
        this.view = view;
        this.widgetInteractor = widgetInteractor;
    }

    @Override
    public void getOperatorsByCategoryId(String categoryId) {
        view.showInitialProgressLoading();

        widgetInteractor.getOperatorsByCategoryId(new Subscriber<List<Operator>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Operator> operators) {
                view.hideInitialProgressLoading();

                if (!operators.isEmpty()) {
                    view.showOperators(operators);
                }
            }
        }, Integer.valueOf(categoryId));
    }

}
