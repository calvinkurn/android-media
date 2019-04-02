package com.tokopedia.tokocash.historytokocash.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.historytokocash.presentation.fragment.DetailTransactionFragment;
import com.tokopedia.tokocash.historytokocash.presentation.model.ItemHistory;

/**
 * Created by nabillasabbaha on 2/12/18.
 */

public class DetailTransactionActivity extends BaseSimpleActivity {

    public static Intent newInstance(Context context, ItemHistory itemHistory) {
        Intent intent = new Intent(context, DetailTransactionActivity.class);
        intent.putExtra(DetailTransactionFragment.ITEM_HISTORY_KEY, itemHistory);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.title_detail_transaction));
    }

    @Override
    protected Fragment getNewFragment() {
        return DetailTransactionFragment
                .newInstance((ItemHistory) getIntent()
                        .getParcelableExtra(DetailTransactionFragment.ITEM_HISTORY_KEY));
    }
}
