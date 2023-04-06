package com.tokopedia.tokochat.view.chatroom.uimodel

import android.content.Context
import android.view.View
import com.gojek.conversations.extensions.ConversationsContext
import com.gojek.conversations.extensions.ConversationsExtension
import com.gojek.conversations.extensions.ConversationsExtensionWidget
import com.gojek.conversations.extensions.ExtensionMessage

class TokoChatImageAttachmentExtension : ConversationsExtension {

    override var conversationsContext: ConversationsContext
        get() = TODO("Not yet implemented")
        set(value) {}

    // No need for this, we create our own view
    override fun getExtensionMessageView(
        extensionMessage: ExtensionMessage,
        context: Context
    ): View? {
        return null
    }

    // Dummy view in here
    override fun getTransientExtensionMessageView(
        extensionMessage: ExtensionMessage,
        context: Context
    ): View? {
        return super.getTransientExtensionMessageView(extensionMessage, context)
    }

    // Handle retry, we need to retry manually
    override fun handleRetry(extensionMessage: ExtensionMessage) {
        super.handleRetry(extensionMessage)
    }

    // Show retry when fail
    override fun shouldShowRetry(extensionMessage: ExtensionMessage): Boolean {
        return super.shouldShowRetry(extensionMessage)
    }

    // Handle cancel
    override fun handleCancel(extensionMessage: ExtensionMessage) {
        super.handleCancel(extensionMessage)
    }

    override fun showDisplayExtensionMessage(
        conversationsContext: ConversationsContext,
        extensionMessage: ExtensionMessage
    ): Boolean {
        return false
    }

    // No need
    override fun getExtensionWidgets(
        conversationsContext: ConversationsContext,
        context: Context
    ): List<ConversationsExtensionWidget>? = null

    // No need
    override fun onWidgetClicked(
        widget: ConversationsExtensionWidget,
        conversationsContext: ConversationsContext,
        context: Context
    ) {}
}
