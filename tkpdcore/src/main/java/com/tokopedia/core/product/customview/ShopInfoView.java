package com.tokopedia.core.product.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.inboxmessage.fragment.SendMessageFragment;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.product.listener.ProductDetailView;
import com.tokopedia.core.product.model.passdata.ProductPass;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ShopBadge;
import com.tokopedia.core.product.model.productother.ProductOther;
import com.tokopedia.core.reputationproduct.util.ReputationLevelUtils;
import com.tokopedia.core.util.SessionHandler;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Angga.Prasetiyo on 27/10/2015.
 */
public class ShopInfoView extends BaseView<ProductDetailData, ProductDetailView> {
    private static final String TAG = ShopInfoView.class.getSimpleName();

    @BindView(R2.id.iv_ava)
    ImageView ivShopAva;
    @BindView(R2.id.iv_gold)
    ImageView ivGoldShop;
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
    private boolean isShopFavorite = false;

    public ShopInfoView(Context context) {
        super(context);
    }

    public ShopInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
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
    public void renderData(@NonNull final ProductDetailData data) {
        tvShopName.setText(Html.fromHtml(data.getShopInfo().getShopName()));
        tvShopLoc.setText(data.getShopInfo().getShopLocation());
        tvReputationPoint.setText(String.format("%d %s", data.getShopInfo().getShopReputation(),
                getContext().getString(R.string.title_poin)));
        if (data.getShopInfo().getShopStats().getShopBadge() != null) generateMedal(data);
        ImageHandler.loadImageCircle2(getContext(), ivShopAva, data.getShopInfo().getShopAvatar(),
                R.drawable.ic_default_shop_ava);
        LuckyShopImage.loadImage(ivLuckyShop, data.getShopInfo().getShopLucky());

        ivBtnFav.setVisibility(data.getShopInfo().getShopIsOwner() == 1 ? GONE : VISIBLE);
        ivGoldShop.setVisibility(data.getShopInfo().getShopIsGold() == 1 ? VISIBLE : GONE);
        ivShopMessage.setVisibility(data.getShopInfo().getShopId()
                .equals(SessionHandler.getShopID(getContext())) ? GONE : VISIBLE);

        updateFavoriteStatus(data.getShopInfo().getShopAlreadyFavorited());

        ivShopAva.setOnClickListener(new ClickShopAva(data));
        tvShopName.setOnClickListener(new ClickShopName(data));
        llRating.setOnClickListener(new ClickShopRating(data));
        ivShopMessage.setOnClickListener(new ClickShopMessage(data));
        ivBtnFav.setOnClickListener(new ClickBtnFave(data));
        setVisibility(VISIBLE);
    }

    private void generateMedal(ProductDetailData data) {
        ShopBadge shopBadge = data.getShopInfo().getShopStats().getShopBadge();
        ReputationLevelUtils.setReputationMedals(getContext(), llReputationMedal,
                shopBadge.getSet(), shopBadge.getLevel(),
                String.valueOf(data.getShopInfo().getShopReputation()));
    }

    public void renderOtherProduct(List<ProductOther> productOtherList) {
        layoutOther.removeAllViews();
        for (ProductOther productOther : productOtherList) {
            OtherProdItemView otherProdItemView = new OtherProdItemView(getContext());
            otherProdItemView.renderData(productOther);
            otherProdItemView.setOnClickListener(new ClickOtherProduct(productOther));
            layoutOther.addView(otherProdItemView);
        }
    }

    public void updateFavoriteStatus(int statFave) {
        switch (statFave) {
            case 0:
                isShopFavorite = false;
                ivBtnFav.setImageResource(R.drawable.ic_fav);
                break;
            case 1:
                isShopFavorite = true;
                ivBtnFav.setImageResource(R.drawable.ic_faved);
                break;
        }
    }

    public void reverseFavoriteStatus() {
        ivBtnFav.clearAnimation();
        if (isShopFavorite) {
            ivBtnFav.setImageResource(R.drawable.ic_fav);
            isShopFavorite = false;
            listener.showToastMessage(getContext().getString(R.string.message_success_unfav));
        } else {
            ivBtnFav.setImageResource(R.drawable.ic_faved);
            isShopFavorite = true;
            listener.showToastMessage(getContext().getString(R.string.message_success_fav));
        }
    }

    public View getProductOtherView() {
        return this.layoutOther;
    }

    private class AnimationFav extends ScaleAnimation {

        public AnimationFav(int fromX, float toX, int fromY, float toY, int pivotXType,
                            float pivotXValue, int pivotYType, float pivotYValue) {
            super(fromX, toX, fromY, toY, pivotXType, pivotXValue, pivotYType, pivotYValue);
            setDuration(250);
            setRepeatCount(Animation.INFINITE);
            setRepeatMode(Animation.REVERSE);
            setFillAfter(false);
        }
    }

    private class ClickOtherProduct implements OnClickListener {

        private final ProductOther data;

        public ClickOtherProduct(ProductOther productOther) {
            this.data = productOther;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ProductInfoActivity.EXTRA_PRODUCT_PASS,
                    ProductPass.Builder.aProductPass()
                            .setProductPrice(data.getProductPrice())
                            .setProductId(data.getProductId())
                            .setProductName(data.getProductName())
                            .setProductImage(data.getProductImage())
                            .build());
            listener.onProductOtherClicked(ProductPass.Builder.aProductPass()
                    .setProductPrice(data.getProductPrice())
                    .setProductId(data.getProductId())
                    .setProductName(data.getProductName())
                    .setProductImage(data.getProductImage())
                    .build());
        }
    }

    private class ClickBtnFave implements OnClickListener {
        private final ProductDetailData data;

        public ClickBtnFave(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            ivBtnFav.startAnimation(new AnimationFav(1, 1.25f, 1, 1.25f,
                    Animation.RELATIVE_TO_SELF, (float) 0.25, Animation.RELATIVE_TO_SELF,
                    (float) 0.25));
            listener.onProductShopFaveClicked(data.getShopInfo().getShopId());
        }
    }

    private class ClickShopMessage implements OnClickListener {
        private final ProductDetailData data;

        public ClickShopMessage(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("login", true);
            bundle.putString(SendMessageFragment.PARAM_SHOP_ID, String.valueOf(data.getShopInfo().getShopId()));
            bundle.putString(SendMessageFragment.PARAM_OWNER_FULLNAME, data.getShopInfo().getShopName());
            listener.onProductShopMessageClicked(bundle);
        }
    }

    private class ClickShopAva implements OnClickListener {
        private final ProductDetailData data;

        public ClickShopAva(ProductDetailData data) {
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

    private class ClickShopName implements OnClickListener {
        private final ProductDetailData data;

        public ClickShopName(ProductDetailData data) {
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

        public ClickShopRating(ProductDetailData data) {
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
}
