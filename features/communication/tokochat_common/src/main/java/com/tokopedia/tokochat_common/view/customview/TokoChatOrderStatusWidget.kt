package com.tokopedia.tokochat_common.view.customview

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.PartialOrderStatusWidgetBinding
import com.tokopedia.tokochat_common.util.CommonUtil
import com.tokopedia.tokochat_common.view.uimodel.TokoChatOrderProgressUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import java.lang.StringBuilder

class TokoChatOrderStatusWidget : LinearLayout {


    private var status: Typography? = null
    private var stateChanger: Typography? = null
    private var icStateChanger: IconUnify? = null
    private var descriptionContainer: ConstraintLayout? = null
    private var cardOrderContainer: ConstraintLayout? = null
    private var productThumbnail: ImageView? = null
    private var productName: Typography? = null
    private var estimateTitle: Typography? = null
    private var estimateValue: Typography? = null
    private var actionBtn: UnifyButton? = null
    private var tokoChatOrderProgressUiModel: TokoChatOrderProgressUiModel? = null
    private var state = State()
    private var canBeRendered = false

    private var listener: Listener? = null

    private var binding: PartialOrderStatusWidgetBinding? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        initViewLayout()
        initBindView()
        binding = PartialOrderStatusWidgetBinding.inflate(LayoutInflater.from(context), this)
    }

    fun renderIfExist() {
        canBeRendered = true
        render(this.listener, this.tokoChatOrderProgressUiModel)
    }

    fun render(listener: Listener?, orderProgressUiModel: TokoChatOrderProgressUiModel?) {
        assignFields(listener, orderProgressUiModel)
        if (!shouldBeRendered()) return
        loadPreviousState()
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

    private fun initViewLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun shouldBeRendered(): Boolean {
        return canBeRendered && tokoChatOrderProgressUiModel != null &&
            tokoChatOrderProgressUiModel?.isEnable?.also { shouldShow ->
                if (!shouldShow) hide()
            } == true
    }

    private fun loadPreviousState() {
        try {
            val stateJsonString = getPref()?.getString(getPrefOrderPrefKey(), DEFAULT_STATE)
            state = CommonUtil.fromJson(stateJsonString, State::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun renderStateDescription() {
        doWhenState(
            isOpen = { showDescription() },
            isClose = { hideDescription() }
        )
    }

    /**
     * Check if the implementation is one of the 2:
     * 1. if transaction is not finished user can open/close
     * 2. if transaction is finished. closed state only
     */
    private fun renderStateChangerButton() {
        if (openCloseState()) {
            renderOpenCloseStateChangerButton()
            bindClickOpenCloseState()
            bindClickCardDescription()
            renderImageThumbnail()
            renderProductName()
            renderEstimation()
            renderActionButton()
        } else {
            unbindClickStatus()
            hideDescription()
            renderCloseStateChangerButton()
            bindClickCloseState()
        }
    }

    private fun renderOrderStatus() {
        binding?.tokochatTpOrderStatus?.text = tokoChatOrderProgressUiModel?.status
    }

    private fun renderLayoutVisibility() {
        showWithCondition(tokoChatOrderProgressUiModel?.isEnable == true)
    }

    private fun renderHasBeenSeen() {
        if (!state.hasSeen) {
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
        stateChanger?.setOnClickListener(clickListener)
        binding?.tokochatTpOrderStatus?.setOnClickListener(clickListener)
        binding?.tokochatIcOrderVisibility?.setOnClickListener(clickListener)
    }

    private fun bindClickCardDescription() {
        binding?.tokochatClDescriptionContainer?.setOnClickListener {
            RouteManager.route(context, tokoChatOrderProgressUiModel?.appLink)
        }
    }

    private fun renderImageThumbnail() {
        productThumbnail?.loadImageRounded(chatOrder.imageUrl, 8f.toPx())
    }

    private fun renderProductName() {
        productName?.text = MethodChecker.fromHtml(chatOrder.name)
    }

    @SuppressLint("SetTextI18n")
    private fun renderEstimation() {
        binding?.tokochatTpEstimateLabel?.text =
            StringBuilder(tokoChatOrderProgressUiModel?.labelTitle.orEmpty()).append(":")
        binding?.tokochatTpEstimateValue?.text = tokoChatOrderProgressUiModel?.labelValue.orEmpty()
    }
    private fun renderCloseStateChangerButton() {
        renderChangerButtonText("")
        renderChangerButtonTextColor()
        renderChangerButtonCompoundDrawable(null)
    }

    private fun bindClickCloseState() {
        stateChanger?.setOnClickListener {
            RouteManager.route(context, tokoChatOrderProgressUiModel?.appLink)
        }
    }

    private fun renderFirstTimeSeen() {
        state.seen()
    }

    private fun renderChangerButtonTextColor() {
        stateChanger?.setTextColor(
            MethodChecker.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_G500
            )
        )
    }

    private fun renderOpenCloseStateChangerButton() {
        renderChangerButtonText()
        renderChangerButtonCompoundDrawable()
    }

    private fun renderChangerButtonCompoundDrawable(@DrawableRes customIcon: Int? = null) {
//        if (customIcon != null) {
//            stateChanger?.setCompoundDrawablesWithIntrinsicBounds(0, 0, customIcon, 0)
//            return
//        }
//        doWhenState(
//            isOpen = {
//                stateChanger?.setCompoundDrawablesWithIntrinsicBounds(
//                    0, 0, R.drawable.ic_topchat_arrow_small_up, 0
//                )
//            },
//            isClose = {
//                stateChanger?.setCompoundDrawablesWithIntrinsicBounds(
//                    0, 0, R.drawable.ic_topchat_arrow_small_down, 0
//                )
//            }
//        )
    }

    private fun renderChangerButtonText(custom: String? = null) {
        if (custom != null) {
            stateChanger?.text = custom
        } else {
            stateChanger?.text = state.bodyVisibility
        }
    }

    private fun getPref(): SharedPreferences? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun getPrefOrderPrefKey(): String {
        return tokoChatOrderProgressUiModel.orderId
    }

    private fun doWhenState(
        isOpen: () -> Unit,
        isClose: () -> Unit
    ) {
        when (state.bodyVisibility) {
            State.OPEN -> isOpen()
            State.CLOSE -> isClose()
        }
    }

    private fun changeState(desiredState: String) {
        if (state.bodyVisibility == desiredState) return
        state.bodyVisibility = desiredState
        renderOpenCloseStateChangerButton()
        renderStateDescription()
    }

    private fun showDescription() {
        binding?.tokochatClDescriptionContainer?.show()
    }

    private fun unbindClickStatus() {
        binding?.tokochatTpOrderStatus?.setOnClickListener(null)
    }

    private fun hideDescription() {
        binding?.tokochatClDescriptionContainer?.hide()
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
            private const val DEFAULT_HAS_SEEN = false
        }
    }

    interface Listener {

    }

    companion object {
        private val LAYOUT = R.layout.partial_order_status_widget
        private val DEFAULT_STATE = CommonUtil.toJson(State())
        private const val PREF_NAME = "Chat_TransactionOrderProgressPreference"
    }
}
