package com.tokopedia.tkpd.feedplus.view.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.feedplus.view.adapter.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/18/17.
 */

public class OfficialStoreViewModel implements Visitable<FeedPlusTypeFactory>{

    private String officialStoreHeaderImageUrl;

    private ArrayList<ProductFeedViewModel> listProduct;

    public OfficialStoreViewModel(String url, ArrayList<ProductFeedViewModel> listProduct) {
        this.officialStoreHeaderImageUrl = url;
        this.listProduct = listProduct;
    }

    @Override
    public int type(FeedPlusTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

    public ArrayList<ProductFeedViewModel> getListProduct() {
        return listProduct;
    }

    public void setListProduct(ArrayList<ProductFeedViewModel> listProduct) {
        this.listProduct = listProduct;
    }

    public String getOfficialStoreHeaderImageUrl() {
        return officialStoreHeaderImageUrl;
    }

    public void setOfficialStoreHeaderImageUrl(String officialStoreHeaderImageUrl) {
        this.officialStoreHeaderImageUrl = officialStoreHeaderImageUrl;
    }
}
