package com.tokopedia.shop.settings.etalase.view.viewholder;

import android.support.v4.content.ContextCompat;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef;
import com.tokopedia.shop.settings.R;
import com.tokopedia.shop.settings.common.util.SpanTextUtil;
import com.tokopedia.shop.settings.etalase.data.BaseShopEtalaseViewModel;
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseTitleViewModel;
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel;

/**
 * Created by hendry on 16/08/18.
 */
public class ShopEtalaseTitleViewHolder extends AbstractViewHolder<ShopEtalaseTitleViewModel> {

    public static final int LAYOUT = R.layout.item_shop_etalase_title;

    private TextView tvEtalaseTitle;

    public ShopEtalaseTitleViewHolder(View itemView) {
        super(itemView);
        tvEtalaseTitle = itemView.findViewById(R.id.tvEtalaseTitle);
    }

    @Override
    public void bind(ShopEtalaseTitleViewModel shopEtalaseTitleViewModel) {
        String keyword = "";
        tvEtalaseTitle.setText(shopEtalaseTitleViewModel.getName());
    }

}
