package com.tokopedia.imagepicker.picker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
    private ArrayList<Integer> placeholderDrawableResList;
    private int maxSize;
    private @StringRes
    int primaryImageStringRes;

    private OnImageEditThumbnailAdapterListener onImageEditThumbnailAdapterListener;
    private final float roundedSize;

    public interface OnImageEditThumbnailAdapterListener {
        void onPickerThumbnailItemClicked(String imagePath, int position);

        void onThumbnailRemoved(int index);
    }

    public ImagePickerThumbnailAdapter(Context context, ArrayList<String> imagePathList, ArrayList<Integer> placeholderDrawableResList,
                                       OnImageEditThumbnailAdapterListener onImageEditThumbnailAdapterListener) {
        this.context = context;
        this.imagePathList = imagePathList;
        this.placeholderDrawableResList = placeholderDrawableResList;
        this.onImageEditThumbnailAdapterListener = onImageEditThumbnailAdapterListener;
        roundedSize = context.getResources().getDimension(R.dimen.dp_6);
        thumbnailSize = context.getResources().getDimensionPixelOffset(R.dimen.dp_72);
    }

    public class ImagePickerThumbnailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView tvCounter;
        private TextView tvCounterPrimary;
        private ImageView ivDelete;

        public ImagePickerThumbnailViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            tvCounter = itemView.findViewById(R.id.tv_counter);
            tvCounterPrimary = itemView.findViewById(R.id.tv_counter_primary);
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
            if (position == 0 && primaryImageStringRes > 0) {
                tvCounterPrimary.setText(primaryImageStringRes);
                tvCounterPrimary.setVisibility(View.VISIBLE);
                tvCounter.setVisibility(View.GONE);
            } else {
                String positionString = String.valueOf(position + 1);
                tvCounter.setText(positionString);
                tvCounterPrimary.setVisibility(View.GONE);
                tvCounter.setVisibility(View.VISIBLE);
            }
        }
    }

    public class PlaceholderThumbnailViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPlaceholder;
        public PlaceholderThumbnailViewHolder(View itemView) {
            super(itemView);
            ivPlaceholder = itemView.findViewById(R.id.image_view_placeholder);
        }

        public void bind(@DrawableRes int drawableRes) {
            ivPlaceholder.setImageResource(drawableRes);
        }
    }

    public void setData(ArrayList<String> imagePathList, @StringRes int primaryImageStringRes,
                        ArrayList<Integer> placeholderDrawableList) {
        this.imagePathList = imagePathList;
        this.primaryImageStringRes = primaryImageStringRes;
        this.placeholderDrawableResList = placeholderDrawableList;
        notifyDataSetChanged();
    }

    public void addData(String imagePath) {
        if (imagePathList == null) {
            imagePathList = new ArrayList<>();
        }
        if (!imagePathList.contains(imagePath)) {
            this.imagePathList.add(imagePath);
            notifyItemChanged(imagePathList.size() - 1);
        }
    }

    public int removeData(String imagePath) {
        int index = this.imagePathList.indexOf(imagePath);
        removeData(index);
        return index;
    }

    public void removeData(int index) {
        if (index > -1) {
            this.imagePathList.remove(index);
            onImageEditThumbnailAdapterListener.onThumbnailRemoved(index);
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
            // else draw the empty preview
            if (placeholderDrawableResList!= null && placeholderDrawableResList.size() > position) {
                int drawableRes = placeholderDrawableResList.get(position);
                ((PlaceholderThumbnailViewHolder)holder).bind(drawableRes);
            } else {
                ((PlaceholderThumbnailViewHolder)holder).bind(R.drawable.ic_loading_toped_new);
            }
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
