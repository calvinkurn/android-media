package com.tokopedia.search.result.presentation.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.EmptySearchViewModel;
import com.tokopedia.search.result.presentation.view.typefactory.SearchSectionTypeFactory;
import com.tokopedia.search.result.presentation.view.typefactory.ShopListTypeFactory;

import java.util.ArrayList;
import java.util.List;

public class ShopListAdapter extends SearchSectionGeneralAdapter {

    private List<Visitable> list = new ArrayList<>();
    private LoadingMoreModel loadingMoreModel;
    private ShopListTypeFactory typeFactory;

    public ShopListAdapter(OnItemChangeView itemChangeView, ShopListTypeFactory typeFactory) {
        super(itemChangeView);
        this.typeFactory = typeFactory;
        loadingMoreModel = new LoadingMoreModel();
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void appendItems(List<Visitable> list) {
        int newItemsFirstPosition = getItemCount();

        this.list.addAll(list);

        notifyItemRangeInserted(newItemsFirstPosition, list.size());
    }

    public void addLoading() {
        int loadingModelPosition = this.list.size();

        this.list.add(loadingMoreModel);
        notifyItemInserted(loadingModelPosition);
    }

    public void removeLoading() {
        int loadingModelPosition = this.list.indexOf(loadingMoreModel);

        if(loadingModelPosition != -1) {
            this.list.remove(loadingMoreModel);

            notifyItemRemoved(loadingModelPosition);
            notifyItemRangeChanged(loadingModelPosition, 1);
        }
    }

    @Override
    public List<Visitable> getItemList() {
        return list;
    }

    @Override
    protected SearchSectionTypeFactory getTypeFactory() {
        return typeFactory;
    }

    @Override
    public boolean isEmptyItem(int position) {
        return checkDataSize(position) && getItemList().get(position) instanceof EmptySearchViewModel;
    }

    @Override
    public int getIconTypeRecyclerView() {
        switch (getTypeFactory().getRecyclerViewItem()) {
            case SearchConstant.RecyclerView.VIEW_PRODUCT:
                return R.drawable.ic_list_green;
            case SearchConstant.RecyclerView.VIEW_PRODUCT_GRID_2:
                return R.drawable.ic_grid_default_green;
            case SearchConstant.RecyclerView.VIEW_PRODUCT_GRID_1:
                return R.drawable.ic_grid_box_green;
            default:
                return R.drawable.ic_grid_default_green;
        }
    }
}