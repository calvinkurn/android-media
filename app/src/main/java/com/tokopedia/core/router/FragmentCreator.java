package com.tokopedia.core.router;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.var.FragmentCreatorConstant;

/**
 * Created by ricoharisin on 11/9/16.
 */

public class FragmentCreator {

    public static Fragment getCatalogDetailListFragment(Context context, String catalogId) {
        Fragment fragment = Fragment.instantiate(context, FragmentCreatorConstant.CATALOG_DETAIL_LIST_FRAGMENT);
        Bundle bundle = new Bundle();
        bundle.putString("catalog_id", catalogId);
        fragment.setArguments(bundle);
        return fragment;
    }

}
