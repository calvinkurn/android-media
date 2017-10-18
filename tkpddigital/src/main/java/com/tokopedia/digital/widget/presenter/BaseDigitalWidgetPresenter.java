package com.tokopedia.digital.widget.presenter;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital.widget.model.lastorder.LastOrder;
import com.tokopedia.digital.widget.model.product.Product;

/**
 * Created by nabillasabbaha on 8/8/17.
 */

public abstract class BaseDigitalWidgetPresenter implements IBaseDigitalWidgetPresenter {

    private final Context context;

    private final LocalCacheHandler localCacheHandlerLastOrder;
    private LocalCacheHandler localCacheHandlerLastClientNumber;
    private LocalCacheHandler cacheHandlerRecentInstantCheckoutUsed;

    public BaseDigitalWidgetPresenter(Context context) {
        this.context = context;
        localCacheHandlerLastOrder = new LocalCacheHandler(context,
                TkpdCache.DIGITAL_WIDGET_LAST_ORDER);
    }

    @Override
    public boolean isAlreadyHaveLastOrderOnCache() {
        return null != localCacheHandlerLastOrder.getString(TkpdCache.Key.DIGITAL_LAST_ORDER);
    }

    @Override
    public LastOrder getLastOrderFromCache() {
        if (isAlreadyHaveLastOrderOnCache()) {
            String temp = localCacheHandlerLastOrder.getString(TkpdCache.Key.DIGITAL_LAST_ORDER);
            return CacheUtil.convertStringToModel(temp, LastOrder.class);
        } else {
            return null;
        }
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
    public boolean isAlreadyHaveLastOrderOnCacheByCategoryId(int categoryId) {
        if (isAlreadyHaveLastOrderOnCache()) {
            String temp = localCacheHandlerLastOrder.getString(TkpdCache.Key.DIGITAL_LAST_ORDER);
            try {
                LastOrder lastOrder = CacheUtil.convertStringToModel(temp, LastOrder.class);
                return (lastOrder.getAttributes().getCategoryId() == categoryId);
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
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
                (selectedProduct != null ? String.valueOf(selectedProduct.getId()) : "")
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