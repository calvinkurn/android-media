package com.tokopedia.core.gallery;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

/**
 * Created by hangnadi on 5/29/17.
 */

public class AlbumMediaLoader extends CursorLoader {
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.SIZE,
            "duration"};
    private static final String SELECTION_ALL =
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";
    private static final String[] SELECTION_ALL_ARGS = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
    };
    private static final String SELECTION_ALBUM =
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND "
                    + " bucket_id=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";
    private static String[] getSelectionAlbumArgs(String albumId) {
        return new String[] {
                String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
                albumId
        };
    }
    private static final String ORDER_BY = MediaStore.Files.FileColumns._ID + " DESC";

    private AlbumMediaLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs,
                             String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    public static CursorLoader newInstance(Context context, AlbumItem albumItem) {
        if (albumItem.isAll()) {
            return new AlbumMediaLoader(
                    context,
                    QUERY_URI,
                    PROJECTION,
                    SELECTION_ALL,
                    SELECTION_ALL_ARGS,
                    ORDER_BY
            );
        } else {
            return new AlbumMediaLoader(
                    context,
                    QUERY_URI,
                    PROJECTION,
                    SELECTION_ALBUM,
                    getSelectionAlbumArgs(albumItem.getmId()),
                    ORDER_BY
            );
        }
    }

    @Override
    public Cursor loadInBackground() {
        return super.loadInBackground();
    }

    @Override
    public void onContentChanged() {
        // FIXME a dirty way to fix loading multiple times
    }
}
