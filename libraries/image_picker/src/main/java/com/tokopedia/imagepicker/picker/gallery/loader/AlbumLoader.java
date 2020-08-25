package com.tokopedia.imagepicker.picker.gallery.loader;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.loader.content.CursorLoader;

import com.tokopedia.imagepicker.picker.gallery.internal.entity.SelectionSpec;
import com.tokopedia.imagepicker.picker.gallery.model.AlbumItem;

/**
 * Created by hangnadi on 5/22/17.
 */

public class AlbumLoader extends CursorLoader {

    private static final String COLUMN_BUCKET_ID = "bucket_id";
    private static final String COLUMN_BUCKET_DISPLAY_NAME = "bucket_display_name";
    public static final String COLUMN_URI = "uri";
    public static final String COLUMN_COUNT = "count";
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");

    private static final String[] COLUMNS = {
            MediaStore.Files.FileColumns._ID,
            COLUMN_BUCKET_ID,
            COLUMN_BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            COLUMN_URI,
            COLUMN_COUNT};

    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            COLUMN_BUCKET_ID,
            COLUMN_BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            "COUNT(*) AS " + COLUMN_COUNT};

    private static final String[] PROJECTION_29 = {
            MediaStore.Files.FileColumns._ID,
            COLUMN_BUCKET_ID,
            COLUMN_BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE};

    // === params for showSingleMediaType: false ===
    private static final String SELECTION =
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                    + ") GROUP BY (bucket_id";
    private static final String SELECTION_29 =
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";
    private static final String[] SELECTION_ARGS = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
    };
    // =============================================

    // === params for showSingleMediaType: true ===
    private static final String SELECTION_FOR_SINGLE_MEDIA_TYPE =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                    + ") GROUP BY (bucket_id";
    private static final String SELECTION_FOR_SINGLE_MEDIA_TYPE_29 =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";

    private static String[] getSelectionArgsForSingleMediaType(int mediaType) {
        return new String[]{String.valueOf(mediaType)};
    }
    // =============================================

    // === params for showSingleMediaType: true ===
    private static final String SELECTION_FOR_SINGLE_MEDIA_GIF_TYPE =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                    + " AND " + MediaStore.MediaColumns.MIME_TYPE + "=?"
                    + ") GROUP BY (bucket_id";
    private static final String SELECTION_FOR_SINGLE_MEDIA_GIF_TYPE_29 =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                    + " AND " + MediaStore.MediaColumns.MIME_TYPE + "=?";

    private static String[] getSelectionArgsForSingleMediaGifType(int mediaType) {
        return new String[]{String.valueOf(mediaType), "image/gif"};
    }
    // =============================================

    private static final String BUCKET_ORDER_BY = "datetaken DESC";

    public AlbumLoader(Context context, String selection, String[] selectionArgs) {
        super(
                context,
                QUERY_URI,
                beforeAndroidTen() ? PROJECTION : PROJECTION_29,
                selection,
                selectionArgs,
                BUCKET_ORDER_BY
        );
    }

    public static CursorLoader newInstance(Context context) {
        String selection;
        String[] selectionArgs;
        if (SelectionSpec.getInstance().onlyShowGif()) {
            selection = beforeAndroidTen()
                    ? SELECTION_FOR_SINGLE_MEDIA_GIF_TYPE : SELECTION_FOR_SINGLE_MEDIA_GIF_TYPE_29;
            selectionArgs = getSelectionArgsForSingleMediaGifType(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
        } else if (SelectionSpec.getInstance().onlyShowImages()) {
            selection = beforeAndroidTen()
                    ? SELECTION_FOR_SINGLE_MEDIA_TYPE : SELECTION_FOR_SINGLE_MEDIA_TYPE_29;
            selectionArgs = getSelectionArgsForSingleMediaType(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
        } else if (SelectionSpec.getInstance().onlyShowVideos()) {
            selection = beforeAndroidTen()
                    ? SELECTION_FOR_SINGLE_MEDIA_TYPE : SELECTION_FOR_SINGLE_MEDIA_TYPE_29;
            selectionArgs = getSelectionArgsForSingleMediaType(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
        } else {
            selection = beforeAndroidTen() ? SELECTION : SELECTION_29;
            selectionArgs = SELECTION_ARGS;
        }
        return new AlbumLoader(context, selection, selectionArgs);
    }



    @Override
    public Cursor loadInBackground() {
        Cursor albums = super.loadInBackground();
        MatrixCursor allAlbum = new MatrixCursor(COLUMNS);
        int totalCount = 0;
        String allAlbumCoverPath = "";
        if (albums != null) {
            while (albums.moveToNext()) {
                totalCount += albums.getInt(albums.getColumnIndex(COLUMN_COUNT));
            }
            if (albums.moveToFirst()) {
                allAlbumCoverPath = albums.getString(albums.getColumnIndex(MediaStore.MediaColumns.DATA));
            }
        }
        allAlbum.addRow(new String[]{AlbumItem.ALBUM_ID_ALL, AlbumItem.ALBUM_ID_ALL, AlbumItem.ALBUM_NAME_ALL,
                allAlbumCoverPath,
                String.valueOf(totalCount)});

        return new MergeCursor(new Cursor[]{allAlbum, albums});
    }

    @Override
    public void onContentChanged() {

    }

    /**
     * @return 是否是 Android 10 （Q） 之前的版本
     */
    private static boolean beforeAndroidTen() {
        return android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.Q;
    }
}
