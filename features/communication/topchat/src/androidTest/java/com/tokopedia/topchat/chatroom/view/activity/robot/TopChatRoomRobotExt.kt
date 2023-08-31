package com.tokopedia.topchat.chatroom.view.activity.robot

import com.tokopedia.topchat.chatroom.view.activity.robot.broadcast.BroadcastRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.composearea.ComposeAreaRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.copytoclipboard.CopyToClipboardRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.header.HeaderRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.imageattachment.ImageAttachmentRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.longclickbubblemenu.LongClickBubbleMenuRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.msgbubble.MsgBubbleRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.previewattachment.PreviewAttachmentResult
import com.tokopedia.topchat.chatroom.view.activity.robot.previewattachment.PreviewAttachmentRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.previewattachment.ProductPreviewRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.product.BannedProductRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.replybubble.ReplyBubbleRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.srw.SrwRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.tickerreminder.TickerReminderRobot

fun generalRobot(func: GeneralRobot.() -> Unit) = GeneralRobot.apply(func)
fun composeAreaRobot(func: ComposeAreaRobot.() -> Unit) = ComposeAreaRobot.apply(func)
fun replyBubbleRobot(func: ReplyBubbleRobot.() -> Unit) = ReplyBubbleRobot.apply(func)
fun copyToClipboardRobot(func: CopyToClipboardRobot.() -> Unit) = CopyToClipboardRobot.apply(func)
fun productPreviewRobot(func: ProductPreviewRobot.() -> Unit) = ProductPreviewRobot.apply(func)
fun msgBubbleRobot(func: MsgBubbleRobot.() -> Unit) = MsgBubbleRobot.apply(func)
fun bannedProductRobot(func: BannedProductRobot.() -> Unit) = BannedProductRobot.apply(func)
fun broadcastRobot(func: BroadcastRobot.() -> Unit) = BroadcastRobot.apply(func)
fun headerRobot(func: HeaderRobot.() -> Unit) = HeaderRobot.apply(func)
fun tickerReminderRobot(func: TickerReminderRobot.() -> Unit) = TickerReminderRobot.apply(func)
fun previewAttachmentResult(func: PreviewAttachmentResult.() -> Unit) = PreviewAttachmentResult.apply(func)
fun productRobot(func: ProductRobot.() -> Unit) = ProductRobot.apply(func)
fun previewAttachmentRobot(func: PreviewAttachmentRobot.() -> Unit) = PreviewAttachmentRobot.apply(func)
fun srwRobot(func: SrwRobot.() -> Unit) = SrwRobot.apply(func)
fun productBundlingRobot(func: ProductBundlingRobot.() -> Unit) = ProductBundlingRobot.apply(func)
fun imageAttachmentRobot(func: ImageAttachmentRobot.() -> Unit) = ImageAttachmentRobot.apply(func)
fun longClickBubbleMenuRobot(func: LongClickBubbleMenuRobot.() -> Unit) = LongClickBubbleMenuRobot.apply(func)
