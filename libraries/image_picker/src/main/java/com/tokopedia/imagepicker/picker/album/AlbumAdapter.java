package com.tokopedia.imagepicker.picker.album;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.gallery.model.AlbumItem;
import com.tokopedia.imagepicker.picker.main.adapter.RecyclerViewCursorAdapter;

import java.io.File;

/**
 * Created by hangnadi on 5/29/17.
 */

public class AlbumAdapter extends RecyclerViewCursorAdapter<AlbumAdapter.AlbumViewHolder> {

    private Context context;

    private OnAlbumAdapterListener onAlbumAdapterListener;
    public interface OnAlbumAdapterListener{
        void onAlbumClicked(AlbumItem albumItem, int position);
    }
    public AlbumAdapter(Context context, OnAlbumAdapterListener listener) {
        super(null);
        this.context = context;
        this.onAlbumAdapterListener = listener;
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
        holder.tvAlbumCount.setText(String.valueOf(albumItem.getCount()));

        // do not need to load animated Gif
        ImageHandler.loadImageFromFileFitCenter(
                context,
                holder.ivAlbumCover,
                new File(albumItem.getCoverPath())
        );
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_list_new_item, parent, false);
        return new AlbumViewHolder(v);
    }

}
