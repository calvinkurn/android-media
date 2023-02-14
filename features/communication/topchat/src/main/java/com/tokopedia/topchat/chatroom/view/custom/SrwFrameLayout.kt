package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestion
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.topchat.chatroom.view.adapter.decoration.SrwItemDecoration
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw.SrwQuestionViewHolder
import com.tokopedia.topchat.chatroom.view.onboarding.SrwOnBoarding
import com.tokopedia.topchat.common.data.Resource
import com.tokopedia.topchat.common.data.Status
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class SrwFrameLayout : FrameLayout {

    private var chatSmartReplyQuestion = ChatSmartReplyQuestion()
    private val typeFactory = SrwTypeFactoryImpl()
    private val rvAdapter = SrwAdapter(typeFactory)

    var rvSrw: RecyclerView? = null
        private set

    private var title: Typography? = null
    private var titleContainer: LinearLayout? = null
    private var titleIcon: IconUnify? = null
    private var srwContentContainer: LinearLayout? = null

    private var bgExpanded: Drawable? = null
    private var onBoarding = SrwOnBoarding()
    private var hasShownOnBoarding = false
    private var itemDecoration = SrwItemDecoration(context)

    /**
     * To differentiate the SRW Tab and Bubble
     */
    var isSrwBubble: Boolean = true
        set(value) {
            field = value
            initBackground()
            itemDecoration.source = getSrwSource()
        }
    var onBoardingAnchor: View? = null

    /**
     * Force to hide, used when reply to specific bubble shown
     */
    var isForceToHide = false

    /**
     * Default state would be expanded
     */
    var isExpanded = true
        set(value) {
            field = value
            updateState()
        }
    private var listener: Listener? = null
    private var latestState: Resource<ChatSmartReplyQuestionResponse>? = null

    interface Listener {
        fun trackViewSrw()
        fun onExpandStateChanged(isExpanded: Boolean)
        fun shouldShowOnBoarding(): Boolean
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    init {
        initViewLayout()
        initViewBind()
        initBackground()
        initToggleExpandCollapsed()
        initRecyclerView()
    }

    fun updateSrwList(data: ChatSmartReplyQuestionResponse?) {
        if (data == null) return
        updateTitle(data)
        updateList(data)
    }

    fun initialize(questionListener: SrwQuestionViewHolder.Listener, listener: Listener) {
        typeFactory.srwQuestionListener = questionListener
        this.listener = listener
    }

    fun initialize(
        srwState: SrwState,
        srwBubbleListener: SrwQuestionViewHolder.Listener?
    ) {
        typeFactory.srwQuestionListener = srwBubbleListener
        this.listener = srwState.listener
        this.isExpanded = srwState.isExpanded
        srwState.latestState?.let {
            updateStatus(it)
            renderSrwState()
        }
    }

    fun isAllowToShow(): Boolean {
        return chatSmartReplyQuestion.hasQuestion && !isForceToHide
    }

    fun updateStatus(latestState: Resource<ChatSmartReplyQuestionResponse>) {
        this.latestState = latestState
        when (latestState.status) {
            Status.SUCCESS -> {
                updateSrwList(latestState.data)
            }
            Status.ERROR -> {
                resetSrw()
            }
            else -> {
            }
        }
    }

    private fun initViewLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initBackground() {
        bgExpanded = ViewUtil.generateBackgroundWithShadow(
            this,
            backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_Background,
            topLeftRadius = getTopLeftRadius(),
            topRightRadius = getTopRightRadius(),
            bottomLeftRadius = R.dimen.dp_topchat_20,
            bottomRightRadius = R.dimen.dp_topchat_20,
            shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_NN600,
            elevation = getElevationShadow(),
            shadowRadius = getElevationShadow(),
            strokeColor = getStrokeColor(),
            strokeWidth = getStrokeWidth(),
            shadowGravity = Gravity.CENTER
        )
        srwContentContainer?.background = bgExpanded
    }

    private fun getTopLeftRadius(): Int {
        return if (isSrwBubble) {
            R.dimen.dp_topchat_20
        } else {
            R.dimen.dp_topchat_0
        }
    }

    private fun getTopRightRadius(): Int {
        return if (isSrwBubble) {
            R.dimen.dp_topchat_0
        } else {
            R.dimen.dp_topchat_20
        }
    }

    private fun getElevationShadow(): Int {
        return if (isSrwBubble) {
            R.dimen.dp_topchat_2
        } else {
            R.dimen.dp_topchat_0
        }
    }

    private fun getStrokeColor(): Int {
        return if (isSrwBubble) {
            com.tokopedia.unifyprinciples.R.color.Unify_Background
        } else {
            com.tokopedia.unifyprinciples.R.color.Unify_NN50
        }
    }

    private fun getStrokeWidth(): Int {
        return if (isSrwBubble) {
            R.dimen.dp_topchat_0
        } else {
            R.dimen.dp_topchat_1
        }
    }

    private fun getSrwSource(): SrwItemDecoration.SrwItemSource {
        return if (isSrwBubble) {
            SrwItemDecoration.SrwItemSource.SRW_BUBBLE
        } else {
            SrwItemDecoration.SrwItemSource.TAB_LAYOUT
        }
    }

    private fun initViewBind() {
        title = findViewById(R.id.tp_srw_partial)
        titleContainer = findViewById(R.id.tp_srw_container_partial)
        titleIcon = findViewById(R.id.ic_header_state_partial)
        rvSrw = findViewById(R.id.rv_srw_partial)
        srwContentContainer = findViewById(R.id.rv_srw_content_container)
    }

    private fun initToggleExpandCollapsed() {
        titleContainer?.setOnClickListener {
            isExpanded = !isExpanded
            updateState()
            listener?.onExpandStateChanged(isExpanded)
        }
    }

    private fun updateState() {
        if (!isVisible) return
        if (isExpanded) {
            rvSrw?.show()
            titleIcon?.setImage(IconUnify.CHEVRON_UP)
        } else {
            rvSrw?.hide()
            titleIcon?.setImage(IconUnify.CHEVRON_DOWN)
        }
    }

    private fun initRecyclerView() {
        rvSrw?.apply {
            setHasFixedSize(true)
            adapter = rvAdapter
            addItemDecoration(itemDecoration)
        }
    }

    private fun updateTitle(data: ChatSmartReplyQuestionResponse) {
        title?.text = data.chatSmartReplyQuestion.title
    }

    private fun updateList(data: ChatSmartReplyQuestionResponse) {
        chatSmartReplyQuestion = data.chatSmartReplyQuestion
        rvAdapter.updateSrwList(chatSmartReplyQuestion)
    }

    fun resetChatReplyQuestion() {
        chatSmartReplyQuestion = ChatSmartReplyQuestion()
    }

    fun resetSrw() {
        resetChatReplyQuestion()
        rvAdapter.resetSrwQuestions()
    }

    fun renderSrwState() {
        show()
        when {
            isLoadingState() -> renderLoadingState()
            isErrorState() -> renderErrorState()
            isSuccessState() -> renderSrwContent()
        }
    }

    fun isLoadingState(): Boolean {
        return latestState != null && latestState?.status == Status.LOADING
    }

    fun isSuccessState(): Boolean {
        return latestState != null && latestState?.status == Status.SUCCESS
    }

    fun isErrorState(): Boolean {
        return latestState != null && latestState?.status == Status.ERROR
    }

    private fun renderLoadingState() {
        hideSrwContent()
        resetSrw()
    }

    private fun renderSrwContent() {
        showSrwContent()
    }

    private fun renderErrorState() {
        hideSrwContent()
        resetSrw()
    }

    fun hideSrw() {
        hide()
        hideSrwContent()
    }

    fun setSrwTitleVisibility(shouldShow: Boolean) {
        titleContainer?.showWithCondition(shouldShow)
    }

    fun setContentMargin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.setMargins(left.toPx(), top.toPx(), right.toPx(), bottom.toPx())
        srwContentContainer?.layoutParams = params
    }

    private fun showSrwContent() {
        if (!hasShownOnBoarding) {
            listener?.trackViewSrw()
            showOnBoardingSrw()
        } else {
            dismissOnBoarding()
        }
        srwContentContainer?.show()
        updateState()
    }

    private fun showOnBoardingSrw() {
        if (listener?.shouldShowOnBoarding() == true) {
            if (isSrwBubble) {
                titleContainer?.let {
                    showOnBoarding(it)
                }
            } else {
                onBoardingAnchor?.let {
                    showOnBoarding(it)
                }
            }
        }
    }

    private fun showOnBoarding(view: View) {
        onBoarding.show(context, view)
        hasShownOnBoarding = true
    }

    private fun dismissOnBoarding() {
        onBoarding.dismiss()
    }

    private fun hideSrwContent() {
        srwContentContainer?.hide()
    }

    fun getStateInfo(): SrwState {
        return SrwState(isExpanded, listener, latestState)
    }

    fun isShowing(): Boolean {
        return isAllowToShow() && isVisible
    }

    class SrwState(
        val isExpanded: Boolean,
        val listener: Listener? = null,
        val latestState: Resource<ChatSmartReplyQuestionResponse>? = null
    )

    companion object {
        private val LAYOUT = R.layout.partial_topchat_srw
        const val TAG = "SRW_COACHMARK"
    }
}

/**
 * Adapter used specifically for [SrwFrameLayout]
 */
class SrwAdapter(
    srwTypeFactory: SrwTypeFactory
) : BaseListAdapter<Visitable<*>, SrwTypeFactory>(srwTypeFactory) {

    fun updateSrwList(chatSmartReplyQuestion: ChatSmartReplyQuestion) {
        visitables.clear()
        visitables.addAll(chatSmartReplyQuestion.questions)
        notifyDataSetChanged()
    }

    fun resetSrwQuestions() {
        visitables.clear()
        notifyDataSetChanged()
    }

}

/**
 * TypeFactory used specifically for [SrwAdapter]
 */
interface SrwTypeFactory : AdapterTypeFactory {
    fun type(questionUiModel: QuestionUiModel): Int
}

/**
 * TypeFactoryImpl used specifically for [SrwAdapter]
 */
class SrwTypeFactoryImpl : BaseAdapterTypeFactory(), SrwTypeFactory {

    var srwQuestionListener: SrwQuestionViewHolder.Listener? = null

    override fun type(questionUiModel: QuestionUiModel): Int {
        return SrwQuestionViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SrwQuestionViewHolder.LAYOUT -> SrwQuestionViewHolder(parent, srwQuestionListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
