package com.tokopedia.digital.product.presenter;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.product.interactor.IProductDigitalInteractor;
import com.tokopedia.digital.product.listener.IProductDigitalView;
import com.tokopedia.digital.product.model.CategoryData;
import com.tokopedia.digital.product.model.ContactData;
import com.tokopedia.digital.product.model.ProductDigitalData;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public class ProductDigitalPresenter implements IProductDigitalPresenter {

    private IProductDigitalView view;
    private IProductDigitalInteractor productDigitalInteractor;

    public ProductDigitalPresenter(IProductDigitalView view,
                                   IProductDigitalInteractor productDigitalInteractor) {
        this.view = view;
        this.productDigitalInteractor = productDigitalInteractor;
    }

    @Override
    public void processGetCategoryAndBannerData() {
        String categoryId = view.getCategoryId();
        TKPDMapParam<String, String> paramQueryCategory = new TKPDMapParam<>();
        TKPDMapParam<String, String> paramQueryBanner = new TKPDMapParam<>();
        paramQueryBanner.put("category_id", categoryId);
        productDigitalInteractor.getCategoryAndBanner(
                view.getCategoryId(),
                view.getGeneratedAuthParamNetwork(paramQueryCategory),
                view.getGeneratedAuthParamNetwork(paramQueryBanner),
                getSubscriberProductDigitalData()
        );
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
                ContactsContract.Data.CONTENT_URI,
                null,
                contactWhere,
                contactWhereParams,
                null
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
        contact.setContactNumber(phoneFormatted);
        return contact;
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
                } else {
            /* Ini diluar dari segalanya hahahaha */
                    view.renderErrorProductDigitalData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(ProductDigitalData productDigitalData) {
                if (productDigitalData.getCategoryData().isSupportedStyle()) {
                    switch (productDigitalData.getCategoryData().getOperatorStyle()) {
                        case CategoryData.STYLE_PRODUCT_CATEGORY_1:
                        case CategoryData.STYLE_PRODUCT_CATEGORY_99:
                            view.renderCategoryProductDataStyle1(productDigitalData.getCategoryData());
                            break;
                        case CategoryData.STYLE_PRODUCT_CATEGORY_2:
                            view.renderCategoryProductDataStyle2(productDigitalData.getCategoryData());
                            break;
                        case CategoryData.STYLE_PRODUCT_CATEGORY_3:
                        case CategoryData.STYLE_PRODUCT_CATEGORY_4:
                        case CategoryData.STYLE_PRODUCT_CATEGORY_5:
                            view.renderCategoryProductDataStyle3(productDigitalData.getCategoryData());
                            break;
                    }
                    view.renderBannerListData(
                            productDigitalData.getCategoryData().getName(),
                            productDigitalData.getBannerDataList()
                    );
                } else {
                    view.renderErrorStyleNotSupportedProductDigitalData("");
                }

            }
        };
    }
}
