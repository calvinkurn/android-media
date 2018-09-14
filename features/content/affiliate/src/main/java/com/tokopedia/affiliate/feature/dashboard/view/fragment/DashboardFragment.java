package com.tokopedia.affiliate.feature.dashboard.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.common.di.DaggerAffiliateComponent;
import com.tokopedia.affiliate.feature.dashboard.di.DaggerDashboardComponent;

/**
 * @author by yfsx on 13/09/18.
 */
public class DashboardFragment extends BaseDaggerFragment {

    private static final String URL_BACKGROUND = "https://ecs7.tokopedia.net/img/android/bg_af_dashboard/drawable-xxxhdpi/bg_af_dashboard.png";

    private static final int TEXT_TYPE_PROFILE_SEEN = 1;
    private static final int TEXT_TYPE_PRODUCT_CLICKED = 2;
    private static final int TEXT_TYPE_PRODUCT_BOUGHT = 3;
    private static final int TECT_RECOMMENDATION_LEFT = 4;

    private FrameLayout layoutSaldo;
    private ImageView ivSaldo;
    private TextView tvSaldo, tvProfileSeen, tvProductClicked, tvProductBought, tvRecommendationCount;
    private LinearLayout layoutProfileSeen, layoutProductClicked, layoutProductBought;
    private RecyclerView rvHistory;
    private CardView cvRecommendation;

    public static DashboardFragment getInstance(Bundle bundle) {
        DashboardFragment fragment = new DashboardFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        DaggerAffiliateComponent affiliateComponent = (DaggerAffiliateComponent) DaggerAffiliateComponent.builder()
                .baseAppComponent(((BaseMainApplication)getActivity().getApplicationContext()).getBaseAppComponent()).build();

        DaggerDashboardComponent.builder()
                .affiliateComponent(affiliateComponent)
                .build().inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_af_dashboard, container, false);
        layoutSaldo = (FrameLayout) view.findViewById(R.id.layout_saldo);
        ivSaldo = (ImageView) view.findViewById(R.id.iv_saldo);
        tvSaldo = (TextView) view.findViewById(R.id.tv_saldo);
        rvHistory = (RecyclerView) view.findViewById(R.id.rv_history);
        tvProfileSeen = (TextView) view.findViewById(R.id.tv_profile_seen);
        tvProductClicked = (TextView) view.findViewById(R.id.tv_product_clicked);
        tvProductBought = (TextView) view.findViewById(R.id.tv_product_bought);
        layoutProfileSeen = (LinearLayout) view.findViewById(R.id.layout_profile_seen);
        layoutProductClicked = (LinearLayout) view.findViewById(R.id.layout_product_clicked);
        layoutProductBought = (LinearLayout) view.findViewById(R.id.layout_product_bought);
        cvRecommendation = (CardView) view.findViewById(R.id.item_recommendation_count);
        tvRecommendationCount = (TextView) view.findViewById(R.id.tv_recommendation_count);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initViewListener();
    }

    private void initView() {
        ImageHandler.LoadImage(ivSaldo, URL_BACKGROUND);
        tvProductBought.setText(countTextBuilder(TEXT_TYPE_PROFILE_SEEN, 0));
        tvProductClicked.setText(countTextBuilder(TEXT_TYPE_PRODUCT_CLICKED, 0));
        tvProfileSeen.setText(countTextBuilder(TEXT_TYPE_PROFILE_SEEN, 0));
        tvRecommendationCount.setText(countTextBuilder(TECT_RECOMMENDATION_LEFT, 0));
    }

    private void initViewListener() {
        cvRecommendation.setOnClickListener(view -> {

        });
    }

    private String countTextBuilder(int textType, int count) {
        String defaultText = getResources().getString(
                textType == TEXT_TYPE_PROFILE_SEEN ?
                        R.string.title_profil_dilihat :
                        (textType == TEXT_TYPE_PRODUCT_CLICKED ?
                                R.string.title_klik_produk :
                                R.string.title_produk_dibeli));
        return String.valueOf(count) + " " + defaultText;
    }
}
