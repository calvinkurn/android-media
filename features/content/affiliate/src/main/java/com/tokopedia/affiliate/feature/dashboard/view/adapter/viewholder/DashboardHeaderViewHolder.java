package com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel;

/**
 * @author by yfsx on 19/09/18.
 */
public class DashboardHeaderViewHolder extends AbstractViewHolder<DashboardHeaderViewModel> {

    private static final String URL_BACKGROUND = "https://ecs7.tokopedia.net/img/android/bg_af_dashboard/drawable-hdpi/bg_af_dashboard.png";

    private static final int TEXT_TYPE_PROFILE_SEEN = 1;
    private static final int TEXT_TYPE_PRODUCT_CLICKED = 2;
    private static final int TEXT_TYPE_PRODUCT_BOUGHT = 3;

    private DashboardContract.View mainView;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_af_dashboard_header;


    private TextView tvSaldo, tvProfileSeen, tvProductClicked, tvProductBought;
    private LinearLayout layoutProfileSeen, layoutProductClicked, layoutProductBought;
    private ImageView ivSaldo;

    public DashboardHeaderViewHolder(View itemView, DashboardContract.View mainView) {
        super(itemView);
        this.mainView = mainView;
        ivSaldo = (ImageView) itemView.findViewById(R.id.iv_saldo);
        tvSaldo = (TextView) itemView.findViewById(R.id.tv_saldo);
        tvProfileSeen = (TextView) itemView.findViewById(R.id.tv_profile_seen);
        tvProductClicked = (TextView) itemView.findViewById(R.id.tv_product_clicked);
        tvProductBought = (TextView) itemView.findViewById(R.id.tv_product_bought);
        layoutProfileSeen = (LinearLayout) itemView.findViewById(R.id.layout_profile_seen);
        layoutProductClicked = (LinearLayout) itemView.findViewById(R.id.layout_product_clicked);
        layoutProductBought = (LinearLayout) itemView.findViewById(R.id.layout_product_bought);
    }

    @Override
    public void bind(DashboardHeaderViewModel element) {
        initView(element);
    }

    private void initView(DashboardHeaderViewModel element) {
        ImageHandler.LoadImage(ivSaldo, URL_BACKGROUND);
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
        tvSaldo.setText(mainView.getContext().getResources().getString(R.string.text_af_default_saldo));
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
