package com.tokopedia.tkpd.discovery.presenter;

import com.drew.lang.annotations.Nullable;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.tkpd.discovery.model.NetworkParam;
import com.tokopedia.tkpd.discovery.model.Breadcrumb;
import com.tokopedia.tkpd.discovery.model.BrowseProductActivityModel;
import com.tokopedia.tkpd.discovery.model.BrowseProductModel;

import java.util.List;

/**
 * Created by noiz354 on 3/24/16.
 */
public interface DiscoveryActivityPresenter {
    BrowseProductActivityModel getBrowseProductActivityModel();
    boolean isFragmentCreated(String TAG);
    void fetchIntent();

    /**
     * get firsttime data from activity for browse products
     * @param firstTimeOnly true means first time, false means not first time
     * @return data that already first fetch
     */
    BrowseProductModel getDataForBrowseProduct(boolean firstTimeOnly);

    @Nullable
    NetworkParam.Product getProductParam();

    @Nullable
    List<Breadcrumb> getProductBreadCrumb();

    String TAG = "DiscoveryActivity";
    String SEARCH_ACTION_INTENT = BuildConfig.APPLICATION_ID+".SEARCH";
    String CHANGE_GRID_ACTION_INTENT = BuildConfig.APPLICATION_ID+".LAYOUT";
    String SEARCH_TERM = "SEARCH_TERM";
    String GRID_TYPE_EXTRA = "GRID_TYPE_EXTRA";
    int REQUEST_SORT = 121;

    String DEPARTMENT_ID = "DEPARTMENT_ID";
    String FRAGMENT_ID = "FRAGMENT_ID";
    int INVALID_FRAGMENT_ID = -1;
    String AD_SRC = "AD_SRC";
    String ALIAS = "ALIAS";

    abstract class DiscoveryActivityPresenterImpl implements DiscoveryActivityPresenter{
        @Override
        public boolean isFragmentCreated(String TAG) {
            return false;
        }

        @Override
        public void fetchIntent() {

        }

        @Override
        public BrowseProductModel getDataForBrowseProduct(boolean firstTimeOnly) {
            return null;
        }

        @Override
        public NetworkParam.Product getProductParam() {
            return null;
        }
    }
}
