package com.tokopedia.imagepicker.editor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.imagepicker.R;

import java.util.ArrayList;

/**
 * Created by hendry on 02/05/18.
 */

public class ImageEditThumbnailAdapter extends RecyclerView.Adapter<ImageEditThumbnailAdapter.ImageEditThumbnailViewHolder> {

    private Context context;
    private ArrayList<String> imagePathList;
    private RecyclerView recyclerView;

    private int selectedIndex = 0;

    private OnImageEditThumbnailAdapterListener onImageEditThumbnailAdapterListener;
    public interface OnImageEditThumbnailAdapterListener{
        void onThumbnailItemClicked(String imagePath, int position);
    }

    public ImageEditThumbnailAdapter(Context context, ArrayList<String> imagePathList, int imageIndex,
                                     OnImageEditThumbnailAdapterListener onImageEditThumbnailAdapterListener) {
        this.context = context;
        this.imagePathList = imagePathList;
        this.selectedIndex = imageIndex;
        this.onImageEditThumbnailAdapterListener = onImageEditThumbnailAdapterListener;
    }


    public class ImageEditThumbnailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private View rectGreenView;

        public ImageEditThumbnailViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            rectGreenView = itemView.findViewById(R.id.rect_green_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (selectedIndex!= position) {
                selectedIndex = position;
                notifyDataSetChanged();

                String imagePath = imagePathList.get(position);
                onImageEditThumbnailAdapterListener.onThumbnailItemClicked(imagePath, position);
            }
            /*if (recyclerView!=null) {
                recyclerView.smoothScrollToPosition(getAdapterPosition());
            }*/
        }
    }

    public void setData(ArrayList<String> imagePathList, int imageIndex) {
        this.imagePathList = imagePathList;
        this.selectedIndex = imageIndex;
        notifyDataSetChanged();
    }

    public void setSelectedIndex(int imageIndex) {
        if (imageIndex!=selectedIndex) {
            this.selectedIndex = imageIndex;
            notifyDataSetChanged();
        }
    }

    @Override
    public ImageEditThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_edit_thumbnail_item, parent, false);

        return new ImageEditThumbnailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageEditThumbnailViewHolder holder, int position) {
        String imagePath = imagePathList.get(position);
        ImageHandler.loadImageAndCache(holder.imageView, imagePath);
        holder.rectGreenView.setVisibility(selectedIndex == position ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        if (imagePathList == null) {
            return 0;
        } else {
            return imagePathList.size();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }
}
