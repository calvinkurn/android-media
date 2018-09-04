package com.tokopedia.shop.product.view.model;

import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;

import java.util.ArrayList;
import java.util.List;

public class ShopProductEtalaseListViewModel implements BaseShopProductViewModel {

    private List<ShopEtalaseViewModel> etalaseModelList;
    private String selectedEtalaseId;

    public ShopProductEtalaseListViewModel(){
        setEtalaseModelList(null, "");
    }

    public ShopProductEtalaseListViewModel(List<ShopEtalaseViewModel> etalaseModelList,
                                           String selectedeEtalaseId){
        setEtalaseModelList(etalaseModelList, selectedeEtalaseId);
    }

    public void setEtalaseModelList(List<ShopEtalaseViewModel> etalaseModelList, String selectedeEtalaseId) {
        if (etalaseModelList == null) {
            this.etalaseModelList = new ArrayList<>();
        } else {
            this.etalaseModelList = etalaseModelList;
        }
        this.selectedEtalaseId = selectedeEtalaseId;
    }

    public List<ShopEtalaseViewModel> getEtalaseModelList() {
        return etalaseModelList;
    }

    public String getSelectedEtalaseId() {
        return selectedEtalaseId;
    }

    public void setSelectedEtalaseId(String selectedEtalaseId) {
        this.selectedEtalaseId = selectedEtalaseId;
    }

    @Override
    public int type(ShopProductAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
