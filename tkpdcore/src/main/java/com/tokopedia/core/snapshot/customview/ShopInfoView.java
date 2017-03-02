package com.tokopedia.core.snapshot.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ShopBadge;
import com.tokopedia.core.reputationproduct.util.ReputationLevelUtils;
import com.tokopedia.core.snapshot.listener.SnapShotFragmentView;
import com.tokopedia.core.util.MethodChecker;

import butterknife.BindView;

/**
 * Created by hangnadi on 3/1/17.
 */

public class ShopInfoView extends BaseView<ProductDetailData, SnapShotFragmentView> {

    @BindView(R2.id.iv_ava)
    ImageView ivShopAva;
    @BindView(R2.id.iv_gold)
    ImageView ivGoldShop;
    @BindView(R2.id.iv_official)
    ImageView ivOfficialStore;
    @BindView(R2.id.iv_lucky)
    ImageView ivLuckyShop;
    @BindView(R2.id.tv_name)
    TextView tvShopName;
    @BindView(R2.id.tv_location)
    TextView tvShopLoc;
    @BindView(R2.id.iv_fav)
    ImageView ivBtnFav;
    @BindView(R2.id.iv_message)
    ImageView ivShopMessage;
    @BindView(R2.id.l_rating)
    LinearLayout llRating;
    @BindView(R2.id.l_medal)
    LinearLayout llReputationMedal;
    @BindView(R2.id.tv_reputation)
    TextView tvReputationPoint;
    @BindView(R2.id.l_other)
    LinearLayout layoutOther;

    public ShopInfoView(Context context) {
        super(context);
    }

    public ShopInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(SnapShotFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_shop_info_product_info;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        ivGoldShop.setVisibility(GONE);
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        tvShopName.setText(MethodChecker.fromHtml(data.getShopInfo().getShopName()));
        tvShopLoc.setText(data.getShopInfo().getShopLocation());
        tvReputationPoint.setText(String.format("%d %s", data.getShopInfo().getShopReputation(),
                getContext().getString(R.string.title_poin)));
        if (data.getShopInfo().getShopStats().getShopBadge() != null) generateMedal(data);
        ImageHandler.loadImageCircle2(getContext(), ivShopAva, data.getShopInfo().getShopAvatar(),
                R.drawable.ic_default_shop_ava);
        LuckyShopImage.loadImage(ivLuckyShop, data.getShopInfo().getShopLucky());

        ivBtnFav.setVisibility(data.getShopInfo().getShopIsOwner() == 1 ? GONE : VISIBLE);
        ivGoldShop.setVisibility(data.getShopInfo().getShopIsGold() == 1 ? VISIBLE : GONE);
        switchOfficialStoreBadge(data.getShopInfo().getShopIsOfficial());

        ivShopAva.setOnClickListener(new ShopInfoView.ClickShopAva(data));
        tvShopName.setOnClickListener(new ShopInfoView.ClickShopName(data));
        llRating.setOnClickListener(new ShopInfoView.ClickShopRating(data));
        ivShopMessage.setVisibility(GONE);
        ivBtnFav.setVisibility(GONE);
        setVisibility(VISIBLE);
    }

    private class ClickShopName implements OnClickListener {
        private final ProductDetailData data;

        ClickShopName(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("shop_id", data.getShopInfo().getShopId());
            bundle.putString("shop_name", data.getShopInfo().getShopName());
            bundle.putString("shop_avatar", data.getShopInfo().getShopAvatar());
            bundle.putInt("shop_favorite", data.getShopInfo().getShopAlreadyFavorited());
            listener.onProductShopNameClicked(bundle);
        }
    }

    private class ClickShopRating implements OnClickListener {
        private final ProductDetailData data;

        ClickShopRating(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putInt("tab", 2);
            bundle.putString("shop_id", data.getShopInfo().getShopId());
            listener.onProductShopRatingClicked(bundle);
        }
    }

    private class ClickShopAva implements OnClickListener {
        private final ProductDetailData data;

        ClickShopAva(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("shop_id", data.getShopInfo().getShopId());
            bundle.putString("shop_name", data.getShopInfo().getShopName());
            bundle.putString("shop_avatar", data.getShopInfo().getShopAvatar());
            bundle.putInt("shop_favorite", data.getShopInfo().getShopAlreadyFavorited());
            listener.onProductShopAvatarClicked(bundle);
        }
    }

    private void switchOfficialStoreBadge(int isOfficialStore) {
        if (isOfficialStore == 1) {
            ivGoldShop.setVisibility(GONE);
            ivOfficialStore.setVisibility(VISIBLE);
        }
    }

    private void generateMedal(ProductDetailData data) {
        ShopBadge shopBadge = data.getShopInfo().getShopStats().getShopBadge();
        ReputationLevelUtils.setReputationMedals(getContext(), llReputationMedal,
                shopBadge.getSet(), shopBadge.getLevel(),
                String.valueOf(data.getShopInfo().getShopReputation()));
    }
}
