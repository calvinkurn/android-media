package com.tokopedia.imagepicker.editor.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.ImageEditActionType;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.unifycomponents.Label;
import com.tokopedia.unifyprinciples.Typography;

import java.util.ArrayList;

/**
 * Created by hendry on 19/04/18.
 */

public class ImageEditorEditActionAdapter implements View.OnClickListener {
    private ArrayList<ImageEditActionType> tabTypeDef;
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
            ArrayList<ImageEditActionType> tabTypeDef,
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
            Typography tvEdit = view.findViewById(R.id.tv_edit);
            Label txtLabel = view.findViewById(R.id.txt_label);
            view.setId(tabTypeDefItem.getAction());
            switch (tabTypeDefItem) {
                case ACTION_CROP:
                    setImageViewFromUnify(ivEdit,
                            com.tokopedia.iconunify.R.drawable.iconunify_crop);
                    tvEdit.setText(context.getString(R.string.crop));
                    break;
                case ACTION_ROTATE:
                    setImageViewFromUnify(ivEdit,
                            com.tokopedia.iconunify.R.drawable.iconunify_rotation);
                    tvEdit.setText(context.getString(R.string.rotate));
                    break;
                case ACTION_WATERMARK:
                    ivEdit.setImageDrawable(MethodChecker.getDrawable(context,R.drawable.ic_logo_watermark));
                    tvEdit.setText(context.getString(R.string.watermark));
                    break;
                case ACTION_CROP_ROTATE:
                    ivEdit.setImageDrawable(MethodChecker.getDrawable(context,R.drawable.ic_crop_rotate));
                    tvEdit.setText(context.getString(R.string.crop_and_rotate));
                    break;
                case ACTION_BRIGHTNESS:
                    setImageViewFromUnify(ivEdit,
                            com.tokopedia.iconunify.R.drawable.iconunify_brightness);
                    tvEdit.setText(context.getString(R.string.brightness));
                    break;
                case ACTION_CONTRAST:
                    setImageViewFromUnify(ivEdit,
                            com.tokopedia.iconunify.R.drawable.iconunify_contrast);
                    tvEdit.setText(context.getString(R.string.contrast));
                    break;
                case ACTION_REMOVE_BACKGROUND:
                    ivEdit.setImageDrawable(MethodChecker.getDrawable(context,R.drawable.ic_menu_remove_background));
                    tvEdit.setText(context.getString(R.string.remove_background));
                    txtLabel.setVisibility(View.VISIBLE);
                    break;
            }
            view.setOnClickListener(this);
            viewGroup.addView(view);
        }
    }

    private void setImageViewFromUnify(ImageView imageView, @DrawableRes int id) {
        imageView.setImageResource(id);
        imageView.setColorFilter(ContextCompat.getColor(context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN800),
                PorterDuff.Mode.SRC_ATOP);
    }

}
