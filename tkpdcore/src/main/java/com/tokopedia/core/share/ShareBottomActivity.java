package com.tokopedia.core.share;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.share.fragment.ProductBottomShareFragment;
import com.tokopedia.core.share.fragment.ProductShareFragment;

public class ShareBottomActivity extends BaseActivity {
    private ShareData data;

    public static Intent createIntent(Context context, ShareData shareData) {
        Intent intent = new Intent(context, ShareBottomActivity.class);
        intent.putExtra(ShareData.TAG, shareData);
        return intent;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SHARE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setStatusBarDim();
        setContentView(R.layout.activity_share_bottom);
        if (getIntent() != null) {
            Intent intent = getIntent();
            data = intent.getParcelableExtra(ShareData.TAG);
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, ProductBottomShareFragment.newInstance(data, false),
                ProductShareFragment.class.getSimpleName());
        fragmentTransaction.commit();

        findViewById(R.id.touch_outside).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        BottomSheetBehavior.from(findViewById(R.id.bottom_sheet))
                .setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        switch (newState) {
                            case BottomSheetBehavior.STATE_HIDDEN:
                                finish();
                                break;
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        // no op
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setStatusBarDim() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

}
