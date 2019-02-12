package com.tokopedia.shop.etalase.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shopetalasepicker.R;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalaseViewHolder extends AbstractViewHolder<ShopEtalaseViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_etalase;
    private final TextView etalasePickerItemName;
    private final ImageView etalasePickerRadioButton;
    private final ImageView etalaseBadgeImageView;
    private final TextView tvCount;

    public ShopEtalaseViewHolder(View itemView) {
        super(itemView);
        etalasePickerItemName = itemView.findViewById(R.id.text_view_name);
        etalasePickerRadioButton = itemView.findViewById(R.id.image_view_etalase_checked);
        etalaseBadgeImageView = itemView.findViewById(R.id.image_view_etalase_badge);
        tvCount = itemView.findViewById(R.id.tv_count);
    }

    @Override
    public void bind(ShopEtalaseViewModel shopEtalaseViewModel) {
        if (!TextUtils.isEmpty(shopEtalaseViewModel.getEtalaseBadge())) {
            ImageHandler.LoadImage(etalaseBadgeImageView, shopEtalaseViewModel.getEtalaseBadge());
            etalaseBadgeImageView.setVisibility(View.VISIBLE);
        } else {
            etalaseBadgeImageView.setVisibility(View.GONE);
        }

        etalasePickerItemName.setText(shopEtalaseViewModel.getEtalaseName());

        if (shopEtalaseViewModel.isSelected()) {
            etalasePickerRadioButton.setVisibility(View.VISIBLE);
        } else {
            etalasePickerRadioButton.setVisibility(View.GONE);
        }

        if (shopEtalaseViewModel.getEtalaseCount() > 0) {
            tvCount.setText(getString(R.string.x_product,
                    String.valueOf(shopEtalaseViewModel.getEtalaseCount())));
            tvCount.setVisibility(View.VISIBLE);
        } else {
            tvCount.setVisibility(View.GONE);
        }
    }
}
