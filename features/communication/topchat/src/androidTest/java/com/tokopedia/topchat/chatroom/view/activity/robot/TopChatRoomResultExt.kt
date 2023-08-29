package com.tokopedia.topchat.chatroom.view.activity.robot

import com.tokopedia.topchat.chatroom.view.activity.robot.composearea.ComposeAreaResult
import com.tokopedia.topchat.chatroom.view.activity.robot.copytoclipboard.CopyToClipboardResult
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralResult
import com.tokopedia.topchat.chatroom.view.activity.robot.msgbubble.MsgBubbleResult
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductPreviewResult
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductResult
import com.tokopedia.topchat.chatroom.view.activity.robot.replybubble.ReplyBubbleResult
import com.tokopedia.topchat.chatroom.view.activity.robot.shippinglocation.ShippingLocationResult
import com.tokopedia.topchat.chatroom.view.activity.robot.srw.SrwResult

fun generalResult(func: GeneralResult.() -> Unit) = GeneralResult.apply(func)
fun composeAreaResult(func: ComposeAreaResult.() -> Unit) = ComposeAreaResult.apply(func)
fun replyBubbleResult(func: ReplyBubbleResult.() -> Unit) = ReplyBubbleResult.apply(func)
fun productResult(func: ProductResult.() -> Unit) = ProductResult.apply(func)
fun copyToClipboardResult(func: CopyToClipboardResult.() -> Unit) = CopyToClipboardResult.apply(func)
fun productPreviewResult(func: ProductPreviewResult.() -> Unit) = ProductPreviewResult.apply(func)
fun srwResult(func: SrwResult.() -> Unit) = SrwResult.apply(func)
fun shippingLocationResult(func: ShippingLocationResult.() -> Unit) = ShippingLocationResult.apply(func)
fun msgBubbleResult(func: MsgBubbleResult.() -> Unit) = MsgBubbleResult.apply(func)
