package com.tokopedia.topchat.chattemplate.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chattemplate.view.listener.ChatTemplateListener;
import com.tokopedia.topchat.chattemplate.view.viewmodel.TemplateChatModel;
import com.tokopedia.unifycomponents.ChipsUnify;

/**
 * Created by stevenfredian on 11/29/17.
 */

public class TemplateChatViewHolder extends AbstractViewHolder<TemplateChatModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_template_chat_layout;

    ChatTemplateListener viewListener;
    ChipsUnify textHolder;

    public TemplateChatViewHolder(View itemView, ChatTemplateListener viewListener) {
        super(itemView);
        textHolder = itemView.findViewById(R.id.chipsText);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final TemplateChatModel element) {
        textHolder.setChipText(element.getMessage());
        textHolder.setOnClickListener(view ->
                viewListener.addTemplateString(element.getMessage())
        );
    }
}
