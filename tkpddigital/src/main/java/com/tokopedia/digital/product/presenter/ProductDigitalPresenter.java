package com.tokopedia.digital.product.presenter;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.data.entity.requestbody.pulsabalance.Attributes;
import com.tokopedia.digital.product.data.entity.requestbody.pulsabalance.RequestBodyPulsaBalance;
import com.tokopedia.digital.product.interactor.IProductDigitalInteractor;
import com.tokopedia.digital.product.listener.IProductDigitalView;
import com.tokopedia.digital.product.model.BannerData;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.ContactData;
import com.tokopedia.digital.product.model.HistoryClientNumber;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.product.model.ProductDigitalData;
import com.tokopedia.digital.product.model.PulsaBalance;
import com.tokopedia.digital.product.model.Validation;
import com.tokopedia.digital.product.service.USSDAccessibilityService;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.digital.utils.ServerErrorHandlerUtil;
import com.tokopedia.digital.widget.presenter.BaseDigitalWidgetPresenter;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public class ProductDigitalPresenter extends BaseDigitalWidgetPresenter
        implements IProductDigitalPresenter {

    private static final String PULSA_CATEGORY_ID = "1";
    private static final String PAKET_DATA_CATEGORY_ID = "2";
    private static final String ROAMING_CATEGORY_ID = "20";
    private static final int MAX_SIM_COUNT = 2;

    private IProductDigitalView view;
    private IProductDigitalInteractor productDigitalInteractor;

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
    private String slotKey = "com.android.phone.force.slot";
    private String accoutHandleKey = "android.telecom.extra.PHONE_ACCOUNT_HANDLE";
    private Handler ussdHandler;
    private int ussdTimeOutTime = 30 * 1000;
    private boolean ussdTimeOut = false;

    private final String PARAM_IS_RESELLER = "is_reseller";
    private final String PARAM_VALUE_IS_RESELLER = "1";

    private final String PARAM_CATEGORY_ID = "category_id";
    private final String PARAM_OPERATOR_ID = "operator_id";
    private final String PARAM_CLIENT_NUMBER = "client_number";
    private final String PARAM_PRODUCT_ID = "product_id";

    private final String PARAM_SORT = "sort";
    private final String PARAM_VALUE_SORT = "label";

    private final static String balance = "balance";

    private CategoryData categoryData;

    public ProductDigitalPresenter(IProductDigitalView view,
                                   IProductDigitalInteractor productDigitalInteractor) {
        super(view.getActivity());
        this.view = view;
        this.productDigitalInteractor = productDigitalInteractor;
    }

    @Override
    public void processGetCategoryAndBannerData(
            String categoryId, String operatorId, String productId, String clientNumber
    ) {
        TKPDMapParam<String, String> paramQueryCategory = new TKPDMapParam<>();
        if (GlobalConfig.isSellerApp()) {
            paramQueryCategory.put(PARAM_IS_RESELLER, PARAM_VALUE_IS_RESELLER);
        }

        TKPDMapParam<String, String> paramQueryBanner = new TKPDMapParam<>();
        paramQueryBanner.put(PARAM_CATEGORY_ID, categoryId);

        TKPDMapParam<String, String> paramQueryNumberList = new TKPDMapParam<>();
        paramQueryNumberList.put(PARAM_CATEGORY_ID, categoryId);
        if (!TextUtils.isEmpty(operatorId)) {
            paramQueryNumberList.put(PARAM_OPERATOR_ID, operatorId);
        }
        if (!TextUtils.isEmpty(productId)) {
            paramQueryNumberList.put(PARAM_PRODUCT_ID, productId);
        }
        if (!TextUtils.isEmpty(clientNumber)) {
            paramQueryNumberList.put(PARAM_CLIENT_NUMBER, clientNumber);
        }
        paramQueryNumberList.put(PARAM_SORT, PARAM_VALUE_SORT);

        view.showInitialProgressLoading();

        productDigitalInteractor.getCategoryAndBanner(
                categoryId,
                view.getGeneratedAuthParamNetwork(paramQueryCategory),
                view.getGeneratedAuthParamNetwork(paramQueryBanner),
                view.getGeneratedAuthParamNetwork(paramQueryNumberList),
                view.getGeneratedAuthParamNetwork(new TKPDMapParam<String, String>()),
                getSubscriberProductDigitalData()
        );
    }

    @Override
    public void processStoreLastInputClientNumberByCategory(
            String lastClientNumber, String categoryId, String operatorId, String productId
    ) {
        LocalCacheHandler localCacheHandler = view.getLastInputClientNumberChaceHandler();
        localCacheHandler.putString(
                TkpdCache.Key.DIGITAL_CLIENT_NUMBER_CATEGORY + categoryId, lastClientNumber
        );
        localCacheHandler.putString(
                TkpdCache.Key.DIGITAL_OPERATOR_ID_CATEGORY + categoryId, operatorId
        );
        localCacheHandler.putString(
                TkpdCache.Key.DIGITAL_PRODUCT_ID_CATEGORY + categoryId, productId
        );
        localCacheHandler.applyEditor();
    }

    @Override
    public ContactData processGenerateContactDataFromUri(Uri contactURI) {
        String id = contactURI.getLastPathSegment();
        ContactData contact = new ContactData();
        ContentResolver contentResolver = view.getContentResolver();
        String contactWhere = ContactsContract.CommonDataKinds.Phone._ID + " = ? AND "
                + ContactsContract.Data.MIMETYPE + " = ?";

        String[] contactWhereParams = new String[]{
                id,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
        };
        Cursor cursorPhone = contentResolver.query(
                ContactsContract.Data.CONTENT_URI, null, contactWhere, contactWhereParams, null
        );

        if (cursorPhone != null) {
            if (cursorPhone.getCount() > 0) {
                if (cursorPhone.moveToNext()) {
                    if (Integer.parseInt(cursorPhone.getString(
                            cursorPhone.getColumnIndex(
                                    ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0
                            ) {
                        String givenName = cursorPhone.getString(
                                cursorPhone.getColumnIndex(
                                        ContactsContract.Contacts.DISPLAY_NAME
                                )
                        );

                        int contactType = cursorPhone.getInt(
                                cursorPhone.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.TYPE
                                )
                        );
                        contact.setContactNumber(cursorPhone.getString(cursorPhone.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                        )));
                        contact.setGivenName(givenName);
                        contact.setContactType(contactType);
                    }
                    cursorPhone.moveToNext();
                }
            }
            cursorPhone.close();
        }
        String phoneFormatted = contact.getContactNumber();
        if (phoneFormatted.startsWith("62")) {
            phoneFormatted = phoneFormatted.replaceFirst("62", "0");
        }
        if (phoneFormatted.startsWith("+62")) {
            phoneFormatted = phoneFormatted.replace("+62", "0");
        }
        phoneFormatted = phoneFormatted.replace(".", "");

        //noinspection ResultOfMethodCallIgnored
        phoneFormatted.replaceAll("[^0-9]+", "");
        contact.setContactNumber(phoneFormatted.replaceAll("\\D+", ""));
        return contact;
    }

    @Override
    public void processStateDataToReRender() {
        CategoryData categoryData = view.getCategoryDataState();
        List<BannerData> bannerDataList = view.getBannerDataListState();
        List<BannerData> otherBannerDataList = view.getOtherBannerDataListState();
        HistoryClientNumber historyClientNumber = view.getHistoryClientNumberState();
        if (categoryData != null) {
            renderCategoryDataAndBannerToView(
                    categoryData, bannerDataList, otherBannerDataList, historyClientNumber
            );
            view.renderStateSelectedAllData();
        }
    }

    @Override
    public void processAddToCartProduct(DigitalCheckoutPassData digitalCheckoutPassData) {
        if (view.isUserLoggedIn()) {
            if (view.getMainApplication() instanceof IDigitalModuleRouter) {
                IDigitalModuleRouter digitalModuleRouter =
                        (IDigitalModuleRouter) view.getMainApplication();
                view.navigateToActivityRequest(
                        digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassData),
                        IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL
                );
            }
        } else {
            view.interruptUserNeedLoginOnCheckout(digitalCheckoutPassData);
        }
    }

    @Override
    public DigitalCheckoutPassData generateCheckoutPassData(
            BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct
    ) {

        String clientNumber = preCheckoutProduct.getClientNumber();
        return new DigitalCheckoutPassData.Builder()
                .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                .categoryId(preCheckoutProduct.getCategoryId())
                .clientNumber(clientNumber)
                .instantCheckout(preCheckoutProduct.isInstantCheckout() ? "1" : "0")
                .isPromo(preCheckoutProduct.isPromo() ? "1" : "0")
                .operatorId(preCheckoutProduct.getOperatorId())
                .productId(preCheckoutProduct.getProductId())
                .utmCampaign((preCheckoutProduct.getCategoryName()))
                .utmContent(view.getVersionInfoApplication())
                .idemPotencyKey(generateATokenRechargeCheckout())
                .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                .voucherCodeCopied(preCheckoutProduct.getVoucherCodeCopied())
                .build();
    }

    @NonNull
    private String generateATokenRechargeCheckout() {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = AuthUtil.md5(timeMillis);
        return view.getUserLoginId() + "_" + (token.isEmpty() ? timeMillis : token);
    }

    @NonNull
    private Subscriber<ProductDigitalData> getSubscriberProductDigitalData() {
        return new Subscriber<ProductDigitalData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
            /* Ini kalau ga ada internet */
                    view.renderErrorNoConnectionProductDigitalData(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException) {
            /* Ini kalau timeout */
                    view.renderErrorTimeoutConnectionProductDigitalData(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
             /* Ini kalau error dari API kasih message error */
                    view.renderErrorProductDigitalData(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
            /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    view.renderErrorProductDigitalData(e.getMessage());
                } else if (e instanceof HttpErrorException) {
            /* Ini Http error, misal 403, 500, 404,
             code http errornya bisa diambil
             e.getErrorCode */
                    view.renderErrorHttpProductDigitalData(e.getMessage());
                } else if (e instanceof ServerErrorException) {
                    view.clearContentRendered();
                    view.closeView();
                    ServerErrorHandlerUtil.handleError(e);
                } else {
                    view.renderErrorProductDigitalData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(ProductDigitalData productDigitalData) {
                view.hideInitialProgressLoading();
                CategoryData categoryData = productDigitalData.getCategoryData();
                List<BannerData> bannerDataList = productDigitalData.getBannerDataList();
                List<BannerData> otherBannerDataList = productDigitalData.getOtherBannerDataList();
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
                        categoryData, bannerDataList, otherBannerDataList, historyClientNumber
                );
            }
        };
    }

    private boolean isPulsaOrPaketDataOrRoaming(String categoryId) {
        return categoryId.equals(PULSA_CATEGORY_ID) || categoryId.equals(PAKET_DATA_CATEGORY_ID) ||
                categoryId.equals(ROAMING_CATEGORY_ID);
    }

    private void renderCategoryDataAndBannerToView(CategoryData categoryData,
                                                   List<BannerData> bannerDataList,
                                                   List<BannerData> otherBannerDataList,
                                                   HistoryClientNumber historyClientNumber) {
        this.categoryData = categoryData;

        if (categoryData.isSupportedStyle()) {
            switch (categoryData.getOperatorStyle()) {
                case CategoryData.STYLE_PRODUCT_CATEGORY_1:
                    view.renderCategoryProductDataStyle1(
                            categoryData, historyClientNumber
                    );
                    break;
                case CategoryData.STYLE_PRODUCT_CATEGORY_2:
                case CategoryData.STYLE_PRODUCT_CATEGORY_99:
                    view.renderCategoryProductDataStyle2(
                            categoryData, historyClientNumber
                    );
                    break;
                case CategoryData.STYLE_PRODUCT_CATEGORY_3:
                case CategoryData.STYLE_PRODUCT_CATEGORY_4:
                case CategoryData.STYLE_PRODUCT_CATEGORY_5:
                    view.renderCategoryProductDataStyle3(
                            categoryData, historyClientNumber
                    );
                    break;
            }
            if (!GlobalConfig.isSellerApp()) {
                view.renderBannerListData(
                        categoryData.getName(),
                        bannerDataList != null ? bannerDataList : new ArrayList<BannerData>()
                );
                view.renderOtherBannerListData(
                        view.getStringFromResource(R.string.other_promo),
                        otherBannerDataList != null ? otherBannerDataList : new ArrayList<BannerData>()
                );
            }
        } else {
            view.renderErrorStyleNotSupportedProductDigitalData(
                    view.getStringFromResource(
                            R.string.message_error_digital_category_style_not_supported
                    )
            );
        }

        renderCheckPulsa();
    }

    @Override
    public void processToCheckBalance(String ussdMobileNumber, int simSlot, String ussdCode) {
        if (checkAccessibilitySettingsOn(view.getActivity())) {
            if (ussdCode != null && !"".equalsIgnoreCase(ussdCode.trim())) {
                view.registerUssdReciever();
                dailUssdToCheckBalance(simSlot, ussdCode);
            } else {
                view.showMessageAlert(view.getActivity().getString(R.string.error_message_ussd_msg_not_parsed), view.getActivity().getString(R.string.message_ussd_title));
                renderCheckPulsa();
            }
        } else {
            view.showAccessibilityAlertDialog();
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
            if (DeviceUtil.getPhoneHandle(view.getActivity(), simPosition) != null) {
                intent.putExtra(accoutHandleKey, (Parcelable) DeviceUtil.getPhoneHandle(view.getActivity(), simPosition));
            }
        }
        if (RequestPermissionUtil.checkHasPermission(view.getActivity(), Manifest.permission.CALL_PHONE)) {
            view.getActivity().startActivity(intent);
        }
        ussdTimeOut = false;
        startUssdCheckBalanceTimer();
    }

    @Override
    public void processPulsaBalanceUssdResponse(String message, int selectedSim) {
        if (ussdTimeOut) {
            ussdTimeOut = false;
        } else {
            productDigitalInteractor.porcessPulsaUssdResponse(getRequestBodyPulsaBalance(message, selectedSim), getSubscriberCheckPulsaBalance(selectedSim));
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
                if (view == null || view.getActivity() == null) {
                    return;
                }
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
            /* Ini kalau ga ada internet */
                    view.showPulsaBalanceError(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);

                } else if (e instanceof SocketTimeoutException) {
            /* Ini kalau timeout */
                    view.showPulsaBalanceError(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof ResponseErrorException) {
             /* Ini kalau error dari API kasih message error */
                    view.showPulsaBalanceError(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
            /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    view.showPulsaBalanceError(e.getMessage());
                } else if (e instanceof HttpErrorException) {
            /* Ini Http error, misal 403, 500, 404,
             code http errornya bisa diambil
             e.getErrorCode */
                    view.showPulsaBalanceError(e.getMessage());
                } else if (e instanceof ServerErrorException) {
                    ServerErrorHandlerUtil.handleError(e);
                } else {
                    view.showPulsaBalanceError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(PulsaBalance pulsaBalance) {
                if (view != null && view.getActivity() != null) {
                    view.renderPulsaBalance(pulsaBalance, selectedSim);
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
        attributes.setIdentifier(view.getDigitalIdentifierParam());
        requestBodyPulsaBalance.setAttributes(attributes);
        return requestBodyPulsaBalance;

    }


    @Override
    public List<Operator> getSelectedUssdOperatorList(int selectedSim) {
        List<Operator> selectedOperatorList = new ArrayList<>();
        String simOperatorName = DeviceUtil.getOperatorName(view.getActivity(), selectedSim);
        CategoryData categoryData = view.getCategoryDataState();
        if(categoryData !=null && categoryData.getOperatorList() != null) {
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
            return new Operator();
    }


    @Override
    public String getDeviceMobileNumber(int selectedSim) {
        String currentMobileNumber = null;
        currentMobileNumber = DeviceUtil.getMobileNumber(view.getActivity(), selectedSim);
        return currentMobileNumber;
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
        ussdHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ussdTimeOut = true;
                if (view != null && view.getActivity() != null) {
                    view.showPulsaBalanceError(view.getActivity().getString(R.string.error_message_ussd_msg_not_parsed));
                }
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
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(view.getActivity(), TkpdCache.DIGITAL_USSD_MOBILE_NUMBER);
        if (selectedSim == 0) {
            return localCacheHandler.getString(TkpdCache.Key.KEY_USSD_SIM1);
        } else if (selectedSim == 1) {
            return localCacheHandler.getString(TkpdCache.Key.KEY_USSD_SIM2);
        }
        return null;
    }

    @Override
    public void storeUssdPhoneNumber(int selectedSim, String number) {
        number = DeviceUtil.formatPrefixClientNumber(number);
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(view.getActivity(), TkpdCache.DIGITAL_USSD_MOBILE_NUMBER);
        if (selectedSim == 0) {
            localCacheHandler.putString(TkpdCache.Key.KEY_USSD_SIM1, number);
        } else if (selectedSim == 1) {
            localCacheHandler.putString(TkpdCache.Key.KEY_USSD_SIM2, number);
        }
        localCacheHandler.applyEditor();
    }

    @Override
    public boolean isCarrierSignalsNotAvailable(String carrierName) {
        final String noSignalStr = view.getActivity().getString(R.string.label_no_signal);
        final String noServiceStr = view.getActivity().getString(R.string.label_no_service);
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
                && view.isUserLoggedIn()) {
            view.removeCheckPulsaCards();
            CategoryData categoryDataState = view.getCategoryDataState();

            if (isOperatorListAvailable(categoryDataState)) {
                if (RequestPermissionUtil.checkHasPermission(view.getActivity(), Manifest.permission.READ_PHONE_STATE)) {
                    List<Validation> validationList = categoryDataState.getClientNumberList().get(0).getValidation();
                    boolean isCheckUssdButtonActive = true;

                    for (int i = 0; i < MAX_SIM_COUNT; i++) {
                        String carrierName = DeviceUtil.getOperatorName(view.getActivity(), i);
                        Operator operator = getSelectedUssdOperator(i);
                        String ussdCode = operator.getUssdCode();
                        if (carrierName != null) {

                            if (ussdCode == null || "".equalsIgnoreCase(ussdCode.trim())) {

                                //show the card if signal is not available with error message
                                if (isCarrierSignalsNotAvailable(carrierName)) {
                                    String operatorErrorMsg = view.getActivity().getString(R.string.label_no_signal);
                                    carrierName = operatorErrorMsg;
                                    view.renderCheckPulsaBalanceData(i, ussdCode, getPhoneNumberForSim(i, operator, validationList), operatorErrorMsg, true, carrierName);
                                    isCheckUssdButtonActive = true;
                                } else {
                                    //if check button was not active for previous sim, then do not show another card for inactive case
                                    if (isCheckUssdButtonActive || i != (MAX_SIM_COUNT - 1)) {
                                        view.renderCheckPulsaBalanceData(i, ussdCode, getPhoneNumberForSim(i, operator, validationList), view.getActivity().getString(R.string.label_operator_not_support), false, carrierName);
                                        isCheckUssdButtonActive = false;
                                    }
                                }
                            } else {
                                view.renderCheckPulsaBalanceData(i, ussdCode, getPhoneNumberForSim(i, operator, validationList), null, true, carrierName);
                            }
                        }
                    }
                } else {
                    view.renderCheckPulsaBalanceData(0, "", "", null, true, null);
                }
            }
        }
    }

    private String getPhoneNumberForSim(int simIndex, Operator operator, List<Validation> validationList){
        String phoneNumber = getUssdPhoneNumberFromCache(simIndex);
        if (!DeviceUtil.validateNumberAndMatchOperator(validationList, operator, phoneNumber)) {
            phoneNumber = getDeviceMobileNumber(simIndex);
            if (!DeviceUtil.validateNumberAndMatchOperator(validationList, operator, phoneNumber)) {
                phoneNumber = "";
            }
        }
        return phoneNumber;
    }

    private boolean isOperatorListAvailable(CategoryData categoryDataState){

        return (categoryDataState != null &&
                categoryDataState.getOperatorList() != null &&
                categoryDataState.getOperatorList().size() != 0);
    }
}

