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

public class ImagePickerThumbnailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int PLACEHOLDER_TYPE = 0;
    public static final int ITEM_TYPE = 1;
    private Context context;
    private ArrayList<String> imagePathList;
    private int maxSize;

    private OnImageEditThumbnailAdapterListener onImageEditThumbnailAdapterListener;
    private final float roundedSize;

    public interface OnImageEditThumbnailAdapterListener {
        void onPickerThumbnailItemClicked(String imagePath, int position);
    }

    public ImagePickerThumbnailAdapter(Context context, ArrayList<String> imagePathList,
                                       OnImageEditThumbnailAdapterListener onImageEditThumbnailAdapterListener) {
        this.context = context;
        this.imagePathList = imagePathList;
        this.onImageEditThumbnailAdapterListener = onImageEditThumbnailAdapterListener;
        roundedSize = context.getResources().getDimension(R.dimen.dp_6);
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
        public void bind(String imagePath) {
            ImageHandler.loadImageRounded2(context, imageView, imagePath, roundedSize);
        }
    }

    public class PlaceholderThumbnailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public PlaceholderThumbnailViewHolder(View itemView) {
            super(itemView);
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

    public void addData(String imagePath) {
        if (imagePathList == null) {
            imagePathList = new ArrayList<>();
        }
        this.imagePathList.add(imagePath);
        notifyItemChanged(imagePathList.size() - 1);
    }

    public void removeData(String imagePath) {
        int index = this.imagePathList.indexOf(imagePath);
        if (index > -1) {
            this.imagePathList.remove(index);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        switch (viewType) {
            case ITEM_TYPE:
                view = inflater.inflate(R.layout.image_picker_thumbnail_item, parent, false);
                return new ImagePickerThumbnailViewHolder(view);
            default:
            case PLACEHOLDER_TYPE:
                view = inflater.inflate(R.layout.image_picker_placeholder_thumbnail_item, parent, false);
                return new PlaceholderThumbnailViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isItemType(position)) {
            String imagePath = imagePathList.get(position);
            ((ImagePickerThumbnailViewHolder)holder).bind(imagePath);
        } else {
            // draw the empty preview
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (imagePathList!= null && position < imagePathList.size()) {
            return ITEM_TYPE;
        }
        return PLACEHOLDER_TYPE;
    }

    private boolean isItemType(int position){
        return getItemViewType(position) == ITEM_TYPE;
    }

    @Override
    public int getItemCount() {
        if (maxSize > 0) {
            return maxSize;
        } else if (imagePathList == null) {
            return 0;
        } else {
            return imagePathList.size();
        }
    }

    public void setMaxData(int size) {
        this.maxSize = size;
    }

}
