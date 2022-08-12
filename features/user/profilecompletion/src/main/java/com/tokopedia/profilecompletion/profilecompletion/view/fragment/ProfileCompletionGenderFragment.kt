package com.tokopedia.profilecompletion.profilecompletion.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.changegender.viewmodel.ChangeGenderViewModel
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.profilecompletion.ProfileCompletionNewConstants
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by stevenfredian on 7/3/17.
 */
class ProfileCompletionGenderFragment : BaseDaggerFragment() {

    private var profileCompletionFragment: ProfileCompletionFragment? = null
    private var txtProceed: View? = null
    private var rgGender: RadioGroup? = null
    private var rbWoman: View? = null
    private var rbMan: View? = null
    private var txtSkip: View? = null
    private var progressBar: View? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ChangeGenderViewModel::class.java) }

    override fun onCreateView(
	inflater: LayoutInflater,
	container: ViewGroup?,
	savedInstanceState: Bundle?
    ): View? {
	val parentView =
	    inflater.inflate(R.layout.fragment_profile_completion_gender, container, false)
	initView(parentView)
	setViewListener()
	initialObserver()
	return parentView
    }

    private fun initView(view: View) {
	rbMan = view.findViewById(R.id.rb_man)
	rbWoman = view.findViewById(R.id.rb_woman)
	rgGender = view.findViewById(R.id.rg_gender)
	txtProceed = profileCompletionFragment?.view?.findViewById(R.id.txt_proceed)
	txtSkip = profileCompletionFragment?.view?.findViewById(R.id.txt_skip)
	progressBar = profileCompletionFragment?.view?.findViewById(R.id.progress_bar)
	profileCompletionFragment?.canProceed(false)
    }

    private fun setViewListener() {
	rgGender?.setOnCheckedChangeListener { radioGroup, i ->
	    profileCompletionFragment?.canProceed(true)
	}
	txtProceed?.setOnClickListener {
	    val selected = rgGender?.findViewById<View>(rgGender?.checkedRadioButtonId ?: 0)
	    var idx = rgGender?.indexOfChild(selected)
	    if (selected === rbMan) {
		idx = ProfileCompletionNewConstants.MALE
	    } else if (selected === rbWoman) {
		idx = ProfileCompletionNewConstants.FEMALE
	    }
	    idx?.let {
		profileCompletionFragment?.disableView()
		if (it == -1) {
		    profileCompletionFragment?.onFailedEditProfile(getString(R.string.invalid_gender))
		} else {
		    context?.let { ctx -> viewModel.mutateChangeGender(ctx, it) }
		}
	    }
	}
	txtSkip?.setOnClickListener { profileCompletionFragment?.skipView(TAG) }
    }

    private fun initialObserver() {
	viewModel.mutateChangeGenderResponse.observe(viewLifecycleOwner, Observer {
	    when (it) {
		is Success -> {
		    if (it.data.data.isSuccess) {
			profileCompletionFragment?.onSuccessEditProfile(
			    ProfileCompletionNewConstants.EDIT_GENDER
			)
		    } else {
			profileCompletionFragment?.onFailedEditProfile(it.data.data.errorMessage)
		    }
		}
		is Fail -> {
		    profileCompletionFragment?.onFailedEditProfile(
			ErrorHandler.getErrorMessage(
			    context,
			    it.throwable
			)
		    )
		}
	    }
	})
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
	getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    companion object {
	const val TAG = "gender"

	fun createInstance(view: ProfileCompletionFragment?): ProfileCompletionGenderFragment {
	    val fragment = ProfileCompletionGenderFragment()
	    fragment.profileCompletionFragment = view
	    return fragment
	}
    }
}