package com.tokopedia.imagepicker.picker.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.adapter.ImagePickerThumbnailAdapter;

import java.util.ArrayList;

/**
 * Created by hendry on 24/05/18.
 */

public class ImagePickerPreviewWidget extends FrameLayout implements ImagePickerThumbnailAdapter.OnImageEditThumbnailAdapterListener {

    private ImagePickerThumbnailAdapter imagePickerThumbnailAdapter;

    private ImagePickerPreviewWidget.OnImagePickerThumbnailListWidgetListener onImagePickerThumbnailListWidgetListener;

    public interface OnImagePickerThumbnailListWidgetListener {
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
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(imagePickerThumbnailAdapter);

        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
    }

    public void setOnImagePickerThumbnailListWidgetListener(OnImagePickerThumbnailListWidgetListener onImagePickerThumbnailListWidgetListener) {
        this.onImagePickerThumbnailListWidgetListener = onImagePickerThumbnailListWidgetListener;
    }

    public void setData(ArrayList<String> imagePathList, @StringRes int primaryImageStringRes,
                        ArrayList<Integer> placeholderDrawableList) {
        imagePickerThumbnailAdapter.setData(imagePathList, primaryImageStringRes, placeholderDrawableList);
    }

    public void addData(String imagePath){
        imagePickerThumbnailAdapter.addData(imagePath);
    }

    public int removeData(String imagePath){
        return imagePickerThumbnailAdapter.removeData(imagePath);
    }

    @Override
    public void onPickerThumbnailItemClicked(String imagePath, int position) {
        if (onImagePickerThumbnailListWidgetListener!= null) {
            onImagePickerThumbnailListWidgetListener.onThumbnailItemClicked(imagePath, position);
        }
    }

    @Override
    public void onThumbnailRemoved(int index) {
        if (onImagePickerThumbnailListWidgetListener!= null) {
            onImagePickerThumbnailListWidgetListener.afterThumbnailRemoved(index);
        }
    }

    public void setMaxAdapterSize(int size) {
        imagePickerThumbnailAdapter.setMaxData(size);
        imagePickerThumbnailAdapter.notifyDataSetChanged();
    }
}
