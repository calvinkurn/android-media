package com.tokopedia.imagepicker.picker.instagram.view.adapter;

import android.provider.MediaStore;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder;
import com.tokopedia.imagepicker.picker.gallery.model.MediaItem;
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
                    InstagramMediaModel item = (InstagramMediaModel) visitables.get(holder.getAdapterPosition());
                    boolean isChecked = true;

                    String itemUrl = item.getImageStandardResolutionUrl();
                    if (supportMultipleSelection) {
                        if (selectedImagePath.contains(itemUrl)) {
                            selectedImagePath.remove(itemUrl);
                            isChecked = false;
                        } else {
                            selectedImagePath.add(itemUrl);
                            isChecked = true;
                        }
                    }

                    if (isChecked && !listener.canAddMoreImage()) {
                        selectedImagePath.remove(itemUrl); //in case support multiple selection
                        return;
                    }

                    if (isChecked && !listener.isImageValid(item)) {
                        selectedImagePath.remove(itemUrl); //in case support multiple selection
                        return;
                    }
                    notifyItemChanged(holder.getAdapterPosition());

                    listener.onItemClicked(item, isChecked);
                }
            });
            InstagramMediaModel item = (InstagramMediaModel) visitables.get(holder.getAdapterPosition());
            ((ImagePickerInstagramViewHolder)holder).setIsCheck(selectedImagePath.contains(item.getImageStandardResolutionUrl()));
        }
        super.onBindViewHolder(holder, position);
    }

    public void removeImageFromSelection(String imagePath) {
        if (visitables == null || visitables.size() == 0) {
            return;
        }
        if (!supportMultipleSelection) {
            return;
        }
        selectedImagePath.remove(imagePath);
        notifyDataSetChanged();
    }

}
