package com.tokopedia.checkout.view.feature.cartlist.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartlist.ShopGroupData;
import com.tokopedia.checkout.view.common.adapter.CartAdapterActionListener;
import com.tokopedia.checkout.view.feature.cartlist.viewholder.CartShopViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartItemHolderData;
import com.tokopedia.checkout.view.common.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.checkout.view.common.holderitemdata.CartItemTickerErrorHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartShopHolderData;
import com.tokopedia.checkout.view.feature.shipment.viewholder.ShipmentSellerCashbackViewHolder;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentSellerCashbackModel;
import com.tokopedia.checkout.view.common.viewholder.CartPromoSuggestionViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.viewholder.CartTickerErrorViewHolder;
import com.tokopedia.checkout.view.common.viewholder.CartVoucherPromoViewHolder;
import com.tokopedia.design.utils.CurrencyFormatUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final CartAdapter.ActionListener cartActionListener;
    private final CartItemAdapter.ActionListener cartItemActionListener;
    private List<Object> cartDataList;
    private ShipmentSellerCashbackModel shipmentSellerCashbackModel;

    @Inject
    public CartAdapter(CartAdapter.ActionListener cartActionListener,
                       CartItemAdapter.ActionListener cartItemActionListener) {
        this.cartDataList = new ArrayList<>();
        this.cartActionListener = cartActionListener;
        this.cartItemActionListener = cartItemActionListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (cartDataList.get(position) instanceof CartShopHolderData) {
            return CartShopViewHolder.TYPE_VIEW_ITEM_SHOP;
        } else if (cartDataList.get(position) instanceof CartPromoSuggestion) {
            return CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION;
        } else if (cartDataList.get(position) instanceof CartItemPromoHolderData) {
            return CartVoucherPromoViewHolder.TYPE_VIEW_PROMO;
        } else if (cartDataList.get(position) instanceof CartItemTickerErrorHolderData) {
            return CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR;
        } else if (cartDataList.get(position) instanceof ShipmentSellerCashbackModel) {
            return ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartShopViewHolder.TYPE_VIEW_ITEM_SHOP, parent, false);
            return new CartShopViewHolder(view, cartActionListener, cartItemActionListener);
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
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            final CartShopViewHolder holderView = (CartShopViewHolder) holder;
            final CartShopHolderData data = (CartShopHolderData) cartDataList.get(position);
            holderView.bindData(data);
        } else if (getItemViewType(position) == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION) {
            final CartPromoSuggestionViewHolder holderView = (CartPromoSuggestionViewHolder) holder;
            final CartPromoSuggestion data = (CartPromoSuggestion) cartDataList.get(position);
            holderView.bindData(data, position);
        } else if (getItemViewType(position) == CartVoucherPromoViewHolder.TYPE_VIEW_PROMO) {
            final CartVoucherPromoViewHolder holderView = (CartVoucherPromoViewHolder) holder;
            final CartItemPromoHolderData data = (CartItemPromoHolderData) cartDataList.get(position);
            holderView.bindData(data, position);
        } else if (getItemViewType(position) == CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR) {
            final CartTickerErrorViewHolder holderView = (CartTickerErrorViewHolder) holder;
            final CartItemTickerErrorHolderData data = (CartItemTickerErrorHolderData) cartDataList.get(position);
            holderView.bindData(data, position);
        } else if (getItemViewType(position) == ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK) {
            final ShipmentSellerCashbackViewHolder holderView = (ShipmentSellerCashbackViewHolder) holder;
            final ShipmentSellerCashbackModel data = (ShipmentSellerCashbackModel) cartDataList.get(position);
            holderView.bindViewHolder(data);
        }
    }

    @Override
    public int getItemCount() {
        return cartDataList.size();
    }

    public void addDataList(List<ShopGroupData> shopGroupDataList) {
        for (ShopGroupData shopGroupData : shopGroupDataList) {
            CartShopHolderData cartShopHolderData = new CartShopHolderData();
            cartShopHolderData.setAllSelected(true);
            cartShopHolderData.setShopGroupData(shopGroupData);
            cartDataList.add(cartShopHolderData);
        }
        notifyDataSetChanged();
    }

    public List<CartShopHolderData> getDataList() {
        List<CartShopHolderData> cartShopHolderDataFinalList = new ArrayList<>();
        for (int i = 0; i < cartDataList.size(); i++) {
            Object object = cartDataList.get(i);
            if (object instanceof CartShopHolderData) {
                cartShopHolderDataFinalList.add((CartShopHolderData) object);
            }
        }
        return cartShopHolderDataFinalList;
    }

    public List<CartItemData> getCartItemDataList() {
        List<CartItemData> cartItemDataList = new ArrayList<>();
        for (Object object : cartDataList) {
            if (object instanceof CartItemHolderData) {
                CartItemHolderData cartItemHolderData = (CartItemHolderData) object;
                cartItemDataList.add(cartItemHolderData.getCartItemData());
            }
        }

        return cartItemDataList;
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
        }

        return needToUpdateParent;
    }

    public void increaseQuantity(int position, int parentPosition) {
        if (getItemViewType(parentPosition) == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            ((CartShopHolderData) cartDataList.get(parentPosition)).getShopGroupData()
                    .getCartItemDataList().get(position).getCartItemData().getUpdatedData().increaseQuantity();
        }
        // Todo : validate data
//        checkForShipmentForm();
    }

    public void decreaseQuantity(int position, int parentPosition) {
        if (getItemViewType(parentPosition) == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            ((CartShopHolderData) cartDataList.get(parentPosition)).getShopGroupData()
                    .getCartItemDataList().get(position).getCartItemData().getUpdatedData().decreaseQuantity();
        }
//        checkForShipmentForm();
    }

    public void resetQuantity(int position, int parentPosition) {
        if (getItemViewType(parentPosition) == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            ((CartShopHolderData) cartDataList.get(parentPosition)).getShopGroupData()
                    .getCartItemDataList().get(position).getCartItemData().getUpdatedData().resetQuantity();
        }
//        checkForShipmentForm();
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

    public void updateItemPromoVoucher(CartItemPromoHolderData cartItemPromoHolderData) {
        for (int i = 0; i < cartDataList.size(); i++) {
            Object object = cartDataList.get(i);
            if (object instanceof CartItemPromoHolderData) {
                cartDataList.set(i, cartItemPromoHolderData);
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
            if (object instanceof CartItemPromoHolderData) {
                ((CartItemPromoHolderData) object).setPromoNotActive();
                notifyItemChanged(i);
            } else if (object instanceof CartPromoSuggestion) {
                ((CartPromoSuggestion) object).setVisible(true);
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

    public void addPromoVoucherData(CartItemPromoHolderData cartItemPromoHolderData) {
        cartDataList.add(cartItemPromoHolderData);
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
        for (Object object : cartDataList) {
            if (object instanceof CartItemHolderData) {
                if (((CartItemHolderData) object).getErrorFormItemValidationType() != CartItemHolderData.ERROR_EMPTY
                        || ((CartItemHolderData) object).getCartItemData().isError()) {
                    canProcess = false;
                }
            }
        }
        if (canProcess) {
            cartActionListener.onCartDataEnableToCheckout();
        } else {
            cartActionListener.onCartDataDisableToCheckout();
        }
    }

    public void updateShipmentSellerCashback(double cashback) {
        if (cashback > 0) {
            if (shipmentSellerCashbackModel == null || cartDataList.indexOf(shipmentSellerCashbackModel) == -1) {
                shipmentSellerCashbackModel = new ShipmentSellerCashbackModel();
                cartDataList.add(shipmentSellerCashbackModel);
            }
            shipmentSellerCashbackModel.setVisible(true);
            shipmentSellerCashbackModel.setSellerCashback(CurrencyFormatUtil.convertPriceValueToIdrFormat((long) cashback, true));
        }

        int index = cartDataList.indexOf(shipmentSellerCashbackModel);
        if (index != -1) {
            notifyItemChanged(index);
        }
    }

    public interface ActionListener extends CartAdapterActionListener {

        void onCartShopNameClicked(CartShopHolderData cartShopHolderData);

        void onShopItemCheckChanged(int itemPosition, boolean checked);

    }
}
