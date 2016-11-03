package com.tokopedia.tkpd.people.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.people.model.PeopleInfoData;
import com.tokopedia.tkpd.people.presenter.PeopleInfoFragmentPresenter;
import com.tokopedia.tkpd.reputationproduct.util.ReputationLevelUtils;

import butterknife.Bind;

/**
 * Created on 6/2/16.
 */
public class PeopleInfoShopOwnerView extends BaseView<PeopleInfoData, PeopleInfoFragmentPresenter> {

    @Bind(R.id.shop_avatar)
    ImageView shopAvatar;
    @Bind(R.id.shop_name)
    TextView shopName;
    @Bind(R.id.shop_loc)
    TextView shopLocation;

    @SuppressWarnings("unused")
    @Bind(R.id.reputation_medal)
    LinearLayout reputationMedal;
    @SuppressWarnings("unused")
    @Bind(R.id.reputation_point)
    TextView reputationPoint;

    public PeopleInfoShopOwnerView(Context context) {
        super(context);
    }

    public PeopleInfoShopOwnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_people_info_shop_owner;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull PeopleInfoData peopleInfoData) {
        PeopleInfoData.ShopInfo shopInfo = peopleInfoData.getShopInfo();
        PeopleInfoData.ShopStats shopStats = peopleInfoData.getShopStats();
        renderShopBadges(shopStats);
        renderShopLocation(shopInfo);
        renderShopName(shopInfo);
        renderShopAvatar(shopInfo);
        setVisibility(VISIBLE);
    }

    private void renderShopAvatar(PeopleInfoData.ShopInfo shopInfo) {
        ImageHandler.loadImageCircle2(getContext(), shopAvatar, shopInfo.getShopAvatar());
        shopAvatar.setOnClickListener(new ShopClick(shopInfo));
    }

    private void renderShopName(PeopleInfoData.ShopInfo shopInfo) {
        shopName.setText(Html.fromHtml(shopInfo.getShopName()));
        shopName.setOnClickListener(new ShopClick(shopInfo));
    }

    private void renderShopLocation(PeopleInfoData.ShopInfo shopInfo) {
        shopLocation.setText(Html.fromHtml(shopInfo.getShopLocation()));
    }

    @SuppressLint("SetTextI18n")
    private void renderShopBadges(PeopleInfoData.ShopStats shopStats) {
        ReputationLevelUtils.setReputationMedals(getContext(), reputationMedal, getMedalType(shopStats), getMedalLevel(shopStats), getScoreMedal(shopStats));
        reputationPoint.setText(shopStats.getShopReputationScore() + " " + getContext().getString(R.string.title_poin));
    }

    private int getMedalType(PeopleInfoData.ShopStats shopStats) {
        return shopStats.getShopBadgeLevel().getSet();
    }

    private int getMedalLevel(PeopleInfoData.ShopStats shopStats) {
        return shopStats.getShopBadgeLevel().getLevel();
    }

    private String getScoreMedal(PeopleInfoData.ShopStats shopStats) {
        return shopStats.getShopReputationScore();
    }

    @Override
    public void setPresenter(@NonNull PeopleInfoFragmentPresenter presenter) {
        this.presenter = presenter;
    }

    private class ShopClick implements OnClickListener {

        private final PeopleInfoData.ShopInfo shopInfo;

        public ShopClick(PeopleInfoData.ShopInfo shopInfo) {
            this.shopInfo = shopInfo;
        }

        @Override
        public void onClick(View view) {
            presenter.onShopClicked(getContext(), shopInfo.getShopId());
        }
    }
}
