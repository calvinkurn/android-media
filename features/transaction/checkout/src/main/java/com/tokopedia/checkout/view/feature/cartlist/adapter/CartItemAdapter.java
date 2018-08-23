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

public class CartItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements CartItemViewHolder.ViewHolderListener {

    private ActionListener actionListener;
    private List<CartItemHolderData> cartItemHolderDataList = new ArrayList<>();
    private CompositeSubscription compositeSubscription;
    private int parentPosition;

    public CartItemAdapter(ActionListener actionListener, int parentPosition) {
        this.actionListener = actionListener;
        this.parentPosition = parentPosition;
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
        holderView.bindData(data, parentPosition, this);
    }

    @Override
    public int getItemCount() {
        return cartItemHolderDataList.size();
    }

    public void unsubscribeSubscription() {
        compositeSubscription.unsubscribe();
    }

    public void addDataList(List<CartItemHolderData> cartItemHolderDataList, boolean selected) {
        for (CartItemHolderData cartItemHolderData : cartItemHolderDataList) {
            cartItemHolderData.setSelected(selected);
            this.cartItemHolderDataList.add(cartItemHolderData);
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

    @Override
    public void onNeedToRefresh(int childPosition) {
        notifyItemChanged(childPosition);
    }

    public interface ActionListener {

        void onCartItemDeleteButtonClicked(CartItemHolderData cartItemHolderData, int position, int parentPosition);

        void onCartItemQuantityPlusButtonClicked(CartItemHolderData cartItemHolderData, int position, int parentPosition);

        void onCartItemQuantityMinusButtonClicked(CartItemHolderData cartItemHolderData, int position, int parentPosition);

        void onCartItemQuantityReseted(int position, int parentPosition, boolean needRefreshItemView);

        void onCartItemProductClicked(CartItemHolderData cartItemHolderData, int position, int parentPosition);

        void onCartItemRemarkEditChange(CartItemData cartItemData, String remark, int position, int parentPosition);

        void onCartItemListIsEmpty(int parentPosition);

        void onCartItemQuantityFormEdited(int position, int parentPosition, boolean needRefreshItemView);

        void onCartItemAfterErrorChecked();

        void onCartItemQuantityInputFormClicked(String qty);

        void onCartItemLabelInputRemarkClicked();

        void onQuantityChanged();

        void onCartItemCheckChanged(int position, int parentPosition, boolean checked);

    }
}
