package com.tokopedia.search.result.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.TopAdsSwitcher;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.EmptySearchViewModel;
import com.tokopedia.search.result.presentation.model.HeaderViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.model.RelatedSearchViewModel;
import com.tokopedia.search.result.presentation.model.TopAdsViewModel;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.TopAdsViewHolder;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;
import com.tokopedia.search.result.presentation.view.typefactory.SearchSectionTypeFactory;
import com.tokopedia.topads.sdk.view.DisplayMode;

import java.util.ArrayList;
import java.util.List;

public final class ProductListAdapter extends SearchSectionGeneralAdapter {

    private static final int ADAPTER_POSITION_HEADER = 0;
    private List<Visitable> list = new ArrayList<>();
    private EmptySearchViewModel emptySearchModel;
    private ProductListTypeFactory typeFactory;
    private int startFrom;
    private int totalData;
    private Context context;
    private LoadingMoreModel loadingMoreModel;
    private TopAdsSwitcher topAdsSwitcher;

    public ProductListAdapter(Context context, OnItemChangeView itemChangeView, ProductListTypeFactory typeFactory) {
        super(itemChangeView);
        this.context = context;
        this.typeFactory = typeFactory;
        loadingMoreModel = new LoadingMoreModel();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        AbstractViewHolder viewHolder = typeFactory.createViewHolder(view, viewType);
        setTopAdsSwitcherForTopAdsViewHolder(viewHolder);
        return viewHolder;
    }

    private void setTopAdsSwitcherForTopAdsViewHolder(AbstractViewHolder viewHolder) {
        if(viewHolder instanceof TopAdsViewHolder){
            setTopAdsSwitcher((TopAdsViewHolder) viewHolder);
        }
    }

    private void setTopAdsSwitcher(TopAdsSwitcher topAdsSwitcher) {
        this.topAdsSwitcher = topAdsSwitcher;
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public void changeListView() {
        super.changeListView();
        if(topAdsSwitcher!=null) {
            topAdsSwitcher.switchDisplay(DisplayMode.LIST);
        }
    }

    @Override
    public void changeDoubleGridView() {
        super.changeDoubleGridView();
        if(topAdsSwitcher!=null) {
            topAdsSwitcher.switchDisplay(DisplayMode.GRID);
        }
    }

    @Override
    public void changeSingleGridView() {
        super.changeSingleGridView();
        if(topAdsSwitcher!=null) {
            topAdsSwitcher.switchDisplay(DisplayMode.BIG);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clearDataBeforeSet() {
        super.clearData();
    }

    public void appendItems(List<Visitable> list) {
        int start = getItemCount();
        this.list.addAll(list);
        notifyItemRangeInserted(start, list.size());
    }

    public void incrementStart() {
        setStartFrom(getStartFrom() + Integer.parseInt(SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS));
    }

    public boolean isEvenPage() {
        return getStartFrom() / Integer.parseInt(SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS) % 2 == 0;
    }

    public int getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(int start) {
        this.startFrom = start;
    }

    public void setWishlistButtonEnabled(String productId, boolean isEnabled) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof ProductItemViewModel) {
                ProductItemViewModel model = ((ProductItemViewModel) list.get(i));
                if (productId.equals(model.getProductID())) {
                    model.setWishlistButtonEnabled(isEnabled);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    public void updateWishlistStatus(String productId, boolean isWishlisted) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof ProductItemViewModel) {
                ProductItemViewModel model = ((ProductItemViewModel) list.get(i));
                if (productId.equals(model.getProductID())) {
                    model.setWishlisted(isWishlisted);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    public void updateWishlistStatus(int adapterPosition, boolean isWishlisted) {
        if (adapterPosition >= 0 && list.get(adapterPosition) instanceof ProductItemViewModel) {
            ((ProductItemViewModel) list.get(adapterPosition)).setWishlisted(isWishlisted);
            notifyItemChanged(adapterPosition);
        }
    }

    public boolean isHeaderBanner(int position) {
        if (checkDataSize(position))
            return getItemList().get(position) instanceof HeaderViewModel;
        return false;
    }

    public boolean isRelatedSearch(int position) {
        if (checkDataSize(position))
            return getItemList().get(position) instanceof RelatedSearchViewModel;
        return false;
    }

    @Override
    public List<Visitable> getItemList() {
        return list;
    }

    @Override
    protected SearchSectionTypeFactory getTypeFactory() {
        return typeFactory;
    }

    public boolean isProductItem(int position) {
        return checkDataSize(position) && list.get(position) instanceof ProductItemViewModel;
    }

    @Override
    public boolean isEmptyItem(int position) {
        return checkDataSize(position) && getItemList().get(position) instanceof EmptySearchViewModel;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public int getTotalData() {
        return totalData;
    }

    public boolean hasNextPage() {
        return getStartFrom() < getTotalData();
    }

    @Override
    public void clearData() {
        super.clearData();
        setStartFrom(0);
        setTotalData(0);
    }

    public boolean hasHeader() {
        return checkDataSize(0) && getItemList().get(0) instanceof HeaderViewModel;
    }

    public boolean isTopAds(int position) {
        return checkDataSize(position) && getItemList().get(position) instanceof TopAdsViewModel;
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
}