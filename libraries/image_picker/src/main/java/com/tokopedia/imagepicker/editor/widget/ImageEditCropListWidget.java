package com.tokopedia.imagepicker.editor.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.editor.adapter.ImageEditorEditCropAdapter;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;

import java.util.ArrayList;

/**
 * Created by hendry on 30/04/18.
 */

public class ImageEditCropListWidget extends FrameLayout implements ImageEditorEditCropAdapter.OnImageEditorEditCropAdapterListener {

    private ViewGroup viewGroupMainContent;

    private OnImageEditCropWidgetListener onImageEditCropWidgetListener;
    public interface OnImageEditCropWidgetListener{
        void onEditCropClicked(ImageRatioTypeDef imageRatioTypeDef);
    }

    public ImageEditCropListWidget(@NonNull Context context) {
        super(context);
        init();
    }

    public ImageEditCropListWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageEditCropListWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImageEditCropListWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setOnImageEditCropWidgetListener(OnImageEditCropWidgetListener onImageEditCropWidgetListener) {
        this.onImageEditCropWidgetListener = onImageEditCropWidgetListener;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_image_edit_crop,
                this, true);
        viewGroupMainContent = findViewById(R.id.vg_editor_crop);
    }

    public void setData(ArrayList<ImageRatioTypeDef> imageRatioTypeDefArrayList) {
        ImageEditorEditCropAdapter imageEditorEditCropAdapter =
                new ImageEditorEditCropAdapter(viewGroupMainContent, getContext(), imageRatioTypeDefArrayList, this);
        imageEditorEditCropAdapter.renderView();
    }

    @Override
    public void onEditCropClicked(ImageRatioTypeDef imageRatioTypeDef) {
        if (onImageEditCropWidgetListener!= null) {
            onImageEditCropWidgetListener.onEditCropClicked(imageRatioTypeDef);
        }
    }


}
