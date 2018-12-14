package com.tokopedia.chat_common.domain.mapper

import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import javax.inject.Inject

/**
 * @author by nisie on 14/12/18.
 */
open class GetExistingChatMapper @Inject constructor() {

    open fun map(pojo: GetExistingChatPojo): ChatroomViewModel {

        //TODO MAPPING
        return ChatroomViewModel()

    }
}