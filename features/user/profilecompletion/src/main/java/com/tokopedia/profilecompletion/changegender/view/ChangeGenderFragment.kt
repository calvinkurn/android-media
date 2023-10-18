package com.tokopedia.profilecompletion.changegender.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.changegender.data.ChangeGenderResult
import com.tokopedia.profilecompletion.changegender.viewmodel.ChangeGenderViewModel
import com.tokopedia.profilecompletion.common.ColorUtils
import com.tokopedia.profilecompletion.databinding.FragmentChangeGenderBinding
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.profileinfo.tracker.ProfileInfoTracker
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ChangeGenderFragment : BaseDaggerFragment() {

    private var _binding: FragmentChangeGenderBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var tracker: ProfileInfoTracker

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(ChangeGenderViewModel::class.java) }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ColorUtils.setBackgroundColor(context, activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeGenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setObserver()
    }

    private fun setListener() {
        binding?.rgGender?.setOnCheckedChangeListener { _: RadioGroup, _: Int ->
            binding?.buttonSubmit?.isEnabled = true
        }

        binding?.buttonSubmit?.setOnClickListener {
            if (radioGroupIsSelected()) {
                tracker.trackOnBtnSimpanChangeGenderClick()
                showLoading()
                val selectedGenderView =
                    binding?.rgGender?.findViewById<RadioButton>(binding?.rgGender?.checkedRadioButtonId ?: 0)
                val selectedGender = binding?.rgGender?.indexOfChild(selectedGenderView)
                context?.let { ctx ->
                    viewModel.mutateChangeGender(
                        mapSelectedGender(selectedGender ?: 0)
                    )
                }
            }
        }
    }

    private fun mapSelectedGender(selectedGender: Int): Int {
        return if (selectedGender > 1) TYPE_WOMAN else TYPE_MAN
    }

    private fun radioGroupIsSelected(): Boolean {
        return binding?.rgGender?.checkedRadioButtonId != -1
    }

    private fun setObserver() {
        viewModel.mutateChangeGenderResponse.observe(
            viewLifecycleOwner,
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
        view?.run {
            val errorMsg = ErrorHandlerSession.getErrorMessage(throwable, context, true)
            tracker.trackOnBtnSimpanChangeGenderFailed(errorMsg)
            Toaster.showError(
                this,
                errorMsg,
                Snackbar.LENGTH_LONG
            )
        }
    }

    private fun onSuccessChangeGender(result: ChangeGenderResult) {
        dismissLoading()
        tracker.trackOnBtnSimpanChangeGenderSuccess()
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
        binding?.mainView?.visibility = View.GONE
        binding?.buttonSubmit?.visibility = View.GONE
        binding?.progressBar?.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        binding?.mainView?.visibility = View.VISIBLE
        binding?.buttonSubmit?.visibility = View.VISIBLE
        binding?.progressBar?.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.mutateChangeGenderResponse.removeObservers(this)
        viewModel.flush()
        _binding = null
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
