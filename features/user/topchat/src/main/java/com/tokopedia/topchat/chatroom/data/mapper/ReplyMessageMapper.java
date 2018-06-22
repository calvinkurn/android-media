package com.tokopedia.topchat.chatroom.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.domain.pojo.replyaction.ReplyActionData;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 8/31/17.as
 */

public class ReplyMessageMapper extends BaseChatAPICallMapper<ReplyActionData,ReplyActionData> {
    @Override
    ReplyActionData mappingToDomain(ReplyActionData data) {
        return data;
    }
}