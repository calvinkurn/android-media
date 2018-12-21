package com.tokopedia.cod

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.transactiondata.entity.response.cod.Data
import kotlinx.android.synthetic.main.fragment_cod_confirmation.*

/**
 * Created by fajarnuha on 17/12/18.
 */
class CodFragment: BaseDaggerFragment(), CodContract.View {

    lateinit var mData: Data

    companion object {

        const val ARGUMENT_COD_DATA = "ARGUMENT_COD_DATA"

        fun newInstance(data: Data): Fragment {
            val bundle = Bundle()
            bundle.putParcelable(ARGUMENT_COD_DATA, data)
            val fragment = CodFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cod_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mData = arguments!!.getParcelable(ARGUMENT_COD_DATA)
    }

    override fun initInjector() {

    }

    override fun getScreenName(): String {
        return "bla"
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

    override fun loadInformation() {

    }
}
