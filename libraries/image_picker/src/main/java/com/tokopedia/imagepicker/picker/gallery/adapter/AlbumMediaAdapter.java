package com.tokopedia.imagepicker.picker.gallery.adapter;

import android.content.Context;
import android.database.Cursor;
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
    private ArrayList<Long> selectionIdList;

    public AlbumMediaAdapter(boolean supportMultipleSelection, ArrayList<Long> selectionIdList, OnMediaClickListener listener) {
        super(null);
        setSelectionIdList(selectionIdList);
        this.supportMultipleSelection = supportMultipleSelection;
        mOnMediaClickListener = listener;
    }

    private void setSelectionIdList(ArrayList<Long> selectionIdList) {
        if (selectionIdList == null) {
            this.selectionIdList = new ArrayList<>();
        } else {
            this.selectionIdList = selectionIdList;
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
            if (selectionIdList.contains(item.getId())) {
                selectionIdList.remove(item.getId());
                isChecked = false;
            } else {
                selectionIdList.add(item.getId());
                isChecked = true;

            }
        }

        if (isChecked && !mOnMediaClickListener.canAddMoreImage()) {
            selectionIdList.remove(item.getId()); //in case support multiple selection
            return;
        }

        if (isChecked && !mOnMediaClickListener.isImageValid(item)) {
            selectionIdList.remove(item.getId()); //in case support multiple selection
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
                        null,
                        holder
                ));
        holder.mMediaGrid.bindMedia(item, selectionIdList);
        holder.mMediaGrid.setOnMediaGridClickListener(this);
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

    public ArrayList<Long> getSelectionIdList() {
        return selectionIdList;
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
