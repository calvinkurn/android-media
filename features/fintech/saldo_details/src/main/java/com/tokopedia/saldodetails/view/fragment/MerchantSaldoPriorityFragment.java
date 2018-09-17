package com.tokopedia.saldodetails.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.contract.MerchantSaldoPriorityContract;
import com.tokopedia.saldodetails.design.UserStatusInfoBottomSheet;
import com.tokopedia.saldodetails.response.model.GqlMerchantSaldoDetailsResponse;

import java.util.List;

public class MerchantSaldoPriorityFragment extends BaseDaggerFragment implements
        MerchantSaldoPriorityContract.View {

    private static final String COLOR_GREEN = "green";
    private static final String COLOR_GREY = "grey";
    private TextView spTitle;
    private TextView spNewTitle;
    private TextView spDescription;
    private RelativeLayout spKYCStatusLayout;
    private TextView spKYCShortDesc;
    private TextView spKYCLongDesc;
    private LinearLayout spDetailListLinearLayout;
    private LinearLayout spActionListLinearLayout;
    private Switch spEnableSwitchCompat;
    private ImageView spRightArrow;
    private ImageView spStatusInfoIcon;

    private static final String NONE = "none";
    private static final String DEFAULT = "default";
    private static final String WARNING = "warning";
    private static final String DANGER = "danger";

    GqlMerchantSaldoDetailsResponse.Details sellerDetails;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saldo_prioritas, container, false);
        Bundle bundle = getArguments();
        sellerDetails = bundle != null ? bundle.getParcelable("seller_details") : null;
        initViews(view);
        initListeners();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void initViews(View view) {
        spTitle = view.findViewById(R.id.sp_title);
        spNewTitle = view.findViewById(R.id.sp_new_title);
        spDescription = view.findViewById(R.id.sp_description);
        spKYCStatusLayout = view.findViewById(R.id.sp_kyc_status);
        spKYCShortDesc = view.findViewById(R.id.sp_kyc_short_desc);
        spKYCLongDesc = view.findViewById(R.id.sp_kyc_long_desc);
        spDetailListLinearLayout = view.findViewById(R.id.sp_detail_list);
        spActionListLinearLayout = view.findViewById(R.id.sp_action_list);
        spEnableSwitchCompat = view.findViewById(R.id.sp_enable_switch);
        spRightArrow = view.findViewById(R.id.sp_right_arrow);
        spStatusInfoIcon = view.findViewById(R.id.sp_status_info_icon);
    }

    private void initListeners() {

        spEnableSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
                if (isChecked) {
                    dialog.setTitle(getResources().getString(R.string.sp_enable_title));
                    dialog.setDesc(getResources().getString(R.string.sp_enable_desc));
                    dialog.setBtnOk(getResources().getString(R.string.sp_btn_ok_enable));
                } else {
                    dialog.setTitle(getResources().getString(R.string.sp_disable_title));

                    // TODO: 17/09/18 check seller state to add optional message.
                    dialog.setDesc(getResources().getString(R.string.sp_disable_desc));
                    dialog.setBtnOk(getResources().getString(R.string.sp_btn_ok_disable));
                }

                dialog.setOnOkClickListener(v -> {
                    // TODO: 17/09/18 call api to set saldo status
                });

                dialog.setBtnCancel(getResources().getString(R.string.sp_btn_cancel));
                dialog.setOnCancelClickListener(v -> {
                    dialog.dismiss();
                });

                dialog.show();
                dialog.getBtnCancel().setTextColor(getResources().getColor(R.color.black_38));
                dialog.getBtnOk().setTextColor(getResources().getColor(R.color.tkpd_main_green));
            }
        });

        if (sellerDetails.isBoxShowPopup()) {
            spKYCStatusLayout.setOnClickListener(v -> {
                UserStatusInfoBottomSheet userStatusInfoBottomSheet =
                        new UserStatusInfoBottomSheet(context);
                userStatusInfoBottomSheet.setBody(sellerDetails.getPopupDesc());
                userStatusInfoBottomSheet.setTitle(sellerDetails.getPopupTitle());
                userStatusInfoBottomSheet.setButtonText(sellerDetails.getPopupButtonText());
                userStatusInfoBottomSheet.show();
            });
        }
    }


    private void populateData() {

        if (!TextUtils.isEmpty(sellerDetails.getTitle())) {
            spTitle.setText(sellerDetails.getTitle());
        } else {
            spTitle.setText("Saldo Prioritas");
        }

        if (sellerDetails.isShowNewLogo()) {
            spNewTitle.setVisibility(View.VISIBLE);
        } else {
            spNewTitle.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(sellerDetails.getDescription())) {
            spDescription.setText(sellerDetails.getDescription());
            spDescription.setVisibility(View.VISIBLE);
        } else {
            spDescription.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(sellerDetails.getBoxTitle())) {
            spKYCStatusLayout.setVisibility(View.VISIBLE);
            spKYCShortDesc.setVisibility(View.VISIBLE);
            spKYCShortDesc.setText(sellerDetails.getBoxTitle());

            if (!TextUtils.isEmpty(sellerDetails.getBoxDesc())) {
                spKYCLongDesc.setVisibility(View.VISIBLE);
                spKYCLongDesc.setText(sellerDetails.getBoxDesc());
            } else {
                spKYCLongDesc.setVisibility(View.GONE);
            }

            if (sellerDetails.isShowRightArrow()) {
                spRightArrow.setVisibility(View.VISIBLE);
            } else {
                spRightArrow.setVisibility(View.GONE);
            }
            setBoxBackground();
        } else {
            spKYCStatusLayout.setVisibility(View.GONE);
        }


        if (sellerDetails.getInfoList() != null &&
                sellerDetails.getInfoList().size() > 0) {
            populateInfolistData(sellerDetails.getInfoList());
        }

        if (sellerDetails.isShowToggle()) {
            spEnableSwitchCompat.setVisibility(View.VISIBLE);
            spEnableSwitchCompat.setChecked(sellerDetails.isIsEnabled());
            spEnableSwitchCompat.setClickable(true);
        } else {
            spEnableSwitchCompat.setVisibility(View.GONE);
        }

        if (sellerDetails.getAnchorList() != null &&
                sellerDetails.getAnchorList().size() > 0) {
            populateAnchorListData(sellerDetails.getAnchorList());
        }

    }

    private void setBoxBackground() {
        String boxType = sellerDetails.getBoxType();
        if (boxType.equalsIgnoreCase(NONE)) {
            spStatusInfoIcon.setVisibility(View.GONE);
        } else if (boxType.equalsIgnoreCase(DEFAULT)) {

            spStatusInfoIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_info_icon_green));
            spKYCStatusLayout.setBackground(getResources().getDrawable(R.drawable.bg_rounded_corners_green));
        } else if (boxType.equalsIgnoreCase(WARNING)) {
            spStatusInfoIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_info_icon_yellow));
            spKYCStatusLayout.setBackground(getResources().getDrawable(R.drawable.bg_rounded_corner_warning));
        } else if (boxType.equalsIgnoreCase(DANGER)) {
            spStatusInfoIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_info_icon_red));
            spKYCStatusLayout.setBackground(getResources().getDrawable(R.drawable.bg_rounded_corner_danger));
        }
    }

    private void populateAnchorListData(List<GqlMerchantSaldoDetailsResponse.AnchorList> anchorList) {
        LayoutInflater layoutInflater = getLayoutInflater();
        spActionListLinearLayout.removeAllViews();

        for (GqlMerchantSaldoDetailsResponse.AnchorList anchorList1 : anchorList) {

            View view = layoutInflater.inflate(R.layout.layout_anchor_list, null);
            TextView anchorLabel = view.findViewById(R.id.anchor_label);

            anchorLabel.setText(anchorList1.getLabel());
            anchorLabel.setTag(anchorList1.getUrl());

            if (anchorList1.getColor().equalsIgnoreCase(COLOR_GREEN)) {
                anchorLabel.setTextColor(getResources().getColor(R.color.tkpd_main_green));
            } else if (anchorList1.getColor().equalsIgnoreCase(COLOR_GREY)) {
                anchorLabel.setTextColor(getResources().getColor(R.color.black_38));
            }

            anchorLabel.setOnClickListener(v -> RouteManager.route(context, String.format("%s?url=%s",
                    ApplinkConst.WEBVIEW,
                    anchorList1.getUrl())));

            spActionListLinearLayout.addView(view);
        }
    }

    private void populateInfolistData(List<GqlMerchantSaldoDetailsResponse.InfoList> infoList) {
        LayoutInflater layoutInflater = getLayoutInflater();
        spDetailListLinearLayout.removeAllViews();
        for (GqlMerchantSaldoDetailsResponse.InfoList infoList1 : infoList) {

            View view = layoutInflater.inflate(R.layout.layout_info_list, null);
            TextView infoLabel = view.findViewById(R.id.info_label_text_view);
            TextView infoValue = view.findViewById(R.id.info_value_text_view);

            infoLabel.setText(infoList1.getLabel());
            infoValue.setText(infoList1.getValue());

            spDetailListLinearLayout.addView(view);
        }
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void showProgressLoading() {

    }

    @Override
    public void hideProgressLoading() {

    }

    @Override
    public void showErrorMessage() {

    }

    public static Fragment newInstance(Bundle bundle) {
        Fragment merchantSaldoPriorityFragment = new MerchantSaldoPriorityFragment();
        merchantSaldoPriorityFragment.setArguments(bundle);
        return merchantSaldoPriorityFragment;
    }
}
