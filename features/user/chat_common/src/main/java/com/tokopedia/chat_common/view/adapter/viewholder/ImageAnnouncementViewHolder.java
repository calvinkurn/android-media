package com.tokopedia.chat_common.view.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.common.utils.image.DynamicSizeImageRequestListener;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.chat_common.R;
import com.tokopedia.chat_common.data.ImageAnnouncementViewModel;
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener;

/**
 * @author by nisie on 5/15/18.
 */
public class ImageAnnouncementViewHolder extends BaseChatViewHolder<ImageAnnouncementViewModel> {

    private final ImageAnnouncementListener listener;
    @LayoutRes
    public static final int LAYOUT = R.layout.layout_image_announcement;

    private ImageView attachment;

    public ImageAnnouncementViewHolder(View itemView, ImageAnnouncementListener listener) {
        super(itemView);
        attachment = itemView.findViewById(R.id.image);
        this.listener = listener;

    }

    @Override
    public void bind(final ImageAnnouncementViewModel viewModel) {
        super.bind(viewModel);
        ImageHandler.loadImageWithListener(attachment, viewModel.getImageUrl(), new DynamicSizeImageRequestListener());
        view.setOnClickListener(view -> listener.onImageAnnouncementClicked(viewModel));
    }

    @Override
    public void onViewRecycled() {
        super.onViewRecycled();
        if (attachment != null) {
            ImageHandler.clearImage(attachment);
        }
    }
}
