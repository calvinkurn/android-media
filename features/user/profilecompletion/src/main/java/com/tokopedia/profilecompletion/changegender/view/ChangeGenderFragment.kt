package com.tokopedia.profilecompletion.changegender.view

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.changegender.data.ChangeGenderResult
import com.tokopedia.profilecompletion.changegender.viewmodel.ChangeGenderViewModel
import com.tokopedia.profilecompletion.di.ProfileCompletionComponent
//import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_change_gender.*
import javax.inject.Inject


class ChangeGenderFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(ChangeGenderViewModel::class.java) }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(ProfileCompletionComponent::class.java).inject(this)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_change_gender, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setObserver()

    }

    private fun setListener() {
        radioGroup.setOnCheckedChangeListener { _: RadioGroup, _: Int ->
            buttonSubmit.buttonCompatType = ButtonCompat.PRIMARY
        }

        buttonSubmit.setOnClickListener {
            if (radioGroupIsSelected()) {
                showLoading()
                val selectedGenderView = radioGroup.findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
                val selectedGender = radioGroup.indexOfChild(selectedGenderView)
                viewModel.mutateChangeGender(mapSelectedGender(selectedGender))
            }
        }
    }

    private fun mapSelectedGender(selectedGender: Int): Int {
        return if (selectedGender > 1) TYPE_WOMAN else TYPE_MAN
    }


    private fun radioGroupIsSelected(): Boolean {
        return radioGroup.checkedRadioButtonId != -1
    }

    private fun setObserver() {
        viewModel.mutateChangeGenderResponse.observe(
                this,
                Observer {
                    when (it) {
                        is Success -> onSuccessChangeGender(it.data)
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

    private fun onSuccessChangeGender(result: ChangeGenderResult) {
        dismissLoading()
        activity?.run {
            val intent = Intent()
            val bundle = Bundle()
            bundle.putInt(EXTRA_PROFILE_SCORE, result.data.completionScore)
            bundle.putInt(EXTRA_SELECTED_GENDER, result.selectedGender)
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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.mutateChangeGenderResponse.removeObservers(this)
        viewModel.clear()


    }

    companion object {

        val TYPE_MAN = 1
        val TYPE_WOMAN = 2

        val EXTRA_PROFILE_SCORE = "profile_score"
        val EXTRA_SELECTED_GENDER = "selected_gender"

        fun createInstance(bundle: Bundle): ChangeGenderFragment {
            val fragment = ChangeGenderFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}