package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandDataModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.factory.HomeTypeFactory;

import java.util.List;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class BrandsViewModel implements Visitable<HomeTypeFactory> {

    private String title;
    private List<BrandDataModel> data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<BrandDataModel> getData() {
        return data;
    }

    public void setData(List<BrandDataModel> data) {
        this.data = data;
    }

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
