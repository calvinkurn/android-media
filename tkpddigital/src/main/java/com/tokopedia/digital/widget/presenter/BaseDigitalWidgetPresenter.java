package com.tokopedia.digital.widget.presenter;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital.widget.model.product.Product;

/**
 * Created by nabillasabbaha on 8/8/17.
 * Modified by rizkyfadillah at 10/6/17.
 */

public abstract class BaseDigitalWidgetPresenter implements IBaseDigitalWidgetPresenter {

    private final Context context;

    private LocalCacheHandler localCacheHandlerLastClientNumber;
    private LocalCacheHandler cacheHandlerRecentInstantCheckoutUsed;

    public BaseDigitalWidgetPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void storeLastInstantCheckoutUsed(String categoryId, boolean checked) {
        if (cacheHandlerRecentInstantCheckoutUsed == null)
            cacheHandlerRecentInstantCheckoutUsed = new LocalCacheHandler(
                    this.context, TkpdCache.DIGITAL_INSTANT_CHECKOUT_HISTORY
            );
        cacheHandlerRecentInstantCheckoutUsed.putBoolean(
                TkpdCache.Key.DIGITAL_INSTANT_CHECKOUT_LAST_IS_CHECKED_CATEGORY + categoryId, checked
        );
        cacheHandlerRecentInstantCheckoutUsed.applyEditor();
    }

    @Override
    public boolean isRecentInstantCheckoutUsed(String categoryId) {
        if (cacheHandlerRecentInstantCheckoutUsed == null)
            cacheHandlerRecentInstantCheckoutUsed = new LocalCacheHandler(
                    this.context, TkpdCache.DIGITAL_INSTANT_CHECKOUT_HISTORY
            );
        return cacheHandlerRecentInstantCheckoutUsed.getBoolean(
                TkpdCache.Key.DIGITAL_INSTANT_CHECKOUT_LAST_IS_CHECKED_CATEGORY + categoryId, false
        );
    }

    @Override
    public void storeLastClientNumberTyped(String categoryId, String clientNumber, Product selectedProduct) {
        if (localCacheHandlerLastClientNumber == null)
            localCacheHandlerLastClientNumber = new LocalCacheHandler(
                    context, TkpdCache.DIGITAL_LAST_INPUT_CLIENT_NUMBER
            );
        localCacheHandlerLastClientNumber.putString(
                TkpdCache.Key.DIGITAL_CLIENT_NUMBER_CATEGORY + categoryId, clientNumber
        );
        localCacheHandlerLastClientNumber.putString(
                TkpdCache.Key.DIGITAL_OPERATOR_ID_CATEGORY + categoryId,
                (selectedProduct != null ?
                        String.valueOf(
                                selectedProduct.getRelationships().getOperator().getData().getId()
                        ) : "")
        );
        localCacheHandlerLastClientNumber.putString(
                TkpdCache.Key.DIGITAL_PRODUCT_ID_CATEGORY + categoryId,
                (selectedProduct != null ? selectedProduct.getId() : "")
        );
        localCacheHandlerLastClientNumber.applyEditor();
    }

    @Override
    public String getLastClientNumberTyped(String categoryId) {
        if (localCacheHandlerLastClientNumber == null)
            localCacheHandlerLastClientNumber = new LocalCacheHandler(
                    context, TkpdCache.DIGITAL_LAST_INPUT_CLIENT_NUMBER);
        return localCacheHandlerLastClientNumber.getString(
                TkpdCache.Key.DIGITAL_CLIENT_NUMBER_CATEGORY + categoryId, "");
    }

    @Override
    public String getLastOperatorSelected(String categoryId) {
        if (localCacheHandlerLastClientNumber == null)
            localCacheHandlerLastClientNumber = new LocalCacheHandler(
                    context, TkpdCache.DIGITAL_LAST_INPUT_CLIENT_NUMBER);
        return localCacheHandlerLastClientNumber.getString(
                TkpdCache.Key.DIGITAL_OPERATOR_ID_CATEGORY + categoryId, "");
    }

    @Override
    public String getLastProductSelected(String categoryId) {
        if (localCacheHandlerLastClientNumber == null)
            localCacheHandlerLastClientNumber = new LocalCacheHandler(
                    context, TkpdCache.DIGITAL_LAST_INPUT_CLIENT_NUMBER);
        return localCacheHandlerLastClientNumber.getString(
                TkpdCache.Key.DIGITAL_PRODUCT_ID_CATEGORY + categoryId, "");
    }

}