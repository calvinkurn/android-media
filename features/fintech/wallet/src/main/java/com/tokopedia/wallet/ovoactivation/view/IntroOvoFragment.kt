package com.tokopedia.wallet.ovoactivation.view

import android.content.Context
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.Dialog
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.wallet.R
import com.tokopedia.wallet.ovoactivation.OvoActivationAnalytics
import com.tokopedia.wallet.di.WalletComponentInstance
import com.tokopedia.wallet.ovoactivation.view.model.PhoneActionModel

import javax.inject.Inject

/**
 * Created by nabillasabbaha on 20/09/18.
 */
class IntroOvoFragment : BaseDaggerFragment(), IntroOvoContract.View {

    private lateinit var activationOvoBtn: UnifyButton
    private lateinit var learnMoreOvoBtn: UnifyButton
    private lateinit var tncOvo: Typography
    private lateinit var titleOvo: Typography
    private lateinit var descFirstOvo: Typography
    private lateinit var descSecondOvo: Typography
    private lateinit var imgIntroOvo: ImageView
    private lateinit var progressBar: View
    private lateinit var listener: OvoFragmentListener
    private var tokocashActive: Boolean = false

    @Inject
    lateinit var presenter: IntroOvoPresenter
    @Inject
    lateinit var ovoActivationAnalytics: OvoActivationAnalytics

    override fun initInjector() {
        activity?.let {
            GraphqlClient.init(it)
            val tokoCashComponent = WalletComponentInstance.getComponent(it.application)
            tokoCashComponent.inject(this)
            presenter.attachView(this)
        }
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_intro_ovo, container, false)
        activationOvoBtn = view.findViewById(R.id.activation_ovo_btn)
        learnMoreOvoBtn = view.findViewById(R.id.learn_more_ovo_btn)
        tncOvo = view.findViewById(R.id.tnc_ovo)
        titleOvo = view.findViewById(R.id.title_intro)
        descFirstOvo = view.findViewById(R.id.description_1)
        descSecondOvo = view.findViewById(R.id.description_2)
        imgIntroOvo = view.findViewById(R.id.image_ovo)
        progressBar = view.findViewById(R.id.progress_bar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getBalanceWallet()

        arguments?.let {
            tokocashActive = it.getBoolean(IntroOvoActivity.TOKOCASH_ACTIVE)
        }

        if (tokocashActive) {
            listener.setTitleHeader(getString(R.string.wallet_title_header_ovo))
            titleOvo.text = getString(R.string.wallet_announcement_ovo_title)
            descFirstOvo.text = getString(R.string.wallet_announcement_ovo_description)
            descSecondOvo.text = getString(R.string.wallet_announcement_ovo_second_desc)
            ImageHandler.loadImageWithId(imgIntroOvo, R.drawable.wallet_ic_intro_ovo)
        } else {
            listener.setTitleHeader(getString(R.string.wallet_title_header_activation_ovo))
            titleOvo.text = getString(R.string.wallet_announcement_activation_ovo_title)
            descFirstOvo.text = getString(R.string.wallet_announcement_activation_ovo_description)
            descSecondOvo.text = getString(R.string.wallet_announcement_activation_ovo_second_desc)
            ImageHandler.loadImageWithId(imgIntroOvo, R.drawable.wallet_ic_intro_activation)
        }

        activationOvoBtn.setOnClickListener {
            presenter.checkPhoneNumber()
            ovoActivationAnalytics.eventClickActivationOvoNow()
        }
    }

    private fun setTncOvo(tncApplink: String) {
        activity?.let {
            val ss = SpannableString(it.getString(R.string.wallet_announcement_ovo_text_tnc))
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    ovoActivationAnalytics.eventClickTnc()
                    directHelpPageWithApplink(tncApplink)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = ContextCompat.getColor(it, com.tokopedia.unifycomponents.R.color.Unify_G500)
                }
            }
            ss.setSpan(ForegroundColorSpan(ContextCompat.getColor(it,
                com.tokopedia.unifycomponents.R.color.Unify_G500)), 31, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ss.setSpan(clickableSpan, 31, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            tncOvo.movementMethod = LinkMovementMethod.getInstance()
            tncOvo.text = ss
        }
    }

