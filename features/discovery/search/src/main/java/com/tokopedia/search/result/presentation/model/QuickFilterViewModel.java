package com.tokopedia.search.result.presentation.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;
import com.tokopedia.topads.sdk.domain.model.CpmModel;

import java.util.ArrayList;
import java.util.List;

public class QuickFilterViewModel implements Visitable<ProductListTypeFactory> {

    private String formattedResultCount;
    private List<Filter> quickFilterList = new ArrayList<>();
    private List<Option> quickFilterOptions = new ArrayList<>();

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public QuickFilterViewModel() {
    }

    public List<Filter> getQuickFilterList() {
        return quickFilterList;
    }

    public void setQuickFilterList(List<Filter> quickFilterList) {
        this.quickFilterList.clear();
        this.quickFilterList.addAll(quickFilterList);
    }

    public List<Option> getQuickFilterOptions() {
        return quickFilterOptions;
    }

    public void setQuickFilterOptions(List<Option> quickFilterOptions) {
        this.quickFilterOptions.clear();
        this.quickFilterOptions.addAll(quickFilterOptions);
    }

    public String getFormattedResultCount() {
        return formattedResultCount;
    }

    public void setFormattedResultCount(String formattedResultCount) {
        this.formattedResultCount = formattedResultCount;
    }
}