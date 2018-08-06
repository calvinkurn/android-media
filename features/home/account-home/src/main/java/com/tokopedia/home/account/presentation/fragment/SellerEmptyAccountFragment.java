package com.tokopedia.home.account.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.AccountHomeRouter;
import com.tokopedia.home.account.presentation.view.InfoCardView;

/**
 * Created by meta on 01/08/18.
 */
public class SellerEmptyAccountFragment extends BaseAccountFragment {

    public static Fragment newInstance() {
        return new SellerEmptyAccountFragment();
    }

    @Override
    protected String getScreenName() {
        return getString(R.string.label_account_seller);
    }

    private InfoCardView topadsInfo;
    private InfoCardView gmInfo;
    private InfoCardView sellerCenterInfo;
    private TextView btnLearnMore;
    private Button btnOpenShop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_account_empty, container, false);
        topadsInfo = view.findViewById(R.id.topads_info);
        gmInfo = view.findViewById(R.id.gm_info);
        sellerCenterInfo = view.findViewById(R.id.seller_center_info);
        btnLearnMore = view.findViewById(R.id.btn_learn_more);
        btnOpenShop = view.findViewById(R.id.btn_open_shop);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        topadsInfo.setImage(R.drawable.ic_topads);
        topadsInfo.setMainText(R.string.title_menu_topads);
        topadsInfo.setSecondaryText(R.string.topads_desc);

        gmInfo.setImage(R.drawable.ic_badge_shop_gm);
        gmInfo.setMainText(R.string.gold_merchant);
        gmInfo.setSecondaryText(R.string.gold_merchant_desc);

        sellerCenterInfo.setImage(R.drawable.ic_seller_center);
        sellerCenterInfo.setMainText(R.string.seller_center);
        sellerCenterInfo.setSecondaryText(R.string.seller_center_desc);

        topadsInfo.setOnClickListener(v -> {
            if(getContext().getApplicationContext() instanceof AccountHomeRouter){
                ((AccountHomeRouter) getContext().getApplicationContext()).
                        gotoTopAdsDashboard(getContext());
            }
        });

        gmInfo.setOnClickListener(v -> {
            if(getContext().getApplicationContext() instanceof AccountHomeRouter){
                ((AccountHomeRouter) getContext().getApplicationContext()).
                        goToGMSubscribe(getContext());
            }
        });

        sellerCenterInfo.setOnClickListener(v ->
                RouteManager.route(getActivity(), ApplinkConst.SELLER_CENTER));

        btnOpenShop.setOnClickListener(v -> {
            if(getContext().getApplicationContext() instanceof AccountHomeRouter){
                startActivity(((AccountHomeRouter) getContext().getApplicationContext()).
                        getIntentCreateShop(getContext()));
            }
        });

        btnLearnMore.setOnClickListener(v -> RouteManager.route(getActivity(), String.format("%s?url=%s",
                ApplinkConst.WEBVIEW,
                AccountConstants.Url.MORE_SELLER)));
    }
}
