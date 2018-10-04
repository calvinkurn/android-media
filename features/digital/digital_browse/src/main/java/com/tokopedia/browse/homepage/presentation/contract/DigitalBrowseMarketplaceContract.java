package com.tokopedia.browse.homepage.presentation.contract;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.browse.common.data.DigitalBrowsePopularAnalyticsModel;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseMarketplaceViewModel;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowsePopularBrandsViewModel;

import java.util.List;

/**
 * @author by furqan on 30/08/18.
 */

public interface DigitalBrowseMarketplaceContract {

    interface View extends CustomerView {

        Context getContext();

        void renderData(DigitalBrowseMarketplaceViewModel marketplaceData);

        void showGetDataError(Throwable e);

        int getCategoryItemCount();

        void sendPopularImpressionAnalytics(List<DigitalBrowsePopularAnalyticsModel> analyticsModelList);

    }

    interface Presenter {

        void onInit();

        void getMarketplaceDataCloud();

        void onDestroyView();

        List<DigitalBrowsePopularAnalyticsModel> getPopularAnalyticsModelList(List<DigitalBrowsePopularBrandsViewModel> popularBrandsList);

        DigitalBrowsePopularAnalyticsModel getPopularAnalyticsModel(DigitalBrowsePopularBrandsViewModel viewModel, int position);
    }

}
