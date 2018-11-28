package com.tokopedia.chat_common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import rx.functions.Action1
import rx.functions.Func1
import java.util.concurrent.TimeUnit

//import com.tokopedia.chat_common.R
/**
 * @author by nisie on 23/11/18.
 */
open class BaseChatFragment : BaseDaggerFragment() {

    override fun getScreenName(): String {

    }

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val rootView = inflater.inflate(R.layout.fragment_chat_room, container, false)
//        activity!!.window.setSoftInputMode(WindowManager.LayoutParams
//                .SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView();
    }

    private fun prepareView() {
//        recyclerView.setHasFixedSize(true)

    }

    private fun initWebsocket() {

    }

    private fun initListener() {

//        replyIsTyping = replyWatcher.map(Func1<String, Boolean> { s -> s.length > 0 })
//
//        replyIsTyping.subscribe(Action1<Boolean> { isNotEmpty ->
//            if (isNotEmpty!!) {
//                presenter.setIsTyping(arguments!!.getString(ChatRoomActivity
//                        .PARAM_MESSAGE_ID))
//                if (needCreateWebSocket()) {
//                    maximize.setVisibility(if (isChatBot) View.GONE else View.VISIBLE)
//                }
//                pickerButton.setVisibility(if (isChatBot) View.VISIBLE else View.GONE)
//                attachButton.setVisibility(View.GONE)
//            }
//        })
//
//        replyIsTyping.debounce(2, TimeUnit.SECONDS)
//                .subscribe(Action1<Boolean> {
//                    try {
//                        presenter.stopTyping(arguments!!.getString(ChatRoomActivity
//                                .PARAM_MESSAGE_ID))
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                })
//
//        if (needCreateWebSocket()) {
//            sendButton.setOnClickListener(generateSendClickListener())
//        } else {
//            sendButton.setOnClickListener(getSendInitMessage())
//        }
//
//        setPickerButton()
//
//        maximize.setOnClickListener(View.OnClickListener {
//            maximize.setVisibility(View.GONE)
//            setPickerButton()
//        })
//
//        pickerButton.setOnClickListener(View.OnClickListener {
//            replyColumn.clearFocus()
//
//            UnifyTracking.eventAttachment(TopChatAnalytics.Category.CHAT_DETAIL,
//                    TopChatAnalytics.Action.CHAT_DETAIL_ATTACH,
//                    TopChatAnalytics.Name.CHAT_DETAIL)
//
//            val builder = ImagePickerBuilder(getString(R.string.choose_image),
//                    intArrayOf(ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_CAMERA), GalleryType.IMAGE_ONLY, ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
//                    ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, null, true, null, null)
//            val intent = ImagePickerActivity.getIntent(context, builder)
//            startActivityForResult(intent, REQUEST_CODE_CHAT_IMAGE)
//        })
//
//        attachButton.setOnClickListener(View.OnClickListener {
//            UnifyTracking.eventInsertAttachment(TopChatAnalytics.Category.CHAT_DETAIL,
//                    TopChatAnalytics.Action.CHAT_DETAIL_INSERT,
//                    TopChatAnalytics.Name.CHAT_DETAIL)
//            presenter.getAttachProductDialog(
//                    arguments!!.getString(ChatRoomActivity
//                            .PARAM_SENDER_ID, ""),
//                    arguments!!.getString(ChatRoomActivity.PARAM_SENDER_NAME, ""),
//                    arguments!!.getString(ChatRoomActivity.PARAM_SENDER_ROLE, "")
//            )
//        })
    }

}