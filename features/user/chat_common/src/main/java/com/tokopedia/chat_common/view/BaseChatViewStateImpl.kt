package com.tokopedia.chat_common.view

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.EventsWatcher
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.BaseChatAdapter
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.chat_common.view.widget.AttachmentMenuRecyclerView
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author : Steven 29/11/18
 */
open class BaseChatViewStateImpl(
        @NonNull open val view: View,
        open val toolbar: Toolbar,
        private val typingListener: TypingListener,
        private val attachmentMenuListener: AttachmentMenu.AttachmentMenuListener
) : BaseChatViewState, ViewTreeObserver.OnGlobalLayoutListener {

    protected lateinit var rootView: ViewGroup
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var mainLoading: ProgressBar
    protected lateinit var replyEditText: EditText
    protected lateinit var replyBox: RelativeLayout
    protected lateinit var actionBox: LinearLayout
    protected lateinit var sendButton: View
    protected lateinit var notifier: View
    protected lateinit var chatMenuButton: ImageView
    protected var attachmentMenu: AttachmentMenuRecyclerView? = null
    protected var attachmentMenuContainer: FrameLayout? = null

    protected lateinit var replyWatcher: Observable<String>
    protected lateinit var replyIsTyping: Observable<Boolean>
    var isTyping: Boolean = false

    private val keyboardOffset = 100

    override fun initView() {
        rootView = view.findViewById(getRootViewId())
        recyclerView = view.findViewById(getRecyclerViewId())
        mainLoading = view.findViewById(getProgressId())
        replyEditText = view.findViewById(getNewCommentId())
        replyBox = view.findViewById(getReplyBoxId())
        actionBox = view.findViewById(getActionBoxId())
        sendButton = view.findViewById(getSendButtonId())
        notifier = view.findViewById(getNotifierId())
        chatMenuButton = view.findViewById(getChatMenuId())
        attachmentMenu = view.findViewById(getAttachmentMenuId())
        attachmentMenuContainer = view.findViewById(getAttachmentMenuContainer())

        (recyclerView.layoutManager as LinearLayoutManager).stackFromEnd = false
        (recyclerView.layoutManager as LinearLayoutManager).reverseLayout = true
        replyEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                scrollDownWhenInBottom()
            }
        }
        replyWatcher = EventsWatcher.text(replyEditText)

        replyIsTyping = replyWatcher.map { t -> t.isNotEmpty() }

        val onError = Action1<Throwable> { it.printStackTrace() }

        replyIsTyping.subscribe(Action1 {
            if (it && !isTyping) {
                typingListener.onStartTyping()
                isTyping = true
            }
        }, onError)

        val onChatDeBounceSubscriber = Action1<Boolean> {
            typingListener.onStopTyping()
            isTyping = false
        }
        replyIsTyping.debounce(2, TimeUnit.SECONDS)
                .skip(1)
                .subscribe(onChatDeBounceSubscriber, onError)


        rootView.viewTreeObserver.addOnGlobalLayoutListener(this)
        setupChatMenu()
    }

    protected open fun setupChatMenu() {
        attachmentMenu?.setAttachmentMenuListener(attachmentMenuListener)
        attachmentMenu?.container = attachmentMenuContainer
        chatMenuButton.setOnClickListener {
            attachmentMenu?.toggle()
        }
    }


    override fun updateHeader(chatroomViewModel: ChatroomViewModel, onToolbarClicked: () -> Unit) {
        val title = toolbar.findViewById<TextView>(R.id.title)
        title.text = getInterlocutorName(chatroomViewModel.getHeaderName())

        setLabel(chatroomViewModel.headerModel.label)

        val avatar = toolbar.findViewById<ImageView>(R.id.user_avatar)
        ImageHandler.loadImageCircle2(avatar.context, avatar, chatroomViewModel.headerModel.image,
                R.drawable.ic_default_avatar)

        val onlineDesc = toolbar.findViewById<TextView>(R.id.subtitle)
        val onlineStatus = toolbar.findViewById<ImageView>(R.id.online_status)

        if (chatroomViewModel.headerModel.isOnline) {
            onlineStatus.setImageResource(getOnlineIndicatorResource())
            onlineDesc.text = view.context.getString(R.string.online)
        } else
            onlineStatus.setImageResource(getOfflineIndicatorResource())

        title.setOnClickListener { onToolbarClicked() }
        avatar.setOnClickListener { onToolbarClicked() }

    }

    @DrawableRes
    open fun getOfflineIndicatorResource() = R.drawable.status_indicator_offline

    @DrawableRes
    open fun getOnlineIndicatorResource() = R.drawable.status_indicator_online

    override fun onShowStartTyping() {
        getAdapter().showTyping()
        scrollDownWhenInBottom()
    }

    override fun onShowStopTyping() {
        getAdapter().removeTyping()
    }

    override fun onReceiveMessageEvent(visitable: Visitable<*>) {
        getAdapter().addElement(visitable)
        scrollDownWhenInBottom()
    }

    override fun onSendingMessage(messageId: String, userId: String, name: String, sendMessage: String, startTime: String) {
        getAdapter().addElement(
                MessageViewModel(
                        messageId,
                        userId,
                        name,
                        startTime,
                        sendMessage
                )
        )
    }

    override fun removeDummyIfExist(successVisitable: Visitable<*>) {
        if (successVisitable is SendableViewModel) {
            getAdapter().removeDummy(successVisitable)
        }
    }

    override fun removeMessageOnReplyBox() {
        replyEditText.setText("")
    }

    private fun setLabel(labelText: String) {
        val ADMIN_TAG = "Administrator"
        val SELLER_TAG = "Penjual"
        val OFFICIAL_TAG = "Official"

        val label = toolbar.findViewById<TextView>(R.id.label)
        label.text = labelText

        when (labelText) {
            SELLER_TAG -> {
                label.setBackgroundResource(R.drawable.topchat_seller_label)
                label.setTextColor(MethodChecker.getColor(label.context, R.color.medium_green))
                label.visibility = View.VISIBLE
            }
            ADMIN_TAG -> {
                label.setBackgroundResource(R.drawable.topchat_admin_label)
                label.setTextColor(MethodChecker.getColor(label.context, R.color.topchat_admin_label_text_color))
                label.visibility = View.VISIBLE
            }
            OFFICIAL_TAG -> {
                label.setBackgroundResource(R.drawable.topchat_admin_label)
                label.setTextColor(MethodChecker.getColor(label.context, R.color.topchat_admin_label_text_color))
                label.visibility = View.VISIBLE
            }
            else -> label.visibility = View.GONE
        }
    }

    open fun scrollDownWhenInBottom() {
        if (checkLastCompletelyVisibleItemIsFirst()) {
            scrollToBottom()
        }
    }

    open fun scrollToBottom() {
        val onNext = Action1<Long> { recyclerView.scrollToPosition(0) }
        val onError = Action1<Throwable> { it.printStackTrace() }
        Observable.timer(250, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError)
    }

    open fun checkLastCompletelyVisibleItemIsFirst(): Boolean {
        return (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() < 2
    }


    fun showLoading() {
        mainLoading.visibility = View.VISIBLE
    }

    fun hideLoading() {
        mainLoading.visibility = View.GONE
    }

    fun setAdapter(adapter: BaseChatAdapter) {
        recyclerView.adapter = adapter
    }

    open fun getAdapter(): BaseChatAdapter {
        return recyclerView.adapter as BaseChatAdapter
    }

    fun addList(listChat: ArrayList<Visitable<*>>) {
        getAdapter().addList(listChat)
    }

    fun getList(): List<Visitable<*>> {
        return (recyclerView.adapter as BaseChatAdapter).getList()
    }

    fun clearEditText() {
        replyEditText.setText("")
    }

    protected fun showReplyBox(replyable: Boolean) {
        val visibilityState = if (replyable) View.VISIBLE else View.GONE
        actionBox.visibility = visibilityState
        replyBox.visibility = visibilityState
    }


    override fun onReceiveRead() {
        getAdapter().changeReadStatus()
    }

    override fun onGlobalLayout() {
        val screenHeight = getScreenHeight()
        val windowRect = Rect().apply {
            rootView.getWindowVisibleDisplayFrame(this)
        }
        val windowHeight = windowRect.bottom - windowRect.top
        val statusBarHeight = getStatusBarHeight()

        val heightDifference = screenHeight - windowHeight - statusBarHeight

        if (heightDifference > keyboardOffset) {
            onKeyboardOpened()
        } else {
            onKeyboardClosed()
        }
    }

    override fun onKeyboardOpened() {
        hideChatMenu()
    }

    override fun onKeyboardClosed() {
        showChatMenu()
    }

    override fun hideChatMenu() {
        attachmentMenu?.isKeyboardOpened = true
        attachmentMenu?.hideMenu()
    }

    override fun showChatMenu() {
        attachmentMenu?.isKeyboardOpened = false
        attachmentMenu?.let {
            if (it.showDelayed) {
                it.showDelayed()
            }
        }
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    private fun getStatusBarHeight(): Int {
        var height = 0
        val resourceId = view.context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            height = view.context.resources.getDimensionPixelSize(resourceId)
        }
        return height
    }

    override fun clear() {
        rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    override fun isAttachmentMenuVisible(): Boolean {
        return attachmentMenu?.isVisible ?: false
    }

    override fun hideAttachmentMenu() {
        attachmentMenu?.hideMenu()
    }

    override fun showErrorWebSocket(isWebSocketError: Boolean) {}

    open fun getInterlocutorName(headerName: CharSequence): CharSequence = headerName
    open fun getRecyclerViewId() = R.id.recycler_view
    open fun getProgressId() = R.id.progress
    open fun getNewCommentId() = R.id.new_comment
    open fun getReplyBoxId() = R.id.reply_box
    open fun getActionBoxId() = R.id.add_comment_area
    open fun getSendButtonId() = R.id.send_but
    open fun getNotifierId() = R.id.notifier
    open fun getChatMenuId() = R.id.iv_chat_menu
    open fun getAttachmentMenuId() = R.id.rv_attachment_menu
    open fun getRootViewId() = R.id.main
    open fun getAttachmentMenuContainer(): Int = R.id.rv_attachment_menu_container

}
