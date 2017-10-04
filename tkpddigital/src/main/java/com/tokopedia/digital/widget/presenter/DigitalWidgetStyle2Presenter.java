package com.tokopedia.digital.widget.presenter;

import android.content.Context;

import com.tokopedia.digital.R;
import com.tokopedia.digital.widget.interactor.IDigitalWidgetInteractor;
import com.tokopedia.digital.widget.listener.IDigitalWidgetStyle2View;
import com.tokopedia.digital.widget.model.operator.Operator;
import com.tokopedia.digital.widget.model.product.Product;

import java.util.List;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 7/21/17.
 */

public class DigitalWidgetStyle2Presenter extends BaseDigitalWidgetPresenter
        implements IDigitalWidgetStyle2Presenter {

    private final IDigitalWidgetInteractor widgetInteractor;

    private final IDigitalWidgetStyle2View view;

    private Context context;

    public DigitalWidgetStyle2Presenter(Context context,
                                        IDigitalWidgetInteractor widgetInteractor,
                                        IDigitalWidgetStyle2View view) {
        super(context);
        this.widgetInteractor = widgetInteractor;
        this.view = view;
        this.context = context;
    }

    @Override
    public void fetchRecentNumber(int categoryId) {
        widgetInteractor.getRecentData(getRecentListNumberSubscriber(), categoryId);
    }

    private Subscriber<List<String>> getRecentListNumberSubscriber() {
        return new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.renderDefaultError();
            }

            @Override
            public void onNext(List<String> results) {
                view.renderDataRecent(results);
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

    private Subscriber<Operator> getOperatorModelSubscriber() {
        return new Subscriber<Operator>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.renderDefaultError();
            }

            @Override
            public void onNext(Operator rechargeOperatorModel) {
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

    private Subscriber<List<Operator>> getOperatorByCategorySubscriber() {
        return new Subscriber<List<Operator>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.renderDefaultError();
            }

            @Override
            public void onNext(List<Operator> rechargeOperatorModels) {
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