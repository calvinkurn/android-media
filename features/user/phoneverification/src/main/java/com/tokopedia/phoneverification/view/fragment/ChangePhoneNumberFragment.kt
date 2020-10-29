package com.tokopedia.phoneverification.view.fragment

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.phoneverification.PhoneVerificationConst
import com.tokopedia.phoneverification.R
import com.tokopedia.phoneverification.di.revamp.PhoneVerificationComponent
import com.tokopedia.phoneverification.view.viewmodel.PhoneVerificationViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.phonenumber.PhoneNumberUtil.transform
import javax.inject.Inject

/**
 * Created by nisie on 2/24/17.
 */
class ChangePhoneNumberFragment : BaseDaggerFragment() {
    var phoneNumberEditText: EditText? = null
    var changePhoneNumberButton: TextView? = null
    var progressDialog: View? = null
    var mainView: View? = null
//    @Inject
//    var presenter: ChangePhoneNumberPresenter? = null

    override fun getScreenName(): String {
        return PhoneVerificationConst.SCREEN_CHANGE_PHONE_NUMBER
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val phoneVerificationViewModel: PhoneVerificationViewModel
            by lazy { ViewModelProvider(this, viewModelFactory).get(PhoneVerificationViewModel::class.java) }


    override fun initInjector() {
//        if (activity != null && activity!!.application != null) {
//            val baseAppComponent = (activity!!.application as BaseMainApplication).baseAppComponent
//            val phoneVerificationComponent = DaggerPhoneVerificationComponent.builder().baseAppComponent(baseAppComponent).build()
//            phoneVerificationComponent.inject(this)
//        }
        getComponent(PhoneVerificationComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_change_phone_number, container, false)
        mainView = view.findViewById(R.id.main_view)
        phoneNumberEditText = view.findViewById<View>(R.id.phone_number) as EditText
        changePhoneNumberButton = view.findViewById<View>(R.id.change_phone_number_button) as TextView
        progressDialog = view.findViewById(R.id.loading_view)
        phoneNumberEditText?.addTextChangedListener(watcher(phoneNumberEditText))
        setViewListener()
        setupObserver()
        return view
    }

    private fun setupObserver() {
        phoneVerificationViewModel.responseLiveData.observe(
                viewLifecycleOwner, Observer {
            when(it){
                is Success -> {
                    onSuccessChangePhoneNumber()
                }

                is Fail -> {
                    onErrorChangePhoneNumber(ErrorHandler.getErrorMessage(activity, it.throwable))
                }
            }
        }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val extras = activity?.intent?.extras
        if (extras != null) {
            phoneNumberEditText?.setText(extras.getString(EXTRA_PHONE_NUMBER, ""))
        }
    }

    private fun watcher(editText: EditText?): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val phone = transform(s.toString())
                if (s.toString().length != phone.length) {
                    editText?.removeTextChangedListener(this)
                    editText?.setText(phone)
                    editText?.setSelection(phone.length)
                    editText?.addTextChangedListener(this)
                }
            }
        }
    }

    private fun setViewListener() {
        changePhoneNumberButton!!.setOnClickListener {
            if (phoneNumberEditText != null && phoneNumberEditText?.text.toString().isNotEmpty()) {
                KeyboardHandler.DropKeyboard(activity, phoneNumberEditText)
                showLoading()
//                presenter!!.changePhoneNumber(phoneNumberEditText?.text.toString().replace("-", ""))
                val phoneNumber = phoneNumberEditText?.text.toString().replace("-", "")
                phoneVerificationViewModel.changePhoneNumber(phoneNumber)
            } else {
                NetworkErrorHelper.showSnackbar(activity, getString(R.string.please_fill_phone_number))

                activity?.let {
                    val errorMessage = it.getString(R.string.please_fill_phone_number)
                    view?.let {itView->
                        Toaster.make(itView, errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                    }
                }
            }
        }
    }

    private fun showLoading() {
        mainView?.visibility = View.INVISIBLE
        progressDialog?.visibility = View.VISIBLE
    }

    private fun onSuccessChangePhoneNumber() {
        finishLoading();
        activity?.let{
            val intent = it.intent
            intent.putExtra(EXTRA_PHONE_NUMBER, phoneNumberEditText?.text.toString())
            it.setResult(RESULT_OK, intent);
            it.finish();
        }
    }


    private fun finishLoading() {
        mainView?.visibility = View.VISIBLE
        progressDialog?.visibility = View.GONE
    }

    private fun onErrorChangePhoneNumber(errorMessage: String) {
        finishLoading()
        NetworkErrorHelper.showSnackbar(activity, errorMessage);
    }


    companion object {
        const val ACTION_CHANGE_PHONE_NUMBER = 111
        const val EXTRA_PHONE_NUMBER = "EXTRA_PHONE_NUMBER"
        fun createInstance(): ChangePhoneNumberFragment {
            return ChangePhoneNumberFragment()
        }
    }
}