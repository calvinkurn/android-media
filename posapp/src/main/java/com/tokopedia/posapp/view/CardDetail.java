package com.tokopedia.posapp.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.posapp.view.viewmodel.card.CreditCardViewModel;

/**
 * Created by okasurya on 8/18/17.
 */

public interface CardDetail {
    interface Presenter extends CustomerPresenter<View> {
        void pay(CreditCardViewModel creditCardViewModel);
    }

    interface View extends CustomerView {

    }
}
