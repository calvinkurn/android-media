package com.tokopedia.saldodetails.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsAnalytics;
import com.tokopedia.saldodetails.di.SaldoDetailsComponent;
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance;
import com.tokopedia.saldodetails.response.model.GqlAnchorListResponse;
import com.tokopedia.saldodetails.response.model.GqlInfoListResponse;
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditResponse;
import com.tokopedia.saldodetails.view.activity.SaldoWebViewActivity;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;

import javax.inject.Inject;

import static com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment.BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS;
import static com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment.BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS_ID;

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
    private CardView mclParentCardView;

    private TextView mclBlockedStatusTV;
    private SaveInstanceCacheManager saveInstanceCacheManager;
    private int saveInstanceCachemanagerId;

    @Inject
    SaldoDetailsAnalytics saldoDetailsAnalytics;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.tokopedia.saldodetails.R.layout.fragment_merchant_credit_details, container, false);
        Bundle bundle = getArguments();
        saveInstanceCachemanagerId = bundle != null ? bundle.getInt(BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS_ID):0;
        saveInstanceCacheManager=new SaveInstanceCacheManager(context,String.valueOf(saveInstanceCachemanagerId));
        merchantCreditDetails=saveInstanceCacheManager.get(BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS,GqlMerchantCreditResponse.class);
        initViews(view);
        if (merchantCreditDetails != null) {
            saldoDetailsAnalytics.eventMCLImpression(String.valueOf(merchantCreditDetails.getStatus()));
        }
        return view;
    }

    private void initViews(View view) {
        mclLogoIV = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_logo);
        mclTitleTV = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_title);
        mclActionItemTV = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_action_item);
        mclDescTV = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_description);
        mclInfoListLL = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_info_list);
        mclBoxLayout = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_box_layout);
        mclboxTitleTV = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_box_title);
        mclBoxDescTV = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_box_Desc);
        mclBlockedStatusTV = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_blocked_status);
        mclParentCardView = view.findViewById(com.tokopedia.saldodetails.R.id.mcl_card_view);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateData();
    }

    private void populateData() {
        if (merchantCreditDetails != null) {

            if (!TextUtils.isEmpty(merchantCreditDetails.getLogoURL())) {
                mclLogoIV.setVisibility(View.VISIBLE);
                ImageHandler.loadImage(context, mclLogoIV, merchantCreditDetails.getLogoURL(), com.tokopedia.design.R.drawable.ic_modal_toko);
            } else {
                mclLogoIV.setImageDrawable(getResources().getDrawable(com.tokopedia.design.R.drawable.ic_modal_toko));
            }

            if (!TextUtils.isEmpty(merchantCreditDetails.getTitle())) {
                mclTitleTV.setText(Html.fromHtml(merchantCreditDetails.getTitle()));
            } else {
                mclTitleTV.setText(getString(com.tokopedia.saldodetails.R.string.modal_toko));
            }

            if (merchantCreditDetails.getAnchorList() != null) {
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
                mclInfoListLL.setVisibility(View.VISIBLE);
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

            mclParentCardView.setOnClickListener(v -> {

                saldoDetailsAnalytics.eventMCLCardCLick(String.valueOf(merchantCreditDetails.getStatus()));
                if (!TextUtils.isEmpty(merchantCreditDetails.getMainRedirectUrl())) {
                    startWebView(merchantCreditDetails.getMainRedirectUrl());
                }
            });
        }
    }

    private void populateBoxData() {

        Drawable drawable = mclBoxLayout.getBackground();
        drawable.setColorFilter(Color.parseColor(merchantCreditDetails.getBoxInfo().getBoxBgColor()), PorterDuff.Mode.SRC_ATOP);
        mclBoxLayout.setBackground(drawable);

        if (!TextUtils.isEmpty(merchantCreditDetails.getBoxInfo().getBoxTitle())) {
            mclboxTitleTV.setVisibility(View.VISIBLE);
            mclboxTitleTV.setText(merchantCreditDetails.getBoxInfo().getBoxTitle());
        } else {
            mclboxTitleTV.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(merchantCreditDetails.getBoxInfo().getBoxDesc())) {
            mclBoxDescTV.setVisibility(View.VISIBLE);
            String descText = merchantCreditDetails.getBoxInfo().getBoxDesc();
            if (!TextUtils.isEmpty(merchantCreditDetails.getBoxInfo().getLinkText())) {
                String linkText = merchantCreditDetails.getBoxInfo().getLinkText();
                descText += " " + linkText;

                SpannableString spannableString = new SpannableString(descText);
                int startIndexOfLink = descText.indexOf(linkText);
                if (startIndexOfLink != -1) {
                    spannableString.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(@NonNull View view) {
                            if (!TextUtils.isEmpty(merchantCreditDetails.getBoxInfo().getLinkUrl())) {
                                startWebView(merchantCreditDetails.getBoxInfo().getLinkUrl());
                            }
                        }

                        @Override
                        public void updateDrawState(@NonNull TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setUnderlineText(false);
                            try {
                                ds.setColor(Color.parseColor(merchantCreditDetails.getBoxInfo().getLinkTextColor()));
                            } catch (Exception e) {
                                ds.setColor(getResources().getColor(com.tokopedia.design.R.color.tkpd_main_green));
                            }

                        }
                    }, startIndexOfLink, startIndexOfLink + linkText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mclBoxDescTV.setMovementMethod(LinkMovementMethod.getInstance());
                    mclBoxDescTV.setText(spannableString);
                } else {
                    mclBoxDescTV.setText(Html.fromHtml(descText));
                }

            } else {
                mclBoxDescTV.setText(Html.fromHtml(descText));
            }

        } else {
            mclBoxDescTV.setVisibility(View.GONE);
        }
    }

    private void startWebView(String linkUrl) {
        startActivity(SaldoWebViewActivity.getWebViewIntent(context, linkUrl));
    }

    private void populateInfolistData() {
        LayoutInflater layoutInflater = getLayoutInflater();
        mclInfoListLL.removeAllViews();

        for (GqlInfoListResponse infoList1 : merchantCreditDetails.getInfoList()) {

            View view = layoutInflater.inflate(com.tokopedia.saldodetails.R.layout.layout_info_list, null);
            TextView infoLabel = view.findViewById(com.tokopedia.saldodetails.R.id.info_label_text_view);
            TextView infoValue = view.findViewById(com.tokopedia.saldodetails.R.id.info_value_text_view);

            infoLabel.setText(infoList1.getLabel());
            infoValue.setText(infoList1.getValue());

            mclInfoListLL.addView(view);
        }
    }

    private void populateAnchorListData() {
        GqlAnchorListResponse gqlAnchorListResponse = merchantCreditDetails.getAnchorList();
        if (gqlAnchorListResponse != null) {
            mclActionItemTV.setText(gqlAnchorListResponse.getLabel());
            try {
                mclActionItemTV.setTextColor(Color.parseColor(gqlAnchorListResponse.getColor()));
            } catch (Exception e) {
                mclActionItemTV.setTextColor(getResources().getColor(com.tokopedia.design.R.color.tkpd_main_green));
            }
            mclActionItemTV.setOnClickListener(v -> {

                saldoDetailsAnalytics.eventMCLActionItemClick(mclActionItemTV.getText().toString(),
                        String.valueOf(merchantCreditDetails.getStatus()));

                if (gqlAnchorListResponse.isShowDialog() &&
                        gqlAnchorListResponse.getDialogInfo() != null) {

                    CloseableBottomSheetDialog closeableBottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(context);
                    View view = getLayoutInflater().inflate(com.tokopedia.saldodetails.R.layout.mcl_bottom_dialog, null);
                    ((TextView) view.findViewById(com.tokopedia.saldodetails.R.id.mcl_bottom_sheet_title)).setText(gqlAnchorListResponse.getDialogInfo().getDialogTitle());
                    ((TextView) view.findViewById(com.tokopedia.saldodetails.R.id.mcl_bottom_sheet_desc)).setText(gqlAnchorListResponse.getDialogInfo().getDialogBody());

                    closeableBottomSheetDialog.setContentView(view);
                    closeableBottomSheetDialog.show();
                    closeableBottomSheetDialog.setCanceledOnTouchOutside(true);
                } else {
                    if (!TextUtils.isEmpty(gqlAnchorListResponse.getLink())) {
                        startWebView(gqlAnchorListResponse.getLink());
                    }
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
        SaldoDetailsComponent saldoDetailsComponent =
                SaldoDetailsComponentInstance.getComponent(getActivity().getApplication());
        saldoDetailsComponent.inject(this);
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
