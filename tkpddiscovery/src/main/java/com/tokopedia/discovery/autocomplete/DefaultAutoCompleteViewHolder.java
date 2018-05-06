package com.tokopedia.discovery.autocomplete;

import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.autocomplete.viewmodel.BaseItemAutoCompleteSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.PopularSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.RecentSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.TitleSearch;
import com.tokopedia.discovery.search.domain.model.SearchData;
import com.tokopedia.discovery.search.domain.model.SearchItem;
import com.tokopedia.discovery.search.view.fragment.SearchResultFragment;

import java.util.ArrayList;
import java.util.List;

public class DefaultAutoCompleteViewHolder extends AbstractViewHolder<DefaultAutoCompleteViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_simple_page_autocomplete;
    private final SearchResultFragment fragment;

    public DefaultAutoCompleteViewHolder(View view, FragmentManager fm) {
        super(view);
        fragment = (SearchResultFragment) fm.findFragmentById(R.id.searchResultFragment);
    }

    @Override
    public void bind(DefaultAutoCompleteViewModel element) {
        fragment.clearData();
        for (SearchData searchData : element.getList()) {
            List<Visitable> list;
            switch (searchData.getId()) {
                case "recent_search":
                    fragment.addBulkSearchResult(
                            insertTitle(
                                    prepareRecentSearch(searchData, element.getSearchTerm()),
                                    searchData.getName()
                            )
                    );
                    continue;
                case "popular_search":
                    fragment.addBulkSearchResult(
                            insertTitle(
                                    preparePopularSearch(searchData, element.getSearchTerm()),
                                    searchData.getName()
                            )
                    );
                    continue;
            }
        }
    }

    private List<Visitable> prepareRecentSearch(SearchData searchData, String searchTerm) {
        List<Visitable> list = new ArrayList<>();
        RecentSearch recentSearch = new RecentSearch();
        List<BaseItemAutoCompleteSearch> childList = new ArrayList<>();
        for (SearchItem item : searchData.getItems()) {
            BaseItemAutoCompleteSearch model = new BaseItemAutoCompleteSearch();
            model.setEventId(searchData.getId());
            model.setEventName(searchData.getName());
            model.setApplink(item.getApplink());
            model.setRecom(item.getRecom());
            model.setUrl(item.getUrl());
            model.setKeyword(item.getKeyword());
            model.setSearchTerm(searchTerm);
            childList.add(model);
        }
        recentSearch.setList(childList);
        list.add(recentSearch);
        return list;
    }

    private List<Visitable> preparePopularSearch(SearchData searchData, String searchTerm) {
        List<Visitable> list = new ArrayList<>();
        PopularSearch popularSearch = new PopularSearch();
        List<BaseItemAutoCompleteSearch> childList = new ArrayList<>();
        for (SearchItem item : searchData.getItems()) {
            BaseItemAutoCompleteSearch model = new BaseItemAutoCompleteSearch();
            model.setEventId(searchData.getId());
            model.setEventName(searchData.getName());
            model.setApplink(item.getApplink());
            model.setRecom(item.getRecom());
            model.setUrl(item.getUrl());
            model.setKeyword(item.getKeyword());
            model.setSearchTerm(searchTerm);
            childList.add(model);
        }
        popularSearch.setList(childList);
        list.add(popularSearch);
        return list;
    }

    private List<Visitable> insertTitle(List<Visitable> list, String name) {
        TitleSearch titleSearch = new TitleSearch();
        titleSearch.setTitle(name);
        list.add(0, titleSearch);
        return list;
    }
}
