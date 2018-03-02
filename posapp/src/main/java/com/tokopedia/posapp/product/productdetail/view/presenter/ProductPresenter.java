package com.tokopedia.posapp.product.productdetail.view.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.posapp.product.productdetail.domain.usecase.GetProductUseCase;
import com.tokopedia.posapp.product.productdetail.view.Product;
import com.tokopedia.posapp.product.productdetail.view.GetProductSubscriber;
import com.tokopedia.tkpdpdp.PreviewProductImageDetail;

import javax.inject.Inject;

/**
 * Created by okasurya on 8/10/17.
 */

public class ProductPresenter extends BaseDaggerPresenter<Product.View>
        implements Product.Presenter {
    private static final String PARAM_PRODUCT_ID = "product_id";
    private static final String PARAM_PRODUCT_KEY = "product_key";
    private static final String PARAM_SHOP_DOMAIN = "shop_domain";

    private Context context;
    private GetProductUseCase getProductUseCase;

    @Inject
    public ProductPresenter(@ApplicationContext Context context,
                            GetProductUseCase getProductUseCase) {
        this.context = context;
        this.getProductUseCase = getProductUseCase;
    }

    @Override
    public void getProduct(ProductPass productPass) {
        RequestParams params = AuthUtil.generateRequestParamsNetwork(context);
        params.putString(PARAM_PRODUCT_ID, productPass.getProductId());
        params.putString(PARAM_PRODUCT_KEY, productPass.getProductKey());
        params.putString(PARAM_SHOP_DOMAIN, productPass.getShopDomain());
        getProductUseCase.execute(params, new GetProductSubscriber(getView()));
    }

    @Override
    public void processToPicturePreview(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PreviewProductImageDetail.class);
        intent.putExtras(bundle);
        getView().navigateToActivity(intent);
    }

    @Override
    public void attachView(Product.View view) {
        super.attachView(view);
    }
}
