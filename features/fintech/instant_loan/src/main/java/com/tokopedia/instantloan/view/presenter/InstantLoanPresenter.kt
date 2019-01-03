package com.tokopedia.instantloan.view.presenter

import android.provider.ContactsContract
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.instantloan.constant.DeviceDataKeys
import com.tokopedia.instantloan.data.model.response.ResponsePhoneData
import com.tokopedia.instantloan.data.model.response.ResponseUserProfileStatus
import com.tokopedia.instantloan.ddcollector.DDCollectorManager
import com.tokopedia.instantloan.ddcollector.OnDeviceDataReady
import com.tokopedia.instantloan.ddcollector.PermissionResultCallback
import com.tokopedia.instantloan.ddcollector.account.Account
import com.tokopedia.instantloan.ddcollector.app.Application
import com.tokopedia.instantloan.ddcollector.bdd.BasicDeviceData
import com.tokopedia.instantloan.ddcollector.contact.Contact
import com.tokopedia.instantloan.domain.interactor.GetLoanProfileStatusUseCase
import com.tokopedia.instantloan.domain.interactor.PostPhoneDataUseCase
import com.tokopedia.instantloan.view.contractor.InstantLoanContractor
import com.tokopedia.user.session.UserSession
import rx.Subscriber
import java.lang.reflect.Type
import java.util.*
import javax.inject.Inject

