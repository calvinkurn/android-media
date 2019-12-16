package com.tokopedia.instantloan.view.presenter

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.instantloan.constant.DeviceDataKeys
import com.tokopedia.instantloan.data.model.response.ResponsePhoneData
import com.tokopedia.instantloan.ddcollector.DDCollectorManager
import com.tokopedia.instantloan.ddcollector.OnDeviceDataReady
import com.tokopedia.instantloan.ddcollector.PermissionResultCallback
import com.tokopedia.instantloan.ddcollector.account.Account
import com.tokopedia.instantloan.ddcollector.app.Application
import com.tokopedia.instantloan.ddcollector.bdd.BasicDeviceData
import com.tokopedia.instantloan.domain.interactor.GetLoanProfileStatusUseCase
import com.tokopedia.instantloan.domain.interactor.PostPhoneDataUseCase
import com.tokopedia.instantloan.domain.subscriber.GetDanaInstanLoanProfileSubscriber
import com.tokopedia.instantloan.view.contractor.DanaInstanLoanContractor
import com.tokopedia.network.utils.ErrorHandler
import rx.Subscriber
import java.lang.reflect.Type
import java.util.*
import javax.inject.Inject

class DanaInstanLoanPresenter @Inject
constructor(private val mGetLoanProfileStatusUseCase: GetLoanProfileStatusUseCase,
            private val mPostPhoneDataUseCase: PostPhoneDataUseCase) :
        BaseDaggerPresenter<DanaInstanLoanContractor.View>(), DanaInstanLoanContractor.Presenter {

    private val danaInstanLoanProfileSubscriber = GetDanaInstanLoanProfileSubscriber(this)
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

    override fun attachView(view: DanaInstanLoanContractor.View) {
        super.attachView(view)
    }

    override fun detachView() {
        mGetLoanProfileStatusUseCase.unsubscribe()
        mPostPhoneDataUseCase.unsubscribe()
        super.detachView()
    }

    override fun getLoanProfileStatus() {
        view.showLoader()
        mGetLoanProfileStatusUseCase.execute(danaInstanLoanProfileSubscriber)
    }

    override fun startDataCollection() {
        view.showLoaderIntroDialog()
        DDCollectorManager.getsInstance().init(view.getActivityContext(), mPermissionRequestCallback)

        DDCollectorManager.getsInstance().process(object : OnDeviceDataReady {
            override fun callback(data: Map<String, Any?>?) {
                mPostPhoneDataUseCase.setBody(getPhoneDataPayload(data))
                mPostPhoneDataUseCase.execute(object : Subscriber<Map<Type, RestResponse>>() {
                    override fun onCompleted() {
                        if (isViewAttached) {
                            view.hideLoaderIntroDialog()
                        }
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

        map?.let {
            data.addProperty(DeviceDataKeys.Common.BRAND, (it[BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.BRAND])
            data.addProperty(DeviceDataKeys.Common.DEVICE_ID, (it[BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.DEVICE_ID])
            data.addProperty(DeviceDataKeys.Common.DEVICE_SDK_VERSION, (it[BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.DEVICE_SDK_VERSION])
            data.addProperty(DeviceDataKeys.Common.DEVICE_SYSTEM_VERSION, (it[BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.DEVICE_SYSTEM_VERSION])
            data.addProperty(DeviceDataKeys.Common.GOOGLE_GAID, (it[BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.GOOGLE_GAID])
            data.addProperty(DeviceDataKeys.Common.IMEI, (it[BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.IMEI])
            data.addProperty(DeviceDataKeys.Common.LATITUDE, (it[BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.LATITUDE])
            data.addProperty(DeviceDataKeys.Common.LONGITUDE, (it[BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.LONGITUDE])
            data.addProperty(DeviceDataKeys.Common.MODEL, (it[BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.MODEL])
            data.addProperty(DeviceDataKeys.Common.SYSTEM_LANGUAGE, (it[BasicDeviceData.DD_BASIC_DEVICE_DATA] as ArrayList<Map<String, String>>)[0][BasicDeviceData.SYSTEM_LANGUAGE])

            val messages = JsonArray()
            data.addProperty(DeviceDataKeys.Common.SMS, messages.toString())

            val contacts = JsonArray()
            data.addProperty(DeviceDataKeys.Common.CONTACT, contacts.toString())

            val callLogs = JsonArray()
            data.addProperty(DeviceDataKeys.Common.CALL, callLogs.toString())

            val accounts = JsonArray()
            var account: JsonObject

            if (it[Account.DD_ACCOUNT] != null) {
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

            if (it[Application.DD_APPLICATION] != null) {
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
        }

        return data
    }
}
