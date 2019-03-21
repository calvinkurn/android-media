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
import com.tokopedia.checkout.domain.datamodel.promostacking.VoucherOrdersItemData;
import com.tokopedia.checkout.view.common.adapter.CartAdapterActionListener;
import com.tokopedia.checkout.view.common.holderitemdata.CartItemTickerErrorHolderData;
import com.tokopedia.checkout.view.common.viewholder.CartPromoSuggestionViewHolder;
import com.tokopedia.checkout.view.common.viewholder.CartVoucherPromoViewHolder;
import com.tokopedia.checkout.view.common.viewholder.ShipmentSellerCashbackViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.viewholder.CartShopViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.viewholder.CartTickerErrorViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.viewholder.CartTopAdsViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartItemHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartShopHolderData;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentSellerCashbackModel;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.promocheckout.common.view.model.PromoData;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView;
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final CartAdapter.ActionListener cartActionListener;
    private final CartItemAdapter.ActionListener cartItemActionListener;
    private List<Object> cartDataList;
    private ShipmentSellerCashbackModel shipmentSellerCashbackModel;
    private CompositeSubscription compositeSubscription;
    private RecyclerView.RecycledViewPool viewPool;
    private Map<Integer, Boolean> checkedItemState;

    @Inject
    public CartAdapter(CartAdapter.ActionListener cartActionListener,
                       CartItemAdapter.ActionListener cartItemActionListener) {
        this.cartDataList = new ArrayList<>();
        this.cartActionListener = cartActionListener;
        this.cartItemActionListener = cartItemActionListener;
        compositeSubscription = new CompositeSubscription();
        viewPool = new RecyclerView.RecycledViewPool();
    }

    public void setCheckedItemState(Map<Integer, Boolean> checkedItemState) {
        this.checkedItemState = checkedItemState;
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
            return new CartShopViewHolder(view, cartActionListener, cartItemActionListener, compositeSubscription, viewPool, cartActionListener);
        } else if (viewType == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION, parent, false);
            return new CartPromoSuggestionViewHolder(view, cartActionListener);
        } else if (viewType == CartVoucherPromoViewHolder.TYPE_VIEW_PROMO) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartVoucherPromoViewHolder.TYPE_VIEW_PROMO, parent, false);
            return new CartVoucherPromoViewHolder(view, cartActionListener);
        } else if (viewType == CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR, parent, false);
            return new CartTickerErrorViewHolder(view, cartActionListener);
        } else if (viewType == ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK, parent, false);
            return new ShipmentSellerCashbackViewHolder(view);
        } else if (viewType == CartTopAdsViewHolder.TYPE_VIEW_CART_TOPADS) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartTopAdsViewHolder.TYPE_VIEW_CART_TOPADS, parent, false);
            return new CartTopAdsViewHolder(view, cartActionListener);
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
            cartShopHolderData.setAllSelected(!shopGroupData.isError());
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

    public void updateItemPromoVoucher(PromoData promoData) {
        for (int i = 0; i < cartDataList.size(); i++) {
            Object object = cartDataList.get(i);
            if (object instanceof PromoData) {
                cartDataList.set(i, promoData);
                notifyItemChanged(i);
            } else if (object instanceof CartPromoSuggestion) {
                ((CartPromoSuggestion) object).setVisible(false);
                notifyItemChanged(i);
            }
        }
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

    public void cancelAutoApplyCoupon() {
        for (int i = 0; i < cartDataList.size(); i++) {
            Object object = cartDataList.get(i);
            if (object instanceof PromoData) {
                ((PromoData) object).setState(TickerCheckoutView.State.EMPTY);
                notifyItemChanged(i);
            } else if (object instanceof CartPromoSuggestion) {
                ((CartPromoSuggestion) object).setVisible(true);
                notifyItemChanged(i);
            }
        }
    }

    public void cancelAutoApplyStackCoupon() {
        for (int i = 0; i < cartDataList.size(); i++) {
            Object object = cartDataList.get(i);
            if (object instanceof PromoStackingData) {
                ((PromoStackingData) object).setState(TickerPromoStackingCheckoutView.State.EMPTY);
                notifyItemChanged(i);
            }
        }
    }


    public void updateSuggestionPromo() {
        for (int i = 0; i < cartDataList.size(); i++) {
            Object object = cartDataList.get(i);
            if (object instanceof CartPromoSuggestion) {
                ((CartPromoSuggestion) object).setVisible(true);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void addPromoVoucherData(PromoData promoData) {
        cartDataList.add(promoData);
        notifyDataSetChanged();
        checkForShipmentForm();
    }

    public void addPromoStackingVoucherData(PromoStackingData promoStackingData) {
        cartDataList.add(promoStackingData);
        notifyDataSetChanged();
        checkForShipmentForm();
    }

    public void addCartTickerError(CartItemTickerErrorHolderData cartItemTickerErrorHolderData) {
        cartDataList.add(cartItemTickerErrorHolderData);
        notifyDataSetChanged();
        checkForShipmentForm();
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
            cartActionListener.onCartDataEnableToCheckout();
        } else {
            String errorMessage = cartActionListener.getDefaultCartErrorMessage();
            for (Object object : cartDataList) {
                if (object instanceof CartItemTickerErrorHolderData) {
                    CartTickerErrorData cartTickerErrorData = ((CartItemTickerErrorHolderData) object).getCartTickerErrorData();
                    if (!TextUtils.isEmpty(cartTickerErrorData.getErrorInfo())) {
                        errorMessage = cartTickerErrorData.getErrorInfo();
                    }
                    break;
                }
            }

            cartActionListener.onCartDataDisableToCheckout(errorMessage);
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

    public PromoStackingData getPromoStackingGlobaldata() {
        for (int i = 0; i < cartDataList.size(); i++) {
            if (cartDataList.get(i) instanceof PromoStackingData) {
                return (PromoStackingData) cartDataList.get(i);
            }
        }
        return null;
    }

    public void mappingTopAdsModel(TopAdsModel adsModel) {
        cartDataList.add(adsModel);
    }

    public interface ActionListener extends CartAdapterActionListener {

        String getDefaultCartErrorMessage();

        void onCartShopNameClicked(CartShopHolderData cartShopHolderData);

        void onShopItemCheckChanged(int itemPosition, boolean checked);

        void onTopAdsItemClicked(Product product);
    }
}
