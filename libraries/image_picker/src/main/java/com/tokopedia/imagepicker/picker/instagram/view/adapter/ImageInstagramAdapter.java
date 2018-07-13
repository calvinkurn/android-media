package com.tokopedia.imagepicker.picker.instagram.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramMediaModel;

import java.util.ArrayList;

/**
 * Created by hendry on 18/05/18.
 */

public class ImageInstagramAdapter extends BaseListAdapter<InstagramMediaModel, ImageInstagramAdapterTypeFactory> {
    private ArrayList<String> selectedImagePath;
    private OnImageInstagramAdapterListener listener;
    private boolean supportMultipleSelection;

    public interface OnImageInstagramAdapterListener {
        void onItemClicked(InstagramMediaModel instagramMediaModel, boolean isChecked);

        boolean isImageValid(InstagramMediaModel instagramMediaModel);

        boolean canAddMoreImage();
    }

    public ImageInstagramAdapter(ImageInstagramAdapterTypeFactory baseListAdapterTypeFactory,
                                 OnImageInstagramAdapterListener onImageInstagramAdapterListener,
                                 ArrayList<String> selectedImagePath,
                                 boolean supportMultipleSelection) {
        super(baseListAdapterTypeFactory, null);
        this.selectedImagePath = selectedImagePath;
        this.listener = onImageInstagramAdapterListener;
        this.supportMultipleSelection = supportMultipleSelection;
    }

    @Override
    public void onBindViewHolder(final AbstractViewHolder holder, int position) {
        if (getItemViewType(position) == ImagePickerInstagramViewHolder.LAYOUT) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    // fix from fabric error some device, index out of bound, when user multi touch.
                    if (!isValidPos(position)) {
                        return;
                    }
                    InstagramMediaModel item = (InstagramMediaModel) visitables.get(position);
                    boolean isChecked = true;

                    String itemUrl = item.getImageStandardResolutionUrl();
                    if (supportMultipleSelection) {
                        isChecked = !selectedImagePath.contains(itemUrl);
                    }

                    if (isChecked && !listener.canAddMoreImage()) {
                        return;
                    }

                    if (isChecked && !listener.isImageValid(item)) {
                        return;
                    }
                    notifyItemChanged(position);

                    listener.onItemClicked(item, isChecked);
                }
            });
            InstagramMediaModel item = (InstagramMediaModel) visitables.get(holder.getAdapterPosition());
            ((ImagePickerInstagramViewHolder)holder).setIsCheck(selectedImagePath.contains(item.getImageStandardResolutionUrl()));
        }
        super.onBindViewHolder(holder, position);
    }

    private boolean isValidPos(int position){
        return position >= 0 && position < visitables.size();
    }

}
