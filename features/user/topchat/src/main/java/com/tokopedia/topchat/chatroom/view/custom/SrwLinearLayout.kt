package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestion
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.topchat.chatroom.view.adapter.decoration.SrwItemDecoration
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw.SrwQuestionViewHolder
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifyprinciples.Typography

class SrwLinearLayout : LinearLayout {

    private var chatSmartReplyQuestion = ChatSmartReplyQuestion()
    private val typeFactory = SrwTypeFactoryImpl()
    private val rvAdapter = SrwAdapter(typeFactory)

    private var title: Typography? = null
    private var titleContainer: LinearLayout? = null
    private var titleIcon: IconUnify? = null
    private var rvSrw: RecyclerView? = null

    private var bgExpanded: Drawable? = null

    /**
     * Default state would be expanded
     */
    private var isExpanded = true

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(
            context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    init {
        initViewLayout()
        initBackground()
        initViewBind()
        initToggleExpandCollapsed()
        initRecyclerView()
    }

    fun updateSrwList(data: ChatSmartReplyQuestionResponse?) {
        if (data == null) return
        updateTitle(data)
        updateList(data)
    }

    fun initialize(listener: SrwQuestionViewHolder.Listener) {
        typeFactory.srwQuestionListener = listener
    }

    fun isAllowToShow(): Boolean {
        return chatSmartReplyQuestion.hasQuestion
    }

    private fun initViewLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initBackground() {
        bgExpanded = ViewUtil.generateBackgroundWithShadow(
                this,
                com.tokopedia.unifyprinciples.R.color.Unify_N0,
                R.dimen.dp_topchat_20,
                R.dimen.dp_topchat_0,
                R.dimen.dp_topchat_20,
                R.dimen.dp_topchat_20,
                com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
                R.dimen.dp_topchat_2,
                R.dimen.dp_topchat_2,
                Gravity.CENTER
        )
        background = bgExpanded
    }

    private fun initViewBind() {
        title = findViewById(R.id.tp_srw_partial)
        titleContainer = findViewById(R.id.tp_srw_container_partial)
        titleIcon = findViewById(R.id.ic_header_state_partial)
        rvSrw = findViewById(R.id.rv_srw_partial)
    }

    private fun initToggleExpandCollapsed() {
        titleContainer?.setOnClickListener {
            isExpanded = !isExpanded
            updateState()
        }
    }

    private fun updateState() {
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
            addItemDecoration(SrwItemDecoration(context))
        }
    }

    private fun updateTitle(data: ChatSmartReplyQuestionResponse) {
        title?.text = data.chatSmartReplyQuestion.title
    }

    private fun updateList(data: ChatSmartReplyQuestionResponse) {
        chatSmartReplyQuestion = data.chatSmartReplyQuestion
        rvAdapter.updateSrwList(chatSmartReplyQuestion)
    }

    companion object {
        private val LAYOUT = R.layout.partial_topchat_srw
    }
}

/**
 * Adapter used specifically for [SrwLinearLayout]
 */
class SrwAdapter(
        srwTypeFactory: SrwTypeFactory
) : BaseListAdapter<Visitable<*>, SrwTypeFactory>(srwTypeFactory) {

    fun updateSrwList(chatSmartReplyQuestion: ChatSmartReplyQuestion) {
        visitables.clear()
        visitables.addAll(chatSmartReplyQuestion.questions)
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