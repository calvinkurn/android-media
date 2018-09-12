package com.tokopedia.shop.product.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.shop.product.view.model.EtalaseHighlightCarouselViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseHighlightViewModel;

import java.util.ArrayList;
import java.util.List;

public class EtalaseHighlightAdapter extends BaseListAdapter<ShopProductEtalaseHighlightViewModel, EtalaseHighlightAdapterTypeFactory> {
    private List<EtalaseHighlightCarouselViewModel> etalaseHighlightCarouselViewModelList;

    public EtalaseHighlightAdapter(List<EtalaseHighlightCarouselViewModel> etalaseHighlightCarouselViewModelList,
                                   EtalaseHighlightAdapterTypeFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
        setEtalaseHighlightCarouselViewModelList(etalaseHighlightCarouselViewModelList);
    }

    public void setEtalaseHighlightCarouselViewModelList(List<EtalaseHighlightCarouselViewModel> etalaseHighlightCarouselViewModelList) {
        if (etalaseHighlightCarouselViewModelList == null) {
            this.etalaseHighlightCarouselViewModelList = new ArrayList<>();
        } else {
            this.etalaseHighlightCarouselViewModelList = etalaseHighlightCarouselViewModelList;
        }
    }

    public List<EtalaseHighlightCarouselViewModel> getEtalaseHighlightCarouselViewModelList() {
        return etalaseHighlightCarouselViewModelList;
    }

    @Override
    protected boolean isItemClickableByDefault() {
        return false;
    }

}
