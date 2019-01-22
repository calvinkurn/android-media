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
import com.tokopedia.topchat.chatroom.view.viewmodel.imageannouncement.ImageAnnouncementViewModel;
import com.tokopedia.topchat.common.util.ChatGlideImageRequestListener;

/**
 * @author by nisie on 5/15/18.
 */
public class ImageAnnouncementViewHolder extends BaseChatViewHolder<ImageAnnouncementViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_image_announcement;

    private ImageView attachment;

    public ImageAnnouncementViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView, viewListener);
        attachment = itemView.findViewById(R.id.image);

    }

    @Override
    public void bind(final ImageAnnouncementViewModel viewModel) {
        super.bind(viewModel);

        ImageHandler.loadImageChat(attachment, viewModel.getImageUrl(), new
                ChatGlideImageRequestListener());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrackingUtils.sendGTMEvent(
                        view.getContext(),
                        new EventTracking(
                                "clickInboxChat",
                                "inbox-chat",
                                "click on thumbnail",
                                viewModel.getBlastId() + " - " + viewModel.getAttachmentId()
                        ).getEvent()
                );
                if (!TextUtils.isEmpty(viewModel.getRedirectUrl())) {
                    viewListener.onGoToWebView(viewModel.getRedirectUrl(),
                            viewModel.getAttachmentId());
                }
            }
        });
    }

    @Override
    public void onViewRecycled() {
        super.onViewRecycled();
        if (attachment != null) {
            Glide.clear(attachment);
        }
    }
}
