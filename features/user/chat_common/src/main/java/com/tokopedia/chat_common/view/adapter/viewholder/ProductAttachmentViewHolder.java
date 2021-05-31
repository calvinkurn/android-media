package com.tokopedia.chat_common.view.adapter.viewholder;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.chat_common.R;
import com.tokopedia.chat_common.data.ProductAttachmentViewModel;
import com.tokopedia.chat_common.data.SendableViewModel;
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.unifycomponents.Label;
import com.tokopedia.unifycomponents.UnifyButton;

import static com.tokopedia.unifycomponents.HelperFunctionKt.toDp;

/**
 * @author by nisie on 5/14/18.
 */
public class ProductAttachmentViewHolder extends BaseChatViewHolder<ProductAttachmentViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.attached_product_chat_item;


    private static final String ROLE_USER = "User";

    private View progressBarSendImage;
    private ImageView chatStatus;
    private View chatBalloon;
    private ImageView thumbnailsImage;
    private UnifyButton tvBuy;
    private ImageView ivATC;
    private ImageView ivWishList;
    private View footerLayout;
    private ImageView freeShipping;

    private LinearLayout productVariantContainer;
    private LinearLayout productColorVariant;
    private ImageView productColorVariantHex;
    private TextView productColorVariantValue;
    private LinearLayout productSizeVariant;
    private TextView productSizeVariantValue;
    private LinearLayout timestampContainer;

    private Context context;
    private ProductAttachmentListener viewListener;

    public ProductAttachmentViewHolder(View itemView) {
        super(itemView);
    }

    public ProductAttachmentViewHolder(View itemView, ProductAttachmentListener viewListener) {
        super(itemView);
        this.context = itemView.getContext();
        chatStatus = itemView.findViewById(R.id.chat_status);
        progressBarSendImage = itemView.findViewById(R.id.progress_bar);
        chatBalloon = itemView.findViewById(R.id.attach_product_chat_container);
        freeShipping = itemView.findViewById(R.id.iv_free_shipping);
        tvBuy = chatBalloon.findViewById(R.id.tv_buy);
        ivATC = chatBalloon.findViewById(R.id.ic_add_to_cart);
        ivWishList = chatBalloon.findViewById(R.id.ic_add_to_wishlist);
        footerLayout = chatBalloon.findViewById(R.id.footer_layout);
        productVariantContainer = itemView.findViewById(R.id.ll_variant);
        productColorVariant = itemView.findViewById(R.id.ll_variant_color);
        productColorVariantHex = itemView.findViewById(R.id.iv_variant_color);
        productColorVariantValue = itemView.findViewById(R.id.tv_variant_color);
        productSizeVariant = itemView.findViewById(R.id.ll_variant_size);
        productSizeVariantValue = itemView.findViewById(R.id.tv_variant_size);
        timestampContainer = itemView.findViewById(R.id.ll_timestamp);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(ProductAttachmentViewModel element) {
        super.bind(element);
        prerequisiteUISetup(element);
        setupProductUI(element, chatBalloon);
        setupFreeShipping(element);
        setupChatBubbleAlignment(chatBalloon, element);
        setupVariantLayout(element);
        setupIfEmptyStock(element);
        viewListener.trackSeenProduct(element);
    }

    @Override
    protected boolean alwaysShowTime() {
        return true;
    }

    @Override
    protected int getDateId() {
        return R.id.tvDate;
    }

    private void setupIfEmptyStock(ProductAttachmentViewModel element) {
        if (element.hasEmptyStock()) {
            tvBuy.setEnabled(false);
            tvBuy.setText(R.string.action_empty_stock);
            ivATC.setImageDrawable(
                    MethodChecker.getDrawable(itemView.getContext(), R.drawable.ic_cart_greyscale_disabled)
            );
        } else {
            tvBuy.setEnabled(true);
            tvBuy.setText(R.string.action_buy);
            ivATC.setImageDrawable(
                    MethodChecker.getDrawable(itemView.getContext(), R.drawable.ic_cart_grayscale_20)
            );
        }
    }

    private void setupVariantLayout(ProductAttachmentViewModel element) {
        if (element.doesNotHaveVariant()) {
            hideVariantLayout();
        } else {
            showVariantLayout();
        }

        if (element.hasColorVariant()) {
            productColorVariant.setVisibility(View.VISIBLE);
            Drawable backgroundDrawable = getBackgroundDrawable(element.getColorHexVariant());
            productColorVariantHex.setBackground(backgroundDrawable);
            productColorVariantValue.setText(element.getColorVariant());
        } else {
            productColorVariant.setVisibility(View.GONE);
        }

        if (element.hasSizeVariant()) {
            productSizeVariant.setVisibility(View.VISIBLE);
            productSizeVariantValue.setText(element.getSizeVariant());
        } else {
            productSizeVariant.setVisibility(View.GONE);
        }
    }

    private void hideVariantLayout() {
        productVariantContainer.setVisibility(View.GONE);
    }

    private void showVariantLayout() {
        productVariantContainer.setVisibility(View.VISIBLE);
    }

    private Drawable getBackgroundDrawable(String hexColor) {
        Drawable backgroundDrawable = MethodChecker.getDrawable(itemView.getContext(), R.drawable.topchat_circle_color_variant_indicator);

        if (backgroundDrawable == null) return null;

        if (isWhiteColor(hexColor)) {
            applyStrokeTo(backgroundDrawable);
            return backgroundDrawable;
        }

        backgroundDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(hexColor), PorterDuff.Mode.SRC_ATOP));
        return backgroundDrawable;
    }

    private void applyStrokeTo(Drawable backgroundDrawable) {
        if (backgroundDrawable instanceof GradientDrawable) {
            float strokeWidth = toDp(1);
            ((GradientDrawable) backgroundDrawable).setStroke((int) strokeWidth, ContextCompat.getColor(itemView.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N100));
        }
    }

    private boolean isWhiteColor(String hexColor) {
        return hexColor.equals(context.getString(R.string.white_color_hex))
                || hexColor.equals(context.getString(R.string.white_color_hex_simpler));
    }

    private void setupChatBubbleAlignment(View productContainerView, ProductAttachmentViewModel element) {
        if (element.isSender()) {
            setChatRight(productContainerView, element);
        } else {
            setChatLeft(productContainerView);
        }
    }

    private void setChatLeft(View productContainerView) {
        productContainerView.setBackground(
                MethodChecker.getDrawable(productContainerView.getContext(), R.drawable.bg_shadow_attach_product)
        );
        setAlignParent(RelativeLayout.ALIGN_PARENT_LEFT, productContainerView);
        chatStatus.setVisibility(View.GONE);
    }

    private void setChatRight(View productContainerView, ProductAttachmentViewModel element) {
        productContainerView.setBackground(
                MethodChecker.getDrawable(productContainerView.getContext(), R.drawable.bg_shadow_attach_product)
        );
        setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT, productContainerView);
        bindChatReadStatus(element);
    }

    @Override
    protected void bindChatReadStatus(SendableViewModel element) {
        super.bindChatReadStatus(element);
        if (alwaysShowTime()) {
            timestampContainer.setVisibility(View.VISIBLE);
        } else {
            timestampContainer.setVisibility(View.GONE);
        }
    }

    protected void prerequisiteUISetup(final ProductAttachmentViewModel element) {
        progressBarSendImage.setVisibility(View.GONE);
        chatBalloon.setOnClickListener(view -> viewListener.onProductClicked(element));
    }

    private void setAlignParent(int alignment, View view) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        params.addRule(alignment);
        params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        view.setLayoutParams(params);
    }

    private void setupProductUI(ProductAttachmentViewModel element, View productContainer) {
        setUIValue(productContainer, R.id.attach_product_chat_image, element
                .getProductImage());
        setUIValue(productContainer, R.id.attach_product_chat_name, element.getProductName());
        setUIValue(productContainer, R.id.attach_product_chat_price, element.getProductPrice());
        setUIDiscount(productContainer, element);
        setFooter(productContainer, element);
    }

    private void setupFreeShipping(ProductAttachmentViewModel product) {
        if (product.hasFreeShipping()) {
            freeShipping.setVisibility(View.VISIBLE);
            ImageHandler.loadImageRounded2(itemView.getContext(), freeShipping, product.getFreeShippingImageUrl());
        }
    }


    private void setUIDiscount(View productContainer, ProductAttachmentViewModel element) {
        setUIVisibility(productContainer, R.id.discount, element.getPriceBefore());
        setUIValue(productContainer, R.id.attach_product_chat_price_old, element.getPriceBefore());
        setUIValue(productContainer, R.id.discount, element.getDropPercentage() + "%");
        setUIVisibility(productContainer, R.id.discount, element.getDropPercentage());
        setUIVisibility(productContainer, R.id.attach_product_chat_price_old, element.getPriceBefore());
        setStrikeThrough(productContainer, R.id.attach_product_chat_price_old);
    }

    private void setUIVisibility(View productContainer, int resourceId, String content) {
        View destination = productContainer.findViewById(resourceId);

        if (!TextUtils.isEmpty(content)) {
            destination.setVisibility(View.VISIBLE);
        } else {
            destination.setVisibility(View.GONE);
        }
    }

    private void setFooter(View productContainer, ProductAttachmentViewModel element) {
        if (element.getCanShowFooter() && !GlobalConfig.isSellerApp()) {
            footerLayout.setVisibility(View.VISIBLE);
            tvBuy.setVisibility(View.VISIBLE);
            ivATC.setVisibility(View.VISIBLE);
            ivWishList.setVisibility(View.VISIBLE);
            tvBuy.setOnClickListener(v -> {
                viewListener.onClickBuyFromProductAttachment(element);
            });

            ivATC.setOnClickListener(v -> {
                if (!element.hasEmptyStock()) {
                    viewListener.onClickATCFromProductAttachment(element);
                }
            });
            bindWishListView(element);
            bindClickAddToWishList(element);
        } else {
            footerLayout.setVisibility(View.GONE);
            tvBuy.setVisibility(View.GONE);
            ivATC.setVisibility(View.GONE);
            ivWishList.setVisibility(View.GONE);
        }
    }

    private void bindWishListView(ProductAttachmentViewModel element) {
        Drawable loveDrawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_attachproduct_wishlist);
        if (loveDrawable == null) return;
        loveDrawable.mutate();
        ivWishList.setImageDrawable(loveDrawable);
        updateWishListIconState(element);
    }

    private void bindClickAddToWishList(ProductAttachmentViewModel element) {
        ivWishList.setOnClickListener(v -> {
            if (element.isWishListed()) {
                removeProductFromWishList(element);
            } else {
                addProductToWishList(element);
            }
        });
    }

    private void removeProductFromWishList(ProductAttachmentViewModel element) {
        viewListener.onClickRemoveFromWishList(element.getStringProductId(), () -> {
            onSuccessRemoveFromWishList(element);
            return null;
        });
    }

    private void addProductToWishList(ProductAttachmentViewModel element) {
        viewListener.onClickAddToWishList(element, () -> {
                    onSuccessAddToWishList(element);
                    return null;
                }
        );
    }

    private void onSuccessRemoveFromWishList(ProductAttachmentViewModel element) {
        element.setWishList(false);
        updateWishListIconState(element);
    }

    private void onSuccessAddToWishList(ProductAttachmentViewModel element) {
        element.setWishList(true);
        updateWishListIconState(element);
    }

    private void updateWishListIconState(ProductAttachmentViewModel element) {
        if (element.isWishListed()) {
            int color = ContextCompat.getColor(itemView.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_R500);
            ivWishList.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        } else {
            ivWishList.clearColorFilter();
        }
    }

    private void setUIValue(View productContainer, int id, String value) {
        View destination = productContainer.findViewById(id);
        if (destination instanceof Label) {
            ((Label) destination).setLabel(value);
        } else if (destination instanceof TextView) {
            ((TextView) destination).setText(value);
        } else if (destination instanceof ImageView) {
            ImageHandler.loadImageRounded2(
                    destination.getContext(),
                    (ImageView) destination,
                    value,
                    toPx(8f)
            );
            this.thumbnailsImage = (ImageView) destination;
        }
    }

    private void setStrikeThrough(View productContainer, int id) {
        View destination = productContainer.findViewById(id);
        if (destination instanceof TextView) {
            ((TextView) destination).setPaintFlags(((TextView) destination).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    private float toPx(float v) {
        return Resources.getSystem().getDisplayMetrics().density * v;
    }

    @Override
    public void onViewRecycled() {
        super.onViewRecycled();
        if (thumbnailsImage != null && thumbnailsImage.getContext() != null) {
            ImageHandler.clearImage(thumbnailsImage);
        }
    }
}
