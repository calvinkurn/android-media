package com.tokopedia.topchat.chatlist.adapter.viewholder.chatlist;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatlist.viewmodel.EmptyChatModel;

/**
 * Created by stevenfredian on 10/26/17.
 */

public class EmptyChatListViewHolder extends AbstractViewHolder<EmptyChatModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.empty_chat_layout;

    Context context;
    ImageView logo;
    TextView title;
    TextView subtitle;

    public EmptyChatListViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;

        logo = (ImageView) itemView.findViewById(R.id.image);
        title = (TextView) itemView.findViewById(R.id.title);
        subtitle = (TextView) itemView.findViewById(R.id.subtitle);
    }

    @Override
    public void bind(EmptyChatModel element) {
        if (element.getType() != EmptyChatModel.SEARCH) {
            logo.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.empty_chat));
            title.setText(context.getString(R.string.no_existing_chat));
            subtitle.setVisibility(View.VISIBLE);
            subtitle.setText(context.getString(R.string.please_try_chat));
        } else {
            logo.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_empty_search));
            subtitle.setText(context.getString(R.string.no_existing_chat_user));
            title.setVisibility(View.GONE);

        }
    }
}
