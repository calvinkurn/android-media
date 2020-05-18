package com.tokopedia.topchat

import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import com.tokopedia.topchat.common.network.TopchatCacheManager
import java.lang.reflect.Type

class TopchatCacheManagerStub: TopchatCacheManager {
    override fun saveCache(key: String, obj: Any) {

    }

    override fun <T> loadCache(key: String, type: Type): T {
        return StickerResponse() as T
    }

    override fun saveState(stateCacheKey: String, isSuccess: Boolean) {

    }

    override fun getPreviousState(stateCacheKey: String): Boolean {
        return true
    }
}