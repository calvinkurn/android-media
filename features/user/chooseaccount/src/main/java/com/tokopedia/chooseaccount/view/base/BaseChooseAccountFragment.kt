package com.tokopedia.chooseaccount.view.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.chooseaccount.R
import com.tokopedia.chooseaccount.databinding.FragmentChooseLoginPhoneAccountBinding
import com.tokopedia.chooseaccount.view.adapter.AccountAdapter
import com.tokopedia.chooseaccount.view.listener.ChooseAccountListener
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.interceptor.akamai.AkamaiErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.view.binding.viewBinding

abstract class BaseChooseAccountFragment: BaseDaggerFragment(), ChooseAccountListener {

    private val binding: FragmentChooseLoginPhoneAccountBinding? by viewBinding()

    protected var crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    protected var adapter: AccountAdapter? = null
    private var listAccount: RecyclerView? = null
    private var toolbarShopCreation: Toolbar? = null

    abstract fun initObserver()

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_choose_login_phone_account, parent, false)
        setHasOptionsMenu(true)
        toolbarShopCreation = view.findViewById(R.id.toolbar_shop_creation)
        listAccount = view.findViewById(R.id.chooseAccountList)
        prepareView()
        return view
    }

    @SuppressLint("WrongConstant")
    private fun prepareView() {
        adapter = AccountAdapter.createInstance(this, ArrayList(), "")
        listAccount?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        listAccount?.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initObserver()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).let {
            it.setSupportActionBar(toolbarShopCreation)
            it.supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(true)
                setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(it, R.color.transparent)))
            }
        }
    }

    protected fun showLoadingProgress() {
        binding?.mainView?.hide()
        binding?.chooseAccountLoader?.show()
    }

    protected fun dismissLoadingProgress() {
        binding?.mainView?.show()
        binding?.chooseAccountLoader?.hide()
    }

    protected fun checkExceptionType(throwable: Throwable) {
        if (throwable is AkamaiErrorException) {
            showPopupErrorAkamai()
        } else {
            onErrorLogin(ErrorHandler.getErrorMessage(context, throwable, builder = getErrorHandlerBuilder()))
        }
    }

    protected fun logUnknownError(throwable: Throwable?, flow: String) {
        ServerLogger.log(Priority.P2, "BUYER_FLOW_LOGIN",
            mapOf(
                "type" to flow,
                "error" to throwable?.message.toEmptyStringIfNull(),
                "throwable" to Log.getStackTraceString(throwable).take(1000)
            )
        )
    }

    protected fun showPopupError(header: String, body: String, url: String) {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(header)
            dialog.setDescription(body)
            dialog.setPrimaryCTAText(getString(R.string.check_full_information))
            dialog.setSecondaryCTAText(getString(R.string.close_popup))
            dialog.setPrimaryCTAClickListener {
                RouteManager.route(activity, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
            }
            dialog.setSecondaryCTAClickListener {
                dialog.hide()
            }
            dialog.show()
        }
    }

    protected fun showPopupErrorAkamai() {
        showPopupError(
            getString(R.string.popup_error_title),
            getString(R.string.popup_error_desc),
            TokopediaUrl.getInstance().MOBILEWEB + TOKOPEDIA_CARE_PATH
        )
    }

    private fun onErrorLogin(errorMessage: String) {
        dismissLoadingProgress()
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    //Impossible Flow
    protected fun onGoToActivationPage(messageErrorException: MessageErrorException) {
        onErrorLogin(ErrorHandler.getErrorMessage(
            context,
            messageErrorException,
            getErrorHandlerBuilder()
        ))
    }

    protected fun getErrorHandlerBuilder(): ErrorHandler.Builder {
        return ErrorHandler.Builder().apply {
                className = this.javaClass.name
        }.build()
    }

    protected fun onGoToSecurityQuestion() {
        activity?.let {
            it.setResult(Activity.RESULT_OK, Intent().putExtra(ApplinkConstInternalGlobal.PARAM_IS_SQ_CHECK, true))
            it.finish()
        }
    }

    companion object {
        const val REQUEST_CODE_PIN_CHALLENGE = 112

        const val OTP_TYPE_AFTER_LOGIN_PHONE = 148
        const val OTP_MODE_PIN = "PIN"

        const val TOKOPEDIA_CARE_PATH = "help"
    }
}