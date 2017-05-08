package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.model.Product;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public class ProductPriceInfoView extends RelativeLayout {

    @BindView(R2.id.tv_price_origin)
    TextView tvPriceOrigin;
    @BindView(R2.id.tv_price_new)
    TextView tvPriceNew;

    public ProductPriceInfoView(Context context) {
        super(context);
        initialView(context, null, 0);
    }

    public ProductPriceInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialView(context, attrs, 0);
    }

    public ProductPriceInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialView(context, attrs, defStyleAttr);
    }

    private void initialView(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(
                R.layout.view_holder_product_price_info_digital_module, this, true
        );
        ButterKnife.bind(this);
    }

    public void renderData(Product product) {
        if (product.getPromo() == null) {
            tvPriceOrigin.setVisibility(GONE);
            tvPriceNew.setText(product.getPrice());
        } else {
            tvPriceOrigin.setVisibility(VISIBLE);
            tvPriceOrigin.setText(product.getPrice());
            tvPriceNew.setText(product.getPromo().getNewPrice());
        }
    }


}
