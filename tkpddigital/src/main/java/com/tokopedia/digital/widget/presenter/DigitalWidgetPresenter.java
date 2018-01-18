package com.tokopedia.digital.widget.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.product.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.domain.DigitalCategoryUseCase;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.ContactData;
import com.tokopedia.digital.product.model.HistoryClientNumber;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.product.model.ProductDigitalData;
import com.tokopedia.digital.widget.listener.IDigitalWidgetView;

import rx.Subscriber;

/**
 * Created by Rizky on 15/01/18.
 */

public class DigitalWidgetPresenter extends BaseDigitalWidgetPresenter implements IDigitalWidgetPresenter {

    private final String PARAM_IS_RESELLER = "is_reseller";
    private final String PARAM_VALUE_IS_RESELLER = "1";

    private final String PARAM_CATEGORY_ID = "category_id";

    private final String PARAM_SORT = "sort";
    private final String PARAM_VALUE_SORT = "label";


    private static final String PULSA_CATEGORY_ID = "1";
    private static final String PAKET_DATA_CATEGORY_ID = "2";
    private static final String ROAMING_CATEGORY_ID = "20";

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
        TKPDMapParam<String, String> paramQueryCategory = new TKPDMapParam<>();
        if (GlobalConfig.isSellerApp()) {
            paramQueryCategory.put(PARAM_IS_RESELLER, PARAM_VALUE_IS_RESELLER);
        }

        TKPDMapParam<String, String> paramQueryFavoriteList = new TKPDMapParam<>();
        paramQueryFavoriteList.put(PARAM_CATEGORY_ID, categoryId);
        paramQueryFavoriteList.put(PARAM_SORT, PARAM_VALUE_SORT);

//        productDigitalInteractor.getCategoryAndBanner(
//                categoryId,
//                getGeneratedAuthParamNetwork(paramQueryCategory),
//                getGeneratedAuthParamNetwork(paramQueryFavoriteList),
//                new Subscriber<ProductDigitalData>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(ProductDigitalData productDigitalData) {
//                        CategoryData categoryData = productDigitalData.getCategoryData();
//                        HistoryClientNumber historyClientNumber =
//                                productDigitalData.getHistoryClientNumber();
//                        if (historyClientNumber.getLastOrderClientNumber() == null) {
//                            String lastSelectedOperatorId = getLastOperatorSelected(categoryData.getCategoryId());
//                            String lastSelectedProductId = getLastProductSelected(categoryData.getCategoryId());
//                            String lastTypedClientNumber = getLastClientNumberTyped(categoryData.getCategoryId());
//                            String verifiedNumber = SessionHandler.getPhoneNumber();
//                            if (!TextUtils.isEmpty(lastTypedClientNumber)) {
//                                historyClientNumber.setLastOrderClientNumber(
//                                        new OrderClientNumber.Builder()
//                                                .clientNumber(lastTypedClientNumber)
//                                                .operatorId(lastSelectedOperatorId)
//                                                .productId(lastSelectedProductId)
//                                                .build());
//                            } else if (isPulsaOrPaketDataOrRoaming(categoryData.getCategoryId()) &
//                                    !TextUtils.isEmpty(verifiedNumber)) {
//                                historyClientNumber.setLastOrderClientNumber(
//                                        new OrderClientNumber.Builder()
//                                                .clientNumber(verifiedNumber)
//                                                .build());
//                            }
//                        }
//                        renderCategoryDataAndBannerToView(
//                                categoryData, historyClientNumber
//                        );
//                    }
//                }
//        );

        digitalCategoryUseCase.execute(digitalCategoryUseCase.createRequestParam(
                categoryId, null, null, null, null, null
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
            switch (categoryData.getOperatorStyle()) {
                case CategoryData.STYLE_PRODUCT_CATEGORY_1:
                    digitalWidgetView.renderCategoryProductDataStyle1(
                            categoryData, historyClientNumber
                    );
                    break;
                case CategoryData.STYLE_PRODUCT_CATEGORY_2:
                case CategoryData.STYLE_PRODUCT_CATEGORY_99:
                    digitalWidgetView.renderCategoryProductDataStyle2(
                            categoryData, historyClientNumber
                    );
                    break;
                case CategoryData.STYLE_PRODUCT_CATEGORY_3:
                case CategoryData.STYLE_PRODUCT_CATEGORY_4:
                case CategoryData.STYLE_PRODUCT_CATEGORY_5:
                    digitalWidgetView.renderCategoryProductDataStyle3(
                            categoryData, historyClientNumber
                    );
                    break;
            }
        }
    }

    private boolean isPulsaOrPaketDataOrRoaming(String categoryId) {
        return categoryId.equals(PULSA_CATEGORY_ID) || categoryId.equals(PAKET_DATA_CATEGORY_ID) ||
                categoryId.equals(ROAMING_CATEGORY_ID);
    }

    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams) {
        return AuthUtil.generateParamsNetwork(context, originParams);
    }

    @Override
    public DigitalCheckoutPassData generateCheckoutPassData(
            BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct,
            String versionInfoApplication,
            String userLoginId
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
                .utmContent(versionInfoApplication)
                .idemPotencyKey(generateATokenRechargeCheckout(userLoginId))
                .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                .voucherCodeCopied(preCheckoutProduct.getVoucherCodeCopied())
                .build();
    }

    @Override
    public ContactData processGenerateContactDataFromUri(Uri contactURI, ContentResolver contentResolver) {
        String id = contactURI.getLastPathSegment();
        ContactData contact = new ContactData();
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

    @NonNull
    private String generateATokenRechargeCheckout(String userLoginId) {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = AuthUtil.md5(timeMillis);
        return userLoginId + "_" + (token.isEmpty() ? timeMillis : token);
    }

//    @Override
//    public void processStoreLastInputClientNumberByCategory(
//            String lastClientNumber, String categoryId, String operatorId, String productId,
//            LocalCacheHandler cacheHandlerLastInputClientNumber
//    ) {
//        LocalCacheHandler localCacheHandler = cacheHandlerLastInputClientNumber;
//        localCacheHandler.putString(
//                TkpdCache.Key.DIGITAL_CLIENT_NUMBER_CATEGORY + categoryId, lastClientNumber
//        );
//        localCacheHandler.putString(
//                TkpdCache.Key.DIGITAL_OPERATOR_ID_CATEGORY + categoryId, operatorId
//        );
//        localCacheHandler.putString(
//                TkpdCache.Key.DIGITAL_PRODUCT_ID_CATEGORY + categoryId, productId
//        );
//        localCacheHandler.applyEditor();
//    }

}
