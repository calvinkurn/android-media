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
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.GlobalConfig;
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
import com.tokopedia.digital.product.model.ProductDigitalData;
import com.tokopedia.digital.product.model.PulsaBalance;
import com.tokopedia.digital.product.service.USSDAccessibilityService;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.digital.utils.ServerErrorHandlerUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public class ProductDigitalPresenter implements IProductDigitalPresenter {

    private IProductDigitalView view;
    private IProductDigitalInteractor productDigitalInteractor;

    private String currentMobileNumber;
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
    private Handler ussdHandler;
    private int ussdTimeOutTime = 30 * 1000;
    private boolean ussdTimeOut = false;

    private final String PARAM_IS_RESELLER = "is_reseller";

    public ProductDigitalPresenter(IProductDigitalView view,
                                   IProductDigitalInteractor productDigitalInteractor) {
        this.view = view;
        this.productDigitalInteractor = productDigitalInteractor;
    }

    @Override
    public void processGetCategoryAndBannerData() {
        String categoryId = view.getCategoryId();
        TKPDMapParam<String, String> paramQueryCategory = new TKPDMapParam<>();
        if (GlobalConfig.isSellerApp()) {
            paramQueryCategory.put(PARAM_IS_RESELLER, "1");
        }
        TKPDMapParam<String, String> paramQueryBanner = new TKPDMapParam<>();
        paramQueryBanner.put("category_id", categoryId);
        view.showInitialProgressLoading();
        productDigitalInteractor.getCategoryAndBanner(
                view.getCategoryId(),
                view.getGeneratedAuthParamNetwork(paramQueryCategory),
                view.getGeneratedAuthParamNetwork(paramQueryBanner),
                view.getGeneratedAuthParamNetwork(new TKPDMapParam<String, String>()),
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
        HistoryClientNumber historyClientNumber = view.getHistoryClientNumberState();
        if (categoryData != null) {
            renderCategoryDataAndBannerToView(
                    categoryData, bannerDataList, historyClientNumber
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
                HistoryClientNumber historyClientNumber =
                        productDigitalData.getHistoryClientNumber();
                renderCategoryDataAndBannerToView(
                        categoryData, bannerDataList, historyClientNumber
                );
            }
        };
    }


    private void renderCategoryDataAndBannerToView(CategoryData categoryData,
                                                   List<BannerData> bannerDataList,
                                                   HistoryClientNumber historyClientNumber) {
        if (categoryData.isSupportedStyle()) {
            switch (categoryData.getOperatorStyle()) {
                case CategoryData.STYLE_PRODUCT_CATEGORY_1:
                case CategoryData.STYLE_PRODUCT_CATEGORY_99:
                    view.renderCategoryProductDataStyle1(
                            categoryData, historyClientNumber
                    );
                    break;
                case CategoryData.STYLE_PRODUCT_CATEGORY_2:
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
            }
        } else {
            view.renderErrorStyleNotSupportedProductDigitalData(
                    view.getStringFromResource(
                            R.string.message_error_digital_category_style_not_supported
                    )
            );
        }

        if (view.isUserLoggedIn() && categoryData.getSlug().equalsIgnoreCase(CategoryData.SLUG_PRODUCT_CATEGORY_PULSA))
            view.renderCheckPulsaBalanceData(null);

    }

    @Override
    public void processToCheckBalance(String ussdMobileNumber) {
        currentMobileNumber = ussdMobileNumber;
        if (checkAccessibilitySettingsOn(view.getActivity())) {
            if (ussdMobileNumber != null) {
                proccessUSSDCall();
            } else {
                String tempMobileNumber = getCurrentMobileNumber();
                if (tempMobileNumber != null && !tempMobileNumber.trim().equalsIgnoreCase("") && !tempMobileNumber.trim().equalsIgnoreCase("0") && !tempMobileNumber.trim().equalsIgnoreCase("0UNKNOWN")) {
                    proccessUSSDCall();
                } else {
                    view.showVerifyUssdOperatorDialogFragment();
                }
            }


        } else {
            view.showAccessibilityAlertDialog();
        }
    }

    private void proccessUSSDCall() {

        String ussd = getSelectedUssdOperator().getUssdCode();

        // Toast.makeText(view.getActivity(), getSelectedUssdOperator().getName() + "and mobile number is " + getCurrentMobileNumber(), Toast.LENGTH_SHORT).show();
        if (ussd != null && !"".equalsIgnoreCase(ussd.trim())) {
            view.registerUssdReciever();
            dailUssdToCheckBalance(ussd);
        } else {
            view.showMessageAlert(view.getActivity().getString(R.string.error_message_ussd_msg_not_parsed) + " and number is " + getCurrentMobileNumber() + " and Operator is " + getSelectedUssdOperator().getName(), view.getActivity().getString(R.string.message_ussd_title));

        }

    }

    private void dailUssdToCheckBalance(String code) {
        String ussdCode = code.replace("#", Uri.encode("#"));
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdCode));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("com.android.phone.force.slot", true);
        intent.putExtra("Cdma_Supp", true);
        //Add all slots here, according to device.. (different device require different key so put all together)
        for (String s : simSlotName)
            intent.putExtra(s, 0); //0 or 1 according to sim.......

        //works only for API >= 23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (DeviceUtil.getPhoneHandleSim1(view.getActivity()) != null) {
                intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", (Parcelable) DeviceUtil.getPhoneHandleSim1(view.getActivity()));
            }
        }
        if (RequestPermissionUtil.checkHasPermission(view.getActivity(), Manifest.permission.CALL_PHONE)) {
            view.getActivity().startActivity(intent);
        }
        ussdTimeOut = false;
        startUssdCheckBalanceTimer();
    }

    @Override
    public void processPulsaBalanceUssdResponse(String message) {
        if (ussdTimeOut) {
            ussdTimeOut = false;
        } else {
            productDigitalInteractor.porcessPulsaUssdResponse(getRequestBodyPulsaBalance(message), getSubscriberCheckPulsaBalance());
            ussdHandler.removeCallbacksAndMessages(null);
        }
    }

    @NonNull
    private Subscriber<PulsaBalance> getSubscriberCheckPulsaBalance() {
        return new Subscriber<PulsaBalance>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
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
                view.renderPulsaBalance(pulsaBalance);
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
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }

        return false;
    }

    private RequestBodyPulsaBalance getRequestBodyPulsaBalance(String message) {
        RequestBodyPulsaBalance requestBodyPulsaBalance = new RequestBodyPulsaBalance();
        requestBodyPulsaBalance.setType("balance");
        Attributes attributes = new Attributes();
        attributes.setOperatorId(parseStringToInt(getSelectedUssdOperator().getOperatorId()));
        attributes.setMessage(message);
        attributes.setClientNumber(getCurrentMobileNumber());
        attributes.setUserAgent(DeviceUtil.getUserAgentForApiCall());
        attributes.setIdentifier(view.getDigitalIdentifierParam());
        requestBodyPulsaBalance.setAttributes(attributes);

        return requestBodyPulsaBalance;

    }


    @Override
    public Operator getSelectedUssdOperator() {
        String tempMobileNumber = getCurrentMobileNumber();
        if (tempMobileNumber != null && !tempMobileNumber.trim().equalsIgnoreCase("") && !tempMobileNumber.trim().equalsIgnoreCase("0") && !tempMobileNumber.trim().equalsIgnoreCase("0UNKNOWN")) {
            CategoryData categoryData = view.getCategoryDataState();
            for (Operator operator : categoryData.getOperatorList()) {
                for (String prefix : operator.getPrefixList()) {
                    if (tempMobileNumber.startsWith(prefix)) {
                        return operator;
                    }
                }

            }
        }
        return new Operator();
    }

    @Override
    public String getCurrentMobileNumber() {
        if (currentMobileNumber == null) {
            currentMobileNumber = DeviceUtil.getMobileNumber(view.getActivity());
        }

        if (currentMobileNumber != null && currentMobileNumber.startsWith("+62")) {
            currentMobileNumber = currentMobileNumber.replace("+62", "");
        }

        if (currentMobileNumber != null && currentMobileNumber.startsWith("62")) {
            currentMobileNumber = currentMobileNumber.substring(2);
        }

        if (currentMobileNumber != null && !currentMobileNumber.startsWith("0")) {
            currentMobileNumber = "0" + currentMobileNumber;
        }
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
               // view.renderPulsaBalance(null);
                view.showPulsaBalanceError(view.getActivity().getString(R.string.error_message_ussd_msg_not_parsed));
            }
        }, ussdTimeOutTime);

    }
}

