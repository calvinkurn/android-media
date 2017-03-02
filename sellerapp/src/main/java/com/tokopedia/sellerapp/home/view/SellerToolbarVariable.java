package com.tokopedia.sellerapp.home.view;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.tokopedia.core.var.ToolbarVariable;


/**
 * Created by normansyahputa on 9/19/16.
 */

public class SellerToolbarVariable extends ToolbarVariable {
    private Toolbar toolbar;
    private AppCompatActivity appCompatActivity;

    public SellerToolbarVariable(AppCompatActivity appCompatActivity, Toolbar toolbar) {
        super(appCompatActivity);
        this.toolbar = toolbar;
        this.appCompatActivity = appCompatActivity;
    }

    @Override
    public void createToolbarWithDrawer() {
        holder = new ViewHolder();
        model = new Model();
        this.type = TYPE_MAIN;
        initView(toolbar);
        initListener();
        setAsActionBar();
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
