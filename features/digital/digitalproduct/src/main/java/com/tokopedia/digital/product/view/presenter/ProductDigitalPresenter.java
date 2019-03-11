package com.tokopedia.digital.product.view.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.RequestPermissionUtil;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.OperatorBuilder;
import com.tokopedia.common_digital.product.presentation.model.Validation;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.common.domain.interactor.GetDigitalCategoryByIdUseCase;
import com.tokopedia.digital.common.view.ViewFactory;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.common.view.presenter.BaseDigitalPresenter;
import com.tokopedia.digital.product.data.entity.requestbody.pulsabalance.Attributes;
import com.tokopedia.digital.product.data.entity.requestbody.pulsabalance.RequestBodyPulsaBalance;
import com.tokopedia.digital.product.domain.interactor.DigitalGetHelpUrlUseCase;
import com.tokopedia.digital.product.domain.interactor.IProductDigitalInteractor;
import com.tokopedia.digital.product.service.USSDAccessibilityService;
import com.tokopedia.digital.product.view.listener.IProductDigitalView;
import com.tokopedia.digital.product.view.model.BannerData;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.GuideData;
import com.tokopedia.digital.product.view.model.HistoryClientNumber;
import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
import com.tokopedia.digital.product.view.model.PulsaBalance;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.digital.utils.ServerErrorHandlerUtil;
import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.network.exception.ResponseDataNullException;
import com.tokopedia.network.exception.ResponseErrorException;
import com.tokopedia.user.session.UserSession;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.hansel.b.at;
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
    private static final String DIGITAL_USSD_MOBILE_NUMBER = "DIGITAL_USSD_MOBILE_NUMBER";
    private static final String KEY_USSD_SIM1 = "KEY_USSD_SIM1";
    private static final String KEY_USSD_SIM2 = "KEY_USSD_SIM2";
    private static final int MAX_SIM_COUNT = 2;
    //private String currentMobileNumber;
    private final static String simSlotName[] = {
            "extra_asus_dial_use_dualsim",
            "com.android.phone.extra.slot",
            "slot",
            "simslot",
            "sim_slot",
            "subscription",
            "Subscription",
            "phone",
            "com.android.phone.DialingMode",
            "simSlot",
            "slot_id",
            "simId",
            "simnum",
            "phone_type",
            "slotId",
            "slotIdx"
    };
    private final static String balance = "balance";
    private final String PARAM_VALUE_SORT = "label";
    //    private Activity activity;
    private DigitalAnalytics digitalAnalytics;
    private IProductDigitalInteractor productDigitalInteractor;
    private GetDigitalCategoryByIdUseCase getDigitalCategoryByIdUseCase;
    private DigitalGetHelpUrlUseCase digitalGetHelpUrlUseCase;
    private String slotKey = "com.android.phone.force.slot";
    private String accoutHandleKey = "android.telecom.extra.PHONE_ACCOUNT_HANDLE";
    private Handler ussdHandler;
    private int ussdTimeOutTime = 30 * 1000;
    private boolean ussdTimeOut = false;
    private CategoryData categoryData;
    private UserSession userSession;

    @Inject
    public ProductDigitalPresenter(
            DigitalAnalytics digitalAnalytics,
            LocalCacheHandler localCacheHandler,
            IProductDigitalInteractor productDigitalInteractor,
            GetDigitalCategoryByIdUseCase getDigitalCategoryByIdUseCase,
            DigitalGetHelpUrlUseCase digitalGetHelpUrlUseCase,
            UserSession userSession) {
        super(localCacheHandler, userSession);
        this.digitalAnalytics = digitalAnalytics;
        this.productDigitalInteractor = productDigitalInteractor;
        this.getDigitalCategoryByIdUseCase = getDigitalCategoryByIdUseCase;
        this.digitalGetHelpUrlUseCase = digitalGetHelpUrlUseCase;
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
                        categoryData, bannerDataList, otherBannerDataList, guideDataList, historyClientNumber
                );

                digitalAnalytics.sendCategoryScreen(getView().getActivity(), productDigitalData.getCategoryData().getName());
            }
        };
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
            } else if (e instanceof ServerErrorException) {
                getView().clearContentRendered();
                getView().closeView();
                ServerErrorHandlerUtil.handleError(e);
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
                            categoryData.getOperatorStyle());

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
                            categoryData.getName(),
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
        renderCheckPulsa();
    }

    @Override
    public void processToCheckBalance(String ussdMobileNumber, int simSlot, String ussdCode) {
        if (checkAccessibilitySettingsOn(getView().getActivity())) {
            if (ussdCode != null && !"".equalsIgnoreCase(ussdCode.trim())) {
                getView().registerUssdReciever();
                dailUssdToCheckBalance(simSlot, ussdCode);
            } else {
                getView().showMessageAlert(getView().getActivity().getString(R.string.error_message_ussd_msg_not_parsed),
                        getView().getActivity().getString(R.string.message_ussd_title));
                renderCheckPulsa();
                getView().showMessageAlert(getView().getActivity().getString(R.string.error_message_ussd_msg_not_parsed),
                        getView().getActivity().getString(R.string.message_ussd_title));
                digitalAnalytics.eventUssdAttempt( getView().getActivity().getString(R.string.status_failed_label) + getView().getActivity().getString(R.string.error_message_ussd_msg_not_parsed));
            }
        } else {
            getView().showAccessibilityAlertDialog();
        }
    }

    private void dailUssdToCheckBalance(int simPosition, String code) {
        String ussdCode = code.replace("#", Uri.encode("#"));
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdCode));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(slotKey, true);
        intent.putExtra("Cdma_Supp", true);
        //Add all slots here, according to device.. (different device require different key so put all together)
        for (String s : simSlotName)
            intent.putExtra(s, simPosition); //0 or 1 according to sim.......

        //works only for API >= 23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (DeviceUtil.getPhoneHandle(getView().getActivity(), simPosition) != null) {
                intent.putExtra(accoutHandleKey, DeviceUtil.getPhoneHandle(getView().getActivity(), simPosition));
            }
        }
