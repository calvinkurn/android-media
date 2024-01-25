package com.tokopedia.chatbot.chatbot2.csat.view

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.csat.di.CsatComponent
import com.tokopedia.chatbot.chatbot2.csat.di.DaggerCsatComponent
import com.tokopedia.chatbot.chatbot2.csat.domain.model.CsatModel
import com.tokopedia.chatbot.chatbot2.csat.domain.model.PointModel
import com.tokopedia.chatbot.chatbot2.csat.domain.model.dummyData
import com.tokopedia.chatbot.databinding.BottomsheetCsatBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.csat_rating.R as csat_ratingR

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
                }
            }
        }
    }

    private fun renderCsat(context: Context, csatModel: CsatModel) {
        viewBinding?.csatTitle?.text = csatModel.title
        renderEmojis(context, csatModel)
        if (csatModel.points.isNotEmpty()) {
            renderReasons(csatModel)
            renderOtherReason(csatModel)
        }
    }

    private fun renderEmojis(context: Context, csatModel: CsatModel) {
        viewBinding?.csatCaption?.text = csatModel.selectedPoint.caption
        viewBinding?.csatEmoji?.removeAllViews()

        csatModel.points.forEach { pointUiModel ->
            val emojiImageUnify = getEmojiImageUnify(context, pointUiModel, csatModel.selectedPoint.score)
            emojiImageUnify.setOnClickListener {
                viewModel.setSelectedScore(pointUiModel)
            }
            viewBinding?.csatEmoji?.addView(emojiImageUnify)
        }
    }

    private fun getEmojiImageUnify(
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
        viewBinding?.csatRvReasons
    }

    private fun renderOtherReason(csatModel: CsatModel) {
        viewBinding?.csatOtherReasonTitle?.text = csatModel.selectedPoint.otherReasonTitle
        viewBinding?.csatOtherReason?.setMessage(csatModel.otherReason)
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
