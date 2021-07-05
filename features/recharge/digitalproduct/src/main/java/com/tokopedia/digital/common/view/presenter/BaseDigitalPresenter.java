package com.tokopedia.digital.common.view.presenter;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.digital.common.constant.DigitalCache;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.view.model.ContactData;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
import com.tokopedia.user.session.UserSessionInterface;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by nabillasabbaha on 8/8/17.
 * Modified by rizkyfadillah at 10/6/17.
 */

public abstract class BaseDigitalPresenter<T extends CustomerView> extends BaseDaggerPresenter<T> implements IBaseDigitalPresenter {

    private final String IDN_CALLING_CODE = "62";
    private final String IDN_CALLING_CODE_WITH_PLUS = "+62";

    private LocalCacheHandler localCacheHandlerLastClientNumber;
    private UserSessionInterface userSession;

    public BaseDigitalPresenter(LocalCacheHandler localCacheHandlerLastClientNumber, UserSessionInterface userSession) {
        this.localCacheHandlerLastClientNumber = localCacheHandlerLastClientNumber;
        this.userSession = userSession;
    }

    @Override
    public void storeLastClientNumberTyped(String categoryId, String operatorId, String clientNumber,
                                           String productId) {
        localCacheHandlerLastClientNumber.putString(
                DigitalCache.DIGITAL_CLIENT_NUMBER_CATEGORY + categoryId, clientNumber
        );
        localCacheHandlerLastClientNumber.putString(
                DigitalCache.DIGITAL_OPERATOR_ID_CATEGORY + categoryId,
                operatorId
        );
        localCacheHandlerLastClientNumber.putString(
                DigitalCache.DIGITAL_PRODUCT_ID_CATEGORY + categoryId,
                productId
        );
        localCacheHandlerLastClientNumber.applyEditor();
    }

    @Override
    public String getLastOperatorSelected(String categoryId) {
        return localCacheHandlerLastClientNumber.getString(
                DigitalCache.DIGITAL_OPERATOR_ID_CATEGORY + categoryId, "");
    }

    @Override
    public String getLastClientNumberTyped(String categoryId) {
        return localCacheHandlerLastClientNumber.getString(
                DigitalCache.DIGITAL_CLIENT_NUMBER_CATEGORY + categoryId, "");
    }

    @Override
    public String getLastProductSelected(String categoryId) {
        return localCacheHandlerLastClientNumber.getString(
                DigitalCache.DIGITAL_PRODUCT_ID_CATEGORY + categoryId, "");
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
        if (phoneFormatted.startsWith(IDN_CALLING_CODE)) {
            phoneFormatted = phoneFormatted.replaceFirst(IDN_CALLING_CODE, "0");
        }
        if (phoneFormatted.startsWith(IDN_CALLING_CODE_WITH_PLUS)) {
            phoneFormatted = phoneFormatted.replace(IDN_CALLING_CODE_WITH_PLUS, "0");
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
        String token = md5(timeMillis);
        return userLoginId + "_" + (token.isEmpty() ? timeMillis : token);
    }

    @Override
    public DigitalCheckoutPassData generateCheckoutPassData(
            BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct,
            String versionInfoApplication,
            String userLoginId
    ) {
        String clientNumber = preCheckoutProduct.getClientNumber();
        return new DigitalCheckoutPassData.Builder()
                .action(DigitalCheckoutPassData.Companion.getDEFAULT_ACTION())
                .categoryId(preCheckoutProduct.getCategoryId())
                .clientNumber(clientNumber)
                .instantCheckout(preCheckoutProduct.isInstantCheckout() ? "1" : "0")
                .isPromo(preCheckoutProduct.isPromo() ? "1" : "0")
                .operatorId(preCheckoutProduct.getOperatorId())
                .productId(preCheckoutProduct.getProductId())
                .utmCampaign((preCheckoutProduct.getCategoryName()))
                .utmContent(versionInfoApplication)
                .idemPotencyKey(generateATokenRechargeCheckout(userLoginId))
                .utmSource(DigitalCheckoutPassData.Companion.getUTM_SOURCE_ANDROID())
                .utmMedium(DigitalCheckoutPassData.Companion.getUTM_MEDIUM_WIDGET())
                .voucherCodeCopied(preCheckoutProduct.getVoucherCodeCopied())
                .build();
    }

    @Override
    public DigitalCheckoutPassData generateCheckoutPassData2(
            ProductDigitalData productDigitalData,
            String categoryId,
            String operatorId,
            String productId,
            String clientNumber,
            String versionInfoApplication,
            String userLoginId
    ) {
        return new DigitalCheckoutPassData.Builder()
                .action(DigitalCheckoutPassData.Companion.getDEFAULT_ACTION())
                .categoryId(categoryId)
                .clientNumber(clientNumber)
                .instantCheckout("0")
                .isPromo("0")
                .operatorId(operatorId)
                .productId(productId)
                .utmCampaign((productDigitalData.getCategoryData().name))
                .utmContent(versionInfoApplication)
                .idemPotencyKey(generateATokenRechargeCheckout(userLoginId))
                .utmSource(DigitalCheckoutPassData.Companion.getUTM_SOURCE_ANDROID())
                .utmMedium(DigitalCheckoutPassData.Companion.getUTM_MEDIUM_WIDGET())
                .voucherCodeCopied("")
                .build();
    }

    private String md5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b & 0xff));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

}