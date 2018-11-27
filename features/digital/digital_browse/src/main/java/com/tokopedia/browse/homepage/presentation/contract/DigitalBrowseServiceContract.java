package com.tokopedia.browse.homepage.presentation.contract;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.browse.common.data.DigitalBrowseServiceAnalyticsModel;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceViewModel;
import com.tokopedia.browse.homepage.presentation.model.IndexPositionModel;

import java.util.Map;

/**
 * @author by furqan on 30/08/18.
 */

public interface DigitalBrowseServiceContract {

    interface View extends CustomerView {

        Context getContext();

        void renderData(DigitalBrowseServiceViewModel viewModel);

        void showTab();

        void hideTab();

        void addTab(String key);

        void renderTab(int selectedTabIndex);

        void showGetDataError(Throwable e);

        int getItemCount();
    }

    interface Presenter {

        void onInit();

        void getDigitalCategoryCloud();

        void processTabData(Map<String, IndexPositionModel> titleMap, DigitalBrowseServiceViewModel viewModel, int categoryId);

        DigitalBrowseServiceAnalyticsModel getItemPositionInGroup(Map<String, IndexPositionModel> titleMap, int itemPositionInList);

        void onDestroyView();

    }
}
