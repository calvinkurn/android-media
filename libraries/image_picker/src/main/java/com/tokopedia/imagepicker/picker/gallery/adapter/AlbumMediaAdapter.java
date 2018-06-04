package com.tokopedia.imagepicker.picker.gallery.adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.gallery.model.MediaItem;
import com.tokopedia.imagepicker.picker.gallery.widget.MediaGrid;
import com.tokopedia.imagepicker.picker.main.adapter.RecyclerViewCursorAdapter;

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

        boolean isImageValid(MediaItem item);

        boolean canAddMoreImage();
    }

    @Override
    public void onThumbnailClicked(ImageView thumbnail, MediaItem item, RecyclerView.ViewHolder holder) {
        boolean isChecked = true;
        if (supportMultipleSelection) {
            if (selectionImagePathList.contains(item.getRealPath())) {
                isChecked = false;
            } else {
                isChecked = true;
            }
        }

        if (isChecked && !mOnMediaClickListener.canAddMoreImage()) {
            return;
        }

        if (isChecked && !mOnMediaClickListener.isImageValid(item)) {
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

    public void removeImageFromSelection(String imagePath) {
        if (getCursor() == null || getCursor().getCount() == 0) {
            return;
        }

        if (!supportMultipleSelection) {
            return;
        }
        selectionImagePathList.remove(imagePath);
        notifyDataSetChanged();
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
                        0, R.drawable.error_drawable,
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
