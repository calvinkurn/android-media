package com.tokopedia.digital.product.view.listener;

import com.tokopedia.common_digital.product.presentation.model.Operator;

import java.util.List;

/**
 * Created by Rizky on 12/19/17.
 */

public interface IOperatorChooserView {

    void showOperators(List<Operator> operators);

    void showInitialProgressLoading();

    void hideInitialProgressLoading();

}
