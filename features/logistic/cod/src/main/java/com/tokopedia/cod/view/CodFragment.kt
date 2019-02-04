package com.tokopedia.cod.view

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.cod.R
import com.tokopedia.cod.di.DaggerCodComponent
import com.tokopedia.logisticanalytics.CodAnalytics
import com.tokopedia.transactiondata.entity.response.cod.Data
import kotlinx.android.synthetic.main.fragment_cod_confirmation.*
import javax.inject.Inject

/**
 * Created by fajarnuha on 17/12/18.
 */
class CodFragment : BaseDaggerFragment(), CodContract.View {

    @Inject lateinit var presenter: CodContract.Presenter
    @Inject lateinit var mTracker: CodAnalytics

    companion object {

        const val ARGUMENT_COD_DATA = "ARGUMENT_COD_DATA"

        fun newInstance(data: Data): Fragment = CodFragment().apply {
            arguments = Bundle().apply { putParcelable(ARGUMENT_COD_DATA, data) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cod_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.attachView(this)
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun initInjector() {
        activity?.let {
            val baseApp = it.application
            if (baseApp is BaseMainApplication) {
                DaggerCodComponent.builder()
                        .baseAppComponent(baseApp.baseAppComponent)
                        .build().inject(this)
            }
        }
    }

    override fun getScreenName(): String {
        return getString(R.string.cod_brand_tag)
    }

    override fun showLoading() {
        progressbar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressbar.visibility = View.GONE
    }

    override fun showError(message: String?) {
        NetworkErrorHelper.showCloseSnackbar(activity, message)
    }

    override fun initView() {
        arguments?.getParcelable<Data>(ARGUMENT_COD_DATA)?.let {
            textview_ticker_message.setText(it.message.messageInfo)
            textview_counter_info.text = MethodChecker.fromHtml(it.counterInfo)
            recycler_view_summary.run {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = CodSummaryAdapter(it.priceSummary)
            }
            button_confirm.setOnClickListener(this::onPayClicked)
        }
    }

    override fun onPayClicked(view: View) {
        mTracker.eventClickBayarDiTempatCod()
        presenter.confirmPayment()
    }

    override fun navigateToThankYouPage(applink: String) {
        val intent = RouteManager.getIntent(context, applink)
        startActivity(intent)
        activity?.finish()
    }

}
