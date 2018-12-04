package com.tokopedia.chat_common.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.chat_common.data.ImageAnnouncementViewModel;
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener;

/**
 * @author by nisie on 5/15/18.
 */
public class ImageAnnouncementViewHolder extends BaseChatViewHolder<ImageAnnouncementViewModel> {

    private final ImageAnnouncementListener listener;
    @LayoutRes
//    public static final int LAYOUT = R.layout.layout_image_announcement;
    public static final int LAYOUT = 0;


    private ImageView attachment;

    public ImageAnnouncementViewHolder(View itemView, ImageAnnouncementListener listener) {
        super(itemView);
//        attachment = itemView.findViewById(R.id.image);
        this.listener = listener;

    }

    @Override
    public void bind(final ImageAnnouncementViewModel viewModel) {
        super.bind(viewModel);

//        ImageHandler.loadImageWithListener(attachment, viewModel.getImageUrl(), new
//                DynamicSizeImageRequestListener());

        view.setOnClickListener(view -> listener.onImageAnnouncementClicked(viewModel));

//TODO MOVE THIS TO TOPCHAT
//        TrackingUtils.sendGTMEvent(
//                new EventTracking(
//                        "clickInboxChat",
//                        "inbox-chat",
//                        "click on thumbnail",
//                        viewModel.getBlastId() + " - " + viewModel.getAttachmentId()
//                ).getEvent()
//        );
//        if (!TextUtils.isEmpty(viewModel.getRedirectUrl())) {
//            viewListener.onGoToWebView(viewModel.getRedirectUrl(),
//                    viewModel.getAttachmentId());
//        }
    }

    @Override
    public void onViewRecycled() {
        super.onViewRecycled();
        if (attachment != null) {
            ImageHandler.clearImage(attachment);
        }
    }
}
