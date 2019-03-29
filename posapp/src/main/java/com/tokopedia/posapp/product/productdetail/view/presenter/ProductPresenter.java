package com.tokopedia.posapp.product.productdetail.view.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.posapp.product.productdetail.domain.usecase.GetProductUseCase;
import com.tokopedia.posapp.product.productdetail.view.Product;
import com.tokopedia.posapp.product.productdetail.view.GetProductSubscriber;
import com.tokopedia.tkpdpdp.PreviewProductImageDetail;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

/**
 * Created by okasurya on 8/10/17.
 */

public class ProductPresenter implements Product.Presenter {
    private static final String PARAM_PRODUCT_ID = "product_id";
    private static final String PARAM_PRODUCT_KEY = "product_key";
    private static final String PARAM_SHOP_DOMAIN = "shop_domain";
    private static final String PARAM_SHOP_ID = "shop_id";
    private static final String PARAM_PERPAGE = "perpage";
    private static final String PARAM_PAGE = "page";

    private Context context;
    private GetProductUseCase getProductUseCase;
    private Product.View view;
    private UserSession userSession;

    @Inject
    public ProductPresenter(@ApplicationContext Context context,
                            GetProductUseCase getProductUseCase,
                            UserSession userSession) {
        this.context = context;
        this.getProductUseCase = getProductUseCase;
        this.userSession = userSession;
    }

    @Override
    public void getProduct(ProductPass productPass) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_PRODUCT_ID, productPass.getProductId());
        params.putString(PARAM_SHOP_ID, userSession.getShopId());
        params.putString(PARAM_PERPAGE, "1");
        params.putString(PARAM_PAGE, "1");
//        params.putString(PARAM_SHOP_DOMAIN, productPass.getShopDomain());
        getProductUseCase.execute(params, new GetProductSubscriber(this.view));
    }

    @Override
    public void processToPicturePreview(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PreviewProductImageDetail.class);
        intent.putExtras(bundle);
        this.view.navigateToActivity(intent);
    }

    @Override
    public void attachView(Product.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
