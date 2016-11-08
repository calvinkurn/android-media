package com.tokopedia.core.product.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.product.listener.ProductDetailView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;

import butterknife.Bind;

/**
 * Created by Angga.Prasetiyo on 29/10/2015.
 */
public class TalkReviewView extends BaseView<ProductDetailData, ProductDetailView> {
    private static final String TAG = TalkReviewView.class.getSimpleName();

    @Bind(R2.id.l_talk)
    LinearLayout llTalk;
    @Bind(R2.id.tv_talk)
    TextView tvTalk;
    @Bind(R2.id.l_review)
    LinearLayout llReview;
    @Bind(R2.id.tv_review)
    TextView tvReview;

    public TalkReviewView(Context context) {
        super(context);
    }

    public TalkReviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }


    @Override
    protected int getLayoutView() {
        return R.layout.view_talk_review_product_info;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(INVISIBLE);
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        tvTalk.setText(data.getStatistic().getProductTalkCount());
        tvReview.setText(data.getStatistic().getProductReviewCount());

        llTalk.setOnClickListener(new ClickTalk(data));
        llReview.setOnClickListener(new ClickReview(data));
        
        setVisibility(VISIBLE);
    }

    public void updateTalkCount(int talkCount) {
        tvTalk.setText(String.valueOf(talkCount));
    }

    private class ClickTalk implements OnClickListener {
        private final ProductDetailData data;

        public ClickTalk(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("product_id", String.valueOf(data.getInfo().getProductId()));
            bundle.putString("shop_id", String.valueOf(data.getShopInfo().getShopId()));
            bundle.putString("prod_name", data.getInfo().getProductName());
            bundle.putString("is_owner", String.valueOf(data.getShopInfo().getShopIsOwner()));
            bundle.putString("product_image", data.getProductImages().get(0).getImageSrc300());
            listener.onProductTalkClicked(bundle);
        }
    }

    private class ClickReview implements OnClickListener {
        private final ProductDetailData data;

        public ClickReview(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("product_id", String.valueOf(data.getInfo().getProductId()));
            bundle.putString("shop_id", String.valueOf(data.getShopInfo().getShopId()));
            bundle.putString("prod_name", data.getInfo().getProductName());
            //bundle.putString("rating_listed", ratingToPass);
            listener.onProductReviewClicked(bundle);
        }
    }
}
