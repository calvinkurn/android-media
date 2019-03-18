package com.tokopedia.gm.statistic.view.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.card.TitleCardView;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.gm.R;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.common.utils.KMNumbers;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.applink.RouteManager;
import android.content.Context;

/**
 * Created by normansyahputa on 11/9/16.
 */

public class GMStatisticProductViewHolder implements GMStatisticViewHolder {

    private TitleCardView popularProductCardView;
    private ImageView ivPopularProduct;
    private TextView tvPopularProductDescription;
    private TextView tvNoOfSelling;
    private GetPopularProduct getPopularProduct;

    public GMStatisticProductViewHolder(View view) {
        popularProductCardView = (TitleCardView) view.findViewById(R.id.popular_product_card_view);
        ivPopularProduct = (ImageView) view.findViewById(R.id.image_popular_product);
        tvPopularProductDescription = (TextView) view.findViewById(R.id.tv_popular_product);
        tvNoOfSelling = (TextView) view.findViewById(R.id.tv_no_of_selling);
        View.OnClickListener goToProductDetailClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProductDetail();
            }
        };
        ivPopularProduct.setOnClickListener(goToProductDetailClickListener);
        tvPopularProductDescription.setOnClickListener(goToProductDetailClickListener);
    }

    private void clickGMStat() {
        if (getPopularProduct != null) {
            UnifyTracking.eventClickGMStatProduct(popularProductCardView.getContext(), getPopularProduct.getProductName());
        }
    }

    public void bindData(GetPopularProduct getPopularProduct) {
        if (getPopularProduct == null ||
                getPopularProduct.getProductId() == 0 ||
                getPopularProduct.getSold() == 0) {
            setViewState(LoadingStateView.VIEW_EMPTY);
            return;
        }
        this.getPopularProduct = getPopularProduct;
        new ImageHandler(popularProductCardView.getContext()).loadImage(ivPopularProduct, getPopularProduct.getImageLink());
        tvPopularProductDescription.setText(MethodChecker.fromHtml(getPopularProduct.getProductName()));
        long sold = getPopularProduct.getSold();
        String text = KMNumbers.getSummaryString(sold);
        tvNoOfSelling.setText(text);
        setViewState(LoadingStateView.VIEW_CONTENT);
    }

    private void gotoProductDetail() {
        if (getPopularProduct == null) {
            return;
        }
        popularProductCardView.getContext().startActivity(getProductIntent(popularProductCardView.getContext(), getPopularProduct.getProductId() + ""));

        // analytic below : https://phab.tokopedia.com/T18496
        clickGMStat();
    }

    private Intent getProductIntent(Context context, String productId) {
        if (context != null) {
            return RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        } else {
            return null;
        }
    }

    @Override
    public void setViewState(int viewState) {
        popularProductCardView.setViewState(viewState);
    }
}