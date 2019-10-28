package com.tokopedia.autocomplete;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

import com.tokopedia.autocomplete.OnScrollListenerAutocomplete;
import com.tokopedia.autocomplete.adapter.ItemClickListener;
import com.tokopedia.autocomplete.adapter.SearchAdapter;
import com.tokopedia.autocomplete.adapter.SearchAdapterTypeFactory;
import com.tokopedia.autocomplete.domain.model.SearchData;
import com.tokopedia.autocomplete.domain.model.SearchItem;
import com.tokopedia.autocomplete.viewmodel.BaseItemAutoCompleteSearch;
import com.tokopedia.autocomplete.viewmodel.PopularSearch;
import com.tokopedia.autocomplete.viewmodel.RecentSearch;
import com.tokopedia.autocomplete.viewmodel.RecentViewSearch;
import com.tokopedia.autocomplete.viewmodel.TitleSearch;

import java.util.ArrayList;
import java.util.List;

public class DefaultAutoCompleteViewHolder extends AbstractViewHolder<DefaultAutoCompleteViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_simple_page_autocomplete;
    private SearchAdapter adapter;
    private RecyclerView recyclerView;

    public DefaultAutoCompleteViewHolder(View view, ItemClickListener clickListener) {
        super(view);
        recyclerView = view.findViewById(R.id.recyclerView);
        SearchAdapterTypeFactory typeFactory = new SearchAdapterTypeFactory(clickListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false);
        adapter = new SearchAdapter(typeFactory);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new OnScrollListenerAutocomplete(view.getContext(), view));
    }

    @Override
    public void bind(DefaultAutoCompleteViewModel element) {
        adapter.clearData();
        for (SearchData searchData : element.getList()) {
            switch (searchData.getId()) {
                case SearchData.AUTOCOMPLETE_RECENT_SEARCH:
                    adapter.addAll(
                            insertTitleWithDeleteAll(
                                    prepareRecentSearch(searchData, element.getSearchTerm()),
                                    searchData.getName()
                            )
                    );
                    continue;
                case SearchData.AUTOCOMPLETE_RECENT_VIEW:
                    adapter.addAll(
                            insertTitle(
                                    prepareRecentViewSearch(searchData, element.getSearchTerm()),
                                    searchData.getName()
                            )
                    );
                    continue;
                case SearchData.AUTOCOMPLETE_POPULAR_SEARCH:
                    adapter.addAll(
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
            model.setIsOfficial(item.isOfficial());
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
            model.setIsOfficial(item.isOfficial());
            childList.add(model);
        }
        popularSearch.setList(childList);
        list.add(popularSearch);
        return list;
    }

    private List<Visitable> prepareRecentViewSearch(SearchData searchData, String searchTerm) {
        List<Visitable> list = new ArrayList<>();
        RecentViewSearch recentViewSearch = new RecentViewSearch();
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
            model.setImageUrl(item.getImageURI());
            model.setProductId(item.getItemId());
            model.setProductPrice(item.getPrice());
            model.setIsOfficial(item.isOfficial());
            childList.add(model);
        }
        recentViewSearch.setList(childList);
        list.add(recentViewSearch);
        return list;
    }

    private List<Visitable> insertTitle(List<Visitable> list, String name) {
        TitleSearch titleSearch = new TitleSearch();
        titleSearch.setTitle(name);
        list.add(0, titleSearch);
        return list;
    }

    private List<Visitable> insertTitleWithDeleteAll(List<Visitable> list, String name) {
        TitleSearch titleSearch = new TitleSearch(true);
        titleSearch.setTitle(name);
        list.add(0, titleSearch);
        return list;
    }
}
