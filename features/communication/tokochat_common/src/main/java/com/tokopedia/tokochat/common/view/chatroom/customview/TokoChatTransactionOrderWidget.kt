package com.tokopedia.tokochat.common.view.chatroom.customview

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
import com.tokopedia.tokochat.common.util.CommonUtil
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatOrderProgressUiModel
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.TokochatItemOrderStatusShimmerBinding
import com.tokopedia.tokochat_common.databinding.TokochatPartialLogisticOrderStatusWidgetBinding
import com.tokopedia.tokochat_common.databinding.TokochatPartialTokofoodOrderStatusWidgetBinding
import com.tokopedia.tokochat_common.databinding.TokochatTransactionWidgetBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class TokoChatTransactionOrderWidget : LinearLayout {

    private var tokoChatOrderProgressUiModel: TokoChatOrderProgressUiModel? = null
    private var state: State? = State()

    private var listener: Listener? = null

    private var binding: TokochatTransactionWidgetBinding? = null

    private var partialTokoFoodOrderStatusWidgetBinding: TokochatPartialTokofoodOrderStatusWidgetBinding? = null
    private var partialLogisticOrderStatusWidgetBinding: TokochatPartialLogisticOrderStatusWidgetBinding? = null
    private var shimmerOrderStatusWidgetBinding: TokochatItemOrderStatusShimmerBinding? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        partialTokoFoodOrderStatusWidgetBinding = null
        partialLogisticOrderStatusWidgetBinding = null
        shimmerOrderStatusWidgetBinding = null
        binding = null
        listener = null
        tokoChatOrderProgressUiModel = null
    }

    init {
        setupViewBinding()
        setBringToFrontAndBgColor()
    }

    fun getTokoChatOrderProgressUiModel() = tokoChatOrderProgressUiModel

    fun updateTransactionWidget(orderProgressUiModel: TokoChatOrderProgressUiModel) {
        if (tokoChatOrderProgressUiModel != orderProgressUiModel) {
            tokoChatOrderProgressUiModel = orderProgressUiModel
            renderUpdate()
        }
    }

    fun showShimmeringWidget() {
        binding?.tokochatLocalloadErrorTransactionWidget?.hide()
        partialTokoFoodOrderStatusWidgetBinding?.root?.hide()
        partialLogisticOrderStatusWidgetBinding?.root?.hide()
        setupShimmeringOrderStatusBinding()
    }

    fun showTransactionWidget(
        orderProgressUiModel: TokoChatOrderProgressUiModel?
    ) {
        this.tokoChatOrderProgressUiModel = orderProgressUiModel
        binding?.tokochatLocalloadErrorTransactionWidget?.hide()
        shimmerOrderStatusWidgetBinding?.root?.hide()
        orderProgressUiModel?.handleOrderProgress(
            onTokoFood = {
                partialTokoFoodOrderStatusWidgetBinding?.root?.show()
                setupPartialTokoFoodOrderStatusBinding()
            },
            onLogistic = {
                partialLogisticOrderStatusWidgetBinding?.root?.show()
                setupPartialLogisticOrderStatusBinding()
            }
        )
        render()
    }

    fun showLocalLoadTransaction() {
        partialTokoFoodOrderStatusWidgetBinding?.root?.hide()
        partialLogisticOrderStatusWidgetBinding?.root?.hide()
        shimmerOrderStatusWidgetBinding?.root?.hide()
        binding?.tokochatLocalloadErrorTransactionWidget?.show()
        setupLocalLoadTransaction()
    }

    fun hideTransactionLocalLoad() {
        binding?.tokochatLocalloadErrorTransactionWidget?.hide()
    }

    private fun setBringToFrontAndBgColor() {
        bringToFront()
        setBackgroundColor(MethodChecker.getColor(context, unifyprinciplesR.color.Unify_Background))
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

    private fun renderUpdate() {
        renderOrderStatus()
        renderProductName()
        renderImageThumbnail()
        renderEstimation()
        renderAdditionalInfo()
        renderButton()
    }

    fun assignListener(listener: Listener?) {
        this.listener = listener
    }

    private fun setupViewBinding() {
        binding = TokochatTransactionWidgetBinding.inflate(LayoutInflater.from(context), this)
    }

    private fun setupPartialTokoFoodOrderStatusBinding() {
        binding?.run {
            val partialTokoFoodOrderStatusVs =
                root.findViewById<View>(R.id.tokochat_partial_tokofood_order_status_viewstub)
            if (partialTokoFoodOrderStatusVs is ViewStub) {
                partialTokoFoodOrderStatusVs.inflate()
                bindPartialTokoFoodOrderStatusWidget(root)
            } else {
                partialTokoFoodOrderStatusVs.show()
            }
        }
    }

    private fun setupPartialLogisticOrderStatusBinding() {
        binding?.run {
            val partialLogisticOrderStatusVs =
                root.findViewById<View>(R.id.tokochat_partial_logistic_order_status_viewstub)
            if (partialLogisticOrderStatusVs is ViewStub) {
                partialLogisticOrderStatusVs.inflate()
                bindPartialLogisticOrderStatusWidget(root)
            } else {
                partialLogisticOrderStatusVs.show()
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

    private fun bindPartialTokoFoodOrderStatusWidget(root: View) {
        if (partialTokoFoodOrderStatusWidgetBinding == null) {
            partialTokoFoodOrderStatusWidgetBinding =
                TokochatPartialTokofoodOrderStatusWidgetBinding
                    .bind(root.findViewById(R.id.tokochat_partial_tokofood_order_status))
        }
    }

    private fun bindPartialLogisticOrderStatusWidget(root: View) {
        if (partialLogisticOrderStatusWidgetBinding == null) {
            partialLogisticOrderStatusWidgetBinding =
                TokochatPartialLogisticOrderStatusWidgetBinding
                    .bind(root.findViewById(R.id.tokochat_partial_logistic_order_status))
        }
    }

    private fun bindShimmerOrderStatusWidget(root: View) {
        if (shimmerOrderStatusWidgetBinding == null) {
            shimmerOrderStatusWidgetBinding =
                TokochatItemOrderStatusShimmerBinding
                    .bind(root.findViewById(R.id.tokochat_shimmer_order_status_viewstub))
        }
    }

    private fun renderStateDescription() {
        doWhenState(
            isOpen = { showContainerDescription() },
            isClose = { hideContainerDescription() }
        )
    }

    private fun showContainerDescription() {
        tokoChatOrderProgressUiModel?.handleOrderProgress(
            onTokoFood = {
                partialTokoFoodOrderStatusWidgetBinding
                    ?.tokochatClOrderNameContainer
                    ?.show()
            },
            onLogistic = {
                partialLogisticOrderStatusWidgetBinding
                    ?.tokochatLogisticClOrderNameContainer
                    ?.show()
            }
        )
    }

    private fun hideContainerDescription() {
        tokoChatOrderProgressUiModel?.handleOrderProgress(
            onTokoFood = {
                partialTokoFoodOrderStatusWidgetBinding
                    ?.tokochatClOrderNameContainer
                    ?.hide()
            },
            onLogistic = {
                partialLogisticOrderStatusWidgetBinding
                    ?.tokochatLogisticClOrderNameContainer
                    ?.hide()
            }
        )
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
            renderAdditionalInfo()
            renderButton()
        } else {
            hideOrderNameContainer()
        }
    }

    private fun renderOrderStatus() {
        tokoChatOrderProgressUiModel?.handleOrderProgress(
            onTokoFood = {
                partialTokoFoodOrderStatusWidgetBinding?.tokochatTpOrderStatus?.text =
                    tokoChatOrderProgressUiModel?.status
            },
            onLogistic = {
                partialLogisticOrderStatusWidgetBinding?.tokochatLogisticTpOrderStatus?.text =
                    tokoChatOrderProgressUiModel?.status
            }
        )
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
        tokoChatOrderProgressUiModel?.handleOrderProgress(
            onTokoFood = {
                partialTokoFoodOrderStatusWidgetBinding?.root?.setBackgroundColor(color)
            },
            onLogistic = {
                partialLogisticOrderStatusWidgetBinding?.root?.setBackgroundColor(color)
            }
        )
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
                isOpen = {
                    changeState(State.CLOSE)
                    listener?.onTransactionWidgetClosed()
                },
                isClose = { changeState(State.OPEN) }
            )
        }
        tokoChatOrderProgressUiModel?.handleOrderProgress(
            onTokoFood = {
                partialTokoFoodOrderStatusWidgetBinding
                    ?.tokochatTpOrderVisibility
                    ?.setOnClickListener(clickListener)
                partialTokoFoodOrderStatusWidgetBinding
                    ?.tokochatTpOrderStatus
                    ?.setOnClickListener(clickListener)
                partialTokoFoodOrderStatusWidgetBinding
                    ?.tokochatIcOrderVisibility
                    ?.setOnClickListener(clickListener)
            },
            onLogistic = {
                partialLogisticOrderStatusWidgetBinding
                    ?.tokochatLogisticTpOrderVisibility
                    ?.setOnClickListener(clickListener)
                partialLogisticOrderStatusWidgetBinding
                    ?.tokochatLogisticTpOrderStatus
                    ?.setOnClickListener(clickListener)
                partialLogisticOrderStatusWidgetBinding
                    ?.tokochatLogisticIcOrderVisibility
                    ?.setOnClickListener(clickListener)
            }
        )
    }

    private fun bindClickCardContainer() {
        tokoChatOrderProgressUiModel?.handleOrderProgress(
            onTokoFood = {
                partialTokoFoodOrderStatusWidgetBinding
                    ?.tokochatClOrderNameContainer
                    ?.setOnClickListener {
                        tokoChatOrderProgressUiModel?.appLink?.let {
                            listener?.onTransactionWidgetClicked(it)
                        }
                    }
            },
            onLogistic = {
                partialLogisticOrderStatusWidgetBinding
                    ?.tokochatLogisticClOrderNameContainer
                    ?.setOnClickListener {
                        tokoChatOrderProgressUiModel?.appLink?.let {
                            listener?.onTransactionWidgetClicked(it)
                        }
                    }
            }
        )
    }

    private fun renderImageThumbnail() {
        tokoChatOrderProgressUiModel?.handleOrderProgress(
            onTokoFood = {
                partialTokoFoodOrderStatusWidgetBinding?.tokochatIvOrderThumbnail?.loadImage(
                    tokoChatOrderProgressUiModel?.imageUrl
                )
            },
            onLogistic = {
                partialLogisticOrderStatusWidgetBinding
                    ?.tokochatLogisticIvOrderThumbnail
                    ?.loadImage(tokoChatOrderProgressUiModel?.imageUrl)
            }
        )
    }

    private fun renderProductName() {
        val productName = MethodChecker.fromHtml(tokoChatOrderProgressUiModel?.name)
        tokoChatOrderProgressUiModel?.handleOrderProgress(
            onTokoFood = {
                partialTokoFoodOrderStatusWidgetBinding?.tokochatTpOrderName?.text =
                    productName
            },
            onLogistic = {
                partialLogisticOrderStatusWidgetBinding?.tokochatLogisticTpOrderName?.text =
                    productName
            }
        )
    }

    private fun renderEstimation() {
        tokoChatOrderProgressUiModel?.handleOrderProgress(
            onTokoFood = {
                partialTokoFoodOrderStatusWidgetBinding?.tokochatTpEstimateLabel?.text =
                    tokoChatOrderProgressUiModel?.labelTitle.orEmpty()
                partialTokoFoodOrderStatusWidgetBinding?.tokochatTpEstimateValue?.text =
                    tokoChatOrderProgressUiModel?.labelValue.orEmpty()
                partialTokoFoodOrderStatusWidgetBinding?.tokochatTpEstimateLabel?.showWithCondition(
                    tokoChatOrderProgressUiModel?.labelTitle?.isNotBlank() == true
                )
                partialTokoFoodOrderStatusWidgetBinding?.tokochatTpEstimateValue?.showWithCondition(
                    tokoChatOrderProgressUiModel?.labelValue?.isNotBlank() == true
                )
            },
            onLogistic = {
                partialLogisticOrderStatusWidgetBinding?.tokochatLogisticTpEstimateLabel?.text =
                    tokoChatOrderProgressUiModel?.labelTitle.orEmpty()
                partialLogisticOrderStatusWidgetBinding?.tokochatLogisticTpEstimateValue?.text =
                    tokoChatOrderProgressUiModel?.labelValue.orEmpty()
                partialLogisticOrderStatusWidgetBinding
                    ?.tokochatLogisticTpEstimateLabel
                    ?.showWithCondition(
                    tokoChatOrderProgressUiModel?.labelValue?.isNotBlank() == true
                    )
                partialLogisticOrderStatusWidgetBinding
                    ?.tokochatLogisticTpEstimateValue
                    ?.showWithCondition(
                        tokoChatOrderProgressUiModel?.labelValue?.isNotBlank() == true
                    )
            }
        )
    }

    private fun renderAdditionalInfo() {
        tokoChatOrderProgressUiModel?.handleOrderProgress(
            onLogistic = {
                partialLogisticOrderStatusWidgetBinding?.tokochatLogisticTpAdditionalInfo?.text =
                    tokoChatOrderProgressUiModel?.additionalInfo
                partialLogisticOrderStatusWidgetBinding
                    ?.tokochatLogisticTpAdditionalInfo
                    ?.showWithCondition(
                        tokoChatOrderProgressUiModel?.additionalInfo?.isNotBlank() == true
                    )
            }
        )
    }

    private fun renderButton() {
        tokoChatOrderProgressUiModel?.handleOrderProgress(
            onLogistic = {
                partialLogisticOrderStatusWidgetBinding?.tokochatLogisticBtnOrderProgress?.text =
                    tokoChatOrderProgressUiModel?.buttonText
                partialLogisticOrderStatusWidgetBinding
                    ?.tokochatLogisticBtnOrderProgress
                    ?.isEnabled = tokoChatOrderProgressUiModel?.buttonEnable?: false
                partialLogisticOrderStatusWidgetBinding
                    ?.tokochatLogisticBtnOrderProgress
                    ?.setOnClickListener {
                        listener?.onButtonClicked(
                            tokoChatOrderProgressUiModel?.buttonApplink?: "")
                    }
            }
        )
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
        tokoChatOrderProgressUiModel?.handleOrderProgress(
            onTokoFood = {
                partialTokoFoodOrderStatusWidgetBinding
                    ?.tokochatIcOrderVisibility
                    ?.setImage(toggleIcon)
            },
            onLogistic = {
                partialLogisticOrderStatusWidgetBinding
                    ?.tokochatLogisticIcOrderVisibility
                    ?.setImage(toggleIcon)
            }
        )
    }

    private fun renderOpenCloseText() {
        tokoChatOrderProgressUiModel?.handleOrderProgress(
            onTokoFood = {
                partialTokoFoodOrderStatusWidgetBinding?.tokochatTpOrderVisibility?.text =
                    State.OPEN
            },
            onLogistic = {
                partialLogisticOrderStatusWidgetBinding?.tokochatLogisticTpOrderVisibility?.text =
                    State.OPEN
            }
        )
    }

    private fun renderChangerButtonText(desiredState: String?) {
        partialTokoFoodOrderStatusWidgetBinding?.tokochatTpOrderVisibility?.text =
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
        tokoChatOrderProgressUiModel?.handleOrderProgress(
            onTokoFood = {
                partialTokoFoodOrderStatusWidgetBinding?.tokochatClOrderNameContainer?.show()
            },
            onLogistic = {
                partialLogisticOrderStatusWidgetBinding?.tokochatLogisticClOrderNameContainer?.show()
            }
        )
    }

    private fun hideOrderNameContainer() {
        tokoChatOrderProgressUiModel?.handleOrderProgress(
            onTokoFood = {
                partialTokoFoodOrderStatusWidgetBinding?.tokochatClOrderNameContainer?.hide()
            },
            onLogistic = {
                partialLogisticOrderStatusWidgetBinding?.tokochatLogisticClOrderNameContainer?.hide()
            }
        )
    }

    private fun TokoChatOrderProgressUiModel?.handleOrderProgress(
        onTokoFood: () -> Unit = {},
        onLogistic: () -> Unit = {},
        onNull: () -> Unit = {}
    ) {
        when (this?.orderProgressType) {
            TokoChatOrderProgressUiModel.Type.TOKOFOOD -> onTokoFood()
            TokoChatOrderProgressUiModel.Type.LOGISTIC -> onLogistic()
            else -> onNull()
        }
    }

    data class State(
        var bodyVisibility: String = DEFAULT_BODY_VISIBILITY,
        var hasSeen: Boolean = NEVER_SEEN
    ) {
        fun seen() {
            this.hasSeen = DEFAULT_HAS_SEEN
        }

        companion object {
            val COLOR_NOT_SEEN = unifyprinciplesR.color.Unify_GN50
            var COLOR_SEEN = unifyprinciplesR.color.Unify_Background

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
        fun onTransactionWidgetClosed()
        fun onButtonClicked(appLink: String)
    }

    companion object {
        private val DEFAULT_STATE = CommonUtil.toJson(State())
        private const val PREF_NAME = "TokoChat_TransactionOrderWidgetPreference"
    }
}
