package com.tokopedia.sellerapp.gmstat.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tokopedia.sellerapp.R;

import static com.tokopedia.sellerapp.gmstat.views.GMStatHeaderViewHelper.MOVE_TO_SET_DATE;

/**
 * Created by normansyahputa on 12/7/16.
 */

public class SetDateActivity extends AppCompatActivity implements SetDateFragment.SetDate {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_date);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public void returnStartAndEndDate(long startDate, long endDate) {
        Intent intent = new Intent();
        intent.putExtra(SetDateFragment.START_DATE, startDate);
        intent.putExtra(SetDateFragment.END_DATE, endDate);
        setResult(MOVE_TO_SET_DATE, intent);
        finish();
    }
}
