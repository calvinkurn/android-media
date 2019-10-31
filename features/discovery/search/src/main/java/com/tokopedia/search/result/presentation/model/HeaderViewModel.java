package com.tokopedia.search.result.presentation.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;
import com.tokopedia.topads.sdk.domain.model.CpmModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 11/7/17.
 */

public class HeaderViewModel implements Visitable<ProductListTypeFactory> {

    private TickerViewModel tickerViewModel;
    private SuggestionViewModel suggestionViewModel;
    private List<Option> quickFilterList = new ArrayList<>();
    private CpmModel cpmModel;

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

    public TickerViewModel getTickerViewModel() {
        return tickerViewModel;
    }

    public void setTickerViewModel(TickerViewModel tickerViewModel) {
        this.tickerViewModel = tickerViewModel;
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
}