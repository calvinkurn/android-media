package com.tokopedia.gamification.taptap.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tokopedia.gamification.R;
import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.taptap.activity.TapTapTokenActivity;
import com.tokopedia.gamification.taptap.compoundview.WidgetSummaryTapTap;
import com.tokopedia.gamification.taptap.data.entiity.RewardButton;
import com.tokopedia.gamification.taptap.database.GamificationDatabaseWrapper;
import com.tokopedia.gamification.taptap.database.GamificationDbCallback;
import com.tokopedia.promogamification.common.applink.ApplinkUtil;

import java.util.List;

public class TapTapSummaryDialogFragment extends DialogFragment implements GamificationDbCallback, WidgetSummaryTapTap.SummaryPageActionListener {

    private static String KEY_BTN = "backbtn";
    private WidgetSummaryTapTap widgetSummaryTapTap;
    private List<RewardButton> rewardButtons;
    private GamificationDatabaseWrapper gamificationDatabaseWrapper;
    private InteractionListener interactionListener;

    public interface InteractionListener {
        void onPlayWithPointsClickedOnSummaryPage();
    }

    public static TapTapSummaryDialogFragment createDialog() {

        TapTapSummaryDialogFragment tapTapSummaryDialogFragment = new TapTapSummaryDialogFragment();
        return tapTapSummaryDialogFragment;

    }

    public void setRewardButtons(List<RewardButton> rewardButtons) {
        this.rewardButtons = rewardButtons;
    }

    public void setListener(InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, com.tokopedia.gamification.R.style.TapTapCustomDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        View view = inflater.inflate(com.tokopedia.gamification.R.layout.gf_popup_summary_page, container, false);
        widgetSummaryTapTap = view.findViewById(R.id.widget_summary_tap_tap);
        widgetSummaryTapTap.setInteractionListener(this);
        gamificationDatabaseWrapper = new GamificationDatabaseWrapper(getContext().getApplicationContext());
        gamificationDatabaseWrapper.getAllEntries(this);
        widgetSummaryTapTap.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onSuccessGetFromDb(List<CrackResultEntity> crackResultEntities) {

        if (crackResultEntities != null && crackResultEntities.size() != 0) {
            widgetSummaryTapTap.setVisibility(View.VISIBLE);
            widgetSummaryTapTap.inflateReward(crackResultEntities);
            widgetSummaryTapTap.renderButtons(rewardButtons);
            gamificationDatabaseWrapper.delete();
        } else {
            dismiss();
        }
    }

    public void showErrorSnackBar(String errorMessage) {
        widgetSummaryTapTap.showErrorSnackBar(errorMessage);
    }

    @Override
    public void onErrorGetFromDb() {
        dismiss();
    }

    @Override
    public void onSuccessGetFromDbForCampaign(List<CrackResultEntity> crackResultEntities) {

    }

    @Override
    public void playWithPoints() {
        if (interactionListener != null)
            interactionListener.onPlayWithPointsClickedOnSummaryPage();

    }

    @Override
    public void dismissDialog() {
        dismiss();
    }

    @Override
    public void navigateToActivity(String applink, String url) {
        ApplinkUtil.navigateToAssociatedPage(getActivity(), applink, url, TapTapTokenActivity.class);
    }

    @Override
    public void onDestroyView() {
        if (widgetSummaryTapTap != null)
            widgetSummaryTapTap.onDestroView();
        super.onDestroyView();
    }
}
