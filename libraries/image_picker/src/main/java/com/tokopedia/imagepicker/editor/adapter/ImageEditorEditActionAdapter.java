package com.tokopedia.imagepicker.editor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.ImageEditActionType;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

/**
 * Created by hendry on 19/04/18.
 */

public class ImageEditorEditActionAdapter implements View.OnClickListener {
    private ImageEditActionType[] tabTypeDef;
    private Context context;
    private ViewGroup viewGroup;

    private OnImageEditorEditActionAdapterListener listener;

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onEditActionClicked(ImageEditActionType.fromInt(v.getId()));
        }
    }

    public interface OnImageEditorEditActionAdapterListener {
        void onEditActionClicked(ImageEditActionType actionEditType);
    }

    public ImageEditorEditActionAdapter(
            ViewGroup viewGroup,
            Context context,
            ImageEditActionType[] tabTypeDef,
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
        for (ImageEditActionType tabTypeDefItem : tabTypeDef) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_edit_image_icon, viewGroup, false);
            ImageView ivEdit = view.findViewById(R.id.iv_edit);
            TextView tvEdit = view.findViewById(R.id.tv_edit);
            view.setId(tabTypeDefItem.getAction());
            switch (tabTypeDefItem) {
                case ACTION_CROP:
                    ivEdit.setImageDrawable(MethodChecker.getDrawable(context,R.drawable.ic_crop));
                    tvEdit.setText(context.getString(R.string.crop));
                    break;
                case ACTION_ROTATE:
                    ivEdit.setImageDrawable(MethodChecker.getDrawable(context,R.drawable.ic_rotate));
                    tvEdit.setText(context.getString(R.string.rotate));
                    break;
                case ACTION_WATERMARK:
                    ivEdit.setImageDrawable(MethodChecker.getDrawable(context,R.drawable.ic_crop_rotate));
                    tvEdit.setText(context.getString(R.string.watermark));
                    break;
                case ACTION_CROP_ROTATE:
                    ivEdit.setImageDrawable(MethodChecker.getDrawable(context,R.drawable.ic_crop_rotate));
                    tvEdit.setText(context.getString(R.string.crop_and_rotate));
                    break;
                case ACTION_BRIGHTNESS:
                    ivEdit.setImageDrawable(MethodChecker.getDrawable(context,R.drawable.ic_brightness));
                    tvEdit.setText(context.getString(R.string.brightness));
                    break;
                case ACTION_CONTRAST:
                    ivEdit.setImageDrawable(MethodChecker.getDrawable(context,R.drawable.ic_contrast));
                    tvEdit.setText(context.getString(R.string.contrast));
                    break;
            }
            view.setOnClickListener(this);
            viewGroup.addView(view);
        }
    }

}
