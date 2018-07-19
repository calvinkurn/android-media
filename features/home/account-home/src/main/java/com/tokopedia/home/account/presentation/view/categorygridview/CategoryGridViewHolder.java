package com.tokopedia.home.account.presentation.view.categorygridview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.view.categorygridview.model.CategoryItem;

/**
 * @author okasurya on 7/19/18.
 */
public class CategoryGridViewHolder extends RecyclerView.ViewHolder {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_category_grid;

    private ImageView imageIcon;
    private TextView textDescription;

    public CategoryGridViewHolder(Context context, View itemView) {
        super(itemView);
        imageIcon = itemView.findViewById(R.id.image_icon);
        textDescription = itemView.findViewById(R.id.text_desc);
    }

    public void bind(CategoryItem categoryItem) {
//        ImageHandler.loadImage();
        textDescription.setText(categoryItem.getDescription());
    }
}
