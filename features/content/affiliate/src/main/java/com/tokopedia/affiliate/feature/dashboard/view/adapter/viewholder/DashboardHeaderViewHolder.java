package com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder;

import android.os.Build;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel;

/**
 * @author by yfsx on 19/09/18.
 */
public class DashboardHeaderViewHolder extends AbstractViewHolder<DashboardHeaderViewModel> {

    private static final String ANDROID_IMAGE_URL = "https://ecs7.tokopedia.net/img/android";
    private static final String FINISH_IMAGE_NAME = "bg_af_dashboard";
    private static final String IMAGE_URL_FORMAT = "%s/%s/%s/%s.png";
    private static final String DRAWABLE = "drawable-";

    private static final int TEXT_TYPE_PROFILE_SEEN = 1;
    private static final int TEXT_TYPE_PRODUCT_CLICKED = 2;
    private static final int TEXT_TYPE_PRODUCT_BOUGHT = 3;

    private DashboardContract.View mainView;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_af_dashboard_header;


    private TextView tvSaldo, tvProfileSeen, tvProductClicked, tvProductBought;
    private LinearLayout layoutProfileSeen, layoutProductClicked, layoutProductBought;
    private ImageView ivSaldo;
    private FrameLayout layoutSaldo;

    public DashboardHeaderViewHolder(View itemView, DashboardContract.View mainView) {
        super(itemView);
        this.mainView = mainView;
        ivSaldo = itemView.findViewById(R.id.iv_saldo);
        tvSaldo = itemView.findViewById(R.id.tv_saldo);
        tvProfileSeen = itemView.findViewById(R.id.tv_profile_seen);
        tvProductClicked = itemView.findViewById(R.id.tv_product_clicked);
        tvProductBought = itemView.findViewById(R.id.tv_product_bought);
        layoutProfileSeen = itemView.findViewById(R.id.layout_profile_seen);
        layoutProductClicked = itemView.findViewById(R.id.layout_product_clicked);
        layoutProductBought = itemView.findViewById(R.id.layout_product_bought);
        layoutSaldo = itemView.findViewById(R.id.layout_saldo);
    }

    @Override
    public void bind(DashboardHeaderViewModel element) {
        initView(element);
        initViewListener(element);
    }

    private void initViewListener(DashboardHeaderViewModel element) {
        layoutSaldo.setOnClickListener(view -> {
            mainView.goToDeposit();
        });
    }

    private void initView(DashboardHeaderViewModel element) {
        ViewTreeObserver observer = layoutSaldo.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    layoutSaldo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    //noinspection deprecation
                    layoutSaldo.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                int height = layoutSaldo.getHeight();
                ivSaldo.setMinimumHeight(height);
                ivSaldo.requestLayout();
            }
        });
        String imageUrl = String.format(IMAGE_URL_FORMAT,
                ANDROID_IMAGE_URL,
                FINISH_IMAGE_NAME,
                DRAWABLE + DisplayMetricUtils.getScreenDensity(mainView.getContext()),
                FINISH_IMAGE_NAME
        );
        ImageHandler.loadImageWithoutPlaceholder(ivSaldo, imageUrl);
        if (element == null) {
            initDefaultValue();
        } else {
            tvSaldo.setText(MethodChecker.fromHtml(element.getSaldoString()));
            tvProductBought.setText(MethodChecker.fromHtml(element.getBuyCount()));
            tvProductClicked.setText(MethodChecker.fromHtml(element.getClickCount()));
            tvProfileSeen.setText(MethodChecker.fromHtml(element.getSeenCount()));
        }
    }

    private void initDefaultValue() {
        tvProductBought.setText(countTextBuilder(TEXT_TYPE_PROFILE_SEEN, 0));
        tvProductClicked.setText(countTextBuilder(TEXT_TYPE_PRODUCT_CLICKED, 0));
        tvProfileSeen.setText(countTextBuilder(TEXT_TYPE_PROFILE_SEEN, 0));
        tvSaldo.setText(getString(R.string.text_af_default_saldo));
    }

    private String countTextBuilder(int textType, int count) {
        String defaultText = mainView.getContext().getResources().getString(
                textType == TEXT_TYPE_PROFILE_SEEN ?
                        R.string.title_profil_dilihat :
                        (textType == TEXT_TYPE_PRODUCT_CLICKED ?
                                R.string.title_klik_produk :
                                R.string.title_produk_dibeli));
        return String.valueOf(count) + " " + defaultText;
    }
}
