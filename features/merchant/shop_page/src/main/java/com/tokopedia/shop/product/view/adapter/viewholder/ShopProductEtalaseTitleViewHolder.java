package com.tokopedia.shop.product.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
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
    private ImageView ivBadge;

    public ShopProductEtalaseTitleViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    @Override
    public void bind(ShopProductEtalaseTitleViewModel shopProductFeaturedViewModel) {
        textView.setText(MethodChecker.fromHtml(shopProductFeaturedViewModel.getEtalaseName()));
        if (!TextUtils.isEmpty(shopProductFeaturedViewModel.getEtalaseBadge())){
            ImageHandler.LoadImage(ivBadge, shopProductFeaturedViewModel.getEtalaseBadge());
            ivBadge.setVisibility(View.VISIBLE);
        } else {
            ivBadge.setVisibility(View.GONE);
        }
    }

    private void findViews(View view) {
        textView = view.findViewById(R.id.text);
        ivBadge = view.findViewById(R.id.image_view_etalase_badge);
    }

}
