package com.tokopedia.digital.product.view.presenter;

import com.tokopedia.digital.product.domain.interactor.GetOperatorsByCategoryIdUseCase;
import com.tokopedia.digital.product.view.listener.IOperatorChooserView;
import com.tokopedia.digital.product.view.model.Operator;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Rizky on 12/19/17.
 */

public class OperatorChooserPresenter implements IOperatorChooserPresenter {

    private GetOperatorsByCategoryIdUseCase getOperatorsByCategoryIdUseCase;
    private IOperatorChooserView view;

    public OperatorChooserPresenter(IOperatorChooserView view,
                                    GetOperatorsByCategoryIdUseCase getOperatorsByCategoryIdUseCase) {
        this.view = view;
        this.getOperatorsByCategoryIdUseCase = getOperatorsByCategoryIdUseCase;
    }

    @Override
    public void getOperatorsByCategoryId(String categoryId) {
        view.showInitialProgressLoading();

        getOperatorsByCategoryIdUseCase.execute(
                getOperatorsByCategoryIdUseCase.createRequestParam(categoryId),
                new Subscriber<List<Operator>>() {
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
                }
        );
    }

}
