package com.tokopedia.posapp.cart.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
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

    AddToCartUseCase addToCartUseCase;

    @Inject
    public AddToCartPresenter(AddToCartUseCase addToCartUseCase) {
        this.addToCartUseCase = addToCartUseCase;
    }

    @Override
    public void add(ProductPass product, int quantity) {
        CartDomain cartDomain = new CartDomain();
        cartDomain.setProductId(Long.parseLong(product.getProductId()));
        cartDomain.setProduct(getProductDomain(product));
        cartDomain.setQuantity(quantity);
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CartConstant.KEY_CART, cartDomain);
        addToCartUseCase.execute(requestParams, new ATCSubscriber(getView()));
    }

    public void addAndCheckout(ProductPass productPass, int quantity) {
        CartDomain cartDomain = new CartDomain();
        cartDomain.setProductId(Long.parseLong(productPass.getProductId()));
        cartDomain.setProduct(getProductDomain(productPass));
        cartDomain.setQuantity(quantity);
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CartConstant.KEY_CART, cartDomain);
        addToCartUseCase.execute(requestParams, new ATCPaymentSubscriber(getView()));
    }

    private ProductDomain getProductDomain(ProductPass product) {
        ProductDomain productDomain = new ProductDomain();
        productDomain.setProductId(Long.parseLong(product.getProductId()));
        productDomain.setProductName(product.getProductName());
        productDomain.setProductImage300(product.getProductImage());
        productDomain.setProductPrice(product.getProductPrice());
        productDomain.setProductPriceUnformatted(CurrencyFormatHelper.convertRupiahToInt(product.getProductPrice()));
        return productDomain;
    }
}
