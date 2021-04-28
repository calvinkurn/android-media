package com.tokopedia.search.result.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView;
import com.tokopedia.search.result.presentation.model.BroadMatchDataView;
import com.tokopedia.search.result.presentation.model.EmptySearchProductDataView;
import com.tokopedia.search.result.presentation.model.GlobalNavDataView;
import com.tokopedia.search.result.presentation.model.ProductItemDataView;
import com.tokopedia.search.result.presentation.model.RecommendationItemDataView;
import com.tokopedia.search.result.presentation.model.TickerDataView;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RecommendationItemViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SmallGridInspirationCardViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SmallGridProductItemViewHolder;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ProductListAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list = new ArrayList<>();
    private ProductListTypeFactory typeFactory;
    private LoadingMoreModel loadingMoreModel = new LoadingMoreModel();
    private OnItemChangeView itemChangeView;

    public ProductListAdapter(OnItemChangeView itemChangeView, ProductListTypeFactory typeFactory) {
        this.itemChangeView = itemChangeView;
        this.typeFactory = typeFactory;
    }

    public void changeSearchNavigationListView(int position) {
        if (position >= 0 && position < list.size()) {
            typeFactory.setRecyclerViewItem(SearchConstant.RecyclerView.VIEW_LIST);
            notifyItemChanged(position, SearchConstant.RecyclerView.VIEW_LIST);
        }
    }

    public void changeSearchNavigationDoubleGridView(int position) {
        if (position >= 0 && position < list.size()) {
            typeFactory.setRecyclerViewItem(SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID);
            notifyItemChanged(position, SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID);
        }
    }

    public void changeSearchNavigationSingleGridView(int position) {
        if (position >= 0 && position < list.size()) {
            typeFactory.setRecyclerViewItem(SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID);
            notifyItemChanged(position, SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID);
        }
    }

    public void changeListView() {
        typeFactory.setRecyclerViewItem(SearchConstant.RecyclerView.VIEW_LIST);
        itemChangeView.onChangeList();
    }

    public void changeDoubleGridView() {
        typeFactory.setRecyclerViewItem(SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID);
        itemChangeView.onChangeDoubleGrid();
    }

    public void changeSingleGridView() {
        typeFactory.setRecyclerViewItem(SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID);
        itemChangeView.onChangeSingleGrid();
    }

    public int getTitleTypeRecyclerView() {
        switch (typeFactory.getRecyclerViewItem()) {
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
        switch (typeFactory.getRecyclerViewItem()) {
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
        switch (typeFactory.getRecyclerViewItem()) {
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

    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NotNull AbstractViewHolder holder, int position) {
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
        return viewType != SmallGridProductItemViewHolder.LAYOUT && viewType != RecommendationItemViewHolder.LAYOUT & viewType != SmallGridInspirationCardViewHolder.LAYOUT;
    }

    @Override
    public void onBindViewHolder(@NotNull AbstractViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            holder.bind(list.get(position), payloads);
        } else {
            super.onBindViewHolder(holder, position, payloads);
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

    public void appendItems(List<Visitable> list) {
        int start = getItemCount();
        this.list.addAll(list);
        notifyItemRangeInserted(start, list.size());
    }

    public void setWishlistButtonEnabled(String productId, boolean isEnabled) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof ProductItemDataView) {
                ProductItemDataView model = ((ProductItemDataView) list.get(i));
                if (productId.equals(model.getProductID())) {
                    model.setWishlistButtonEnabled(isEnabled);
                    notifyItemChanged(i);
                }
            }
        }
    }

    public void updateWishlistStatus(String productId, boolean isWishlisted) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof ProductItemDataView) {
                ProductItemDataView model = ((ProductItemDataView) list.get(i));
                if (productId.equals(model.getProductID())) {
                    model.setWishlisted(isWishlisted);
                    notifyItemChanged(i, "wishlist");
                }
            } else if(list.get(i) instanceof RecommendationItemDataView){
                RecommendationItemDataView model = (RecommendationItemDataView) list.get(i);
                if(productId.equals(String.valueOf(model.getRecommendationItem().getProductId()))){
                    model.getRecommendationItem().setWishlist(isWishlisted);
                    notifyItemChanged(i, "wishlist");
                }
            } else if (list.get(i) instanceof BroadMatchDataView) {
                BroadMatchDataView broadMatchDataView = (BroadMatchDataView) list.get(i);
                for (BroadMatchItemDataView broadMatchItemDataView : broadMatchDataView.getBroadMatchItemDataViewList()) {
                    if (broadMatchItemDataView.getId().equals(productId)) {
                        broadMatchItemDataView.setWishlisted(isWishlisted);
                    }
                }
            }
        }
    }

    public void updateWishlistStatus(int adapterPosition, boolean isWishlisted) {
        if (adapterPosition >= 0 && list.get(adapterPosition) instanceof ProductItemDataView) {
            ((ProductItemDataView) list.get(adapterPosition)).setWishlisted(isWishlisted);
            notifyItemChanged(adapterPosition);
        }else if (adapterPosition >= 0 && list.get(adapterPosition) instanceof RecommendationItemDataView) {
            ((RecommendationItemDataView) list.get(adapterPosition)).getRecommendationItem().setWishlist(isWishlisted);
            notifyItemChanged(adapterPosition);
        }
    }

    private boolean checkDataSize(int position) {
        return list != null && list.size() > 0
                && position > -1 && position < list.size();
    }

    public boolean isProductItem(int position) {
        return checkDataSize(position) && list.get(position) instanceof ProductItemDataView;
    }

    public boolean isRecommendationItem(int position){
        return checkDataSize(position) && list.get(position) instanceof RecommendationItemDataView;
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

    public void showEmptyState(GlobalNavDataView globalNavDataView, EmptySearchProductDataView emptySearchProductDataView) {
        clearData();

        if (globalNavDataView != null) {
            list.add(globalNavDataView);
        }

        list.add(emptySearchProductDataView);

        notifyItemRangeInserted(0, list.size());
    }

    public void clearData() {
        int itemSizeBeforeCleared = getItemCount();

        list.clear();

        notifyItemRangeRemoved(0, itemSizeBeforeCleared);
    }

    public boolean isListEmpty() {
        return list.isEmpty();
    }

    public void removePriceFilterTicker() {
        int i = 0;
        Iterator iterator = list.iterator();

        while(iterator.hasNext()) {
            Object visitable = iterator.next();

            if (visitable instanceof TickerDataView) {
                iterator.remove();
                notifyItemRangeRemoved(i, 1);
                return;
            }

            i++;
        }
    }

    public void refreshItemAtIndex(int index) {
        if (index < 0 || index >= list.size()) return;

        notifyItemChanged(index);
    }

    public List<Visitable> getItemList() {
        return list;
    }

    public interface OnItemChangeView {
        void onChangeList();

        void onChangeDoubleGrid();

        void onChangeSingleGrid();
    }
}