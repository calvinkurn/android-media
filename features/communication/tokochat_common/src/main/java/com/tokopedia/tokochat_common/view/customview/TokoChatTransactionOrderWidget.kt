package com.tokopedia.tokochat_common.view.customview

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.ItemOrderStatusShimmerBinding
import com.tokopedia.tokochat_common.databinding.PartialOrderStatusWidgetBinding
import com.tokopedia.tokochat_common.databinding.TokochatTransactionWidgetBinding
import com.tokopedia.tokochat_common.util.CommonUtil
import com.tokopedia.tokochat_common.view.uimodel.TokoChatOrderProgressUiModel
import java.lang.StringBuilder

class TokoChatTransactionOrderWidget : LinearLayout {

    private var tokoChatOrderProgressUiModel: TokoChatOrderProgressUiModel? = null
    private var state: State? = State()

    private var listener: Listener? = null

    private var binding: TokochatTransactionWidgetBinding? = null

    private var partialOrderStatusWidgetBinding: PartialOrderStatusWidgetBinding? = null

    private var shimmerOrderStatusWidgetBinding: ItemOrderStatusShimmerBinding? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setupViewBinding()
    }

    fun showShimmeringWidget() {
        binding?.tokochatLocalloadErrorTransactionWidget?.hide()
        partialOrderStatusWidgetBinding?.root?.hide()
        setupShimmeringOrderStatusBinding()
    }

    fun showTransactionWidget() {
        binding?.tokochatLocalloadErrorTransactionWidget?.hide()
        partialOrderStatusWidgetBinding?.root?.show()
    }

    fun render(listener: Listener?, orderProgressUiModel: TokoChatOrderProgressUiModel?) {
        assignFields(listener, orderProgressUiModel)
        loadPreviousState()
        renderOrderNameContainer()
        renderBackgroundColor()
        renderStateDescription()
        renderStateChangerButton()
        renderOrderStatus()
        renderLayoutVisibility()
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
        partialOrderStatusWidgetBinding = PartialOrderStatusWidgetBinding.inflate(LayoutInflater.from(context), this)
    }

    private fun setupPartialOrderStatusBinding() {
        binding?.run {
            val partialOrderStatusVs =
                root.findViewById<View>(R.id.tokochat_partial_order_status_viewstub)
            if (partialOrderStatusVs is ViewStub) {
                partialOrderStatusVs.inflate()
                if (partialOrderStatusWidgetBinding == null) {
                    partialOrderStatusWidgetBinding =
                        PartialOrderStatusWidgetBinding.bind(partialOrderStatusVs)
                }
            } else {
                partialOrderStatusVs.show()
            }
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

    private fun setupShimmeringOrderStatusBinding() {
        binding?.run {
            val shimmerOrderStatusVs =
                root.findViewById<View>(R.id.tokochat_shimmer_order_status_viewstub)
            if (shimmerOrderStatusVs is ViewStub) {
                shimmerOrderStatusVs.inflate()
                if (shimmerOrderStatusWidgetBinding == null) {
                    shimmerOrderStatusWidgetBinding =
                        ItemOrderStatusShimmerBinding.bind(shimmerOrderStatusVs)
                }
            } else {
                shimmerOrderStatusVs.show()
            }
        }
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
        if (state?.hasSeen == true) {
            renderOpenCloseStateChangerButton()
            bindClickOpenCloseState()
            bindClickCardContainer()
            showOrderNameContainer()
            renderImageThumbnail()
            renderProductName()
            renderEstimation()
        } else {
            hideOrderNameContainer()
            renderCloseStateChangerButton()
        }
    }

    private fun renderOrderStatus() {
        partialOrderStatusWidgetBinding?.tokochatTpOrderStatus?.text = tokoChatOrderProgressUiModel?.status
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
        partialOrderStatusWidgetBinding?.tokochatClOrderNameContainer?.setBackgroundColor(color)
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
            RouteManager.route(context, tokoChatOrderProgressUiModel?.appLink)
        }
    }

    private fun renderImageThumbnail() {
        partialOrderStatusWidgetBinding?.tokochatIvOrderThumbnail?.loadImage(tokoChatOrderProgressUiModel?.imageUrl)
    }

    private fun renderProductName() {
        partialOrderStatusWidgetBinding?.tokochatTpOrderName?.text = MethodChecker.fromHtml(tokoChatOrderProgressUiModel?.name)
    }

    private fun renderEstimation() {
        partialOrderStatusWidgetBinding?.tokochatTpEstimateLabel?.text =
            StringBuilder(tokoChatOrderProgressUiModel?.labelTitle.orEmpty()).append(":")
        partialOrderStatusWidgetBinding?.tokochatTpEstimateValue?.text = tokoChatOrderProgressUiModel?.labelValue.orEmpty()
    }
    private fun renderCloseStateChangerButton() {
        renderChangerButtonText()
        renderChangerButtonCompoundDrawable(null)
    }

    private fun renderFirstTimeSeen() {
        state?.seen()
    }

    private fun renderOpenCloseStateChangerButton() {
        renderChangerButtonText()
        renderChangerButtonCompoundDrawable()
    }

    private fun renderChangerButtonCompoundDrawable(@DrawableRes customIcon: Int? = null) {

    }

    private fun renderChangerButtonText() {

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
        renderOpenCloseStateChangerButton()
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
        var hasSeen: Boolean = DEFAULT_HAS_SEEN
    ) {
        fun seen() {
            this.hasSeen = true
        }

        companion object {
            val COLOR_NOT_SEEN = com.tokopedia.unifyprinciples.R.color.Unify_G100
            var COLOR_SEEN = com.tokopedia.unifyprinciples.R.color.Unify_Background

            const val OPEN = "Tutup"
            const val CLOSE = "Lihat"

            private const val DEFAULT_BODY_VISIBILITY = OPEN
            private const val DEFAULT_HAS_SEEN = true
        }
    }

    sealed class ViewState {
        object TransactionOrderWidget: ViewState()
        object OrderShimmeringState: ViewState()
        object OrderErrorState: ViewState()
    }

    interface Listener {
        fun onRetryClicked()
    }

    companion object {
        private val LAYOUT = R.layout.partial_order_status_widget
        private val DEFAULT_STATE = CommonUtil.toJson(State())
        private const val PREF_NAME = "TokoChat_TransactionOrderWidgetPreference"
    }
}
