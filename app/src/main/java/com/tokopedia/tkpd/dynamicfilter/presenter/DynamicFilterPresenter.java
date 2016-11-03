package com.tokopedia.tkpd.dynamicfilter.presenter;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.tkpd.discovery.model.Breadcrumb;
import com.tokopedia.tkpd.dynamicfilter.model.DynamicFilterModel;

import java.util.List;

/**
 * Created by noiz354 on 7/11/16.
 */
public interface DynamicFilterPresenter {

    String SEARCH_PRODUCT = "search_product";
    String SEARCH_CATALOG = "search_catalog";
    String SEARCH_SHOP = "search_shop";
    String DIRECTORY = "directory";
    String CATALOG_PRODUCT = "catalog_product";
    String HOT_PRODUCT = "hot_product";

    String INPUT_TYPE_CHECKBOX = "checkbox";
    String INPUT_TYPE_TEXT = "textbox";

    int SEARCHABLE = 1;
    int UNSEARCHABLE = 0;
    String BREADCRUMB = "BREADCRUMB";
    String CURR_CATEGORY = "CURR_CATEGORY";
    String FILTER_CATEGORY = "FILTER_CATEGORY";
    String FILTER_SOURCE = "FILTER_SOURCE";

    void fetchExtras(Intent intent);
    List<Breadcrumb> getBreadCrumb();
    List<DynamicFilterModel.Filter> getFilterCategory();
    String getCurrentCategory();
}
