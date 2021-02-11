package com.tokopedia.topchat.chattemplate.view.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chattemplate.view.listener.TemplateChatContract;
import com.tokopedia.topchat.chattemplate.view.viewmodel.TemplateChatModel;

/**
 * Created by stevenfredian on 11/29/17.
 */

public class ItemAddTemplateChatViewHolder extends AbstractViewHolder<TemplateChatModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_add_template_chat_settings;
    TemplateChatContract.View viewListener;
    View view;
    TextView textView;
    ImageView imageView;

    public ItemAddTemplateChatViewHolder(View itemView, TemplateChatContract.View viewListener) {
        super(itemView);
        view = itemView;
        imageView = itemView.findViewById(R.id.setting);
        textView = itemView.findViewById(R.id.caption);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final TemplateChatModel element) {
        if (element.size() >= 5) {
            imageView.setImageDrawable(MethodChecker.getDrawable(view.getContext(), R.drawable.ic_plus_grey));
            textView.setTextColor(MethodChecker.getColor(view.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_20));
        } else {
            imageView.setImageDrawable(MethodChecker.getDrawable(view.getContext(), R.drawable.ic_topchat_plus_green));
            textView.setTextColor(MethodChecker.getColor(view.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_G400));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onEnter(null, getAdapterPosition());
            }
        });
    }
}
