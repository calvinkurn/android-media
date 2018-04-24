package com.tokopedia.imagepicker.picker.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.gallery.model.AlbumItem;

import java.io.File;

/**
 * Created by hangnadi on 5/26/17.
 */

public class AlbumAdapter extends CursorAdapter {

    public AlbumAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.album_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        AlbumItem albumItem = AlbumItem.valueOf(cursor);
        ((TextView) view.findViewById(R.id.album_name)).setText(albumItem.getDisplayName(context));
        ((TextView) view.findViewById(R.id.album_media_count)).setText(String.valueOf(albumItem.getCount()));

        // do not need to load animated Gif
        ImageHandler.loadImageFromFileFitCenter(
                context,
                (ImageView) view.findViewById(R.id.album_cover),
                new File(albumItem.getCoverPath())
        );
    }
}
