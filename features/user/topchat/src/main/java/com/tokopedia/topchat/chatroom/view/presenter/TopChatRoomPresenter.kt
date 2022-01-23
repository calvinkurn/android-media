package com.tokopedia.topchat.chatroom.view.presenter

import androidx.annotation.StringRes
import androidx.collection.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.data.BaseChatUiModel.Builder.Companion.generateCurrentReplyTime
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Mode.MODE_API
import com.tokopedia.chat_common.data.WebsocketEvent.Mode.MODE_WEBSOCKET
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.attachcommon.preview.ProductPreview
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_DELETE_MSG
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.chat_common.network.ChatUrl.Companion.CHAT_WEBSOCKET_DOMAIN
import com.tokopedia.chat_common.presenter.BaseChatPresenter
import com.tokopedia.chat_common.util.IdentifierUtil
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.device.info.DeviceInfo
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.data.UploadImageDummy
import com.tokopedia.topchat.chatroom.data.activityresult.UpdateProductStockResult
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaButtonAttachment
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer
import com.tokopedia.topchat.chatroom.view.listener.TopChatContract
import com.tokopedia.topchat.chatroom.view.uimodel.StickerUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.InvoicePreviewUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateUiModel
import com.tokopedia.topchat.common.data.Resource
import com.tokopedia.topchat.common.mapper.ImageUploadMapper
import com.tokopedia.topchat.common.util.AddressUtil
import com.tokopedia.topchat.common.util.ImageUtil
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.websocket.RxWebSocketUtil
import com.tokopedia.websocket.WebSocketResponse
import com.tokopedia.websocket.WebSocketSubscriber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.Interceptor
import okhttp3.WebSocket
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author : Steven 11/12/18
 */

open class TopChatRoomPresenter @Inject constructor(
    userSession: UserSessionInterface,
    private var topChatRoomWebSocketMessageMapper: TopChatRoomWebSocketMessageMapper,
    private var getTemplateChatRoomUseCase: GetTemplateChatRoomUseCase,
    private val dispatchers: CoroutineDispatchers
) : BaseChatPresenter<TopChatContract.View>(userSession, topChatRoomWebSocketMessageMapper),
    TopChatContract.Presenter, CoroutineScope {

    val onGoingStockUpdate: ArrayMap<String, UpdateProductStockResult> = ArrayMap()

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    override fun destroyWebSocket() { }

    fun getTemplate(isSeller: Boolean) {
        getTemplateChatRoomUseCase.execute(
            GetTemplateChatRoomUseCase.generateParam(isSeller),
            object : Subscriber<GetTemplateUiModel>() {
                override fun onNext(templateUiModel: GetTemplateUiModel?) {
                    val templateList = arrayListOf<Visitable<Any>>()
                    if (templateUiModel != null) {
                        if (templateUiModel.isEnabled) {
                            templateUiModel.listTemplate?.let {
                                templateList.addAll(it)
                            }
                        }
                    }
                    view?.onSuccessGetTemplate(templateList)
                }

                override fun onCompleted() {

                }

                override fun onError(e: Throwable?) {
                    view?.onErrorGetTemplate()
                }

            }
        )
    }

    override fun isUploading(): Boolean {
        return false
    }

    override fun showErrorSnackbar(@StringRes stringId: Int) {}

    override fun detachView() {
        getTemplateChatRoomUseCase.unsubscribe()
        super.detachView()
    }

    override fun addOngoingUpdateProductStock(
        productId: String,
        product: ProductAttachmentUiModel, adapterPosition: Int,
        parentMetaData: SingleProductAttachmentContainer.ParentViewHolderMetaData?
    ) {
        val result = UpdateProductStockResult(product, adapterPosition, parentMetaData)
        onGoingStockUpdate[productId] = result
    }

    override fun clearText() {}

    override fun sendMessageWithWebsocket(
        messageId: String, sendMessage: String,
        startTime: String, opponentId: String
    ) { }

    override fun sendMessageWithApi(
        messageId: String, sendMessage: String,
        startTime: String
    ) {}

    companion object {
        const val ENABLE_UPLOAD_IMAGE_SERVICE = "android_enable_topchat_upload_image_service"
        private val PROBLEMATIC_DEVICE = listOf("iris88", "iris88_lite", "lenovo k9")
    }
}