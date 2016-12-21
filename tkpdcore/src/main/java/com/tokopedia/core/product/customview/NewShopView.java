package com.tokopedia.core.product.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.core.R;
import com.tokopedia.core.product.listener.ProductDetailView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created by Angga.Prasetiyo on 02/11/2015.
 */
public class NewShopView extends BaseView<ProductDetailData, ProductDetailView> {
    private static final String TAG = NewShopView.class.getSimpleName();

    public NewShopView(Context context) {
        super(context);
    }

    public NewShopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_new_shop_product_info;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        if (SessionHandler.isV4Login(getContext())) {
            switch (SessionHandler.getShopID(getContext())) {
                case "0":
                    setVisibility(View.VISIBLE);
                    break;
                case "":
                    setVisibility(View.VISIBLE);
                    break;
                default:
                    setVisibility(View.GONE);
                    break;
            }
        } else {
            setVisibility(View.VISIBLE);
        }
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProductNewShopClicked();
            }
        });
    }
}
