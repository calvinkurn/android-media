package com.tokopedia.shop.product.view.adapter.newadapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.shop.common.constant.ShopPageConstant;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductEtalaseListViewHolder;
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener;
import com.tokopedia.shop.product.view.model.newmodel.BaseShopProductViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductEtalaseListViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductEtalaseTitleViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductFeaturedViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductPromoViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductViewModel;
import com.tokopedia.shop.product.view.widget.OnStickySingleHeaderListener;
import com.tokopedia.shop.product.view.widget.StickySingleHeaderView;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_ETALASE_POSITION;
import static com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_ETALASE_TITLE_POSITION;
import static com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_FEATURED_POSITION;
import static com.tokopedia.shop.common.constant.ShopPageConstant.DEFAULT_PROMO_POSITION;
import static com.tokopedia.shop.common.constant.ShopPageConstant.ITEM_OFFSET;

public class ShopProductNewAdapter extends BaseListAdapter<BaseShopProductViewModel, ShopProductAdapterTypeFactory>
        implements DataEndlessScrollListener.OnDataEndlessScrollListener,
        StickySingleHeaderView.OnStickySingleHeaderAdapter{

    private ShopProductPromoViewModel shopProductPromoViewModel;
    private List<ShopProductViewModel> shopProductViewModelList;
    private ShopProductFeaturedViewModel shopProductFeaturedViewModel;
    private ShopProductEtalaseListViewModel shopProductEtalaseListViewModel;
    private ShopProductEtalaseTitleViewModel shopProductEtalaseTitleViewModel;

    //    private View promoView;
//    private String promoUrl;

    private ShopProductAdapterTypeFactory shopProductAdapterTypeFactory;
    private OnStickySingleHeaderListener onStickySingleHeaderViewListener;

    public ShopProductNewAdapter(ShopProductAdapterTypeFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory, null);
        this.shopProductAdapterTypeFactory = baseListAdapterTypeFactory;
        shopProductPromoViewModel = new ShopProductPromoViewModel();
        shopProductViewModelList = new ArrayList<>();
        shopProductFeaturedViewModel = new ShopProductFeaturedViewModel();
        shopProductEtalaseListViewModel = new ShopProductEtalaseListViewModel();
        shopProductEtalaseTitleViewModel = new ShopProductEtalaseTitleViewModel(null);
        visitables.add(shopProductPromoViewModel);
        visitables.add(shopProductFeaturedViewModel);
        visitables.add(shopProductEtalaseListViewModel);
        visitables.add(shopProductEtalaseTitleViewModel);
    }

    public void setShopProductPromoViewModel(ShopProductPromoViewModel shopProductPromoViewModel) {
        if (shopProductPromoViewModel == null) {
            this.shopProductPromoViewModel = new ShopProductPromoViewModel();
        } else {
            this.shopProductPromoViewModel = shopProductPromoViewModel;
        }
        setVisitable(DEFAULT_PROMO_POSITION, this.shopProductPromoViewModel);
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

    public void setShopEtalaseTitle(String etalaseName) {
        if (TextUtils.isEmpty(etalaseName)) {
            this.shopProductEtalaseTitleViewModel = new ShopProductEtalaseTitleViewModel(null);
        } else {
            this.shopProductEtalaseTitleViewModel = new ShopProductEtalaseTitleViewModel(etalaseName);
        }
        setVisitable(DEFAULT_ETALASE_TITLE_POSITION, this.shopProductEtalaseTitleViewModel);
    }

    public void setSelectedEtalaseId(String etalaseId) {
        shopProductEtalaseListViewModel.setSelectedEtalaseId(etalaseId);
        notifyItemChanged(DEFAULT_ETALASE_POSITION);
    }

    /**
     * @return true, if add etalase to current list; false if no add needed.
     */
    public boolean addEtalaseFromListMore(String etalaseId, String etalaseName) {
        List<ShopEtalaseViewModel> shopEtalaseViewModelList = shopProductEtalaseListViewModel.getEtalaseModelList();
        for (int i = 0, sizei = shopEtalaseViewModelList.size(); i < sizei; i++) {
            if (shopEtalaseViewModelList.get(i).getEtalaseId().equalsIgnoreCase(etalaseId)) {
                return false;
            }
        }
        // add the etalase by permutation
        // 1 2 3 4 5; after add 6 will be 1 6 2 3 4
        ShopEtalaseViewModel shopEtalaseViewModelToAdd = new ShopEtalaseViewModel(etalaseId, etalaseName);
        // index no 0 will always be "All Etalase", so, add from index 1.
        int indexToAdd = shopEtalaseViewModelList.size() > 1 ? 1 : 0;
        shopEtalaseViewModelList.add(indexToAdd, shopEtalaseViewModelToAdd);
        if (shopEtalaseViewModelList.size() > ShopPageConstant.ETALASE_TO_SHOW) {
            shopEtalaseViewModelList.remove(shopEtalaseViewModelList.size() - 1);
        }
        return true;
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
        clearDataExceptProduct();
        clearProductList();
    }

    public void clearDataExceptProduct() {
        setShopProductPromoViewModel(null);
        setShopProductFeaturedViewModel(null);
        setShopEtalase(null);
        setShopEtalaseTitle(null);
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
    public RecyclerView.ViewHolder createStickyViewHolder(ViewGroup parent) {
        int stickyViewType = getItemViewType(getStickyHeaderPosition());
        View view = onCreateViewItem(parent, stickyViewType);
        return shopProductAdapterTypeFactory.createViewHolder(view, stickyViewType);
    }

    @Override
    public void bindSticky(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof ShopProductEtalaseListViewHolder) {
            ((ShopProductEtalaseListViewHolder)viewHolder).bind(shopProductEtalaseListViewModel);
        }
    }

    @Override
    public void setListener(OnStickySingleHeaderListener onStickySingleHeaderViewListener) {
        this.onStickySingleHeaderViewListener = onStickySingleHeaderViewListener;
    }

    public void refreshSticky(){
        if (onStickySingleHeaderViewListener!= null) {
            onStickySingleHeaderViewListener.refreshSticky();
        }
    }

}
