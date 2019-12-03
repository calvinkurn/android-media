package com.tokopedia.salam.umrah.checkout.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.data.Schemes
import com.tokopedia.salam.umrah.checkout.di.UmrahCheckoutComponent
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutInstallmentActivity
import com.tokopedia.salam.umrah.checkout.presentation.adapter.UmrahCheckoutInstallmentAdapter
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.util.CommonParam
import kotlinx.android.synthetic.main.fragment_umrah_checkout_installment.*
import javax.inject.Inject

class UmrahCheckoutInstallmentFragment : BaseDaggerFragment(), UmrahCheckoutInstallmentAdapter.UmrahInstallmentListener {

    lateinit var schemes : ArrayList<Schemes>

    @Inject
    lateinit var trackingUmrahUtil: UmrahTrackingAnalytics

    var defaultOptionSchemes = 0

    private val umrahCheckoutInstallmentAdapter by lazy { UmrahCheckoutInstallmentAdapter(context!!, this) }

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(UmrahCheckoutComponent::class.java).inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_checkout_installment, container, false)

    companion object{
        fun getInstance(dataSchemes: ArrayList<Schemes>, defaultOptionSchemes: Int): UmrahCheckoutInstallmentFragment = UmrahCheckoutInstallmentFragment().also {
            it.arguments = Bundle().apply {
                putParcelableArrayList(UmrahCheckoutInstallmentActivity.EXTRA_SCHEMES, dataSchemes)
                putInt(UmrahCheckoutInstallmentActivity.EXTRA_DEFAULT_OPTION_SCHEMES, defaultOptionSchemes)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            schemes = it.getParcelableArrayList(UmrahCheckoutInstallmentActivity.EXTRA_SCHEMES) ?: arrayListOf()
            defaultOptionSchemes = it.getInt(UmrahCheckoutInstallmentActivity.EXTRA_DEFAULT_OPTION_SCHEMES)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView(){
        tv_umrah_checkout_installment.setTextDescription(getText(R.string.umrah_checkout_payment_option_ticker_desc))
        if (schemes.isNotEmpty()){
            schemes.apply {
                umrahCheckoutInstallmentAdapter.lastCheckedPosition = defaultOptionSchemes
                rv_umrah_checkout_installment.apply {
                    adapter = umrahCheckoutInstallmentAdapter
                    layoutManager = LinearLayoutManager(
                            context,
                            LinearLayoutManager.VERTICAL, false
                    )
                }
                umrahCheckoutInstallmentAdapter.setList(this)

            }
        }

        btn_umrah_checkout_installment.setOnClickListener {
            activity?.run {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(CommonParam.ARG_CHECKOUT_PAYMENT_OPTION, umrahCheckoutInstallmentAdapter.lastCheckedPosition)
                })
                finish()
            }
        }


    }

    override fun onInstallment(position: Int) {
        trackingUmrahUtil.getInstallmentTypeCheckoutTracker(schemes.get(position).title)
    }
}