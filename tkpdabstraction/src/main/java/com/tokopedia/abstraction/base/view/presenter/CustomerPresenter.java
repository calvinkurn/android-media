package com.tokopedia.abstraction.base.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * @author kulomady on 11/20/16.
 */

public interface CustomerPresenter<V extends CustomerView> {

    void attachView(V view);

    void detachView();
}
