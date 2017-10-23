package com.tokopedia.digital.widget.presenter;

import android.content.Context;
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
        param.put("category_id", categoryId);
        param.put("sort", "label");
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
                        view.renderLastTypedClientNumber();
                    } else if (isPulsaOrPaketData(categoryId) &
                            !TextUtils.isEmpty(SessionHandler.getPhoneNumber())) {
                        view.renderVerifiedNumber();
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
        if (!TextUtils.isEmpty(orderClientNumber.getLastProduct())) {
            attributes.setProductId(Integer.valueOf(orderClientNumber.getLastProduct()));
        }
        lastOrder.setAttributes(attributes);
        return lastOrder;
    }

    private boolean isPulsaOrPaketData(String categoryId) {
        return (categoryId.equals(PULSA_CATEGORY_ID) ||
                categoryId.equals(PAKET_DATA_CATEGORY_ID));
    }

    @Override
    public void getOperatorById(String operatorId) {
        widgetInteractor.getOperatorById(getOperatorModelSubscriber(), operatorId);
    }

    private Subscriber<Operator> getOperatorModelSubscriber() {
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
    public void validatePhonePrefix(String phonePrefix, int categoryId, Boolean validatePrefix) {
        widgetInteractor.getProductsFromPrefix(getListProductSubscriber(), categoryId, phonePrefix,
                validatePrefix);
    }

    private Subscriber<List<Product>> getListProductSubscriber() {
        return new Subscriber<List<Product>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.renderEmptyProduct(context.getString(R.string.error_message_product));
            }

            @Override
            public void onNext(List<Product> products) {
                if (!products.isEmpty()) {
                    processOperatorById(products);
                    view.renderDataProducts(products);
                } else {
                    view.renderEmptyProduct(context.getString(R.string.error_message_product));
                }
            }
        };
    }

    private void processOperatorById(List<Product> products) {
        String operatorId = String.valueOf(
                products.get(0).getRelationships().getOperator().getData().getId());
        widgetInteractor.getOperatorById(getOperatorSubscriber(), operatorId);
    }

    private Subscriber<Operator> getOperatorSubscriber() {
        return new Subscriber<Operator>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.renderEmptyOperator(context.getString(R.string.error_message_operator));
            }

            @Override
            public void onNext(Operator operator) {
                if (operator != null)
                    view.renderDataOperator(operator);
                else
                    view.renderEmptyOperator(context.getString(R.string.error_message_operator));
            }
        };
    }

    @Override
    public void validateOperatorWithProducts(int categoryId, String operatorId) {
        widgetInteractor.getProductsFromOperator(getListProductSubscriber(), categoryId, operatorId);
    }

    @Override
    public void validateOperatorWithoutProducts(int categoryId, String operatorId) {
        widgetInteractor.getProductsFromOperator(getListProductFromOperatorSubscriber(), categoryId, operatorId);
    }

    private Subscriber<List<Product>> getListProductFromOperatorSubscriber() {
        return new Subscriber<List<Product>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.renderEmptyProduct(context.getString(R.string.error_message_product));
            }

            @Override
            public void onNext(List<Product> products) {
                if (!products.isEmpty()) {
                    processOperatorById(products);
                } else {
                    view.renderEmptyProduct(context.getString(R.string.error_message_product));
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