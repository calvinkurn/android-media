package com.tokopedia.sellerapp.gmstat.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.tokopedia.sellerapp.R;

import static com.tokopedia.sellerapp.gmstat.views.GMStatActivity.IS_GOLD_MERCHANT;
import static com.tokopedia.sellerapp.gmstat.views.GMStatHeaderViewHelper.MOVE_TO_SET_DATE;

/**
 * Created by normansyahputa on 12/7/16.
 */

public class SetDateActivity extends AppCompatActivity implements SetDateFragment.SetDate {

    private boolean isGoldMerchant;
    private boolean isAfterRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isAfterRotate) {
            fetchIntent(getIntent().getExtras());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_600));
        }
        setContentView(R.layout.activity_set_date);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.green_600));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        isAfterRotate = savedInstanceState == null ? false : true;
    }

    private void fetchIntent(Bundle extras) {
        if(extras != null){
            isGoldMerchant = extras.getBoolean(IS_GOLD_MERCHANT, false);
        }
    }
    @Override
    public void returnStartAndEndDate(long startDate, long endDate) {
        Intent intent = new Intent();
        intent.putExtra(SetDateFragment.START_DATE, startDate);
        intent.putExtra(SetDateFragment.END_DATE, endDate);
        setResult(MOVE_TO_SET_DATE, intent);
        finish();
    }

    @Override
    public boolean isGMStat() {
        return isGoldMerchant;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == com.tokopedia.core.R.id.home) {
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
