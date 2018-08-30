package com.tokopedia.digital.product.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.common_digital.product.presentation.model.Operator;

import java.util.List;

/**
 * Created by Rizky on 29/08/18.
 */
public class OperatorChooserContract {

    public interface View extends CustomerView {

        void showOperators(List<Operator> operators);

        void showInitialProgressLoading();

        void hideInitialProgressLoading();

    }

    public interface Presenter extends CustomerPresenter<View> {

        void getOperatorsByCategoryId(String categoryId);

    }

}
