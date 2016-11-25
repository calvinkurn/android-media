package com.tokopedia.discovery.dynamicfilter.presenter;

import android.content.Intent;

import com.tokopedia.discovery.dynamicfilter.model.DynamicFilterModel;
import com.tokopedia.discovery.model.Breadcrumb;

import java.util.List;

/**
 * Created by noiz354 on 7/11/16.
 */
public interface DynamicFilterPresenter {

//    String VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT = "search_product";
//    String VALUES_DYNAMIC_FILTER_SEARCH_CATALOG = "search_catalog";
//    String VALUES_DYNAMIC_FILTER_SEARCH_SHOP = "search_shop";
//    String VALUES_DYNAMIC_FILTER_DIRECTORY = "directory";
//    String VALUES_DYNAMIC_FILTER_HOT_PRODUCT = "hot_product";

    String BREADCRUMB = "BREADCRUMB";
    String CURR_CATEGORY = "CURR_CATEGORY";
    String FILTER_CATEGORY = "FILTER_CATEGORY";
    String FILTER_SOURCE = "FILTER_SOURCE";

    String EXTRA_PRODUCT_BREADCRUMB_LIST = "EXTRA_PRODUCT_BREADCRUMB_LIST";
    String EXTRA_CURRENT_CATEGORY = "EXTRA_CURRENT_CATEGORY";
    String EXTRA_FILTER_CATEGORY_LIST = "EXTRA_FILTER_CATEGORY_LIST";
    String EXTRA_FILTER_SOURCE = "EXTRA_FILTER_SOURCE";

    void fetchExtras(Intent intent);

    List<Breadcrumb> getBreadCrumb();

    List<DynamicFilterModel.Filter> getFilterCategory();

    String getCurrentCategory();
}
