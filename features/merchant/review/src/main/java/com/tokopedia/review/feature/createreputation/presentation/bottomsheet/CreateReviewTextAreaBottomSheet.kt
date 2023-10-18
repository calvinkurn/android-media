package com.tokopedia.review.feature.createreputation.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.review.R
import com.tokopedia.review.common.extension.collectLatestWhenResumed
import com.tokopedia.review.databinding.BottomsheetCreateReviewTextAreaBinding
import com.tokopedia.review.feature.createreputation.di.CreateReviewDaggerInstance
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewTextAreaTextUiModel
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTextAreaTitleUiState
import com.tokopedia.review.feature.createreputation.presentation.viewmodel.CreateReviewViewModel
import com.tokopedia.review.feature.createreputation.presentation.widget.BaseReviewCustomView
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewTemplate
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewTextArea
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filterIsInstance
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CreateReviewTextAreaBottomSheet : BottomSheetUnify(), CoroutineScope {

    companion object {
        private const val TEXT_AREA_MAX_MIN_LINE = 4
        const val TAG = "CreateReviewTextAreaBottomSheet"
    }

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + SupervisorJob()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var trackingQueue: TrackingQueue

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    private var binding by viewBinding(BottomsheetCreateReviewTextAreaBinding::bind)

    private val daggerHandler = DaggerHandler()
    private val uiStateHandler = UiStateHandler()
    private val reviewTemplateListener = ReviewTemplateListener()
    private val textAreaListener = TextAreaListener()
    private val baseCreateReviewCustomViewListener = BaseCreateReviewCustomViewListener()
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity(), viewModelFactory).get(CreateReviewViewModel::class.java)
    }

    init {
        showCloseIcon = false
        isFullpage = true
        clearContentPadding = true
        setShowListener {
            viewModel.updateTextAreaHasFocus(hasFocus = true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        daggerHandler.initInjector()
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetCreateReviewTextAreaBinding.inflate(inflater).also {
            it.textAreaExpandedCreateReviewBottomSheet.setMinLine(TEXT_AREA_MAX_MIN_LINE)
            it.textAreaExpandedCreateReviewBottomSheet.setMaxLine(Int.MAX_VALUE)
            setChild(it.root)
        }
        uiStateHandler.initUiStateCollectors()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        binding?.reviewTemplateExpandedCreateReviewBottomSheet?.makeWrapContent()
        binding?.reviewTemplateExpandedCreateReviewBottomSheet?.setMargins(
            bottom = 8.toPx()
        )
        binding?.textAreaExpandedCreateReviewBottomSheet?.removeBorder()
    }

    private fun setupListeners() {
        baseCreateReviewCustomViewListener.attachListener()
        textAreaListener.attachListener()
        reviewTemplateListener.attachListener()
    }

    private fun setupBottomSheet() {
        context?.let { context ->
            setAction(ContextCompat.getDrawable(context, R.drawable.ic_collapse)) {
                viewModel.updateTextAreaHasFocus(hasFocus = false)
                viewModel.dismissTextAreaBottomSheet()
            }
        }
    }

    private inner class DaggerHandler {
        fun initInjector() {
            context?.let {
                CreateReviewDaggerInstance
                    .getInstance(it)
                    .inject(this@CreateReviewTextAreaBottomSheet)
            }
        }
    }

    private inner class UiStateHandler {
        private fun collectTextAreaUiState() {
            collectLatestWhenResumed(viewModel.textAreaUiState) {
                binding?.textAreaExpandedCreateReviewBottomSheet?.updateUi(
                    uiState = it,
                    source = CreateReviewTextAreaTextUiModel.Source.CREATE_REVIEW_EXPANDED_TEXT_AREA
                )
                binding?.helperTextAreaExpandedCreateReviewBottomSheet?.updateUi(it)
            }
        }

        private fun collectReviewTemplateUiState() {
            collectLatestWhenResumed(viewModel.templateUiState) {
                binding?.reviewTemplateExpandedCreateReviewBottomSheet?.updateUi(it)
            }
        }

        private fun collectTopicsUiState() {
            collectLatestWhenResumed(viewModel.topicsUiState) {
                binding?.topicsExpandedCreateReviewBottomSheet?.updateUI(it)
            }
        }

        private fun collectTextAreaTitleUiState() {
            collectLatestWhenResumed(
                viewModel.textAreaTitleUiState.filterIsInstance<CreateReviewTextAreaTitleUiState.Showing>()
            ) {
                context?.let { context -> setTitle(it.textRes.getStringValue(context)) }
            }
        }

        fun initUiStateCollectors() {
            collectTextAreaUiState()
            collectReviewTemplateUiState()
            collectTopicsUiState()
            collectTextAreaTitleUiState()
        }
    }

    private inner class TextAreaListener: CreateReviewTextArea.Listener {
        override fun onTextChanged(text: String, source: CreateReviewTextAreaTextUiModel.Source) {
            viewModel.setReviewText(text, source)
        }

        override fun onFocusChanged(hasFocus: Boolean) {
            viewModel.updateTextAreaHasFocus(hasFocus)
        }

        override fun onExpandButtonClicked() {
            // noop
        }

        fun attachListener() {
            binding?.textAreaExpandedCreateReviewBottomSheet?.setListener(this)
        }
    }

    private inner class ReviewTemplateListener: CreateReviewTemplate.Listener {
        override fun onTemplateSelected(template: com.tokopedia.review.feature.createreputation.model.CreateReviewTemplate) {
            viewModel.selectTemplate(template)
        }

        fun attachListener() {
            binding?.reviewTemplateExpandedCreateReviewBottomSheet?.setListener(this)
        }
    }

    private inner class BaseCreateReviewCustomViewListener: BaseReviewCustomView.Listener {
        override fun onRequestClearTextAreaFocus() {
            viewModel.updateTextAreaHasFocus(hasFocus = false)
        }

        override fun onRequestTextAreaFocus() {
            viewModel.updateTextAreaHasFocus(hasFocus = true)
        }

        fun attachListener() {
            binding?.textAreaExpandedCreateReviewBottomSheet?.setBaseCustomViewListener(this)
            binding?.reviewTemplateExpandedCreateReviewBottomSheet?.setBaseCustomViewListener(this)
            binding?.helperTextAreaExpandedCreateReviewBottomSheet?.setBaseCustomViewListener(this)
        }
    }
}
