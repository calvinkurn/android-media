package com.tokopedia.chatbot.chatbot2.csat.view

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
import com.tokopedia.chatbot.chatbot2.csat.domain.model.SubmitButtonState
import com.tokopedia.chatbot.chatbot2.csat.domain.model.dummyData
import com.tokopedia.chatbot.databinding.BottomsheetCsatBinding
import com.tokopedia.kotlin.extensions.view.toIntOrZero
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
        csat_ratingR.drawable.emoji_inactive_5,
    )

    private val activeEmojiDrawables = listOf(
        csat_ratingR.drawable.emoji_active_1,
        csat_ratingR.drawable.emoji_active_2,
        csat_ratingR.drawable.emoji_active_3,
        csat_ratingR.drawable.emoji_active_4,
        csat_ratingR.drawable.emoji_active_5,
    )

    private val captionColors = listOf(
        unifyprinciplesR.color.Unify_RN500,
        unifyprinciplesR.color.Unify_YN500,
        unifyprinciplesR.color.Unify_YN300,
        unifyprinciplesR.color.Unify_GN500,
        unifyprinciplesR.color.Unify_GN500,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = BottomsheetCsatBinding.inflate(LayoutInflater.from(context))
        initializeBottomSheet()
        viewModel.initializeData(dummyData)
        observeViewModel()
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
            viewModel.submitButtonStateFlow.collectLatest {
                renderButtonState(it)
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
                captionColors[csatModel.selectedPoint.score - 1]
            )
        )
        viewBinding?.csatEmojiContainer?.removeAllViews()

        csatModel.points.forEach { pointUiModel ->
            val emojiImageUnify =
                getEmojiLayout(context, pointUiModel, csatModel.selectedPoint.score)
            emojiImageUnify.setOnClickListener {
                viewModel.setSelectedScore(pointUiModel)
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
                inactiveEmojiDrawables[point.score - 1]
            } else {
                activeEmojiDrawables[selectedScore - 1]
            }
        } catch (exception: IndexOutOfBoundsException) {
            Timber.e(exception)
            inactiveEmojiDrawables[0]
        }
    }

    private fun renderReasons(csatModel: CsatModel) {
        viewBinding?.csatReasonTitle?.text = csatModel.title
        viewBinding?.csatReasonContainer?.removeAllViews()
        csatModel.selectedPoint.reasons.forEach { reason ->
            val reasonLayout = layoutInflater.inflate(R.layout.item_csat_reason, null)
            reasonLayout.findViewById<Typography>(R.id.csat_reason_title).text = reason
            reasonLayout.findViewById<CheckboxUnify>(R.id.csat_reason_checkbox)
                .setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        viewModel.selectSelectedReason(reason)
                    } else {
                        viewModel.unselectSelectedReason(reason)
                    }
                    viewModel.updateButton()
                }
            viewBinding?.csatReasonContainer?.addView(reasonLayout)
        }
    }

    private fun renderOtherReason(csatModel: CsatModel) {
        viewBinding?.csatOtherReasonTitle?.text = csatModel.selectedPoint.otherReasonTitle
        viewBinding?.csatOtherReason?.setCounter(csatModel.selectedPoint.maximumOtherReasonChar)
        viewBinding?.csatOtherReason?.minLine = 4
        viewBinding?.csatOtherReason?.setLabel("Tulis detailnya di sini, ya..")
        viewBinding?.csatOtherReason?.setMessage("Min. ${csatModel.selectedPoint.minimumOtherReasonChar} karakter")
        viewBinding?.csatOtherReason?.editText?.addTextChangedListener {
            viewModel.setOtherReason(it.toString())
            viewModel.updateButton()
        }
    }

    private fun renderButtonState(state: SubmitButtonState) {
        viewBinding?.csatButtonSubmit?.isEnabled = state.isEnabled
    }

    override fun getComponent(): CsatComponent {
        return DaggerCsatComponent
            .builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    companion object {
        fun newInstance(): CsatBottomsheet {
            val bottomSheet = CsatBottomsheet()
            val bundle = Bundle()
            // ... extra
            bottomSheet.arguments = bundle
            return bottomSheet
        }
    }

}
