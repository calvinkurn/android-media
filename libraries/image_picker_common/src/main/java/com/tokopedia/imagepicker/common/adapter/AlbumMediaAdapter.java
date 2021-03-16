package com.tokopedia.imagepicker.common.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.imagepicker.common.R;
import com.tokopedia.imagepicker.common.model.MediaItem;
import com.tokopedia.imagepicker.common.widget.MediaGrid;

import java.util.ArrayList;

/**
 * Created by hangnadi on 5/29/17.
 */

public class AlbumMediaAdapter extends RecyclerViewCursorAdapter<AlbumMediaAdapter.MediaViewHolder>
        implements MediaGrid.OnMediaGridClickListener {

    private RecyclerView mRecyclerView;
    private OnMediaClickListener mOnMediaClickListener;
    private int mImageResize;

    private boolean supportMultipleSelection;
    private ArrayList<String> selectionImagePathList;

    public AlbumMediaAdapter(boolean supportMultipleSelection, ArrayList<String> selectionImagePathList, OnMediaClickListener listener) {
        super(null);
        setSelectionIdList(selectionImagePathList);
        this.supportMultipleSelection = supportMultipleSelection;
        mOnMediaClickListener = listener;
    }

    private void setSelectionIdList(ArrayList<String> selectionImagePathList) {
        if (selectionImagePathList == null) {
            this.selectionImagePathList = new ArrayList<>();
        } else {
            this.selectionImagePathList = selectionImagePathList;
        }
    }

    public interface OnMediaClickListener {
        void onMediaClick(MediaItem item, boolean checked, int adapterPosition);

        boolean isMediaValid(MediaItem item);

        boolean canAddMoreMedia();
    }

    @Override
    public void onThumbnailClicked(ImageView thumbnail, MediaItem item, RecyclerView.ViewHolder holder) {
        boolean isChecked = true;
        if (supportMultipleSelection) {
            isChecked = !selectionImagePathList.contains(item.getPath());
        }

        if (isChecked && !mOnMediaClickListener.canAddMoreMedia()) {
            return;
        }

        if (isChecked && !mOnMediaClickListener.isMediaValid(item)) {
            return;
        }

        notifyItemChanged(holder.getAdapterPosition());

        if (mOnMediaClickListener != null) {
            mOnMediaClickListener.onMediaClick(
                    item,
                    isChecked,
                    holder.getAdapterPosition());
        }
    }

    class MediaViewHolder extends RecyclerView.ViewHolder {

        private MediaGrid mMediaGrid;

        MediaViewHolder(View itemView) {
            super(itemView);

            mMediaGrid = (MediaGrid) itemView;
        }
    }

    @Override
    protected void onBindViewHolder(MediaViewHolder holder, Cursor cursor) {

        final MediaItem item = MediaItem.valueOf(cursor);
        holder.mMediaGrid.preBindMedia(
                new MediaGrid.PreBindInfo(getImageResize(holder.mMediaGrid.getContext()),
                        0, com.tokopedia.abstraction.R.drawable.error_drawable,
                        holder
                ));
        holder.mMediaGrid.bindMedia(item, selectionImagePathList);
        holder.mMediaGrid.setOnMediaGridClickListener(this);
    }

    private int getImageResize(Context context) {
        if (mImageResize == 0) {
            RecyclerView.LayoutManager lm = mRecyclerView.getLayoutManager();
            int spanCount = ((GridLayoutManager) lm).getSpanCount();
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            int availableWidth = screenWidth - context.getResources().getDimensionPixelSize(
                    R.dimen.image_picker_media_grid_spacing) * (spanCount - 1);
            mImageResize = availableWidth / spanCount;
            mImageResize = (int) (mImageResize * 0.85f);
        }
        return mImageResize;
    }

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_grid_item, parent, false);
        return new MediaViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }
}
