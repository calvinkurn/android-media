package com.tokopedia.sellerapp.gmstat.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core.welcome.WelcomeActivity;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.SellerMainApplication;
import com.tokopedia.sellerapp.drawer.DrawerVariableSeller;
import com.tokopedia.sellerapp.gmstat.presenters.GMStat;
import com.tokopedia.sellerapp.gmstat.utils.GMStatNetworkController;
import com.tokopedia.sellerapp.home.utils.ImageHandler;
import com.tokopedia.sellerapp.home.view.SellerToolbarVariable;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tokopedia.sellerapp.gmstat.views.GMStatHeaderViewHelper.MOVE_TO_SET_DATE;
import static com.tokopedia.sellerapp.gmstat.views.SetDateActivity.PERIOD_TYPE;
import static com.tokopedia.sellerapp.gmstat.views.SetDateActivity.SELECTION_PERIOD;
import static com.tokopedia.sellerapp.gmstat.views.SetDateActivity.SELECTION_TYPE;
import static com.tokopedia.sellerapp.gmstat.views.SetDateFragment.END_DATE;
import static com.tokopedia.sellerapp.gmstat.views.SetDateFragment.START_DATE;

public class GMStatActivity extends AppCompatActivity implements GMStat, SessionHandler.onLogoutListener {

    @Inject
    GMStatNetworkController gmStatNetworkController;

    @Inject
    ImageHandler imageHandler;

    @BindView(R.id.drawer_layout_nav)
    DrawerLayout drawerLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindString(R.string.title_activity_gmstat)
    String titleActivityGMStat;

    @BindColor(R.color.green_600)
    int green600;

    @BindColor(R.color.tkpd_main_green)
    int tkpdMainGreenColor;

    SellerToolbarVariable sellerToolbarVariable;

    public static final String IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT";
    public static final String SHOP_ID = "SHOP_ID";
    boolean isAfterRotate = false;
    private boolean isGoldMerchant;
    private String shopId;
    private DrawerVariableSeller drawer;

    private final long shop_id_staging = 560900;
//    private final long shop_id_staging = 67726;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isAfterRotate){
            fetchIntent(getIntent().getExtras());
        }
        SellerMainApplication.get(this).getComponent().inject(this);
        setContentView(R.layout.activity_gmstat);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(green600);
        }
        toolbar.setTitle(titleActivityGMStat);
        toolbar.setBackgroundColor(tkpdMainGreenColor);
        setSupportActionBar(toolbar);
        initDrawer();

        isAfterRotate = savedInstanceState != null;
    }

    private void initDrawer() {
        drawer = new DrawerVariableSeller(this);
        sellerToolbarVariable = new SellerToolbarVariable(this, toolbar);
        sellerToolbarVariable.createToolbarWithDrawer();
        drawer.setToolbar(sellerToolbarVariable);
        drawer.createDrawer();
        drawer.setEnabled(true);
        drawer.setDrawerPosition(TkpdState.DrawerPosition.SELLER_GM_STAT);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("NIS", "CLICK");
                drawer.openDrawer();
            }
        });
    }

    private void fetchIntent(Bundle extras) {
        if(extras != null){
            isGoldMerchant = extras.getBoolean(IS_GOLD_MERCHANT, false);
            shopId = extras.getString(SHOP_ID, "");

            //[START] This is staging version
//            isGoldMerchant = true;
//            shopId = shop_id_staging+"";
            //[END] This is staging version
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
                int lastSelection = data.getIntExtra(SELECTION_PERIOD, 1);
                int selectionType = data.getIntExtra(SELECTION_TYPE, PERIOD_TYPE);
                if(sDate != -1 && eDate != -1){
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
                    if(fragment != null && fragment instanceof GMStatActivityFragment){
                        ((GMStatActivityFragment)fragment).fetchData(sDate, eDate, lastSelection, selectionType);
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

    @Override
    public String getShopId() {
        return shopId;
    }

    @Override
    public void onLogout(Boolean success) {
        if (success) {
            finish();
            Intent intent;
            if (GlobalConfig.isSellerApp()) {
                intent = new Intent(this, WelcomeActivity.class);
            } else {
                intent = HomeRouter.getHomeActivity(this);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
