package com.tokopedia.digital.product.listener;

import android.view.View;

import com.tokopedia.digital.widget.model.operator.Operator;

import java.util.List;

/**
 * Created by Rizky on 12/19/17.
 */

public interface IOperatorChooserView {

    void showOperators(List<Operator> operators);

    void showInitialProgressLoading();

    void hideInitialProgressLoading();

}
