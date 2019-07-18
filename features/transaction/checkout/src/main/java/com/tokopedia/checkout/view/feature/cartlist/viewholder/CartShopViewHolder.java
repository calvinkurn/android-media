package com.tokopedia.checkout.view.feature.cartlist.viewholder;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.promostacking.VoucherOrdersItemData;
import com.tokopedia.checkout.view.feature.cartlist.ActionListener;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartItemAdapter;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartItemHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartShopHolderData;
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView;
import com.tokopedia.unifyprinciples.Typography;

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
    private Typography tvShopName;
    private ImageView imgShopBadge;
    private ImageView imgFulfillment;
    private Typography tvFulfillDistrict;
    private RecyclerView rvCartItem;
    private LinearLayout layoutError;
    private TextView tvErrorTitle;
    private TextView tvErrorDescription;
    private LinearLayout layoutWarning;
    private TextView tvWarningTitle;
    private TextView tvWarningDescription;

    private ActionListener actionListener;
    private CartItemAdapter.ActionListener cartItemAdapterListener;
    private CartItemAdapter cartItemAdapter;
    private CompositeSubscription compositeSubscription;
    private TickerPromoStackingCheckoutView tickerPromoStackingCheckoutView;

    public CartShopViewHolder(View itemView, ActionListener actionListener,
                              CartItemAdapter.ActionListener cartItemAdapterListener,
                              CompositeSubscription compositeSubscription) {
        super(itemView);
        this.actionListener = actionListener;
        this.cartItemAdapterListener = cartItemAdapterListener;
        this.compositeSubscription = compositeSubscription;

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
        imgFulfillment = itemView.findViewById(R.id.img_shop_fulfill);
        tvFulfillDistrict = itemView.findViewById(R.id.tv_fulfill_district);
        tickerPromoStackingCheckoutView = itemView.findViewById(R.id.voucher_merchant_holder_view);
    }

    public void bindData(CartShopHolderData cartShopHolderData, final int position) {
        cbSelectShop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                CartShopViewHolder.this.renderPromoMerchant(cartShopHolderData, checked);
            }
        });

        if (cartShopHolderData.getShopGroupData().isError() || cartShopHolderData.getShopGroupData().isWarning()) {
            llWarningAndError.setVisibility(View.VISIBLE);
        } else {
            llWarningAndError.setVisibility(View.GONE);
        }
        renderErrorItemHeader(cartShopHolderData);
        renderWarningItemHeader(cartShopHolderData);

        String labelShop = tvShopName.getContext().getResources().getString(R.string.label_toko) + " ";
        String shopName = cartShopHolderData.getShopGroupData().getShopName();

        SpannableStringBuilder completeLabelShop = new SpannableStringBuilder();
        completeLabelShop.append(labelShop);
        int start = labelShop.length();
        completeLabelShop.append(shopName);
        completeLabelShop.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, completeLabelShop.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        System.out.println("## completeLabelShop = "+completeLabelShop+", start = "+start+", end  = "+completeLabelShop.length());

        tvShopName.setText(completeLabelShop);
        tvShopName.setOnClickListener(v -> actionListener.onCartShopNameClicked(cartShopHolderData));

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
        rvCartItem.setAdapter(cartItemAdapter);
        ((SimpleItemAnimator) rvCartItem.getItemAnimator()).setSupportsChangeAnimations(false);

        cbSelectShop.setEnabled(!cartShopHolderData.getShopGroupData().isError());
        cbSelectShop.setChecked(cartShopHolderData.isAllSelected());
        cbSelectShop.setOnClickListener(cbSelectShopClickListener(cartShopHolderData));

        imgFulfillment.setVisibility(cartShopHolderData.getShopGroupData().isFulfillment() ?
                View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(cartShopHolderData.getShopGroupData().getFulfillmentName())) {
            tvFulfillDistrict.setVisibility(View.VISIBLE);
            tvFulfillDistrict.setText(cartShopHolderData.getShopGroupData().getFulfillmentName());
        } else {
            tvFulfillDistrict.setVisibility(View.GONE);
        }

    }

    private void renderPromoMerchant(CartShopHolderData cartShopHolderData, boolean priorityToEnabled) {
        if (cartShopHolderData.getShopGroupData().isHasPromoList()) {
            tickerPromoStackingCheckoutView.setVisibility(View.VISIBLE);

            if (!priorityToEnabled) {
                int disabledItem = 0;
                for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                    if (!cartItemHolderData.isSelected()) {
                        disabledItem++;
                    }
                }
                if (disabledItem == cartShopHolderData.getShopGroupData().getCartItemDataList().size()) {
                    tickerPromoStackingCheckoutView.setVariant(TickerPromoStackingCheckoutView.Variant.MERCHANT);
                    tickerPromoStackingCheckoutView.disableView();
                } else {
                    tickerPromoStackingCheckoutView.setState(TickerPromoStackingCheckoutView.State.EMPTY);
                }
                if (cartShopHolderData.getShopGroupData().getVoucherOrdersItemData() != null) {
                    actionListener.onCancelVoucherMerchantClicked(cartShopHolderData.getShopGroupData().getVoucherOrdersItemData().getCode(), getAdapterPosition(), true);
                    cartShopHolderData.getShopGroupData().setVoucherOrdersItemData(null);
                }
            } else {
                int disabledItem = 0;
                for (CartItemHolderData cartItemHolderData : cartShopHolderData.getShopGroupData().getCartItemDataList()) {
                    if (!cartItemHolderData.isSelected()) {
                        disabledItem++;
                    }
                }
                if (disabledItem == cartShopHolderData.getShopGroupData().getCartItemDataList().size()) {
                    tickerPromoStackingCheckoutView.setVariant(TickerPromoStackingCheckoutView.Variant.MERCHANT);
                    tickerPromoStackingCheckoutView.disableView();
                    if (cartShopHolderData.getShopGroupData().getVoucherOrdersItemData() != null) {
                        actionListener.onCancelVoucherMerchantClicked(cartShopHolderData.getShopGroupData().getVoucherOrdersItemData().getCode(), getAdapterPosition(), true);
                        cartShopHolderData.getShopGroupData().setVoucherOrdersItemData(null);
                    }
                } else {
                    boolean isApplied = false;
                    tickerPromoStackingCheckoutView.enableView();
                    if (disabledItem > 0) {
                        if (cartShopHolderData.getShopGroupData().getVoucherOrdersItemData() != null &&
                            cartShopHolderData.getShopGroupData().getVoucherOrdersItemData().getIsAutoapply()) {
                            actionListener.onCancelVoucherMerchantClicked(cartShopHolderData.getShopGroupData().getVoucherOrdersItemData().getCode(), getAdapterPosition(), true);
                            tickerPromoStackingCheckoutView.setState(TickerPromoStackingCheckoutView.State.EMPTY);
                            tickerPromoStackingCheckoutView.setVariant(TickerPromoStackingCheckoutView.Variant.MERCHANT);
                            tickerPromoStackingCheckoutView.setVisibility(View.VISIBLE);
                        } else {
                            isApplied = true;
                        }
                    } else {
                        isApplied = true;
                    }

                    if (isApplied) {
                        if (cartShopHolderData.getShopGroupData().getVoucherOrdersItemData() != null) {
                            tickerPromoStackingCheckoutView.setVariant(TickerPromoStackingCheckoutView.Variant.MERCHANT);
                            VoucherOrdersItemData voucherOrdersItemData = cartShopHolderData.getShopGroupData().getVoucherOrdersItemData();
                            String state = voucherOrdersItemData.getMessageData().getState();
                            if (state.equalsIgnoreCase("red")) {
                                tickerPromoStackingCheckoutView.setState(TickerPromoStackingCheckoutView.State.FAILED);
                            } else if (state.equalsIgnoreCase("green")) {
                                tickerPromoStackingCheckoutView.setState(TickerPromoStackingCheckoutView.State.ACTIVE);
                            } else {
                                tickerPromoStackingCheckoutView.setState(TickerPromoStackingCheckoutView.State.INACTIVE);
                            }
                            tickerPromoStackingCheckoutView.setDesc(voucherOrdersItemData.getInvoiceDescription());
                            tickerPromoStackingCheckoutView.setTitle(voucherOrdersItemData.getMessageData().getText());
                        } else {
                            tickerPromoStackingCheckoutView.setState(TickerPromoStackingCheckoutView.State.EMPTY);
                            tickerPromoStackingCheckoutView.setVariant(TickerPromoStackingCheckoutView.Variant.MERCHANT);
                            tickerPromoStackingCheckoutView.setVisibility(View.VISIBLE);
                        }
                    }

                    tickerPromoStackingCheckoutView.setActionListener(new TickerPromoStackingCheckoutView.ActionListener() {
                        @Override
                        public void onClickUsePromo() {
                            actionListener.onVoucherMerchantPromoClicked(cartShopHolderData.getShopGroupData());
                        }

                        @Override
                        public void onResetPromoDiscount() {
                            actionListener.onCancelVoucherMerchantClicked(cartShopHolderData.getShopGroupData().getVoucherOrdersItemData().getCode(), getAdapterPosition(), false);
                        }

                        @Override
                        public void onClickDetailPromo() {

                        }

                        @Override
                        public void onDisablePromoDiscount() {
                            actionListener.onCancelVoucherMerchantClicked(cartShopHolderData.getShopGroupData().getVoucherOrdersItemData().getCode(), getAdapterPosition(), true);
                        }
                    });
                }
            }
        } else {
            tickerPromoStackingCheckoutView.setVisibility(View.GONE);
        }
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
            renderPromoMerchant(data, false);
        } else {
            cbSelectShop.setEnabled(true);
            flShopItemContainer.setForeground(ContextCompat.getDrawable(flShopItemContainer.getContext(), R.drawable.fg_enabled_item));
            llShopContainer.setBackgroundColor(llShopContainer.getContext().getResources().getColor(R.color.white));
            layoutError.setVisibility(View.GONE);
            renderPromoMerchant(data, true);
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
                        actionListener.onShopItemCheckChanged(getAdapterPosition(), isChecked);
                    }
                }
            }
        };
    }
}
