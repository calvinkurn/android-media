package com.tokopedia.tkpd.feedplus;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * @author by nisie on 5/15/17.
 */

public interface FeedPlus{

    public interface View extends CustomerView {
    }

    public interface Presenter extends CustomerPresenter<View>{
    }
}
