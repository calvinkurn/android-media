package com.tokopedia.checkout.view.feature.cartlist.viewholder;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartAdapter;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartItemAdapter;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartItemHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartShopHolderData;

import java.util.Map;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Irfan Khoirul on 21/08/18.
 */

public class CartShopViewHolder extends RecyclerView.ViewHolder {

    public static final int TYPE_VIEW_ITEM_SHOP = R.layout.item_shop;

    private LinearLayout llWarningAndError;
    private FrameLayout flShopItemContainer;
    private LinearLayout llShopContainer;
    private CheckBox cbSelectShop;
    private TextView tvShopName;
    private ImageView imgShopBadge;
    private RecyclerView rvCartItem;
    private LinearLayout layoutError;
    private TextView tvErrorTitle;
    private TextView tvErrorDescription;
    private LinearLayout layoutWarning;
    private TextView tvWarningTitle;
    private TextView tvWarningDescription;
    private ImageView promoMerchant;

    private CartAdapter.ActionListener cartAdapterListener;
    private CartItemAdapter.ActionListener cartItemAdapterListener;
    private CartItemAdapter cartItemAdapter;
    private CompositeSubscription compositeSubscription;
    private RecyclerView.RecycledViewPool viewPool;

    public CartShopViewHolder(View itemView, CartAdapter.ActionListener cartAdapterListener,
                              CartItemAdapter.ActionListener cartItemAdapterListener,
                              CompositeSubscription compositeSubscription,
                              RecyclerView.RecycledViewPool viewPool) {
        super(itemView);
        this.cartAdapterListener = cartAdapterListener;
        this.cartItemAdapterListener = cartItemAdapterListener;
        this.compositeSubscription = compositeSubscription;
        this.viewPool = viewPool;

        llWarningAndError = itemView.findViewById(R.id.ll_warning_and_error);
        flShopItemContainer = itemView.findViewById(R.id.fl_shop_item_container);
        llShopContainer = itemView.findViewById(R.id.ll_shop_container);
        cbSelectShop = itemView.findViewById(R.id.cb_select_shop);
        tvShopName = itemView.findViewById(R.id.tv_shop_name);
        imgShopBadge = itemView.findViewById(R.id.img_shop_badge);
        rvCartItem = itemView.findViewById(R.id.rv_cart_item);
        layoutError = itemView.findViewById(R.id.layout_error);
        tvErrorTitle = itemView.findViewById(R.id.tv_error_title);
        tvErrorDescription = itemView.findViewById(R.id.tv_error_description);
        layoutWarning = itemView.findViewById(R.id.layout_warning);
        tvWarningTitle = itemView.findViewById(R.id.tv_warning_title);
        tvWarningDescription = itemView.findViewById(R.id.tv_warning_description);
        promoMerchant = itemView.findViewById(R.id.promo_merchant);
    }

    public void bindData(CartShopHolderData cartShopHolderData, Map<Integer, Boolean> checkedItemState) {
        if (cartShopHolderData.getShopGroupData().isError() || cartShopHolderData.getShopGroupData().isWarning()) {
            llWarningAndError.setVisibility(View.VISIBLE);
        } else {
            llWarningAndError.setVisibility(View.GONE);
        }
        renderErrorItemHeader(cartShopHolderData);
        renderWarningItemHeader(cartShopHolderData);

        tvShopName.setText(cartShopHolderData.getShopGroupData().getShopName());
        tvShopName.setOnClickListener(v -> cartAdapterListener.onCartShopNameClicked(cartShopHolderData));

        if (cartShopHolderData.getShopGroupData().isOfficialStore() || cartShopHolderData.getShopGroupData().isGoldMerchant()) {
            if (!cartShopHolderData.getShopGroupData().getShopBadge().isEmpty()) {
                ImageHandler.loadImageWithoutPlaceholder(imgShopBadge, cartShopHolderData.getShopGroupData().getShopBadge());
                imgShopBadge.setVisibility(View.VISIBLE);
            }
        } else {
            imgShopBadge.setVisibility(View.GONE);
        }

        cartItemAdapter = new CartItemAdapter(cartItemAdapterListener, compositeSubscription, getAdapterPosition());
        cartItemAdapter.addDataList(cartShopHolderData.getShopGroupData().getCartItemDataList());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rvCartItem.getContext());
        rvCartItem.setLayoutManager(linearLayoutManager);
//        rvCartItem.setRecycledViewPool(viewPool);
//        linearLayoutManager.setInitialPrefetchItemCount(1);
        rvCartItem.setAdapter(cartItemAdapter);
        ((SimpleItemAnimator) rvCartItem.getItemAnimator()).setSupportsChangeAnimations(false);

