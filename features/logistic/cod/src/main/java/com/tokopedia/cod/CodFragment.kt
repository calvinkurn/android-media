package com.tokopedia.cod

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.transactiondata.entity.response.cod.Data
import kotlinx.android.synthetic.main.fragment_cod_confirmation.*

/**
 * Created by fajarnuha on 17/12/18.
 */
class CodFragment: BaseDaggerFragment(), CodContract.View {

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
        initView()
    }

    override fun initInjector() {

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

    override fun showError(message: String) {
        NetworkErrorHelper.showCloseSnackbar(activity, message)
    }

    override fun initView() {
        arguments?.getParcelable<Data>(ARGUMENT_COD_DATA)?.let {
            textview_ticker_message.setText(it.message.messageInfo)
            textview_counter_info.setText(it.counterInfo)
            recycler_view_summary.run {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = CodSummaryAdapter(it.priceSummary)
            }
            button_confirm.setOnClickListener(this::onPayClicked)
        }
    }

    override fun onPayClicked(view: View) {
        Toast.makeText(context, "confirm", Toast.LENGTH_SHORT).show()
    }

    override fun navigateToThankYouPage() {
        Toast.makeText(context, "navigate thankyou page", Toast.LENGTH_SHORT).show()
    }
}
