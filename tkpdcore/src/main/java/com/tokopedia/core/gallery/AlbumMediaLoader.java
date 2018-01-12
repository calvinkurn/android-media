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
    public static final String BUCKET_ID = "bucket_id";
    public static final String BUCKET_DISPLAY_NAME = "bucket_display_name";
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.SIZE,
            "duration"};
    private static final String SELECTION_ALL =
            String.format("(%s=? OR %s=?) AND %s>0",
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.MediaColumns.SIZE
            );
    private static final String SELECTION_ALL_IMAGE_ONLY =
            String.format("(%s=?) AND %s>0",
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.MediaColumns.SIZE
            );

    private static final String[] SELECTION_ALL_ARGS = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
    };
    private static final String SELECTION_ALBUM =
            String.format("(%s=? OR %s=?) AND %s=? AND %s>0",
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    BUCKET_ID,
                    MediaStore.MediaColumns.SIZE
            );

    private static final String SELECTION_ALBUM_IMAGE_ONLY =
            String.format("(%s=?) AND %s=? AND %s>0",
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    BUCKET_ID,
                    MediaStore.MediaColumns.SIZE
            );

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

    public static CursorLoader newInstance(Context context, AlbumItem albumItem, int galeryType) {
        String[] selectionArgs;
        String selectionAlbum;
        if(galeryType ==GalleryType.ofImageOnly()){
            if (albumItem.isAll()) {
                selectionArgs = new String[] {
                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
                };
                selectionAlbum = SELECTION_ALL_IMAGE_ONLY;
            }else{
                selectionArgs = new String[] {
                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                        albumItem.getmId()
                };
                selectionAlbum = SELECTION_ALBUM_IMAGE_ONLY;
            }
        }else {
            if (albumItem.isAll()) {
                selectionArgs = SELECTION_ALL_ARGS;
                selectionAlbum = SELECTION_ALL;
            } else {
                selectionArgs = getSelectionAlbumArgs(albumItem.getmId());
                selectionAlbum = SELECTION_ALBUM;
            }
        }
        return new AlbumMediaLoader(context,
                QUERY_URI,
                PROJECTION,
                selectionAlbum,
                selectionArgs,
                ORDER_BY);
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
