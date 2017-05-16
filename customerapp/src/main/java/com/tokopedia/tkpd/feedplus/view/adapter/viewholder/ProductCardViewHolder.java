package com.tokopedia.tkpd.feedplus.view.adapter.viewholder;

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

public class ProductCardViewHolder<T> extends AbstractViewHolder<ProductCardViewModel>{

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.shop_avatar)
    ImageView shopAvatar;

    @BindView(R.id.gold_merchant)
    ImageView goldMerchantBadge;

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.share_button)
    View shareButton;

    FeedPlus.View viewListener;

    public ProductCardViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(ProductCardViewModel productCardViewModel) {
        String titleText = "<b>" + productCardViewModel.getShopName() + "</b> "
                + productCardViewModel.getActionText();
        title.setText(MethodChecker.fromHtml(titleText));
        ImageHandler.LoadImage(shopAvatar, productCardViewModel.getShopAvatar());

        if (productCardViewModel.isGoldMerchant())
            goldMerchantBadge.setVisibility(View.VISIBLE);
        else
            goldMerchantBadge.setVisibility(View.GONE);

        time.setText(productCardViewModel.getPostTime());

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onShareButtonClicked();
            }
        });
    }

}
