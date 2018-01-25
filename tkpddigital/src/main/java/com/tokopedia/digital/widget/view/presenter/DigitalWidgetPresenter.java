package com.tokopedia.digital.widget.view.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.common.domain.DigitalCategoryUseCase;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
import com.tokopedia.digital.widget.view.ViewFactory;
import com.tokopedia.digital.widget.view.listener.IDigitalWidgetView;

import rx.Subscriber;

/**
 * Created by Rizky on 15/01/18.
 */

public class DigitalWidgetPresenter extends BaseDigitalPresenter implements IDigitalWidgetPresenter {

    private static final String PULSA_CATEGORY_ID = "1";
    private static final String PAKET_DATA_CATEGORY_ID = "2";
    private static final String ROAMING_CATEGORY_ID = "20";

    private final String PARAM_VALUE_SORT = "label";

    private Context context;
    private IDigitalWidgetView digitalWidgetView;
    private DigitalCategoryUseCase digitalCategoryUseCase;

    public DigitalWidgetPresenter(Context context,
                                  IDigitalWidgetView digitalWidgetView,
                                  DigitalCategoryUseCase digitalCategoryUseCase) {
        super(context);
        this.context = context;
        this.digitalWidgetView = digitalWidgetView;
        this.digitalCategoryUseCase = digitalCategoryUseCase;
    }

    @Override
    public void fetchCategory(String categoryId) {
        digitalCategoryUseCase.execute(digitalCategoryUseCase.createRequestParam(
                categoryId, PARAM_VALUE_SORT
        ), new Subscriber<ProductDigitalData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ProductDigitalData productDigitalData) {
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
                    .renderCategoryDataAndBannerToView(context,
                            categoryData.getOperatorStyle());

            digitalWidgetView.renderCategory(digitalProductView, categoryData, historyClientNumber);
        }
    }

    private boolean isPulsaOrPaketDataOrRoaming(String categoryId) {
        return categoryId.equals(PULSA_CATEGORY_ID) || categoryId.equals(PAKET_DATA_CATEGORY_ID) ||
                categoryId.equals(ROAMING_CATEGORY_ID);
    }

}
