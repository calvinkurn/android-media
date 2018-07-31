package com.tokopedia.train.common.presentation;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.design.component.Menus;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.TrainRouter;
import com.tokopedia.train.homepage.presentation.fragment.TrainHomepageFragment;

/**
 * Created by alvarisi on 2/19/18.
 */

public abstract class TrainBaseActivity extends BaseSimpleActivity {

    private Menus menus;

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
                getResources().getString(R.string.train_homepage_bottom_menu_order_list),
                getResources().getString(R.string.train_homepage_bottom_menu_promo)
        };
        menus.setItemMenuList(menuItem);

        menus.setOnActionClickListener(view -> menus.dismiss());

        menus.setOnItemMenuClickListener((itemMenus, pos) -> {
            switch (pos) {
                case 0:
                    if (getApplication() instanceof TrainRouter) {
                        startActivity(((TrainRouter) getApplication()).getTrainOrderListIntent(this));
                    }
                    break;
                case 1:
                    if (getApplication() instanceof TrainRouter) {
                        startActivity(((TrainRouter) getApplication()).getPromoListIntent(this));
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
}
