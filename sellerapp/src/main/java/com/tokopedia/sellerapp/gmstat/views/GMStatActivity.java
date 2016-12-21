package com.tokopedia.sellerapp.gmstat.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.SellerMainApplication;
import com.tokopedia.sellerapp.gmstat.presenters.GMStat;
import com.tokopedia.sellerapp.gmstat.utils.GMStatNetworkController;
import com.tokopedia.sellerapp.home.utils.ImageHandler;

import javax.inject.Inject;

import static com.tokopedia.sellerapp.gmstat.views.GMStatHeaderViewHelper.MOVE_TO_SET_DATE;
import static com.tokopedia.sellerapp.gmstat.views.SetDateFragment.END_DATE;
import static com.tokopedia.sellerapp.gmstat.views.SetDateFragment.START_DATE;

public class GMStatActivity extends AppCompatActivity implements GMStat{

    @Inject
    GMStatNetworkController gmStatNetworkController;

    @Inject
    ImageHandler imageHandler;

    public static final String IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT";
    boolean isAfterRotate = false;
    private boolean isGoldMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isAfterRotate){
            fetchIntent(getIntent().getExtras());
        }
        SellerMainApplication.get(this).getComponent().inject(this);
        setContentView(R.layout.activity_gmstat);
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
        isAfterRotate = savedInstanceState == null ? false : true;
    }

    private void fetchIntent(Bundle extras) {
        if(extras != null){
            isGoldMerchant = extras.getBoolean(IS_GOLD_MERCHANT, false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is the same
        if(requestCode==MOVE_TO_SET_DATE){
            if(data != null){
                long sDate = data.getLongExtra(START_DATE, -1);
                long eDate = data.getLongExtra(END_DATE, -1);
                if(sDate != -1 && eDate != -1){
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
                    if(fragment != null && fragment instanceof GMStatActivityFragment){
                        ((GMStatActivityFragment)fragment).fetchData(sDate, eDate);
                    }
                }
            }
        }
    }

    @Override
    public GMStatNetworkController getGmStatNetworkController() {
        return gmStatNetworkController;
    }

    @Override
    public ImageHandler getImageHandler() {
        return imageHandler;
    }

    @Override
    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }
}
