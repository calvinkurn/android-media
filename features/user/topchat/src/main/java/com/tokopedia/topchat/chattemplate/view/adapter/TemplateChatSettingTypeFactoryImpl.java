package com.tokopedia.topchat.chattemplate.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.topchat.chattemplate.view.fragment.TemplateChatFragment;
import com.tokopedia.topchat.chattemplate.view.listener.TemplateChatContract;
import com.tokopedia.topchat.chattemplate.view.adapter.viewholder.ItemAddTemplateChatViewHolder;
import com.tokopedia.topchat.chattemplate.view.adapter.viewholder.ItemTemplateChatViewHolder;
import com.tokopedia.topchat.chattemplate.view.viewmodel.TemplateChatModel;
import com.tokopedia.topchat.chattemplate.view.adapter.viewholder.ItemAddTemplateChatViewHolder;
import com.tokopedia.topchat.chattemplate.view.adapter.viewholder.ItemTemplateChatViewHolder;
import com.tokopedia.topchat.chattemplate.view.fragment.TemplateChatFragment;
import com.tokopedia.topchat.chattemplate.view.listener.TemplateChatContract;
import com.tokopedia.topchat.chattemplate.view.viewmodel.TemplateChatModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class TemplateChatSettingTypeFactoryImpl extends BaseAdapterTypeFactory implements TemplateChatSettingTypeFactory {

    TemplateChatContract.View viewListener;

    public TemplateChatSettingTypeFactoryImpl(TemplateChatContract.View listener) {
        this.viewListener = listener;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == ItemTemplateChatViewHolder.LAYOUT) {
            viewHolder = new ItemTemplateChatViewHolder(view, viewListener);
        } else if (type == ItemAddTemplateChatViewHolder.LAYOUT) {
            viewHolder = new ItemAddTemplateChatViewHolder(view, viewListener);
        } else {
            return super.createViewHolder(view, type);
        }
        return viewHolder;
    }

    @Override
    public int type(TemplateChatModel templateChatModel) {
        if (templateChatModel.isIcon()) {
            return ItemAddTemplateChatViewHolder.LAYOUT;
        }
        return ItemTemplateChatViewHolder.LAYOUT;
    }

}
