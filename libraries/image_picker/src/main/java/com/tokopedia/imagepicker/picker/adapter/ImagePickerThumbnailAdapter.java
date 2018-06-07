package com.tokopedia.imagepicker.picker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

public class ImagePickerThumbnailAdapter extends RecyclerView.Adapter<ImagePickerThumbnailAdapter.ImagePickerThumbnailViewHolder> {

    private Context context;
    private ArrayList<String> imagePathList;

    private OnImageEditThumbnailAdapterListener onImageEditThumbnailAdapterListener;

    public interface OnImageEditThumbnailAdapterListener {
        void onPickerThumbnailItemClicked(String imagePath, int position);
    }

    public ImagePickerThumbnailAdapter(Context context, ArrayList<String> imagePathList,
                                       OnImageEditThumbnailAdapterListener onImageEditThumbnailAdapterListener) {
        this.context = context;
        this.imagePathList = imagePathList;
        this.onImageEditThumbnailAdapterListener = onImageEditThumbnailAdapterListener;
    }


    public class ImagePickerThumbnailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;

        public ImagePickerThumbnailViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            //TODO onclick

        }
    }

    public void setData(ArrayList<String> imagePathList) {
        this.imagePathList = imagePathList;
        notifyDataSetChanged();
    }

    @Override
    public ImagePickerThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_picker_thumbnail_item, parent, false);
        return new ImagePickerThumbnailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImagePickerThumbnailViewHolder holder, int position) {
        String imagePath = imagePathList.get(position);
        ImageHandler.loadImageAndCache(holder.imageView, imagePath);
    }

    @Override
    public int getItemCount() {
        if (imagePathList == null) {
            return 0;
        } else {
            return imagePathList.size();
        }
    }

}