class InstantLoanPresenter @Inject
constructor(private val mGetLoanProfileStatusUseCase: GetLoanProfileStatusUseCase,
            private val mPostPhoneDataUseCase: PostPhoneDataUseCase) :
        BaseDaggerPresenter<InstantLoanContractor.View>(), InstantLoanContractor.Presenter {

    @Inject
    lateinit var userSession: UserSession

    private val mPermissionRequestCallback = object : PermissionResultCallback {

        override fun permissionGranted(requestCode: Int) {

        }

        override fun permissionDenied(requestCode: Int) {
            view.hideIntroDialog()
            view.hideLoaderIntroDialog()
        }

        override fun neverAskAgain(requestCode: Int) {
            view.hideIntroDialog()
            view.hideLoaderIntroDialog()
        }
    }

    override fun attachView(view: InstantLoanContractor.View) {
        super.attachView(view)
    }

    override fun initialize() {

    }

    override fun getLoanProfileStatus() {
        view.showLoader()
        mGetLoanProfileStatusUseCase.execute(object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {
                view.hideLoader()
            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.onErrorLoanProfileStatus(ErrorHandler.getErrorMessage(view.getActivityContext(), e))
                }
            }

            override fun onNext(typeRestResponseMap: Map<Type, RestResponse>) {
                val restResponse = typeRestResponseMap[ResponseUserProfileStatus::class.java]
                val responseUserProfileStatus = restResponse!!.getData<ResponseUserProfileStatus>()
                view.onSuccessLoanProfileStatus(responseUserProfileStatus?.userProfileLoanEntity!!)
                view.setUserOnGoingLoanStatus(
                        responseUserProfileStatus.userProfileLoanEntity!!.onGoingLoanId != 0,
                        responseUserProfileStatus.userProfileLoanEntity!!.onGoingLoanId)

            }
        })
    }

    override fun postPhoneData(userId: String) {

    }

    override fun isUserLoggedIn(): Boolean {
        return userSession != null && userSession.isLoggedIn
    }

    override fun startDataCollection() {
        view.showLoaderIntroDialog()
        DDCollectorManager.getsInstance().init(view.getActivityContext(), mPermissionRequestCallback)

        DDCollectorManager.getsInstance().process(object : OnDeviceDataReady {
            override fun callback(data: Map<String, Any?>?) {
                mPostPhoneDataUseCase.setBody(getPhoneDataPayload(data))
                mPostPhoneDataUseCase.execute(object : Subscriber<Map<Type, RestResponse>>() {
                    override fun onCompleted() {
                        view.hideLoaderIntroDialog()
                    }

                    override fun onError(e: Throwable) {
                        if (isViewAttached) {
                            view.onErrorPhoneDataUploaded(ErrorHandler.getErrorMessage(view.getAppContext(), e))
                        }
                    }

                    override fun onNext(typeRestResponseMap: Map<Type, RestResponse>) {
                        val restResponse = typeRestResponseMap[ResponsePhoneData::class.java]
                        val responsePhoneData = restResponse!!.getData<ResponsePhoneData>()
                        view.onSuccessPhoneDataUploaded(responsePhoneData.data!!)
                    }
                })
            }
        })
    }

    private fun getPhoneDataPayload(map: Map<String, Any?>?): JsonObject {

        val data = JsonObject()

        data.addProperty(DeviceDataKeys.Common.BRAND, (map!![BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.BRAND])
        data.addProperty(DeviceDataKeys.Common.DEVICE_ID, (map!![BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.DEVICE_ID])
        data.addProperty(DeviceDataKeys.Common.DEVICE_SDK_VERSION, (map!![BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.DEVICE_SDK_VERSION])
        data.addProperty(DeviceDataKeys.Common.DEVICE_SYSTEM_VERSION, (map!![BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.DEVICE_SYSTEM_VERSION])
        data.addProperty(DeviceDataKeys.Common.GOOGLE_GAID, (map!![BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.GOOGLE_GAID])
        data.addProperty(DeviceDataKeys.Common.IMEI, (map!![BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.IMEI])
        data.addProperty(DeviceDataKeys.Common.LATITUDE, (map!![BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.LATITUDE])
        data.addProperty(DeviceDataKeys.Common.LONGITUDE, (map!![BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.LONGITUDE])
        data.addProperty(DeviceDataKeys.Common.MODEL, (map!![BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.MODEL])
        data.addProperty(DeviceDataKeys.Common.SYSTEM_LANGUAGE, (map!![BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.SYSTEM_LANGUAGE])

        val messages = JsonArray()
        data.addProperty(DeviceDataKeys.Common.SMS, messages.toString())

        val contacts = JsonArray()
        var contact: JsonObject

        if (map[Contact.DD_CONTACT] != null) {
            for (entry in map[Contact.DD_CONTACT] as List<HashMap<String, String>>) {
                if (entry == null) {
                    continue
                }
                contact = JsonObject()
                contact.addProperty(DeviceDataKeys.Contact.NAME, entry[ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME])
                contact.addProperty(DeviceDataKeys.Contact.PHONE, entry[ContactsContract.CommonDataKinds.Phone.NUMBER])
                contact.addProperty(DeviceDataKeys.Contact.TIME, entry[ContactsContract.CommonDataKinds.Phone.CONTACT_STATUS_TIMESTAMP])
                contact.addProperty(DeviceDataKeys.Contact.TIMES, entry[ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED])
                contact.addProperty(DeviceDataKeys.Contact.LAST_TIME, entry[ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED])
                contacts.add(contact)
            }
        }
        data.addProperty(DeviceDataKeys.Common.CONTACT, contacts.toString())

        val callLogs = JsonArray()
        data.addProperty(DeviceDataKeys.Common.CALL, callLogs.toString())

        val accounts = JsonArray()
        var account: JsonObject

        if (map[Account.DD_ACCOUNT] != null) {
            for (entry in map[Account.DD_ACCOUNT] as List<HashMap<String, String>>) {
                if (entry == null) {
                    continue
                }
                account = JsonObject()
                account.addProperty(DeviceDataKeys.Account.NAME, entry[Account.NAME])
                account.addProperty(DeviceDataKeys.Account.TYPE, entry[Account.TYPE])
                accounts.add(account)
            }
        }

        data.addProperty(DeviceDataKeys.Common.ACCOUNTS, accounts.toString())

        val apps = JsonArray()
        var app: JsonObject

        if (map[Application.DD_APPLICATION] != null) {
            for (entry in map[Application.DD_APPLICATION] as List<HashMap<String, String>>) {
                if (entry == null) {
                    continue
                }
                app = JsonObject()
                app.addProperty(DeviceDataKeys.App.APP_NAME, entry[Application.NAME])
                app.addProperty(DeviceDataKeys.App.PACKAGE_NAME, entry[Application.PACKAGE_NAME])
                app.addProperty(DeviceDataKeys.App.INSTALL_TIME, entry[Application.FIRST_INSTALL_TIME])
                app.addProperty(DeviceDataKeys.App.UPDATE_TIME, entry[Application.LAST_UPDATE_TIME])
                app.addProperty(DeviceDataKeys.App.APP_TYPE, entry[Application.TYPE])
                apps.add(app)
            }
        }

        data.addProperty(DeviceDataKeys.Common.APPS, apps.toString())
        return data
    }
}
