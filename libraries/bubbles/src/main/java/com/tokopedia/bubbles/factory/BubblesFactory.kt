package com.tokopedia.bubbles.factory

import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import com.tokopedia.bubbles.data.model.BubbleHistoryItemModel
import com.tokopedia.bubbles.data.model.BubbleNotificationModel

interface BubblesFactory {

    fun setupBubble(
        builder: NotificationCompat.Builder,
        model: BubbleNotificationModel,
        bubbleBitmap: Bitmap?
    )
    fun updateShorcuts(
        historyModels: List<BubbleHistoryItemModel>,
        bubbleModel: BubbleNotificationModel,
        bubbleBitmap: Bitmap?
    )
    fun getBitmapWidth(): Int
    fun getBitmapHeight(): Int
    fun getBubbleHeight(): Int
}