        cbSelectShop.setEnabled(!cartShopHolderData.getShopGroupData().isError());
        cbSelectShop.setChecked(cartShopHolderData.isAllSelected());
        cbSelectShop.setOnClickListener(cbSelectShopClickListener(cartShopHolderData));

        promoMerchant.setOnClickListener(promoShopClickListener());
    }

    private void renderErrorItemHeader(CartShopHolderData data) {
        if (data.getShopGroupData().isError()) {
            cbSelectShop.setEnabled(false);
            flShopItemContainer.setForeground(ContextCompat.getDrawable(flShopItemContainer.getContext(), R.drawable.fg_disabled_item));
            llShopContainer.setBackgroundResource(R.drawable.bg_error_shop);
            tvErrorTitle.setText(data.getShopGroupData().getErrorTitle());
            String errorDescription = data.getShopGroupData().getErrorDescription();
            if (!TextUtils.isEmpty(errorDescription)) {
                tvErrorDescription.setText(errorDescription);
                tvErrorDescription.setVisibility(View.VISIBLE);
            } else {
                tvErrorDescription.setVisibility(View.GONE);
            }
            layoutError.setVisibility(View.VISIBLE);
        } else {
            cbSelectShop.setEnabled(true);
            flShopItemContainer.setForeground(ContextCompat.getDrawable(flShopItemContainer.getContext(), R.drawable.fg_enabled_item));
            llShopContainer.setBackgroundResource(0);
            layoutError.setVisibility(View.GONE);
        }
    }

    private void renderWarningItemHeader(CartShopHolderData data) {
        if (data.getShopGroupData().isWarning()) {
            tvWarningTitle.setText(data.getShopGroupData().getWarningTitle());
            String warningDescription = data.getShopGroupData().getWarningDescription();
            if (!TextUtils.isEmpty(warningDescription)) {
                tvWarningDescription.setText(warningDescription);
                tvWarningDescription.setVisibility(View.VISIBLE);
            } else {
                tvWarningDescription.setVisibility(View.GONE);
            }
            layoutWarning.setVisibility(View.VISIBLE);
        } else {
            layoutWarning.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener cbSelectShopClickListener(CartShopHolderData cartShopHolderData) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cartShopHolderData.getShopGroupData().isError()) {
                    boolean isChecked;
                    if (cartShopHolderData.isPartialSelected()) {
                        isChecked = false;
                        cartShopHolderData.setAllSelected(false);
                        cartShopHolderData.setPartialSelected(false);
                    } else {
                        isChecked = !cartShopHolderData.isAllSelected();
                    }
                    cbSelectShop.setChecked(isChecked);
                    boolean isAllSelected = true;
                    for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                        if (cartItemHolderData.getCartItemData().isError() && cartItemHolderData.getCartItemData().isSingleChild()) {
                            isAllSelected = false;
                        }
                    }
                    cartShopHolderData.setAllSelected(isAllSelected);
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        cartAdapterListener.onShopItemCheckChanged(getAdapterPosition(), isChecked);
                    }
                }
            }
        };
    }

    private View.OnClickListener promoShopClickListener() {
        return v -> cartAdapterListener.onPromoMerchantClicked();
    }

}
