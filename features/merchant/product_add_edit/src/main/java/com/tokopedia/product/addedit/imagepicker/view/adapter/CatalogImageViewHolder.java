package com.tokopedia.product.addedit.imagepicker.view.adapter;

import android.view.View;
import android.widget.ImageView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.product.addedit.R;
import com.tokopedia.product.addedit.imagepicker.view.model.CatalogModelView;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public class CatalogImageViewHolder extends AbstractViewHolder<CatalogModelView> {
    public static final int LAYOUT = R.layout.product_add_edit_item_media_catalog;

    private ImageView imageCatalog;
    private View ivCheck;
    private int imageResize;
    private boolean isChecked;

    public CatalogImageViewHolder(View parent, int imageResize) {
        super(parent);
        imageCatalog = itemView.findViewById(R.id.image_catalog);
        ivCheck = itemView.findViewById(R.id.iv_check);
        this.imageResize = imageResize;
    }

    public void setIsCheck(boolean isChecked){
        this.isChecked = isChecked;
    }

    @Override
    public void bind(CatalogModelView element) {
        ImageHandler.LoadImageResize(imageCatalog.getContext(), imageCatalog, element.getImageUrl(),
                imageResize, imageResize);
        if (isChecked){
            ivCheck.setVisibility(View.VISIBLE);
        } else {
            ivCheck.setVisibility(View.GONE);
        }
    }
}
