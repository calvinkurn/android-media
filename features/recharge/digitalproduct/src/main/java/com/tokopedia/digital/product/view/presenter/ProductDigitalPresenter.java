package com.tokopedia.digital.product.view.presenter;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.common_digital.atc.DigitalAddToCartUseCase;
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams;
import com.tokopedia.common_digital.atc.data.response.ResponseCartData;
import com.tokopedia.common_digital.atc.utils.DigitalAtcMapper;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.common.RechargeAnalytics;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.common.domain.interactor.GetDigitalCategoryByIdUseCase;
import com.tokopedia.digital.common.view.ViewFactory;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.common.view.presenter.BaseDigitalPresenter;
import com.tokopedia.digital.product.view.listener.IProductDigitalView;
import com.tokopedia.digital.product.view.model.BannerData;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.GuideData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.network.exception.ResponseDataNullException;
import com.tokopedia.network.exception.ResponseErrorException;
import com.tokopedia.user.session.UserSessionInterface;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import kotlin.Unit;
import rx.Subscriber;

import static com.tokopedia.digital.product.view.adapter.PromoGuidePagerAdapter.GUIDE_TAB;
import static com.tokopedia.digital.product.view.adapter.PromoGuidePagerAdapter.PROMO_TAB;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public class ProductDigitalPresenter extends BaseDigitalPresenter<IProductDigitalView>
        implements IProductDigitalPresenter {

    public static final int TAB_COUNT_ONE = 1;
    public static final int TAB_COUNT_TWO = 2;

    private static final String PULSA_CATEGORY_ID = "1";
    private static final String PAKET_DATA_CATEGORY_ID = "2";
    private static final String ROAMING_CATEGORY_ID = "20";
    private final String PARAM_VALUE_SORT = "label";
    private DigitalAnalytics digitalAnalytics;
    private RechargeAnalytics rechargeAnalytics;
    private GetDigitalCategoryByIdUseCase getDigitalCategoryByIdUseCase;
    private CategoryData categoryData;
    private DigitalAddToCartUseCase addToCartUseCase;
    private UserSessionInterface userSession;

    @Inject
    public ProductDigitalPresenter(
            DigitalAnalytics digitalAnalytics,
            RechargeAnalytics rechargeAnalytics,
            LocalCacheHandler localCacheHandler,
            GetDigitalCategoryByIdUseCase getDigitalCategoryByIdUseCase,
            com.tokopedia.common_digital.atc.DigitalAddToCartUseCase addToCartUseCase,
            UserSessionInterface userSession) {
        super(localCacheHandler, userSession);
        this.digitalAnalytics = digitalAnalytics;
        this.rechargeAnalytics = rechargeAnalytics;
        this.addToCartUseCase = addToCartUseCase;
        this.getDigitalCategoryByIdUseCase = getDigitalCategoryByIdUseCase;
        this.userSession = userSession;
    }

    @Override
    public void processGetCategoryAndBannerData(
            String categoryId, String operatorId, String productId, String clientNumber
    ) {
        getView().showInitialProgressLoading();

        getDigitalCategoryByIdUseCase.execute(getDigitalCategoryByIdUseCase.createRequestParam(
                categoryId, operatorId, productId, clientNumber, PARAM_VALUE_SORT, true
        ), getSubscriberProductDigitalData());
    }

    @Override
    public void getCategoryData(String categoryId, String operatorId, String productId, String clientNumber) {
        getView().showInitialProgressLoading();

        getDigitalCategoryByIdUseCase.execute(getDigitalCategoryByIdUseCase.createRequestParam(
                categoryId, operatorId, productId, clientNumber, PARAM_VALUE_SORT, true
        ), new Subscriber<ProductDigitalData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                handleCategoryError(e);
            }

            @Override
            public void onNext(ProductDigitalData productDigitalData) {
                getView().hideInitialProgressLoading();
                getView().sendOpenScreenEventTracking(productDigitalData.getCategoryData());
                getView().goToCartPage(productDigitalData);
            }
        });
    }

    @Override
    public void processStateDataToReRender() {
        CategoryData categoryData = getView().getCategoryDataState();
        List<BannerData> bannerDataList = getView().getBannerDataListState();
        List<BannerData> otherBannerDataList = getView().getOtherBannerDataListState();
        List<GuideData> guideDataList = getView().getGuideDataListState();
        HistoryClientNumber historyClientNumber = getView().getHistoryClientNumberState();
        if (categoryData != null) {
            renderCategoryDataAndBannerToView(
                    categoryData, bannerDataList, otherBannerDataList, guideDataList, historyClientNumber
            );
            getView().renderStateSelectedAllData();
        }
    }

    @Override
    public void addToCart(DigitalCheckoutPassData digitalCheckoutPassData,
                          RequestBodyIdentifier digitalIdentifierParam,
                          DigitalSubscriptionParams digitalSubscriptionParams) {

        getView().onBuyButtonLoading(true);
        addToCartUseCase.setRequestParams(
                DigitalAddToCartUseCase.Companion.getRequestBodyAtcDigital(
                        digitalCheckoutPassData,
                        Integer.parseInt(userSession.getUserId()),
                        digitalIdentifierParam,
                        digitalSubscriptionParams
                ), digitalCheckoutPassData.getIdemPotencyKey());

        addToCartUseCase.execute(
                (it) -> {
                    onSuccess(it, digitalCheckoutPassData);
                    return Unit.INSTANCE;
                }
                , (it) -> {
                    onError(it);
                    return Unit.INSTANCE;
                }, addToCartUseCase.getRequestParams());
    }

    private void onSuccess(Map<Type, ? extends RestResponse> responseMap, DigitalCheckoutPassData digitalCheckoutPassData) {
        Type token = new TypeToken<DataResponse<ResponseCartData>>() {
        }.getType();
        ResponseCartData responseCartData = ((DataResponse<ResponseCartData>) responseMap.get(token).getData()).getData();
        if (responseCartData != null && responseCartData.getId() != null) {
            rechargeAnalytics.eventAddToCart(DigitalAtcMapper.INSTANCE.mapToDigitalAtcTrackingModel(responseCartData,
                    digitalCheckoutPassData, userSession.getUserId()));
            getView().navigateToDigitalCart(digitalCheckoutPassData);
        } else onError(new Throwable());
        getView().onBuyButtonLoading(false);
    }

    private void onError(Throwable e) {
        getView().showSnackBar(handleCategoryToaster(e));
        getView().onBuyButtonLoading(false);
    }

    @NonNull
    private Subscriber<ProductDigitalData> getSubscriberProductDigitalData() {
        return new Subscriber<ProductDigitalData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                handleCategoryError(e);
            }

            @Override
            public void onNext(ProductDigitalData productDigitalData) {
                getView().hideInitialProgressLoading();
                CategoryData categoryData = productDigitalData.getCategoryData();
                List<BannerData> bannerDataList = productDigitalData.getBannerDataList();
                List<BannerData> otherBannerDataList = productDigitalData.getOtherBannerDataList();
                List<GuideData> guideDataList = productDigitalData.getGuideDataList();
                HistoryClientNumber historyClientNumber =
                        productDigitalData.getHistoryClientNumber();
                if (historyClientNumber.getLastOrderClientNumber() == null) {
                    String lastSelectedOperatorId = getLastOperatorSelected(categoryData.categoryId);
                    String lastSelectedProductId = getLastProductSelected(categoryData.categoryId);
                    String lastTypedClientNumber = getLastClientNumberTyped(categoryData.categoryId);
                    String verifiedNumber = userSession.getPhoneNumber();
                    if (!TextUtils.isEmpty(lastTypedClientNumber)) {
                        historyClientNumber.setLastOrderClientNumber(
                                new OrderClientNumber.Builder()
                                        .clientNumber(lastTypedClientNumber)
                                        .operatorId(lastSelectedOperatorId)
                                        .productId(lastSelectedProductId)
                                        .build());
                    } else if (isPulsaOrPaketDataOrRoaming(categoryData.categoryId) &
                            !TextUtils.isEmpty(verifiedNumber)) {
                        historyClientNumber.setLastOrderClientNumber(
                                new OrderClientNumber.Builder()
                                        .clientNumber(verifiedNumber)
                                        .build());
                    }
                }
                renderCategoryDataAndBannerToView(
                        categoryData, bannerDataList, otherBannerDataList, guideDataList, historyClientNumber
                );
                getView().sendOpenScreenEventTracking(productDigitalData.getCategoryData());
            }
        };
    }

    private String handleCategoryToaster(Throwable e) {
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            return ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL;
        } else if (e instanceof SocketTimeoutException) {
            return ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
        } else if (e instanceof ResponseErrorException
                || e instanceof ResponseDataNullException
                || e instanceof HttpErrorException) {
            return e.getMessage();
        } else {
            return ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
        }
    }

    private void handleCategoryError(Throwable e) {
        if (isViewAttached()) {
            getView().stopTrace();
            if (e instanceof UnknownHostException || e instanceof ConnectException) {
                /* Ini kalau ga ada internet */
                getView().renderErrorNoConnectionProductDigitalData(
                        ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                );
            } else if (e instanceof SocketTimeoutException) {
                /* Ini kalau timeout */
                getView().renderErrorTimeoutConnectionProductDigitalData(
                        ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                );
            } else if (e instanceof ResponseErrorException) {
                /* Ini kalau error dari API kasih message error */
                getView().renderErrorProductDigitalData(e.getMessage());
            } else if (e instanceof ResponseDataNullException) {
                /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                getView().renderErrorProductDigitalData(e.getMessage());
            } else if (e instanceof HttpErrorException) {
            /* Ini Http error, misal 403, 500, 404,
             code http errornya bisa diambil
             e.getErrorCode */
                getView().renderErrorHttpProductDigitalData(e.getMessage());
            } else {
                getView().renderErrorProductDigitalData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }
        }
    }

    private boolean isPulsaOrPaketDataOrRoaming(String categoryId) {
        return categoryId.equals(PULSA_CATEGORY_ID) || categoryId.equals(PAKET_DATA_CATEGORY_ID) ||
                categoryId.equals(ROAMING_CATEGORY_ID);
    }

    private void renderCategoryDataAndBannerToView(CategoryData categoryData,
                                                   List<BannerData> bannerDataList,
                                                   List<BannerData> otherBannerDataList,
                                                   List<GuideData> guideDataList,
                                                   HistoryClientNumber historyClientNumber) {
        this.categoryData = categoryData;
        if (categoryData.isSupportedStyle()) {
            BaseDigitalProductView digitalProductView = ViewFactory
                    .renderCategoryDataAndBannerToView(getView().getActivity(),
                            categoryData.operatorStyle);

            getView().renderCategory(digitalProductView, categoryData, historyClientNumber);

            if (!GlobalConfig.isSellerApp()) {
                getView().showPromoContainer();
                if (bannerDataList.size() > 0 && guideDataList.size() > 0) {
                    getView().renderPromoGuideTab(TAB_COUNT_TWO, PROMO_TAB);
                } else if (bannerDataList.size() > 0 && guideDataList.size() == 0) {
                    getView().renderPromoGuideTab(TAB_COUNT_ONE, PROMO_TAB);
                } else if (bannerDataList.size() == 0 && guideDataList.size() > 0) {
                    getView().renderPromoGuideTab(TAB_COUNT_ONE, GUIDE_TAB);
                }

                if (bannerDataList.size() > 0 || guideDataList.size() > 0) {
                    getView().showPromoGuideTab();
                    getView().renderBannerListData(
                            categoryData.name,
                            bannerDataList != null ? bannerDataList : new ArrayList<BannerData>()
                    );
                    getView().renderOtherBannerListData(
                            getView().getStringFromResource(R.string.other_promo),
                            otherBannerDataList != null ? otherBannerDataList : new ArrayList<BannerData>()
                    );
                    getView().renderGuideListData(
                            guideDataList != null ? guideDataList : new ArrayList<>()
                    );
                } else {
                    getView().hidePromoGuideTab();
                }
            }
        } else {
            getView().renderErrorStyleNotSupportedProductDigitalData(
                    getView().getStringFromResource(
                            R.string.message_error_digital_category_style_not_supported
                    )
            );
        }
        getView().stopTrace();
        renderCheckETollBalance();
    }

    private void renderCheckETollBalance() {
        if (!GlobalConfig.isSellerApp()
                && categoryData != null
                && categoryData.additionalFeature != null
                && categoryData.additionalFeature.getFeatureId() == 1
                && getView().isDigitalSmartcardEnabled()
                && getView().getActivity() != null) {
            getView().renderCheckETollBalance(categoryData.additionalFeature.getText(),
                    categoryData.additionalFeature.getButtonText());
        }
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
        passData.setSource(DigitalCheckoutPassData.Companion.getPARAM_NATIVE());
        passData.setFromPDP(true);
        return passData;
    }

    public void trackVisitRechargePushEventRecommendation(int categoryId) {
        rechargeAnalytics.trackVisitRechargePushEventRecommendation(categoryId);
    }
}