package com.tokopedia.search.result.presentation.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.toolargetool.TooLargeTool;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.search.result.presentation.model.CatalogHeaderViewModel;
import com.tokopedia.search.result.presentation.model.CatalogViewModel;
import com.tokopedia.search.result.presentation.model.EmptySearchViewModel;
import com.tokopedia.search.result.presentation.view.typefactory.CatalogListTypeFactory;
import com.tokopedia.search.result.presentation.view.typefactory.SearchSectionTypeFactory;

import java.util.ArrayList;
import java.util.List;

public class CatalogListAdapter extends SearchSectionGeneralAdapter {

    private List<Visitable> mVisitables;
    private final CatalogListTypeFactory typeFactory;

    private boolean nextPage;
    private int startFrom;

    public CatalogListAdapter(OnItemChangeView itemChangeView, CatalogListTypeFactory typeFactory) {
        super(itemChangeView);
        this.typeFactory = typeFactory;
        mVisitables = new ArrayList<>();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(mVisitables.get(position));
    }

    @Override
    public int getItemCount() {
        return mVisitables.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemViewType(int position) {
        return mVisitables.get(position).type(typeFactory);
    }

    public void setElement(List<Visitable> data) {
        mVisitables = data;
        notifyDataSetChanged();
    }

    public void addElements(List<Visitable> data) {
        int start = getItemCount();
        mVisitables.addAll(data);
        notifyItemRangeInserted(start, data.size());
    }

    public List<Visitable> getElements() {
        return mVisitables;
    }

    public void removeItem(int position) {
        mVisitables.remove(position);
        notifyItemRemoved(position);
    }

    public boolean isEmpty() {
        return mVisitables.isEmpty();
    }

    public boolean hasNextPage() {
        return nextPage;
    }

    public void setNextPage(boolean nextPage) {
        this.nextPage = nextPage;
    }

    public void incrementStart() {
        setStartFrom(getStartFrom() + Integer.parseInt(SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS));
    }

    public void resetStartFrom() {
        setStartFrom(Integer.parseInt(SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS));
    }

    public int getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(int startFrom) {
        this.startFrom = startFrom;
    }

    @Override
    protected SearchSectionTypeFactory getTypeFactory() {
        return typeFactory;
    }

    @Override
    public List<Visitable> getItemList() {
        return mVisitables;
    }

    public boolean isCatalogHeader(int position) {
        if (checkDataSize(position))
            return getItemList().get(position) instanceof CatalogHeaderViewModel;
        return false;
    }

    @Override
    public boolean isEmptyItem(int position) {
        return checkDataSize(position) && getItemList().get(position) instanceof EmptySearchViewModel;
    }

    // Override parent's clearData
    // CatalogListAdapter do not need to notifyItemRangeRemoved
    // It will be done via TopAdsRecyclerAdapter
    @Override
    public void clearData() {
        getItemList().clear();
    }
}
