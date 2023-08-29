package com.tokopedia.topchat.chatroom.view.activity.robot

import com.tokopedia.topchat.chatroom.view.activity.robot.broadcast.BroadcastRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.composearea.ComposeAreaRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.copytoclipboard.CopyToClipboardRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.header.HeaderRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.msgbubble.MsgBubbleRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.previewattachment.PreviewAttachmentResult
import com.tokopedia.topchat.chatroom.view.activity.robot.product.BannedProductRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductPreviewRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.replybubble.ReplyBubbleRobot

fun generalRobot(func: GeneralRobot.() -> Unit) = GeneralRobot.apply(func)
fun composeAreaRobot(func: ComposeAreaRobot.() -> Unit) = ComposeAreaRobot.apply(func)
fun replyBubbleRobot(func: ReplyBubbleRobot.() -> Unit) = ReplyBubbleRobot.apply(func)
fun copyToClipboardRobot(func: CopyToClipboardRobot.() -> Unit) = CopyToClipboardRobot.apply(func)
fun productPreviewRobot(func: ProductPreviewRobot.() -> Unit) = ProductPreviewRobot.apply(func)
fun msgBubbleRobot(func: MsgBubbleRobot.() -> Unit) = MsgBubbleRobot.apply(func)
fun bannedProductRobot(func: BannedProductRobot.() -> Unit) = BannedProductRobot.apply(func)
fun broadcastRobot(func: BroadcastRobot.() -> Unit) = BroadcastRobot.apply(func)
fun headerRobot(func: HeaderRobot.() -> Unit) = HeaderRobot.apply(func)
fun previewAttachmentResult(func: PreviewAttachmentResult.() -> Unit) = PreviewAttachmentResult.apply(func)
