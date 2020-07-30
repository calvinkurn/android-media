package com.tokopedia.digital.product.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.digital.product.domain.interactor.GetOperatorsByCategoryIdUseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Rizky on 12/19/17.
 */

public class OperatorChooserPresenter extends BaseDaggerPresenter<OperatorChooserContract.View> implements OperatorChooserContract.Presenter {

    private GetOperatorsByCategoryIdUseCase getOperatorsByCategoryIdUseCase;

    @Inject
    public OperatorChooserPresenter(GetOperatorsByCategoryIdUseCase getOperatorsByCategoryIdUseCase) {
        this.getOperatorsByCategoryIdUseCase = getOperatorsByCategoryIdUseCase;
    }

    @Override
    public void getOperatorsByCategoryId(String categoryId) {
        getView().showInitialProgressLoading();

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
                        getView().hideInitialProgressLoading();

                        if (!operators.isEmpty()) {
                            getView().showOperators(operators);
                        }
                    }
                }
        );
    }

}
