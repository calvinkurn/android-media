package com.tokopedia.imagepicker.editor.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.editor.adapter.ImageEditThumbnailAdapter;

import java.util.ArrayList;

/**
 * Created by hendry on 30/04/18.
 */

public class ImageEditThumbnailListWidget extends FrameLayout implements ImageEditThumbnailAdapter.OnImageEditThumbnailAdapterListener {
    private ImageEditThumbnailAdapter imageEditThumbnailAdapter;

    private OnImageEditThumbnailListWidgetListener OnImageEditThumbnailListWidgetListener;
    public interface OnImageEditThumbnailListWidgetListener {
        void onThumbnailItemClicked(String imagePath, int position);
    }

    public ImageEditThumbnailListWidget(@NonNull Context context) {
        super(context);
        init();
    }

    public ImageEditThumbnailListWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageEditThumbnailListWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImageEditThumbnailListWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_image_edit_thumbnail_list,
                this, true);
        imageEditThumbnailAdapter = new ImageEditThumbnailAdapter(getContext(), null, 0, this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(imageEditThumbnailAdapter);
    }

    public void setOnImageEditThumbnailListWidgetListener(ImageEditThumbnailListWidget.OnImageEditThumbnailListWidgetListener onImageEditThumbnailListWidgetListener) {
        OnImageEditThumbnailListWidgetListener = onImageEditThumbnailListWidgetListener;
    }

    public void setData(ArrayList<String> imagePathList, int imageIndex) {
        imageEditThumbnailAdapter.setData(imagePathList, imageIndex);
    }

    public void setIndex(int imageIndex) {
        imageEditThumbnailAdapter.setSelectedIndex(imageIndex);
    }

    @Override
    public void onThumbnailItemClicked(String imagePath, int position) {
        if (OnImageEditThumbnailListWidgetListener!= null) {
            OnImageEditThumbnailListWidgetListener.onThumbnailItemClicked(imagePath, position);
        }
    }
}
