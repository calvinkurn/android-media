package com.tokopedia.instantloan.view.contractor;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.instantloan.view.model.BannerViewModel;

import java.util.List;

/**
 * Created by lavekush on 22/03/18.
 */

public interface BannerContractor {

    interface View extends CustomerView {
        void renderUserList(List<BannerViewModel> banners);
        void nextBanner();
        void previousBanner();
    }

    interface Presenter extends CustomerPresenter<View> {
        void loadBanners();
    }
}
