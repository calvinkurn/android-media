package com.tokopedia.top_ads_headline.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.HeadlineAdStepperModel
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.activity.HeadlineStepperActivity
import com.tokopedia.top_ads_headline.view.viewmodel.AdDetailsViewModel
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_ad_details.*
import javax.inject.Inject

private const val view_iklan_toko = "view - buat iklan toko"
private const val click_lanjutkan_toko = "click - lanjutkan on buat iklan toko page"
class AdDetailsFragment : BaseHeadlineStepperFragment<HeadlineAdStepperModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private lateinit var adDetailsViewModel: AdDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adDetailsViewModel = ViewModelProvider(this, viewModelFactory).get(AdDetailsViewModel::class.java)
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineCreatFormEvent(view_iklan_toko, "{${userSession.shopId}}", userSession.userId)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ad_details, container, false)
    }

    override fun getScreenName(): String {
        return AdDetailsFragment::class.java.simpleName
    }

    override fun initInjector() {
        DaggerHeadlineAdsComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment =
                AdDetailsFragment()
    }

    override fun gotoNextPage() {
        stepperModel?.groupName = headlineAdNameInput?.textFieldInput?.text.toString()
        stepperListener?.goToNextPage(stepperModel)
    }

    override fun updateToolBar() {
        if (activity is HeadlineStepperActivity) {
            (activity as HeadlineStepperActivity).updateToolbarTitle(getString(R.string.topads_headline_ad_detail_fragment_label))
        }
    }

    override fun populateView() {
        setUpSubmitButtonClick()
        setUpAdNameEditText()
    }

    private fun setUpAdNameEditText() {
        headlineAdNameInput?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                headlineAdNameInput?.setError(false)
                btnSubmit?.isEnabled = s.toString().trim().isNotEmpty()
                if (s.toString().isBlank()) {
                    headlineAdNameInput?.getFirstIcon()?.hide()
                } else {
                    headlineAdNameInput?.getFirstIcon()?.show()
                }
            }
        })
        headlineAdNameInput?.textFieldInput?.setOnEditorActionListener { v, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> validateGroup(v?.text.toString())
            }
            Utils.dismissKeyboard(context, v)
            true
        }
        headlineAdNameInput?.getFirstIcon()?.setOnClickListener {
            headlineAdNameInput?.textFieldInput?.setText("")
            it.hide()
        }
    }

    private fun setUpSubmitButtonClick() {
        btnSubmit.setOnClickListener {
            if (headlineAdNameInput?.textFieldInput?.text.toString().isBlank()) {
                onError(getString(R.string.topads_headline_ad_name_required))
            } else {
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineCreatFormClickEvent(click_lanjutkan_toko, "{${userSession.shopId}} - {${headlineAdNameInput?.textFieldInput?.text.toString()}}", userSession.userId)
                validateGroup(headlineAdNameInput?.textFieldInput?.text.toString())
            }
        }
    }

    private fun validateGroup(s: String?) {
        s?.let {
            adDetailsViewModel.validateGroup(it, this::onSuccess, this::onError)
        }
    }

    private fun onError(errorMsg: String) {
        errorTextVisibility(true)
        headlineAdNameInput?.setMessage(errorMsg)
    }

    private fun onSuccess() {
        errorTextVisibility(false)
        gotoNextPage()
    }

    private fun errorTextVisibility(visible: Boolean) {
        headlineAdNameInput?.setError(visible)
        btnSubmit?.isEnabled = !visible
    }

}