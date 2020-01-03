package com.tokopedia.topchat.chattemplate.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import androidx.core.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chattemplate.view.listener.TemplateChatContract;
import com.tokopedia.topchat.chattemplate.view.viewmodel.TemplateChatModel;

/**
 * Created by stevenfredian on 11/29/17.
 */

public class ItemTemplateChatViewHolder extends AbstractViewHolder<TemplateChatModel> {

    @LayoutRes
    public static final int LAYOUT = com.tokopedia.topchat.R.layout.item_template_chat_settings;
    TemplateChatContract.View viewListener;
    TextView textHolder;
    private View setting;
    private View edit;

    public ItemTemplateChatViewHolder(View itemView, TemplateChatContract.View viewListener) {
        super(itemView);
        textHolder = itemView.findViewById(com.tokopedia.topchat.R.id.caption);
        setting = itemView.findViewById(com.tokopedia.topchat.R.id.setting);
        edit = itemView.findViewById(R.id.edit);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final TemplateChatModel element) {
        textHolder.setText(element.getMessage());
        final ItemTemplateChatViewHolder ini = this;
        setting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    viewListener.onDrag(ini);
                }
                return false;
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onEnter(element.getMessage(), getAdapterPosition());
            }
        });
    }
}
