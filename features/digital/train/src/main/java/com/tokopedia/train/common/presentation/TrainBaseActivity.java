package com.tokopedia.train.common.presentation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.design.component.Menus;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.TrainRouter;
import com.tokopedia.train.common.constant.TrainUrl;
import com.tokopedia.train.common.di.TrainComponent;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.util.TrainAnalytics;

import javax.inject.Inject;

/**
 * Created by alvarisi on 2/19/18.
 */

public abstract class TrainBaseActivity extends BaseSimpleActivity {
    private static final int MENU_ORDER_LIST = 0;
    private static final int MENU_PROMO = 1;
    private static final int MENU_HELP = 2;

    private Menus menus;

    private TrainComponent trainComponent;

    @Inject
    TrainAnalytics trainAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initInjector();
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        showBottomMenus();
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        if (isOverflowMenuVisible()) {
            getMenuInflater().inflate(R.menu.menu_train_homepage, menu);
            updateOptionMenuColorWhite(menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isOverflowMenuVisible()) {
            if (item.getItemId() == R.id.action_overflow_menu) {
                showBottomMenus();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void showBottomMenus() {
        menus = new Menus(this);
        String[] menuItem = new String[]{
                getResources().getString(R.string.train_homepage_bottom_menu_transaction_list),
                getResources().getString(R.string.train_homepage_bottom_menu_promo),
                getResources().getString(R.string.train_homepage_bottom_menu_help)
        };
        menus.setItemMenuList(menuItem);

        menus.setOnActionClickListener(view -> menus.dismiss());

        menus.setOnItemMenuClickListener((itemMenus, pos) -> {
            switch (pos) {
                case MENU_ORDER_LIST:
                    if (getApplication() instanceof TrainRouter) {
                        trainAnalytics.eventClickTransactionList();
                        startActivity(((TrainRouter) getApplication()).getTrainOrderListIntent(this));
                    }
                    break;
                case MENU_PROMO:
                    if (getApplication() instanceof TrainRouter) {
                        trainAnalytics.eventClickPromoList();
                        startActivity(((TrainRouter) getApplication()).getPromoListIntent(this,  TrainUrl.PARAM_TRAIN_MENU_ID, TrainUrl.PARAM_TRAIN_SUBMENU_ID));
                    }
                    break;
                case MENU_HELP:
                    if (getApplication() instanceof TrainRouter) {
                        trainAnalytics.eventClickHelp();
                        startActivity(((TrainRouter) getApplication()).getWebviewActivity(this, TrainUrl.HELP_PAGE));
                    }
                    break;
            }
            menus.dismiss();
        });

        menus.setActionText(getResources().getString(R.string.train_homepage_bottom_menu_action_text));

        menus.show();
    }

    protected boolean isOverflowMenuVisible() {
        return true;
    }

    protected void initInjector() {
        if (trainComponent == null) {
            trainComponent = TrainComponentUtils.getTrainComponent(getApplication());
        }
        trainComponent.inject(this);
    }

}
