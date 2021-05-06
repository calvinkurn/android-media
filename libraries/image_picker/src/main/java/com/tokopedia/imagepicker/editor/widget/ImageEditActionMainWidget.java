package com.tokopedia.imagepicker.editor.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.ImageEditActionType;
import com.tokopedia.imagepicker.editor.adapter.ImageEditorEditActionAdapter;

/**
 * Created by hendry on 30/04/18.
 */

public class ImageEditActionMainWidget extends FrameLayout implements ImageEditorEditActionAdapter.OnImageEditorEditActionAdapterListener {

    private ViewGroup viewGroupMainContent;

    private OnImageEditActionMainWidgetListener onImageEditActionMainWidgetListener;
    public interface OnImageEditActionMainWidgetListener{
        void onEditActionClicked (ImageEditActionType editActionType);
    }

    public ImageEditActionMainWidget(@NonNull Context context) {
        super(context);
        init();
    }

    public ImageEditActionMainWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageEditActionMainWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImageEditActionMainWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setOnImageEditActionMainWidgetListener(OnImageEditActionMainWidgetListener onImageEditActionMainWidgetListener) {
        this.onImageEditActionMainWidgetListener = onImageEditActionMainWidgetListener;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_image_edit_action_main,
                this, true);
        viewGroupMainContent = findViewById(R.id.vg_editor_main_content);
    }

    public void setData( ImageEditActionType[] imageEditActionType) {
        ImageEditorEditActionAdapter imageEditorEditActionAdapter =
                new ImageEditorEditActionAdapter(viewGroupMainContent, getContext(), imageEditActionType, this);
        imageEditorEditActionAdapter.renderView();
    }

    @Override
    public void onEditActionClicked(ImageEditActionType actionEditType) {
        if (onImageEditActionMainWidgetListener!= null) {
            onImageEditActionMainWidgetListener.onEditActionClicked(actionEditType);
        }
    }
}
