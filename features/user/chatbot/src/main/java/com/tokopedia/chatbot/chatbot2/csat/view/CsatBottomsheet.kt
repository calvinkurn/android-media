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
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.csat.di.CsatComponent
import com.tokopedia.chatbot.chatbot2.csat.di.DaggerCsatComponent
import com.tokopedia.chatbot.chatbot2.csat.view.model.CsatUiModel
import com.tokopedia.chatbot.chatbot2.csat.view.model.PointUiModel
import com.tokopedia.chatbot.chatbot2.csat.view.model.dummyData
import com.tokopedia.chatbot.databinding.BottomsheetCsatBinding
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.csat_rating.R as csat_ratingR

class CsatBottomsheet :
    BottomSheetUnify(),
    HasComponent<CsatComponent> {

    @Inject
    lateinit var userSession: UserSessionInterface

    private var viewBinding: BottomsheetCsatBinding? = null

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
        observeData()
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
        customPeekHeight = (getScreenHeight() / 2).toDp()
        setChild(viewBinding?.root)
    }

    private fun observeData() {
        // Todo : observe from view model
        context?.let {
            renderCsat(it, dummyData)
        }
    }

    private fun renderCsat(context: Context, data: CsatUiModel) {
        viewBinding?.csatTitle?.text = data.title
        renderEmojis(context, data)
        if (data.points.isNotEmpty()) {
            renderReasons(data.points.first())
            renderOtherReason(data.points.first())
        }
    }

    private fun renderEmojis(context: Context, csatUiModel: CsatUiModel) {
        viewBinding?.csatCaption?.text = ""
        viewBinding?.csatCaption?.invisible()

        viewBinding?.csatEmoji?.removeAllViews()

        val selectedScore = csatUiModel.points.firstOrNull { it.isSelected }?.score ?: 0
        csatUiModel.points.forEach { pointUiModel ->
            val emojiImageUnify = getEmojiImageUnify(context, pointUiModel, selectedScore)
            emojiImageUnify.setOnClickListener {
                // viewModel.setSelectedPoint ... then re render
            }

            viewBinding?.csatEmoji?.addView(emojiImageUnify)
            if (pointUiModel.isSelected) {
                viewBinding?.csatCaption?.text = pointUiModel.caption
                viewBinding?.csatCaption?.show()
            }
        }
    }

    private fun getEmojiImageUnify(
        context: Context,
        point: PointUiModel,
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

    private fun mapDrawable(point: PointUiModel, selectedScore: Int): Int {
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

    private fun renderReasons(data: PointUiModel) {
        viewBinding?.csatReasonTitle?.text = data.reasonTitle
        viewBinding?.csatRvReasons
    }

    private fun renderOtherReason(data: PointUiModel) {
        viewBinding?.csatOtherReasonTitle?.text = data.otherReasonTitle
        viewBinding?.csatOtherReason?.setMessage(data.otherReason)
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
