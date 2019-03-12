package com.tokopedia.saldodetails.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsAnalytics;
import com.tokopedia.saldodetails.response.model.GqlAnchorListResponse;
import com.tokopedia.saldodetails.response.model.GqlInfoListResponse;
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditResponse;

import javax.inject.Inject;

import static com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment.BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS;

public class MerchantCreditDetailFragment extends BaseDaggerFragment {

    private Context context;
    private GqlMerchantCreditResponse merchantCreditDetails;

    private ImageView mclLogoIV;
    private TextView mclTitleTV;
    private TextView mclActionItemTV;
    private TextView mclDescTV;
    private LinearLayout mclInfoListLL;
    private RelativeLayout mclBoxLayout;
    private TextView mclboxTitleTV;
    private TextView mclBoxDescTV;

    @Inject
    SaldoDetailsAnalytics saldoDetailsAnalytics;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merchant_credit_details, container, false);
        Bundle bundle = getArguments();
        merchantCreditDetails = bundle != null ? bundle.getParcelable(BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS) : null;
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mclLogoIV = view.findViewById(R.id.mcl_logo);
        mclTitleTV = view.findViewById(R.id.mcl_title);
        mclActionItemTV = view.findViewById(R.id.mcl_action_item);
        mclDescTV = view.findViewById(R.id.mcl_description);
        mclInfoListLL = view.findViewById(R.id.mcl_info_list);
        mclBoxLayout = view.findViewById(R.id.mcl_box_layout);
        mclboxTitleTV = view.findViewById(R.id.mcl_box_title);
        mclBoxDescTV = view.findViewById(R.id.mcl_box_Desc);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateData();
        initListeners();
    }

    private void initListeners() {

    }

    private void populateData() {
        if (merchantCreditDetails != null) {

            if (!TextUtils.isEmpty(merchantCreditDetails.getLogoURL())) {
                // TODO: 12/3/19 pass the placeholder
                mclLogoIV.setVisibility(View.VISIBLE);
                ImageHandler.loadImage(context, mclLogoIV, merchantCreditDetails.getLogoURL(), null);
            } else {
                mclLogoIV.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(merchantCreditDetails.getTitle())) {
                mclTitleTV.setText(Html.fromHtml(merchantCreditDetails.getTitle()));
            }

            if (merchantCreditDetails.getAnchorList() != null &&
                    merchantCreditDetails.getAnchorList().size() > 0) {
                populateAnchorListData();
            } else {
                mclActionItemTV.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(merchantCreditDetails.getBodyDesc())) {
                mclDescTV.setText(merchantCreditDetails.getBodyDesc());
                mclDescTV.setVisibility(View.VISIBLE);
            } else {
                mclDescTV.setVisibility(View.GONE);
            }

            if (merchantCreditDetails.getInfoList() != null &&
                    merchantCreditDetails.getInfoList().size() > 0) {
                populateInfolistData();
            } else {
                mclInfoListLL.setVisibility(View.GONE);
            }

            if (merchantCreditDetails.isShowBox() && merchantCreditDetails.getBoxInfo() != null) {
                mclBoxLayout.setVisibility(View.VISIBLE);
                populateBoxData();
            } else {
                mclBoxLayout.setVisibility(View.GONE);
            }
        }
    }

    private void populateBoxData() {

        if (!TextUtils.isEmpty(merchantCreditDetails.getBoxInfo().getBoxTitle())) {
            mclboxTitleTV.setVisibility(View.VISIBLE);
            mclboxTitleTV.setText(merchantCreditDetails.getBoxInfo().getBoxTitle());
        } else {
            mclboxTitleTV.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(merchantCreditDetails.getBoxInfo().getBoxDesc())) {
            mclBoxDescTV.setVisibility(View.VISIBLE);

            // TODO: 12/3/19 check for link

            mclBoxDescTV.setText(merchantCreditDetails.getBoxInfo().getBoxTitle());
        } else {
            mclBoxDescTV.setVisibility(View.GONE);
        }
    }

    private void populateInfolistData() {
        LayoutInflater layoutInflater = getLayoutInflater();
        mclInfoListLL.removeAllViews();

        for (GqlInfoListResponse infoList1 : merchantCreditDetails.getInfoList()) {

            View view = layoutInflater.inflate(R.layout.layout_info_list, null);
            TextView infoLabel = view.findViewById(R.id.info_label_text_view);
            TextView infoValue = view.findViewById(R.id.info_value_text_view);

            infoLabel.setText(infoList1.getLabel());
            infoValue.setText(infoList1.getValue());

            mclInfoListLL.addView(view);
        }
        mclInfoListLL.setVisibility(View.VISIBLE);
    }

    private void populateAnchorListData() {

        GqlAnchorListResponse gqlAnchorListResponse = merchantCreditDetails.getAnchorList().get(0);

        if (gqlAnchorListResponse != null) {

            mclActionItemTV.setText(gqlAnchorListResponse.getLabel());

            try {
                mclActionItemTV.setTextColor(Color.parseColor(gqlAnchorListResponse.getColor()));
            } catch (Exception e) {
                mclActionItemTV.setTextColor(getResources().getColor(R.color.tkpd_main_green));
            }

            mclActionItemTV.setOnClickListener(v -> {
                saldoDetailsAnalytics.eventAnchorLabelClick(mclActionItemTV.getText().toString());

                if (gqlAnchorListResponse.isShowDialog()) {

                    // TODO: 12/3/19 open bottom sheet

                } else {
                    RouteManager.route(context, String.format("%s?url=%s",
                            ApplinkConst.WEBVIEW, gqlAnchorListResponse.getLink()));
                }

            });

        }
        mclActionItemTV.setVisibility(View.VISIBLE);
    }

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new MerchantCreditDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
