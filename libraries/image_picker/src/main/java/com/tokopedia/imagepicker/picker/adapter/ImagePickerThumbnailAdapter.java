package com.tokopedia.imagepicker.picker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.imagepicker.R;

import java.util.ArrayList;

/**
 * Created by hendry on 02/05/18.
 */

public class ImagePickerThumbnailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int PLACEHOLDER_TYPE = 0;
    public static final int ITEM_TYPE = 1;
    private final int thumbnailSize;
    private Context context;
    private ArrayList<String> imagePathList;
    private int maxSize;

    private OnImageEditThumbnailAdapterListener onImageEditThumbnailAdapterListener;
    private final float roundedSize;

    public interface OnImageEditThumbnailAdapterListener {
        void onPickerThumbnailItemClicked(String imagePath, int position);

        void onThumbnailRemoved(String imagePath);
    }

    public ImagePickerThumbnailAdapter(Context context, ArrayList<String> imagePathList,
                                       OnImageEditThumbnailAdapterListener onImageEditThumbnailAdapterListener) {
        this.context = context;
        this.imagePathList = imagePathList;
        this.onImageEditThumbnailAdapterListener = onImageEditThumbnailAdapterListener;
        roundedSize = context.getResources().getDimension(R.dimen.dp_6);
        thumbnailSize = context.getResources().getDimensionPixelOffset(R.dimen.dp_72);
    }

    public class ImagePickerThumbnailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView tvCounter;
        private ImageView ivDelete;

        public ImagePickerThumbnailViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            tvCounter = itemView.findViewById(R.id.tv_counter);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            itemView.setOnClickListener(this);
            ivDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v == ivDelete) {
                removeData(position);
            } else {
                // TODO drag and drop (long clicked?)
                // click on item
            }
        }

        public void bind(String imagePath, int position) {
            Glide.with(context)
                    .load(imagePath)
                    .asBitmap()
                    .override(thumbnailSize, thumbnailSize)
                    .centerCrop()
                    .into(new BitmapImageViewTarget(imageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(imageView.getContext().getResources(), resource);
                            circularBitmapDrawable.setCornerRadius(roundedSize);
                            imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            String positionString = String.valueOf(position + 1);
            tvCounter.setText(positionString);
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
        removeData(index);
    }

    public void removeData(int index) {
        if (index > -1) {
            String imagePath = this.imagePathList.remove(index);
            onImageEditThumbnailAdapterListener.onThumbnailRemoved(imagePath);
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
            ((ImagePickerThumbnailViewHolder) holder).bind(imagePath, position);
        } else {
            // draw the empty preview
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (imagePathList != null && position < imagePathList.size()) {
            return ITEM_TYPE;
        }
        return PLACEHOLDER_TYPE;
    }

    private boolean isItemType(int position) {
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
