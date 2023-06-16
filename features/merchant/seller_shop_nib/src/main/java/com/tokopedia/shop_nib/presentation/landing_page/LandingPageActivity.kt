package com.tokopedia.shop_nib.presentation.landing_page

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop_nib.R
import com.tokopedia.shop_nib.databinding.SsnActivityLandingPageBinding
import com.tokopedia.shop_nib.di.component.DaggerShopNibComponent
import com.tokopedia.shop_nib.domain.entity.NibSubmissionResult
import com.tokopedia.shop_nib.presentation.submission.NibSubmissionFragment
import com.tokopedia.shop_nib.presentation.submitted.NibAlreadySubmittedFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class LandingPageActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[LandingPageViewModel::class.java] }
    private var binding: SsnActivityLandingPageBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setupDependencyInjection()
        super.onCreate(savedInstanceState)

        binding = SsnActivityLandingPageBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)

        observePreviousNibSubmission()
        viewModel.checkPreviousSubmission()
    }

    private fun setupDependencyInjection() {
        DaggerShopNibComponent.builder()
            .baseAppComponent((applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)

    }

    private fun observePreviousNibSubmission() {
        viewModel.previousSubmissionState.observe(this){ result ->
            binding?.loader?.gone()

            when(result) {
                is Success -> handleData(result.data)
                is Fail -> {
                    val fragment = NibSubmissionFragment.newInstance()
                    renderFragment(fragment)
                }
            }
        }
    }

    private fun handleData(submissionResult: NibSubmissionResult) {
        if (submissionResult.success) {
            handleRedirection(submissionResult.hasPreviousSubmission)
        } else {
            showError(submissionResult.errorMessage)
        }
    }

    private fun handleRedirection(hasPreviousSubmission: Boolean) {
        if (hasPreviousSubmission) {
            val fragment = NibAlreadySubmittedFragment.newInstance()
            renderFragment(fragment)
        } else {
            val fragment = NibSubmissionFragment.newInstance()
            renderFragment(fragment)
        }
    }

    private fun showError(errorMessage: String) {
        binding?.container?.showToasterError(errorMessage)
    }

    private fun renderFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, fragment.tag)
            .commit()
    }

}

