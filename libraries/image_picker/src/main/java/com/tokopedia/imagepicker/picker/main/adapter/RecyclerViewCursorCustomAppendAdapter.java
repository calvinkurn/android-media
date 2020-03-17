package com.tokopedia.imagepicker.picker.main.adapter;

import android.database.Cursor;
import android.provider.MediaStore;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class RecyclerViewCursorCustomAppendAdapter<VH extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<VH> {

    private Cursor mCursor;
    private int mRowIDColumn;

    //custom image list that will be appended at the start of original imageList
    private ArrayList<String> appendedImagePathOrUrlList;

    public RecyclerViewCursorCustomAppendAdapter(Cursor c, ArrayList<String> appendedImagePathOrUrlList) {
        setHasStableIds(true);
        swapCursor(c);
        setAppendedImagePathList(appendedImagePathOrUrlList);
    }

    private void setAppendedImagePathList(ArrayList<String> appendedImagePathList) {
        if (appendedImagePathList == null) {
            this.appendedImagePathOrUrlList = new ArrayList<>();
        } else {
            this.appendedImagePathOrUrlList = appendedImagePathList;
        }
    }

    protected abstract void onBindViewHolder(VH holder, Cursor cursor);
    protected abstract void onBindViewHolder(VH holder, String appendedImagePathOrUrl);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (isCursorType(position)) {
            if (!isDataValid(mCursor)) {
                throw new IllegalStateException("Cannot bind view holder when cursor is in invalid state.");
            }
            int cursorPosition = cursorPosition(position);
            if (!mCursor.moveToPosition(cursorPosition)) {
                throw new IllegalStateException("Could not move cursor to position " + position
                        + " when trying to bind view holder");
            }
            onBindViewHolder(holder, mCursor);
        } else {
            onBindViewHolder(holder, appendedImagePathOrUrlList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return getCursorCount() + getAppendedCount();
    }

    public int getAppendedCount() {
        int appendedCount;
        if (appendedImagePathOrUrlList != null) {
            appendedCount = appendedImagePathOrUrlList.size();
        } else {
            appendedCount = 0;
        }
        return appendedCount;
    }

    public int getCursorCount() {
        int cursorCount;
        if (isDataValid(mCursor)) {
            cursorCount = mCursor.getCount();
        } else {
            cursorCount = 0;
        }
        return cursorCount;
    }

    @Override
    public long getItemId(int position) {
        if (isCursorType(position)) {
            return getItemIdCursor(cursorPosition(position));
        } else {
            return getItemIdAppended(appendedImagePathOrUrlList.get(position));
        }
    }

    public boolean isCursorType(int position) {
        return position >= getAppendedCount();
    }

    public int cursorPosition(int adapterPosition) {
        return adapterPosition - getAppendedCount();
    }

    public long getItemIdCursor(int cursorPosition) {
        if (!isDataValid(mCursor)) {
            throw new IllegalStateException("Cannot lookup item id when cursor is in invalid state.");
        }
        if (!mCursor.moveToPosition(cursorPosition)) {
            throw new IllegalStateException("Could not move cursor to position " + cursorPosition
                    + " when trying to get an item id");
        }

        return mCursor.getLong(mRowIDColumn);
    }

    public long getItemIdAppended(String appendedImagePathOrUrl) {
        return -1 * Math.abs(appendedImagePathOrUrl.hashCode());
    }

    public void swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return;
        }

        if (newCursor != null) {
            mCursor = newCursor;
            mRowIDColumn = mCursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID);
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(getAppendedCount(), getCursorCount());
            mCursor = null;
            mRowIDColumn = -1;
        }
    }

    public void setAppendedImagePathOrUrlList(ArrayList<String> appendedImagePathOrUrlList) {
        this.appendedImagePathOrUrlList = appendedImagePathOrUrlList;
    }

    public Cursor getCursor() {
        return mCursor;
    }

    private boolean isDataValid(Cursor cursor) {
        return cursor != null && !cursor.isClosed();
    }
}