package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestion
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.srw.SrwTitleUiModel
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw.SrwQuestionViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw.SrwTitleViewHolder

class SrwRecyclerView : RecyclerView {

    private var chatSmartReplyQuestion = ChatSmartReplyQuestion()
    private val rvAdapter = SrwAdapter(SrwTypeFactoryImpl())

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(
            context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    init {
        setHasFixedSize(true)
        adapter = rvAdapter
    }

    fun updateSrwList(data: ChatSmartReplyQuestionResponse?) {
        if (data == null) return
        chatSmartReplyQuestion = data.chatSmartReplyQuestion
        rvAdapter.updateSrwList(chatSmartReplyQuestion)
    }
}

/**
 * Adapter used specifically for [SrwRecyclerView]
 */
class SrwAdapter(
        srwTypeFactory: SrwTypeFactory
) : BaseListAdapter<Visitable<*>, SrwTypeFactory>(srwTypeFactory) {

    fun updateSrwList(chatSmartReplyQuestion: ChatSmartReplyQuestion) {
        visitables.clear()
        visitables.addAll(chatSmartReplyQuestion.visitables)
        notifyDataSetChanged()
    }

}

/**
 * TypeFactory used specifically for [SrwAdapter]
 */
interface SrwTypeFactory : AdapterTypeFactory {
    fun type(srwTitleUiModel: SrwTitleUiModel): Int
    fun type(questionUiModel: QuestionUiModel): Int
}

/**
 * TypeFactoryImpl used specifically for [SrwAdapter]
 */
class SrwTypeFactoryImpl : BaseAdapterTypeFactory(), SrwTypeFactory {
    override fun type(srwTitleUiModel: SrwTitleUiModel): Int {
        return SrwTitleViewHolder.LAYOUT
    }

    override fun type(questionUiModel: QuestionUiModel): Int {
        return SrwQuestionViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SrwTitleViewHolder.LAYOUT -> SrwTitleViewHolder(parent)
            SrwQuestionViewHolder.LAYOUT -> SrwQuestionViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}