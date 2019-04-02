package com.tokopedia.digital.widget.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.digital.common.domain.interactor.GetDigitalCategoryByIdUseCase;
import com.tokopedia.digital.common.view.ViewFactory;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.common.view.presenter.BaseDigitalPresenter;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
import com.tokopedia.digital.widget.view.listener.IDigitalWidgetView;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author rizkyfadillah on 15/01/18.
 */

public class DigitalWidgetCategoryCategoryPresenter extends BaseDigitalPresenter<IDigitalWidgetView> implements IDigitalWidgetCategoryPresenter {

    private static final String PULSA_CATEGORY_ID = "1";
    private static final String PAKET_DATA_CATEGORY_ID = "2";
    private static final String ROAMING_CATEGORY_ID = "20";

    private final String PARAM_VALUE_SORT = "label";

    private UserSession userSession;
    private GetDigitalCategoryByIdUseCase getDigitalCategoryByIdUseCase;

    @Inject
    public DigitalWidgetCategoryCategoryPresenter(LocalCacheHandler localCacheHandler,
                                                  UserSession userSession,
                                                  GetDigitalCategoryByIdUseCase getDigitalCategoryByIdUseCase) {
        super(localCacheHandler, userSession);
        this.userSession = userSession;
        this.getDigitalCategoryByIdUseCase = getDigitalCategoryByIdUseCase;
    }

    @Override
    public void fetchCategory(String categoryId) {
        getView().showInitialProgressLoading();

        getDigitalCategoryByIdUseCase.execute(getDigitalCategoryByIdUseCase.createRequestParam(
                categoryId, PARAM_VALUE_SORT, true
        ), new Subscriber<ProductDigitalData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideInitialProgressLoading();
            }

            @Override
            public void onNext(ProductDigitalData productDigitalData) {
                getView().hideInitialProgressLoading();

                CategoryData categoryData = productDigitalData.getCategoryData();
                HistoryClientNumber historyClientNumber =
                        productDigitalData.getHistoryClientNumber();
                if (historyClientNumber.getLastOrderClientNumber() == null) {
                    String lastSelectedOperatorId = getLastOperatorSelected(categoryData.getCategoryId());
                    String lastSelectedProductId = getLastProductSelected(categoryData.getCategoryId());
                    String lastTypedClientNumber = getLastClientNumberTyped(categoryData.getCategoryId());
                    String verifiedNumber = userSession.getPhoneNumber();
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
                    .renderCategoryDataAndBannerToView(getView().getContext(),
                            categoryData.getOperatorStyle());

            getView().renderCategory(digitalProductView, categoryData, historyClientNumber);
        }
    }

    private boolean isPulsaOrPaketDataOrRoaming(String categoryId) {
        return categoryId.equals(PULSA_CATEGORY_ID) || categoryId.equals(PAKET_DATA_CATEGORY_ID) ||
                categoryId.equals(ROAMING_CATEGORY_ID);
    }

    @Override
    public void detachView() {
        super.detachView();
        getDigitalCategoryByIdUseCase.unsubscribe();
    }

    @Override
    public DigitalCheckoutPassData generateCheckoutPassData(
            BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct,
            String versionInfoApplication,
            String userLoginId
    ) {
        DigitalCheckoutPassData passData = super.generateCheckoutPassData(preCheckoutProduct,
                versionInfoApplication,
                userLoginId);
        passData.setSource(DigitalCheckoutPassData.Companion.getPARAM_WIDGET());
        return passData;
    }
}
