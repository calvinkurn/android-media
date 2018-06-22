package com.tokopedia.topchat.chatroom.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.domain.pojo.SendMessagePojo;
import com.tokopedia.topchat.chatroom.view.viewmodel.SendMessageViewModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/25/17.
 */

public class SendMessageMapper extends BaseChatAPICallMapper<SendMessagePojo,SendMessageViewModel> {
    @Override
    SendMessageViewModel mappingToDomain(SendMessagePojo data) {
        return new SendMessageViewModel(data.isSuccess());
    }
}
