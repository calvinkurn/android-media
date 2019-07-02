package com.tokopedia.checkout.view.feature.cartlist.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartTickerErrorData;
import com.tokopedia.checkout.domain.datamodel.cartlist.ShopGroupData;
import com.tokopedia.checkout.view.common.PromoActionListener;
import com.tokopedia.checkout.view.common.holderitemdata.CartItemTickerErrorHolderData;
import com.tokopedia.checkout.view.common.viewholder.CartPromoSuggestionViewHolder;
import com.tokopedia.checkout.view.common.viewholder.CartVoucherPromoViewHolder;
import com.tokopedia.checkout.view.common.viewholder.ShipmentSellerCashbackViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.ActionListener;
import com.tokopedia.checkout.view.feature.cartlist.viewholder.CartEmptyViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.viewholder.CartLoadingViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.viewholder.CartRecentViewViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.viewholder.CartRecommendationViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.viewholder.CartSectionHeaderViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.viewholder.CartShopViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.viewholder.CartTickerErrorViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.viewholder.CartTopAdsViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.viewholder.CartWishlistViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartEmptyHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartItemHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartLoadingHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartRecentViewHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartRecentViewItemHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartRecommendationItemHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartSectionHeaderHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartShopHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartWishlistHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartWishlistItemHolderData;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentSellerCashbackModel;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.promocheckout.common.view.model.PromoData;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ActionListener actionListener;
    private final PromoActionListener promoActionListener;
    private final CartItemAdapter.ActionListener cartItemActionListener;
    private List<Object> cartDataList;
    private ShipmentSellerCashbackModel shipmentSellerCashbackModel;
    private CompositeSubscription compositeSubscription;
    private CartEmptyHolderData cartEmptyHolderData;
    private CartLoadingHolderData cartLoadingHolderData;
    private CartWishlistAdapter cartWishlistAdapter;
    private CartRecentViewAdapter cartRecentViewAdapter;

    @Inject
    public CartAdapter(ActionListener actionListener, PromoActionListener promoActionListener,
                       CartItemAdapter.ActionListener cartItemActionListener) {
        this.cartDataList = new ArrayList<>();
        this.actionListener = actionListener;
        this.cartItemActionListener = cartItemActionListener;
        this.promoActionListener = promoActionListener;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public int getItemViewType(int position) {
        if (cartDataList.get(position) instanceof CartShopHolderData) {
            return CartShopViewHolder.TYPE_VIEW_ITEM_SHOP;
        } else if (cartDataList.get(position) instanceof CartPromoSuggestion) {
            return CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION;
        } else if (cartDataList.get(position) instanceof PromoStackingData) {
            return CartVoucherPromoViewHolder.TYPE_VIEW_PROMO;
        } else if (cartDataList.get(position) instanceof CartItemTickerErrorHolderData) {
            return CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR;
        } else if (cartDataList.get(position) instanceof ShipmentSellerCashbackModel) {
            return ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK;
        } else if (cartDataList.get(position) instanceof TopAdsModel) {
            return CartTopAdsViewHolder.TYPE_VIEW_CART_TOPADS;
        } else if (cartDataList.get(position) instanceof CartEmptyHolderData) {
            return CartEmptyViewHolder.Companion.getLAYOUT();
        } else if (cartDataList.get(position) instanceof CartRecentViewHolderData) {
            return CartRecentViewViewHolder.Companion.getLAYOUT();
        } else if (cartDataList.get(position) instanceof CartWishlistHolderData) {
            return CartWishlistViewHolder.Companion.getLAYOUT();
        } else if (cartDataList.get(position) instanceof CartSectionHeaderHolderData) {
            return CartSectionHeaderViewHolder.Companion.getLAYOUT();
        } else if (cartDataList.get(position) instanceof CartRecommendationItemHolderData) {
            return CartRecommendationViewHolder.Companion.getLAYOUT();
        } else if (cartDataList.get(position) instanceof CartLoadingHolderData) {
            return CartLoadingViewHolder.Companion.getLAYOUT();
        } else {
            return super.getItemViewType(position);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartShopViewHolder.TYPE_VIEW_ITEM_SHOP, parent, false);
            return new CartShopViewHolder(view, actionListener, cartItemActionListener, compositeSubscription);
        } else if (viewType == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION, parent, false);
            return new CartPromoSuggestionViewHolder(view, promoActionListener);
        } else if (viewType == CartVoucherPromoViewHolder.TYPE_VIEW_PROMO) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartVoucherPromoViewHolder.TYPE_VIEW_PROMO, parent, false);
            return new CartVoucherPromoViewHolder(view, promoActionListener);
        } else if (viewType == CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR, parent, false);
            return new CartTickerErrorViewHolder(view, actionListener);
        } else if (viewType == ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK, parent, false);
            return new ShipmentSellerCashbackViewHolder(view);
        } else if (viewType == CartTopAdsViewHolder.TYPE_VIEW_CART_TOPADS) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartTopAdsViewHolder.TYPE_VIEW_CART_TOPADS, parent, false);
            return new CartTopAdsViewHolder(view, actionListener);
        } else if (viewType == CartEmptyViewHolder.Companion.getLAYOUT()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartEmptyViewHolder.Companion.getLAYOUT(), parent, false);
            return new CartEmptyViewHolder(view, actionListener);
        } else if (viewType == CartRecentViewViewHolder.Companion.getLAYOUT()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartRecentViewViewHolder.Companion.getLAYOUT(), parent, false);
            return new CartRecentViewViewHolder(view, actionListener);
        } else if (viewType == CartWishlistViewHolder.Companion.getLAYOUT()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartWishlistViewHolder.Companion.getLAYOUT(), parent, false);
            return new CartWishlistViewHolder(view, actionListener);
        } else if (viewType == CartSectionHeaderViewHolder.Companion.getLAYOUT()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartSectionHeaderViewHolder.Companion.getLAYOUT(), parent, false);
            return new CartSectionHeaderViewHolder(view, actionListener);
        } else if (viewType == CartRecommendationViewHolder.Companion.getLAYOUT()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartRecommendationViewHolder.Companion.getLAYOUT(), parent, false);
            return new CartRecommendationViewHolder(view, actionListener);
        } else if (viewType == CartLoadingViewHolder.Companion.getLAYOUT()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartLoadingViewHolder.Companion.getLAYOUT(), parent, false);
            return new CartLoadingViewHolder(view);
        }
        throw new RuntimeException("No view holder type found");
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            final CartShopViewHolder holderView = (CartShopViewHolder) holder;
            final CartShopHolderData data = (CartShopHolderData) cartDataList.get(position);
            holderView.bindData(data, position);
        } else if (getItemViewType(position) == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION) {
            final CartPromoSuggestionViewHolder holderView = (CartPromoSuggestionViewHolder) holder;
            final CartPromoSuggestion data = (CartPromoSuggestion) cartDataList.get(position);
            holderView.bindData(data, position);
        } else if (getItemViewType(position) == CartVoucherPromoViewHolder.TYPE_VIEW_PROMO) {
            final CartVoucherPromoViewHolder holderView = (CartVoucherPromoViewHolder) holder;
            final PromoStackingData data = (PromoStackingData) cartDataList.get(position);
            holderView.bindData(data, position);
        } else if (getItemViewType(position) == CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR) {
            final CartTickerErrorViewHolder holderView = (CartTickerErrorViewHolder) holder;
            final CartItemTickerErrorHolderData data = (CartItemTickerErrorHolderData) cartDataList.get(position);
            holderView.bindData(data, position);
        } else if (getItemViewType(position) == ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK) {
            final ShipmentSellerCashbackViewHolder holderView = (ShipmentSellerCashbackViewHolder) holder;
            final ShipmentSellerCashbackModel data = (ShipmentSellerCashbackModel) cartDataList.get(position);
            holderView.bindViewHolder(data);
        } else if (getItemViewType(position) == CartTopAdsViewHolder.TYPE_VIEW_CART_TOPADS) {
            final CartTopAdsViewHolder holderView = (CartTopAdsViewHolder) holder;
            final TopAdsModel data = (TopAdsModel) cartDataList.get(position);
            holderView.renderTopAds(data);
        } else if (getItemViewType(position) == CartEmptyViewHolder.Companion.getLAYOUT()) {
            final CartEmptyViewHolder holderView = (CartEmptyViewHolder) holder;
            final CartEmptyHolderData data = (CartEmptyHolderData) cartDataList.get(position);
            holderView.bind(data);
        } else if (getItemViewType(position) == CartRecentViewViewHolder.Companion.getLAYOUT()) {
            final CartRecentViewViewHolder holderView = (CartRecentViewViewHolder) holder;
            final CartRecentViewHolderData data = (CartRecentViewHolderData) cartDataList.get(position);
            holderView.bind(data);
            cartRecentViewAdapter = holderView.getRecentViewAdapter();
        } else if (getItemViewType(position) == CartWishlistViewHolder.Companion.getLAYOUT()) {
            final CartWishlistViewHolder holderView = (CartWishlistViewHolder) holder;
            final CartWishlistHolderData data = (CartWishlistHolderData) cartDataList.get(position);
            holderView.bind(data);
            cartWishlistAdapter = holderView.getWishlistAdapter();
        } else if (getItemViewType(position) == CartSectionHeaderViewHolder.Companion.getLAYOUT()) {
            final CartSectionHeaderViewHolder holderView = (CartSectionHeaderViewHolder) holder;
            final CartSectionHeaderHolderData data = (CartSectionHeaderHolderData) cartDataList.get(position);
            holderView.bind(data);
        } else if (getItemViewType(position) == CartRecommendationViewHolder.Companion.getLAYOUT()) {
            final CartRecommendationViewHolder holderView = (CartRecommendationViewHolder) holder;
            final CartRecommendationItemHolderData data = (CartRecommendationItemHolderData) cartDataList.get(position);
            holderView.bind(data);
        } else if (getItemViewType(position) == CartLoadingViewHolder.Companion.getLAYOUT()) {
            final CartLoadingViewHolder holderView = (CartLoadingViewHolder) holder;
            final CartLoadingHolderData data = (CartLoadingHolderData) cartDataList.get(position);
            holderView.bind(data);
        }
    }

    @Override
    public int getItemCount() {
        return cartDataList.size();
    }

    public void unsubscribeSubscription() {
        compositeSubscription.unsubscribe();
    }

    public void addDataList(List<ShopGroupData> shopGroupDataList) {
        for (ShopGroupData shopGroupData : shopGroupDataList) {
            CartShopHolderData cartShopHolderData = new CartShopHolderData();
            if (shopGroupData.isError()) {
                cartShopHolderData.setAllSelected(false);
            } else {
                cartShopHolderData.setAllSelected(shopGroupData.isChecked());
            }
            cartShopHolderData.setShopGroupData(shopGroupData);
            cartDataList.add(cartShopHolderData);
        }
        notifyDataSetChanged();
    }

    public List<CartShopHolderData> getAllShopGroupDataList() {
        List<CartShopHolderData> cartShopHolderDataFinalList = new ArrayList<>();
        for (int i = 0; i < cartDataList.size(); i++) {
            Object object = cartDataList.get(i);
            if (object instanceof CartShopHolderData) {
                cartShopHolderDataFinalList.add((CartShopHolderData) object);
            }
        }
        return cartShopHolderDataFinalList;
    }

    public List<CartItemData> getSelectedCartItemData() {
        List<CartItemData> cartItemDataList = new ArrayList<>();
        if (cartDataList != null) {
            for (Object data : cartDataList) {
                if (data instanceof CartShopHolderData) {
                    CartShopHolderData cartShopHolderData = (CartShopHolderData) data;
                    if ((cartShopHolderData.isPartialSelected() || cartShopHolderData.isAllSelected()) &&
                            cartShopHolderData.getShopGroupData().getCartItemDataList() != null) {
                        for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                            if (cartItemHolderData.isSelected() && !cartItemHolderData.getCartItemData().isError()) {
                                cartItemDataList.add(cartItemHolderData.getCartItemData());
                            }
                        }
                    }
                }
            }
        }

        return cartItemDataList;
    }

    public List<CartShopHolderData> getSelectedCartShopHolderData() {
        List<CartShopHolderData> cartShopHolderDataList = new ArrayList<>();
        if (cartDataList != null) {
            for (Object data : cartDataList) {
                if (data instanceof CartShopHolderData) {
                    CartShopHolderData cartShopHolderData = (CartShopHolderData) data;
                    if ((cartShopHolderData.isPartialSelected() || cartShopHolderData.isAllSelected()) &&
                            cartShopHolderData.getShopGroupData().getCartItemDataList() != null) {
                        cartShopHolderDataList.add(cartShopHolderData);
                    }
                }
            }
        }

        return cartShopHolderDataList;
    }

    public List<CartItemData> getAllCartItemData() {
        List<CartItemData> cartItemDataList = new ArrayList<>();
        if (cartDataList != null) {
            for (Object data : cartDataList) {
                if (data instanceof CartShopHolderData) {
                    CartShopHolderData cartShopHolderData = (CartShopHolderData) data;
                    if (cartShopHolderData.getShopGroupData().getCartItemDataList() != null) {
                        for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                            cartItemDataList.add(cartItemHolderData.getCartItemData());
                        }
                    }
                }
            }
        }

        return cartItemDataList;
    }

    public List<CartItemHolderData> getAllCartItemHolderData() {
        List<CartItemHolderData> cartItemDataList = new ArrayList<>();
        if (cartDataList != null) {
            for (Object data : cartDataList) {
                if (data instanceof CartShopHolderData) {
                    CartShopHolderData cartShopHolderData = (CartShopHolderData) data;
                    if (cartShopHolderData.getShopGroupData().getCartItemDataList() != null) {
                        cartItemDataList.addAll(cartShopHolderData.getShopGroupData().getCartItemDataList());
                    }
                }
            }
        }

        return cartItemDataList;
    }

    public void setAllShopSelected(boolean selected) {
        if (cartDataList != null) {
            for (int i = 0; i < cartDataList.size(); i++) {
                if (cartDataList.get(i) instanceof CartShopHolderData) {
                    CartShopHolderData cartShopHolderData = ((CartShopHolderData) cartDataList.get(i));
                    if (cartShopHolderData.getShopGroupData().getCartItemDataList() != null &&
                            cartShopHolderData.getShopGroupData().getCartItemDataList().size() == 1) {
                        for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                            if (cartItemHolderData.getCartItemData().isError() && cartItemHolderData.getCartItemData().isSingleChild()) {
                                setShopSelected(i, false);
                            } else {
                                setShopSelected(i, selected);
                            }
                        }
                    } else {
                        setShopSelected(i, selected);
                    }
                }
            }
        }
    }

    public void setShopSelected(int position, boolean selected) {
        Object object = cartDataList.get(position);
        if (object instanceof CartShopHolderData) {
            CartShopHolderData cartShopHolderData = (CartShopHolderData) object;
            cartShopHolderData.setAllSelected(selected);
            for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                cartItemHolderData.setSelected(selected);
            }
        }
    }

    public boolean setItemSelected(int position, int parentPosition, boolean selected) {
        boolean needToUpdateParent = false;
        Object object = cartDataList.get(parentPosition);
        if (object instanceof CartShopHolderData) {
            CartShopHolderData cartShopHolderData = (CartShopHolderData) object;
            boolean shopAlreadySelected = cartShopHolderData.isAllSelected() || cartShopHolderData.isPartialSelected();
            int selectedCount = 0;
            for (int i = 0; i < cartShopHolderData.getShopGroupData().getCartItemDataList().size(); i++) {
                CartItemHolderData cartItemHolderData = cartShopHolderData.getShopGroupData().getCartItemDataList().get(i);
                if (i == position) {
                    cartItemHolderData.setSelected(selected);
                }

                if (cartItemHolderData.isSelected()) {
                    selectedCount++;
                }
            }

            if (selectedCount == 0) {
                cartShopHolderData.setAllSelected(false);
                cartShopHolderData.setPartialSelected(false);
                needToUpdateParent = shopAlreadySelected;
            } else if (selectedCount > 0 && selectedCount < cartShopHolderData.getShopGroupData().getCartItemDataList().size()) {
                cartShopHolderData.setAllSelected(false);
                cartShopHolderData.setPartialSelected(true);
                needToUpdateParent = !shopAlreadySelected;
            } else {
                cartShopHolderData.setAllSelected(true);
                cartShopHolderData.setPartialSelected(false);
                needToUpdateParent = !shopAlreadySelected;
            }

            // Check does has cash back item
            boolean hasCashbackItem = false;
            for (CartItemHolderData data : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                if (data.getCartItemData().getOriginData().isCashBack()) {
                    hasCashbackItem = true;
                    break;
                }
            }
            // Check is it the last cash back item to uncheck & still contain non cash back item
            boolean isTheLastCashbackItem = true;
            boolean hasUncheckedItem = false;
            if (!selected && hasCashbackItem) {
                // Check does it still contain unchecked item
                for (CartItemHolderData data : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                    if (data.isSelected() && !data.getCartItemData().getOriginData().isCashBack()) {
                        hasUncheckedItem = true;
                        break;
                    }
                }
                // Check is it the last cash back item to be unchecked
                for (int j = 0; j < cartShopHolderData.getShopGroupData().getCartItemDataList().size(); j++) {
                    CartItemHolderData data = cartShopHolderData.getShopGroupData().getCartItemDataList().get(j);
                    if (j != position && data.isSelected() && data.getCartItemData().getOriginData().isCashBack()) {
                        isTheLastCashbackItem = false;
                        break;
                    }
                }
            }
            if (hasUncheckedItem && isTheLastCashbackItem) {
                needToUpdateParent = true;
            }
        }

        return needToUpdateParent;
    }

    public void increaseQuantity(int position, int parentPosition) {
        if (getItemViewType(parentPosition) == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            ((CartShopHolderData) cartDataList.get(parentPosition)).getShopGroupData()
                    .getCartItemDataList().get(position).getCartItemData().getUpdatedData().increaseQuantity();
        }
        checkForShipmentForm();
    }

    public void decreaseQuantity(int position, int parentPosition) {
        if (getItemViewType(parentPosition) == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            ((CartShopHolderData) cartDataList.get(parentPosition)).getShopGroupData()
                    .getCartItemDataList().get(position).getCartItemData().getUpdatedData().decreaseQuantity();
        }
        checkForShipmentForm();
    }

    public void resetQuantity(int position, int parentPosition) {
        if (getItemViewType(parentPosition) == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            ((CartShopHolderData) cartDataList.get(parentPosition)).getShopGroupData()
                    .getCartItemDataList().get(position).getCartItemData().getUpdatedData().resetQuantity();
        }
        checkForShipmentForm();
    }

    public void notifyItems(int position) {
        Object itemData = cartDataList.get(position);
        String itemDataParentId = ((CartItemHolderData) itemData).getCartItemData().getOriginData().getParentId();
        notifyItemChanged(position);
        for (Object object : cartDataList) {
            if (object instanceof CartItemHolderData) {
                String parentId = ((CartItemHolderData) object).getCartItemData().getOriginData().getParentId();
                if (parentId.equals(itemDataParentId)) {
                    notifyItemChanged(cartDataList.indexOf(object));
                }
            }
        }
    }

    public void addPromoSuggestion(CartPromoSuggestion cartPromoSuggestion) {
        cartDataList.add(cartPromoSuggestion);
        notifyDataSetChanged();
        checkForShipmentForm();
    }

    public void resetData() {
        cartDataList.clear();
        notifyDataSetChanged();
        checkForShipmentForm();
    }

    public void updateItemPromoStackVoucher(PromoStackingData promoStackingData) {
        for (int i = 0; i < cartDataList.size(); i++) {
            Object object = cartDataList.get(i);
            if (object instanceof PromoStackingData) {
                cartDataList.set(i, promoStackingData);
                notifyItemChanged(i);
            } else if (object instanceof CartPromoSuggestion) {
                ((CartPromoSuggestion) object).setVisible(false);
                notifyItemChanged(i);
            }
        }
    }

    public void addPromoStackingVoucherData(PromoStackingData promoStackingData) {
        cartDataList.add(promoStackingData);
        notifyDataSetChanged();
        checkForShipmentForm();
    }

    public void removePromoStackingVoucherData() {
        if (cartDataList.get(0) instanceof PromoStackingData) {
            cartDataList.remove(cartDataList.get(0));
            notifyItemRemoved(0);
        }
    }

    public void addCartTickerError(CartItemTickerErrorHolderData cartItemTickerErrorHolderData) {
        cartDataList.add(cartItemTickerErrorHolderData);
        notifyDataSetChanged();
        checkForShipmentForm();
    }

    public void addCartEmptyData() {
        if (cartEmptyHolderData == null) {
            cartEmptyHolderData = new CartEmptyHolderData();
        }
        cartDataList.add(cartEmptyHolderData);
        notifyDataSetChanged();
    }

    public void addCartRecentViewData(CartSectionHeaderHolderData cartSectionHeaderHolderData,
                                      CartRecentViewHolderData cartRecentViewHolderData) {
        int recentViewIndex = 0;
        for (Object item : cartDataList) {
            if (item instanceof CartEmptyHolderData ||
                    item instanceof CartShopHolderData) {
                recentViewIndex = cartDataList.indexOf(item);
            }
        }
        int startIndex = recentViewIndex + 1;
        cartDataList.add(++recentViewIndex, cartSectionHeaderHolderData);
        cartDataList.add(++recentViewIndex, cartRecentViewHolderData);
        notifyItemRangeInserted(startIndex, 2);
    }

    public void addCartWishlistData(CartSectionHeaderHolderData cartSectionHeaderHolderData,
                                    CartWishlistHolderData cartWishlistHolderData) {
        int wishlistIndex = 0;
        for (Object item : cartDataList) {
            if (item instanceof CartEmptyHolderData ||
                    item instanceof CartShopHolderData ||
                    item instanceof CartRecentViewHolderData) {
                wishlistIndex = cartDataList.indexOf(item);
            }
        }
        int startIndex = wishlistIndex + 1;
        cartDataList.add(++wishlistIndex, cartSectionHeaderHolderData);
        cartDataList.add(++wishlistIndex, cartWishlistHolderData);
        notifyItemRangeInserted(startIndex, 2);
    }

    public void addCartRecommendationData(CartSectionHeaderHolderData cartSectionHeaderHolderData,
                                          List<CartRecommendationItemHolderData> cartRecommendationItemHolderDataList) {
        int recommendationIndex = 0;
        for (Object item : cartDataList) {
            if (item instanceof CartEmptyHolderData ||
                    item instanceof CartShopHolderData ||
                    item instanceof CartRecentViewHolderData ||
                    item instanceof CartWishlistHolderData ||
                    item instanceof CartRecommendationItemHolderData) {
                recommendationIndex = cartDataList.indexOf(item);
            }
        }

        int startIndex = recommendationIndex + 1;
        int count = 0;
        if (cartSectionHeaderHolderData != null) {
            cartDataList.add(++recommendationIndex, cartSectionHeaderHolderData);
            count += 1;
        }

        cartDataList.addAll(++recommendationIndex, cartRecommendationItemHolderDataList);
        count += cartRecommendationItemHolderDataList.size();
        notifyItemRangeInserted(startIndex, count);
    }

    public void removeCartEmptyData() {
        cartDataList.remove(cartEmptyHolderData);
        notifyDataSetChanged();

        cartEmptyHolderData = null;
    }

    public void checkForShipmentForm() {
        boolean canProcess = true;
        int checkedCount = 0;
        for (Object object : cartDataList) {
            if (object instanceof CartShopHolderData) {
                CartShopHolderData cartShopHolderData = (CartShopHolderData) object;
                if (!cartShopHolderData.getShopGroupData().isError()) {
                    if (cartShopHolderData.isAllSelected()) {
                        checkedCount += cartShopHolderData.getShopGroupData().getCartItemDataList().size();
                    } else if (cartShopHolderData.isPartialSelected()) {
                        if (cartShopHolderData.getShopGroupData().getCartItemDataList() != null) {
                            for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                                if (cartItemHolderData.isSelected()) {
                                    checkedCount++;
                                    if (cartItemHolderData.getErrorFormItemValidationType() != CartItemHolderData.ERROR_EMPTY ||
                                            cartItemHolderData.getCartItemData().isError()) {
                                        canProcess = false;
                                        break;
                                    }
                                }
                            }
                            if (!canProcess) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (canProcess && checkedCount > 0) {
            actionListener.onCartDataEnableToCheckout();
        } else {
            String errorMessage = actionListener.getDefaultCartErrorMessage();
            for (Object object : cartDataList) {
                if (object instanceof CartItemTickerErrorHolderData) {
                    CartTickerErrorData cartTickerErrorData = ((CartItemTickerErrorHolderData) object).getCartTickerErrorData();
                    if (!TextUtils.isEmpty(cartTickerErrorData.getErrorInfo())) {
                        errorMessage = cartTickerErrorData.getErrorInfo();
                    }
                    break;
                }
            }

            actionListener.onCartDataDisableToCheckout(errorMessage);
        }
    }

    public void updateShipmentSellerCashback(double cashback) {
        if (cashback > 0) {
            if (shipmentSellerCashbackModel == null || cartDataList.indexOf(shipmentSellerCashbackModel) == -1) {
                shipmentSellerCashbackModel = new ShipmentSellerCashbackModel();
                cartDataList.add(shipmentSellerCashbackModel);
            }
            shipmentSellerCashbackModel.setVisible(true);
            shipmentSellerCashbackModel.setSellerCashback(CurrencyFormatUtil.convertPriceValueToIdrFormat((long) cashback, false));
        } else {
            if (shipmentSellerCashbackModel != null) {
                cartDataList.remove(shipmentSellerCashbackModel);
            }
        }

        int index = cartDataList.indexOf(shipmentSellerCashbackModel);
        if (index != -1) {
            notifyItemChanged(index);
        }
    }

    public void notifyByProductId(String productId, boolean isWishlisted) {
        for (int i = 0; i < cartDataList.size(); i++) {
            if (cartDataList.get(i) instanceof CartShopHolderData) {
                CartShopHolderData cartShopHolderData = (CartShopHolderData) cartDataList.get(i);
                if (cartShopHolderData.getShopGroupData().getCartItemDataList() != null) {
                    for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                        if (cartItemHolderData.getCartItemData().getOriginData().getProductId().equals(productId)) {
                            cartItemHolderData.getCartItemData().getOriginData().setWishlisted(isWishlisted);
                            notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }
        }
    }

    public PromoData getPromoData() {
        for (int i = 0; i < cartDataList.size(); i++) {
            if (cartDataList.get(i) instanceof PromoData) {
                return (PromoData) cartDataList.get(i);
            }
        }
        return null;
    }

    public PromoStackingData getPromoStackingGlobalData() {
        for (int i = 0; i < cartDataList.size(); i++) {
            if (cartDataList.get(i) instanceof PromoStackingData) {
                return (PromoStackingData) cartDataList.get(i);
            }
        }
        return null;
    }

    public CartShopHolderData getCartShopHolderDataByIndex(int index) {
        if (cartDataList.get(index) instanceof CartShopHolderData) {
            return (CartShopHolderData) cartDataList.get(index);
        }
        return null;
    }

    public List<CartShopHolderData> getAllCartShopHolderData() {
        List<CartShopHolderData> cartShopHolderDataList = new ArrayList<>();
        for (int i = 0; i < cartDataList.size(); i++) {
            CartShopHolderData cartShopHolderData = getCartShopHolderDataByIndex(i);
            if (cartShopHolderData != null) {
                cartShopHolderDataList.add(cartShopHolderData);
            }
        }

        return cartShopHolderDataList;
    }

    public void mappingTopAdsModel(TopAdsModel adsModel) {
        cartDataList.add(adsModel);
    }

    public void notifyWishlist(String productId, boolean isWishlist) {
        for (Object object : cartDataList) {
            if (object instanceof CartWishlistHolderData) {
                List<CartWishlistItemHolderData> wishlist = ((CartWishlistHolderData) object).getWishList();
                for (CartWishlistItemHolderData data : wishlist) {
                    if (data.getId().equals(productId)) {
                        data.setWishlist(isWishlist);
                        if (cartWishlistAdapter != null) {
                            cartWishlistAdapter.notifyItemChanged(wishlist.indexOf(data));
                        }
                        break;
                    }
                }
                break;
            }
        }
    }

    public void notifyRecentView(String productId, boolean isWishlist) {
        for (Object object : cartDataList) {
            if (object instanceof CartRecentViewHolderData) {
                List<CartRecentViewItemHolderData> recentViews = ((CartRecentViewHolderData) object).getRecentViewList();
                for (CartRecentViewItemHolderData data : recentViews) {
                    if (data.getId().equals(productId)) {
                        data.setWishlist(isWishlist);
                        if (cartRecentViewAdapter != null) {
                            cartRecentViewAdapter.notifyItemChanged(recentViews.indexOf(data));
                        }
                        break;
                    }
                }
                break;
            }
        }
    }

    public void addCartLoadingData() {
        if (cartLoadingHolderData == null) {
            cartLoadingHolderData = new CartLoadingHolderData();
        }
        cartDataList.add(cartLoadingHolderData);
        notifyItemInserted(cartDataList.indexOf(cartLoadingHolderData));
    }

    public void removeCartLoadingData() {
        cartDataList.remove(cartLoadingHolderData);
    }

    public void removeCartItemById(List<Integer> cartIds) {
        // Store item first before remove item to prevent ConcurrentModificationException
        List<CartShopHolderData> toBeRemovedcartShopholderData = new ArrayList<>();
        for (Object object : cartDataList) {
            if (object instanceof CartShopHolderData) {
                List<CartItemHolderData> cartItemHolderDataList = ((CartShopHolderData) object).getShopGroupData().getCartItemDataList();
                List<CartItemHolderData> toBeRemovedCartItemHolderData = new ArrayList<>();
                for (CartItemHolderData cartItemHolderData : cartItemHolderDataList) {
                    if (cartIds.contains(cartItemHolderData.getCartItemData().getOriginData().getCartId())) {
                        toBeRemovedCartItemHolderData.add(cartItemHolderData);
                        break;
                    }
                }
                for (CartItemHolderData cartItemHolderData : toBeRemovedCartItemHolderData) {
                    cartItemHolderDataList.remove(cartItemHolderData);
                }
                if (cartItemHolderDataList.size() == 0) {
                    toBeRemovedcartShopholderData.add((CartShopHolderData) object);
                }
            }
        }
        for (CartShopHolderData cartShopHolderData : toBeRemovedcartShopholderData) {
            cartDataList.remove(cartShopHolderData);
        }
        notifyDataSetChanged();
    }

}
