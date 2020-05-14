package com.tokopedia.shop.address.view.mapper;

import com.tokopedia.shop.address.view.model.ShopAddressViewModel;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoAddress;
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 12/14/17.
 */

public class ShopAddressViewModelMapper {

    @Inject
    public ShopAddressViewModelMapper() {
    }

    public List<ShopAddressViewModel> transform(ShopInfo shopInfo) {
        List<ShopAddressViewModel> visitableList = new ArrayList<>();
        ShopAddressViewModel shopAddressViewModel = new ShopAddressViewModel();
        shopAddressViewModel.setId(shopInfo.getAddressData().getId());
        shopAddressViewModel.setName(shopInfo.getAddressData().getName());
        shopAddressViewModel.setContent(shopInfo.getAddressData().getAddress());
        shopAddressViewModel.setArea(shopInfo.getAddressData().getArea());
        shopAddressViewModel.setEmail(shopInfo.getAddressData().getEmail());
        shopAddressViewModel.setPhone(shopInfo.getAddressData().getPhone());
        shopAddressViewModel.setFax(shopInfo.getAddressData().getFax());
        visitableList.add(shopAddressViewModel);
        return visitableList;
    }
}
