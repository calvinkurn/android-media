package com.tokopedia.talk.feature.write.presentation.fragment

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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.talk.R
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringContract
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringListener
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.common.utils.setCustomMovementMethod
import com.tokopedia.talk.databinding.FragmentTalkWriteBinding
import com.tokopedia.talk.feature.write.analytics.TalkWriteTracking
import com.tokopedia.talk.feature.write.data.model.DiscussionGetWritingForm
import com.tokopedia.talk.feature.write.di.DaggerTalkWriteComponent
import com.tokopedia.talk.feature.write.di.TalkWriteComponent
import com.tokopedia.talk.feature.write.presentation.decorator.SpacingItemDecoration
import com.tokopedia.talk.feature.write.presentation.uimodel.TalkWriteCategory
import com.tokopedia.talk.feature.write.presentation.viewmodel.TalkWriteViewModel
import com.tokopedia.talk.feature.write.presentation.widget.TalkWriteCategoryChipsWidget
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

open class TalkWriteFragment : BaseDaggerFragment(),
    HasComponent<TalkWriteComponent>, TalkWriteCategoryChipsWidget.ChipClickListener,
    TalkPerformanceMonitoringContract {

    companion object {
        const val KEY_CACHE_MANAGER = "cache_manager_id"
        const val KEY_SELECTED_CATEGORY = "selected_category"

        @JvmStatic
        fun createNewInstance(
            productId: String,
            isVariantSelected: Boolean,
            availableVariants: String
        ): TalkWriteFragment {
            return TalkWriteFragment().apply {
                arguments = Bundle()
                arguments?.putString(TalkConstants.PARAM_PRODUCT_ID, productId)
                arguments?.putBoolean(
                    TalkConstants.PARAM_APPLINK_IS_VARIANT_SELECTED,
                    isVariantSelected
                )
                arguments?.putString(
                    TalkConstants.PARAM_APPLINK_AVAILABLE_VARIANT,
                    availableVariants
                )
            }
        }

    }

    @Inject
    lateinit var viewModel: TalkWriteViewModel

    private val adapter = TalkWriteCategoryChipsWidget(this)
    private var talkPerformanceMonitoringListener: TalkPerformanceMonitoringListener? = null
    private var cacheManager: SaveInstanceCacheManager? = null

    private var binding by autoCleared<FragmentTalkWriteBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            cacheManager =
                SaveInstanceCacheManager(it, savedInstanceState?.getString(KEY_CACHE_MANAGER))
        }
        getDataFromArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        observeReviewForm()
        observeButtonState()
        observeCategories()
        observeSubmitFormResult()
        observeTextFieldState()
        binding = FragmentTalkWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getComponent(): TalkWriteComponent {
        return DaggerTalkWriteComponent.builder()
            .talkComponent(getComponent(TalkComponent::class.java)).build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        stopPreparePerfomancePageMonitoring()
        startNetworkRequestPerformanceMonitoring()
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
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
        TalkWriteTracking.eventClickChips(
            viewModel.getUserId(),
            viewModel.getProductId().toString(),
            category.categoryName,
            category.content,
            viewModel.isVariantSelected,
            viewModel.availableVariants
        )
        viewModel.toggleCategory(category)
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
        binding.writeCategoryChips.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                talkPerformanceMonitoringListener?.stopRenderPerformanceMonitoring()
                talkPerformanceMonitoringListener?.stopPerformanceMonitoring()
                binding.writeCategoryChips.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun castContextToTalkPerformanceMonitoringListener(context: Context): TalkPerformanceMonitoringListener? {
        return if (context is TalkPerformanceMonitoringListener) {
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

    override fun onDestroy() {
        removeObservers(viewModel.writeFormData)
        removeObservers(viewModel.buttonState)
        removeObservers(viewModel.textFieldState)
        removeObservers(viewModel.categoryChips)
        removeObservers(viewModel.submitFormResult)
        super.onDestroy()
    }

    private fun initView() {
        binding.writeTNC.setOnClickListener {
            goToTermsAndConditionsPage()
        }
        binding.talkWriteButton.setOnClickListener {
            submitNewQuestion()
        }
        binding.talkWriteError.talkConnectionErrorRetryButton.setOnClickListener {
            binding.talkWriteError.readingImageError.loadImage(com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)
            viewModel.refresh()
            showLoading()
        }
    }

    private fun initRecycleView() {
        context?.resources?.getDimensionPixelOffset(com.tokopedia.unifycomponents.R.dimen.layout_lvl1)
            ?.let { binding.writeCategoryChips.addItemDecoration(SpacingItemDecoration(it)) }
        binding.writeCategoryChips.apply {
            adapter = this@TalkWriteFragment.adapter
            layoutManager = ChipsLayoutManager.newBuilder(context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
        }
    }

    private fun initTnC() {
        context?.let {
            binding.writeTNC.apply {
                text = HtmlLinkHelper(it, getString(R.string.reply_header_tnc)).spannedString
                setCustomMovementMethod { goToTermsAndConditionsPage() }
            }
        }
    }

    private fun goToReplyPage(questionId: String) {
        val intent = RouteManager.getIntent(
            context,
            Uri.parse(
                UriUtil.buildUri(
                    ApplinkConstInternalGlobal.TALK_REPLY,
                    questionId
                )
            )
                .buildUpon()
                .appendQueryParameter(TalkConstants.PARAM_PRODUCT_ID, viewModel.getProductId())
                .appendQueryParameter(TalkConstants.PARAM_SHOP_ID, viewModel.shopId)
                .appendQueryParameter(TalkConstants.PARAM_SOURCE, TalkConstants.WRITING_SOURCE)
                .build().toString()
        )
        startActivity(intent)
        activity?.finish()
    }

    private fun goToTermsAndConditionsPage(): Boolean {
        return RouteManager.route(
            activity,
            "${ApplinkConst.WEBVIEW}?url=${TalkConstants.TERMS_AND_CONDITIONS_PAGE_URL}"
        )
    }

    private fun getDataFromArguments() {
        arguments?.let {
            viewModel.setProductId(it.getString(TalkConstants.PARAM_PRODUCT_ID) ?: "")
            viewModel.isVariantSelected =
                it.getBoolean(TalkConstants.PARAM_APPLINK_IS_VARIANT_SELECTED)
            viewModel.availableVariants =
                it.getString(TalkConstants.PARAM_APPLINK_AVAILABLE_VARIANT, "0")
        }
    }

    private fun observeReviewForm() {
        viewModel.writeFormData.observe(viewLifecycleOwner, {
            when (it) {
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
        viewModel.buttonState.observe(viewLifecycleOwner, {
            binding.talkWriteButton.isEnabled = it.isAnyCategorySelected && it.isNotTextEmpty
        })
    }

    private fun observeTextFieldState() {
        viewModel.textFieldState.observe(viewLifecycleOwner, {
            if (it) {
                binding.writeQuestionTextArea.show()
            } else {
                binding.writeQuestionTextArea.hide()
            }
        })
    }

    private fun observeCategories() {
        viewModel.categoryChips.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                return@Observer
            }
            updateFromCacheIfExist()
            adapter.setData(it)
            val selectedCategory = it.firstOrNull { category -> category.isSelected }
            if (selectedCategory == null) {
                binding.writeCategoryDetails.hide()
                return@Observer
            }
            showCategoryDetails(selectedCategory.content)
        })
    }

    private fun observeSubmitFormResult() {
        viewModel.submitFormResult.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    TalkWriteTracking.eventClickSendButton(
                        userId = viewModel.getUserId(),
                        productId = viewModel.getProductId().toString(),
                        category = viewModel.getSelectedCategory()?.categoryName.toString(),
                        isSuccess = true,
                        isVariantSelected = viewModel.isVariantSelected,
                        availableVariants = viewModel.availableVariants
                    )
                    goToReplyPage(it.data.discussionId)
                }
                is Fail -> {
                    TalkWriteTracking.eventClickSendButton(
                        viewModel.getUserId(),
                        viewModel.getProductId().toString(),
                        viewModel.getSelectedCategory()?.categoryName.toString(),
                        false,
                        it.throwable.message,
                        viewModel.isVariantSelected,
                        viewModel.availableVariants
                    )
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
        val talkWriteCategory: TalkWriteCategory? =
            cacheManager?.get(KEY_SELECTED_CATEGORY, TalkWriteCategory::class.java)
        talkWriteCategory?.let {
            viewModel.toggleCategory(talkWriteCategory)
        }
        cacheManager = null
    }

    private fun submitNewQuestion() {
        viewModel.submitForm(binding.writeQuestionTextArea.textAreaInput.text.toString())
    }

    private fun setCharLimits(maxChar: Int, minChar: Int) {
        binding.writeQuestionTextArea.apply {
            textAreaCounter = maxChar
            textAreaInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        viewModel.updateIsTextNotEmpty(it.isNotEmpty() && it.length >= minChar)
                        isError = s.length == maxChar
                        textAreaMessage = if (isError) {
                            getString(R.string.write_question_error_message)
                        } else {
                            ""
                        }
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // No op
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // No op
                }
            })
            textAreaInput.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    val editText = v as? EditText
                    editText?.let {
                        if (it.text.isNotEmpty() && it.text.length < minChar) {
                            isError = true
                            textAreaMessage =
                                getString(R.string.write_question_length_minimum_error)
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
        binding.talkWriteHeader.bind(productName, productUrl)
    }

    private fun showErrorToaster(message: String) {
        view?.let {
            Toaster.build(
                it,
                message,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(R.string.talk_ok)
            ).show()
        }
    }

    private fun showError() {
        binding.talkWriteError.root.show()
    }

    private fun showLoading() {
        binding.talkWriteLoading.root.show()
    }

    private fun hideError() {
        binding.talkWriteError.root.hide()
    }

    private fun hideLoading() {
        binding.talkWriteLoading.root.hide()
    }

    private fun showCategoryDetails(content: String?) {
        if (content.isNullOrEmpty()) {
            binding.writeCategoryDetails.hide()
            return
        }
        binding.writeCategoryDetails.apply {
            show()
            setContent(content)
        }
    }

    private fun initToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                supportActionBar?.hide()
                setSupportActionBar(binding.headerTalkWrite)
                binding.headerTalkWrite.title = getString(R.string.title_write_page)
            }
        }
    }
}
