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
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class ReplyBubbleAreaMessage : ConstraintLayout {

    private var title: TextView? = null
    private var description: TextView? = null
    private var closeBtn: ImageView? = null
    private var container: ConstraintLayout? = null
    var referredMsg: ParentReply? = null
    var listener: Listener? = null
        private set

    interface Listener {
        fun getUserName(senderId: String): String
        fun goToBubble(parentReply: ParentReply)
        fun showReplyOption()
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

    //todo review this
    private val bgSender = ViewUtil.generateBackgroundWithShadow(
        view =this,
        backgroundColor =  com.tokopedia.unifyprinciples.R.color.Unify_G200,
        topLeftRadius =  com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        topRightRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        bottomLeftRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        bottomRightRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
        elevation = R.dimen.dp_chatbot_2,
        shadowRadius =  R.dimen.dp_chatbot_1,
        shadowGravity = Gravity.CENTER,
        strokeColor = com.tokopedia.unifyprinciples.R.color.Unify_G200,
        strokeWidth = getStrokeWidthSenderDimenRes()
    )
    //todo review this
    private val bgOpposite = ViewUtil.generateBackgroundWithShadow(
        view = this,
        backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_N0,
        topLeftRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        topRightRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        bottomLeftRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        bottomRightRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
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
        initOnClickListener()
    }

    private fun initLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initViewBinding() {
        title = findViewById(R.id.reply_from)
        description = findViewById(R.id.reply_message)
        closeBtn = findViewById(R.id.close_btn)
        container = findViewById(R.id.container)
    }

    private fun initOnClickListener(){
        closeBtn?.setOnClickListener {
         //   container?.hide()
        }
    }

    //TODO bind background
    fun bindReplyData(uiModel : BaseChatUiModel){
        val parentReply = uiModel.parentReply
        if (parentReply!=null && !uiModel.isDeleted()){
            bindParentReplyData(parentReply,uiModel.replyId)
            updateCloseButtonState(false)
            show()
        }else{
            hide()
        }
    }

    private fun bindParentReplyData(parentReply: ParentReply, replyId: String) {
        referTo(parentReply)
        setTitle(parentReply.senderId)
        setReplyMsg(parentReply.mainText)
        //TODO fix bindClick
    //    bindClick(parentReply, childReplyId)
    }

    private fun referTo(parentReply: ParentReply) {
        referredMsg = parentReply
    }

    private fun setTitle(senderId: String?) {
        senderId ?: return
        val senderName = listener?.getUserName(senderId)
        this.title?.text = MethodChecker.fromHtml(senderName)
    }

    private fun setReplyMsg(msg: String) {
        description?.text = MethodChecker.fromHtml(msg)
    }

    private fun updateCloseButtonState(enableCloseButton: Boolean) {
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

    fun getStrokeWidthSenderDimenRes(): Int {
        return R.dimen.dp_chatbot_3
    }

    fun updateBackground(isLeft : Boolean){
        if(isLeft)
            background = bgOpposite
        else
            background = bgSender
    }

    companion object {
        val LAYOUT = R.layout.text_reply_bubble
    }

}