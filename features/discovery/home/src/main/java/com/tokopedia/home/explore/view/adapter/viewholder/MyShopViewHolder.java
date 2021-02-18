package com.tokopedia.home.explore.view.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.home.R;
import com.tokopedia.home.explore.domain.model.ShopData;
import com.tokopedia.home.explore.listener.CategoryAdapterListener;
import com.tokopedia.media.loader.JvmMediaLoader;
import com.tokopedia.home.explore.view.adapter.datamodel.MyShopDataModel;

import static com.tokopedia.gm.common.constant.GMCommonConstantKt.GM_TITLE;

/**
 * Created by errysuprayogi on 12/5/17.
 */

public class MyShopViewHolder extends AbstractViewHolder<MyShopDataModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_my_shop;

    View container;
    TextView titleTxt;
    ImageView imageView;
    TextView button;
    ImageView reputationMedal;
    ImageView badgeImage;
    TextView badgeTxt;

    private Context context;
    private CategoryAdapterListener listener;

    public MyShopViewHolder(View itemView, final CategoryAdapterListener listener) {
        super(itemView);
        this.listener = listener;
        this.context = itemView.getContext();
        container = itemView.findViewById(R.id.container);
        titleTxt = itemView.findViewById(R.id.title);
        imageView = itemView.findViewById(R.id.image_shop);
        badgeImage = itemView.findViewById(R.id.badge);
        button = itemView.findViewById(R.id.btn_ubah);
        reputationMedal = itemView.findViewById(R.id.reputation_medal);
        badgeTxt = itemView.findViewById(R.id.official_store_txt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.openShopSetting();
            }
        });
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.openShop();
            }
        });
        titleTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.openShop();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.openShop();
            }
        });
    }

    @Override
    public void bind(MyShopDataModel element) {
        ShopData data = element.getShopData();
        titleTxt.setText(MethodChecker.fromHtml(data.getShopName()));
        JvmMediaLoader.loadImage(imageView, data.getLogo());
        if (data.getIsOfficial() == 1) {
            badgeTxt.setText(getString(R.string.official_store));
            badgeImage.setImageResource(R.drawable.ic_official);
        } else if (data.isIsGoldBadge()) {
            badgeImage.setImageDrawable(MethodChecker.getDrawable(context, com.tokopedia.gm.common.R.drawable.ic_power_merchant));
            badgeTxt.setText(GM_TITLE);
        } else {
            badgeTxt.setVisibility(View.GONE);
            badgeImage.setVisibility(View.GONE);
        }
        Glide.with(reputationMedal.getContext())
                .load(data.getReputationBadge())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(reputationMedal);
    }

}
