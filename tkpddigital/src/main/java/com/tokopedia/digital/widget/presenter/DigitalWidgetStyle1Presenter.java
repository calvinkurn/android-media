package com.tokopedia.digital.widget.presenter;

import android.content.Context;

import com.tokopedia.core.database.model.RechargeOperatorModel;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.database.recharge.recentOrder.LastOrderEntity;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.R;
import com.tokopedia.digital.widget.interactor.IDigitalWidgetInteractor;
import com.tokopedia.digital.widget.listener.IDigitalWidgetStyle1View;
import com.tokopedia.digital.widget.model.DigitalNumberList;

import java.util.List;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 7/21/17.
 * Modified by rizkyfadillah at 10/6/17.
 */

public class DigitalWidgetStyle1Presenter extends BaseDigitalWidgetPresenter
        implements IDigitalWidgetStyle1Presenter {

    private final IDigitalWidgetInteractor widgetInteractor;
    private final IDigitalWidgetStyle1View view;
    private Context context;

    public DigitalWidgetStyle1Presenter(Context context,
                                        IDigitalWidgetInteractor widgetInteractor,
                                        IDigitalWidgetStyle1View view) {
        super(context, widgetInteractor, view);
        this.context = context;
        this.widgetInteractor = widgetInteractor;
        this.view = view;
    }

    @Override
    public void fetchNumberList(String categoryId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("category_id", categoryId);
        param.put("sort", "label");
        widgetInteractor.getNumberList(getNumberListSubscriber(categoryId),
                AuthUtil.generateParamsNetwork(context, param));
    }

    private Subscriber<DigitalNumberList> getNumberListSubscriber(final String categoryId) {
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
                if (digitalNumberList.getLastOrder() != null) {
                    LastOrder lastOrder = new LastOrder();
                    LastOrderEntity lastOrderEntity = new LastOrderEntity();
                    LastOrderEntity.AttributesBean attributesBean = new LastOrderEntity.AttributesBean();
                    attributesBean.setClient_number(digitalNumberList.getLastOrder().getClientNumber());
                    attributesBean.setCategory_id(Integer.valueOf(digitalNumberList.getLastOrder().getCategoryId()));
                    attributesBean.setOperator_id(Integer.valueOf(digitalNumberList.getLastOrder().getOperatorId()));
                    attributesBean.setProduct_id(Integer.valueOf(digitalNumberList.getLastOrder().getLastProduct()));
                    lastOrderEntity.setAttributes(attributesBean);
                    lastOrder.setData(lastOrderEntity);

                    view.renderLastOrder(lastOrder);
                } else if (getLastClientNumberTyped(categoryId) != null){
                    view.renderLastTypedClientNumber();
                }
            }
        };
    }

    @Override
    public void getOperatorById(String operatorId) {
        widgetInteractor.getOperatorById(getOperatorModelSubscriber(), operatorId);
    }

    @Override
    public void validatePhonePrefix(String phonePrefix, int categoryId, Boolean validatePrefix) {
        widgetInteractor.getProductsFromPrefix(getListProductSubscriber(), categoryId, phonePrefix,
                validatePrefix);
    }

    @Override
    public void validateOperatorWithProducts(int categoryId, String operatorId) {
        widgetInteractor.getProductsFromOperator(getListProductSubscriber(), categoryId, operatorId);
    }

    @Override
    public void validateOperatorWithoutProducts(int categoryId, String operatorId) {
        widgetInteractor.getProductsFromOperator(getListProductFromOperatorSubscriber(), categoryId, operatorId);
    }

    @Override
    public void fetchDefaultProduct(String categoryId, String operatorId, String productId) {
        widgetInteractor.getProductById(getProductSubscribe(), categoryId, operatorId, productId);
    }

    private void processOperatorById(List<Product> products) {
        String operatorId = String.valueOf(
                products.get(0).getRelationships().getOperator().getData().getId()
        );
        widgetInteractor.getOperatorById(getOperatorSubscriber(), operatorId);
    }

    private Subscriber<RechargeOperatorModel> getOperatorModelSubscriber() {
        return new Subscriber<RechargeOperatorModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.renderDefaultError();
            }

            @Override
            public void onNext(RechargeOperatorModel rechargeOperatorModel) {
                view.renderOperator(rechargeOperatorModel);
            }
        };
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

    private Subscriber<RechargeOperatorModel> getOperatorSubscriber() {
        return new Subscriber<RechargeOperatorModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.renderEmptyOperator(context.getString(R.string.error_message_operator));
            }

            @Override
            public void onNext(RechargeOperatorModel rechargeOperatorModel) {
                if (rechargeOperatorModel != null)
                    view.renderDataOperator(rechargeOperatorModel);
                else
                    view.renderEmptyOperator(context.getString(R.string.error_message_operator));
            }
        };
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

    @Override
    public void onDestroy() {
        widgetInteractor.onDestroy();
    }
}