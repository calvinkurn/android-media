package com.tokopedia.instantdebitbca.data.view.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bca.xco.widget.BCARegistrasiXCOWidget
import com.bca.xco.widget.BCAXCOListener
import com.bca.xco.widget.XCOEnum
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.authentication.AuthKey
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.instantdebitbca.R
import com.tokopedia.instantdebitbca.data.domain.NotifyDebitRegisterBcaUseCase
import com.tokopedia.instantdebitbca.data.view.activity.InstantDebitBcaActivity
import com.tokopedia.instantdebitbca.data.view.interfaces.InstantDebitBcaContract
import com.tokopedia.instantdebitbca.data.view.presenter.InstantDebitBcaPresenter
import com.tokopedia.instantdebitbca.data.view.utils.DeviceUtil
import com.tokopedia.instantdebitbca.data.view.utils.InstantDebitBcaInstance
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 25/03/19.
 */
open class InstantDebitBcaFragment @Inject constructor(): BaseDaggerFragment(), InstantDebitBcaContract.View, BCAXCOListener {

    @Inject
    protected lateinit var presenter: InstantDebitBcaPresenter
    @Inject
    protected lateinit var userSession: UserSessionInterface

    protected lateinit var layoutWidget: RelativeLayout
    private lateinit var widgetBca: BCARegistrasiXCOWidget
    private lateinit var listener: ActionListener
    private var applinkUrl: String? = ""

    private val deviceId: String
        get() {
            val deviceMap = HashMap<String, String>()
            deviceMap[NotifyDebitRegisterBcaUseCase.USER_AGENT] = DeviceUtil.userAgent
            deviceMap[NotifyDebitRegisterBcaUseCase.IP_ADDRESS] = DeviceUtil.localIpAddress
            return convertObjToJsonString(deviceMap)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_instant_debit_bca, container, false)
        layoutWidget = view.findViewById(R.id.layoutWidget)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            applinkUrl = it.getString(InstantDebitBcaActivity.CALLBACK_URL)
        }
        presenter.getAccessTokenBca()
        createAndSetBcaWidget()
    }

    override fun createAndSetBcaWidget() {
        widgetBca = BCARegistrasiXCOWidget(activity, XCOEnum.ENVIRONMENT.PROD)
        widgetBca.setListener(this)
        layoutWidget.addView(widgetBca)
    }

    override fun initInjector() {
        activity?.let {
            val instantDebitBcaComponent = InstantDebitBcaInstance.getComponent(it.application)
            instantDebitBcaComponent.inject(this)
            presenter.attachView(this)
        }
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun openWidgetBca(accessToken: String) {
        if(::widgetBca.isInitialized) {
            widgetBca.openWidget(accessToken, AuthKey.API_KEY_INSTANT_DEBIT_BCA, AuthKey.API_SEED_INSTANT_DEBIT_BCA,
                    userSession.userId, AuthKey.INSTANT_DEBIT_BCA_MERCHANT_ID)
        }
    }

    override fun showErrorMessage(throwable: Throwable) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, ErrorHandler.getErrorMessage(activity, throwable))
    }

    protected fun convertObjToJsonString(obj: Any): String {
        val gsonObj = Gson()
        val data = gsonObj.toJson(obj)
        data.replace("\"", "\\\"")
        return data
    }

    override fun onBCASuccess(xcoID: String?, credentialType: String?, credentialNo: String?, maxLimit: String?) {
        val mapCardData = HashMap<String, String>()
        mapCardData[NotifyDebitRegisterBcaUseCase.XCOID] = xcoID ?: ""
        mapCardData[NotifyDebitRegisterBcaUseCase.CREDENTIAL_TYPE] = credentialType ?: ""
        mapCardData[NotifyDebitRegisterBcaUseCase.CREDENTIAL_NO] = credentialNo ?: ""
        mapCardData[NotifyDebitRegisterBcaUseCase.MAX_LIMIT] = maxLimit ?: ""
        val debitData = convertObjToJsonString(mapCardData)

        presenter.notifyDebitRegisterBca(debitData, deviceId)
    }

    protected fun isPresenterInitialized(): Boolean{
        return ::presenter.isInitialized
    }

    override fun onBCATokenExpired(tokenStatus: String) {

    }

    override fun onBCARegistered(xcoID: String) {

    }

    override fun onBCACloseWidget() {

    }

    override fun redirectPageAfterRegisterBca() {
        if(::listener.isInitialized) {
            listener.redirectPage(applinkUrl)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onAttachActivity(context: Context) {
        listener = context as ActionListener
    }

    interface ActionListener {
        fun redirectPage(applinkUrl: String?)
    }

    companion object {

        fun newInstance(callbackUrl: String): Fragment {
            val fragment = InstantDebitBcaFragment()
            val bundle = Bundle()
            bundle.putString(InstantDebitBcaActivity.CALLBACK_URL, callbackUrl)
            fragment.arguments = bundle
            return fragment
        }
    }
}
