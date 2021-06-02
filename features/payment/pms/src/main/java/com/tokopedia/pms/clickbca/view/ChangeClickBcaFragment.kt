package com.tokopedia.pms.clickbca.view

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.pms.R
import com.tokopedia.pms.clickbca.view.ChangeClickBcaActivity.Companion.MERCHANT_CODE
import com.tokopedia.pms.clickbca.view.ChangeClickBcaActivity.Companion.TRANSACTION_ID
import com.tokopedia.pms.clickbca.view.ChangeClickBcaActivity.Companion.USER_ID_KLIK_BCA
import com.tokopedia.pms.paymentlist.di.PmsComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_change_click_bca.*
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 6/25/18.
 */
class ChangeClickBcaFragment : BaseDaggerFragment() {
    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val viewModel: ChangeClickBcaViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(ChangeClickBcaViewModel::class.java)
    }

    private var loaderDialog: LoaderDialog? = null
    private var transactionId: String = ""
    private var merchantCode: String = ""
    private var userIdKlikBca: String = ""
    override fun getScreenName() = ""

    override fun initInjector() = getComponent(PmsComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            transactionId = it.getString(TRANSACTION_ID, "")
            merchantCode = it.getString(MERCHANT_CODE, "")
            userIdKlikBca = it.getString(USER_ID_KLIK_BCA, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_change_click_bca, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        input_layout_click_bca_user_id.apply {
            textFieldInput.setText(userIdKlikBca)
            setMessage(
                getString(
                    R.string.payment_label_helper_change_click_bca,
                    userIdKlikBca
                )
            )
        }
        button_use.setOnClickListener {
            val userId = input_layout_click_bca_user_id.textFieldInput.text.toString()
            if (TextUtils.isEmpty(userId)) {
                NetworkErrorHelper.showRedCloseSnackbar(
                    activity,
                    getString(R.string.payment_label_error_empty_user_id)
                )
                return@setOnClickListener
            }
            showLoadingDialog()
            viewModel.changeClickBcaUserId(
                transactionId,
                merchantCode,
                userId
            )
        }
        viewModel.editResultLiveData.observe(viewLifecycleOwner, Observer {
            hideLoadingDialog()
            when (it) {
                is Success -> onResultChangeClickBcaUserId(it.data.isSuccess, it.data.message)
                is Fail -> onErrorChangeClickBcaUserID(it.throwable)
            }
        })
    }

    private fun onErrorChangeClickBcaUserID(e: Throwable) {
        Toaster.make(
            button_use,
            ErrorHandler.getErrorMessage(context, e),
            Toaster.LENGTH_LONG,
            Toaster.TYPE_ERROR
        )
    }

    private fun onResultChangeClickBcaUserId(isSuccess: Boolean, message: String) {
        activity?.let {
            if (isSuccess) {
                Toaster.make(button_use, message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL)
                it.setResult(Activity.RESULT_OK)
                it.finish()
            } else {
                Toaster.make(button_use, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
            }
        }
    }

    private fun showLoadingDialog() {
        loaderDialog = context?.let { LoaderDialog(it) }
        loaderDialog?.dialog?.setOverlayClose(false)
        loaderDialog?.show()
    }

    private fun hideLoadingDialog() {
        loaderDialog?.dialog?.dismiss()
        loaderDialog = null
    }

    companion object {

        fun createInstance(
            transactionId: String?,
            merchantCode: String?,
            userIdKlikBca: String?
        ): Fragment {
            val changeClickBcaFragment = ChangeClickBcaFragment()
            val bundle = Bundle()
            bundle.putString(TRANSACTION_ID, transactionId)
            bundle.putString(MERCHANT_CODE, merchantCode)
            bundle.putString(USER_ID_KLIK_BCA, userIdKlikBca)
            changeClickBcaFragment.arguments = bundle
            return changeClickBcaFragment
        }
    }
}