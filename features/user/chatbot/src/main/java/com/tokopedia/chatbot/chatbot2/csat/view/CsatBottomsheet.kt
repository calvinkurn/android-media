package com.tokopedia.chatbot.chatbot2.csat.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.csat.di.CsatComponent
import com.tokopedia.chatbot.chatbot2.csat.di.DaggerCsatComponent
import com.tokopedia.chatbot.chatbot2.csat.domain.model.CsatModel
import com.tokopedia.chatbot.chatbot2.csat.domain.model.PointModel
import com.tokopedia.chatbot.chatbot2.csat.view.CsatActivity.Companion.EXTRA_CSAT_DATA
import com.tokopedia.chatbot.chatbot2.csat.view.CsatActivity.Companion.EXTRA_CSAT_SELECTED_SCORE
import com.tokopedia.chatbot.databinding.BottomsheetCsatBinding
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import com.tokopedia.csat_rating.R as csat_ratingR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR


class CsatBottomsheet :
    BottomSheetUnify(),
    HasComponent<CsatComponent> {

    @Inject
    lateinit var viewModel: CsatViewModel

    private var viewBinding by autoClearedNullable<BottomsheetCsatBinding>()

    private val inactiveEmojiDrawables = listOf(
        csat_ratingR.drawable.emoji_inactive_1,
        csat_ratingR.drawable.emoji_inactive_2,
        csat_ratingR.drawable.emoji_inactive_3,
        csat_ratingR.drawable.emoji_inactive_4,
        csat_ratingR.drawable.emoji_inactive_5
    )

    private val activeEmojiDrawables = listOf(
        csat_ratingR.drawable.emoji_active_1,
        csat_ratingR.drawable.emoji_active_2,
        csat_ratingR.drawable.emoji_active_3,
        csat_ratingR.drawable.emoji_active_4,
        csat_ratingR.drawable.emoji_active_5
    )

    private val captionColors = listOf(
        unifyprinciplesR.color.Unify_RN500,
        unifyprinciplesR.color.Unify_YN500,
        unifyprinciplesR.color.Unify_YN300,
        unifyprinciplesR.color.Unify_GN500,
        unifyprinciplesR.color.Unify_GN500
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetStyle)
    }

    @SuppressLint("DeprecatedMethod")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initializeViewBinding()
        initializeBottomSheet()
        initializeCsatData()
        initializeObserver()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initializeViewBinding() {
        viewBinding = BottomsheetCsatBinding.inflate(LayoutInflater.from(context))
    }

    private fun initializeCsatData() {
        val selectedScore: Int? = arguments?.getInt(EXTRA_CSAT_SELECTED_SCORE)
        val csatModel: CsatModel? = arguments?.getParcelable(EXTRA_CSAT_DATA)
        if (selectedScore != null && csatModel != null) {
            viewModel.initializeData(selectedScore, csatModel)
        } else {
            activity?.finish()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewBinding = null
        activity?.finish()
        super.onDismiss(dialog)
    }

    private fun initializeBottomSheet() {
        isDragable = false
        isHideable = false
        showCloseIcon = false
        showHeader = false
        clearContentPadding = true
        showKnob = false
        isFullpage = true
        setChild(viewBinding?.root)
    }

    private fun initializeObserver() {
        observeDataState()
        observeEvent()
    }

    private fun observeDataState() {
        lifecycleScope.launchWhenStarted {
            viewModel.csatDataStateFlow.collectLatest { csatModel ->
                context?.let { context ->
                    renderCsat(context, csatModel)
                    viewModel.updateButton()
                }
            }
        }
    }

    private fun observeEvent() {
        lifecycleScope.launchWhenStarted {
            viewModel.csatEventFlow.collect { csatEvent ->
                when (csatEvent) {
                    is CsatEvent.UpdateButton -> renderButtonState(csatEvent)
                    is CsatEvent.NavigateToSubmitCsat -> navigateToSubmitCsat(csatEvent)
                    CsatEvent.FallbackDismissBottomSheet -> activity?.finish()
                }
            }
        }
    }

    private fun navigateToSubmitCsat(csatEvent: CsatEvent.NavigateToSubmitCsat) {
        activity?.let {
            val intentResult = Intent().apply {
                putExtra(CASE_ID, csatEvent.csatData.caseId)
                putExtra(CASE_CHAT_ID, csatEvent.csatData.caseChatId)
                putExtra(EMOJI_STATE, csatEvent.csatData.selectedPoint.score)
                putExtra(SERVICE, csatEvent.csatData.service)
                putExtra(OTHER_REASON, csatEvent.csatData.otherReason)
                val dynamicReasons = arrayListOf<String>().apply {
                    addAll(csatEvent.csatData.selectedReasons)
                }
                putStringArrayListExtra(DYNAMIC_REASON, dynamicReasons)
            }
            it.setResult(Activity.RESULT_OK, intentResult)
            it.finish()
        }
    }

    private fun renderCsat(context: Context, csatModel: CsatModel) {
        viewBinding?.csatTitle?.text = csatModel.title
        renderEmojis(context, csatModel)
        renderReasons(csatModel)
        renderOtherReason(csatModel)
    }

    private fun renderEmojis(context: Context, csatModel: CsatModel) {
        viewBinding?.csatCaption?.text = csatModel.selectedPoint.caption
        viewBinding?.csatCaption?.setTextColor(
            ContextCompat.getColor(
                context,
                captionColors.getItemSafe(csatModel.selectedPoint.score - 1)
            )
        )
        viewBinding?.csatEmojiContainer?.removeAllViews()

        csatModel.points.forEach { pointUiModel ->
            val emojiImageUnify =
                getEmojiLayout(context, pointUiModel, csatModel.selectedPoint.score)
            emojiImageUnify.setOnClickListener {
                viewModel.processAction(CsatUserAction.SelectScore(pointUiModel))
            }
            viewBinding?.csatEmojiContainer?.addView(emojiImageUnify)
        }
    }

    private fun getEmojiLayout(
        context: Context,
        point: PointModel,
        selectedScore: Int
    ): ImageUnify {
        val imageLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        imageLayoutParams.weight = 1.0f

        val imageUnify = ImageUnify(context)
        imageUnify.setImageResource(mapDrawable(point, selectedScore))
        imageUnify.scaleType = ImageView.ScaleType.FIT_CENTER
        imageUnify.layoutParams = imageLayoutParams

        return imageUnify
    }

    private fun mapDrawable(point: PointModel, selectedScore: Int): Int {
        return try {
            if (point.score > selectedScore) {
                inactiveEmojiDrawables.getItemSafe(point.score - 1)
            } else {
                activeEmojiDrawables.getItemSafe(selectedScore - 1)
            }
        } catch (exception: IndexOutOfBoundsException) {
            Timber.e(exception)
            inactiveEmojiDrawables[0]
        }
    }

    private fun renderReasons(csatModel: CsatModel) {
        viewBinding?.csatReasonTitle?.text = csatModel.selectedPoint.reasonTitle
        viewBinding?.csatReasonContainer?.removeAllViews()
        csatModel.selectedPoint.reasons.forEach { reason ->
            val reasonLayout = layoutInflater.inflate(R.layout.item_csat_reason, null)
            val reasonMessage = reasonLayout.findViewById<Typography>(R.id.csat_reason_title)
            reasonMessage.text = reason
            val reasonCheckBox = reasonLayout.findViewById<CheckboxUnify>(R.id.csat_reason_checkbox)
            reasonCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    viewModel.processAction(CsatUserAction.SelectReason(reason))
                } else {
                    viewModel.processAction(CsatUserAction.UnselectReason((reason)))
                }
                viewModel.updateButton()
            }
            reasonLayout.setOnClickListener {
                reasonCheckBox.isChecked = !reasonCheckBox.isChecked
            }
            viewBinding?.csatReasonContainer?.addView(reasonLayout)
        }
    }

    private fun renderOtherReason(csatModel: CsatModel) {
        viewBinding?.csatOtherReasonTitle?.text = csatModel.selectedPoint.otherReasonTitle
        viewBinding?.csatOtherReason?.apply {
            minLine = OTHER_REASON_MIN_LINE
            setLabel(context?.getString(R.string.chatbot_dynamic_csat_other_reason_label).orEmpty())
            setMessage(
                context?.getString(
                    R.string.chatbot_dynamic_csat_min_other_reason_char_label,
                    csatModel.minimumOtherReasonChar
                ).orEmpty()
            )
            editText.addTextChangedListener {
                viewModel.processAction(CsatUserAction.SetOtherReason(it.toString()))
                viewModel.updateButton()
            }
            editText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    delayScrollToBottom()
                }
            }
            editText.setOnClickListener {
                delayScrollToBottom()
            }
        }
    }

    private fun delayScrollToBottom() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                scrollToBottom()
            }
        }, SCROLL_DELAY)
    }

    private fun scrollToBottom() {
        viewBinding?.container?.smoothScrollTo(
            0,
            viewBinding?.container?.getChildAt(0)?.height.orZero()
        )
    }

    private fun renderButtonState(state: CsatEvent.UpdateButton) {
        viewBinding?.csatButtonSubmit?.isEnabled = state.isEnabled
        viewBinding?.csatButtonSubmit?.setOnClickListener {
            viewModel.processAction(CsatUserAction.SubmitCsat)
        }
    }

    override fun getComponent(): CsatComponent {
        return DaggerCsatComponent
            .builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    private fun List<Int>.getItemSafe(index: Int): Int {
        return if (index >= 0 && index < this.size) this[index] else this[0]
    }

    companion object {

        const val CASE_ID = "caseID"
        const val CASE_CHAT_ID = "caseChatID"
        const val EMOJI_STATE = "emoji_state"
        const val SERVICE = "service"
        const val OTHER_REASON = "other_reason"
        const val DYNAMIC_REASON = "dynamic_reason"

        const val OTHER_REASON_MIN_LINE = 4
        const val SCROLL_DELAY = 100L

        fun newInstance(selectedScore: Int, csatModel: CsatModel): CsatBottomsheet {
            val bottomSheet = CsatBottomsheet()
            val bundle = Bundle()
            bundle.putInt(EXTRA_CSAT_SELECTED_SCORE, selectedScore)
            bundle.putParcelable(EXTRA_CSAT_DATA, csatModel)
            bottomSheet.arguments = bundle
            return bottomSheet
        }
    }
}
