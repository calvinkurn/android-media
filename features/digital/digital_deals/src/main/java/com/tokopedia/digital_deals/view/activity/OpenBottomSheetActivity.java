package com.tokopedia.digital_deals.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.digital_deals.view.customview.DealsCategoryBottomSheet;
import com.tokopedia.digital_deals.view.model.CategoryItem;

import java.util.ArrayList;
import java.util.List;

public class OpenBottomSheetActivity extends BaseSimpleActivity {

    private static final String CATEGORY_LIST = "categoryList";
    private static ArrayList<CategoryItem> categoryItemArrayList = new ArrayList<>();


    public static Intent setCategoryList(Context context, List<CategoryItem> categoryItemList) {
        categoryItemArrayList.clear();
        categoryItemArrayList.addAll(categoryItemList);
        Intent intent = new Intent(context,  OpenBottomSheetActivity.class);
        intent.putParcelableArrayListExtra(CATEGORY_LIST, categoryItemArrayList);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DealsCategoryBottomSheet dealsCategoryBottomSheet = new DealsCategoryBottomSheet();
        dealsCategoryBottomSheet.setDealsCategoryList(getIntent().getParcelableArrayListExtra(CATEGORY_LIST));
        dealsCategoryBottomSheet.show(getSupportFragmentManager(), "");

        dealsCategoryBottomSheet.setDismissListener(new BottomSheets.BottomSheetDismissListener() {
            @Override
            public void onDismiss() {
                OpenBottomSheetActivity.this.finish();
            }
        });
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }
}
