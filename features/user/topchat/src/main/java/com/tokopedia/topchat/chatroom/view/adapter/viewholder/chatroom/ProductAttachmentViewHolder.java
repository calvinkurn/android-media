package com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatroom;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.BaseChatViewHolder;
import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract;
import com.tokopedia.topchat.chatroom.view.viewmodel.productattachment.ProductAttachmentViewModel;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.BaseChatViewHolder;
import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract;

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
    private TextView name;
    private TextView label;
    private TextView dot;
    private ImageView thumbnailsImage;

    private Context context;
    private ChatRoomContract.View viewListener;

    public ProductAttachmentViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView, viewListener);
        this.context = itemView.getContext();
        chatStatus = itemView.findViewById(R.id.chat_status);
        name = itemView.findViewById(R.id.name);
        label = itemView.findViewById(R.id.label);
        dot = itemView.findViewById(R.id.dot);
        progressBarSendImage = itemView.findViewById(R.id.progress_bar);
        chatBalloon = itemView.findViewById(R.id.attach_product_chat_container);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(ProductAttachmentViewModel element) {
        super.bind(element);
        prerequisiteUISetup(element);
        setupChatBubbleAlignment(chatBalloon, element);
        setupProductUI(element, chatBalloon);
    }

    private void setupChatBubbleAlignment(View productContainerView, ProductAttachmentViewModel element) {
        if (element.isSender()) {
            setChatRight(productContainerView, element);
        } else {
            setChatLeft(productContainerView, element);
        }
    }

    private void setChatLeft(View productContainerView, ProductAttachmentViewModel element) {
        productContainerView.setBackground(context.getResources().getDrawable(R.drawable
                .attach_product_left_bubble));
        setAlignParent(RelativeLayout.ALIGN_PARENT_LEFT, productContainerView);
        chatStatus.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        label.setVisibility(View.GONE);
        dot.setVisibility(View.GONE);
    }

    private void setChatRight(View productContainerView, ProductAttachmentViewModel element) {
        productContainerView.setBackground(context.getResources().getDrawable(R.drawable
                .attach_product_right_bubble));
        setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT, productContainerView);
        setChatReadStatus(element);
    }

    protected void prerequisiteUISetup(final ProductAttachmentViewModel element) {
        progressBarSendImage.setVisibility(View.GONE);

        chatBalloon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (element.getProductId() != null
                        && element.getProductName() != null
                        && element.getProductPrice() != null) {
                    viewListener.productClicked(element.getProductId(),
                            element.getProductName(),
                            element.getProductPrice(),
                            element.getDateTimeInMilis(),
                            element.getProductUrl());
                }
            }
        });

        if (element.getFromRole() != null
                && !TextUtils.isEmpty(element.getFromRole())
                && !element.getFromRole().toLowerCase().equals(ROLE_USER.toLowerCase())
                && element.isSender()
                && !element.isDummy()
                && element.isShowRole()) {
            name.setText(element.getFrom());
            label.setText(element.getFromRole());
            name.setVisibility(View.VISIBLE);
            dot.setVisibility(View.VISIBLE);
            label.setVisibility(View.VISIBLE);

        } else {
            name.setVisibility(View.GONE);
            label.setVisibility(View.GONE);
            dot.setVisibility(View.GONE);
        }
    }

    private void setChatReadStatus(ProductAttachmentViewModel element) {
        int imageResource;
        if(element.isShowTime()) {
            chatStatus.setVisibility(View.VISIBLE);
            if (element.isRead()) {
                imageResource = R.drawable.ic_chat_read;
            } else {
                imageResource = R.drawable.ic_chat_unread;
            }

            if (element.isDummy()) {
                imageResource = R.drawable.ic_chat_pending;
            }
            chatStatus.setImageResource(imageResource);
        } else {
            chatStatus.setVisibility(View.GONE);
        }
    }

    private void setAlignParent(int alignment, View view) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        params.addRule(alignment);
        view.setLayoutParams(params);
    }

    private void setupProductUI(ProductAttachmentViewModel element, View productContainer) {
        setUIValue(productContainer, R.id.attach_product_chat_image, element
                .getProductImage());
        setUIValue(productContainer, R.id.attach_product_chat_name, element.getProductName());
        setUIValue(productContainer, R.id.attach_product_chat_price, element.getProductPrice());
    }

    private void setUIValue(View productContainer, int id, String value) {
        View destination = productContainer.findViewById(id);
        if (destination instanceof TextView)
            ((TextView) destination).setText(value);
        else if (destination instanceof ImageView) {
            ImageHandler.loadImageRounded2(destination.getContext(), (ImageView) destination,
                    value);
            this.thumbnailsImage = (ImageView) destination;

        }
    }

    @Override
    public void onViewRecycled() {
        super.onViewRecycled();
        if (thumbnailsImage != null) {
            Glide.clear(thumbnailsImage);
        }
    }
}
