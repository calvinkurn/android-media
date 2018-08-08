package com.tokopedia.core.newgallery.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.newgallery.model.ImageModel;
import com.tokopedia.core.newgallery.presenter.ImageGalleryView;

import java.io.File;
import java.util.List;

/**
 * Created by m.normansyah on 12/6/15.
 */
public class ImageAlbumAdapter extends RecyclerView.Adapter<ImageAlbumAdapter.ViewHolder> {
    int maxSelection = -1;
    private List<ImageModel> data;

    public ImageAlbumAdapter(List<ImageModel> data) {
        this.data = data;
    }

    public int getMaxSelection() {
        return maxSelection;
    }

    public void setMaxSelection(int maxSelection) {
        this.maxSelection = maxSelection;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.picture_galery_album_item, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(data.get(position), maxSelection);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addAll(List<ImageModel> data) {
        this.data.addAll(data);
    }

    /**
     * This is view holder class
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageView;
        LinearLayout mBorder;
        TextView mAlbumname;
        ImageModel folderModel;
        int maxSelection = ImageGalleryAdapter.UNLIMITED_SELECTION;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.picture_gallery_album_imageview);
            mBorder = (LinearLayout) itemView.findViewById(R.id.border_gallery_album_layout);
            mAlbumname = (TextView) itemView.findViewById(R.id.picture_gallery_album_name);

            mImageView.setOnClickListener(this);
            mBorder.setOnClickListener(this);
            mImageView.setOnClickListener(this);
        }

        public void bindView(ImageModel item, int maxSelection) {
            this.folderModel = item;
            mAlbumname.setText(item.getName());
            ImageHandler.loadImageFit2(itemView.getContext(), mImageView, new File(item.getPathFile()));
            setMaxSelection(maxSelection);
        }

        public void moveToImageGallery() {
            if (itemView.getContext() != null && itemView.getContext() instanceof ImageGalleryView) {
                ((ImageGalleryView) itemView.getContext()).moveToGallery(getAdapterPosition(), maxSelection);
            }
        }

        public void setMaxSelection(int maxSelection) {
            this.maxSelection = maxSelection;
        }

        @Override
        public void onClick(View v) {
            moveToImageGallery();
        }
    }
}
