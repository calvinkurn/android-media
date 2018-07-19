package com.tokopedia.feedplus.view.adapter.viewholder.kol;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.kol.WhitelistViewModel;

/**
 * @author by yfsx on 25/06/18.
 */
public class WhitelistViewHolder extends AbstractViewHolder<WhitelistViewModel> {

    private FeedPlus.View mainView;

    private CardView cardView;
    private ImageView ivPhoto;
    private TextView tvTitle, tvDesc;

    private UserSession userSession;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_post_entry;

    public WhitelistViewHolder(View itemView, FeedPlus.View mainView) {
        super(itemView);
        this.mainView = mainView;
        this.cardView = itemView.findViewById(R.id.card_view);
        this.ivPhoto = itemView.findViewById(R.id.iv_photo);
        this.tvTitle = itemView.findViewById(R.id.tv_title);
        this.tvDesc = itemView.findViewById(R.id.tv_desc);
        userSession = mainView.getUserSession();
    }

    @Override
    public void bind(WhitelistViewModel element) {
        initView(element);
        initViewListener(element);
    }

    private void initView(WhitelistViewModel model) {
        tvTitle.setText(!TextUtils.isEmpty(model.getWhitelist().getTitle()) ?
                formatName(model.getWhitelist().getTitle(),
                        model.getWhitelist().getTitleIdentifier()) :
                "");
        tvDesc.setText(!TextUtils.isEmpty(model.getWhitelist().getDesc()) ?
                model.getWhitelist().getDesc() :
                "");
        ImageHandler.LoadImage(ivPhoto, userSession.getProfilePicture());
    }

    private void initViewListener(final WhitelistViewModel model) {
        cardView.setOnClickListener(view -> mainView.onWhitelistClicked(model.getWhitelist().getUrl()));
    }

    private String formatName(String nameTemp, String identifier) {
        return nameTemp.replace(identifier, userSession.getName());
    }
}
