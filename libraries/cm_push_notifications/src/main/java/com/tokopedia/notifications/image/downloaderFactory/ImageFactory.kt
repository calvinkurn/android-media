package com.tokopedia.notifications.image.downloaderFactory

import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CMNotificationUtils
import com.tokopedia.notifications.image.downloaderFactory.factoryIml.*
import com.tokopedia.notifications.model.BaseNotificationModel

class ImageFactory (val baseNotificationModel: BaseNotificationModel){

    private fun getNotificationImageDownloader() : NotificationImageDownloader?{
        if(baseNotificationModel.endTime < System.currentTimeMillis())
            return null
        if(CMNotificationUtils.hasActionButton(baseNotificationModel))
            baseNotificationModel.type = CMConstant.NotificationType.ACTION_BUTTONS
        return when(baseNotificationModel.type){
            CMConstant.NotificationType.GENERAL -> GeneralNotiImageDownloader(baseNotificationModel)
            CMConstant.NotificationType.ACTION_BUTTONS -> ActionButtonImageDownloader(baseNotificationModel)
            CMConstant.NotificationType.BIG_IMAGE -> ImageNotificationImageDownloader(baseNotificationModel)
            CMConstant.NotificationType.PERSISTENT -> PersistentImageDownloader(baseNotificationModel)
            CMConstant.NotificationType.GRID_NOTIFICATION -> GridImageDownloader(baseNotificationModel)
            CMConstant.NotificationType.CAROUSEL_NOTIFICATION -> CarouselImageDownloader(baseNotificationModel)
            CMConstant.NotificationType.PRODUCT_NOTIIFICATION -> ProductImageDownloader(baseNotificationModel)
            CMConstant.NotificationType.VISUAL_NOTIIFICATION -> VisualImageDownloader(baseNotificationModel)
            else -> null
        }
    }

    companion object{
        fun provideNotificationImageDownloader(baseNotificationModel: BaseNotificationModel) : NotificationImageDownloader?{
            return ImageFactory(baseNotificationModel).getNotificationImageDownloader()
        }
    }
}

