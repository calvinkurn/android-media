package com.tokopedia.profilecompletion.changename.view


import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.changename.data.ChangeNameResult
import com.tokopedia.profilecompletion.changename.viewmodel.ChangeNameViewModel
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_change_fullname, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserver()
    }

    private fun initListener() {
        changeNameButtonSave?.setOnClickListener {
            when {
                changeNameTextName?.text.isNullOrEmpty() || changeNameTextName?.text?.length as Int  <= MINIMUM_LENGTH -> {
                    onErrorChangeName(Throwable(resources.getString(R.string.error_name_too_short)))
                }
                changeNameTextName?.text?.length as Int > MAXIMUM_LENGTH -> {
                    onErrorChangeName(Throwable(resources.getString(R.string.error_name_too_long)))
                }
                else -> {
                    showLoading()
                    viewModel.changePublicName(changeNameTextName?.text.toString())
                }
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
    }


    private fun onErrorChangeName(throwable: Throwable) {
        hideLoading()
        view?.let { v ->
            throwable.message?.let {
                msg -> Toaster.make(v, msg, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
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
        private const val MINIMUM_LENGTH = 3
        private const val MAXIMUM_LENGTH = 128

        const val EXTRA_PROFILE_SCORE = "profile_score"
        const val EXTRA_PUBLIC_NAME = "public_name"

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = ChangeNameFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}
