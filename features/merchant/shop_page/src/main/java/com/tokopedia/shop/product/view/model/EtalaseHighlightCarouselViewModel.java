package com.tokopedia.shop.product.view.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.view.adapter.EtalaseHighlightAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class EtalaseHighlightCarouselViewModel implements Visitable<BaseAdapterTypeFactory>{

    private List<ShopProductViewModel> shopProductViewModelList;
    private ShopEtalaseViewModel shopEtalaseViewModel;

    public EtalaseHighlightCarouselViewModel(List<ShopProductViewModel> shopProductViewModelList,
                                             ShopEtalaseViewModel shopEtalaseViewModel) {
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

    public ShopEtalaseViewModel getShopEtalaseViewModel() {
        return shopEtalaseViewModel;
    }

    private void setEtalaseViewModel(ShopEtalaseViewModel shopEtalaseViewModel) {
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
