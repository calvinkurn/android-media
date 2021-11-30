package com.tokopedia.wallet.ovoactivation.view

import android.app.Application
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.wallet.ovoactivation.OvoActivationAnalytics
import com.tokopedia.wallet.R
import com.tokopedia.wallet.di.WalletComponentInstance
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 20/09/18.
 */
class ActivationOvoFragment : BaseDaggerFragment() {

    private lateinit var activationNewAccountBtn: UnifyButton
    private lateinit var changeNumberBtn: UnifyButton
    private lateinit var activationDesc: Typography
    private lateinit var registeredApplink: String
    private var phoneNumber: String = ""
    private var changeMsisdnApplink: String = ""

    @Inject
    lateinit var ovoActivationAnalytics: OvoActivationAnalytics

    override fun initInjector() {
        val walletComponent = WalletComponentInstance.getComponent(activity?.application as Application)
        walletComponent.inject(this)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_activation_ovo, container, false)
        activationNewAccountBtn = view.findViewById(R.id.activation_ovo_btn)
        changeNumberBtn = view.findViewById(R.id.change_number_btn)
        activationDesc = view.findViewById(R.id.activation_desc)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            registeredApplink = it.getString(ActivationOvoActivity.REGISTERED_APPLINK, "")
            phoneNumber = it.getString(ActivationOvoActivity.PHONE_NUMBER, "")
            changeMsisdnApplink = it.getString(ActivationOvoActivity.CHANGE_MSISDN_APPLINK, "")
        }

        activationDesc.text = setContentAndBoldPhoneNumber()
        activationNewAccountBtn.setOnClickListener {
            ovoActivationAnalytics.eventClickMakeNewOvoAccount()
            directPageWithApplink(registeredApplink)
        }
        changeNumberBtn.setOnClickListener {
            ovoActivationAnalytics.eventClickChangePhoneNumber()
            directPageWithApplink(changeMsisdnApplink)
        }
    }

    private fun setContentAndBoldPhoneNumber(): SpannableString {
        val activationDesc = String.format(getString(R.string.wallet_activation_ovo_desc), phoneNumber)
        val spannableString = SpannableString(activationDesc)
        val endIndex = 22 + phoneNumber.length
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 22, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    private fun directPageWithApplink(ApplinkSchema: String?) {
        if (RouteManager.isSupportApplink(activity, ApplinkSchema)) {
            val intentRegisteredApplink = RouteManager.getIntent(activity, ApplinkSchema)
            startActivity(intentRegisteredApplink)
            activity?.let {
                it.finish()
            }
        }
    }

    companion object {

        fun newInstance(registeredApplink: String,
                        phoneNumber: String, changeMsisdnApplink: String): ActivationOvoFragment {
            val fragment = ActivationOvoFragment()
            val bundle = Bundle()
            bundle.putString(ActivationOvoActivity.REGISTERED_APPLINK, registeredApplink)
            bundle.putString(ActivationOvoActivity.PHONE_NUMBER, phoneNumber)
            bundle.putString(ActivationOvoActivity.CHANGE_MSISDN_APPLINK, changeMsisdnApplink)
            fragment.arguments = bundle
            return fragment
        }
    }
}
