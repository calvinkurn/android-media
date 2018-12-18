package com.tokopedia.cod

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import kotlinx.android.synthetic.main.fragment_cod_confirmation.*

/**
 * Created by fajarnuha on 17/12/18.
 */
class CodFragment: BaseDaggerFragment(), CodContract.View {

    companion object {
        fun newInstance(): Fragment {
            val bundle: Bundle = Bundle()
            val fragment: CodFragment = CodFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cod_confirmation, container, false)
    }

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
