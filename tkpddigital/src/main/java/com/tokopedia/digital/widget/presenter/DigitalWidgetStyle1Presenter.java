package com.tokopedia.digital.widget.presenter;

import android.content.Context;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.widget.interactor.IDigitalWidgetInteractor;
import com.tokopedia.digital.widget.listener.IDigitalWidgetStyle1View;
import com.tokopedia.digital.widget.model.lastorder.Attributes;
import com.tokopedia.digital.widget.model.lastorder.LastOrder;
import com.tokopedia.digital.widget.model.operator.Operator;
import com.tokopedia.digital.widget.model.product.Product;
import com.tokopedia.digital.widget.model.DigitalNumberList;

import java.util.List;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 7/21/17.
 * Modified by rizkyfadillah at 10/6/17.
 */

public class DigitalWidgetStyle1Presenter extends BaseDigitalWidgetPresenter
        implements IDigitalWidgetStyle1Presenter {

    private static final String PULSA_CATEGORY_ID = "1";
    private static final String PAKET_DATA_CATEGORY_ID = "2";
    private static final String ROAMING_CATEGORY_ID = "20";

    private final String PARAM_CATEGORY_ID = "category_id";
    private final String PARAM_SORT = "sort";

    private final String PARAM_VALUE_LABEL = "label";

    private final IDigitalWidgetInteractor widgetInteractor;
    private final IDigitalWidgetStyle1View view;
    private Context context;

    public DigitalWidgetStyle1Presenter(Context context,
                                        IDigitalWidgetInteractor widgetInteractor,
                                        IDigitalWidgetStyle1View view) {
        super(context);
        this.context = context;
        this.widgetInteractor = widgetInteractor;
        this.view = view;
    }

    @Override
    public void fetchNumberList(String categoryId, boolean showLastOrder) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(PARAM_CATEGORY_ID, categoryId);
        param.put(PARAM_SORT, PARAM_VALUE_LABEL);

        widgetInteractor.getNumberList(getNumberListSubscriber(categoryId, showLastOrder),
                AuthUtil.generateParamsNetwork(context, param));
    }

    private Subscriber<DigitalNumberList> getNumberListSubscriber(final String categoryId,
                                                                  final boolean showLastOrder) {
        return new Subscriber<DigitalNumberList>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                view.renderDefaultError();
            }

            @Override
            public void onNext(DigitalNumberList digitalNumberList) {
                view.renderNumberList(digitalNumberList.getOrderClientNumbers());
                if (showLastOrder) {
                    if (digitalNumberList.getLastOrder() != null) {
                        LastOrder lastOrder = mapOrderClientNumberToLastOrder(digitalNumberList
                                .getLastOrder());
                        view.renderLastOrder(lastOrder);
                    } else if (!TextUtils.isEmpty(getLastClientNumberTyped(categoryId))) {
                        view.renderLastTypedClientNumber(getLastClientNumberTyped(categoryId));
                    } else if (isPulsaOrPaketDataOrRoaming(categoryId) &
                            !TextUtils.isEmpty(SessionHandler.getPhoneNumber())) {
                        view.renderVerifiedNumber(SessionHandler.getPhoneNumber());
                    }
                }
            }
        };
    }

    private LastOrder mapOrderClientNumberToLastOrder(OrderClientNumber orderClientNumber) {
        LastOrder lastOrder = new LastOrder();
        Attributes attributes = new Attributes();
        attributes.setClientNumber(orderClientNumber.getClientNumber());
        attributes.setCategoryId(Integer.valueOf(orderClientNumber.getCategoryId()));
        attributes.setOperatorId(Integer.valueOf(orderClientNumber.getOperatorId()));
        if (!TextUtils.isEmpty(orderClientNumber.getProductId())) {
            attributes.setProductId(Integer.valueOf(orderClientNumber.getProductId()));
        }
        lastOrder.setAttributes(attributes);
        return lastOrder;
    }

    private boolean isPulsaOrPaketDataOrRoaming(String categoryId) {
        return (categoryId.equals(PULSA_CATEGORY_ID) ||
                categoryId.equals(PAKET_DATA_CATEGORY_ID) ||
                categoryId.equals(ROAMING_CATEGORY_ID));
    }

    @Override
    public void getOperatorById(String operatorId) {
        widgetInteractor.getOperatorById(getOperatorBydIdSubscriber(), operatorId);
    }

    private Subscriber<Operator> getOperatorBydIdSubscriber() {
        return new Subscriber<Operator>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.renderDefaultError();
            }

            @Override
            public void onNext(Operator operator) {
                view.renderOperator(operator);
            }
        };
    }

    @Override
    public void getOperatorAndProductsByPrefix(String phonePrefix, int categoryId, boolean validatePrefix) {
        widgetInteractor.getOperatorAndProductsFromPrefix(operatorAndProductsSubscriber(), categoryId,
                phonePrefix);
    }

    @Override
    public void getOperatorAndProductsByOperatorId(int categoryId, String operatorId) {
        widgetInteractor.getOperatorAndProductsByOperatorId(operatorAndProductsSubscriber(), categoryId, operatorId);
    }

    private Subscriber<Pair<Operator, List<Product>>> operatorAndProductsSubscriber() {
        return new Subscriber<Pair<Operator, List<Product>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.renderEmptyProduct(context.getString(R.string.error_message_product));
            }

            @Override
            public void onNext(Pair<Operator, List<Product>> pairOperatorProducts) {
                Operator operator = pairOperatorProducts.first;
                List<Product> products = pairOperatorProducts.second;

                if (operator != null) {
                    view.renderDataOperator(operator);

                    if (!products.isEmpty()) {
                        view.renderDataProducts(products, operator.getAttributes().getRule().isShowPrice());
                    } else {
                        view.renderEmptyProduct(context.getString(R.string.error_message_product));
                    }
                } else {
                    view.renderEmptyOperator(context.getString(R.string.error_message_operator));
                }
            }
        };
    }

    @Override
    public void fetchDefaultProduct(String categoryId, String operatorId, String productId) {
        widgetInteractor.getProductById(getProductSubscribe(), categoryId, operatorId, productId);
    }

    private Subscriber<Product> getProductSubscribe() {
        return new Subscriber<Product>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.renderErrorProduct(context.getString(R.string.error_message_not_found_product));
            }

            @Override
            public void onNext(Product product) {
                view.renderProduct(product);
            }
        };
    }
}