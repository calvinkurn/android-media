package com.tokopedia.home.account.presentation.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridItemViewModel;

import q.rorbin.badgeview.QBadgeView;

/**
 * @author okasurya on 7/19/18.
 */
public class MenuGridItemViewHolder extends RecyclerView.ViewHolder {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_menu_grid_item;

    private RelativeLayout layoutCategoryGrid;
    private ImageView imageIcon;
    private TextView textDescription;
    private QBadgeView badge;

    private Context context;
    private AccountItemListener listener;

    public MenuGridItemViewHolder(Context context, View itemView, AccountItemListener listener) {
        super(itemView);
        this.context = context;
        this.listener = listener;

        this.layoutCategoryGrid = itemView.findViewById(R.id.layout_category_grid);
        this.imageIcon = itemView.findViewById(R.id.image_icon);
        this.textDescription = itemView.findViewById(R.id.text_desc);
        this.badge = new QBadgeView(context);
    }

    public void bind(MenuGridItemViewModel menuItem) {
        if (!TextUtils.isEmpty(menuItem.getImageUrl())) {
            ImageHandler.loadImage(context, imageIcon, menuItem.getImageUrl(), R.drawable.ic_big_notif_customerapp);
        } else if (menuItem.getResourceId() != 0) {
            imageIcon.setImageDrawable(AppCompatResources.getDrawable(context, menuItem.getResourceId()));
        }

        if (menuItem.getCount() > 0) {
            badge.bindTarget(imageIcon);
            badge.setBadgeGravity(Gravity.END | Gravity.TOP);
            badge.setBadgeNumber(menuItem.getCount());
        } else {
            badge.setVisibility(View.GONE);
        }

        textDescription.setText(menuItem.getDescription());

        if (listener != null)
            layoutCategoryGrid.setOnClickListener(v -> listener.onMenuGridItemClicked(menuItem));
    }
}
