package com.tokopedia.search.result.presentation.view.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.*;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RecommendationItemViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SmallGridProductItemViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.TopAdsViewHolder;
import com.tokopedia.search.result.presentation.view.listener.TopAdsSwitcher;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;
import com.tokopedia.search.result.presentation.view.typefactory.SearchSectionTypeFactory;
import com.tokopedia.topads.sdk.view.DisplayMode;

import java.util.ArrayList;
import java.util.List;

public final class ProductListAdapter extends SearchSectionGeneralAdapter {

    private List<Visitable> list = new ArrayList<>();
    private ProductListTypeFactory typeFactory;
    private int startFrom;
    private int totalData;
    private LoadingMoreModel loadingMoreModel;
    private TopAdsSwitcher topAdsSwitcher;
    private GlobalNavViewModel globalNavViewModel;

    public ProductListAdapter(OnItemChangeView itemChangeView, ProductListTypeFactory typeFactory) {
        super(itemChangeView);
        this.typeFactory = typeFactory;
        loadingMoreModel = new LoadingMoreModel();
    }

    @NonNull
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
        setFullSpanForStaggeredGrid(holder, holder.getItemViewType());

        holder.bind(list.get(position));
    }

    private void setFullSpanForStaggeredGrid(AbstractViewHolder holder, int viewType) {
        if(holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams layoutParams =
                    (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();

            layoutParams.setFullSpan(isStaggeredGridFullSpan(viewType));
        }
    }

    private boolean isStaggeredGridFullSpan(int viewType) {
        return viewType != SmallGridProductItemViewHolder.LAYOUT && viewType != RecommendationItemViewHolder.LAYOUT;
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
                }
            } else if(list.get(i) instanceof RecommendationItemViewModel){
                RecommendationItemViewModel model = (RecommendationItemViewModel) list.get(i);
                if(productId.equals(String.valueOf(model.getRecommendationItem().getProductId()))){
                    model.getRecommendationItem().setWishlist(isWishlisted);
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
        }else if (adapterPosition >= 0 && list.get(adapterPosition) instanceof RecommendationItemViewModel) {
            ((RecommendationItemViewModel) list.get(adapterPosition)).getRecommendationItem().setWishlist(isWishlisted);
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

    public boolean isRecommendationItem(int position){
        return checkDataSize(position) && list.get(position) instanceof RecommendationItemViewModel;
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
    public void showEmptyState(Context context, String query, boolean isFilterActive, String sectionTitle) {
        clearData();
        if (globalNavViewModel != null) {
            getItemList().add(globalNavViewModel);
        }
        getItemList().add(mapEmptySearch(context, query, isFilterActive,
                globalNavViewModel == null));
        notifyDataSetChanged();
    }

    private EmptySearchViewModel mapEmptySearch(Context context, String query,
                                                boolean isFilterActive,
                                                boolean isBannerAdsAllowed) {
        EmptySearchViewModel emptySearchViewModel = new EmptySearchViewModel();
        emptySearchViewModel.setImageRes(R.drawable.product_search_not_found);
        emptySearchViewModel.setBannerAdsAllowed(isBannerAdsAllowed);
        if (isFilterActive) {
            emptySearchViewModel.setTitle(context.getString(R.string.msg_empty_search_product_title));
            emptySearchViewModel.setContent(context.getString(R.string.msg_empty_search_product_content_with_filter));
        } else {
            emptySearchViewModel.setTitle(context.getString(R.string.msg_empty_search_product_title));
            emptySearchViewModel.setContent(context.getString(R.string.msg_empty_search_product_content));
            emptySearchViewModel.setButtonText(context.getString(R.string.msg_empty_search_product_button));
        }
        return emptySearchViewModel;
    }

    public void setGlobalNavViewModel(GlobalNavViewModel globalNavViewModel) {
        this.globalNavViewModel = globalNavViewModel;
    }
}