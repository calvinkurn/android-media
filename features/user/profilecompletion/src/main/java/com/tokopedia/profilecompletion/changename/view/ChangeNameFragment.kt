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
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.changename.domain.pojo.ChangeNameResult
import com.tokopedia.profilecompletion.changename.viewmodel.ChangeNameViewModel
import com.tokopedia.profilecompletion.common.ColorUtils
import com.tokopedia.profilecompletion.databinding.FragmentChangeFullnameBinding
import com.tokopedia.profilecompletion.databinding.FragmentOnboardPinBinding
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.profileinfo.tracker.ProfileInfoTracker
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

/**
 * created by rival 23/10/19
 */
class ChangeNameFragment : BaseDaggerFragment() {

    private var _binding: FragmentChangeFullnameBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentChangeFullnameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ColorUtils.setBackgroundColor(context, activity)
        oldName = arguments?.getString(ApplinkConstInternalUserPlatform.PARAM_FULL_NAME).toString()
        chancesChangeName =
            arguments?.getString(ApplinkConstInternalUserPlatform.PARAM_CHANCE_CHANGE_NAME)
                .toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (oldName.isNotEmpty()) {
            binding?.changeNameTextName?.editText?.setText(oldName)
            binding?.changeNameTextName?.editText?.text?.length?.let {
                binding?.changeNameTextName?.editText?.setSelection(it)
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
        binding?.changeNameTextName?.icon2?.setOnClickListener {
            binding?.changeNameTextName?.editText?.text?.clear()
        }

        binding?.changeNameTextName?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding?.changeNameTextName?.isInputError = false
                binding?.changeNameTextName?.setMessage("")
                if (s != null) {
                    when {
                        s.length < MINIMUM_LENGTH || s.length > MAXIMUM_LENGTH -> {
                            when {
                                s.isEmpty() -> onErrorChangeName(
                                    Throwable(
                                        activity?.resources?.getString(
                                            R.string.error_name_cant_empty
                                        )
                                    )
                                )
                                s.length < MINIMUM_LENGTH -> onErrorChangeName(
                                    Throwable(
                                        activity?.resources?.getString(
                                            R.string.error_name_min_3
                                        )
                                    )
                                )
                                s.length > MAXIMUM_LENGTH -> onErrorChangeName(
                                    Throwable(
                                        activity?.resources?.getString(
                                            R.string.error_name_max_35
                                        )
                                    )
                                )
                            }
                            binding?.changeNameButtonSave?.isEnabled = false
                        }
                        s.toString() == oldName -> {
                            binding?.changeNameButtonSave?.isEnabled = false
                        }
                        else -> {
                            binding?.changeNameButtonSave?.isEnabled = true
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (binding?.changeNameTextName?.editText?.text?.isNotEmpty() == true) {
                    binding?.changeNameTextName?.icon2?.show()
                } else {
                    binding?.changeNameTextName?.icon2?.gone()
                }

            }
        })

        binding?.changeNameButtonSave?.setOnClickListener {
            infoTracker.trackOnClickBtnSimpanChangeNameClick()
            val fullName = binding?.changeNameTextName?.editText?.text
            if (fullName != null) {
                showLoading()
                viewModel.changePublicName(binding?.changeNameTextName?.editText?.text.toString())
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
            binding?.changeNameTextNote?.text = changeNameHint
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
        binding?.changeNameTextName?.isInputError = true
        throwable.message?.let { message ->
            infoTracker.trackOnClickBtnSimpanChangeNameFailed(message)
            activity?.let {
                binding?.changeNameTextName?.setMessage(message)
            }
        }
    }

    private fun showLoading() {
        binding?.changeNameViewMain?.hide()
        binding?.changeNameProgressBar?.show()
    }

    private fun hideLoading() {
        binding?.changeNameViewMain?.show()
        binding?.changeNameProgressBar?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
