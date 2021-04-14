package com.tokopedia.review.feature.reputationhistory.view.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;

import com.tokopedia.core.shopinfo.models.shopmodel.Stats;
import com.tokopedia.review.R;
import com.tokopedia.unifyprinciples.Typography;

/**
 * @author normansyahputa on 3/20/17.
 */

public class ReputationView extends FrameLayout implements BaseView<ReputationView.ReputationUiModel> {

    LinearLayout reputationBadgeListener;
    Typography reputationPoints;
    @LayoutRes
    private int defaultLayoutId;

    public ReputationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        defaultLayoutId = R.layout.reputation_item_view_reputation;
        LayoutInflater.from(context).inflate(defaultLayoutId, this);

        reputationBadgeListener = (LinearLayout) findViewById(R.id.reputation_badge_listener);
        reputationPoints = (Typography) findViewById(R.id.reputation_points);
    }

    @Override
    public void init(ReputationUiModel data) {

        ReputationLevelUtils.setReputationMedalsWithoutDialog(getContext(), reputationBadgeListener, data.typeMedal, data.levelMedal, data.reputationPoints);

        reputationPoints.setText(String.format("%s %s", data.stats.shopReputationScore, getContext().getString(R.string.point)));
    }

    public static class ReputationUiModel {
        public int typeMedal;
        public int levelMedal;
        public String reputationPoints;
        public Stats stats;
    }


}