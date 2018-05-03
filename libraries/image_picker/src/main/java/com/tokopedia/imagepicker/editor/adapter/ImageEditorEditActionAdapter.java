package com.tokopedia.imagepicker.editor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.ImagePickerBuilder;

import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImageEditActionTypeDef.TYPE_CROP;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImageEditActionTypeDef.TYPE_CROP_ROTATE;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImageEditActionTypeDef.TYPE_ROTATE;
import static com.tokopedia.imagepicker.picker.ImagePickerBuilder.ImageEditActionTypeDef.TYPE_WATERMARK;

/**
 * Created by hendry on 19/04/18.
 */

public class ImageEditorEditActionAdapter implements View.OnClickListener {
    private @ImagePickerBuilder.ImagePickerTabTypeDef
    int[] tabTypeDef;
    private Context context;
    private ViewGroup viewGroup;

    private OnImageEditorEditActionAdapterListener listener;

    @Override
    public void onClick(View v) {
        if (listener!=null) {
            listener.onEditActionClicked(v.getId());
        }
    }

    public interface OnImageEditorEditActionAdapterListener{
        void onEditActionClicked(@ImagePickerBuilder.ImageEditActionTypeDef int actionEditType);
    }

    public ImageEditorEditActionAdapter(
            ViewGroup viewGroup,
            Context context,
            @ImagePickerBuilder.ImageEditActionTypeDef int[] tabTypeDef,
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
                case TYPE_CROP:
                    ivEdit.setImageResource(R.drawable.ic_crop);
                    tvEdit.setText(context.getString(R.string.crop));
                    break;
                case TYPE_ROTATE:
                    ivEdit.setImageResource(R.drawable.ic_rotate);
                    tvEdit.setText(context.getString(R.string.rotate));
                    break;
                case TYPE_WATERMARK:
                    ivEdit.setImageResource(R.drawable.circle_red);
                    tvEdit.setText(context.getString(R.string.watermark));
                    break;
                case TYPE_CROP_ROTATE:
                    ivEdit.setImageResource(R.drawable.ic_crop);
                    tvEdit.setText(context.getString(R.string.crop_and_rotate));
                    break;
            }
            view.setOnClickListener(this);
            viewGroup.addView(view);
        }
    }

}
