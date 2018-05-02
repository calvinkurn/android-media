package com.tokopedia.discovery.autocomplete;

import android.support.annotation.LayoutRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.autocomplete.viewmodel.AutoCompleteSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.BaseItemAutoCompleteSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.CategorySearch;
import com.tokopedia.discovery.autocomplete.viewmodel.DigitalSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.InCategorySearch;
import com.tokopedia.discovery.autocomplete.viewmodel.ShopSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.TitleSearch;
import com.tokopedia.discovery.search.domain.model.SearchData;
import com.tokopedia.discovery.search.domain.model.SearchItem;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;
import com.tokopedia.discovery.search.view.adapter.SearchPageAdapter;
import com.tokopedia.discovery.search.view.fragment.SearchResultFragment;

import java.util.ArrayList;
import java.util.List;

public class TabAutoCompleteViewHolder extends AbstractViewHolder<TabAutoCompleteViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_suggestion_tabbing;
    private final FragmentManager fragmentManager;

    private SearchPageAdapter pageAdapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public TabAutoCompleteViewHolder(View view,
                                     FragmentManager fragmentManager,
                                     ItemClickListener clickListener) {
        super(view);
        this.fragmentManager = fragmentManager;
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        pageAdapter = new SearchPageAdapter(fragmentManager, itemView.getContext());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void bind(TabAutoCompleteViewModel element) {
        SearchResultFragment allFragment = pageAdapter.getRegisteredFragment(0);
        allFragment.clearData();
        SearchResultFragment productFragment = pageAdapter.getRegisteredFragment(1);
        productFragment.clearData();
        SearchResultFragment shopFragment = pageAdapter.getRegisteredFragment(2);
        shopFragment.clearData();

        for (SearchData searchData : element.getList()) {
            List<Visitable> list;
            switch (searchData.getId()) {
                case "digital":
                    allFragment.addBulkSearchResult(prepareDigitalSearch(searchData, element.getSearchTerm()));
                    continue;
                case "category":
//                    allFragment.addBulkSearchResult(prepareCategorySearch(searchData, element.getSearchTerm()));
                    continue;
                case "autocomplete":
                    list = prepareAutoCompleteSearch(searchData, element.getSearchTerm());
                    allFragment.addBulkSearchResult(list);
                    productFragment.addBulkSearchResult(list);
                    continue;
                case "hotlist":
                    continue;
                case "in_category":
                    list = prepareInCategorySearch(searchData, element.getSearchTerm());
                    allFragment.addBulkSearchResult(list);
                    productFragment.addBulkSearchResult(list);
                    continue;
                case "shop":
                    list = prepareShopSearch(searchData, element.getSearchTerm());
//                    allFragment.addBulkSearchResult(addShopTitle(list, searchData.getName()));
                    shopFragment.addBulkSearchResult(list);
                    continue;
            }
        }
    }

    private List<Visitable> addShopTitle(List<Visitable> list, String name) {
        TitleSearch titleSearch = new TitleSearch();
        titleSearch.setTitle(name);
        list.add(0, titleSearch);
        return list;
    }

    private List<Visitable> prepareShopSearch(SearchData searchData, String searchTerm) {
        List<Visitable> list = new ArrayList<>();
        for (SearchItem item : searchData.getItems()) {
            ShopSearch model = new ShopSearch();
            model.setEventId(searchData.getId());
            model.setEventName(searchData.getName());
            model.setSearchTerm(searchTerm);
            model.setKeyword(item.getKeyword());
            model.setUrl(item.getUrl());
            model.setApplink(item.getApplink());
            model.setImageUrl(item.getImageURI());
            model.setOfficial(item.isOfficial());
            model.setLocation(item.getLocation());
            list.add(model);
        }
        return list;
    }

    private List<Visitable> prepareInCategorySearch(SearchData searchData, String searchTerm) {
        List<Visitable> list = new ArrayList<>();
        for (SearchItem item : searchData.getItems()) {
            InCategorySearch model = new InCategorySearch();
            model.setEventId(searchData.getId());
            model.setEventName(searchData.getName());
            model.setSearchTerm(searchTerm);
            model.setKeyword(item.getKeyword());
            model.setUrl(item.getUrl());
            model.setApplink(item.getApplink());
            model.setRecom(item.getRecom());
            model.setCategoryId(item.getSc());
            list.add(model);
        }
        return list;
    }

    private List<Visitable> prepareAutoCompleteSearch(SearchData searchData, String searchTerm) {
        List<Visitable> list = new ArrayList<>();
        for (SearchItem item : searchData.getItems()) {
            AutoCompleteSearch model = new AutoCompleteSearch();
            model.setEventId(searchData.getId());
            model.setEventName(searchData.getName());
            model.setApplink(item.getApplink());
            model.setUrl(item.getUrl());
            model.setKeyword(item.getKeyword());
            model.setSearchTerm(searchTerm);
            list.add(model);
        }
        return list;
    }

    private List<Visitable> prepareCategorySearch(SearchData searchData, String searchTerm) {
        List<Visitable> list = new ArrayList<>();
        TitleSearch title = new TitleSearch();
        title.setTitle(searchData.getName());
        list.add(title);
        CategorySearch categorySearch = new CategorySearch();
        List<BaseItemAutoCompleteSearch> childList = new ArrayList<>();
        for (SearchItem item : searchData.getItems()) {
            BaseItemAutoCompleteSearch model = new BaseItemAutoCompleteSearch();
            model.setEventId(searchData.getId());
            model.setEventName(searchData.getName());
            model.setApplink(item.getApplink());
            model.setRecom(item.getRecom());
            model.setUrl(item.getUrl());
            model.setImageUrl(item.getImageURI());
            model.setKeyword(item.getKeyword());
            model.setCategoryId(item.getSc());
            model.setSearchTerm(searchTerm);
            childList.add(model);
        }
        categorySearch.setList(childList);
        list.add(categorySearch);
        return list;
    }

    private List<Visitable> prepareDigitalSearch(SearchData searchData, String searchTerm) {
        List<Visitable> list = new ArrayList<>();
        for (SearchItem item : searchData.getItems()) {
            DigitalSearch model = new DigitalSearch();
            model.setEventId(searchData.getId());
            model.setEventName(searchData.getName());
            model.setApplink(item.getApplink());
            model.setRecom(item.getRecom());
            model.setUrl(item.getUrl());
            model.setImageUrl(item.getImageURI());
            model.setKeyword(item.getKeyword());
            model.setSearchTerm(searchTerm);
            list.add(model);
        }
        return list;
    }
}
