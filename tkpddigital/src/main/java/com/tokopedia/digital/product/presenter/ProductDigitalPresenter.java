package com.tokopedia.digital.product.presenter;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

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
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.interactor.IProductDigitalInteractor;
import com.tokopedia.digital.product.listener.IProductDigitalView;
import com.tokopedia.digital.product.model.BannerData;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.ContactData;
import com.tokopedia.digital.product.model.HistoryClientNumber;
import com.tokopedia.digital.product.model.ProductDigitalData;
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
        paramQueryCategory.put(PARAM_IS_RESELLER, "1");
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
    }

}
