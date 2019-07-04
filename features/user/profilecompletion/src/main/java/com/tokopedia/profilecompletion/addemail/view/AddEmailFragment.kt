package com.tokopedia.profilecompletion.addemail.view

//import com.tokopedia.unifycomponents.Toaster
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addemail.viewmodel.AddEmailViewModel
import com.tokopedia.profilecompletion.changegender.data.AddEmailPojo
import com.tokopedia.profilecompletion.di.ProfileCompletionComponent
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_add_email.*
import javax.inject.Inject


class AddEmailFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(AddEmailViewModel::class.java) }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(ProfileCompletionComponent::class.java).inject(this)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_email, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setObserver()

    }

    private fun setListener() {
        et_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setErrorText("")
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        buttonSubmit.setOnClickListener {
            val email = et_email.text.toString()
            if (email.isBlank()) {
                setErrorText(getString(R.string.error_field_required))
            } else if (!isValidEmail(email)) {
                setErrorText(getString(R.string.wrong_email_format))
            }else{
                showLoading()
                viewModel.mutateAddEmail(et_email.text.toString().trim())
            }
        }
    }

    private fun setErrorText(s: String) {
        if (TextUtils.isEmpty(s)) {
            tv_message.setVisibility(View.VISIBLE)
            tv_error.setVisibility(View.GONE)
            buttonSubmit.isEnabled = true
            buttonSubmit.buttonCompatType = ButtonCompat.PRIMARY
            wrapper_email.setErrorEnabled(true)
        } else {
            wrapper_email.setErrorEnabled(false)
            tv_error.setVisibility(View.VISIBLE)
            tv_error.setText(s)
            tv_message.setVisibility(View.GONE)
            buttonSubmit.isEnabled = false
            buttonSubmit.buttonCompatType = ButtonCompat.PRIMARY_DISABLED

        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun setObserver() {
        viewModel.mutateAddEmailResponse.observe(
                this,
                Observer {
                    when (it) {
                        is Success -> onSuccessAddEmail(it.data)
                        is Fail -> onErrorChangeGender(it.throwable)
                    }
                }
        )

    }

    private fun onErrorChangeGender(throwable: Throwable) {
        dismissLoading()
        Log.d("NISNIS", throwable.message)
        //TODO uncomment after unify is fixed
//        view?.run {
//            Toaster.showError(
//                    this,
//                    ErrorHandlerSession.getErrorMessage(throwable, context, true),
//                    Snackbar.LENGTH_LONG)
//        }
    }

    private fun onSuccessAddEmail(data: AddEmailPojo) {
        dismissLoading()
        activity?.run {
            val intent = Intent()
            val bundle = Bundle()
            bundle.putInt(EXTRA_PROFILE_SCORE, data.addEmailData.userProfileCompletionUpdate.completionScore)
            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }


    private fun showLoading() {
        mainView.visibility = View.GONE
        buttonSubmit.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        mainView.visibility = View.VISIBLE
        buttonSubmit.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }


    companion object {

        val TYPE_MAN = 1
        val TYPE_WOMAN = 2

        val EXTRA_PROFILE_SCORE = "profile_score"
        val EXTRA_SELECTED_GENDER = "selected_gender"

        fun createInstance(bundle: Bundle): AddEmailFragment {
            val fragment = AddEmailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}