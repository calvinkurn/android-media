package com.tokopedia.topchat.chatroom.view.presenter

import androidx.annotation.StringRes
import androidx.collection.ArrayMap
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.presenter.BaseChatPresenter
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.topchat.chatroom.data.activityresult.UpdateProductStockResult
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer
import com.tokopedia.topchat.chatroom.view.listener.TopChatContract
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import rx.Subscriber
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