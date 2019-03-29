package com.tokopedia.topchat.chattemplate.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.topchat.chattemplate.view.viewmodel.TemplateChatModel;
import com.tokopedia.topchat.chattemplate.view.viewmodel.TemplateChatModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public interface TemplateChatTypeFactory {

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(TemplateChatModel templateChatModel);
}
