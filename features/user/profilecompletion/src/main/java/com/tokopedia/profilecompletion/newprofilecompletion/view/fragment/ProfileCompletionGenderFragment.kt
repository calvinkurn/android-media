package com.tokopedia.profilecompletion.newprofilecompletion.view.fragment

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
import com.tokopedia.profilecompletion.newprofilecompletion.ProfileCompletionNewConstants
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by stevenfredian on 7/3/17.
 */
class ProfileCompletionGenderFragment : BaseDaggerFragment() {

    private var profileCompletionFragment: ProfileCompletionFragment? = null
    private var proceed: View? = null
    private var radioGroup: RadioGroup? = null
    private var avaWoman: View? = null
    private var avaMan: View? = null
    private var skip: View? = null
    private var progress: View? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ChangeGenderViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parentView = inflater.inflate(R.layout.fragment_profile_completion_gender, container, false)
        initView(parentView)
        setViewListener()
        initialObserver()
        return parentView
    }

    private fun initView(view: View) {
        avaMan = view.findViewById(R.id.ava_man)
        avaWoman = view.findViewById(R.id.ava_woman)
        radioGroup = view.findViewById(R.id.radioGroup)
        proceed = profileCompletionFragment?.view?.findViewById(R.id.proceed)
        skip = profileCompletionFragment?.view?.findViewById(R.id.skip)
        progress = profileCompletionFragment?.view?.findViewById(R.id.progress)
        profileCompletionFragment?.canProceed(false)
    }

    private fun setViewListener() {
        radioGroup?.setOnCheckedChangeListener { radioGroup, i ->
            profileCompletionFragment?.canProceed(true)
        }
        proceed?.setOnClickListener {
            val selected = radioGroup?.findViewById<View>(radioGroup?.checkedRadioButtonId?: 0)
            var idx = radioGroup?.indexOfChild(selected)
            if (selected === avaMan) {
                idx = ProfileCompletionNewConstants.MALE
            } else if (selected === avaWoman) {
                idx = ProfileCompletionNewConstants.FEMALE
            }
            idx?.let {
                profileCompletionFragment?.disableView()
                if(it == -1) {
                    profileCompletionFragment?.onFailedEditProfile(getString(R.string.invalid_gender))
                } else {
                    context?.let { ctx -> viewModel.mutateChangeGender(ctx, it) }
                }
            }
        }
        skip?.setOnClickListener { profileCompletionFragment?.skipView(TAG) }
    }

    private fun initialObserver() {
        viewModel.mutateChangeGenderResponse.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> {
                    if(it.data.data.isSuccess) {
                        profileCompletionFragment?.onSuccessEditProfile(ProfileCompletionNewConstants.EDIT_GENDER)
                    } else {
                        profileCompletionFragment?.onFailedEditProfile(it.data.data.errorMessage)
                    }
                }
                is Fail -> { profileCompletionFragment?.onFailedEditProfile(ErrorHandler.getErrorMessage(context, it.throwable)) }
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