package com.tokopedia.core.people.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.people.model.InputOutputData;
import com.tokopedia.core.people.model.PeopleFavShop;
import com.tokopedia.core.people.model.PeopleInfoData;
import com.tokopedia.core.people.presenter.PeopleInfoFragmentPresenter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;

import butterknife.BindView;

/**
 * Createdon 5/31/16.
 */
public class PeopleInfoHeaderView extends BaseView<InputOutputData, PeopleInfoFragmentPresenter> {

    @BindView(R2.id.user_avatar)
    ImageView avatar;
    @BindView(R2.id.user_name)
    TextView username;
    @BindView(R2.id.user_favorite)
    TextView textFavorite;
    @BindView(R2.id.user_following)
    TextView textFollowing;
    @BindView(R2.id.message_user)
    View actionMessage;
    @BindView(R2.id.manage_user)
    View actionManage;

    public PeopleInfoHeaderView(Context context) {
        super(context);
    }

    public PeopleInfoHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_people_info_header;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull InputOutputData data) {
        PeopleInfoData peopleInfoData = data.getPeopleInfoData();

        renderUserAvatar(peopleInfoData.getUserInfo().getUserImage());
        renderUserName(peopleInfoData.getUserInfo().getUserName());
        renderUserFavorite(data.getPeopleFavShopData());
        renderActionButton(peopleInfoData.getUserInfo().getUserId());

        if (data.getPeopleFavShopData().getDataRandomFavShop().getTotalFave() != 0) {
            textFavorite.setOnClickListener(new FavoritedShopClick(peopleInfoData));
        }

        textFollowing.setVisibility(GlobalConfig.isSellerApp() ? GONE : VISIBLE);

        textFollowing.setOnClickListener(new FollowingClick(peopleInfoData));

        actionManage.setOnClickListener(new ManageClick(peopleInfoData));
        actionMessage.setOnClickListener(new MessageClick(peopleInfoData));

        setVisibility(VISIBLE);
    }

    private void renderUserFavorite(PeopleFavShop data) {
        textFavorite.setText(
                getContext().getString(R.string.template_people_total_fav_shop)
                        .replace("XYZ", data.getDataRandomFavShop().getTotalFaveFmt())
        );
        textFollowing.setText(
                getContext().getString(R.string.template_people_total_following)
        );
    }

    private void renderUserName(String stringUserName) {
        username.setText(stringUserName);
    }

    private void renderUserAvatar(String imageURL) {
        ImageHandler.loadImageCircle2(getContext(), avatar, imageURL);
    }

    private void renderActionButton(String userId) {
        if (SessionHandler.isV4Login(getContext())) {
            if (SessionHandler.getLoginID(getContext()).equals(userId)) {
                actionMessage.setVisibility(GONE);
                actionManage.setVisibility(VISIBLE);
            } else {
                actionMessage.setVisibility(GONE);
                actionManage.setVisibility(GONE);
            }
        } else {
            actionMessage.setVisibility(GONE);
            actionManage.setVisibility(GONE);
        }
    }

    @Override
    public void setPresenter(@NonNull PeopleInfoFragmentPresenter presenter) {
        this.presenter = presenter;
    }

    private class MessageClick implements OnClickListener {

        private final PeopleInfoData data;

        private MessageClick(PeopleInfoData data) {
            this.data = data;
        }

        @Override
        public void onClick(View view) {
            presenter.onActionMessageClicked(getContext(), data.getUserInfo());
        }
    }

    private class ManageClick implements OnClickListener {

        private final PeopleInfoData data;

        private ManageClick(PeopleInfoData data) {
            this.data = data;
        }

        @Override
        public void onClick(View view) {
            presenter.onActionManageClicked(getContext(), data.getUserInfo());
        }
    }

    private class FavoritedShopClick implements OnClickListener {

        private final PeopleInfoData data;

        private FavoritedShopClick(PeopleInfoData data) {
            this.data = data;
        }

        @Override
        public void onClick(View view) {
            presenter.onFavoritedShoplicked(getContext(), data.getUserInfo());
        }
    }

    private class FollowingClick implements OnClickListener {
        private final PeopleInfoData data;

        private FollowingClick(PeopleInfoData data) {
            this.data = data;
        }
        @Override
        public void onClick(View view) {
            presenter.onFollowingClicked(getContext(), data.getUserInfo());
        }
    }
}
