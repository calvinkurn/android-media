package com.tokopedia.home.explore.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.home.explore.view.adapter.datamodel.CategoryFavoriteDataModel;
import com.tokopedia.home.explore.view.adapter.datamodel.CategoryGridListDataModel;
import com.tokopedia.home.explore.view.adapter.datamodel.MyShopDataModel;
import com.tokopedia.home.explore.view.adapter.datamodel.SellDataModel;

/**
 * Created by errysuprayogi on 1/26/18.
 */

public interface TypeFactory extends AdapterTypeFactory {

    int type(CategoryGridListDataModel viewModel);

    int type(SellDataModel viewModel);

    int type(CategoryFavoriteDataModel viewModel);

    int type(MyShopDataModel viewModel);

}
