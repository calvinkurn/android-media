package com.tokopedia.tkpd.tkpdcustomer.feedplus.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * @author by nisie on 5/18/17.
 */

public interface FeedPlusDetail {

    public interface View extends CustomerView {

        void onWishlistClicked();
    }

    public interface Presenter extends CustomerPresenter<View> {
    }
}
