package com.tokopedia.search.result.presentation.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.search.result.domain.model.SearchCatalogModel;
import com.tokopedia.search.result.presentation.model.CatalogHeaderViewModel;
import com.tokopedia.search.result.presentation.model.CatalogViewModel;

import java.util.ArrayList;
import java.util.List;

public class CatalogViewModelMapper {

    public List<Visitable> mappingCatalogViewModelWithHeader(SearchCatalogModel domain) {
        List<Visitable> list = new ArrayList<>();

        if (domain.catalogList == null || domain.catalogList.isEmpty()) {
            return list;
        }

        list.add(new CatalogHeaderViewModel());

        for (SearchCatalogModel.Catalog item : domain.catalogList) {
            list.add(createCatalogViewModelFromSearchCatalogModel(item));
        }

        return list;
    }

    public List<Visitable> mappingCatalogViewModelWithoutHeader(SearchCatalogModel domain) {
        List<Visitable> list = new ArrayList<>();

        if (domain.catalogList == null || domain.catalogList.isEmpty()) {
            return list;
        }

        for (SearchCatalogModel.Catalog item : domain.catalogList) {
            list.add(createCatalogViewModelFromSearchCatalogModel(item));
        }

        return list;
    }

    private CatalogViewModel createCatalogViewModelFromSearchCatalogModel(SearchCatalogModel.Catalog item) {
        CatalogViewModel model = new CatalogViewModel();
        model.setID(item.catalogId);
        model.setName(item.catalogName);
        model.setDesc(item.catalogDescription);
        model.setImage(item.catalogImage);
        model.setImage300(item.catalogImage300);
        model.setPrice(item.catalogPrice);
        model.setProductCounter(item.catalogCountProduct);
        model.setURL(item.catalogUri);

        return model;
    }
}
