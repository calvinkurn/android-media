package com.tokopedia.explore.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by milhamj on 20/07/18.
 */

public interface ExploreFragmentListener {

    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {

    }

}
