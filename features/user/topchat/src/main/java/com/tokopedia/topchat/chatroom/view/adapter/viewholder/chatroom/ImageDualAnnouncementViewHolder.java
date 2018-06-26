package com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatroom;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.BaseChatViewHolder;
import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract;
import com.tokopedia.topchat.chatroom.view.viewmodel.imageannouncement
        .ImageDualAnnouncementViewModel;

/**
 * Created by Hendri on 22/06/18.
 */
public class ImageDualAnnouncementViewHolder extends BaseChatViewHolder<ImageDualAnnouncementViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_announcement_dual_image;

    private ImageView top;
    private ImageView bottom;
    private CardView container;

    public ImageDualAnnouncementViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView, viewListener);
        top = itemView.findViewById(R.id.dual_image_top);
        bottom = itemView.findViewById(R.id.dual_image_bottom);
        container = itemView.findViewById(R.id.card_group_chat_message);
    }

    @Override
    public void bind(ImageDualAnnouncementViewModel viewModel) {
        super.bind(viewModel);

//        view.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
//        view.getLayoutParams().height = view.getLayoutParams().width / 2;

        ImageHandler.LoadImage(top, viewModel.getImageUrlLeft());
        top.setOnClickListener((View v) -> {
            if (!TextUtils.isEmpty(viewModel.getRedirectUrlLeft())) {
                viewListener.onGoToWebView(viewModel.getRedirectUrlLeft(),
                        viewModel.getAttachmentId());
            } });
        ImageHandler.LoadImage(bottom, viewModel.getImageUrlRight());
        bottom.setOnClickListener((View v) -> {
            if (!TextUtils.isEmpty(viewModel.getRedirectUrlRight())) {
                viewListener.onGoToWebView(viewModel.getRedirectUrlRight(),
                        viewModel.getAttachmentId());
            } });
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
