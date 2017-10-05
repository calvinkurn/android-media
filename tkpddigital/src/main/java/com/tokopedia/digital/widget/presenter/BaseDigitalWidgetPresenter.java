package com.tokopedia.digital.widget.presenter;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.database.recharge.recentOrder.LastOrderEntity;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital.widget.interactor.IDigitalWidgetInteractor;
import com.tokopedia.digital.widget.listener.BaseDigitalWidgetView;
import com.tokopedia.digital.widget.model.DigitalNumberList;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 8/8/17.
 */

public abstract class BaseDigitalWidgetPresenter implements IBaseDigitalWidgetPresenter {

    private final Context context;
    private BaseDigitalWidgetView view;
    private IDigitalWidgetInteractor widgetInteractor;

    private final LocalCacheHandler localCacheHandlerLastOrder;
    private LocalCacheHandler localCacheHandlerLastClientNumber;
    private LocalCacheHandler cacheHandlerRecentInstantCheckoutUsed;

    BaseDigitalWidgetPresenter(Context context, IDigitalWidgetInteractor widgetInteractor,
                               BaseDigitalWidgetView view) {
        this.context = context;
        this.view = view;
        this.widgetInteractor = widgetInteractor;
        localCacheHandlerLastOrder = new LocalCacheHandler(context,
                TkpdCache.DIGITAL_WIDGET_LAST_ORDER);
    }

//    @Override
//    public void fetchNumberList(String categoryId) {
//        TKPDMapParam<String, String> param = new TKPDMapParam<>();
//        param.put("category_id", categoryId);
//        param.put("sort", "label");
//        widgetInteractor.getNumberList(getNumberListSubscriber(),
//                AuthUtil.generateParamsNetwork(context, param));
//    }
//
//    private Subscriber<DigitalNumberList> getNumberListSubscriber() {
//        return new Subscriber<DigitalNumberList>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(DigitalNumberList digitalNumberList) {
//                view.renderNumberList(digitalNumberList.getOrderClientNumbers());
//                if (digitalNumberList.getLastOrder() != null) {
//                    LastOrder lastOrder = new LastOrder();
//                    LastOrderEntity lastOrderEntity = new LastOrderEntity();
//                    LastOrderEntity.AttributesBean attributesBean = new LastOrderEntity.AttributesBean();
//                    attributesBean.setClient_number(digitalNumberList.getLastOrder().getClientNumber());
//                    attributesBean.setCategory_id(Integer.valueOf(digitalNumberList.getLastOrder().getCategoryId()));
//                    attributesBean.setOperator_id(Integer.valueOf(digitalNumberList.getLastOrder().getOperatorId()));
//                    attributesBean.setProduct_id(Integer.valueOf(digitalNumberList.getLastOrder().getLastProduct()));
//                    lastOrderEntity.setAttributes(attributesBean);
//                    lastOrder.setData(lastOrderEntity);
//                    view.renderLastOrder(lastOrder);
//                } else {
//                    view.renderLastOrder(getLastOrderFromCache());
//                }
//            }
//        };
//    }


    @Override
    public boolean isAlreadyHaveLastOrderOnCache() {
        return null != localCacheHandlerLastOrder.getString(TkpdCache.Key.DIGITAL_LAST_ORDER);
    }

//    @Override
//    public LastOrder getLastOrderFromCache() {
//        if (isAlreadyHaveLastOrderOnCache()) {
//            String temp = localCacheHandlerLastOrder.getString(TkpdCache.Key.DIGITAL_LAST_ORDER);
//            return CacheUtil.convertStringToModel(temp, LastOrder.class);
//        } else {
//            return null;
//        }
//    }

    //    @Override
    LastOrder getLastOrderFromCache() {
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
                return (lastOrder.getData().getAttributes().getCategory_id() == categoryId);
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