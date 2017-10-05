package com.tokopedia.digital.widget.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.core.database.model.RechargeOperatorModel;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.database.recharge.recentOrder.LastOrderEntity;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.widget.interactor.IDigitalWidgetInteractor;
import com.tokopedia.digital.widget.listener.IDigitalWidgetStyle1View;
import com.tokopedia.digital.widget.model.DigitalNumberList;

import java.util.List;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 7/21/17.
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
        widgetInteractor.getNumberList(getNumberListSubscriber(),
                AuthUtil.generateParamsNetwork(context, param));
    }

    private Subscriber<DigitalNumberList> getNumberListSubscriber() {
        return new Subscriber<DigitalNumberList>() {
            @Override
            public void onCompleted() {
                Log.d("DigitalWidgetStyle1Presenter", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("DigitalWidgetStyle1Presenter", "onError: " + e.getMessage());
            }

            @Override
            public void onNext(DigitalNumberList digitalNumberList) {
                Log.d("DigitalWidgetStyle1Presenter", "onNext");
                view.renderNumberList(digitalNumberList.getOrderClientNumbers());
                if (digitalNumberList.getLastOrder() != null) {
                    LastOrder lastOrder = new LastOrder();
                    LastOrderEntity lastOrderEntity = new LastOrderEntity();
                    LastOrderEntity.AttributesBean attributesBean = new LastOrderEntity.AttributesBean();
                    attributesBean.setClient_number(digitalNumberList.getLastOrder().getClientNumber());
                    if (digitalNumberList.getLastOrder().getCategoryId() != null) {
                        attributesBean.setCategory_id(Integer.valueOf(digitalNumberList.getLastOrder().getCategoryId()));
                    } else {
                        Log.d("DigitalWidgetStylePresenter1", "categoryId is null");
                    }
                    if (digitalNumberList.getLastOrder().getOperatorId() != null) {
                        attributesBean.setOperator_id(Integer.valueOf(digitalNumberList.getLastOrder().getOperatorId()));
                    } else {
                        Log.d("DigitalWidgetStylePresenter1", "operatorId is null");
                    }
                    if (digitalNumberList.getLastOrder().getLastProduct() != null) {
                        attributesBean.setProduct_id(Integer.valueOf(digitalNumberList.getLastOrder().getLastProduct()));
                    } else {
                        Log.d("DigitalWidgetStylePresenter1", "lastProduct is null");
                    }
                    lastOrderEntity.setAttributes(attributesBean);
                    lastOrder.setData(lastOrderEntity);
                    view.renderLastOrder(lastOrder);
                } else {
                    view.renderLastOrder(getLastOrderFromCache());
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
                view.renderEmptyProduct("Product List is Empty");
            }

            @Override
            public void onNext(List<Product> products) {
                if (!products.isEmpty()) {
                    processOperatorById(products);
                    view.renderDataProducts(products);
                } else {
                    view.renderEmptyProduct("Product List is Empty");
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
                view.renderEmptyOperator("Empty operator");
            }

            @Override
            public void onNext(RechargeOperatorModel rechargeOperatorModel) {
                if (rechargeOperatorModel != null)
                    view.renderDataOperator(rechargeOperatorModel);
                else
                    view.renderEmptyOperator("Empty operator");
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
                view.renderEmptyProduct("Product List is Empty");
            }

            @Override
            public void onNext(List<Product> products) {
                if (!products.isEmpty()) {
                    processOperatorById(products);
                } else {
                    view.renderEmptyProduct("Product List is Empty");
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
                e.printStackTrace();
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