package com.tokopedia.posapp.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.posapp.domain.usecase.AddToCartUseCase;
import com.tokopedia.posapp.domain.usecase.GetProductCampaignUseCase;
import com.tokopedia.posapp.domain.usecase.GetProductUseCase;
import com.tokopedia.posapp.view.Product;
import com.tokopedia.posapp.view.subscriber.GetProductCampaignSubscriber;
import com.tokopedia.posapp.view.subscriber.GetProductSubscriber;

import javax.inject.Inject;

/**
 * Created by okasurya on 8/10/17.
 */

public class ProductPresenter extends BaseDaggerPresenter<Product.View>
        implements Product.Presenter {
    private static final String PARAM_PRODUCT_ID = "product_id";
    private static final String PARAM_PRODUCT_KEY = "product_key";
    private static final String PARAM_SHOP_DOMAIN = "shop_domain";
    public static final String PRODUCT_ID = "PRODUCT_ID";

    private Context context;
    private GetProductUseCase getProductUseCase;
    private GetProductCampaignUseCase getProductCampaignUseCase;

    @Inject
    public ProductPresenter(@ApplicationContext Context context,
                            GetProductUseCase getProductUseCase,
                            GetProductCampaignUseCase getProductCampaignUseCase,
                            AddToCartUseCase addToCartUseCase) {
        this.context = context;
        this.getProductUseCase = getProductUseCase;
        this.getProductCampaignUseCase = getProductCampaignUseCase;
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
    public void getProductCampaign(int productId) {
        RequestParams params = RequestParams.create();
        params.putString(PRODUCT_ID, productId+"");
        getProductCampaignUseCase.execute(params, new GetProductCampaignSubscriber(getView()));
    }

    @Override
    public void attachView(Product.View view) {
        super.attachView(view);
    }
}
