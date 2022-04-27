package com.tokopedia.chatbot.view.customview.reply

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class ReplyBubbleAreaMessage : ConstraintLayout {

    private var title: TextView? = null
    private var description: TextView? = null
    private var closeBtn: ImageView? = null
    private var container: ConstraintLayout? = null
    private var replyIcon: IconUnify? = null
    var referredMsg: ParentReply? = null
    var listener: Listener? = null
        private set

    interface Listener {
        fun getUserName(senderId: String): String
        fun goToBubble(parentReply: ParentReply)
        fun showReplyOption(messageUiModel: MessageUiModel)
    }

    fun setReplyListener(listener: Listener) {
        this.listener = listener
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val bgRight = ViewUtil.generateBackgroundWithShadow(
        view =this,
        backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_GN100,
        topLeftRadius =  com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4,
        topRightRadius = com.tokopedia.unifyprinciples.R.dimen.unify_space_0,
        bottomLeftRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        bottomRightRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
        elevation = R.dimen.dp_chatbot_2,
        shadowRadius =  R.dimen.dp_chatbot_1,
        shadowGravity = Gravity.CENTER,
        strokeColor = com.tokopedia.unifyprinciples.R.color.Unify_GN50,
        strokeWidth = getStrokeWidthSenderDimenRes()
    )
    
    private val bgLeft = ViewUtil.generateBackgroundWithShadow(
        view = this,
        backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_N50,
        topLeftRadius = com.tokopedia.unifyprinciples.R.dimen.unify_space_0,
        topRightRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4,
        bottomLeftRadius = com.tokopedia.unifyprinciples.R.dimen.unify_space_0,
        bottomRightRadius = com.tokopedia.unifyprinciples.R.dimen.unify_space_0,
        shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
        elevation =  R.dimen.dp_chatbot_2,
        shadowRadius =  R.dimen.dp_chatbot_1,
        shadowGravity = Gravity.CENTER,
        strokeColor = com.tokopedia.unifyprinciples.R.color.Unify_N0,
        strokeWidth = getStrokeWidthSenderDimenRes()
    )

    init {
        initLayout()
        initViewBinding()
        updateCloseButtonState(true)
    }

    private fun initLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initViewBinding() {
        title = findViewById(R.id.reply_from)
        description = findViewById(R.id.reply_message)
        closeBtn = findViewById(R.id.close_btn)
        container = findViewById(R.id.container)
        replyIcon = findViewById(R.id.reply_icon)
    }

    fun bindReplyData(uiModel : BaseChatUiModel){
        val parentReply = uiModel.parentReply
        if (parentReply!=null && !uiModel.isDeleted()){
            bindParentReplyData(parentReply,uiModel.replyId,"","")
            updateCloseButtonState(false)
            show()
        }else{
            hide()
        }
    }

    fun updateReplyButtonState(toShow: Boolean) {
        if (toShow)
            replyIcon?.show()
        else
            replyIcon?.hide()
    }

    private fun bindParentReplyData(parentReply: ParentReply, replyId: String?, message : String, from : String) {
        referTo(parentReply)
        setTitle(from)
        setReplyMsg(message)
        //TODO fix bindClick
    //    bindClick(parentReply, replyId)
    }

    private fun referTo(parentReply: ParentReply) {
        referredMsg = parentReply
    }

    private fun setTitle(sender: String?) {
        sender ?: return
        title?.text = sender
    }

    private fun setReplyMsg(msg: String?) {
        msg ?: return
        description?.text = MethodChecker.fromHtml(msg)
    }

    fun updateCloseButtonState(enableCloseButton: Boolean) {
        if (enableCloseButton) {
            closeBtn?.show()
            closeBtn?.setOnClickListener {
                clearReferTo()
                hide()
            }
        } else {
            closeBtn?.hide()
        }
    }

    private fun clearReferTo() {
        referredMsg = null
    }

    private fun getStrokeWidthSenderDimenRes(): Int {
        return R.dimen.unify_space_2
    }

    fun updateBackground(orientation : Int){
        if(orientation == LEFT_ORIENTATION)
            background = bgLeft
        else if (orientation == RIGHT_ORIENTATION)
            background = bgRight
        else
            background = bgRight
    }

    fun composeMsg(title : String?, msg : String?){
        setTitle(title)
        setReplyMsg(msg)
    }

    fun composeReplyData(
        referredMsg: BaseChatUiModel,
        text: CharSequence,
        enableCloseButton: Boolean = false
    ) {
        val parentReply = ParentReply(
            attachmentId = referredMsg.attachmentId,
            attachmentType = referredMsg.attachmentType,
            senderId = referredMsg.fromUid ?: "",
            replyTime = referredMsg.replyTime ?: "",
            mainText = referredMsg.message,
            subText = "",
            imageUrl = referredMsg.getReferredImageUrl(),
            localId = referredMsg.localId,
            source = "chat",
            replyId = referredMsg.replyId
        )
        bindParentReplyData(parentReply, null,referredMsg.message,referredMsg.from)
        updateCloseButtonState(enableCloseButton)
        show()
    }

    companion object {
        val LAYOUT = R.layout.text_reply_bubble
        val LEFT_ORIENTATION = 0
        val RIGHT_ORIENTATION = 1
    }

}