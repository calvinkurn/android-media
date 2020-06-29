package com.tokopedia.talk.feature.write.presentation.fragment

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.write.analytics.TalkWriteTracking
import com.tokopedia.talk.feature.write.data.model.DiscussionGetWritingForm
import com.tokopedia.talk.feature.write.di.DaggerTalkWriteComponent
import com.tokopedia.talk.feature.write.di.TalkWriteComponent
import com.tokopedia.talk.feature.write.presentation.decorator.SpacingItemDecoration
import com.tokopedia.talk.feature.write.presentation.listener.TalkWriteCategoryDetailsListener
import com.tokopedia.talk.feature.write.presentation.uimodel.TalkWriteCategory
import com.tokopedia.talk.feature.write.presentation.viewmodel.TalkWriteViewModel
import com.tokopedia.talk.feature.write.presentation.widget.TalkWriteCategoryChipsWidget
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_talk_reading.*
import kotlinx.android.synthetic.main.fragment_talk_write.*
import kotlinx.android.synthetic.main.partial_talk_connection_error.*
import javax.inject.Inject

class TalkWriteFragment : BaseDaggerFragment(),
        HasComponent<TalkWriteComponent>, TalkWriteCategoryChipsWidget.ChipClickListener,
        TalkWriteCategoryDetailsListener {

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

    private val adapter = TalkWriteCategoryChipsWidget(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromArguments()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        observeReviewForm()
        observeButtonState()
        observeCategories()
        return inflater.inflate(R.layout.fragment_talk_write, container, false)
    }

    override fun getComponent(): TalkWriteComponent {
        return DaggerTalkWriteComponent.builder().talkComponent(
                getComponent(TalkComponent::class.java))
                .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initRecycleView()
        initQuestionTextField()
        showLoading()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component.inject(this)
    }

    override fun onChipClicked(category: TalkWriteCategory) {
        viewModel.toggleCategory(category)
    }

    override fun onClickGoToChat() {
        goToChat()
    }

    private fun initView() {
        writeTNC.setOnClickListener {
            goToTermsAndConditionsPage()
        }
        talkWriteButton.setOnClickListener {
            submitNewQuestion()
        }
        talkConnectionErrorRetryButton.setOnClickListener {

        }
        talkConnectionErrorGoToSettingsButton.setOnClickListener {
            goToSettingsPage()
        }
    }

    private fun initRecycleView() {
        context?.resources?.getDimensionPixelOffset(com.tokopedia.unifycomponents.R.dimen.layout_lvl1)?.let { writeCategoryChips.addItemDecoration(SpacingItemDecoration(it)) }
        writeCategoryChips.apply {
            adapter = this@TalkWriteFragment.adapter
            layoutManager = ChipsLayoutManager.newBuilder(context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()
        }
    }

    private fun initQuestionTextField() {
        writeQuestionTextArea.textAreaInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    viewModel.updateIsTextEmpty(it.isNotEmpty())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No op
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No op
            }
        })
    }

    private fun goToChat() {

    }

    private fun goToReplyPage(questionId: Int) {
        // TO DO add shop ID
        val intent = RouteManager.getIntent(
                context,
                Uri.parse(UriUtil.buildUri(ApplinkConstInternalGlobal.TALK_REPLY, questionId.toString()))
                        .buildUpon()
                        .appendQueryParameter(TalkConstants.PARAM_PRODUCT_ID, viewModel.getProductId().toString())
                        .appendQueryParameter(TalkConstants.PARAM_SHOP_ID, "")
                        .appendQueryParameter(TalkConstants.PARAM_SOURCE, TalkConstants.WRITING_SOURCE)
                        .build().toString()
        )
        startActivity(intent)
    }

    private fun goToSettingsPage() {
        RouteManager.route(context, ApplinkConstInternalGlobal.GENERAL_SETTING)
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
                    onSuccessGetWriteData(it.data)
                }
                is Fail -> {
                    showError()
                }
            }
        })
    }

    private fun observeButtonState() {
        viewModel.buttonState.observe(viewLifecycleOwner, Observer {
            talkWriteButton.isEnabled = it.isAnyCategorySelected && it.isNotTextEmpty
        })
    }

    private fun observeCategories() {
        viewModel.categoryChips.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()) {
                return@Observer
            }
            adapter.setData(it)
            val selectedCategory = it.firstOrNull { category -> category.isSelected }
            if(selectedCategory == null) {
                writeCategoryDetails.hide()
                return@Observer
            }
            showCategoryDetails(selectedCategory.content)
        })
    }

    private fun onSuccessGetWriteData(discussionGetWritingForm: DiscussionGetWritingForm) {
        setMaxCharsLimit(discussionGetWritingForm.maxChar)
    }

    private fun submitNewQuestion() {
        TalkWriteTracking.eventClickSendButton(viewModel.userId, viewModel.getProductId().toString())
    }

    private fun setMaxCharsLimit(limit: Int) {
        writeQuestionTextArea.textAreaCounter = limit
    }

    private fun showErrorToaster() {
        view?.let { Toaster.build(talkReadingContainer, getString(R.string.reading_connection_error_toaster_message), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.talk_ok)).show()}
    }

    private fun showError() {
        talkWriteError.show()
    }

    private fun showLoading() {
        talkWriteLoading.show()
    }

    private fun hideError() {
        talkWriteError.hide()
    }

    private fun hideLoading() {
        talkWriteLoading.hide()
    }

    private fun showCategoryDetails(content: String?) {
        if(content.isNullOrEmpty()) {
            return
        }
        writeCategoryDetails.apply {
            show()
            setContent(content, this@TalkWriteFragment)
        }
    }
}