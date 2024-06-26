package com.tokopedia.imagepicker.common.loader;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.loader.content.CursorLoader;

import com.tokopedia.imagepicker.common.GalleryType;
import com.tokopedia.imagepicker.common.RemoteConfigInstance;
import com.tokopedia.imagepicker.common.internal.entity.Album;
import com.tokopedia.imagepicker.common.model.MimeType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Created by hangnadi on 5/22/17.
 */

public class AlbumLoader extends CursorLoader {

    private static final String TOKOPEDIA_FOLDER_PREFIX = "Tokopedia";
    private static final String COLUMN_BUCKET_ID = "bucket_id";
    private static final String COLUMN_BUCKET_DISPLAY_NAME = "bucket_display_name";
    public static final String COLUMN_URI = "uri";
    public static final String COLUMN_COUNT = "count";
    public static final String REMOTE_CONFIG_ALBUM_LOAD_NEW = "android_album_load_new";
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
    private static final String SELECTION_NO_MEDIA =
            MediaStore.MediaColumns.SIZE + ">0"
                    + " AND " + AlbumMediaLoader.BUCKET_DISPLAY_NAME + " NOT LIKE '" + TOKOPEDIA_FOLDER_PREFIX + " %' "
                    + ") GROUP BY (bucket_id";
    private static final String SELECTION =
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + SELECTION_NO_MEDIA;
    private static final String SELECTION_29_NO_MEDIA =
            MediaStore.MediaColumns.SIZE + ">0";
    private static final String SELECTION_29 =
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + SELECTION_29_NO_MEDIA;
    private static final String[] SELECTION_ARGS = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
    };
    // =============================================

    // === params for showSingleMediaType: true ===
    private static final String SELECTION_FOR_SINGLE_MEDIA_TYPE_NO_MEDIA =
            MediaStore.MediaColumns.SIZE + ">0"
                    + " AND " + AlbumMediaLoader.BUCKET_DISPLAY_NAME + " NOT LIKE '" + TOKOPEDIA_FOLDER_PREFIX + " %' "
                    + ") GROUP BY (bucket_id";
    private static final String SELECTION_FOR_SINGLE_MEDIA_TYPE =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + SELECTION_FOR_SINGLE_MEDIA_TYPE_NO_MEDIA;
    private static final String SELECTION_FOR_SINGLE_MEDIA_TYPE_29_NO_MEDIA =
            MediaStore.MediaColumns.SIZE + ">0"
                    + " AND " + AlbumMediaLoader.BUCKET_DISPLAY_NAME + " NOT LIKE '" + TOKOPEDIA_FOLDER_PREFIX + " %' ";
    private static final String SELECTION_FOR_SINGLE_MEDIA_TYPE_29 =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + SELECTION_FOR_SINGLE_MEDIA_TYPE_29_NO_MEDIA;

    private static String[] getSelectionArgsForSingleMediaType(int mediaType) {
        return new String[]{String.valueOf(mediaType)};
    }
    // =============================================

    // === params for showSingleMediaType: true ===
    private static final String SELECTION_FOR_SINGLE_MEDIA_GIF_TYPE_NO_MEDIA =
            MediaStore.MediaColumns.SIZE + ">0"
                    + " AND " + MediaStore.MediaColumns.MIME_TYPE + "=?"
                    + " AND " + AlbumMediaLoader.BUCKET_DISPLAY_NAME + " NOT LIKE '" + TOKOPEDIA_FOLDER_PREFIX + " %' "
                    + ") GROUP BY (bucket_id";
    private static final String SELECTION_FOR_SINGLE_MEDIA_GIF_TYPE =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + SELECTION_FOR_SINGLE_MEDIA_GIF_TYPE_NO_MEDIA;
    private static final String SELECTION_FOR_SINGLE_MEDIA_GIF_TYPE_29_NO_MEDIA =
            MediaStore.MediaColumns.SIZE + ">0"
                    + " AND " + MediaStore.MediaColumns.MIME_TYPE + "=?"
                    + " AND " + AlbumMediaLoader.BUCKET_DISPLAY_NAME + " NOT LIKE '" + TOKOPEDIA_FOLDER_PREFIX + " %' ";

    private static final String SELECTION_FOR_SINGLE_MEDIA_GIF_TYPE_29 =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + SELECTION_FOR_SINGLE_MEDIA_GIF_TYPE_29_NO_MEDIA;

    private static String[] getSelectionArgsForSingleMediaGifType(int mediaType) {
        return new String[]{String.valueOf(mediaType), "image/gif"};
    }
    // =============================================

    private static final String BUCKET_ORDER_BY = "datetaken DESC";

    public AlbumLoader(Context context, Uri uri, String selection, String[] selectionArgs) {
        super(
                context,
                uri,
                beforeAndroidTen() ? PROJECTION : PROJECTION_29,
                selection,
                selectionArgs,
                BUCKET_ORDER_BY
        );
    }

    public static CursorLoader newInstance(Context context, GalleryType galleryType) {
        boolean useNewLogic = RemoteConfigInstance.INSTANCE.getRemoteConfig(context).getBoolean(REMOTE_CONFIG_ALBUM_LOAD_NEW, true);
        if (useNewLogic) {
            return newInstanceNew(context, galleryType);
        } else {
            return newInstanceOld(context, galleryType);
        }
    }

    public static CursorLoader newInstanceOld(Context context, GalleryType galleryType) {
        String selection;
        String[] selectionArgs;
        if (galleryType == GalleryType.GIF_ONLY) {
            selection = beforeAndroidTen()
                    ? SELECTION_FOR_SINGLE_MEDIA_GIF_TYPE : SELECTION_FOR_SINGLE_MEDIA_GIF_TYPE_29;
            selectionArgs = getSelectionArgsForSingleMediaGifType(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
        } else if (galleryType == GalleryType.IMAGE_ONLY) {
            selection = beforeAndroidTen()
                    ? SELECTION_FOR_SINGLE_MEDIA_TYPE : SELECTION_FOR_SINGLE_MEDIA_TYPE_29;
            selectionArgs = getSelectionArgsForSingleMediaType(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
        } else if (galleryType == GalleryType.VIDEO_ONLY) {
            selection = beforeAndroidTen()
                    ? SELECTION_FOR_SINGLE_MEDIA_TYPE : SELECTION_FOR_SINGLE_MEDIA_TYPE_29;
            selectionArgs = getSelectionArgsForSingleMediaType(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
        } else {
            selection = beforeAndroidTen() ? SELECTION : SELECTION_29;
            selectionArgs = SELECTION_ARGS;
        }
        return new AlbumLoader(context, QUERY_URI, selection, selectionArgs);
    }

    public static CursorLoader newInstanceNew(Context context, GalleryType galleryType) {
        String selection;
        String[] selectionArgs;
        Uri uri = QUERY_URI;
        if (galleryType == GalleryType.GIF_ONLY) {
            uri = getUriImageCheckOS();
            selection = beforeAndroidTen()
                    ? SELECTION_FOR_SINGLE_MEDIA_GIF_TYPE_NO_MEDIA : SELECTION_FOR_SINGLE_MEDIA_GIF_TYPE_29_NO_MEDIA;
            selectionArgs = new String[]{"image/gif"};
        } else if (galleryType == GalleryType.IMAGE_ONLY) {
            uri = getUriImageCheckOS();
            selection = beforeAndroidTen()
                    ? SELECTION_FOR_SINGLE_MEDIA_TYPE_NO_MEDIA : SELECTION_FOR_SINGLE_MEDIA_TYPE_29_NO_MEDIA;
            selectionArgs = new String[]{};
        } else if (galleryType == GalleryType.VIDEO_ONLY) {
            selection = beforeAndroidTen()
                    ? SELECTION_FOR_SINGLE_MEDIA_TYPE_NO_MEDIA : SELECTION_FOR_SINGLE_MEDIA_TYPE_29_NO_MEDIA;
            selectionArgs = new String[]{};
            uri = getUriVideoCheckOS();
        } else {
            selection = beforeAndroidTen() ? SELECTION : SELECTION_29;
            selectionArgs = SELECTION_ARGS;
        }
        return new AlbumLoader(context, uri, selection, selectionArgs);
    }

    private static Uri getUriImageCheckOS() {
        Uri uri;
        if (beforeAndroidTen()) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else {
            uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        }
        return uri;
    }

    private static Uri getUriVideoCheckOS() {
        Uri uri;
        if (beforeAndroidTen()) {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else {
            uri = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        }
        return uri;
    }


    @Override
    public Cursor loadInBackground() {
        Cursor albums = super.loadInBackground();
        MatrixCursor allAlbum = new MatrixCursor(COLUMNS);

        if (beforeAndroidTen()) {
            int totalCount = 0;
            Uri allAlbumCoverUri = null;
            MatrixCursor otherAlbums = new MatrixCursor(COLUMNS);
            if (albums != null) {
                while (albums.moveToNext()) {
                    long fileId = albums.getLong(
                            albums.getColumnIndex(MediaStore.Files.FileColumns._ID));
                    long bucketId = albums.getLong(
                            albums.getColumnIndex(COLUMN_BUCKET_ID));
                    String bucketDisplayName = albums.getString(
                            albums.getColumnIndex(COLUMN_BUCKET_DISPLAY_NAME));
                    String mimeType = albums.getString(
                            albums.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
                    Uri uri = getUri(albums);
                    int count = albums.getInt(albums.getColumnIndex(COLUMN_COUNT));

                    otherAlbums.addRow(new String[]{
                            Long.toString(fileId),
                            Long.toString(bucketId), bucketDisplayName, mimeType, uri.toString(),
                            String.valueOf(count)});
                    totalCount += count;
                }
                if (albums.moveToFirst()) {
                    allAlbumCoverUri = getUri(albums);
                }
            }

            allAlbum.addRow(new String[]{
                    Album.ALBUM_ID_ALL, Album.ALBUM_ID_ALL, Album.ALBUM_NAME_ALL, null,
                    allAlbumCoverUri == null ? null : allAlbumCoverUri.toString(),
                    String.valueOf(totalCount)});

            return new MergeCursor(new Cursor[]{allAlbum, otherAlbums});
        } else {
            int totalCount = 0;
            Uri allAlbumCoverUri = null;

            // Pseudo GROUP BY
            Map<Long, Long> countMap = new HashMap<>();
            if (albums != null) {
                while (albums.moveToNext()) {
                    long bucketId = albums.getLong(albums.getColumnIndex(COLUMN_BUCKET_ID));

                    Long count = countMap.get(bucketId);
                    if (count == null) {
                        count = 1L;
                    } else {
                        count++;
                    }
                    countMap.put(bucketId, count);
                }
            }

            MatrixCursor otherAlbums = new MatrixCursor(COLUMNS);
            if (albums != null) {
                if (albums.moveToFirst()) {
                    allAlbumCoverUri = getUri(albums);

                    Set<Long> done = new HashSet<>();

                    do {
                        long bucketId = albums.getLong(albums.getColumnIndex(COLUMN_BUCKET_ID));

                        if (done.contains(bucketId)) {
                            continue;
                        }

                        long fileId = albums.getLong(
                                albums.getColumnIndex(MediaStore.Files.FileColumns._ID));
                        String bucketDisplayName = albums.getString(
                                albums.getColumnIndex(COLUMN_BUCKET_DISPLAY_NAME));
                        String mimeType = albums.getString(
                                albums.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
                        Uri uri = getUri(albums);
                        long count = countMap.get(bucketId);

                        otherAlbums.addRow(new String[]{
                                Long.toString(fileId),
                                Long.toString(bucketId),
                                bucketDisplayName,
                                mimeType,
                                uri.toString(),
                                String.valueOf(count)});
                        done.add(bucketId);

                        totalCount += count;
                    } while (albums.moveToNext());
                }
            }

            allAlbum.addRow(new String[]{
                    Album.ALBUM_ID_ALL,
                    Album.ALBUM_ID_ALL, Album.ALBUM_NAME_ALL, null,
                    allAlbumCoverUri == null ? null : allAlbumCoverUri.toString(),
                    String.valueOf(totalCount)});

            return new MergeCursor(new Cursor[]{allAlbum, otherAlbums});
        }
    }

    private static Uri getUri(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
        String mimeType = cursor.getString(
                cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
        Uri contentUri;

        if (MimeType.isImage(mimeType)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if (MimeType.isVideo(mimeType)) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else {
            // ?
            contentUri = MediaStore.Files.getContentUri("external");
        }

        return ContentUris.withAppendedId(contentUri, id);
    }

    @Override
    public void onContentChanged() {
        // FIXME a dirty way to fix loading multiple times
    }


    /**
     * @return 是否是 Android 10 （Q） 之前的版本
     */
    private static boolean beforeAndroidTen() {
        return android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.Q;
    }
}
