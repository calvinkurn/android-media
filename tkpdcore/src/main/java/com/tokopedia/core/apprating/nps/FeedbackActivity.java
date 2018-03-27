package com.tokopedia.core.apprating.nps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.RatingEvent;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.core.R;

/**
 * Created by meta on 26/03/18.
 */

public class FeedbackActivity extends BaseSimpleActivity {

    public static void startActivity(Context context, float rating) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        intent.putExtra(FeedbackActivity.class.getSimpleName(), rating);
        context.startActivity(intent);
    }

    private Spinner spCategory;
    private EditText etDesc;

    private float rating;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rating = getIntent().getFloatExtra(FeedbackActivity.class.getSimpleName(), 0);

        spCategory = findViewById(R.id.sp_fb_category);
        etDesc = findViewById(R.id.et_fb_desc);
        Button btnSend = findViewById(R.id.btn_fb_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });
    }

    private void sendFeedback() {
        if (spCategory.getSelectedItemPosition() > 0) {
            String category = spCategory.getSelectedItem().toString();
            String description = etDesc.getText().toString();

            // still on confirmation

            FeedbackThankPageActivity.startActivity(this, rating);
            this.finish();

        } else {
            Toast.makeText(this, R.string.fb_warning, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

}
