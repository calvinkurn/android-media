package com.tokopedia.topchat.chatroom.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.domain.pojo.existingchat.ExistingChatPojo;
import com.tokopedia.topchat.chatroom.domain.pojo.reply.ReplyData;
import com.tokopedia.topchat.chatroom.view.viewmodel.ChatRoomViewModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by Hendri on 07/06/18.
 */
public class GetExistingChatMapper extends BaseChatAPICallMapper<ExistingChatPojo,ExistingChatPojo> {

    @Override
    ExistingChatPojo mappingToDomain(ExistingChatPojo data) {
        return data;
    }
}
