package com.tokopedia.recharge_credit_card

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.recharge_credit_card.bottomsheet.CCBankListBottomSheet
import com.tokopedia.recharge_credit_card.di.RechargeCCInstance
import com.tokopedia.recharge_credit_card.util.RechargeCCUtil
import com.tokopedia.recharge_credit_card.viewmodel.RechargeCCViewModel
import com.tokopedia.recharge_credit_card.viewmodel.RechargeSubmitCCViewModel
import com.tokopedia.recharge_credit_card.widget.CCClientNumberWidget
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_recharge_cc.*
import javax.inject.Inject

class RechargeCCFragment : BaseDaggerFragment() {

    private lateinit var rechargeCCViewModel: RechargeCCViewModel
    private lateinit var rechargeSubmitCCViewModel: RechargeSubmitCCViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface


    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        activity?.let {
            val creditCardComponent = RechargeCCInstance.getComponent(it.application)
            creditCardComponent.inject(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            rechargeCCViewModel = viewModelProvider.get(RechargeCCViewModel::class.java)
            rechargeSubmitCCViewModel = viewModelProvider.get(RechargeSubmitCCViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recharge_cc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cc_widget_client_number.setListener(object : CCClientNumberWidget.ActionListener {
            override fun onClickNextButton(clientNumber: String) {
                dialogConfirmation()
            }
        })

        list_bank_btn.setOnClickListener {
            activity?.let {
                val bottomSheetBankList = CCBankListBottomSheet()
                bottomSheetBankList.show(it.supportFragmentManager, "Bank list")
            }
        }
    }

    private fun dialogConfirmation() {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.cc_title_dialog))
            dialog.setDescription(getString(R.string.cc_desc_dialog))
            dialog.setPrimaryCTAText(getString(R.string.cc_cta_btn_primary))
            dialog.setSecondaryCTAText(getString(R.string.cc_cta_btn_secondary))
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                submitCreditCard("26", "856", "2695", cc_widget_client_number.getClientNumber())
            }
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun submitCreditCard(categoryId: String, operatorId: String, productId: String, clientNumber: String) {
        showLoading()
        if (userSession.isLoggedIn) {
            rechargeCCViewModel.getSignatureCreditCard(GraphqlHelper.loadRawString(resources, R.raw.query_cc_signature), 26)

            rechargeCCViewModel.signature.observe(this, Observer {
                //rechargeSubmitCCViewModel.postCreditCard(it)
                rechargeSubmitCCViewModel.postCreditCard(clientNumber, operatorId, productId, "8785147",
                    "1f5492f7c5d8c6badfa82f082fdd479e4dd82909")
            })
            rechargeCCViewModel.errorSignature.observe(this, Observer {
                hideLoading()
                view?.run {
                    Toaster.make(this, it, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR)
                }
            })

            rechargeSubmitCCViewModel.redirectUrl.observe(this, Observer {
                val passData = DigitalCheckoutPassData.Builder()
                        .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                        .categoryId(categoryId)
                        .clientNumber(clientNumber)
                        .instantCheckout("0")
                        .isPromo("0")
                        .operatorId(operatorId)
                        .productId(productId)
                        .idemPotencyKey(RechargeCCUtil.generateIdemPotencyCheckout(userSession.userId))
                        .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                        .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                        .needGetCart(true)
                        .build()

                val intent = RouteManager.getIntent(activity, ApplinkConsInternalDigital.CART_DIGITAL)
                intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, passData)
                startActivityForResult(intent, REQUEST_CODE_CART)
            })

            rechargeSubmitCCViewModel.errorSubmitCreditCard.observe(this, Observer {
                hideLoading()
                view?.run {
                    Toaster.make(this, it, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR)
                }
            })

        } else {
            navigateUserLogin()
        }
    }

    private fun navigateUserLogin() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }

    private fun hideLoading() {
        cc_progress_bar.visibility = View.GONE
    }

    private fun showLoading() {
        cc_progress_bar.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_CART -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    if (data.hasExtra(DigitalExtraParam.EXTRA_MESSAGE)) {
                        val message = data.getStringExtra(DigitalExtraParam.EXTRA_MESSAGE)
                        if (!TextUtils.isEmpty(message)) {
                            view?.run {
                                Toaster.make(this, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR)
                            }
                        }
                    }
                }
            }
            REQUEST_CODE_LOGIN -> {
                if (userSession.isLoggedIn) {
//                    val intent = RouteManager.getIntent(activity, ApplinkConsInternalDigital.CART_DIGITAL)
//                    intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, digitalCheckoutPassDataState)
//                    startActivityForResult(intent, REQUEST_CODE_CART_DIGITAL)
                }
            }
        }
    }

    companion object {

        const val REQUEST_CODE_CART = 1000
        const val REQUEST_CODE_LOGIN = 1001

        fun newInstance(): Fragment {
            return RechargeCCFragment()
        }
    }
}