package com.tokopedia.checkout.view.feature.removecartitem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.checkout.view.feature.removecartitem.viewholder.RemoveCartProductHeaderViewHolder;
import com.tokopedia.checkout.view.feature.removecartitem.viewholder.RemoveCartProductItemViewHolder;
import com.tokopedia.checkout.view.feature.removecartitem.viewmodel.CartProductHeaderViewModel;
import com.tokopedia.checkout.view.feature.removecartitem.viewmodel.CartProductItemViewModel;
import com.tokopedia.checkout.view.feature.removecartitem.viewmodel.CartRemoveProductModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Irfan Khoirul on 24/05/18.
 */
@Deprecated
public class RemoveCartItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CartRemoveProductModel> cartRemoveProductModels = new ArrayList<>();

    private CartProductHeaderViewModel cartProductHeaderViewModel;
    private List<CartProductItemViewModel> cartProductItemViewModels = new ArrayList<>();
    private List<String> checkedCartIds = new ArrayList<>();

    private RemoveCartItemViewListener removeCartItemViewListener;

    @Inject
    public RemoveCartItemAdapter() {
    }

    @Override
    public int getItemViewType(int position) {
        CartRemoveProductModel item = cartRemoveProductModels.get(position);

        if (item instanceof CartProductHeaderViewModel) {
            return RemoveCartProductHeaderViewHolder.ITEM_VIEW_REMOVE_ALL_CHECKBOX;
        } else if (item instanceof CartProductItemViewModel) {
            return RemoveCartProductItemViewHolder.ITEM_CART_REMOVE_PRODUCT;
        }

        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        if (viewType == RemoveCartProductHeaderViewHolder.ITEM_VIEW_REMOVE_ALL_CHECKBOX) {
            return new RemoveCartProductHeaderViewHolder(view, this);
        } else if (viewType == RemoveCartProductItemViewHolder.ITEM_CART_REMOVE_PRODUCT) {
            return new RemoveCartProductItemViewHolder(view, this);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        CartRemoveProductModel data = cartRemoveProductModels.get(position);

        if (viewType == RemoveCartProductHeaderViewHolder.ITEM_VIEW_REMOVE_ALL_CHECKBOX) {
            ((RemoveCartProductHeaderViewHolder) holder).bindData((CartProductHeaderViewModel) data);
        } else if (viewType == RemoveCartProductItemViewHolder.ITEM_CART_REMOVE_PRODUCT) {
            ((RemoveCartProductItemViewHolder) holder).bindData((CartProductItemViewModel) data);
        }
    }

    @Override
    public int getItemCount() {
        return cartRemoveProductModels.size();
    }

    public void addCartProductHeaderViewModel(CartProductHeaderViewModel cartProductHeaderViewModel) {
        this.cartProductHeaderViewModel = cartProductHeaderViewModel;
        cartRemoveProductModels.add(cartProductHeaderViewModel);
        notifyDataSetChanged();
    }

    public void addCartProductItemViewModel(List<CartProductItemViewModel> cartProductItemViewModels) {
        this.cartProductItemViewModels = cartProductItemViewModels;
        cartRemoveProductModels.addAll(cartProductItemViewModels);
        notifyDataSetChanged();
    }

    public int getCheckedItemCount() {
        return checkedCartIds.size();
    }

    public void setRemoveCartItemViewListener(RemoveCartItemViewListener removeCartItemViewListener) {
        this.removeCartItemViewListener = removeCartItemViewListener;
    }

    public RemoveCartItemViewListener getRemoveCartItemViewListener() {
        return removeCartItemViewListener;
    }

    public void updateCheckedCartId(int itemPosition, String cartId, boolean checked) {
        if (checked) {
            if (!checkedCartIds.contains(cartId)) {
                checkedCartIds.add(cartId);
            }
        } else {
            if (checkedCartIds.contains(cartId)) {
                checkedCartIds.remove(cartId);
            }
        }
        removeCartItemViewListener.onSingleItemCheckChanged(checked, itemPosition);
    }

    public List<String> getCheckedCartIds() {
        return checkedCartIds;
    }

    public void updateHeader(boolean checked) {
        cartProductHeaderViewModel.setChecked(checked);
    }

    public void updateAllItem(boolean checked) {
        checkedCartIds.clear();
        for (CartProductItemViewModel cartProductItemViewModel : cartProductItemViewModels) {
            String cartId = String.valueOf(cartProductItemViewModel.getCartItemData().getOriginData().getCartId());
            if (checked) {
                if (!checkedCartIds.contains(cartId)) {
                    checkedCartIds.add(cartId);
                }
            } else {
                if (checkedCartIds.contains(cartId)) {
                    checkedCartIds.remove(cartId);
                }
            }
        }
    }
}
