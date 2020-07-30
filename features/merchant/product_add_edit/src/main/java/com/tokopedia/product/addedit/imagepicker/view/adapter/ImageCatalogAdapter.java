package com.tokopedia.product.addedit.imagepicker.view.adapter;


import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.product.addedit.imagepicker.view.model.CatalogModelView;

import java.util.ArrayList;

/**
 * Created by zulfikarrahman on 6/6/18.
 */

public class ImageCatalogAdapter extends BaseListAdapter<CatalogModelView, CatalogAdapterTypeFactory> {

    private ArrayList<String> selectedImagePath;
    private OnImageCatalogAdapterListener listener;

    public interface OnImageCatalogAdapterListener {
        void onItemClicked(CatalogModelView catalogModelView, boolean isChecked);

        boolean canAddMoreImage();
    }

    public ImageCatalogAdapter(CatalogAdapterTypeFactory adapterTypeFactory, OnImageCatalogAdapterListener onImageCatalogAdapterListener, ArrayList<String> imagePath) {
        super(adapterTypeFactory, null);
        this.selectedImagePath = imagePath;
        this.listener = onImageCatalogAdapterListener;
    }

    @Override
    public void onBindViewHolder(final AbstractViewHolder holder, int position) {
        if (getItemViewType(position) == CatalogImageViewHolder.LAYOUT) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CatalogModelView item = (CatalogModelView) visitables.get(holder.getAdapterPosition());
                    boolean isChecked = true;

                    String itemUrl = item.getImageUrl();
                    if (selectedImagePath.contains(itemUrl)) {
                        isChecked = false;
                    } else {
                        isChecked = true;
                    }

                    if (isChecked && !listener.canAddMoreImage()) {
                        return;
                    }
                    notifyItemChanged(holder.getAdapterPosition());

                    listener.onItemClicked(item, isChecked);
                }
            });
            CatalogModelView item = (CatalogModelView) visitables.get(holder.getAdapterPosition());
            ((CatalogImageViewHolder) holder).setIsCheck(selectedImagePath.contains(item.getImageUrl()));
        }
        super.onBindViewHolder(holder, position);
    }

}
