package com.tokopedia.chat_common.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.DynamicSizeImageRequestListener;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.chat_common.R;
import com.tokopedia.chat_common.data.ImageUploadViewModel;
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener;

/**
 * Created by stevenfredian on 11/28/17.
 */

public class ImageUploadViewHolder extends BaseChatViewHolder<ImageUploadViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.layout_image_upload;


    private static final String ROLE_USER = "User";
    private static final int BLUR_WIDTH = 30;
    private static final int BLUR_HEIGHT = 30;
    private final ImageUploadListener listener;

    private View progressBarSendImage;
    private ImageView chatStatus;
    private View chatBalloon;
    private TextView name;
    private TextView label;
    private TextView dot;
    private ImageView attachment;
    private ImageView action;

    public ImageUploadViewHolder(View itemView, ImageUploadListener listener) {
        super(itemView);
        this.listener = listener;
        chatStatus = itemView.findViewById(R.id.chat_status);
        name = itemView.findViewById(R.id.name);
        label = itemView.findViewById(R.id.label);
        dot = itemView.findViewById(R.id.dot);
        attachment = itemView.findViewById(R.id.image);
        action = itemView.findViewById(R.id.left_action);
        progressBarSendImage = itemView.findViewById(R.id.progress_bar);
        chatBalloon = itemView.findViewById(R.id.card_group_chat_message);
    }

    @Override
    public void bind(final ImageUploadViewModel element) {
        if (element != null) {

            super.bind(element);

            prerequisiteUISetup(element);
            setupChatBubbleAlignment(chatBalloon, element);

            view.setOnClickListener(view -> {
                if (element.getImageUrl() != null && element.getReplyTime() != null) {
                    listener.onImageUploadClicked(element.getImageUrl(), element.getReplyTime());
                }
            });

            if (element.isDummy()) {
                setVisibility(progressBarSendImage, View.VISIBLE);
                ImageHandler.loadImageBlurredWithListener(attachment, element.getImageUrl(),
                        BLUR_WIDTH, BLUR_HEIGHT,
                        new DynamicSizeImageRequestListener());
            } else {
                setVisibility(progressBarSendImage, View.GONE);
                ImageHandler.loadImageWithListener(attachment, element.getImageUrl(),
                        new DynamicSizeImageRequestListener());
            }


            if (element.isRetry()) {
                setRetryView(element);
            }


            setVisibility(attachment, View.VISIBLE);

        }
    }

    private void setupChatBubbleAlignment(View chatBalloon, ImageUploadViewModel element) {
        if (element.isSender()) {
            setChatRight(chatBalloon);
            setReadStatus(element);
        } else {
            setChatLeft(chatBalloon);
        }
    }

    private void setChatLeft(View chatBalloon) {
        setAlignParent(RelativeLayout.ALIGN_PARENT_LEFT, chatBalloon);
        setAlignParent(RelativeLayout.ALIGN_PARENT_LEFT, hour);
        chatStatus.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        label.setVisibility(View.GONE);
        dot.setVisibility(View.GONE);
    }

    private void setChatRight(View chatBalloon) {
        setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT, chatBalloon);
        setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT, hour);
        chatStatus.setVisibility(View.VISIBLE);
    }

    private void setAlignParent(int alignment, View view) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        params.addRule(alignment);
        view.setLayoutParams(params);
    }

    private void setRetryView(final ImageUploadViewModel element) {
        setVisibility(action, View.VISIBLE);
        setClickListener(action, view -> listener.onRetrySendImage(element));
        setVisibility(hour, View.GONE);
        setVisibility(chatStatus, View.GONE);
        setVisibility(progressBarSendImage, View.GONE);
    }

    protected void prerequisiteUISetup(ImageUploadViewModel element) {
        action.setVisibility(View.GONE);
        progressBarSendImage.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(element.getFromRole())
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

    public void setReadStatus(ImageUploadViewModel element) {
        int imageResource;
        if (element.isShowTime()) {
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


    private void setVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    private void setClickListener(View view, View.OnClickListener onClickListener) {
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
    }

    @Override
    public void onViewRecycled() {
        super.onViewRecycled();
        if (attachment != null) {
            ImageHandler.clearImage(attachment);
        }
    }
}
