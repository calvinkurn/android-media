package com.tokopedia.home.account.presentation.view.categorygridview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.view.categorygridview.model.CategoryItem;

/**
 * @author okasurya on 7/19/18.
 */
public class CategoryGridViewHolder extends RecyclerView.ViewHolder {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_category_grid;

    private LinearLayout layoutCategoryGrid;
    private ImageView imageIcon;
    private TextView textDescription;

    private Context context;

    public CategoryGridViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;

        this.layoutCategoryGrid = itemView.findViewById(R.id.layout_category_grid);
        this.imageIcon = itemView.findViewById(R.id.image_icon);
        this.textDescription = itemView.findViewById(R.id.text_desc);
    }

    public void bind(CategoryItem categoryItem, CategoryGridView.OnClickListener listener) {
        if(!TextUtils.isEmpty(categoryItem.getImageUrl())) {
            ImageHandler.loadImage(context, imageIcon, categoryItem.getImageUrl(), R.drawable.ic_big_notif_customerapp);
        } else if(categoryItem.getResourceId() != 0) {
            imageIcon.setImageDrawable(AppCompatResources.getDrawable(context, categoryItem.getResourceId()));
        }

        textDescription.setText(categoryItem.getDescription());

        if(listener != null) layoutCategoryGrid.setOnClickListener(v -> listener.onCategoryItemClicked(categoryItem));
    }
}
