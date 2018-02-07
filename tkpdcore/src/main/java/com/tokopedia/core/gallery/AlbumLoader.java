package com.tokopedia.core.gallery;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

/**
 * Created by hangnadi on 5/22/17.
 */

public class AlbumLoader extends CursorLoader {

    public static final String COLUMN_COUNT = "count";
    private static final String[] COLUMNS = {
            MediaStore.Files.FileColumns._ID,
            "bucket_id",
            "bucket_display_name",
            MediaStore.MediaColumns.DATA,
            COLUMN_COUNT};

    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");

    // Get relevant columns for use later.
    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            "bucket_id",
            "bucket_display_name",
            MediaStore.MediaColumns.DATA,
            "COUNT(*) AS " + COLUMN_COUNT
    };

    // Return only video and image metadata.
    private static final String SELECTION =
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
            + " OR "
            + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
            + " AND " + MediaStore.MediaColumns.SIZE + ">0"
            + ") GROUP BY (bucket_id";

    private static final String[] SELECTION_ARGS = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
    };

    private static final String BUCKET_ORDER_BY = "datetaken DESC";

    public AlbumLoader(Context context, String[] selectionArgs) {
        super(context, QUERY_URI, PROJECTION, SELECTION, selectionArgs, BUCKET_ORDER_BY);
    }

    public static CursorLoader createInstance(Context context, int galleryType){
        String[] selectionArgs;
        if(galleryType == GalleryType.ofImageOnly()){
            selectionArgs = new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)};
        }else{
            selectionArgs = SELECTION_ARGS;
        }
        return new AlbumLoader(context, selectionArgs);
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
        allAlbum.addRow(new String[]{AlbumItem.ALBUM_ID_ALL, AlbumItem.ALBUM_ID_ALL, AlbumItem.ALBUM_NAME_ALL, allAlbumCoverPath,
                String.valueOf(totalCount)});

        return new MergeCursor(new Cursor[]{allAlbum, albums});
    }

    @Override
    public void onContentChanged() {

    }
}
