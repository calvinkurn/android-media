package com.tokopedia.imagepicker.picker.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.adapter.ImagePickerThumbnailAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 24/05/18.
 */

public class ImagePickerPreviewWidget extends FrameLayout implements ImagePickerThumbnailAdapter.OnImageEditThumbnailAdapterListener {

    private ImagePickerThumbnailAdapter imagePickerThumbnailAdapter;

    private ImagePickerPreviewWidget.OnImagePickerThumbnailListWidgetListener onImagePickerThumbnailListWidgetListener;
    private RecyclerView recyclerView;

    public interface OnImagePickerThumbnailListWidgetListener {
        void onThumbnailItemLongClicked(String imagePath, int position);

        void onThumbnailItemClicked(String imagePath, int position);

        void afterThumbnailRemoved(int index);
    }

    public ImagePickerPreviewWidget(@NonNull Context context) {
        super(context);
        init();
    }

    public ImagePickerPreviewWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImagePickerPreviewWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImagePickerPreviewWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_image_picker_thumbnail_list,
                this, true);
        imagePickerThumbnailAdapter = new ImagePickerThumbnailAdapter(
                getContext(), null, null, this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(imagePickerThumbnailAdapter);

        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
    }

    public void setCanReorder(boolean canReorder) {
        imagePickerThumbnailAdapter.setCanReorder(canReorder);
    }

    public void setOnImagePickerThumbnailListWidgetListener(OnImagePickerThumbnailListWidgetListener onImagePickerThumbnailListWidgetListener) {
        this.onImagePickerThumbnailListWidgetListener = onImagePickerThumbnailListWidgetListener;
    }

    public void setData(List<String> imagePathList, boolean usePrimaryString,
                        List<Integer> placeholderDrawableList) {
        imagePickerThumbnailAdapter.setData(imagePathList, usePrimaryString, placeholderDrawableList);
    }

    public void addData(String imagePath) {
        imagePickerThumbnailAdapter.addData(imagePath);
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int position = imagePickerThumbnailAdapter.getImagePathList().size();
                recyclerView.smoothScrollToPosition(
                        position >= imagePickerThumbnailAdapter.getItemCount() ? position - 1 : position);
            }
        }, 1);
    }

    public void reorderPosition(int fromPosition, int toPosition) {
        List<String> imagePathList = this.imagePickerThumbnailAdapter.getImagePathList();
        if (fromPosition > -1) {
            String imagePathFrom = imagePathList.get(fromPosition);
            imagePathList.remove(fromPosition);

            imagePathList.add(toPosition, imagePathFrom);
            imagePickerThumbnailAdapter.notifyDataSetChanged();
        }
    }

    public int removeData(String imagePath) {
        final int position = imagePickerThumbnailAdapter.removeData(imagePath);
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.smoothScrollToPosition(position > 0 ? position - 1 : position);
            }
        }, 1);
        return position;
    }

    @Override
    public void onPickerThumbnailItemClicked(String imagePath, int position) {
        if (onImagePickerThumbnailListWidgetListener != null) {
            onImagePickerThumbnailListWidgetListener.onThumbnailItemClicked(imagePath, position);
        }
    }

    @Override
    public void onPickerThumbnailItemLongClicked(String imagePath, int position) {
        if (onImagePickerThumbnailListWidgetListener != null) {
            onImagePickerThumbnailListWidgetListener.onThumbnailItemLongClicked(imagePath, position);
        }
    }

    @Override
    public void onThumbnailRemoved(int index) {
        if (onImagePickerThumbnailListWidgetListener != null) {
            onImagePickerThumbnailListWidgetListener.afterThumbnailRemoved(index);
        }
    }

    public void setMaxAdapterSize(int size) {
        imagePickerThumbnailAdapter.setMaxData(size);
        imagePickerThumbnailAdapter.notifyDataSetChanged();
    }
}
