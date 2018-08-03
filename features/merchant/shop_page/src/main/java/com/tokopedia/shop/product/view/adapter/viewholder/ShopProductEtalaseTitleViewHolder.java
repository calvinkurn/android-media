package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseTitleViewModel;

/**
 * Created by normansyahputa on 2/22/18.
 */

public class ShopProductEtalaseTitleViewHolder extends AbstractViewHolder<ShopProductEtalaseTitleViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_etalase_title_view;
    private TextView textView;

    public ShopProductEtalaseTitleViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    @Override
    public void bind(ShopProductEtalaseTitleViewModel shopProductFeaturedViewModel) {
        textView.setText(MethodChecker.fromHtml(shopProductFeaturedViewModel.getEtalaseName()));
    }

    private void findViews(View view) {
        textView = view.findViewById(R.id.text);
    }

}
