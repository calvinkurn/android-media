package com.tokopedia.browse.homepage.presentation.contract;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceCategoryViewModel;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceViewModel;
import com.tokopedia.browse.homepage.presentation.model.IndexPositionModel;

import java.util.List;
import java.util.Map;

/**
 * @author by furqan on 30/08/18.
 */

public interface DigitalBrowseServiceContract {

    interface View extends CustomerView {

        Context getContext();

        void renderData(DigitalBrowseServiceViewModel viewModel);

        void addTab(String key);
    }

    interface Presenter {

        void onInit();

        void getDigitalCategoryCloud();

        void processTabData(Map<String, IndexPositionModel> titleMap);

    }
}
