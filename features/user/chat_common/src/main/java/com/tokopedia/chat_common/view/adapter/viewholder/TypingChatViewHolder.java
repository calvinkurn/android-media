package com.tokopedia.chat_common.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.chat_common.data.TypingChatModel;
import com.tokopedia.chat_common.R;

/**
 * Created by stevenfredian on 10/26/17.
 */

public class TypingChatViewHolder extends AbstractViewHolder<TypingChatModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.typing_chat_layout;

            ImageView logo;

    public TypingChatViewHolder(View itemView) {
        super(itemView);
        logo = itemView.findViewById(R.id.image);
    }

    @Override
    public void bind(TypingChatModel element) {
        ImageHandler.loadGif(logo, R.raw.typing, R.raw.typing);

    }
}
