package com.tokopedia.checkout.view.feature.cartlist.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.view.feature.cartlist.viewholder.CartItemViewHolder;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartItemHolderData;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Irfan Khoirul on 21/08/18.
 */

public class CartItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ActionListener actionListener;
    private List<CartItemHolderData> cartItemHolderDataList = new ArrayList<>();
    private CompositeSubscription compositeSubscription;

    public CartItemAdapter() {
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public int getItemViewType(int position) {
        return CartItemViewHolder.TYPE_VIEW_ITEM_CART;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(CartItemViewHolder.TYPE_VIEW_ITEM_CART, parent, false);
        return new CartItemViewHolder(view, compositeSubscription, actionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final CartItemViewHolder holderView = (CartItemViewHolder) holder;
        final CartItemHolderData data = cartItemHolderDataList.get(position);
        holderView.bindData(data);
    }

    @Override
    public int getItemCount() {
        return cartItemHolderDataList.size();
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

    public void deleteItem(CartItemData cartItemData, int shopPosition) {
        for (int i = 0; i < cartItemHolderDataList.size(); i++) {
            CartItemHolderData data = cartItemHolderDataList.get(i);
            if (data.getCartItemData().getOriginData().getCartId() ==
                    cartItemData.getOriginData().getCartId()) {
                cartItemHolderDataList.remove(i);
                notifyItemRemoved(i);
            }
        }

        if (cartItemHolderDataList.isEmpty()) {
            actionListener.onCartItemListIsEmpty(shopPosition);
        }
    }

    public void increaseQuantity(int position) {
        if (getItemViewType(position) == CartItemViewHolder.TYPE_VIEW_ITEM_CART) {
            ((CartItemHolderData) cartItemHolderDataList.get(position))
                    .getCartItemData().getUpdatedData().increaseQuantity();
        }
        notifyItemChanged(position);
//        checkForShipmentForm();
    }

    public void resetQuantity(int position) {
        if (getItemViewType(position) == CartItemViewHolder.TYPE_VIEW_ITEM_CART) {
            ((CartItemHolderData) cartItemHolderDataList.get(position))
                    .getCartItemData().getUpdatedData().resetQuantity();
        }
        notifyItemChanged(position);
//        checkForShipmentForm();
    }

    public void decreaseQuantity(int position) {
        if (getItemViewType(position) == CartItemViewHolder.TYPE_VIEW_ITEM_CART) {
            ((CartItemHolderData) cartItemHolderDataList.get(position))
                    .getCartItemData().getUpdatedData().decreaseQuantity();
        }
        notifyItemChanged(position);
//        checkForShipmentForm();
    }

    public interface ActionListener {

        void onCartItemDeleteButtonClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemQuantityPlusButtonClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemQuantityReseted(int position, boolean needRefreshItemView);

        void onCartItemQuantityMinusButtonClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemProductClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemRemarkEditChange(CartItemData cartItemData, int position, String remark);

        void onCartItemListIsEmpty(int shopPosition);

        void onCartItemQuantityFormEdited(int position, boolean needRefreshItemView);

        void onCartItemAfterErrorChecked();

        void onCartItemQuantityInputFormClicked(String qty);

        void onCartItemLabelInputRemarkClicked();

        void onQuantityChanged();

    }
}
