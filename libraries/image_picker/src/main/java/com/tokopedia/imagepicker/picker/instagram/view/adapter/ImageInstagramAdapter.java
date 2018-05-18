package com.tokopedia.imagepicker.picker.instagram.view.adapter;

import android.view.View;

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
    private ArrayList<String> selectedInstagramId;
    private OnImageInstagramAdapterListener listener;
    private boolean supportMultipleSelection;

    public interface OnImageInstagramAdapterListener {
        void onItemClicked(InstagramMediaModel instagramMediaModel, boolean isChecked);

        boolean isImageValid(InstagramMediaModel instagramMediaModel);

        boolean canAddMoreImage();
    }

    public ImageInstagramAdapter(ImageInstagramAdapterTypeFactory baseListAdapterTypeFactory,
                                 OnImageInstagramAdapterListener onImageInstagramAdapterListener,
                                 ArrayList<String> selectedInstagramId,
                                 boolean supportMultipleSelection) {
        super(baseListAdapterTypeFactory, null);
        this.selectedInstagramId = selectedInstagramId;
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
                    if (supportMultipleSelection) {
                        if (selectedInstagramId.contains(item.getId())) {
                            selectedInstagramId.remove(item.getId());
                            isChecked = false;
                        } else {
                            selectedInstagramId.add(item.getId());
                            isChecked = true;
                        }
                    }

                    if (isChecked && !listener.canAddMoreImage()) {
                        selectedInstagramId.remove(item.getId()); //in case support multiple selection
                        return;
                    }

                    if (isChecked && !listener.isImageValid(item)) {
                        selectedInstagramId.remove(item.getId()); //in case support multiple selection
                        return;
                    }
                    notifyItemChanged(holder.getAdapterPosition());

                    listener.onItemClicked(item, isChecked);
                }
            });
            InstagramMediaModel item = (InstagramMediaModel) visitables.get(holder.getAdapterPosition());
            ((ImagePickerInstagramViewHolder)holder).setIsCheck(selectedInstagramId.contains(item.getId()));
        }
        super.onBindViewHolder(holder, position);
    }

    public ArrayList<String> getSelectedInstagramId() {
        return selectedInstagramId;
    }
}
