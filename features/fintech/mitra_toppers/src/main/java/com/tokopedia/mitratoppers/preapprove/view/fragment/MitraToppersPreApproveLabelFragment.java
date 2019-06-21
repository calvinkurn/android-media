package com.tokopedia.mitratoppers.preapprove.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.mitratoppers.MitraToppersComponentInstance;
import com.tokopedia.mitratoppers.MitraToppersRouter;
import com.tokopedia.mitratoppers.R;
import com.tokopedia.mitratoppers.common.di.component.MitraToppersComponent;
import com.tokopedia.mitratoppers.preapprove.data.model.response.preapprove.ResponsePreApprove;
import com.tokopedia.mitratoppers.preapprove.view.activity.MitraToppersPreApproveWebViewActivity;
import com.tokopedia.mitratoppers.preapprove.view.listener.MitraToppersPreApproveView;
import com.tokopedia.mitratoppers.preapprove.view.presenter.MitraToppersPreApprovePresenter;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import static com.tokopedia.mitratoppers.common.constant.MitraToppersTrackingConstant.ACTION_PREAPPROVED;
import static com.tokopedia.mitratoppers.common.constant.MitraToppersTrackingConstant.ACTION_PREAPPROVED_CLICK;
import static com.tokopedia.mitratoppers.common.constant.MitraToppersTrackingConstant.CATEGORY_PREAPPROVED;
import static com.tokopedia.mitratoppers.common.constant.MitraToppersTrackingConstant.EVENT_FINTECH;
import static com.tokopedia.mitratoppers.common.constant.MitraToppersTrackingConstant.LABEL_CLICK;

public class MitraToppersPreApproveLabelFragment extends BaseDaggerFragment implements MitraToppersPreApproveView {

    public static final String GOLD_MERCHANT = "gold_merchant";
    public static final String OFFICIAL_STORE = "official_store";
    public static final String REGULAR = "regular";
    @Inject
    public MitraToppersPreApprovePresenter mitraToppersPreApprovePresenter;

    private UserSessionInterface userSession;
    private View rootView;
    private LabelView labelView;

    private boolean isGoldMerchant;
    private boolean isOfficialStore;

    public static MitraToppersPreApproveLabelFragment newInstance() {
        return new MitraToppersPreApproveLabelFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSession = new UserSession(getActivity());
    }

    @Override
    protected void initInjector() {
        MitraToppersComponent mitraToppersComponent = MitraToppersComponentInstance.get(
                (BaseMainApplication)getActivity().getApplication());
        mitraToppersComponent.inject(this);
        mitraToppersPreApprovePresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  LayoutInflater.from(getContext()).inflate(R.layout.fragment_mitra_toppers_preapprove, container, false);
        rootView = view.findViewById(R.id.vg_mitra_toppers_root);
        labelView = view.findViewById(R.id.label_view_mitra_toppers);
        rootView.setVisibility(View.GONE);
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        showLoading();
        mitraToppersPreApprovePresenter.attachView(this);
        mitraToppersPreApprovePresenter.getPreApproveBalanceUseCase();
    }

    @Override
    public void onPause() {
        super.onPause();
        mitraToppersPreApprovePresenter.detachView();
    }

    private void showLoading(){
        // no loading shown currently
    }

    private void hideLoading(){
        // no loading hidden currently
    }

    @Override
    public void onSuccessGetPreApprove(final ResponsePreApprove responsePreApprove) {
        hideLoading();
        String amountString = responsePreApprove.getPreapp().getRatePerMonth3m().getAmountIdr();
        if (TextUtils.isEmpty(amountString)) {
            rootView.setVisibility(View.GONE);
            return;
        } else {
            labelView.setContent(amountString);
        }
        final String amountIntegerString = String.valueOf(responsePreApprove.getPreapp().getRatePerMonth3m().getAmount());
        final String preApproveUrl = responsePreApprove.getUrl();
        if (!TextUtils.isEmpty(preApproveUrl)) {
            labelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TrackApp.getInstance().getGTM().sendGTMGeneralEvent(EVENT_FINTECH,
                            CATEGORY_PREAPPROVED, ACTION_PREAPPROVED_CLICK, LABEL_CLICK + " - " + amountIntegerString,
                            userSession.getShopId(),
                            isGoldMerchant? GOLD_MERCHANT : isOfficialStore? OFFICIAL_STORE : REGULAR,
                            userSession.getUserId(),null);
                    Intent intent = MitraToppersPreApproveWebViewActivity.getIntent(getContext(),
                            preApproveUrl);
                    startActivity(intent);
                }
            });
        }
        TrackApp.getInstance().getGTM().sendGTMGeneralEvent(EVENT_FINTECH,
                        CATEGORY_PREAPPROVED, ACTION_PREAPPROVED, amountIntegerString,
                        userSession.getShopId(),
                        isGoldMerchant? GOLD_MERCHANT : isOfficialStore? OFFICIAL_STORE : REGULAR,
                        userSession.getUserId(),null);
        rootView.setVisibility(View.VISIBLE);
    }

    public void setUserInfo(boolean isOfficialStore, boolean isGoldMerchant){
        this.isGoldMerchant = isGoldMerchant;
        this.isOfficialStore = isOfficialStore;
    }

    @Override
    public void onErrorGetPreApprove(Throwable e) {
        hideLoading();
        rootView.setVisibility(View.GONE);
    }

}
