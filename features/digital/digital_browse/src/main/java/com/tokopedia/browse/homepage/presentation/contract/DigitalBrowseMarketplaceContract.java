package com.tokopedia.browse.homepage.presentation.contract;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseMarketplaceViewModel;

/**
 * @author by furqan on 30/08/18.
 */

public interface DigitalBrowseMarketplaceContract {

    interface View extends CustomerView {

        Context getContext();

        void renderData(DigitalBrowseMarketplaceViewModel marketplaceData);

        void showGetDataError(Throwable e);

        int getCategoryItemCount();

    }

    interface Presenter {

        void onInit();

        void getMarketplaceDataCloud();

    }

}
