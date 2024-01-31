package com.tokopedia.chatbot.chatbot2.csat.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
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
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
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
        viewBinding = BottomsheetCsatBinding.inflate(LayoutInflater.from(context))
        initializeBottomSheet()

        val selectedScore: Int? = arguments?.getInt(EXTRA_CSAT_SELECTED_SCORE)
        val csatModel: CsatModel? = arguments?.getParcelable(EXTRA_CSAT_DATA)
        if (selectedScore != null && csatModel != null) {
            viewModel.initializeData(selectedScore, csatModel)
            observeViewModel()
        } else {
            activity?.finish()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewBinding = null
        activity?.finish()
        super.onDismiss(dialog)
    }

    private fun initializeBottomSheet() {
        isDragable = true
        isHideable = true
        showCloseIcon = false
        showHeader = false
        clearContentPadding = true
        showKnob = true
        isFullpage = true
        setChild(viewBinding?.root)
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.csatDataStateFlow.collectLatest { csatModel ->
                context?.let { context ->
                    renderCsat(context, csatModel)
                    viewModel.updateButton()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.csatEventFlow.collect {
                when (it) {
                    is CsatEvent.UpdateButton -> {
                        renderButtonState(it)
                    }

                    is CsatEvent.ShowError -> {
                    }

                    is CsatEvent.NavigateToPreviousPage -> {
                    }

                    CsatEvent.FallbackDismissBottomSheet -> activity?.finish()
                }
            }
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
                viewModel.processAction(CsatAction.SelectScore(pointUiModel))
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
                    viewModel.processAction(CsatAction.SelectReason(reason))
                } else {
                    viewModel.processAction(CsatAction.UnselectReason((reason)))
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
            setCounter(csatModel.maximumOtherReasonChar)
            minLine = 4
            setLabel("Tulis detailnya di sini, ya..")
            viewBinding?.csatOtherReason?.setMessage("Min. ${csatModel.minimumOtherReasonChar} karakter")
            viewBinding?.csatOtherReason?.editText?.addTextChangedListener {
                // todo : add debounce
                viewModel.processAction(CsatAction.SetOtherReason(it.toString()))
                viewModel.updateButton()
            }
        }
    }

    private fun renderButtonState(state: CsatEvent.UpdateButton) {
        viewBinding?.csatButtonSubmit?.isEnabled = state.isEnabled
        viewBinding?.csatButtonSubmit?.setOnClickListener {
            viewModel.processAction(CsatAction.SendCsat)
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
