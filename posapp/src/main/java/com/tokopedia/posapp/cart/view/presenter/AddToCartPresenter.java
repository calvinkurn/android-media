package com.tokopedia.posapp.cart.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.posapp.PosSessionHandler;
import com.tokopedia.posapp.cart.CartConstant;
import com.tokopedia.posapp.cart.domain.model.CartDomain;
import com.tokopedia.posapp.cart.domain.usecase.AddToCartUseCase;
import com.tokopedia.posapp.cart.view.subscriber.ATCPaymentSubscriber;
import com.tokopedia.posapp.cart.view.subscriber.ATCSubscriber;
import com.tokopedia.posapp.cart.view.AddToCart;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

/**
 * Created by okasurya on 8/22/17.
 */

public class AddToCartPresenter extends BaseDaggerPresenter<AddToCart.View>
        implements AddToCart.Presenter {

    private AddToCartUseCase addToCartUseCase;

    @Inject
    public AddToCartPresenter(AddToCartUseCase addToCartUseCase) {
        this.addToCartUseCase = addToCartUseCase;
    }

    @Override
    public void add(ProductDetailData product, int quantity) {
        CartDomain cartDomain = new CartDomain();
        cartDomain.setProductId(product.getInfo().getProductId());
        cartDomain.setProduct(getProductDomain(product));
        cartDomain.setQuantity(quantity);
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CartConstant.KEY_CART, cartDomain);
        addToCartUseCase.execute(requestParams, new ATCSubscriber(getView()));
    }

    public void addAndCheckout(ProductDetailData productPass, int quantity) {
        CartDomain cartDomain = new CartDomain();
        cartDomain.setProductId(productPass.getInfo().getProductId());
        cartDomain.setProduct(getProductDomain(productPass));
        cartDomain.setQuantity(quantity);
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CartConstant.KEY_CART, cartDomain);
        addToCartUseCase.execute(requestParams, new ATCPaymentSubscriber(getView()));
    }

    private ProductDomain getProductDomain(ProductDetailData product) {
        ProductDomain productDomain = new ProductDomain();
        productDomain.setProductId(product.getInfo().getProductId());
        productDomain.setProductName(product.getInfo().getProductName());
        productDomain.setProductImage300(product.getProductImages().get(0).getImageSrc300());
        productDomain.setProductPrice(product.getInfo().getProductPrice());
        productDomain.setProductPriceUnformatted(product.getInfo().getProductPriceUnformatted());
        return productDomain;
    }
}
