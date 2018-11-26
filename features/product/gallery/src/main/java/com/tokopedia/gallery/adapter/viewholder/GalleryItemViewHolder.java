package com.tokopedia.gallery.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.gallery.GalleryView;
import com.tokopedia.gallery.R;
import com.tokopedia.gallery.viewmodel.ImageReviewItem;

public class GalleryItemViewHolder extends AbstractViewHolder<ImageReviewItem> {

    @LayoutRes
    public static final int LAYOUT = R.layout.gallery_item;

    private ImageView galleryImage;
    private GalleryView galleryView;

    public GalleryItemViewHolder(View itemView, GalleryView galleryView) {
        super(itemView);
        galleryImage = itemView.findViewById(R.id.galleryImage);
        this.galleryView = galleryView;
    }

    public void bind(ImageReviewItem imageReviewItem) {
        ImageHandler.LoadImage(galleryImage, imageReviewItem.getImageUrlThumbnail());
        galleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryView.onGalleryItemClicked(getAdapterPosition());
            }
        });
    }
}