package com.tokopedia.feedplus.view.adapter.viewholder.kol;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.kol.WhitelistViewModel;

/**
 * @author by yfsx on 25/06/18.
 */
public class WhitelistViewHolder extends AbstractViewHolder<WhitelistViewModel> {

    private FeedPlus.View mainView;

    private ImageView ivAvatar;
    private TextView tvCaption;
    private ButtonCompat btnCreatePost;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_post_entry;

    public WhitelistViewHolder(View itemView, FeedPlus.View mainView) {
        super(itemView);
        this.mainView = mainView;
        this.ivAvatar = itemView.findViewById(R.id.ivAvatar);
        this.tvCaption = itemView.findViewById(R.id.tvCaption);
        this.btnCreatePost = itemView.findViewById(R.id.btnCreatePost);
    }

    @Override
    public void bind(WhitelistViewModel element) {
        initView(element);
        initViewListener();
    }

    private void initView(WhitelistViewModel model) {
        tvCaption.setText(MethodChecker.fromHtml(String.format("<b>%s,</b> %s",
                mainView.getUserSession().getShopName(),
                model.getWhitelist().getDesc())));

        ImageHandler.loadImageCircle2(
                ivAvatar.getContext(),
                ivAvatar,
                model.getWhitelist().getImage()
        );
    }

    private void initViewListener() {
        btnCreatePost.setOnClickListener(view ->
                mainView.onWhitelistClicked()
        );
    }
}
