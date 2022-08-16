package com.tokopedia.profilecompletion.changename.view


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.changename.data.analytics.ChangeNameTracker
import com.tokopedia.profilecompletion.changename.domain.pojo.ChangeNameResult
import com.tokopedia.profilecompletion.changename.viewmodel.ChangeNameViewModel
import com.tokopedia.profilecompletion.common.ColorUtils
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.profileinfo.tracker.ProfileInfoTracker
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_change_fullname.*
import javax.inject.Inject

/**
 * created by rival 23/10/19
 */
class ChangeNameFragment : BaseDaggerFragment() {

    @Inject
    lateinit var infoTracker: ProfileInfoTracker

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ChangeNameViewModel::class.java) }

    @Inject
    lateinit var userSession: UserSessionInterface

    private var oldName: String = ""
    private var chancesChangeName: String = ""

    override fun getScreenName(): String = ""

    override fun initInjector() {
	getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    override fun onCreateView(
	inflater: LayoutInflater,
	container: ViewGroup?,
	savedInstanceState: Bundle?
    ): View? {
	return inflater.inflate(R.layout.fragment_change_fullname, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	ColorUtils.setBackgroundColor(context, activity)
	oldName = arguments?.getString(ApplinkConstInternalUserPlatform.PARAM_FULL_NAME).toString()
	chancesChangeName =
	    arguments?.getString(ApplinkConstInternalUserPlatform.PARAM_CHANCE_CHANGE_NAME).toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
	super.onViewCreated(view, savedInstanceState)

	if (oldName.isNotEmpty()) {
	    changeNameTextName?.editText?.setText(oldName)
	    changeNameTextName?.editText?.text?.length?.let {
		changeNameTextName?.editText?.setSelection(it)
	    }
	}

	initObserver()
	initListener()
	viewModel.getUserProfileRole()
    }

    override fun onFragmentBackPressed(): Boolean {
	infoTracker.trackOnClickBtnBackChangeName()
	return super.onFragmentBackPressed()
    }

    private fun initListener() {
	changeNameTextName?.icon2?.setOnClickListener {
	    changeNameTextName?.editText?.text?.clear()
	}

	changeNameTextName?.editText?.addTextChangedListener(object : TextWatcher {
	    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

	    }

	    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
		changeNameTextName.isInputError = false
		changeNameTextName.setMessage("")
		if (s != null) {
		    when {
			s.length < MINIMUM_LENGTH || s.length > MAXIMUM_LENGTH -> {
			    when {
					s.isEmpty() -> onErrorChangeName(Throwable(activity?.resources?.getString(R.string.error_name_cant_empty)))
					s.length < MINIMUM_LENGTH -> onErrorChangeName(Throwable(activity?.resources?.getString(R.string.error_name_min_3)))
					s.length > MAXIMUM_LENGTH -> onErrorChangeName(Throwable(activity?.resources?.getString(R.string.error_name_max_35)))
			    }
			    changeNameButtonSave?.isEnabled = false
			}
			s.toString() == oldName -> {
			    changeNameButtonSave?.isEnabled = false
			}
			else -> {
			    changeNameButtonSave?.isEnabled = true
			}
		    }
		}
	    }

	    override fun afterTextChanged(s: Editable?) {
		if (changeNameTextName?.editText?.text?.isNotEmpty() == true) {
		    changeNameTextName?.icon2?.show()
		} else {
		    changeNameTextName?.icon2?.gone()
		}

	    }
	})

	changeNameButtonSave?.setOnClickListener {
	    infoTracker.trackOnClickBtnSimpanChangeNameClick()
	    val fullName = changeNameTextName?.editText?.text
	    if (fullName != null) {
			showLoading()
			viewModel.changePublicName(changeNameTextName?.editText?.text.toString())
	    } else {
			onErrorChangeName(Throwable(activity?.resources?.getString(R.string.error_name_too_short)))
	    }
	}
    }

    private fun initObserver() {
	viewModel.changeNameResponse.observe(viewLifecycleOwner, Observer {
	    when (it) {
		is Success -> onSuccessChangeName(it.data)
		is Fail -> onErrorChangeName(it.throwable)
	    }
	})

	viewModel.userProfileRole.observe(viewLifecycleOwner, Observer {
	    when (it) {
		is Success -> {
		    updateChangesCounter(it.data.chancesChangeName)
		}
		is Fail -> {
		    updateChangesCounter(chancesChangeName)
		}
	    }
	})
    }

    private fun updateChangesCounter(counter: String) {
		activity?.getString(R.string.change_name_note)?.let { changeNameHint ->
			changeNameTextNote?.text = changeNameHint
		}
    }

    private fun onSuccessChangeName(result: ChangeNameResult) {
	hideLoading()
	userSession.name = result.fullName

	activity?.run {
	    val intent = Intent()
	    val bundle = Bundle()
	    bundle.putInt(EXTRA_PROFILE_SCORE, result.data.completionScore)
	    bundle.putString(EXTRA_PUBLIC_NAME, result.fullName)
	    intent.putExtras(bundle)
	    setResult(Activity.RESULT_OK, intent)
	    finish()
	}

	infoTracker.trackOnClickBtnSimpanChangeNameSuccess()
    }


    private fun onErrorChangeName(throwable: Throwable) {
	hideLoading()
	changeNameTextName.isInputError = true
	throwable.message?.let { message ->
	    infoTracker.trackOnClickBtnSimpanChangeNameFailed(message)
	    activity?.let {
		changeNameTextName.setMessage(message)
	    }
	}
    }

    private fun showLoading() {
	changeNameViewMain?.hide()
	changeNameProgressBar?.show()
    }

    private fun hideLoading() {
	changeNameViewMain?.show()
	changeNameProgressBar?.hide()
    }

    companion object {
	const val MINIMUM_LENGTH = 3
	const val MAXIMUM_LENGTH = 35

	const val EXTRA_PROFILE_SCORE = "profile_score"
	const val EXTRA_PUBLIC_NAME = "public_name"

	fun createInstance(bundle: Bundle): Fragment {
	    val fragment = ChangeNameFragment()
	    fragment.arguments = bundle
	    return fragment
	}
    }

}
