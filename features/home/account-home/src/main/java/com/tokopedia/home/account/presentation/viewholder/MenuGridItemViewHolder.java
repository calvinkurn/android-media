package com.tokopedia.home.account.presentation.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.viewmodel.MenuItemViewModel;

/**
 * @author okasurya on 7/19/18.
 */
public class MenuGridItemViewHolder extends RecyclerView.ViewHolder {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_menu_grid_item;

    private RelativeLayout layoutCategoryGrid;
    private ImageView imageIcon;
    private TextView textDescription;

    private Context context;

    public MenuGridItemViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;

        this.layoutCategoryGrid = itemView.findViewById(R.id.layout_category_grid);
        this.imageIcon = itemView.findViewById(R.id.image_icon);
        this.textDescription = itemView.findViewById(R.id.text_desc);
    }

    public void bind(MenuItemViewModel menuItemViewModel) {
        if (!TextUtils.isEmpty(menuItemViewModel.getImageUrl())) {
            ImageHandler.loadImage(context, imageIcon, menuItemViewModel.getImageUrl(), R.drawable.ic_big_notif_customerapp);
        } else if (menuItemViewModel.getResourceId() != 0) {
            imageIcon.setImageDrawable(AppCompatResources.getDrawable(context, menuItemViewModel.getResourceId()));
        }

        textDescription.setText(menuItemViewModel.getDescription());

//        if(listener != null) layoutCategoryGrid.setOnClickListener(v -> listener.onCategoryItemClicked(menuItemViewModel));
    }
}
