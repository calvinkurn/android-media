package com.tokopedia.chat_common.view.adapter.viewholder;

import android.content.res.Resources;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.common.utils.image.DynamicSizeImageRequestListener;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.chat_common.R;
import com.tokopedia.chat_common.data.ImageAnnouncementUiModel;
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener;

/**
 * @author by nisie on 5/15/18.
 */
public class ImageAnnouncementViewHolder extends BaseChatViewHolder<ImageAnnouncementUiModel> {

    private final ImageAnnouncementListener listener;
    @LayoutRes
    public static final int LAYOUT = R.layout.layout_image_announcement;

    private ImageView attachment;
    private LinearLayout container;
    private Button btnCheckNow;

    public ImageAnnouncementViewHolder(View itemView, ImageAnnouncementListener listener) {
        super(itemView);
        attachment = itemView.findViewById(R.id.image);
        container = itemView.findViewById(R.id.card_group_chat_message);
        btnCheckNow = itemView.findViewById(R.id.btn_check);
        this.listener = listener;
    }

    @Override
    public void bind(final ImageAnnouncementUiModel viewModel) {
        super.bind(viewModel);
        ImageHandler.loadImageWithListener(attachment, viewModel.getImageUrl(), new DynamicSizeImageRequestListener());
        container.setOnClickListener(view -> listener.onImageAnnouncementClicked(viewModel));
        btnCheckNow.setOnClickListener(view -> listener.onImageAnnouncementClicked(viewModel));
        bindCornerAttachment();
    }

    private void bindCornerAttachment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            attachment.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(
                            0,
                            0,
                            view.getWidth(),
                            view.getHeight() + toDp(8),
                            toDp(8)
                    );
                }
            });
            attachment.setClipToOutline(true);
        }
    }

    @Override
    public void onViewRecycled() {
        super.onViewRecycled();
        if (attachment != null) {
            ImageHandler.clearImage(attachment);
        }
    }

    @Override
    protected int getDateId() {
        return R.id.tvDate;
    }

    private int toDp(int val) {
        return (int) (val * Resources.getSystem().getDisplayMetrics().density);
    }
}
