package com.tokopedia.profilecompletion.changename.view


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.changename.data.analytics.ChangeNameTracker
import com.tokopedia.profilecompletion.changename.domain.pojo.ChangeNameResult
import com.tokopedia.profilecompletion.changename.viewmodel.ChangeNameViewModel
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_add_name_register.view.*
import kotlinx.android.synthetic.main.fragment_change_fullname.*
import javax.inject.Inject

/**
 * created by rival 23/10/19
 */
class ChangeNameFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ChangeNameViewModel::class.java) }

    private var oldName: String = ""

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_change_fullname, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oldName = arguments?.getString(ApplinkConstInternalGlobal.PARAM_FULL_NAME).toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserver()

        if (oldName.isNotEmpty()) {
            changeNameTextName?.setText(oldName)
        }
    }

    private fun initListener() {
        changeNameTextName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    when {
                        s.isEmpty() || s.length < MINIMUM_LENGTH || s.length > MAXIMUM_LENGTH -> {
                            if (s.length < MINIMUM_LENGTH) {
                                onErrorChangeName(Throwable(resources.getString(R.string.error_name_too_short)))
                            } else if (s.length > MAXIMUM_LENGTH) {
                                onErrorChangeName(Throwable(resources.getString(R.string.error_name_too_long_35)))
                            }
                            changeNameButtonSave?.isEnabled = false
                        }
                        s.toString() == oldName -> {
                            onErrorChangeName(Throwable(resources.getString(R.string.error_name_same_with_previous)))
                            changeNameButtonSave?.isEnabled = false
                        }
                        else -> {
                            changeNameButtonSave?.isEnabled = true
                            activity?.let {
                                changeNameTextMessage?.run {
                                    text = getString(R.string.change_name_visible_on_another_user)
                                    setTextColor(ContextCompat.getColor(it, R.color.grey_700))
                                }
                            }
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        changeNameButtonSave?.setOnClickListener {
            val fullName = changeNameTextName?.text
            if (fullName != null) {
                showLoading()
                viewModel.changePublicName(changeNameTextName?.text.toString())
            } else {
                onErrorChangeName(Throwable(resources.getString(R.string.error_name_too_short)))
            }
        }
    }

    private fun initObserver() {
        viewModel.changeNameResponse.observe(
                this,
                Observer {
                    when (it) {
                        is Success -> onSuccessChangeName(it.data)
                        is Fail -> onErrorChangeName(it.throwable)
                    }
                }
        )
    }

    private fun onSuccessChangeName(result: ChangeNameResult) {
        hideLoading()
        activity?.run {
            val intent = Intent()
            val bundle = Bundle()
            bundle.putInt(EXTRA_PROFILE_SCORE, result.data.completionScore)
            bundle.putString(EXTRA_PUBLIC_NAME, result.fullName)
            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        ChangeNameTracker().onSuccessChangeName()
    }


    private fun onErrorChangeName(throwable: Throwable) {
        hideLoading()

        throwable.message?.let { message ->
            activity?.let {
                changeNameTextMessage?.run {
                    text = message
                    setTextColor(ContextCompat.getColor(it, R.color.red_500))
                    ChangeNameTracker().onFailedChangeName(throwable.message.toString())
                }
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
