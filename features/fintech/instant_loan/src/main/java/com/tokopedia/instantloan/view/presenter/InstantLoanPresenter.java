package com.tokopedia.instantloan.view.presenter;

import android.os.Environment;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.instantloan.constant.DeviceDataKeys;
import com.tokopedia.instantloan.ddcollector.DDCollectorManager;
import com.tokopedia.instantloan.ddcollector.OnDeviceDataReady;
import com.tokopedia.instantloan.ddcollector.PermissionResultCallback;
import com.tokopedia.instantloan.ddcollector.account.Account;
import com.tokopedia.instantloan.ddcollector.app.Application;
import com.tokopedia.instantloan.ddcollector.bdd.BasicDeviceData;
import com.tokopedia.instantloan.ddcollector.call.Call;
import com.tokopedia.instantloan.ddcollector.contact.Contact;
import com.tokopedia.instantloan.ddcollector.sms.Sms;
import com.tokopedia.instantloan.domain.interactor.GetLoanProfileStatusUseCase;
import com.tokopedia.instantloan.domain.interactor.PostPhoneDataUseCase;
import com.tokopedia.instantloan.domain.model.LoanProfileStatusModelDomain;
import com.tokopedia.instantloan.domain.model.PhoneDataModelDomain;
import com.tokopedia.instantloan.view.contractor.InstantLoanContractor;
import com.tokopedia.instantloan.view.mapper.LoanStatusMapper;
import com.tokopedia.instantloan.view.mapper.PhoneDataMapper;
import com.tokopedia.instantloan.view.model.LoanProfileStatusViewModel;
import com.tokopedia.instantloan.view.model.PhoneDataViewModel;
import com.tokopedia.usecase.RequestParams;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lavekush on 19/03/18.
 */

