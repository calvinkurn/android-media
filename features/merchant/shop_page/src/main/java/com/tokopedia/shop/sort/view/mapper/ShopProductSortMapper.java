package com.tokopedia.shop.sort.view.mapper;

import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort;
import com.tokopedia.shop.sort.view.model.ShopProductSortModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 2/25/18.
 */

public class ShopProductSortMapper {

    public List<ShopProductSortModel> convertSort(List<ShopProductSort> shopProductSortList) {
        List<ShopProductSortModel> result = new ArrayList<>();
        for (ShopProductSort data : shopProductSortList) {
            ShopProductSortModel shopProductFilterModel = new ShopProductSortModel();
            shopProductFilterModel.setInputType(data.getInputType());
            shopProductFilterModel.setKey(data.getKey());
            shopProductFilterModel.setName(data.getName());
            shopProductFilterModel.setValue(data.getValue());
            result.add(shopProductFilterModel);
        }
        return result;
    }
}
