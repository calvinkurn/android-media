package com.tokopedia.loyalty.view.compoundview;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.view.data.PromoMenuData;
import com.tokopedia.media.loader.JvmMediaLoader;

import kotlin.Unit;

/**
 * @author anggaprasetiyo on 08/01/18.
 */

public class MenuPromoTab extends BaseCustomView {

    ImageView ivIcon;
    TextView tvTitle;
    private PromoMenuData promoMenuData;

    public MenuPromoTab(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public MenuPromoTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MenuPromoTab(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(
                R.layout.holder_menu_promo_tab, this, true
        );
        ivIcon = findViewById(R.id.iv_icon);
        tvTitle = findViewById(R.id.tv_title);
    }

    public void renderData(PromoMenuData promoMenuData) {
        this.promoMenuData = promoMenuData;
        tvTitle.setText(promoMenuData.getTitle());
        renderNormalState();
    }

    public void renderNormalState() {
        tvTitle.setTextColor(getContext().getResources().getColor(com.tokopedia.design.R.color.grey_600));
        JvmMediaLoader.loadImageWithCacheData(ivIcon, promoMenuData.getIconNormal());
    }

    public void renderActiveState() {
        tvTitle.setTextColor(getContext().getResources().getColor(com.tokopedia.abstraction.R.color.tkpd_main_green));
        JvmMediaLoader.loadImageWithCacheData(ivIcon, promoMenuData.getIconActive());
    }
}
