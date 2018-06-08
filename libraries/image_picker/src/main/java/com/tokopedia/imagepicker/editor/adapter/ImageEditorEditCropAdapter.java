package com.tokopedia.imagepicker.editor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;

import java.util.ArrayList;

/**
 * Created by hendry on 19/04/18.
 */

public class ImageEditorEditCropAdapter implements View.OnClickListener {
    private ArrayList<ImageRatioTypeDef> imageRatioTypeDefArrayList;
    private Context context;
    private ViewGroup viewGroup;

    private OnImageEditorEditCropAdapterListener listener;
    private int maxIconWidth;

    @Override
    public void onClick(View v) {
        if (listener != null) {
            int position = v.getId();
            listener.onEditCropClicked(imageRatioTypeDefArrayList.get(position));
        }
    }

    public interface OnImageEditorEditCropAdapterListener {
        void onEditCropClicked(ImageRatioTypeDef imageRatioTypeDef);
    }

    public ImageEditorEditCropAdapter(
            ViewGroup viewGroup,
            Context context,
            ArrayList<ImageRatioTypeDef> imageRatioTypeDefArrayList,
            OnImageEditorEditCropAdapterListener listener) {
        this.viewGroup = viewGroup;
        this.imageRatioTypeDefArrayList = imageRatioTypeDefArrayList;
        this.context = context;
        this.listener = listener;
        this.maxIconWidth = context.getResources().getDimensionPixelSize(R.dimen.dp_24);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public void renderView() {
        if (viewGroup.getChildCount() > 0) {
            viewGroup.removeAllViews();
        }
        int position = 0;
        for (ImageRatioTypeDef imageRatioTypeDef : imageRatioTypeDefArrayList) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_edit_crop_image_icon, viewGroup, false);
            ImageView ivEdit = view.findViewById(R.id.iv_edit);
            TextView tvEdit = view.findViewById(R.id.tv_edit);
            view.setId(position);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ivEdit.getLayoutParams();
            switch (imageRatioTypeDef) {
                case ORIGINAL:
                    layoutParams.width = maxIconWidth;
                    layoutParams.height = maxIconWidth;
                    tvEdit.setText(context.getString(R.string.ratio_original));
                    break;
                case RATIO_1_1:
                    layoutParams.width = maxIconWidth;
                    layoutParams.height = maxIconWidth;
                    tvEdit.setText(context.getString(R.string.ratio_1_1));
                    break;
                case RATIO_3_4:
                    layoutParams.width = 3 * maxIconWidth / 4;
                    layoutParams.height = maxIconWidth;
                    tvEdit.setText(context.getString(R.string.ratio_3_4));
                    break;
                case RATIO_4_3:
                    layoutParams.width = maxIconWidth;
                    layoutParams.height = 3 * maxIconWidth / 4;
                    tvEdit.setText(context.getString(R.string.ratio_4_3));
                    break;
                case RATIO_9_16:
                    layoutParams.width = 9 * maxIconWidth / 16;
                    layoutParams.height = maxIconWidth;
                    tvEdit.setText(context.getString(R.string.ratio_9_16));
                    break;
                case RATIO_16_9:
                    layoutParams.width = maxIconWidth;
                    layoutParams.height = 9 * maxIconWidth / 16;
                    tvEdit.setText(context.getString(R.string.ratio_16_9));
                    break;
            }
            ivEdit.setLayoutParams(layoutParams);

            view.setOnClickListener(this);
            viewGroup.addView(view);
            position++;
        }
    }

}
