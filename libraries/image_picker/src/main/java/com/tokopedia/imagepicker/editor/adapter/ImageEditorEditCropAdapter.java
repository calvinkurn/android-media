package com.tokopedia.imagepicker.editor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.common.ImageRatioType;

import java.util.ArrayList;

/**
 * Created by hendry on 19/04/18.
 */

public class ImageEditorEditCropAdapter implements View.OnClickListener {
    private ArrayList<ImageRatioType> imageRatioTypeDefArrayList;
    private Context context;
    private ViewGroup viewGroup;
    private ImageRatioType selectedImageRatio;

    private OnImageEditorEditCropAdapterListener listener;
    private int maxIconWidth;

    private View tempSelectedView;

    @Override
    public void onClick(View v) {
        if (listener != null) {
            int position = v.getId();
            if (tempSelectedView != v) {
                tempSelectedView.setSelected(false);
                tempSelectedView = v;
                tempSelectedView.setSelected(true);
                this.selectedImageRatio = imageRatioTypeDefArrayList.get(position);
                listener.onEditCropClicked(imageRatioTypeDefArrayList.get(position));
            }
        }
    }

    public interface OnImageEditorEditCropAdapterListener {
        void onEditCropClicked(ImageRatioType imageRatioTypeDef);
    }

    public void setRatio(ImageRatioType imageRatio){
        this.selectedImageRatio = imageRatio;
        if (tempSelectedView!= null &&
                imageRatio != imageRatioTypeDefArrayList.get(tempSelectedView.getId()) &&
                viewGroup.getChildCount() > 0) {
            tempSelectedView.setSelected(false);

            int position = 0;
            for (ImageRatioType imageRatioTypeDef : imageRatioTypeDefArrayList) {
                View view = viewGroup.getChildAt(position);
                if (imageRatioTypeDef == selectedImageRatio) {
                    view.setSelected(true);
                    tempSelectedView = view;
                } else {
                    view.setSelected(false);
                }
                position++;
            }

        }

    }

    public ImageEditorEditCropAdapter(
            ViewGroup viewGroup,
            Context context,
            ArrayList<ImageRatioType> imageRatioTypeDefArrayList,
            ImageRatioType selectedImageRatio,
            OnImageEditorEditCropAdapterListener listener) {
        this.viewGroup = viewGroup;
        this.selectedImageRatio = selectedImageRatio;
        setImageRatioTypeDefArrayList(imageRatioTypeDefArrayList);
        this.context = context;
        this.listener = listener;
        this.maxIconWidth = context.getResources().getDimensionPixelSize(R.dimen.dp_24);
    }

    private void setImageRatioTypeDefArrayList(ArrayList<ImageRatioType> imageRatioTypeDefArrayList) {
        if (imageRatioTypeDefArrayList == null || imageRatioTypeDefArrayList.size() == 0) {
            this.imageRatioTypeDefArrayList = new ArrayList<>();
            this.imageRatioTypeDefArrayList.add(selectedImageRatio);
        } else {
            this.imageRatioTypeDefArrayList = imageRatioTypeDefArrayList;
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public void renderView() {
        if (viewGroup.getChildCount() > 0) {
            viewGroup.removeAllViews();
        }
        int position = 0;
        for (ImageRatioType imageRatioTypeDef : imageRatioTypeDefArrayList) {
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

            if (imageRatioTypeDef == selectedImageRatio) {
                view.setSelected(true);
                tempSelectedView = view;
            } else {
                view.setSelected(false);
            }

            view.setOnClickListener(this);
            viewGroup.addView(view);
            position++;
        }
    }

    public ImageRatioType getSelectedImageRatio() {
        return selectedImageRatio;
    }
}
