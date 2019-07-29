package com.tokopedia.shop.product.view.adapter;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.common.constant.ShopPageConstant;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseListViewHolder;
import com.tokopedia.shop.product.view.model.BaseShopProductViewModel;
import com.tokopedia.shop.product.view.model.EtalaseHighlightCarouselViewModel;
import com.tokopedia.shop.product.view.model.ShopMerchantVoucherViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseHighlightViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseListViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseTitleViewModel;
import com.tokopedia.shop.product.view.model.ShopProductFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.shop.product.view.widget.OnStickySingleHeaderListener;
import com.tokopedia.shop.product.view.widget.StickySingleHeaderView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.shop.analytic.ShopPageTrackingConstant.ALL_ETALASE;
import static com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_ETALASE_HIGHLIGHT_POSITION;
import static com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_ETALASE_POSITION;
import static com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_ETALASE_TITLE_POSITION;
import static com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_FEATURED_POSITION;
import static com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_MERCHANT_VOUCHER_POSITION;
import static com.tokopedia.shop.common.constant.ShopPageConstant.ITEM_OFFSET;

public class ShopProductAdapter extends BaseListAdapter<BaseShopProductViewModel, ShopProductAdapterTypeFactory>
        implements DataEndlessScrollListener.OnDataEndlessScrollListener,
        StickySingleHeaderView.OnStickySingleHeaderAdapter {

    private boolean needToShowEtalase = false;

    private ShopMerchantVoucherViewModel shopMerchantVoucherViewModel;
    private List<ShopProductViewModel> shopProductViewModelList;
    private ShopProductFeaturedViewModel shopProductFeaturedViewModel;
    private ShopProductEtalaseListViewModel shopProductEtalaseListViewModel;
    private ShopProductEtalaseTitleViewModel shopProductEtalaseTitleViewModel;
    private ShopProductEtalaseHighlightViewModel shopProductEtalaseHighlightViewModel;

    private ShopProductAdapterTypeFactory shopProductAdapterTypeFactory;
    private OnStickySingleHeaderListener onStickySingleHeaderViewListener;
    private RecyclerView recyclerView;

    // this view holder is to hold the state between the sticky and non-sticky etalase view holder.
    private WeakReference<ShopProductEtalaseListViewHolder> shopProductEtalaseListViewHolderWeakReference;
    private WeakReference<ShopProductEtalaseListViewHolder> shopProductEtalaseListStickyWeakReference;

    public ShopProductAdapter(ShopProductAdapterTypeFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory, null);
        this.shopProductAdapterTypeFactory = baseListAdapterTypeFactory;
        shopMerchantVoucherViewModel = new ShopMerchantVoucherViewModel(null);
        shopProductViewModelList = new ArrayList<>();
        shopProductFeaturedViewModel = new ShopProductFeaturedViewModel();
        shopProductEtalaseHighlightViewModel = new ShopProductEtalaseHighlightViewModel(null);
        shopProductEtalaseListViewModel = new ShopProductEtalaseListViewModel();
        shopProductEtalaseTitleViewModel = new ShopProductEtalaseTitleViewModel(null, null);
        visitables.add(shopMerchantVoucherViewModel);
        visitables.add(shopProductFeaturedViewModel);
        visitables.add(shopProductEtalaseHighlightViewModel);
        visitables.add(shopProductEtalaseListViewModel);
        visitables.add(shopProductEtalaseTitleViewModel);

        baseListAdapterTypeFactory.attachAdapter(this);
    }

    public void setNeedToShowEtalase(boolean needToShowEtalase) {
        if (this.needToShowEtalase != needToShowEtalase) {
            this.needToShowEtalase = needToShowEtalase;
            notifyItemChanged(DEFAULT_ETALASE_POSITION);
            notifyItemChanged(DEFAULT_ETALASE_TITLE_POSITION);
        }
    }

    public String getEtalaseNameHighLight(ShopProductViewModel shopProductViewModel) {
        ShopProductEtalaseHighlightViewModel shopProductEtalaseHighlightViewModel = getShopProductEtalaseHighlightViewModel();
        List<EtalaseHighlightCarouselViewModel> etalaseHighlightCarouselViewModelList =
                shopProductEtalaseHighlightViewModel.getEtalaseHighlightCarouselViewModelList();
        for (int i = 0, sizei = etalaseHighlightCarouselViewModelList.size(); i < sizei; i++) {
            List<ShopProductViewModel> shopProductViewModelList = etalaseHighlightCarouselViewModelList.get(i).getShopProductViewModelList();
            for (int j =0, sizej = shopProductViewModelList.size(); j<sizej; j++) {
                ShopProductViewModel shopProductViewModelEtalase = shopProductViewModelList.get(j);
                if (shopProductViewModelEtalase.getId().equals(shopProductViewModel.getId())) {
                    return etalaseHighlightCarouselViewModelList.get(i).getShopEtalaseViewModel().getEtalaseName();
                }
            }
        }
        return ALL_ETALASE;
    }

    public boolean isNeedToShowEtalase() {
        return needToShowEtalase;
    }

    public void setShopMerchantVoucherViewModel(ShopMerchantVoucherViewModel shopMerchantVoucherViewModel) {
        if (shopMerchantVoucherViewModel == null) {
            this.shopMerchantVoucherViewModel = new ShopMerchantVoucherViewModel(null);
        } else {
            this.shopMerchantVoucherViewModel = shopMerchantVoucherViewModel;
        }
        setVisitable(DEFAULT_MERCHANT_VOUCHER_POSITION, this.shopMerchantVoucherViewModel);
    }

    public void setShopProductFeaturedViewModel(ShopProductFeaturedViewModel shopProductFeaturedViewModel) {
        if (shopProductFeaturedViewModel == null) {
            this.shopProductFeaturedViewModel = new ShopProductFeaturedViewModel();
        } else {
            this.shopProductFeaturedViewModel = shopProductFeaturedViewModel;
        }
        setVisitable(DEFAULT_FEATURED_POSITION, this.shopProductFeaturedViewModel);
    }

    public void setShopEtalase(ShopProductEtalaseListViewModel shopProductEtalaseListViewModel) {
        if (shopProductEtalaseListViewModel == null) {
            this.shopProductEtalaseListViewModel = new ShopProductEtalaseListViewModel();
        } else {
            this.shopProductEtalaseListViewModel = shopProductEtalaseListViewModel;
        }
        setVisitable(DEFAULT_ETALASE_POSITION, this.shopProductEtalaseListViewModel);
    }

    public void setShopProductEtalaseHighlightViewModel(ShopProductEtalaseHighlightViewModel shopProductEtalaseHighlightViewModel) {
        if (shopProductEtalaseHighlightViewModel == null) {
            this.shopProductEtalaseHighlightViewModel = new ShopProductEtalaseHighlightViewModel();
        } else {
            this.shopProductEtalaseHighlightViewModel = shopProductEtalaseHighlightViewModel;
        }
        setVisitable(DEFAULT_ETALASE_HIGHLIGHT_POSITION, this.shopProductEtalaseHighlightViewModel);
    }

    public ShopProductEtalaseHighlightViewModel getShopProductEtalaseHighlightViewModel() {
        return shopProductEtalaseHighlightViewModel;
    }

    public void setShopEtalaseTitle(String etalaseName, String etalaseBadge) {
        if (TextUtils.isEmpty(etalaseName)) {
            this.shopProductEtalaseTitleViewModel = new ShopProductEtalaseTitleViewModel(null, null);
        } else {
            this.shopProductEtalaseTitleViewModel = new ShopProductEtalaseTitleViewModel(etalaseName, etalaseBadge);
        }
        setVisitable(DEFAULT_ETALASE_TITLE_POSITION, this.shopProductEtalaseTitleViewModel);
    }

    public void setSelectedEtalaseId(String etalaseId) {
        shopProductEtalaseListViewModel.setSelectedEtalaseId(etalaseId);
        notifyItemChanged(DEFAULT_ETALASE_POSITION);
    }

    public ShopProductFeaturedViewModel getShopProductFeaturedViewModel() {
        return shopProductFeaturedViewModel;
    }

    public boolean isEtalaseInChip(String etalaseId) {
        List<ShopEtalaseViewModel> shopEtalaseViewModelList = shopProductEtalaseListViewModel.getEtalaseModelList();
        for (int i = 0, sizei = shopEtalaseViewModelList.size(); i < sizei; i++) {
            if (shopEtalaseViewModelList.get(i).getEtalaseId().equalsIgnoreCase(etalaseId)) {
                return true;
            }
        }
        return false;
    }

    public ShopProductEtalaseListViewModel getShopProductEtalaseListViewModel() {
        return shopProductEtalaseListViewModel;
    }

    private void setVisitable(int position, Visitable visitable) {
        visitables.set(position, visitable);
        notifyItemChanged(position);
    }

    @Override
    public void showErrorNetwork(String message, ErrorNetworkModel.OnRetryListener onRetryListener) {
        errorNetworkModel.setErrorMessage(message);
        errorNetworkModel.setOnRetryListener(onRetryListener);
        this.shopProductViewModelList.clear();
        clearAllNonDataElement();
        visitables.add(errorNetworkModel);
        notifyDataSetChanged();
    }

    public void clearProductList() {
        int productSize = shopProductViewModelList.size();
        if (visitables.size() > ITEM_OFFSET) {
            visitables.subList(ITEM_OFFSET, ITEM_OFFSET + productSize).clear();
            notifyItemRangeRemoved(ITEM_OFFSET, ITEM_OFFSET + productSize);
        }
        shopProductViewModelList.clear();
    }

    public void addProductList(List<ShopProductViewModel> shopProductViewModelArrayList) {
        this.shopProductViewModelList.addAll(shopProductViewModelArrayList);
        visitables.addAll(shopProductViewModelArrayList);
    }

    public void replaceProductList(List<ShopProductViewModel> shopProductViewModelArrayList) {
        if (this.shopProductViewModelList == shopProductViewModelArrayList) {
            return;
        }
        if (this.shopProductViewModelList.size() > 0) {
            clearProductList();
        }
        addProductList(shopProductViewModelArrayList);
    }

    @Override
    public void clearAllElements() {
        clearMerchantVoucherData();
        clearFeaturedData();
        clearEtalaseHighlightData();
        clearEtalaseData();
        clearProductList();
    }


    public void clearMerchantVoucherData() {
        setShopMerchantVoucherViewModel(null);
    }

    public void clearFeaturedData() {
        setShopProductFeaturedViewModel(null);
    }

    public void clearEtalaseData() {
        setShopProductFeaturedViewModel(null);
    }

    public void clearEtalaseHighlightData() {
        setShopProductEtalaseHighlightViewModel(null);
    }

    public List<ShopProductViewModel> getShopProductViewModelList() {
        return shopProductViewModelList;
    }

    public void updateWishListStatus(String productId, boolean wishList) {
        for (int i = 0, sizei = shopProductViewModelList.size(); i < sizei; i++) {
            ShopProductViewModel shopProductViewModel = shopProductViewModelList.get(i);
            if (shopProductViewModel.getId().equalsIgnoreCase(productId)) {
                shopProductViewModel.setWishList(wishList);
                notifyItemChanged(ITEM_OFFSET + i);
                break;
            }
        }
        boolean isFeaturedChanged = shopProductFeaturedViewModel.updateWishListStatus(productId, wishList);
        if (isFeaturedChanged) {
            notifyItemChanged(DEFAULT_FEATURED_POSITION);
        }

        boolean isEtalaseChanged = shopProductEtalaseHighlightViewModel.updateWishListStatus(productId, wishList);
        if (isEtalaseChanged) {
            notifyItemChanged(DEFAULT_ETALASE_HIGHLIGHT_POSITION);
        }

    }

    @Override
    protected boolean isShowLoadingMore() {
        return shopProductViewModelList.size() > 0;
    }

    @Override
    protected boolean isItemClickableByDefault() {
        return false;
    }

    @Override
    public int getEndlessDataSize() {
        return shopProductViewModelList.size();
    }

    @Override
    public int getStickyHeaderPosition() {
        return ShopPageConstant.DEFAULT_ETALASE_POSITION;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AbstractViewHolder abstractViewHolder = super.onCreateViewHolder(parent, viewType);
        if (viewType == ShopProductEtalaseListViewHolder.LAYOUT &&
                abstractViewHolder instanceof ShopProductEtalaseListViewHolder) {
            shopProductEtalaseListViewHolderWeakReference = new WeakReference<>((ShopProductEtalaseListViewHolder) abstractViewHolder);
        }
        return abstractViewHolder;
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        // mechanism to transfer the state from sticky etalase state to non-sticky view holder
        if (holder instanceof ShopProductEtalaseListViewHolder) {
            if (onStickySingleHeaderViewListener != null &&
                    onStickySingleHeaderViewListener.isStickyShowed()) {
                Parcelable recyclerViewState = null;
                try {
                    if (shopProductEtalaseListStickyWeakReference != null &&
                            shopProductEtalaseListStickyWeakReference.get() != null) {
                        recyclerViewState = shopProductEtalaseListStickyWeakReference.get().getRecyclerViewState();
                    }
                } catch (Throwable e) {
                    recyclerViewState = null;
                }
                ((ShopProductEtalaseListViewHolder) holder).setRecyclerViewState(recyclerViewState);
            } else {
                ((ShopProductEtalaseListViewHolder) holder).setRecyclerViewState(null);
            }
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public RecyclerView.ViewHolder createStickyViewHolder(ViewGroup parent) {
        // mechanism to transfer the state from sticky view holder to current etalase state
        Parcelable recyclerViewState = null;
        try {
            if (shopProductEtalaseListStickyWeakReference != null &&
                    shopProductEtalaseListStickyWeakReference.get() != null) {
                recyclerViewState = shopProductEtalaseListStickyWeakReference.get().getRecyclerViewState();
            }
        } catch (Throwable e) {
            recyclerViewState = null;
        }
        if (recyclerViewState != null) {
            if (shopProductEtalaseListViewHolderWeakReference != null &&
                    shopProductEtalaseListViewHolderWeakReference.get() != null) {
                shopProductEtalaseListViewHolderWeakReference.get().setRecyclerViewState(recyclerViewState);
            }
        }

        int stickyViewType = getItemViewType(getStickyHeaderPosition());
        View view = onCreateViewItem(parent, stickyViewType);
        AbstractViewHolder abstractViewHolder = shopProductAdapterTypeFactory.createViewHolder(view, stickyViewType);
        if (abstractViewHolder instanceof ShopProductEtalaseListViewHolder) {
            shopProductEtalaseListStickyWeakReference = new WeakReference<>((ShopProductEtalaseListViewHolder) abstractViewHolder);
        }
        return abstractViewHolder;
    }

    @Override
    public void bindSticky(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof ShopProductEtalaseListViewHolder) {
            // mechanism to transfer the state from the current etalase state to sticky view holder
            Parcelable recyclerViewState = null;
            try {
                if (shopProductEtalaseListViewHolderWeakReference != null &&
                        shopProductEtalaseListViewHolderWeakReference.get() != null) {
                    recyclerViewState = shopProductEtalaseListViewHolderWeakReference.get().getRecyclerViewState();
                }
            } catch (Throwable e) {
                recyclerViewState = null;
            }
            ((ShopProductEtalaseListViewHolder) viewHolder).setRecyclerViewState(recyclerViewState);
            ((ShopProductEtalaseListViewHolder) viewHolder).bind(shopProductEtalaseListViewModel);
        }
    }

    @Override
    public void setListener(OnStickySingleHeaderListener onStickySingleHeaderViewListener) {
        this.onStickySingleHeaderViewListener = onStickySingleHeaderViewListener;
    }

    public void refreshSticky() {
        if (onStickySingleHeaderViewListener != null) {
            recyclerView.post(() -> onStickySingleHeaderViewListener.refreshSticky());
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }
}
