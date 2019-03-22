package com.tokopedia.instantdebitbca

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bca.xco.widget.BCARegistrasiXCOWidget
import com.bca.xco.widget.BCAXCOListener
import com.bca.xco.widget.XCOEnum
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.instantdebitbca.di.InstantDebitBcaInstance
import javax.inject.Inject
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by nabillasabbaha on 21/03/19.
 */
class InstantDebitBca2Fragment : BaseDaggerFragment(), BCAXCOListener, InstantDebitBcaContract.View {

    private lateinit var layoutWidget: RelativeLayout
    private lateinit var widgetBca : BCARegistrasiXCOWidget

    @Inject lateinit var userSession : UserSessionInterface
    @Inject lateinit var presenter : InstantDebitBcaPresenter

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
        GraphqlClient.init(activity!!)
        val instantDebitBcaComponent = InstantDebitBcaInstance.getComponent(activity!!.application)
        instantDebitBcaComponent.inject(this)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_instant_debit_bca, container, false);
        layoutWidget = view.findViewById(R.id.layoutWidget) as RelativeLayout
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        widgetBca = BCARegistrasiXCOWidget(activity, XCOEnum.ENVIRONMENT.DEV)
        widgetBca.setListener(this);
        layoutWidget.addView(widgetBca)
        presenter.getAccessTokenInstantDebitBca()
    }

    override fun onBCARegistered(xcoID: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBCACloseWidget() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBCASuccess(xcoID: String?, credentialType: String?, credentialNo: String?, maxLimit: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBCATokenExpired(tokenStatus: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun openWidgetBca(accessToken: String) {
        widgetBca.openWidget(accessToken, "f333e9e6-4de0-46ec-aa6f-5683afd56bc0", "7a272369-4c29-44a6-ab84-94b120298a35",
                userSession.userId, "61017")
    }

    override fun showErrorMessage(throwable: Throwable) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, ErrorHandler.getErrorMessage(activity, throwable))
    }

    companion object {
        fun newInstance() : Fragment {
            val fragment = InstantDebitBca2Fragment();
            return fragment;
        }
    }
}
