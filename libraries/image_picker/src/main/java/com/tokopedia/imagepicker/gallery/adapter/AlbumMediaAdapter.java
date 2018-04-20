package com.tokopedia.imagepicker.gallery.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.gallery.model.AlbumItem;
import com.tokopedia.imagepicker.gallery.model.MediaItem;
import com.tokopedia.imagepicker.gallery.widget.MediaGrid;

/**
 * Created by hangnadi on 5/29/17.
 */

public class AlbumMediaAdapter extends RecyclerViewCursorAdapter<AlbumMediaAdapter.MediaViewHolder>
        implements MediaGrid.OnMediaGridClickListener {

    private RecyclerView mRecyclerView;
    private OnMediaClickListener mOnMediaClickListener;
    private int mImageResize;

    public AlbumMediaAdapter() {
        super(null);
    }

    public interface OnMediaClickListener {
        void onMediaClick(AlbumItem album, MediaItem item, int adapterPosition);
    }

    public void registerOnMediaClickListener(OnMediaClickListener listener) {
        mOnMediaClickListener = listener;
    }

    @Override
    public void onThumbnailClicked(ImageView thumbnail, MediaItem item, RecyclerView.ViewHolder holder) {
        if (mOnMediaClickListener != null) {
            mOnMediaClickListener.onMediaClick(null, item, holder.getAdapterPosition());
        }
    }

    protected class MediaViewHolder extends RecyclerView.ViewHolder {

        private MediaGrid mMediaGrid;

        public MediaViewHolder(View itemView) {
            super(itemView);

            mMediaGrid = (MediaGrid) itemView;
        }
    }

    @Override
    protected void onBindViewHolder(MediaViewHolder holder, Cursor cursor) {
        MediaViewHolder mediaViewHolder = holder;

        final MediaItem item = MediaItem.valueOf(cursor);
        mediaViewHolder.mMediaGrid.preBindMedia(
                new MediaGrid.PreBindInfo(getImageResize(mediaViewHolder.mMediaGrid.getContext()),
                null,
                holder
        ));
        mediaViewHolder.mMediaGrid.bindMedia(item);
        mediaViewHolder.mMediaGrid.setOnMediaGridClickListener(this);
    }

    private int getImageResize(Context context) {
        if (mImageResize == 0) {
            RecyclerView.LayoutManager lm = mRecyclerView.getLayoutManager();
            int spanCount = ((GridLayoutManager) lm).getSpanCount();
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            int availableWidth = screenWidth - context.getResources().getDimensionPixelSize(
                    R.dimen.media_grid_spacing) * (spanCount - 1);
            mImageResize = availableWidth / spanCount;
            mImageResize = (int) (mImageResize * 0.85f);
        }
        return mImageResize;
    }

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_media_grid_item, parent, false);
        return new MediaViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }
}
