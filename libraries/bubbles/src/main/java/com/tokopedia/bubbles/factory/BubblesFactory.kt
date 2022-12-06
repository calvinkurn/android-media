package com.tokopedia.bubbles.factory

import androidx.core.app.NotificationCompat
import com.tokopedia.bubbles.data.model.BubbleHistoryItemModel
import com.tokopedia.bubbles.data.model.BubbleNotificationModel

interface BubblesFactory {

    fun setupBubble(builder: NotificationCompat.Builder,
                    model: BubbleNotificationModel)
    fun updateShorcuts(historyModels: List<BubbleHistoryItemModel>, bubbleModel: BubbleNotificationModel)
    fun getBitmapWidth(): Int
    fun getBitmapHeight(): Int
    fun getBubbleHeight(): Int

}