    override fun setApplinkButton(helpAllink: String, tncApplink: String) {
        setTncOvo(tncApplink)
        learnMoreOvoBtn.setOnClickListener {
            ovoActivationAnalytics.eventClickOvoLearnMore()
            directHelpPageWithApplink(helpAllink)
        }
    }

    fun directHelpPageWithApplink(ApplinkSchema: String) {
        if (RouteManager.isSupportApplink(activity, ApplinkSchema)) {
            val intentRegisteredApplink = RouteManager.getIntent(activity, ApplinkSchema)
            startActivity(intentRegisteredApplink)
        }
    }

    override fun directPageWithApplink(ApplinkSchema: String) {
        if (RouteManager.isSupportApplink(activity, ApplinkSchema)) {
            val intentRegisteredApplink = RouteManager.getIntent(activity, ApplinkSchema)
            startActivity(intentRegisteredApplink)
            activity?.let {
                it.finish()
            }
        }
    }

    override fun directPageWithExtraApplink(unRegisteredApplink: String, registeredApplink: String,
                                            phoneNumber: String, changeMsisdnApplink: String) {
        if (RouteManager.isSupportApplink(activity, unRegisteredApplink)) {
            val intentRegisteredApplink = RouteManager.getIntent(activity, unRegisteredApplink)
            intentRegisteredApplink.putExtra(ActivationOvoActivity.REGISTERED_APPLINK, registeredApplink)
            intentRegisteredApplink.putExtra(ActivationOvoActivity.PHONE_NUMBER, phoneNumber)
            intentRegisteredApplink.putExtra(ActivationOvoActivity.CHANGE_MSISDN_APPLINK, changeMsisdnApplink)
            startActivity(intentRegisteredApplink)
            activity?.let {
                it.finish()
            }
        }
    }

    override fun showDialogErrorPhoneNumber(phoneActionModel: PhoneActionModel) {
        val dialog = Dialog(activity, Dialog.Type.LONG_PROMINANCE)
        dialog.setTitle(phoneActionModel.titlePhoneAction)
        dialog.setDesc(phoneActionModel.descPhoneAction)
        dialog.setBtnOk(phoneActionModel.labelBtnPhoneAction)
        dialog.setOnOkClickListener {
            ovoActivationAnalytics.eventClickPopupPhoneNumber(phoneActionModel.labelBtnPhoneAction)
            directPageWithApplink(phoneActionModel.applinkPhoneAction)
            dialog.dismiss()
        }
        dialog.setBtnCancel(getString(R.string.wallet_button_label_cancel))
        dialog.setOnCancelClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun showSnackbarErrorMessage(message: String) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, message)
    }

    override fun getErrorMessage(e: Throwable): String {
        return ErrorHandler.getErrorMessage(activity, e)
    }

    override fun showProgressBar() {
        if (progressBar != null) {
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun hideProgressBar() {
        if (progressBar != null) {
            progressBar.visibility = View.GONE
        }
    }

    override fun onAttachActivity(context: Context) {
        super.onAttachActivity(context)
        listener = context as OvoFragmentListener
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroyView()
    }

    internal interface OvoFragmentListener {
        fun setTitleHeader(titleHeader: String)
    }

    companion object {

        fun newInstance(tokocashActive: Boolean): IntroOvoFragment {
            val fragment = IntroOvoFragment()
            val bundle = Bundle()
            bundle.putBoolean(IntroOvoActivity.TOKOCASH_ACTIVE, tokocashActive)
            fragment.arguments = bundle
            return fragment
        }
    }
}