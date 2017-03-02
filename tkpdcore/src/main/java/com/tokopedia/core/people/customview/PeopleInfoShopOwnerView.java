package com.tokopedia.core.people.customview;

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
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.people.model.PeopleInfoData;
import com.tokopedia.core.people.presenter.PeopleInfoFragmentPresenter;
import com.tokopedia.core.reputationproduct.util.ReputationLevelUtils;
import com.tokopedia.core.util.MethodChecker;

import butterknife.BindView;

/**
 * Created on 6/2/16.
 */
public class PeopleInfoShopOwnerView extends BaseView<PeopleInfoData, PeopleInfoFragmentPresenter> {

    @BindView(R2.id.shop_avatar)
    ImageView shopAvatar;
    @BindView(R2.id.shop_name)
    TextView shopName;
    @BindView(R2.id.shop_loc)
    TextView shopLocation;

    @SuppressWarnings("unused")
    @BindView(R2.id.reputation_medal)
    LinearLayout reputationMedal;
    @SuppressWarnings("unused")
    @BindView(R2.id.reputation_point)
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
        shopName.setText(MethodChecker.fromHtml(shopInfo.getShopName()));
        shopName.setOnClickListener(new ShopClick(shopInfo));
    }

    private void renderShopLocation(PeopleInfoData.ShopInfo shopInfo) {
        shopLocation.setText(MethodChecker.fromHtml(shopInfo.getShopLocation()));
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
