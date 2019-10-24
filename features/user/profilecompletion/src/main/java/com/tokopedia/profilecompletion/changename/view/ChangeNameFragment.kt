package com.tokopedia.profilecompletion.changename.view


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.profilecompletion.R
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
                    context?.let { ctx ->
                        viewModel.mutateChangeName(ctx, changeNameTextName?.text.toString())
                    }
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.mutateChangeNameResponse.observe(
                this,
                Observer {
                    when (it) {
                        is Success -> onSuccessChangeName(it.data.fullName)
                        is Fail -> onErrorChangeName(it.throwable)
                    }
                }
        )
    }

    private fun onSuccessChangeName(name: String) {
        hideLoading()
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
        changeNameViewMain?.visibility = View.GONE
        changeNameProgressBar?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        changeNameViewMain?.visibility = View.VISIBLE
        changeNameProgressBar?.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.mutateChangeNameResponse.removeObservers(this)
        viewModel.clear()
    }

    companion object {
        private const val MINIMUM_LENGTH = 3
        private const val MAXIMUM_LENGTH = 128

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = ChangeNameFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}