public class InstantLoanPresenter extends BaseDaggerPresenter<InstantLoanContractor.View>
        implements InstantLoanContractor.Presenter {

    private LoanStatusMapper mMapper;
    private PhoneDataMapper mPhoneDataMapper;
    private GetLoanProfileStatusUseCase mGetLoanProfileStatusUseCase;
    private PostPhoneDataUseCase mPostPhoneDataUseCase;

    @Inject
    public InstantLoanPresenter(GetLoanProfileStatusUseCase getLoanProfileStatusUseCase, PostPhoneDataUseCase mPostPhoneDataUseCase, LoanStatusMapper mapper, PhoneDataMapper phoneDataMapper) {
        this.mMapper = mapper;
        this.mPhoneDataMapper = phoneDataMapper;
        this.mGetLoanProfileStatusUseCase = getLoanProfileStatusUseCase;
        this.mPostPhoneDataUseCase = mPostPhoneDataUseCase;
    }

    @Override
    public void attachView(InstantLoanContractor.View view) {
        super.attachView(view);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void getLoanProfileStatus() {
        getView().showLoader();
        mGetLoanProfileStatusUseCase.getExecuteObservable(RequestParams.EMPTY).map(new Func1<LoanProfileStatusModelDomain, LoanProfileStatusViewModel>() {
            @Override
            public LoanProfileStatusViewModel call(LoanProfileStatusModelDomain bannerDomains) {
                return mMapper.transform(bannerDomains);

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoanProfileStatusViewModel>() {
                    @Override
                    public void onCompleted() {
                        getView().hideLoader();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onErrorLoanProfileStatus(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                    }

                    @Override
                    public void onNext(LoanProfileStatusViewModel viewModel) {
                        getView().onSuccessLoanProfileStatus(viewModel);
                    }
                });
    }

    @Override
    public void postPhoneData(String userId) {

    }

    @Override
    public boolean isUserLoggedIn() {
        return SessionHandler.isV2Login(getView().getAppContext());
    }

    @Override
    public void startDataCollection() {
        getView().showLoaderIntroDialog();
        DDCollectorManager.getsInstance().init(getView().getActivityContext(), mPermissionRequestCallback);
        DDCollectorManager.getsInstance().process(new OnDeviceDataReady() {
            @Override
            public void callback(Map<String, Object> data) {
                mPostPhoneDataUseCase.setBody(getPhoneDataPayload(data));
                mPostPhoneDataUseCase.getExecuteObservable(RequestParams.EMPTY).map(new Func1<PhoneDataModelDomain, PhoneDataViewModel>() {
                    @Override
                    public PhoneDataViewModel call(PhoneDataModelDomain modelDomain) {
                        return mPhoneDataMapper.transform(modelDomain);

                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<PhoneDataViewModel>() {
                            @Override
                            public void onCompleted() {
                                getView().hideLoaderIntroDialog();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().onErrorPhoneDataUploaded(ErrorHandler.getErrorMessage(getView().getAppContext(), e));
                            }

                            @Override
                            public void onNext(PhoneDataViewModel viewModel) {
                                getView().onSuccessPhoneDataUploaded(viewModel);
                            }
                        });
            }
        });
    }

    private JsonObject getPhoneDataPayload(Map<String, Object> map) {

        JsonObject data = new JsonObject();

        data.addProperty(DeviceDataKeys.BRAND, ((ArrayList<Map<String, String>>) map.get(BasicDeviceData.DD_BASIC_DEVICE_DATA)).get(0).get(BasicDeviceData.BRAND));
        data.addProperty(DeviceDataKeys.DEVICE_ID, ((ArrayList<Map<String, String>>) map.get(BasicDeviceData.DD_BASIC_DEVICE_DATA)).get(0).get(BasicDeviceData.DEVICE_ID));
        data.addProperty(DeviceDataKeys.DEVICE_SDK_VERSION, ((ArrayList<Map<String, String>>) map.get(BasicDeviceData.DD_BASIC_DEVICE_DATA)).get(0).get(BasicDeviceData.DEVICE_SDK_VERSION));
        data.addProperty(DeviceDataKeys.DEVICE_SYSTEM_VERSION, ((ArrayList<Map<String, String>>) map.get(BasicDeviceData.DD_BASIC_DEVICE_DATA)).get(0).get(BasicDeviceData.DEVICE_SYSTEM_VERSION));
        data.addProperty(DeviceDataKeys.GOOGLE_GAID, ((ArrayList<Map<String, String>>) map.get(BasicDeviceData.DD_BASIC_DEVICE_DATA)).get(0).get(BasicDeviceData.GOOGLE_GAID));
        data.addProperty(DeviceDataKeys.IMEI, ((ArrayList<Map<String, String>>) map.get(BasicDeviceData.DD_BASIC_DEVICE_DATA)).get(0).get(BasicDeviceData.IMEI));
        data.addProperty(DeviceDataKeys.LATITUDE, ((ArrayList<Map<String, String>>) map.get(BasicDeviceData.DD_BASIC_DEVICE_DATA)).get(0).get(BasicDeviceData.LATITUDE));
        data.addProperty(DeviceDataKeys.LONGITUDE, ((ArrayList<Map<String, String>>) map.get(BasicDeviceData.DD_BASIC_DEVICE_DATA)).get(0).get(BasicDeviceData.LONGITUDE));
        data.addProperty(DeviceDataKeys.MODEL, ((ArrayList<Map<String, String>>) map.get(BasicDeviceData.DD_BASIC_DEVICE_DATA)).get(0).get(BasicDeviceData.MODEL));
        data.addProperty(DeviceDataKeys.SYSTEM_LANGUAGE, ((ArrayList<Map<String, String>>) map.get(BasicDeviceData.DD_BASIC_DEVICE_DATA)).get(0).get(BasicDeviceData.SYSTEM_LANGUAGE));

        JsonArray messages = new JsonArray();
        JsonObject message;

        for (Map<String, String> entry : (List<HashMap<String, String>>) map.get(Sms.DD_SMS)) {
            if (entry == null) {
                continue;
            }
            message = new JsonObject();
            message.addProperty(DeviceDataKeys.Sms.PHONE, entry.get(Telephony.Sms.Inbox.ADDRESS));
            message.addProperty(DeviceDataKeys.Sms.CONTENT, entry.get(Telephony.Sms.Inbox.BODY));
            message.addProperty(DeviceDataKeys.Sms.TYPE, entry.get(Telephony.Sms.Inbox.TYPE));
            message.addProperty(DeviceDataKeys.Sms.TIME, entry.get(Telephony.Sms.Inbox.DATE));
            messages.add(message);
            if (messages.size() == 100) {
                break;
            }
        }

        data.addProperty(DeviceDataKeys.SMS, messages.toString());

        JsonArray contacts = new JsonArray();
        JsonObject contact;

        for (Map<String, String> entry : (List<HashMap<String, String>>) map.get(Contact.DD_CONTACT)) {
            if (entry == null) {
                continue;
            }
            contact = new JsonObject();
            contact.addProperty(DeviceDataKeys.Contact.NAME, entry.get(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            contact.addProperty(DeviceDataKeys.Contact.PHONE, entry.get(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contact.addProperty(DeviceDataKeys.Contact.TIME, entry.get(ContactsContract.CommonDataKinds.Phone.CONTACT_STATUS_TIMESTAMP));
            contact.addProperty(DeviceDataKeys.Contact.TIMES, entry.get(ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED));
            contact.addProperty(DeviceDataKeys.Contact.LAST_TIME, entry.get(ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED));
            contacts.add(contact);
        }

        data.addProperty(DeviceDataKeys.CONTACT, contacts.toString());

        JsonArray callLogs = new JsonArray();
        JsonObject callLog;

        for (Map<String, String> entry : (List<HashMap<String, String>>) map.get(Call.DD_CALL)) {
            if (entry == null) {
                continue;
            }
            callLog = new JsonObject();
            callLog.addProperty(DeviceDataKeys.Call.PHONE, entry.get(CallLog.Calls.NUMBER));
            callLog.addProperty(DeviceDataKeys.Call.TYPE, entry.get(CallLog.Calls.TYPE));
            callLog.addProperty(DeviceDataKeys.Call.DURATION, entry.get(CallLog.Calls.DURATION));
            callLog.addProperty(DeviceDataKeys.Call.TIME, entry.get(CallLog.Calls.DATE));
            callLogs.add(callLog);
        }

        data.addProperty(DeviceDataKeys.CALL, callLogs.toString());

        JsonArray accounts = new JsonArray();
        JsonObject account;

        for (Map<String, String> entry : (List<HashMap<String, String>>) map.get(Account.DD_ACCOUNT)) {
            if (entry == null) {
                continue;
            }
            account = new JsonObject();
            account.addProperty(DeviceDataKeys.Account.NAME, entry.get(Account.NAME));
            account.addProperty(DeviceDataKeys.Account.TYPE, entry.get(Account.TYPE));
            accounts.add(account);
        }

        data.addProperty(DeviceDataKeys.ACCOUNTS, accounts.toString());

        JsonArray apps = new JsonArray();
        JsonObject app;

        for (Map<String, String> entry : (List<HashMap<String, String>>) map.get(Application.DD_APPLICATION)) {
            if (entry == null) {
                continue;
            }
            app = new JsonObject();
            app.addProperty(DeviceDataKeys.App.APP_NAME, entry.get(Application.NAME));
            app.addProperty(DeviceDataKeys.App.PACKAGE_NAME, entry.get(Application.PACKAGE_NAME));
            app.addProperty(DeviceDataKeys.App.INSTALL_TIME, entry.get(Application.FIRST_INSTALL_TIME));
            app.addProperty(DeviceDataKeys.App.UPDATE_TIME, entry.get(Application.LAST_UPDATE_TIME));
            app.addProperty(DeviceDataKeys.App.APP_TYPE, entry.get(Application.TYPE));
            apps.add(app);
        }

        data.addProperty(DeviceDataKeys.APPS, apps.toString());
        return data;
    }

    private PermissionResultCallback mPermissionRequestCallback = new PermissionResultCallback() {

        @Override
        public void permissionGranted(int requestCode) {

        }

        @Override
        public void permissionDenied(int requestCode) {
            //TODO @lavekush need show popup for why we asking these permission
        }

        @Override
        public void neverAskAgain(int requestCode) {
            //TODO @lavekush show never ask again help page and exit from instant loan features
        }
    };
}
