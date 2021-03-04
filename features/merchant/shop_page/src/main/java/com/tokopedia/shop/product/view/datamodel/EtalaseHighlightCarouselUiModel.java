package com.tokopedia.shop.product.view.datamodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.EtalaseHighlightAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class EtalaseHighlightCarouselUiModel implements Visitable<BaseAdapterTypeFactory>{

    private List<ShopProductUiModel> shopProductUiModelList;
    private ShopEtalaseItemDataModel shopEtalaseViewModel;

    public EtalaseHighlightCarouselUiModel(List<ShopProductUiModel> shopProductUiModelList,
                                           ShopEtalaseItemDataModel shopEtalaseViewModel) {
        setShopProductEtalaseHighLightViewModel(shopProductUiModelList);
        setEtalaseViewModel(shopEtalaseViewModel);
    }

    private void setShopProductEtalaseHighLightViewModel(List<ShopProductUiModel> shopProductUiModelList) {
        if (shopProductUiModelList == null) {
            this.shopProductUiModelList = new ArrayList<>();
        } else {
            this.shopProductUiModelList = shopProductUiModelList;
        }
    }

    public List<ShopProductUiModel> getShopProductUiModelList() {
        return shopProductUiModelList;
    }

    public ShopEtalaseItemDataModel getShopEtalaseViewModel() {
        return shopEtalaseViewModel;
    }

    private void setEtalaseViewModel(ShopEtalaseItemDataModel shopEtalaseViewModel) {
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
