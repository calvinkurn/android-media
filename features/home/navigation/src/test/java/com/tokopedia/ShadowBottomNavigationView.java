package com.tokopedia;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadows.ShadowFrameLayout;

import javax.annotation.Nullable;

@Implements(BottomNavigationView.class)
public class ShadowBottomNavigationView extends ShadowFrameLayout {
    @RealObject
    BottomNavigationView real;

    private BottomNavigationView.OnNavigationItemSelectedListener listener;

    public MenuItem getSelectedMenuItem() {
        for (int i = 0; i < real.getMenu().size(); i++) {
            MenuItem item = real.getMenu().getItem(i);
            if(item.isChecked()) {
                return item;
            }
        }
        return null;
    }

    @Implementation
    public void setOnNavigationItemSelectedListener(
            @Nullable BottomNavigationView.OnNavigationItemSelectedListener listener) {
        this.listener = listener;
    }

    public void selectMenuItem(int index) {
        real.getMenu().getItem(index).setChecked(true);
        listener.onNavigationItemSelected(getSelectedMenuItem());
    }
}
