package com.tokopedia.purchase_platform.features.cart.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartItemViewHolder;
import com.tokopedia.purchase_platform.features.cart.view.uimodel.CartItemHolderData;

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
    private int parentPosition;
    private CompositeSubscription compositeSubscription;

    public CartItemAdapter(ActionListener actionListener, CompositeSubscription compositeSubscription, int parentPosition) {
        this.actionListener = actionListener;
        this.parentPosition = parentPosition;
        this.compositeSubscription = compositeSubscription;
    }

    @Override
    public int getItemViewType(int position) {
        return CartItemViewHolder.Companion.getTYPE_VIEW_ITEM_CART();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(CartItemViewHolder.Companion.getTYPE_VIEW_ITEM_CART(), parent, false);
        return new CartItemViewHolder(view, compositeSubscription, actionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final CartItemViewHolder holderView = (CartItemViewHolder) holder;
        final CartItemHolderData data = cartItemHolderDataList.get(position);
        holderView.bindData(data, parentPosition, this, cartItemHolderDataList.size());
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        ((CartItemViewHolder) holder).clear();
    }

    @Override
    public int getItemCount() {
        return cartItemHolderDataList.size();
    }

    public void addDataList(List<CartItemHolderData> cartItemHolderDataList) {
        this.cartItemHolderDataList.clear();
        this.cartItemHolderDataList.addAll(cartItemHolderDataList);
        notifyDataSetChanged();
    }

    @Override
    public void onNeedToRefreshSingleProduct(int childPosition) {
        notifyItemChanged(childPosition);
        actionListener.onNeedToRecalculate();
    }

    @Override
    public void onNeedToRefreshSingleShop(int parentPosition) {
        actionListener.onNeedToRefreshSingleShop(parentPosition);
        actionListener.onNeedToRecalculate();
    }

    @Override
    public void onNeedToRefreshAllShop() {
        actionListener.onNeedToRefreshMultipleShop();
        actionListener.onNeedToRecalculate();
    }

    public interface ActionListener {

        void onCartItemDeleteButtonClicked(CartItemHolderData cartItemHolderData, int position, int parentPosition);

        void onCartItemQuantityPlusButtonClicked(CartItemHolderData cartItemHolderData, int position, int parentPosition);

        void onCartItemQuantityMinusButtonClicked(CartItemHolderData cartItemHolderData, int position, int parentPosition);

        void onCartItemQuantityReseted(int position, int parentPosition, boolean needRefreshItemView);

        void onCartItemProductClicked(CartItemHolderData cartItemHolderData, int position, int parentPosition);

        void onCartItemAfterErrorChecked();

        void onCartItemQuantityInputFormClicked(String qty);

        void onCartItemLabelInputRemarkClicked();

        boolean onCartItemCheckChanged(int position, int parentPosition, boolean checked);

        void onWishlistCheckChanged(String productId, boolean isChecked);

        void onNeedToRefreshSingleShop(int parentPosition);

        void onNeedToRefreshMultipleShop();

        void onNeedToRecalculate();

        void onCartItemShowTickerPriceDecrease(String productId);

        void onCartItemShowTickerStockDecreaseAndAlreadyAtcByOtherUser(String productId);

        void onCartItemShowTickerOutOfStock(String productId);

        void onCartItemSimilarProductUrlClicked(String similarProductUrl);

        CompositeSubscription onGetCompositeSubscriber();

        void onCartItemQuantityChangedThenHitUpdateCartAndValidateUse();
    }
}
