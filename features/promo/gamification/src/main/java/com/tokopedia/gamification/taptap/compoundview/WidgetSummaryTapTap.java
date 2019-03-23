package com.tokopedia.gamification.taptap.compoundview;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.gamification.GamificationRouter;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.applink.ApplinkUtil;
import com.tokopedia.gamification.data.entity.CrackBenefitEntity;
import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.taptap.activity.TapTapTokenActivity;
import com.tokopedia.gamification.taptap.data.entiity.RewardButton;
import com.tokopedia.gamification.taptap.utils.TapTapConstants;
import com.tokopedia.gamification.util.TapTapAnalyticsTrackerUtil;

import java.util.ArrayList;
import java.util.List;

public class WidgetSummaryTapTap extends FrameLayout {
    private ImageView imageSinar;
    private Button btnBottomLeft;
    private Button btnBottomRight;
    private Button btnTop;
    private RecyclerView rvRewards;
    private RewardsAdapter rewardsAdapter;
    private SummaryPageActionListener interactionListener;
    private RelativeLayout rlChildSummary;
    private View errorView;
    private TextView tvErrorMessage;
    private TextView tvBtnErrorOk;

    public interface SummaryPageActionListener {

        void playWithPoints();

        void dismissDialog();
    }

    public WidgetSummaryTapTap(@NonNull Context context) {
        super(context);
        init();
    }

