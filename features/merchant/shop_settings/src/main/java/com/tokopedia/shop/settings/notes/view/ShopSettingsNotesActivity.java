package com.tokopedia.shop.settings.notes.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.shop.settings.R;

public class ShopSettingsNotesActivity extends BaseSimpleActivity implements
        ShopSettingsNotesFragment.OnShopSettingsNoteFragmentListener{

    private TextView tvSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvSave = findViewById(R.id.tvSave);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopSettingsNotesFragment fragment = getCurrentFragment();
                if (fragment.isReorderMode()) {
                    fragment.saveReorder();
                }
            }
        });
        tvSave.setVisibility(View.GONE);
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopSettingsNotesFragment.newInstance();
    }


    private ShopSettingsNotesFragment getCurrentFragment() {
        return (ShopSettingsNotesFragment) getFragment();
    }

    @Override
    public void onBackPressed() {
        ShopSettingsNotesFragment fragment = getCurrentFragment();
        if (fragment != null && fragment.isReorderMode()) {
            fragment.cancelReorderMode();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_shop_setting_note;
    }

    public View getSaveButton(){
        return tvSave;
    }
}
