package com.tokopedia.checkout.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.view.holderitemdata.CartItemHolderData;
import com.tokopedia.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.checkout.view.holderitemdata.CartItemTickerErrorHolderData;
import com.tokopedia.checkout.view.viewholder.CartListItemViewHolder;
import com.tokopedia.checkout.view.viewholder.CartPromoSuggestionViewHolder;
import com.tokopedia.checkout.view.viewholder.CartTickerErrorViewHolder;
import com.tokopedia.checkout.view.viewholder.CartVoucherPromoViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ActionListener actionListener;
    private List<Object> cartItemHolderDataList;
    private CompositeSubscription compositeSubscription;

    @Inject
    public CartListAdapter(ActionListener actionListener) {
        this.cartItemHolderDataList = new ArrayList<>();
        this.actionListener = actionListener;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == CartListItemViewHolder.TYPE_VIEW_ITEM_CART) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartListItemViewHolder.TYPE_VIEW_ITEM_CART, parent, false);
            return new CartListItemViewHolder(view, compositeSubscription, actionListener);
        } else if (viewType == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION, parent, false);
            return new CartPromoSuggestionViewHolder(view, actionListener);
        } else if (viewType == CartVoucherPromoViewHolder.TYPE_VIEW_PROMO) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartVoucherPromoViewHolder.TYPE_VIEW_PROMO, parent, false);
            return new CartVoucherPromoViewHolder(view, actionListener);
        } else if (viewType == CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR, parent, false);
            return new CartTickerErrorViewHolder(view, actionListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == CartListItemViewHolder.TYPE_VIEW_ITEM_CART) {
            final CartListItemViewHolder holderView = (CartListItemViewHolder) holder;
            final CartItemHolderData data = (CartItemHolderData) cartItemHolderDataList.get(position);
            holderView.bindData(data);
        } else if (getItemViewType(position) == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION) {
            final CartPromoSuggestionViewHolder holderView = (CartPromoSuggestionViewHolder) holder;
            final CartPromoSuggestion data = (CartPromoSuggestion) cartItemHolderDataList.get(position);
            holderView.bindData(data, position);
        } else if (getItemViewType(position) == CartVoucherPromoViewHolder.TYPE_VIEW_PROMO) {
            final CartVoucherPromoViewHolder holderView = (CartVoucherPromoViewHolder) holder;
            final CartItemPromoHolderData data = (CartItemPromoHolderData) cartItemHolderDataList.get(position);
            holderView.bindData(data, position);
        } else if (getItemViewType(position) == CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR) {
            final CartTickerErrorViewHolder holderView = (CartTickerErrorViewHolder) holder;
            final CartItemTickerErrorHolderData data = (CartItemTickerErrorHolderData) cartItemHolderDataList.get(position);
            holderView.bindData(data, position);
        }
    }


    @Override
    public int getItemCount() {
        return cartItemHolderDataList.size();
    }

    public CompositeSubscription getCompositeSubscription() {
        return compositeSubscription;
    }

    public void unsubscribeSubscription() {
        compositeSubscription.unsubscribe();
    }

    public void addDataList(List<CartItemData> cartItemDataList) {
        for (CartItemData cartItemData : cartItemDataList) {
            CartItemHolderData cartItemHolderData = new CartItemHolderData();
            cartItemHolderData.setCartItemData(cartItemData);
            cartItemHolderData.setEditableRemark(false);
            cartItemHolderData.setErrorFormItemValidationMessage("");
            cartItemHolderData.setEditableRemark(false);
            cartItemHolderDataList.add(cartItemHolderData);
        }
        notifyDataSetChanged();
    }

    public List<CartItemHolderData> getDataList() {
        List<CartItemHolderData> cartItemHolderDataFinalList = new ArrayList<>();
        for (int i = 0; i < cartItemHolderDataList.size(); i++) {
            Object object = cartItemHolderDataList.get(i);
            if (object instanceof CartItemHolderData) {
                cartItemHolderDataFinalList.add((CartItemHolderData) object);
            }
        }
        return cartItemHolderDataFinalList;
    }

    public List<CartItemData> getCartItemDataList() {
        List<CartItemData> cartItemDataList = new ArrayList<>();
        for (Object object : cartItemHolderDataList) {
            if (object instanceof CartItemHolderData) {
                CartItemHolderData cartItemHolderData = (CartItemHolderData) object;
                cartItemDataList.add(cartItemHolderData.getCartItemData());
            }
        }

        return cartItemDataList;
    }

    public void increaseQuantity(int position) {
        if (getItemViewType(position) == CartListItemViewHolder.TYPE_VIEW_ITEM_CART) {
            ((CartItemHolderData) cartItemHolderDataList.get(position))
                    .getCartItemData().getUpdatedData().increaseQuantity();
        }
        notifyItemChanged(position);
        checkForShipmentForm();
    }

    public void resetQuantity(int position) {
        if (getItemViewType(position) == CartListItemViewHolder.TYPE_VIEW_ITEM_CART) {
            ((CartItemHolderData) cartItemHolderDataList.get(position))
                    .getCartItemData().getUpdatedData().resetQuantity();
        }
        notifyItemChanged(position);
        checkForShipmentForm();
    }

    public void decreaseQuantity(int position) {
        if (getItemViewType(position) == CartListItemViewHolder.TYPE_VIEW_ITEM_CART) {
            ((CartItemHolderData) cartItemHolderDataList.get(position))
                    .getCartItemData().getUpdatedData().decreaseQuantity();
        }
        notifyItemChanged(position);
        checkForShipmentForm();
    }

    public void notifyItems(int position) {
        Object itemData = cartItemHolderDataList.get(position);
        String itemDataParentId = ((CartItemHolderData) itemData).getCartItemData().getOriginData().getParentId();
        notifyItemChanged(position);
        for (Object object : cartItemHolderDataList) {
            if (object instanceof CartItemHolderData) {
                String parentId = ((CartItemHolderData) object).getCartItemData().getOriginData().getParentId();
                if (parentId.equals(itemDataParentId)) {
                    notifyItemChanged(cartItemHolderDataList.indexOf(object));
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (cartItemHolderDataList.get(position) instanceof CartItemHolderData) {
            return CartListItemViewHolder.TYPE_VIEW_ITEM_CART;
        } else if (cartItemHolderDataList.get(position) instanceof CartPromoSuggestion) {
            return CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION;
        } else if (cartItemHolderDataList.get(position) instanceof CartItemPromoHolderData) {
            return CartVoucherPromoViewHolder.TYPE_VIEW_PROMO;
        } else if (cartItemHolderDataList.get(position) instanceof CartItemTickerErrorHolderData) {
            return CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR;
        } else {
            return super.getItemViewType(position);
        }
    }

    public void addPromoSuggestion(CartPromoSuggestion cartPromoSuggestion) {
        cartItemHolderDataList.add(cartPromoSuggestion);
        notifyDataSetChanged();
        checkForShipmentForm();
    }

    public void resetData() {
        cartItemHolderDataList.clear();
        notifyDataSetChanged();
        checkForShipmentForm();
    }

    public void deleteItem(CartItemData cartItemData) {
        for (int i = 0; i < cartItemHolderDataList.size(); i++) {
            Object data = cartItemHolderDataList.get(i);
            if (data instanceof CartItemHolderData) {
                if (((CartItemHolderData) data).getCartItemData().getOriginData().getCartId()
                        == cartItemData.getOriginData().getCartId()) {
                    cartItemHolderDataList.remove(i);
                    notifyItemRemoved(i);
                }
            }
        }

        if (getDataList().isEmpty()) actionListener.onCartItemListIsEmpty();
        checkForShipmentForm();
    }

    public void updateItemPromoVoucher(CartItemPromoHolderData cartItemPromoHolderData) {
        for (int i = 0; i < cartItemHolderDataList.size(); i++) {
            Object object = cartItemHolderDataList.get(i);
            if (object instanceof CartItemPromoHolderData) {
                cartItemHolderDataList.set(i, cartItemPromoHolderData);
                notifyItemChanged(i);
            } else if (object instanceof CartPromoSuggestion) {
                ((CartPromoSuggestion) object).setVisible(false);
                notifyItemChanged(i);
            }
        }
    }

    public void cancelAutoApplyCoupon() {
        for (int i = 0; i < cartItemHolderDataList.size(); i++) {
            Object object = cartItemHolderDataList.get(i);
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
        for (int i = 0; i < cartItemHolderDataList.size(); i++) {
            Object object = cartItemHolderDataList.get(i);
            if (object instanceof CartPromoSuggestion) {
                ((CartPromoSuggestion) object).setVisible(true);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void addPromoVoucherData(CartItemPromoHolderData cartItemPromoHolderData) {
        cartItemHolderDataList.add(cartItemPromoHolderData);
        notifyDataSetChanged();
        checkForShipmentForm();
    }

    public void addCartTickerError(CartItemTickerErrorHolderData cartItemTickerErrorHolderData) {
        cartItemHolderDataList.add(cartItemTickerErrorHolderData);
        notifyDataSetChanged();
        checkForShipmentForm();
    }

    public void checkForShipmentForm() {
        boolean canProcess = true;
        for (Object object : cartItemHolderDataList) {
            if (object instanceof CartItemHolderData) {
                if (((CartItemHolderData) object).getErrorFormItemValidationType() != CartItemHolderData.ERROR_EMPTY
                        || ((CartItemHolderData) object).getCartItemData().isError()) {
                    canProcess = false;
                }
            }
        }
        if (canProcess) {
            actionListener.onCartDataEnableToCheckout();
        } else {
            actionListener.onCartDataDisableToCheckout();
        }
    }

    public interface ActionListener extends CartAdapterActionListener {

        void onCartItemDeleteButtonClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemQuantityPlusButtonClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemQuantityReseted(int position, boolean needRefreshItemView);

        void onCartItemQuantityMinusButtonClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemProductClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemShopNameClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemRemarkEditChange(CartItemData cartItemData, int position, String remark);

        void onCartItemListIsEmpty();

        void onCartItemQuantityFormEdited(int position, boolean needRefreshItemView);

        void onCartItemAfterErrorChecked();

        void onCartItemQuantityInputFormClicked(String qty);

        void onCartItemLabelInputRemarkClicked();
    }
}
