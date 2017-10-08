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
import com.tokopedia.digital.widget.listener.IDigitalWidgetStyle2View;
import com.tokopedia.digital.widget.model.DigitalNumberList;

import java.util.List;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 7/21/17.
 * Modified by rizkyfadillah at 10/6/17.
 */

public class DigitalWidgetStyle2Presenter extends BaseDigitalWidgetPresenter
        implements IDigitalWidgetStyle2Presenter {

    private final IDigitalWidgetInteractor widgetInteractor;
    private final IDigitalWidgetStyle2View view;
    private Context context;

    public DigitalWidgetStyle2Presenter(Context context,
                                        IDigitalWidgetInteractor widgetInteractor,
                                        IDigitalWidgetStyle2View view) {
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
                    view.renderDataProducts(products);
                } else {
                    view.renderEmptyProduct(context.getString(R.string.error_message_product));
                }
            }
        };
    }

    @Override
    public void getOperatorById(String operatorId) {
        widgetInteractor.getOperatorById(getOperatorModelSubscriber(), operatorId);
    }

    private Subscriber<RechargeOperatorModel> getOperatorModelSubscriber() {
        return new Subscriber<RechargeOperatorModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.renderDefaultError();
            }

            @Override
            public void onNext(RechargeOperatorModel rechargeOperatorModel) {
                view.renderOperator(rechargeOperatorModel);
            }
        };
    }

    @Override
    public void validateOperatorWithProducts(int categoryId, String operatorId) {
        widgetInteractor.getProductsFromOperator(getListProductSubscriber(), categoryId, operatorId);
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

    @Override
    public void fetchOperatorByCategory(int categoryId) {
        widgetInteractor.getOperatorsFromCategory(getOperatorByCategorySubscriber(), categoryId);
    }

    private Subscriber<List<RechargeOperatorModel>> getOperatorByCategorySubscriber() {
        return new Subscriber<List<RechargeOperatorModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.renderDefaultError();
            }

            @Override
            public void onNext(List<RechargeOperatorModel> rechargeOperatorModels) {
                if (rechargeOperatorModels.size() > 0)
                    view.renderOperators(rechargeOperatorModels);
                else
                    view.renderEmptyOperators(context.getString(R.string.error_message_operator));
            }
        };
    }

    @Override
    public void onDestroy() {
        widgetInteractor.onDestroy();
    }
}