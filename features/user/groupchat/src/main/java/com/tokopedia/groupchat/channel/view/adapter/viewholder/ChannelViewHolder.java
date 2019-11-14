package com.tokopedia.groupchat.channel.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.channel.view.model.ChannelViewModel;
import com.tokopedia.groupchat.common.util.TextFormatter;

/**
 * @author by StevenFredian on 13/02/18.
 */


public class ChannelViewHolder extends AbstractViewHolder<ChannelViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.card_group_chat;
    private final ImageView image;
    private final ImageView profile;
    private final TextView title;
    private final TextView subtitle;
    private final TextView name;
    private final TextView participant;

    public ChannelViewHolder(View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.product_image);
        profile = itemView.findViewById(R.id.prof_pict);
        title = itemView.findViewById(com.tokopedia.design.R.id.title);
        subtitle = itemView.findViewById(R.id.subtitle);
        name = itemView.findViewById(R.id.name);
        participant = itemView.findViewById(R.id.participant);

    }

    @Override
    public void bind(ChannelViewModel element) {

        participant.setText(TextFormatter.format(String.valueOf(element.getTotalView())));
        name.setText(element.getAdminName());
        title.setText(element.getTitle());
        subtitle.setText(element.getDescription());

        ImageHandler.loadImage2(image, element.getImage(),
                com.tokopedia.design.R.drawable.loading_page);
        ImageHandler.loadImageCircle2(profile.getContext(), profile, element.getAdminPicture(),
                com.tokopedia.design.R.drawable.loading_page);

    }
}
