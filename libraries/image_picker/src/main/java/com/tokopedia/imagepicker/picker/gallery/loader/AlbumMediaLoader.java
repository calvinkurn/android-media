package com.tokopedia.imagepicker.picker.gallery.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.loader.content.CursorLoader;

import com.tokopedia.imagepicker.picker.gallery.model.AlbumItem;
import com.tokopedia.imagepicker.picker.gallery.model.MimeType;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;

import static com.tokopedia.imagepicker.common.util.ImageUtils.TOKOPEDIA_FOLDER_PREFIX;

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
            MediaStore.Video.VideoColumns.DURATION,
            MediaStore.Video.VideoColumns.RESOLUTION};

    // we exclude TOKOPEDIA_FOLDER_PREFIX so the edit result and camera result will not show up.
    // the edit result are too much and not needed.

    // media type [image] or media type [video] AND size > 0 and mime type not [gif]
    private static final String SELECTION_ALL =
            String.format("(%s=? OR %s=?) AND %s>0 AND %s!=? AND %s NOT LIKE '"+TOKOPEDIA_FOLDER_PREFIX +" %%' ",
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.MediaColumns.SIZE,
                    MediaStore.MediaColumns.MIME_TYPE,
                    BUCKET_DISPLAY_NAME
            );

    private static final String[] SELECTION_ALL_ARGS = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
            MimeType.GIF.toString()
    };

    // media type [image] AND size > 0 and mime type not [gif]
    private static final String SELECTION_ALL_IMAGE_ONLY =
            String.format("(%s=?) AND %s>0 AND %s!=? AND %s NOT LIKE '"+TOKOPEDIA_FOLDER_PREFIX +" %%' " ,
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.MediaColumns.SIZE,
                    MediaStore.MediaColumns.MIME_TYPE,
                    BUCKET_DISPLAY_NAME
            );

    private static final String SELECTION_ALL_VIDEO_ONLY =
            String.format("(%s=?) AND %s>0 " ,
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.MediaColumns.SIZE
            );

    private static final String[] SELECTION_IMAGE_ONLY_ARGS = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            MimeType.GIF.toString()
    };

    private static final String[] SELECTION_VIDEO_ONLY_ARGS = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
    };

    private static final String SELECTION_ALBUM =
            String.format("(%s=? OR %s=?) AND %s=? AND %s>0 AND %s NOT LIKE '"+TOKOPEDIA_FOLDER_PREFIX +" %%' ",
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    BUCKET_ID,
                    MediaStore.MediaColumns.SIZE,
                    BUCKET_DISPLAY_NAME
            );

    // MediaType = [IMAGE] AND id = [ALBUM_ID]; no need to check gif type
    private static final String SELECTION_ALBUM_IMAGE_ONLY =
            String.format("%s=? AND %s=? AND %s>0 AND %s NOT LIKE '"+TOKOPEDIA_FOLDER_PREFIX +" %%' ",
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    BUCKET_ID,
                    MediaStore.MediaColumns.SIZE,
                    BUCKET_DISPLAY_NAME
            );

    private static final String SELECTION_ALBUM_VIDEO_ONLY =
            String.format("%s=? AND %s=? AND %s>0 ",
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
        String selectionString;
        if(galeryType == GalleryType.IMAGE_ONLY){
            if (albumItem.isAll()) {
                selectionArgs = SELECTION_IMAGE_ONLY_ARGS;
                selectionString = SELECTION_ALL_IMAGE_ONLY;
            }else{
                selectionArgs = new String[] {
                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                        albumItem.getmId()
                };
                selectionString = SELECTION_ALBUM_IMAGE_ONLY;
            }
        }else if(galeryType == GalleryType.VIDEO_ONLY){
            if (albumItem.isAll()) {
                selectionArgs = SELECTION_VIDEO_ONLY_ARGS;
                selectionString = SELECTION_ALL_VIDEO_ONLY;
            }else{
                selectionArgs = new String[] {
                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
                        albumItem.getmId()
                };
                selectionString = SELECTION_ALBUM_VIDEO_ONLY;
            }
        }else {
            if (albumItem.isAll()) {
                selectionArgs = SELECTION_ALL_ARGS;
                selectionString = SELECTION_ALL;
            } else {
                selectionArgs = getSelectionAlbumArgs(albumItem.getmId());
                selectionString = SELECTION_ALBUM;
            }
        }
        return new AlbumMediaLoader(context,
                QUERY_URI,
                PROJECTION,
                selectionString,
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
