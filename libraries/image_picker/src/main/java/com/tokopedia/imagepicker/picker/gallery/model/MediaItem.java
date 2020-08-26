package com.tokopedia.imagepicker.picker.gallery.model;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.tokopedia.imagepicker.common.util.ImageUtils;

/**
 * Created by hangnadi on 5/29/17.
 */

public class MediaItem implements Parcelable {

    public static final String X = "x";
    private final long id;
    private final String mimeType;
    private final Uri uri;
    private final long size;
    private final long duration; // only for video, in ms
    private final String videoResolution; // only for video, in ms
    private long height;
    private long width;

    private MediaItem(long id, String mimeType, long size, long duration, String videoResolution) {
        this.id = id;
        this.mimeType = mimeType;
        Uri contentUri;
        if (isImage()) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if (isVideo()) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else {
            // ?
            contentUri = MediaStore.Files.getContentUri("external");
        }
        this.uri = ContentUris.withAppendedId(contentUri, id);
        this.size = size;
        this.duration = duration;
        this.videoResolution = videoResolution == null? "" : videoResolution;
    }

    public long getWidth(Context context) {
        if(context == null)
            return 0;

        calculateWidthAndHeight(context);
        return width;
    }

    public long getHeight(Context context) {
        if(context == null)
            return 0;

        calculateWidthAndHeight(context);
        return height;
    }

    public long getDuration() {
        return duration;
    }

    public String getVideoResolution() {
        return videoResolution;
    }

    public int getMinimumVideoResolution() {
        String[] minResolution = videoResolution.split(X);
        if (minResolution.length == 2) {
            try {
                return Math.min(
                        Integer.parseInt(minResolution[0]),
                        Integer.parseInt(minResolution[1]));
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    private void calculateWidthAndHeight(Context context) {
        if (width == 0 || height == 0) {
            int[] widthHeight = ImageUtils.getWidthAndHeight(context, getRealPath());
            width = widthHeight[0];
            height = widthHeight[1];
        }
    }

    public static MediaItem valueOf(Cursor cursor) {
        int resolution =  cursor.getColumnIndex(MediaStore.Video.VideoColumns.RESOLUTION);

        return new MediaItem(cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)),
                cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)),
                cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)),
                cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION)),
                resolution > 0 ? cursor.getString(resolution):""
        );
    }

    public boolean isImage() {
        return mimeType.equals(MimeType.JPEG.toString())
                || mimeType.equals(MimeType.PNG.toString())
                || mimeType.equals(MimeType.GIF.toString())
                || mimeType.equals(MimeType.BMP.toString())
                || mimeType.equals(MimeType.WEBP.toString());
    }

    public boolean isGif() {
        return mimeType.equals(MimeType.GIF.toString());
    }

    public boolean isVideo() {
        return mimeType.equals(MimeType.MPEG.toString())
                || mimeType.equals(MimeType.MP4.toString())
                || mimeType.equals(MimeType.QUICKTIME.toString())
                || mimeType.equals(MimeType.THREEGPP.toString())
                || mimeType.equals(MimeType.THREEGPP2.toString())
                || mimeType.equals(MimeType.MKV.toString())
                || mimeType.equals(MimeType.WEBM.toString())
                || mimeType.equals(MimeType.TS.toString())
                || mimeType.equals(MimeType.AVI.toString());
    }

    public Uri getContentUri() {
        return uri;
    }

    public Uri getRealPath() {
        return getContentUri();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public long getId() {
        return id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.mimeType);
        dest.writeParcelable(this.uri, flags);
        dest.writeLong(this.size);
        dest.writeLong(this.duration);
        dest.writeLong(this.height);
        dest.writeLong(this.width);
        dest.writeString(this.videoResolution);
    }

    protected MediaItem(Parcel in) {
        this.id = in.readLong();
        this.mimeType = in.readString();
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.size = in.readLong();
        this.duration = in.readLong();
        this.height = in.readLong();
        this.width = in.readLong();
        this.videoResolution = in.readString();
    }

    public static final Creator<MediaItem> CREATOR = new Creator<MediaItem>() {
        @Override
        public MediaItem createFromParcel(Parcel source) {
            return new MediaItem(source);
        }

        @Override
        public MediaItem[] newArray(int size) {
            return new MediaItem[size];
        }
    };
}
