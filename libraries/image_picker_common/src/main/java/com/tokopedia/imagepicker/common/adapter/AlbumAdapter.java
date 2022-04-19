package com.tokopedia.imagepicker.common.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.imagepicker.common.GalleryType;
import com.tokopedia.imagepicker.common.R;
import com.tokopedia.imagepicker.common.model.AlbumItem;

/**
 * Created by hangnadi on 5/29/17.
 */

public class AlbumAdapter extends RecyclerViewCursorAdapter<AlbumAdapter.AlbumViewHolder> {

    private Context context;
    private GalleryType galleryType;

    private OnAlbumAdapterListener onAlbumAdapterListener;
    public interface OnAlbumAdapterListener{
        void onAlbumClicked(AlbumItem albumItem, int position);
    }
    public AlbumAdapter(Context context, OnAlbumAdapterListener listener, GalleryType galleryType) {
        super(null);
        this.context = context;
        this.onAlbumAdapterListener = listener;
        this.galleryType = galleryType;
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivAlbumCover;
        TextView tvAlbumName;
        TextView tvAlbumCount;
        AlbumViewHolder(View itemView) {
            super(itemView);
            ivAlbumCover = itemView.findViewById(R.id.iv_album_cover);
            tvAlbumName = itemView.findViewById(R.id.tv_album_name);
            tvAlbumCount = itemView.findViewById(R.id.tv_album_count);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Cursor cursor = getCursor();
            int position = getAdapterPosition();
            cursor.moveToPosition(position);
            if (onAlbumAdapterListener!= null){
                onAlbumAdapterListener.onAlbumClicked(AlbumItem.valueOf(cursor), position);
            }
        }
    }

    @Override
    protected void onBindViewHolder(AlbumViewHolder holder, Cursor cursor) {
        AlbumItem albumItem = AlbumItem.valueOf(cursor);
        holder.tvAlbumName.setText(albumItem.getDisplayName(context));
        int resourseString;
        switch (galleryType) {
            case IMAGE_ONLY:
                resourseString = R.string.x_photos;
                break;
            case VIDEO_ONLY:
                resourseString = R.string.x_videos;
                break;
            default:
            case ALL:
                resourseString = R.string.x_media;
                break;
        }
        holder.tvAlbumCount.setText(context.getString(resourseString, albumItem.getCount()));

        // do not need to load animated Gif
        ImageHandler.loadImageFromUriFitCenter(
                context,
                holder.ivAlbumCover,
                albumItem.getCoverPath()
        );
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_list_new_item, parent, false);
        return new AlbumViewHolder(v);
    }

}
