package com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatroom;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.BaseChatViewHolder;
import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract;
import com.tokopedia.topchat.chatroom.view.viewmodel.imageannouncement
        .ImageDualAnnouncementViewModel;
import com.tokopedia.topchat.common.util.ChatGlideImageRequestListener;

/**
 * Created by Hendri on 22/06/18.
 */
public class ImageDualAnnouncementViewHolder extends BaseChatViewHolder<ImageDualAnnouncementViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_announcement_dual_image;

    private ImageView top;
    private ImageView bottom;

    public ImageDualAnnouncementViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView, viewListener);
        top = itemView.findViewById(R.id.dual_image_top);
        bottom = itemView.findViewById(R.id.dual_image_bottom);
    }

    @Override
    public void bind(ImageDualAnnouncementViewModel viewModel) {
        super.bind(viewModel);

        ImageHandler.loadImageChat(top, viewModel.getImageUrlTop(), new ChatGlideImageRequestListener());
        top.setOnClickListener((View v) -> {
            TrackingUtils.sendGTMEvent(
                    v.getContext(),
                    new EventTracking(
                            "clickInboxChat",
                            "inbox-chat",
                            "click on thumbnail",
                            viewModel.getBlastId() + " - " + viewModel.getAttachmentId()
                    ).getEvent()
            );
            if (!TextUtils.isEmpty(viewModel.getRedirectUrlTop())) {
                viewListener.onGoToWebView(viewModel.getRedirectUrlTop(),
                        viewModel.getAttachmentId());
            }
        });
        ImageHandler.loadImageChat(bottom, viewModel.getImageUrlBottom(), new ChatGlideImageRequestListener());
        bottom.setOnClickListener((View v) -> {
            TrackingUtils.sendGTMEvent(
                    v.getContext(),
                    new EventTracking(
                            "clickInboxChat",
                            "inbox-chat",
                            "click on thumbnail",
                            viewModel.getBlastId() + " - " + viewModel.getAttachmentId()
                    ).getEvent()
            );
            if (!TextUtils.isEmpty(viewModel.getRedirectUrlBottom())) {
                viewListener.onGoToWebView(viewModel.getRedirectUrlBottom(),
                        viewModel.getAttachmentId());
            }
        });
    }

    @Override
    public void onViewRecycled() {
        super.onViewRecycled();
        if (top != null) {
            Glide.clear(top);
        }

        if (bottom != null) {
            Glide.clear(bottom);
        }
    }
}
