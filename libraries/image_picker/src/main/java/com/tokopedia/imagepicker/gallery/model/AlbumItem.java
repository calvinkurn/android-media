package com.tokopedia.imagepicker.gallery.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.gallery.loader.AlbumLoader;
import com.tokopedia.imagepicker.gallery.loader.AlbumMediaLoader;

/**
 * Created by hangnadi on 5/22/17.
 */

public class AlbumItem implements Parcelable {

    public static final String ALBUM_ID_ALL = String.valueOf(-1);
    public static final String ALBUM_NAME_ALL = "All";

    private final String mId;
    private final String coverPath;
    private final String displayName;
    private long mCount;

    public AlbumItem(String mId, String coverPath, String displayName, long mCount) {
        this.mId = mId;
        this.coverPath = coverPath;
        this.displayName = displayName;
        this.mCount = mCount;
    }

    public String getmId() {
        return mId;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getmCount() {
        return mCount;
    }

    public void setmCount(long mCount) {
        this.mCount = mCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.coverPath);
        dest.writeString(this.displayName);
        dest.writeLong(this.mCount);
    }

    protected AlbumItem(Parcel in) {
        this.mId = in.readString();
        this.coverPath = in.readString();
        this.displayName = in.readString();
        this.mCount = in.readLong();
    }

    public static final Creator<AlbumItem> CREATOR = new Creator<AlbumItem>() {
        @Override
        public AlbumItem createFromParcel(Parcel source) {
            return new AlbumItem(source);
        }

        @Override
        public AlbumItem[] newArray(int size) {
            return new AlbumItem[size];
        }
    };

    public static AlbumItem valueOf(Cursor cursor) {
        return new AlbumItem(
                cursor.getString(cursor.getColumnIndex(AlbumMediaLoader.BUCKET_ID)),
                cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)),
                cursor.getString(cursor.getColumnIndex(AlbumMediaLoader.BUCKET_DISPLAY_NAME)),
                cursor.getLong(cursor.getColumnIndex(AlbumLoader.COLUMN_COUNT)));
    }

    public long getCount() {
        return mCount;
    }

    public void addCaptureCount() {
        mCount++;
    }

    public String getDisplayName(Context context) {
        if (isAll()) {
            return context.getString(R.string.album_name_all);
        }
        return displayName;
    }

    public boolean isAll() {
        return ALBUM_ID_ALL.equals(mId);
    }

    public boolean isEmpty() {
        return mCount == 0;
    }
}
