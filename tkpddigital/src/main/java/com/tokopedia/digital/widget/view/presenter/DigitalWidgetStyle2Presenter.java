package com.tokopedia.digital.widget.view.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.view.presenter.BaseDigitalPresenter;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.widget.domain.interactor.IDigitalWidgetInteractor;
import com.tokopedia.digital.widget.view.listener.IDigitalWidgetStyle2View;
import com.tokopedia.digital.widget.view.model.lastorder.Attributes;
import com.tokopedia.digital.widget.view.model.lastorder.LastOrder;
import com.tokopedia.digital.widget.view.model.operator.Operator;
import com.tokopedia.digital.widget.view.model.product.Product;
import com.tokopedia.digital.widget.view.model.DigitalNumberList;

import java.util.List;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 7/21/17.
 * Modified by rizkyfadillah at 10/6/17.
 */
@Deprecated
public class DigitalWidgetStyle2Presenter extends BaseDigitalPresenter
        implements IDigitalWidgetStyle2Presenter {

    private final IDigitalWidgetInteractor widgetInteractor;
    private final IDigitalWidgetStyle2View view;
    private Context context;

    public DigitalWidgetStyle2Presenter(Context context,
                                        LocalCacheHandler localCacheHandler,
                                        IDigitalWidgetInteractor widgetInteractor,
                                        IDigitalWidgetStyle2View view) {
        super(context, localCacheHandler);
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
                    } else if (getLastClientNumberTyped(categoryId) != null) {
                        view.renderLastTypedClientNumber(getLastClientNumberTyped(categoryId));
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
        widgetInteractor.getProductsByOperatorId(getListProductSubscriber(), categoryId, operatorId);
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
    public void getOperatorsByCategoryId(int categoryId, boolean showLastOrder) {
        widgetInteractor.getOperatorsByCategoryId(getOperatorByCategorySubscriber(showLastOrder), categoryId);
    }

    private Subscriber<List<Operator>> getOperatorByCategorySubscriber(final boolean showLastOrder) {
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
                if (rechargeOperatorModels.size() > 0) {
                    view.renderOperators(rechargeOperatorModels, showLastOrder);
                } else {
                    view.renderEmptyOperators(context.getString(R.string.error_message_operator));
                }
            }
        };
    }

    @Override
    public void detachView() {

    }
}