//        if (RequestPermissionUtil.checkHasPermission(getView().getActivity(), Manifest.permission.CALL_PHONE)) {
//            getView().getActivity().startActivity(intent);
//        }
        ussdTimeOut = false;
        startUssdCheckBalanceTimer();
    }

    @Override
    public void processPulsaBalanceUssdResponse(String message, int selectedSim) {
        if (ussdTimeOut) {
            ussdTimeOut = false;
        } else {
            productDigitalInteractor.porcessPulsaUssdResponse(getRequestBodyPulsaBalance(message, selectedSim),
                    getSubscriberCheckPulsaBalance(selectedSim));
            removeUssdTimerCallback();
        }
    }

    @NonNull
    private Subscriber<PulsaBalance> getSubscriberCheckPulsaBalance(final int selectedSim) {
        return new Subscriber<PulsaBalance>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (getView() == null || getView().getActivity() == null) {
                    return;
                }
//                if (e instanceof UnknownHostException || e instanceof ConnectException) {
//                    /* Ini kalau ga ada internet */
//                    getView().showPulsaBalanceError(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
//                } else if (e instanceof SocketTimeoutException) {
//                    /* Ini kalau timeout */
//                    getView().showPulsaBalanceError(
//                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
//                    );
//                } else if (e instanceof ResponseErrorException) {
//                    /* Ini kalau error dari API kasih message error */
//                    getView().showPulsaBalanceError(e.getMessage());
//                } else if (e instanceof ResponseDataNullException) {
//                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
//                    getView().showPulsaBalanceError(e.getMessage());
//                } else if (e instanceof HttpErrorException) {
//                    /* Ini Http error, misal 403, 500, 404,
//                    code http errornya bisa diambil
//                    e.getErrorCode */
//                    getView().showPulsaBalanceError(e.getMessage());
//                } else if (e instanceof ServerErrorException) {
//                    ServerErrorHandlerUtil.handleError(e);
//                } else {
//                    getView().showPulsaBalanceError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
//                }
            }

            @Override
            public void onNext(PulsaBalance pulsaBalance) {
                if (getView() != null && getView().getActivity() != null) {
                    getView().renderPulsaBalance(pulsaBalance, selectedSim);
                }
            }
        };
    }

    private boolean checkAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName() + "/" + USSDAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    context.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "accessibility not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                    context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private RequestBodyPulsaBalance getRequestBodyPulsaBalance(String message, int selectedSim) {
        String number = getUssdPhoneNumberFromCache(selectedSim);
        if (number == null || "".equalsIgnoreCase(number.trim())) {
            number = getDeviceMobileNumber(selectedSim);
        }
        RequestBodyPulsaBalance requestBodyPulsaBalance = new RequestBodyPulsaBalance();
        requestBodyPulsaBalance.setType(balance);
        Attributes attributes = new Attributes();
        attributes.setOperatorId(parseStringToInt(getSelectedUssdOperator(selectedSim).getOperatorId()));
        attributes.setMessage(message);
        attributes.setClientNumber(number);
        attributes.setUserAgent(DeviceUtil.getUserAgentForApiCall());
        attributes.setIdentifier(getView().getDigitalIdentifierParam());
        requestBodyPulsaBalance.setAttributes(attributes);
        return requestBodyPulsaBalance;
    }

    @Override
    public List<Operator> getSelectedUssdOperatorList(int selectedSim) {
        List<Operator> selectedOperatorList = new ArrayList<>();
        String simOperatorName = DeviceUtil.getOperatorName(getView().getActivity(), selectedSim);
        CategoryData categoryData = getView().getCategoryDataState();
        if (categoryData != null && categoryData.getOperatorList() != null) {
            for (Operator operator : categoryData.getOperatorList()) {
                if (DeviceUtil.verifyUssdOperator(simOperatorName, operator.getName())) {
                    selectedOperatorList.add(operator);
                }
            }
        }
        return selectedOperatorList;
    }

    @Override
    public Operator getSelectedUssdOperator(int selectedSim) {
        String number = getUssdPhoneNumberFromCache(selectedSim);
        if (number == null || "".equalsIgnoreCase(number.trim())) {
            number = getDeviceMobileNumber(selectedSim);
        }
        List<Operator> selectedOperatorList = getSelectedUssdOperatorList(selectedSim);
        for (Operator operator : selectedOperatorList) {
            if (DeviceUtil.matchOperatorAndNumber(operator, number)) {
                return operator;
            }
        }
        if (selectedOperatorList.size() > 0)
            return selectedOperatorList.get(0);
        else
            return new OperatorBuilder().createOperator();
    }

    @Override
    public String getDeviceMobileNumber(int selectedSim) {
        return DeviceUtil.getMobileNumber(getView().getActivity(), selectedSim);
    }

    private int parseStringToInt(String source) {
        int result = 0;
        if (source != null) {
            try {
                result = Integer.parseInt(source);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private void startUssdCheckBalanceTimer() {
        ussdHandler = new Handler();
        ussdHandler.postDelayed(() -> {
            ussdTimeOut = true;
            if (getView() != null && getView().getActivity() != null) {
                getView().showPulsaBalanceError(getView().getActivity().getString(R.string.error_message_ussd_msg_not_parsed));
            }
        }, ussdTimeOutTime);
    }

    @Override
    public void removeUssdTimerCallback() {
        if (ussdHandler != null) {
            ussdHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public String getUssdPhoneNumberFromCache(int selectedSim) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(getView().getActivity(), DIGITAL_USSD_MOBILE_NUMBER);
        if (selectedSim == 0) {
            return localCacheHandler.getString(KEY_USSD_SIM1);
        } else if (selectedSim == 1) {
            return localCacheHandler.getString(KEY_USSD_SIM2);
        }
        return null;
    }

    @Override
    public void storeUssdPhoneNumber(int selectedSim, String number) {
        number = DeviceUtil.formatPrefixClientNumber(number);
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(getView().getActivity(), DIGITAL_USSD_MOBILE_NUMBER);
        if (selectedSim == 0) {
            localCacheHandler.putString(KEY_USSD_SIM1, number);
        } else if (selectedSim == 1) {
            localCacheHandler.putString(KEY_USSD_SIM2, number);
        }
        localCacheHandler.applyEditor();
    }

    @Override
    public boolean isCarrierSignalsNotAvailable(String carrierName) {
        final String noSignalStr = getView().getActivity().getString(R.string.label_no_signal);
        final String noServiceStr = getView().getActivity().getString(R.string.label_no_service);
        if (carrierName == null) {
            return false;
        }
        carrierName = carrierName.toLowerCase();
        return (carrierName.contains(noServiceStr.toLowerCase()) || carrierName.contains(noSignalStr.toLowerCase()));
    }

    @Override
    public void renderCheckPulsa() {
        if (!GlobalConfig.isSellerApp()
                && categoryData != null
                && categoryData.getSlug().equalsIgnoreCase(CategoryData.SLUG_PRODUCT_CATEGORY_PULSA)
                && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && getView().isUserLoggedIn()
                && getView().getActivity() != null) {
            getView().removeCheckPulsaCards();
            CategoryData categoryDataState = getView().getCategoryDataState();

            if (isOperatorListAvailable(categoryDataState)) {
                if (RequestPermissionUtil.checkHasPermission(getView().getActivity(), Manifest.permission.READ_PHONE_STATE)) {
                    List<Validation> validationList = categoryDataState.getClientNumberList().get(0).getValidation();
                    boolean isCheckUssdButtonActive = true;

                    for (int i = 0; i < MAX_SIM_COUNT; i++) {
                        String carrierName = DeviceUtil.getOperatorName(getView().getActivity(), i);
                        Operator operator = getSelectedUssdOperator(i);
                        String ussdCode = operator.getUssdCode();
                        if (carrierName != null) {

                            if (ussdCode == null || "".equalsIgnoreCase(ussdCode.trim())) {

                                //show the card if signal is not available with error message
                                if (isCarrierSignalsNotAvailable(carrierName)) {
                                    String operatorErrorMsg = getView().getActivity().getString(R.string.label_no_signal);
                                    carrierName = operatorErrorMsg;
                                    getView().renderCheckPulsaBalanceData(i, ussdCode,
                                            getPhoneNumberForSim(i, operator, validationList),
                                            operatorErrorMsg, true, carrierName);
                                    isCheckUssdButtonActive = true;
                                } else {
                                    //if check button was not active for previous sim, then do not show another card for inactive case
                                    if (isCheckUssdButtonActive || i != (MAX_SIM_COUNT - 1)) {
                                        getView().renderCheckPulsaBalanceData(i, ussdCode,
                                                getPhoneNumberForSim(i, operator, validationList),
                                                getView().getActivity().getString(R.string.label_operator_not_support),
                                                false, carrierName);
                                        isCheckUssdButtonActive = false;
                                    }
                                }
                            } else {
                                getView().renderCheckPulsaBalanceData(i, ussdCode, getPhoneNumberForSim(i, operator, validationList), null, true, carrierName);
                            }
                        }
                    }
                } else {
                    getView().renderCheckPulsaBalanceData(0, "", "", null, true, null);
                }
            }
        }
    }

    private void renderCheckETollBalance() {
        if (!GlobalConfig.isSellerApp()
                && categoryData != null
                && categoryData.getAdditionalFeature() != null
                && categoryData.getAdditionalFeature().getFeatureId() == 1
                && getView().isDigitalSmartcardEnabled()
                && getView().getActivity() != null) {
            getView().renderCheckETollBalance(categoryData.getAdditionalFeature().getText(),
                    categoryData.getAdditionalFeature().getButtonText());
        }
    }

    @Override
    public void processGetHelpUrlData(String categoryId) {
        digitalGetHelpUrlUseCase.execute(digitalGetHelpUrlUseCase.createRequest(categoryId),
                new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String url) {
                        if (url != null && url.length() > 0) {
                            getView().showHelpMenu(url);
                        }
                    }
                });
    }

    @Override
    public void onHelpMenuClicked() {
        if (getView().getHelpUrl() != null && getView().getHelpUrl().length() > 0) {
            getView().navigateToWebview(getView().getHelpUrl());
        }
    }

    private String getPhoneNumberForSim(int simIndex, Operator operator, List<Validation> validationList) {
        String phoneNumber = getUssdPhoneNumberFromCache(simIndex);
        if (!DeviceUtil.validateNumberAndMatchOperator(validationList, operator, phoneNumber)) {
            phoneNumber = getDeviceMobileNumber(simIndex);
            if (!DeviceUtil.validateNumberAndMatchOperator(validationList, operator, phoneNumber)) {
                phoneNumber = "";
            }
        }
        return phoneNumber;
    }

    private boolean isOperatorListAvailable(CategoryData categoryDataState) {
        return (categoryDataState != null &&
                categoryDataState.getOperatorList() != null &&
                categoryDataState.getOperatorList().size() != 0);
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
        return passData;
    }

}