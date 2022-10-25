package com.tokopedia.tokochat_common.view.customview

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.TokochatItemOrderStatusShimmerBinding
import com.tokopedia.tokochat_common.databinding.TokochatPartialOrderStatusWidgetBinding
import com.tokopedia.tokochat_common.databinding.TokochatTransactionWidgetBinding
import com.tokopedia.tokochat_common.util.CommonUtil
import com.tokopedia.tokochat_common.view.uimodel.TokoChatOrderProgressUiModel
import java.lang.StringBuilder

class TokoChatTransactionOrderWidget : LinearLayout {

    private var tokoChatOrderProgressUiModel: TokoChatOrderProgressUiModel? = null
    private var state: State? = State()

    private var listener: Listener? = null

    private var binding: TokochatTransactionWidgetBinding? = null

    private var partialOrderStatusWidgetBinding: TokochatPartialOrderStatusWidgetBinding? = null

    private var shimmerOrderStatusWidgetBinding: TokochatItemOrderStatusShimmerBinding? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        partialOrderStatusWidgetBinding = null
        shimmerOrderStatusWidgetBinding = null
        binding = null
    }

    init {
        setupViewBinding()
    }

    fun showShimmeringWidget() {
        binding?.tokochatLocalloadErrorTransactionWidget?.hide()
        partialOrderStatusWidgetBinding?.root?.hide()
        setupShimmeringOrderStatusBinding()
    }

    fun showTransactionWidget(
        listener: Listener?,
        orderProgressUiModel: TokoChatOrderProgressUiModel?
    ) {
        assignFields(listener, orderProgressUiModel)
        binding?.tokochatLocalloadErrorTransactionWidget?.hide()
        shimmerOrderStatusWidgetBinding?.root?.hide()
        partialOrderStatusWidgetBinding?.root?.show()
        setupPartialOrderStatusBinding()
        render()
    }

    fun showLocalLoadTransaction() {
        partialOrderStatusWidgetBinding?.root?.hide()
        shimmerOrderStatusWidgetBinding?.root?.hide()
        binding?.tokochatLocalloadErrorTransactionWidget?.show()
        setupLocalLoadTransaction()
    }

    private fun setupLocalLoadTransaction() {
        binding?.tokochatLocalloadErrorTransactionWidget?.run {
            progressState = false
            refreshBtn?.setOnClickListener {
                progressState = true
                listener?.onLocalLoadRetryClicked()
            }
        }
    }

    private fun render() {
        loadPreviousState()
        renderLayoutVisibility()
        if (tokoChatOrderProgressUiModel?.isEnable == false) return
        renderOrderNameContainer()
        renderBackgroundColor()
        renderStateDescription()
        renderStateChangerButton()
        renderOrderStatus()
        renderHasBeenSeen()
        saveCurrentState()
    }

    private fun assignFields(
        listener: Listener?,
        orderProgressUiModel: TokoChatOrderProgressUiModel?
    ) {
        this.tokoChatOrderProgressUiModel = orderProgressUiModel
        this.listener = listener
    }

    private fun setupViewBinding() {
        binding = TokochatTransactionWidgetBinding.inflate(LayoutInflater.from(context), this)
    }

    private fun setupPartialOrderStatusBinding() {
        binding?.run {
            val partialOrderStatusVs =
                root.findViewById<View>(R.id.tokochat_partial_order_status_viewstub)
            if (partialOrderStatusVs is ViewStub) {
                partialOrderStatusVs.inflate()
                bindPartialOrderStatusWidget(root)
            } else {
                partialOrderStatusVs.show()
            }
        }
    }

    private fun setupShimmeringOrderStatusBinding() {
        binding?.run {
            val shimmerOrderStatusVs =
                root.findViewById<View>(R.id.tokochat_shimmer_order_status_viewstub)
            if (shimmerOrderStatusVs is ViewStub) {
                shimmerOrderStatusVs.inflate()
                bindShimmerOrderStatusWidget(root)
            } else {
                shimmerOrderStatusVs.show()
            }
        }
    }

    private fun bindPartialOrderStatusWidget(root: View) {
        if (partialOrderStatusWidgetBinding == null) {
            partialOrderStatusWidgetBinding =
                TokochatPartialOrderStatusWidgetBinding.bind(root.findViewById(R.id.tokochat_partial_order_status_viewstub))
        }
    }

    private fun bindShimmerOrderStatusWidget(root: View) {
        if (shimmerOrderStatusWidgetBinding == null) {
            shimmerOrderStatusWidgetBinding =
                TokochatItemOrderStatusShimmerBinding.bind(root.findViewById(R.id.tokochat_shimmer_order_status_viewstub))
        }
    }

    private fun renderStateDescription() {
        doWhenState(
            isOpen = { showContainerDescription() },
            isClose = { hideContainerDescription() }
        )
    }

    private fun showContainerDescription() {
        partialOrderStatusWidgetBinding?.tokochatClOrderNameContainer?.show()
    }

    private fun hideContainerDescription() {
        partialOrderStatusWidgetBinding?.tokochatClOrderNameContainer?.hide()
    }

    private fun loadPreviousState() {
        try {
            val stateJsonString = getPref()?.getString(getPrefOrderPrefKey(), DEFAULT_STATE)
            state = CommonUtil.fromJson(stateJsonString, State::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun renderOrderNameContainer() {
        doWhenState(
            isOpen = { showOrderNameContainer() },
            isClose = { hideOrderNameContainer() }
        )
    }

    /**
     * Check if the implementation is one of the 2:
     * 1. if buka text, will show order name container
     * 2. if tutup text, will closed order name container
     */
    private fun renderStateChangerButton() {
        if (state?.bodyVisibility == State.OPEN) {
            bindClickOpenCloseState()
            bindClickCardContainer()
            showOrderNameContainer()
            renderOpenCloseText()
            renderImageThumbnail()
            renderProductName()
            renderEstimation()
        } else {
            hideOrderNameContainer()
        }
    }

    private fun renderOrderStatus() {
        partialOrderStatusWidgetBinding?.tokochatTpOrderStatus?.text =
            tokoChatOrderProgressUiModel?.status
    }

    private fun renderLayoutVisibility() {
        showWithCondition(tokoChatOrderProgressUiModel?.isEnable == true)
    }

    private fun renderBackgroundColor() {
        val colorRes = if (state?.hasSeen == false) {
            State.COLOR_NOT_SEEN
        } else {
            State.COLOR_SEEN
        }
        val color = MethodChecker.getColor(context, colorRes)
        partialOrderStatusWidgetBinding?.root?.setBackgroundColor(color)
    }

    private fun renderHasBeenSeen() {
        if (state?.hasSeen == false) {
            renderFirstTimeSeen()
        }
    }

    private fun saveCurrentState() {
        val stateJsonString = CommonUtil.toJson(state)
        getPref()?.edit()?.putString(getPrefOrderPrefKey(), stateJsonString)?.apply()
    }

    private fun bindClickOpenCloseState() {
        val clickListener = OnClickListener {
            doWhenState(
                isOpen = { changeState(State.CLOSE) },
                isClose = { changeState(State.OPEN) }
            )
        }
        partialOrderStatusWidgetBinding?.tokochatTpOrderVisibility?.setOnClickListener(clickListener)
        partialOrderStatusWidgetBinding?.tokochatTpOrderStatus?.setOnClickListener(clickListener)
        partialOrderStatusWidgetBinding?.tokochatIcOrderVisibility?.setOnClickListener(clickListener)
    }

    private fun bindClickCardContainer() {
        partialOrderStatusWidgetBinding?.tokochatClOrderNameContainer?.setOnClickListener {
            tokoChatOrderProgressUiModel?.appLink?.let {
                if (it.isNotBlank()) {
                    listener?.onTransactionWidgetClicked(it)
                }
            }
        }
    }

    private fun renderImageThumbnail() {
        partialOrderStatusWidgetBinding?.tokochatIvOrderThumbnail?.loadImage(
            tokoChatOrderProgressUiModel?.imageUrl
        )
    }

    private fun renderProductName() {
        partialOrderStatusWidgetBinding?.tokochatTpOrderName?.text =
            MethodChecker.fromHtml(tokoChatOrderProgressUiModel?.name)
    }

    private fun renderEstimation() {
        if (tokoChatOrderProgressUiModel?.labelTitle?.isNotBlank() == true &&
            tokoChatOrderProgressUiModel?.labelValue?.isNotBlank() == true) {
            partialOrderStatusWidgetBinding?.tokochatTpEstimateLabel?.text =
                StringBuilder(tokoChatOrderProgressUiModel?.labelTitle.orEmpty()).append(":")
            partialOrderStatusWidgetBinding?.tokochatTpEstimateValue?.text =
                tokoChatOrderProgressUiModel?.labelValue.orEmpty()
        } else {
            partialOrderStatusWidgetBinding?.tokochatTpEstimateLabel?.hide()
            partialOrderStatusWidgetBinding?.tokochatTpEstimateValue?.hide()
        }
    }

    private fun renderFirstTimeSeen() {
        state?.seen()
    }

    private fun renderToggleStateChangerButton(desiredState: String?) {
        renderChangerButtonText(desiredState)
        renderChangerIconArrow(desiredState)
    }

    private fun renderChangerIconArrow(desiredState: String?) {
        val toggleIcon = if (desiredState == State.CLOSE) {
            IconUnify.CHEVRON_DOWN
        } else {
            IconUnify.CHEVRON_UP
        }
        partialOrderStatusWidgetBinding?.tokochatIcOrderVisibility?.setImage(toggleIcon)
    }

    private fun renderOpenCloseText() {
        partialOrderStatusWidgetBinding?.tokochatTpOrderVisibility?.text = State.OPEN
    }

    private fun renderChangerButtonText(desiredState: String?) {
        partialOrderStatusWidgetBinding?.tokochatTpOrderVisibility?.text =
            if (desiredState == State.CLOSE) {
                State.CLOSE
            } else {
                State.OPEN
            }
    }

    private fun getPref(): SharedPreferences? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun getPrefOrderPrefKey(): String {
        return tokoChatOrderProgressUiModel?.orderId.orEmpty()
    }

    private fun doWhenState(
        isOpen: () -> Unit,
        isClose: () -> Unit
    ) {
        when (state?.bodyVisibility) {
            State.OPEN -> isOpen()
            State.CLOSE -> isClose()
        }
    }

    private fun changeState(desiredState: String) {
        if (state?.bodyVisibility == desiredState) return
        state?.bodyVisibility = desiredState
        renderToggleStateChangerButton(desiredState)
        renderOrderNameContainer()
    }

    private fun showOrderNameContainer() {
        partialOrderStatusWidgetBinding?.tokochatClOrderNameContainer?.show()
    }

    private fun hideOrderNameContainer() {
        partialOrderStatusWidgetBinding?.tokochatClOrderNameContainer?.hide()
    }

    data class State(
        var bodyVisibility: String = DEFAULT_BODY_VISIBILITY,
        var hasSeen: Boolean = NEVER_SEEN
    ) {
        fun seen() {
            this.hasSeen = DEFAULT_HAS_SEEN
        }

        companion object {
            val COLOR_NOT_SEEN = com.tokopedia.unifyprinciples.R.color.Unify_G100
            var COLOR_SEEN = com.tokopedia.unifyprinciples.R.color.Unify_Background

            const val OPEN = "Tutup"
            const val CLOSE = "Lihat"

            private const val DEFAULT_BODY_VISIBILITY = OPEN
            private const val DEFAULT_HAS_SEEN = true
            private const val NEVER_SEEN = false
        }
    }

    interface Listener {
        fun onLocalLoadRetryClicked()
        fun onTransactionWidgetClicked(appLink: String)
    }

    companion object {
        private val DEFAULT_STATE = CommonUtil.toJson(State())
        private const val PREF_NAME = "TokoChat_TransactionOrderWidgetPreference"
    }
}