    public WidgetSummaryTapTap(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetSummaryTapTap(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_reward_summary_tap_tap, this, true);
        imageSinar = view.findViewById(R.id.image_sinar);
        rvRewards = view.findViewById(R.id.rv_rewards);
        btnTop = view.findViewById(R.id.btn_top);
        btnBottomLeft = view.findViewById(R.id.btn_bottom_left);
        btnBottomRight = view.findViewById(R.id.btn_bottom_right);
        rlChildSummary = view.findViewById(R.id.rl_child_summary);
        errorView = view.findViewById(R.id.error_view);
        tvErrorMessage = view.findViewById(R.id.tv_msg);
        tvBtnErrorOk = view.findViewById(R.id.snack_ok);
        rewardsAdapter = new RewardsAdapter(null);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), R.drawable.item_divider_summary_page);
        dividerItemDecoration.setHorizontalMargin(getResources().getDimensionPixelOffset(R.dimen.dp_8));
        rvRewards.addItemDecoration(dividerItemDecoration);
        initListBound(getScreenHeight(), 0);
        tvBtnErrorOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                errorView.setVisibility(GONE);
            }
        });
    }

    public void showErrorSnackBar(String errorMessage) {
        errorView.setVisibility(VISIBLE);
        tvErrorMessage.setText(errorMessage);
        TapTapAnalyticsTrackerUtil.sendEvent(getContext(),
                TapTapAnalyticsTrackerUtil.EventKeys.VIEW_GAME,
                TapTapAnalyticsTrackerUtil.CategoryKeys.CATEGORY_TAP_TAP,
                TapTapAnalyticsTrackerUtil.ActionKeys.REWARD_SUMMARY_IMPRESSION_ON_ERROR_TOASTER,
                "");
    }


    private void initListBound(int height, int width) {
        ViewGroup.LayoutParams ivFullLp = rvRewards.getLayoutParams();
        ivFullLp.height = (int) (height / 2.5f);
        rvRewards.requestLayout();
        rvRewards.setAdapter(rewardsAdapter);
    }

    public void setInteractionListener(SummaryPageActionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    public void inflateReward(List<CrackResultEntity> rewards) {
        rewardsAdapter.updateList(rewards);
        runLayoutAnimation();
        Animation rotateAnimationCrackResult = AnimationUtils.loadAnimation(getContext(), R.anim.animation_rotate_bg_crack_result);
        rotateAnimationCrackResult.setDuration(15000);
        imageSinar.startAnimation(rotateAnimationCrackResult);

    }

    public void renderButtons(List<RewardButton> rewardButtons) {
        if (rewardButtons != null && rewardButtons.size() != 0) {
            if (rewardButtons.size() == 1) {
                btnTop.setVisibility(GONE);
                btnBottomLeft.setVisibility(GONE);
                btnBottomRight.setVisibility(VISIBLE);
                setActionButton(rewardButtons.get(0), btnTop);
            } else if (rewardButtons.size() == 2) {
                btnBottomLeft.setVisibility(GONE);
                btnTop.setVisibility(VISIBLE);
                btnBottomRight.setVisibility(VISIBLE);
                setActionButton(rewardButtons.get(0), btnTop);
                setActionButton(rewardButtons.get(1), btnBottomRight);
            } else {
                btnBottomLeft.setVisibility(VISIBLE);
                btnTop.setVisibility(VISIBLE);
                btnBottomRight.setVisibility(VISIBLE);
                setActionButton(rewardButtons.get(0), btnTop);
                setActionButton(rewardButtons.get(1), btnBottomLeft);
                setActionButton(rewardButtons.get(2), btnBottomRight);
            }
        } else {
            btnTop.setVisibility(GONE);
            btnBottomLeft.setVisibility(GONE);
            btnBottomRight.setVisibility(GONE);
        }
    }

    private void setActionButton(RewardButton rewardButton, Button btnAction) {
        btnAction.setText(rewardButton.getText());
        btnAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TapTapConstants.ButtonType.PLAY_WITH_POINTS.equalsIgnoreCase(rewardButton.getType())) {
                    interactionListener.playWithPoints();
                } else {
                    interactionListener.dismissDialog();
                    if (rewardButton.getApplink().contains("tokopedia://tokopoints")) {
                        getContext().startActivity(((GamificationRouter) getContext().getApplicationContext()).getTokoPointsIntent(getContext()));
                    } else {
                        ApplinkUtil.navigateToAssociatedPage(getContext(), rewardButton.getApplink(),
                                rewardButton.getUrl(),
                                TapTapTokenActivity.class);
                    }

                }
                TapTapAnalyticsTrackerUtil.sendEvent(getContext(),
                        TapTapAnalyticsTrackerUtil.EventKeys.CLICK_GAME,
                        TapTapAnalyticsTrackerUtil.CategoryKeys.CATEGORY_TAP_TAP,
                        TapTapAnalyticsTrackerUtil.ActionKeys.REWARD_SUMMARY_CLICK,
                        rewardButton.getText());
            }
        });
    }

    class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.RewardsHolder> {


        private List<CrackResultEntity> rewardItems;

        public RewardsAdapter(List<CrackResultEntity> rewardItems) {
            this.rewardItems = rewardItems;
        }

        @NonNull
        @Override
        public RewardsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new RewardsHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_rewards_summary, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RewardsHolder rewardsHolder, int i) {
            rewardsHolder.bindData(rewardItems.get(i));
        }

        @Override
        public int getItemCount() {
            return rewardItems == null ? 0 : rewardItems.size();
        }

        public void updateList(List<CrackResultEntity> rewards) {
            this.rewardItems = rewards;
        }

        class RewardsHolder extends RecyclerView.ViewHolder {

            private final TextView rewardText;
            private final ImageView imageReward;

            public RewardsHolder(@NonNull View itemView) {
                super(itemView);
                rewardText = itemView.findViewById(R.id.reward_text);
                imageReward = itemView.findViewById(R.id.image_reward);

            }

            public void bindData(CrackResultEntity crackResultEntity) {

                ImageHandler.loadImage(getContext(), imageReward, crackResultEntity.getImageUrl(), R.color.grey_1100, R.color.grey_1100);
                ArrayList<String> rewardString = null;
                if (crackResultEntity.getBenefits() != null) {
                    rewardString = new ArrayList<>();
                    for (CrackBenefitEntity crackBenefitEntity : crackResultEntity.getBenefits()) {
                        rewardString.add(crackBenefitEntity.getText());
                    }
                }

                if (rewardString != null)
                    rewardText.setText(TextUtils.join("\n", rewardString));
            }
        }
    }


    private void runLayoutAnimation() {

        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_bottom);

        rvRewards.setLayoutAnimation(controller);
        rvRewards.getAdapter().notifyDataSetChanged();
        rvRewards.scheduleLayoutAnimation();
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
