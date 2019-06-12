package com.tokopedia.imagepicker.editor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

/**
 * Created by hendry on 19/04/18.
 */

public class ImageEditorEditActionAdapter implements View.OnClickListener {
    private @ImagePickerTabTypeDef
    int[] tabTypeDef;
    private Context context;
    private ViewGroup viewGroup;

    private OnImageEditorEditActionAdapterListener listener;

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onEditActionClicked(v.getId());
        }
    }

    public interface OnImageEditorEditActionAdapterListener {
        void onEditActionClicked(@ImageEditActionTypeDef int actionEditType);
    }

    public ImageEditorEditActionAdapter(
            ViewGroup viewGroup,
            Context context,
            @ImageEditActionTypeDef int[] tabTypeDef,
            OnImageEditorEditActionAdapterListener listener) {
        this.viewGroup = viewGroup;
        this.tabTypeDef = tabTypeDef;
        this.context = context;
        this.listener = listener;
    }

    public void renderView() {
        if (viewGroup.getChildCount() > 0) {
            viewGroup.removeAllViews();
        }
        for (int tabTypeDefItem : tabTypeDef) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_edit_image_icon, viewGroup, false);
            ImageView ivEdit = view.findViewById(R.id.iv_edit);
            TextView tvEdit = view.findViewById(R.id.tv_edit);
            view.setId(tabTypeDefItem);
            switch (tabTypeDefItem) {
                case ImageEditActionTypeDef.ACTION_CROP:
                    ivEdit.setImageDrawable(MethodChecker.getDrawable(context,R.drawable.ic_crop));
                    tvEdit.setText(context.getString(R.string.crop));
                    break;
                case ImageEditActionTypeDef.ACTION_ROTATE:
                    ivEdit.setImageDrawable(MethodChecker.getDrawable(context,R.drawable.ic_rotate));
                    tvEdit.setText(context.getString(R.string.rotate));
                    break;
                case ImageEditActionTypeDef.ACTION_WATERMARK:
                    ivEdit.setImageDrawable(MethodChecker.getDrawable(context,R.drawable.ic_crop_rotate));
                    tvEdit.setText(context.getString(R.string.watermark));
                    break;
                case ImageEditActionTypeDef.ACTION_CROP_ROTATE:
                    ivEdit.setImageDrawable(MethodChecker.getDrawable(context,R.drawable.ic_crop_rotate));
                    tvEdit.setText(context.getString(R.string.crop_and_rotate));
                    break;
                case ImageEditActionTypeDef.ACTION_BRIGHTNESS:
                    ivEdit.setImageDrawable(MethodChecker.getDrawable(context,R.drawable.ic_brightness));
                    tvEdit.setText(context.getString(R.string.brightness));
                    break;
                case ImageEditActionTypeDef.ACTION_CONTRAST:
                    ivEdit.setImageDrawable(MethodChecker.getDrawable(context,R.drawable.ic_contrast));
                    tvEdit.setText(context.getString(R.string.contrast));
                    break;
            }
            view.setOnClickListener(this);
            viewGroup.addView(view);
        }
    }

}
