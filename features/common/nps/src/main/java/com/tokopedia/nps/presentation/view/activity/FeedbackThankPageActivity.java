package com.tokopedia.nps.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.nps.NpsConstant;
import com.tokopedia.nps.R;
import com.tokopedia.nps.presentation.view.dialog.AppRatingDialog;

/**
 * Created by meta on 26/03/18.
 */

public class FeedbackThankPageActivity extends BaseSimpleActivity {

    public static void startActivity(Context context, float rating) {
        Intent intent = new Intent(context, FeedbackThankPageActivity.class);
        intent.putExtra(FeedbackThankPageActivity.class.getSimpleName(), rating);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_feedback_thank;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final float rating = getIntent().getFloatExtra(FeedbackThankPageActivity.class.getSimpleName(), 0);

        TextView tvDesc = findViewById(R.id.tv_fb_thank);
        Button btnClose = findViewById(R.id.btn_fb_action);

        String message = getString(R.string.fb_thank_desc);
        String action = getString(R.string.fb_close);

        if (rating > NpsConstant.Feedback.GOOD_RATING_THRESHOLD) {
            message = getString(R.string.fb_thank_desc_1);
            action = getString(R.string.fb_give_rating);
        }

        tvDesc.setText(message);
        btnClose.setText(action);

        btnClose.setOnClickListener(v -> {
            if (rating > NpsConstant.Feedback.GOOD_RATING_THRESHOLD) {
                AppRatingDialog.openPlayStore(FeedbackThankPageActivity.this);
            }
            FeedbackThankPageActivity.this.finish();
        });
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }
}
