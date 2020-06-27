package com.tokopedia.talk.feature.write.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.write.di.DaggerTalkWriteComponent
import com.tokopedia.talk.feature.write.di.TalkWriteComponent
import com.tokopedia.talk.feature.write.presentation.viewmodel.TalkWriteViewModel
import com.tokopedia.talk_old.R
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_talk_write.*
import javax.inject.Inject

class TalkWriteFragment : BaseDaggerFragment(), HasComponent<TalkWriteComponent> {

    companion object {

        @JvmStatic
        fun createNewInstance(productId: Int): TalkWriteFragment {
            return TalkWriteFragment().apply {
                arguments = Bundle()
                arguments?.putInt(TalkConstants.PARAM_PRODUCT_ID, productId)
            }
        }

    }

    @Inject
    lateinit var viewModel: TalkWriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromArguments()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        observeReviewForm()
        return inflater.inflate(R.layout.fragment_talk_write, container, false)
    }

    override fun getComponent(): TalkWriteComponent {
        return DaggerTalkWriteComponent.builder().talkComponent(
                getComponent(TalkComponent::class.java))
                .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component.inject(this)
    }

    private fun initView() {
        writeTNC.setOnClickListener {
            goToTermsAndConditionsPage()
        }
    }

    private fun initQuestionTextField() {

    }

    private fun goToTermsAndConditionsPage() {
        RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${TalkConstants.TERMS_AND_CONDITIONS_PAGE_URL}")
    }

    private fun getDataFromArguments() {
        arguments?.let {
            viewModel.setProductId(it.getInt(TalkConstants.PARAM_PRODUCT_ID))
        }
    }

    private fun observeReviewForm() {
        viewModel.writeFormData.observe(viewLifecycleOwner, Observer {
            hideLoading()
            when(it) {
                is Success -> {
                    hideError()
                }
                is Fail -> {
                    showError()
                }
            }
        })
    }

    private fun showLoading() {
        talkWriteLoading.show()
    }

    private fun hideLoading() {
        talkWriteLoading.hide()
    }

    private fun showError() {
        talkWriteError.show()
    }

    private fun hideError() {
        talkWriteError.hide()
    }
}