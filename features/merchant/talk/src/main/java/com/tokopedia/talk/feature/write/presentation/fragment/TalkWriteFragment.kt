package com.tokopedia.talk.feature.write.presentation.fragment

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import androidx.lifecycle.Observer
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonNull
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringContract
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringListener
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.common.utils.setCustomMovementMethod
import com.tokopedia.talk.feature.reading.presentation.fragment.TalkReadingFragment
import com.tokopedia.talk.feature.reply.presentation.activity.TalkReplyActivity
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
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import kotlinx.android.synthetic.main.fragment_talk_write.*
import kotlinx.android.synthetic.main.partial_talk_connection_error.*
import kotlinx.android.synthetic.main.widget_talk_write_header.*
import javax.inject.Inject

class TalkWriteFragment : BaseDaggerFragment(),
        HasComponent<TalkWriteComponent>, TalkWriteCategoryChipsWidget.ChipClickListener,
        TalkWriteCategoryDetailsListener, TalkPerformanceMonitoringContract {

    companion object {
        const val TOP_CHAT_APPLINK = "tokopedia://topchat/askseller/"
        const val KEY_CACHE_MANAGER = "cache_manager_id"
        const val KEY_SELECTED_CATEGORY = "selected_category"

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
    private var talkPerformanceMonitoringListener: TalkPerformanceMonitoringListener? = null
    private var cacheManager: SaveInstanceCacheManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            cacheManager = SaveInstanceCacheManager(it, savedInstanceState?.getString(KEY_CACHE_MANAGER))
        }
        getDataFromArguments()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        observeReviewForm()
        observeButtonState()
        observeCategories()
        observeSubmitFormResult()
        return inflater.inflate(R.layout.fragment_talk_write, container, false)
    }

    override fun getComponent(): TalkWriteComponent {
        return DaggerTalkWriteComponent.builder().talkComponent(
                getComponent(TalkComponent::class.java))
                .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stopPreparePerfomancePageMonitoring()
        startNetworkRequestPerformanceMonitoring()
        initView()
        initRecycleView()
        initTnC()
        showLoading()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component.inject(this)
    }

    override fun onChipClicked(category: TalkWriteCategory) {
        TalkWriteTracking.eventClickChips(viewModel.getUserId(), viewModel.getProductId().toString(), category.categoryName, category.content)
        viewModel.toggleCategory(category)
    }

    override fun onClickGoToChat(): Boolean {
        TalkWriteTracking.eventClickAskSeller(viewModel.getUserId(), viewModel.getProductId().toString())
        return goToChat()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        talkPerformanceMonitoringListener = castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun stopPreparePerfomancePageMonitoring() {
        talkPerformanceMonitoringListener?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        talkPerformanceMonitoringListener?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        talkPerformanceMonitoringListener?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        talkPerformanceMonitoringListener?.startRenderPerformanceMonitoring()
        writeCategoryChips.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                talkPerformanceMonitoringListener?.stopRenderPerformanceMonitoring()
                talkPerformanceMonitoringListener?.stopPerformanceMonitoring()
                writeCategoryChips.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun castContextToTalkPerformanceMonitoringListener(context: Context): TalkPerformanceMonitoringListener? {
        return if(context is TalkPerformanceMonitoringListener) {
            context
        } else {
            null
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        cacheManager?.put(KEY_SELECTED_CATEGORY, viewModel.getSelectedCategory())
        outState.putString(KEY_CACHE_MANAGER, cacheManager?.id)
    }

    private fun initView() {
        writeTNC.setOnClickListener {
            goToTermsAndConditionsPage()
        }
        talkWriteButton.setOnClickListener {
            submitNewQuestion()
        }
        talkConnectionErrorRetryButton.setOnClickListener {
            viewModel.refresh()
            showLoading()
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

    private fun initTnC() {
        context?.let {
            writeTNC.apply {
                text = HtmlLinkHelper(it, getString(R.string.reply_header_tnc)).spannedString
                setCustomMovementMethod { goToTermsAndConditionsPage() }
            }
        }
    }

    private fun goToChat(): Boolean {
        val intent = RouteManager.getIntent(context, TOP_CHAT_APPLINK + viewModel.shopId)
        intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, getProductInfoToJson())
        startActivity(intent)
        return true
    }

    private fun goToReplyPage(questionId: Int) {
        val intent = RouteManager.getIntent(
                context,
                Uri.parse(UriUtil.buildUri(ApplinkConstInternalGlobal.TALK_REPLY, questionId.toString()))
                        .buildUpon()
                        .appendQueryParameter(TalkConstants.PARAM_PRODUCT_ID, viewModel.getProductId().toString())
                        .appendQueryParameter(TalkConstants.PARAM_SHOP_ID, viewModel.shopId)
                        .appendQueryParameter(TalkConstants.PARAM_SOURCE, TalkConstants.WRITING_SOURCE)
                        .build().toString()
        )
        startActivity(intent)
        activity?.finish()
    }

    private fun goToSettingsPage() {
        RouteManager.route(context, ApplinkConstInternalGlobal.GENERAL_SETTING)
    }

    private fun goToTermsAndConditionsPage() : Boolean {
        return RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${TalkConstants.TERMS_AND_CONDITIONS_PAGE_URL}")
    }

    private fun getDataFromArguments() {
        arguments?.let {
            viewModel.setProductId(it.getInt(TalkConstants.PARAM_PRODUCT_ID))
        }
    }

    private fun observeReviewForm() {
        viewModel.writeFormData.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> {
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    hideError()
                    onSuccessGetWriteData(it.data)
                }
                is Fail -> {
                    showError()
                }
            }
            hideLoading()
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
            updateFromCacheIfExist()
            adapter.setData(it)
            val selectedCategory = it.firstOrNull { category -> category.isSelected }
            if(selectedCategory == null) {
                writeCategoryDetails.hide()
                return@Observer
            }
            showCategoryDetails(selectedCategory.content)
        })
    }

    private fun observeSubmitFormResult() {
        viewModel.submitFormResult.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> {
                    TalkWriteTracking.eventClickSendButton(viewModel.getUserId(), viewModel.getProductId().toString(), viewModel.getSelectedCategory()?.categoryName.toString(), true)
                    goToReplyPage(it.data.discussionId.toIntOrZero())
                }
                is Fail -> {
                    TalkWriteTracking.eventClickSendButton(viewModel.getUserId(), viewModel.getProductId().toString(), viewModel.getSelectedCategory()?.categoryName.toString(), false,  it.throwable.message)
                    showErrorToaster(it.throwable.message ?: getString(R.string.write_submit_error))
                }
            }
        })
    }

    private fun onSuccessGetWriteData(discussionGetWritingForm: DiscussionGetWritingForm) {
        with(discussionGetWritingForm) {
            setCharLimits(maxChar, minChar)
            setProductCard(productName, productThumbnailUrl)
        }
    }

    private fun updateFromCacheIfExist() {
        val talkWriteCategory: TalkWriteCategory? = cacheManager?.get(KEY_SELECTED_CATEGORY, TalkWriteCategory::class.java)
        talkWriteCategory?.let {
            viewModel.toggleCategory(talkWriteCategory)
        }
        cacheManager = null
    }

    private fun submitNewQuestion() {
        viewModel.submitForm(writeQuestionTextArea.textAreaInput.text.toString())
    }

    private fun setCharLimits(maxChar: Int, minChar: Int) {
        writeQuestionTextArea.apply {
            textAreaCounter = maxChar
            textAreaInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        viewModel.updateIsTextNotEmpty(it.isNotEmpty() && it.length >= minChar)
                        isError = s.length == maxChar
                        textAreaMessage = if(isError) {
                            getString(R.string.write_question_error_message)
                        } else {
                            ""
                        }
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // No op
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // No op
                }
            })
            textAreaInput.setOnFocusChangeListener { v, hasFocus ->
                if(!hasFocus) {
                    val editText = v as? EditText
                    editText?.let {
                        if(it.text.isNotEmpty() && it.text.length < minChar) {
                            isError = true
                            textAreaMessage = getString(R.string.write_question_length_minimum_error)
                        }
                    }
                } else {
                    isError = false
                    textAreaMessage = ""
                }
            }
        }
    }

    private fun setProductCard(productName: String, productUrl: String) {
        talkWriteProductName.text = productName
        talkWriteProductImage.loadImage(productUrl)
    }

    private fun showErrorToaster(message: String) {
        view?.let { Toaster.build(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.talk_ok)).show()}
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
            writeCategoryDetails.hide()
            return
        }
        writeCategoryDetails.apply {
            show()
            setContent(content, this@TalkWriteFragment)
        }
    }

    private fun getProductInfoToJson(): String {
        (viewModel.writeFormData.value as? Success)?.let {
            val productInfo = listOf(ProductPreview(id = viewModel.getProductId().toString(), name = it.data.productName, imageUrl = it.data.productThumbnailUrl, price = it.data.productPrice.toIntOrZero().getCurrencyFormatted()))
            val gson = Gson()
            return gson.toJson(productInfo, productInfo.javaClass)
        }
        return ""
    }
}