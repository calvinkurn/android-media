package com.tokopedia.tkpd.product.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.addtocart.model.ProductCartPass;
import com.tokopedia.tkpd.product.listener.ProductDetailView;
import com.tokopedia.tkpd.product.model.productdetail.ProductDetailData;
import com.tokopedia.tkpd.util.SessionHandler;

import butterknife.Bind;

/**
 * Created by Angga.Prasetiyo on 02/11/2015.
 */
public class ButtonBuyView extends BaseView<ProductDetailData, ProductDetailView> {
    private static final String TAG = ButtonBuyView.class.getSimpleName();

    @Bind(R2.id.tv_buy)
    TextView tvBuy;

    public ButtonBuyView(Context context) {
        super(context);
    }

    public ButtonBuyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_buy_product;
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
        if (data.getPreOrder() != null && data.getPreOrder().getPreorderStatus().equals("1")
                && !data.getPreOrder().getPreorderStatus().equals("0")
                && !data.getPreOrder().getPreorderProcessTime().equals("0")
                && !data.getPreOrder().getPreorderProcessTimeType().equals("0")
                && !data.getPreOrder().getPreorderProcessTimeTypeString().equals("0")) {
            tvBuy.setText(getContext().getString(R.string.title_pre_order));
        } else {
            tvBuy.setText(getContext().getString(R.string.title_buy));
        }
        setOnClickListener(new ClickBuy(data));
        if (data.getShopInfo().getShopIsOwner() == 1 || data.getShopInfo().getShopStatus() != 1
                || (data.getInfo().getProductStatus().equals("3") & data.getShopInfo().getShopStatus() == 1)
                || (data.getShopInfo().getShopIsAllowManage() == 1)) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
    }

    private class ClickBuy implements OnClickListener {
        private final ProductDetailData data;

        public ClickBuy(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            if (SessionHandler.isV4Login(getContext())) {
                String weightProduct = "";
                switch (data.getInfo().getProductWeightUnit()) {
                    case "gr":
                        weightProduct = String.valueOf((Float.parseFloat(data.getInfo()
                                .getProductWeight())) / 1000);
                        break;
                    case "kg":
                        weightProduct = data.getInfo().getProductWeight();
                        break;
                }
                ProductCartPass pass = ProductCartPass.Builder.aProductCartPass()
                        .setImageUri(data.getProductImages().get(0).getImageSrc300())
                        .setMinOrder(Integer.parseInt(data.getInfo().getProductMinOrder()))
                        .setProductId(String.valueOf(data.getInfo().getProductId()))
                        .setProductName(data.getInfo().getProductName())
                        .setWeight(weightProduct)
                        .setShopId(data.getShopInfo().getShopId())
                        .setPrice(data.getInfo().getProductPrice())
                        .build();
                if (!data.getBreadcrumb().isEmpty())
                    pass.setProductCategory(data.getBreadcrumb().get(0).getDepartmentName());

                listener.onProductBuySessionLogin(pass);
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean("login", true);
                listener.onProductBuySessionNotLogin(bundle);
            }
        }
    }
}
