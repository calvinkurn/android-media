package com.tokopedia.notifications.model

enum class NotificationStatus(val statusInt: Int) {
    PENDING(0), ACTIVE(1),
    DELETE(3), COMPLETED(4)
}

enum class NotificationMode(val modeInt: Int){
    POST_NOW(0), OFFLINE(1)
}