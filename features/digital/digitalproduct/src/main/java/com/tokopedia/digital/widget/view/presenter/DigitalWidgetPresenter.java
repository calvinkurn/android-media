package com.tokopedia.digital.widget.view.presenter;

import android.text.TextUtils;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.common_digital.product.domain.usecase.GetCategoryByIdUseCase;
import com.tokopedia.common_digital.product.presentation.model.CategoryData;
import com.tokopedia.common_digital.product.presentation.model.HistoryClientNumber;
import com.tokopedia.common_digital.product.presentation.model.OrderClientNumber;
import com.tokopedia.common_digital.product.presentation.model.ProductDigitalData;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.common.view.ViewFactory;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.common.view.presenter.BaseDigitalPresenter;
import com.tokopedia.digital.widget.view.listener.IDigitalWidgetView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author rizkyfadillah on 15/01/18.
 */

public class DigitalWidgetPresenter extends BaseDigitalPresenter implements IDigitalWidgetPresenter {

    private static final String PULSA_CATEGORY_ID = "1";
    private static final String PAKET_DATA_CATEGORY_ID = "2";
    private static final String ROAMING_CATEGORY_ID = "20";

    private final String PARAM_VALUE_SORT = "label";

    private IDigitalWidgetView digitalWidgetView;
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @Inject
    public DigitalWidgetPresenter(LocalCacheHandler localCacheHandler,
            GetCategoryByIdUseCase getCategoryByIdUseCase) {
        super(localCacheHandler);
        this.getCategoryByIdUseCase = getCategoryByIdUseCase;
    }

    @Override
    public void attachView(CustomerView view) {
        this.digitalWidgetView = (IDigitalWidgetView) view;
    }

    @Override
    public void fetchCategory(String categoryId) {
        digitalWidgetView.showInitialProgressLoading();

        getCategoryByIdUseCase.execute(getCategoryByIdUseCase.createRequestParam(
                categoryId, PARAM_VALUE_SORT, true
        ), new Subscriber<ProductDigitalData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                digitalWidgetView.hideInitialProgressLoading();
            }

            @Override
            public void onNext(ProductDigitalData productDigitalData) {
                digitalWidgetView.hideInitialProgressLoading();

                CategoryData categoryData = productDigitalData.getCategoryData();
                HistoryClientNumber historyClientNumber =
                        productDigitalData.getHistoryClientNumber();
                if (historyClientNumber.getLastOrderClientNumber() == null) {
                    String lastSelectedOperatorId = getLastOperatorSelected(categoryData.getCategoryId());
                    String lastSelectedProductId = getLastProductSelected(categoryData.getCategoryId());
                    String lastTypedClientNumber = getLastClientNumberTyped(categoryData.getCategoryId());
                    String verifiedNumber = SessionHandler.getPhoneNumber();
                    if (!TextUtils.isEmpty(lastTypedClientNumber)) {
                        historyClientNumber.setLastOrderClientNumber(
                                new OrderClientNumber.Builder()
                                        .clientNumber(lastTypedClientNumber)
                                        .operatorId(lastSelectedOperatorId)
                                        .productId(lastSelectedProductId)
                                        .build());
                    } else if (isPulsaOrPaketDataOrRoaming(categoryData.getCategoryId()) &
                            !TextUtils.isEmpty(verifiedNumber)) {
                        historyClientNumber.setLastOrderClientNumber(
                                new OrderClientNumber.Builder()
                                        .clientNumber(verifiedNumber)
                                        .build());
                    }
                }
                renderCategoryDataAndBannerToView(
                        categoryData, historyClientNumber
                );
            }
        });
    }

    private void renderCategoryDataAndBannerToView(CategoryData categoryData,
                                                   HistoryClientNumber historyClientNumber) {
        if (categoryData.isSupportedStyle()) {
            BaseDigitalProductView digitalProductView = ViewFactory
                    .renderCategoryDataAndBannerToView(digitalWidgetView.getContext(),
                            categoryData.getOperatorStyle());

            digitalWidgetView.renderCategory(digitalProductView, categoryData, historyClientNumber);
        }
    }

    private boolean isPulsaOrPaketDataOrRoaming(String categoryId) {
        return categoryId.equals(PULSA_CATEGORY_ID) || categoryId.equals(PAKET_DATA_CATEGORY_ID) ||
                categoryId.equals(ROAMING_CATEGORY_ID);
    }

    @Override
    public void detachView() {
        getCategoryByIdUseCase.unsubscribe();
    }

}
