package com.tokopedia.topchat.chatlist.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.topchat.chatlist.viewmodel.fallback.FallbackAttachmentViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.message.MessageViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.SecurityInfoViewModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public interface ChatRoomTypeFactory {

    AbstractViewHolder createViewHolder(View view, int viewType);

    // NEW VERSION

    int type(FallbackAttachmentViewModel fallbackAttachmentViewModel);

    int type(MessageViewModel messageViewModel);

    //OTHER
    int type(SecurityInfoViewModel timeMachineChatModel);


}
