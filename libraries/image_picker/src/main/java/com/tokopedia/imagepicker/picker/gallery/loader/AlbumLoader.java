package com.tokopedia.imagepicker.picker.gallery.loader;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.loader.content.CursorLoader;

import com.tokopedia.imagepicker.picker.gallery.model.AlbumItem;
import com.tokopedia.imagepicker.picker.gallery.model.MimeType;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;

import static com.tokopedia.imagepicker.common.util.ImageUtils.TOKOPEDIA_FOLDER_PREFIX;
import static com.tokopedia.imagepicker.picker.gallery.loader.AlbumMediaLoader.BUCKET_DISPLAY_NAME;
import static com.tokopedia.imagepicker.picker.gallery.loader.AlbumMediaLoader.BUCKET_ID;

/**
 * Created by hangnadi on 5/22/17.
 */

public class AlbumLoader extends CursorLoader {

    public static final String COLUMN_COUNT = "count";
    private static final String[] COLUMNS = {
            MediaStore.Files.FileColumns._ID,
            BUCKET_ID,
            BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.DATA,
            COLUMN_COUNT};

    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");

    // Get relevant columns for use later.
    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            BUCKET_ID,
            BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.DATA,
            "COUNT(*) AS " + COLUMN_COUNT
    };

    // we exclude TOKOPEDIA_FOLDER_PREFIX so the edit result and camera result will not show up.
    // the edit result are too much and not needed.

    // Return only video and image metadata.
    private static final String SELECTION =
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                    + " AND " + MediaStore.MediaColumns.MIME_TYPE + " != '" + MimeType.GIF.toString()+"' "
                    + " AND " + BUCKET_DISPLAY_NAME + " NOT LIKE '"+TOKOPEDIA_FOLDER_PREFIX+" %' "
                    + ") GROUP BY (bucket_id";

    // Return only video and image metadata.
    private static final String SELECTION_IMAGE_ONLY =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                    + " AND " + MediaStore.MediaColumns.MIME_TYPE + " != '" + MimeType.GIF.toString()+"' "
                    + " AND " + BUCKET_DISPLAY_NAME + " NOT LIKE '"+TOKOPEDIA_FOLDER_PREFIX+" %' "
                    + ") GROUP BY (bucket_id";

    private static final String SELECTION_VIDEO_ONLY =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                    + ") GROUP BY (bucket_id";

    private static final String[] SELECTION_ARGS = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
    };

    private static final String[] SELECTION_ARGS_IMAGE_ONLY = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
    };

    private static final String[] SELECTION_ARGS_VIDEO_ONLY = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
    };

    private static final String BUCKET_ORDER_BY = "datetaken DESC";

    public AlbumLoader(Context context, String selection, String[] selectionArgs) {
        super(context, QUERY_URI, PROJECTION, SELECTION, selectionArgs, BUCKET_ORDER_BY);
    }

    public static CursorLoader createInstance(Context context, int galleryType) {
        String[] selectionArgs;
        String selection;
        if (galleryType == GalleryType.IMAGE_ONLY) {
            selection = SELECTION_IMAGE_ONLY;
            selectionArgs = SELECTION_ARGS_IMAGE_ONLY;
        } else if (galleryType == GalleryType.VIDEO_ONLY) {
            selection = SELECTION_VIDEO_ONLY;
            selectionArgs = SELECTION_ARGS_VIDEO_ONLY;
        } else {
            selection = SELECTION;
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
}
