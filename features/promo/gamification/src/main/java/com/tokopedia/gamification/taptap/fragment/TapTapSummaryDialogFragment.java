package com.tokopedia.gamification.taptap.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.gamification.GamificationRouter;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.taptap.compoundview.WidgetSummaryTapTap;
import com.tokopedia.gamification.taptap.data.entiity.ActionButton;
import com.tokopedia.gamification.taptap.data.entiity.BackButton;
import com.tokopedia.gamification.taptap.data.entiity.RewardButton;
import com.tokopedia.gamification.taptap.database.GamificationDatabaseWrapper;
import com.tokopedia.gamification.taptap.database.GamificationDbCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TapTapSummaryDialogFragment extends DialogFragment implements GamificationDbCallback, WidgetSummaryTapTap.SummaryPageActionListener {

    private static String KEY_BTN = "backbtn";
    private WidgetSummaryTapTap widgetSummaryTapTap;
    private List<RewardButton> rewardButtons;
    private GamificationDatabaseWrapper gamificationDatabaseWrapper;


    public static TapTapSummaryDialogFragment createDialog() {

        TapTapSummaryDialogFragment tapTapSummaryDialogFragment = new TapTapSummaryDialogFragment();
        return tapTapSummaryDialogFragment;

    }

    public void setRewardButtons(List<RewardButton> rewardButtons) {
        this.rewardButtons= rewardButtons;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.TapTapCustomDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        View view = inflater.inflate(R.layout.gf_popup_summary_page, container, false);
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

    @Override
    public void onErrorGetFromDb() {
        dismiss();
    }

    @Override
    public void onSuccessGetFromDbForCampaign(List<CrackResultEntity> crackResultEntities) {

    }

    @Override
    public void playWithPoints() {

    }
}
