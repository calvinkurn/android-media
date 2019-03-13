package com.tokopedia.feedplus.view.adapter.viewholder.kol;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.kol.WhitelistViewModel;

/**
 * @author by yfsx on 25/06/18.
 */
public class WhitelistViewHolder extends AbstractViewHolder<WhitelistViewModel> {

    private FeedPlus.View mainView;

    private ImageView ivPhoto;
    private TextView tvTitle, tvDescription;
    private ButtonCompat btnSeeProfile, btnCreateContent;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_post_entry;

    private static final String SHOP_ID = "{shop_id}";
    private static final String USER_ID = "{user_id}";

    public WhitelistViewHolder(View itemView, FeedPlus.View mainView) {
        super(itemView);
        this.mainView = mainView;
        this.ivPhoto = itemView.findViewById(R.id.ivPhoto);
        this.tvTitle = itemView.findViewById(R.id.tvTitle);
        this.tvDescription = itemView.findViewById(R.id.tvDescription);
        this.btnSeeProfile = itemView.findViewById(R.id.btnSeeProfile);
        this.btnCreateContent = itemView.findViewById(R.id.btnCreateContent);
    }

    @Override
    public void bind(WhitelistViewModel element) {
        initView(element);
        initViewListener();
    }

    private void initView(WhitelistViewModel model) {
        tvTitle.setText(MethodChecker.fromHtml(model.getWhitelist().getTitle()));
        tvDescription.setText(MethodChecker.fromHtml(model.getWhitelist().getDesc()));
        ImageHandler.loadImageCircle2(
                ivPhoto.getContext(),
                ivPhoto,
                model.getWhitelist().getImage()
        );

        if (mainView.getUserSession().hasShop()) {
            btnSeeProfile.setText(R.string.feed_see_profile);
        } else {
            btnSeeProfile.setText(R.string.feed_see_shop);
        }
    }

    private void initViewListener() {
        btnSeeProfile.setOnClickListener(v -> onProfileClick());
        tvTitle.setOnClickListener(v -> onProfileClick());
        ivPhoto.setOnClickListener(v -> onProfileClick());

        btnCreateContent.setOnClickListener(view ->
                mainView.onWhitelistClicked()
        );
    }

    private void onProfileClick() {
        if (mainView.getUserSession().hasShop()) {
            RouteManager.route(
                    btnSeeProfile.getContext(),
                    ApplinkConst.PROFILE.replace(USER_ID, mainView.getUserSession()
                            .getUserId())
            );

        } else {
            RouteManager.route(
                    btnSeeProfile.getContext(),
                    ApplinkConst.SHOP.replace(SHOP_ID, mainView.getUserSession()
                            .getShopId())
            );
        }
    }
}
