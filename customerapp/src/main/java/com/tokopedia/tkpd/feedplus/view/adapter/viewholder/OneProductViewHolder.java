package com.tokopedia.tkpd.feedplus.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.feedplus.FeedPlus;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductCardViewModel;

import butterknife.BindView;

/**
 * @author by nisie on 5/16/17.
 */

public class OneProductViewHolder extends AbstractViewHolder<ProductCardViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_product_one;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.shop_avatar)
    ImageView shopAvatar;

    @BindView(R.id.gold_merchant)
    ImageView goldMerchantBadge;

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.product_image)
    ImageView productImage;

    @BindView(R.id.product_name)
    TextView productName;

    @BindView(R.id.product_price)
    TextView productPrice;

    @BindView(R.id.share_button)
    View shareButton;

    private ProductCardViewModel productCardViewModel;
    private FeedPlus.View viewListener;

    private Context context;

    public OneProductViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);
        context = itemView.getContext();
        this.viewListener = viewListener;
    }

    @Override
    public void bind(ProductCardViewModel productCardViewModel) {
        this.productCardViewModel = productCardViewModel;
        String titleText = "<b>" + productCardViewModel.getShopName() + "</b> "
                + productCardViewModel.getActionText();
        title.setText(MethodChecker.fromHtml(titleText));
        ImageHandler.LoadImage(shopAvatar, productCardViewModel.getShopAvatar());

        if (productCardViewModel.isGoldMerchant())
            goldMerchantBadge.setVisibility(View.VISIBLE);
        else
            goldMerchantBadge.setVisibility(View.GONE);

        time.setText(productCardViewModel.getPostTime());

        ImageHandler.LoadImage(productImage, productCardViewModel.getListProduct().get(0).getImageSource());

        productName.setText(MethodChecker.fromHtml(productCardViewModel.getListProduct().get(0).getName()));
        productPrice.setText(productCardViewModel.getListProduct().get(0).getPrice());

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onShareButtonClicked();
            }
        });

    }


}
