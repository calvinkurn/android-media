package com.tokopedia.core.product.customview;

import android.content.Context;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.product.listener.ProductDetailView;
import com.tokopedia.core.product.model.productdetail.ProductCampaign;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.MethodChecker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindString;
import butterknife.BindView;


/**
 * Created by Angga.Prasetiyo on 26/10/2015.
 */
public class HeaderInfoView extends BaseView<ProductDetailData, ProductDetailView> {
    private static final String TAG = HeaderInfoView.class.getSimpleName();
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";

    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_price)
    TextView tvPrice;
    @BindView(R2.id.tv_viewed)
    TextView tvViewed;
    @BindView(R2.id.tv_brought)
    TextView tvBrought;
    @BindView(R2.id.label_cashback)
    TextView cashbackTextView;
    @BindView(R2.id.cashback_holder)
    LinearLayout cashbackHolder;
    @BindView(R2.id.title_viewed)
    TextView titleViewed;
    @BindView(R2.id.title_sold)
    TextView titleSold;
    @BindView(R2.id.text_original_price)
    TextView textOriginalPrice;
    @BindView(R2.id.text_discount)
    TextView textDiscount;
    @BindView(R2.id.linear_discount_timer_holder)
    LinearLayout linearDiscountTimerHolder;
    @BindView(R2.id.text_discount_timer)
    TextView textDiscountTimer;
    @BindString(R2.string.label_discount)
    public String discount;
    @BindString(R2.string.label_discount_timer)
    String labelPromotionTimer;
    @BindString(R2.string.label_price_with_idr)
    String labelPriceWithIdr;


    public HeaderInfoView(Context context) {
        super(context);
    }

    public HeaderInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_header_product_info;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(INVISIBLE);
        tvBrought.setText("");
        tvViewed.setText("");
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        tvName.setText(MethodChecker.fromHtml(data.getInfo().getProductName()));
        tvPrice.setText(data.getInfo().getProductPrice());
        titleViewed.setVisibility(VISIBLE);
        titleSold.setVisibility(VISIBLE);
        tvBrought.setText(data.getStatistic().getProductSoldCount());
        tvViewed.setText(data.getStatistic().getProductViewCount());
        setVisibility(VISIBLE);

        if(data.getCashBack() !=null && !data.getCashBack().getProductCashback().isEmpty()) {
            cashbackHolder.setVisibility(VISIBLE);
            cashbackTextView.setVisibility(VISIBLE);
            cashbackTextView.setText(getContext().getString(R.string.value_cashback)
                    .replace("X", data.getCashBack().getProductCashback()));
        }
    }

    public void renderTempData(ProductPass productPass) {
        tvName.setText(productPass.getProductName());
        tvPrice.setText(productPass.getProductPrice());
        setVisibility(VISIBLE);
    }

    public void renderProductCampaign(final ProductCampaign data) {
        if(data != null && data.getOriginalPrice() != null) {
            cashbackHolder.setVisibility(VISIBLE);
            textDiscount.setVisibility(VISIBLE);
            textOriginalPrice.setVisibility(VISIBLE);

            textOriginalPrice.setText(String.format(labelPriceWithIdr, data.getOriginalPrice()));
            textOriginalPrice.setPaintFlags(
                    textOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );

            textDiscount.setText(String.format(discount,data.getPercentageAmount()));

            try {
                SimpleDateFormat sf = new SimpleDateFormat(DATE_TIME_FORMAT);
                long now = new Date().getTime();
                long end = sf.parse(data.getEndDate()).getTime();
                long delta = end - now;

                new CountDownTimer(delta, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        textDiscountTimer.setText(
                                String.format(
                                        labelPromotionTimer,
                                        TimeUnit.MILLISECONDS.toDays(millisUntilFinished),
                                        (millisUntilFinished / (1000 * 60 * 60)) % 24,
                                        (millisUntilFinished / (1000 * 60)) % 60,
                                        (millisUntilFinished / (1000)) % 60
                                )
                        );
                    }

                    @Override
                    public void onFinish() {
                        linearDiscountTimerHolder.setVisibility(GONE);
                        textDiscount.setVisibility(GONE);
                        tvPrice.setText(data.getOriginalPrice());
                    }
                }.start();

                linearDiscountTimerHolder.setVisibility(VISIBLE);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
    }
}
