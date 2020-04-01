package com.tokopedia.shop.newproduct.view.datamodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.shop.newproduct.view.adapter.EtalaseHighlightAdapterTypeFactory;
import com.tokopedia.shop.newproduct.view.adapter.ShopProductAdapterTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class EtalaseHighlightCarouselViewModel implements Visitable<BaseAdapterTypeFactory>{

    private List<ShopProductViewModel> shopProductViewModelList;
    private ShopProductEtalaseChipItemViewModel shopEtalaseViewModel;

    public EtalaseHighlightCarouselViewModel(List<ShopProductViewModel> shopProductViewModelList,
                                             ShopProductEtalaseChipItemViewModel shopEtalaseViewModel) {
        setShopProductEtalaseHighLightViewModel(shopProductViewModelList);
        setEtalaseViewModel(shopEtalaseViewModel);
    }

    private void setShopProductEtalaseHighLightViewModel(List<ShopProductViewModel> shopProductViewModelList) {
        if (shopProductViewModelList == null) {
            this.shopProductViewModelList = new ArrayList<>();
        } else {
            this.shopProductViewModelList = shopProductViewModelList;
        }
    }

    public List<ShopProductViewModel> getShopProductViewModelList() {
        return shopProductViewModelList;
    }

    public ShopProductEtalaseChipItemViewModel getShopEtalaseViewModel() {
        return shopEtalaseViewModel;
    }

    private void setEtalaseViewModel(ShopProductEtalaseChipItemViewModel shopEtalaseViewModel) {
        this.shopEtalaseViewModel = shopEtalaseViewModel;
    }

    @Override
    public int type(BaseAdapterTypeFactory typeFactory) {
        if (typeFactory instanceof EtalaseHighlightAdapterTypeFactory) {
            return ((EtalaseHighlightAdapterTypeFactory)typeFactory).type(this);
        } else if (typeFactory instanceof ShopProductAdapterTypeFactory){
            return ((ShopProductAdapterTypeFactory)typeFactory).type(this);
        } else {
            return 0;
        }
    }

}
