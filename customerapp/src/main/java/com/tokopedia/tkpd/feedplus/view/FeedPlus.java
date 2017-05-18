package com.tokopedia.tkpd.feedplus.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductCardViewModel;

/**
 * @author by nisie on 5/15/17.
 */

public interface FeedPlus{

    public interface View extends CustomerView {
        void onShareButtonClicked();

        void onGoToProductDetail();

        void onGoToFeedDetail(ProductCardViewModel productCardViewModel);

        void onGoToShopDetail();
    }

    public interface Presenter extends CustomerPresenter<View>{
    }
}
