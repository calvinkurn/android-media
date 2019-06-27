package com.tokopedia.search.result.presentation.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.discovery.common.data.Option;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;
import com.tokopedia.topads.sdk.domain.model.CpmModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 11/7/17.
 */

public class HeaderViewModel implements Visitable<ProductListTypeFactory> {

    private SuggestionViewModel suggestionViewModel;
    private List<Option> quickFilterList = new ArrayList<>();
    private GuidedSearchViewModel guidedSearch;
    private CpmModel cpmModel;
    private GlobalNavViewModel globalNavViewModel;

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public HeaderViewModel() {
    }

    public CpmModel getCpmModel() {
        return cpmModel;
    }

    public void setCpmModel(CpmModel cpmModel) {
        this.cpmModel = cpmModel;
    }

    public void setSuggestionViewModel(SuggestionViewModel suggestionModel) {
        this.suggestionViewModel = suggestionModel;
    }

    public SuggestionViewModel getSuggestionViewModel() {
        return suggestionViewModel;
    }

    public boolean hasHeader() {
        return (suggestionViewModel != null);
    }

    public List<Option> getQuickFilterList() {
        return quickFilterList;
    }

    public void setQuickFilterList(List<Option> quickFilterList) {
        this.quickFilterList.clear();
        this.quickFilterList.addAll(quickFilterList);
    }

    public GuidedSearchViewModel getGuidedSearch() {
        return guidedSearch;
    }

    public void setGuidedSearch(GuidedSearchViewModel guidedSearch) {
        this.guidedSearch = guidedSearch;
    }

    public GlobalNavViewModel getGlobalNavViewModel() {
        return globalNavViewModel;
    }

    public void setGlobalNavViewModel(GlobalNavViewModel globalNavViewModel) {
        this.globalNavViewModel = globalNavViewModel;
    }
}