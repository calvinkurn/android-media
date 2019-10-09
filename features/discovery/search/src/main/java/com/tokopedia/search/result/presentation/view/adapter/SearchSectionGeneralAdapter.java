package com.tokopedia.search.result.presentation.view.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.EmptySearchViewModel;
import com.tokopedia.search.result.presentation.view.typefactory.SearchSectionTypeFactory;

import java.util.List;

public abstract class SearchSectionGeneralAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private OnItemChangeView itemChangeView;

    SearchSectionGeneralAdapter(OnItemChangeView itemChangeView) {
        this.itemChangeView = itemChangeView;
    }

    public void changeListView() {
        getTypeFactory().setRecyclerViewItem(SearchConstant.RecyclerView.VIEW_LIST);
        itemChangeView.onChangeList();
    }

    public void changeDoubleGridView() {
        getTypeFactory().setRecyclerViewItem(SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID);
        itemChangeView.onChangeDoubleGrid();
    }

    public void changeSingleGridView() {
        getTypeFactory().setRecyclerViewItem(SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID);
        itemChangeView.onChangeSingleGrid();
    }


    public int getTitleTypeRecyclerView() {
        switch (getTypeFactory().getRecyclerViewItem()) {
            case SearchConstant.RecyclerView.VIEW_LIST:
                return R.string.list;
            case SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID:
                return R.string.grid;
            case SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID:
                return R.string.grid;
            default:
                return R.string.grid;
        }
    }

    public int getIconTypeRecyclerView() {
        switch (getTypeFactory().getRecyclerViewItem()) {
            case SearchConstant.RecyclerView.VIEW_LIST:
                return R.drawable.search_ic_list;
            case SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID:
                return R.drawable.search_ic_grid;
            case SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID:
                return R.drawable.search_ic_big_list;
            default:
                return R.drawable.search_ic_grid;
        }
    }

    public SearchConstant.ViewType getCurrentLayoutType() {
        switch (getTypeFactory().getRecyclerViewItem()) {
            case SearchConstant.RecyclerView.VIEW_LIST:
                return SearchConstant.ViewType.LIST;
            case SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID:
                return SearchConstant.ViewType.SMALL_GRID;
            case SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID:
                return SearchConstant.ViewType.BIG_GRID;
            default:
                return SearchConstant.ViewType.SMALL_GRID;
        }
    }

    public boolean isLoading(int position) {
        return checkDataSize(position) && getItemList().get(position) instanceof LoadingModel;
    }

    public boolean isEmptyItem(int position) {
        return checkDataSize(position) && getItemList().get(position) instanceof EmptySearchViewModel;
    }

    public void clearData() {
        int itemSizeBeforeCleared = getItemCount();

        getItemList().clear();

        notifyItemRangeRemoved(0, itemSizeBeforeCleared);
    }

    public boolean isListEmpty() {
        return getItemList().isEmpty();
    }

    protected boolean checkDataSize(int position) {
        return getItemList() != null && getItemList().size() > 0
                && position > -1 && position < getItemList().size();
    }

    public void showEmptyState(Context context, String query, boolean isFilterActive, String sectionTitle) {
        clearData();
        getItemList().add(mappingEmptySearch(context, query, isFilterActive, sectionTitle));
        notifyDataSetChanged();
    }

    protected EmptySearchViewModel mappingEmptySearch(Context context, String query, boolean isFilterActive, String sectionTitle) {
        EmptySearchViewModel emptySearchViewModel = new EmptySearchViewModel();
        emptySearchViewModel.setImageRes(com.tokopedia.design.R.drawable.ic_empty_search);
        if (isFilterActive) {
            emptySearchViewModel.setTitle(getEmptySearchTitle(context, sectionTitle));
            emptySearchViewModel.setContent(String.format(context.getString(R.string.msg_empty_search_with_filter_2), query));
        } else {
            emptySearchViewModel.setTitle(getEmptySearchTitle(context, sectionTitle));
            emptySearchViewModel.setContent(String.format(context.getString(R.string.empty_search_content_template), query));
            emptySearchViewModel.setButtonText(context.getString(R.string.empty_search_button_text));
        }
        return emptySearchViewModel;
    }

    private String getEmptySearchTitle(Context context, String sectionTitle) {
        String templateText = context.getString(R.string.msg_empty_search_with_filter_1);
        return String.format(templateText, sectionTitle);
    }

    public abstract List<Visitable> getItemList();

    protected abstract SearchSectionTypeFactory getTypeFactory();

    public interface OnItemChangeView {
        void onChangeList();

        void onChangeDoubleGrid();

        void onChangeSingleGrid();
    }
}
