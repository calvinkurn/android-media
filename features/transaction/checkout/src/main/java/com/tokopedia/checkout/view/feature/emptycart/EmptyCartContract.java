package com.tokopedia.checkout.view.feature.emptycart;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by Irfan Khoirul on 14/09/18.
 */

public interface EmptyCartContract {

    interface View extends CustomerView {
    }

    interface Presenter extends CustomerPresenter<View> {
    }

}
