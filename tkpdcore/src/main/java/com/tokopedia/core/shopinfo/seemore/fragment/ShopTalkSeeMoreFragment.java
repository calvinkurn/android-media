package com.tokopedia.core.shopinfo.seemore.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.shopinfo.models.talkmodel.ShopTalk;
import com.tokopedia.core.shopinfo.models.talkmodel.ShopTalkResult;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.shopinfo.seemore.adapter.ShopTalkSeeMoreAdapter;
import com.tokopedia.core.shopinfo.seemore.model.ShopTalkSeeMore;
import android.support.v7.widget.LinearLayoutManager;

import java.util.List;

/**
 * Created by normansyahputa on 2/14/18.
 */

public class ShopTalkSeeMoreFragment extends com.tokopedia.core.shopinfo.fragment.ShopTalkFragment {

    public static Fragment createInstance() {
        return new ShopTalkSeeMoreFragment();
    }

    @Override
    protected void initView(View view) {
        adapter = ShopTalkSeeMoreAdapter.createInstance(getActivity(), getShopTalkListener());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

        if(!isViewShown) {
            fetchData();
        }
    }

    @Override
    public void onGetShopTalk(ShopTalkResult result) {
        boolean hasNext = PagingHandler.CheckHasNext(result.getPaging());

        List<ShopTalk> list = result.getList();
        boolean isSeeMoreEnabled = false;
        if(!result.getList().isEmpty() && result.getList().size() >= 10 && hasNext){
            // add see more
            isSeeMoreEnabled = true;

            list = list.subList(0,10);
        }
        adapter.addList(list);
        if (adapter.getList().isEmpty()) {
            adapter.showEmptyFull(true);
        }else {
            adapter.showEmptyFull(false);
        }
        if(isSeeMoreEnabled) {
            adapter.addItem(new ShopTalkSeeMore());
        }
        adapter.setHaveNext(false);
    }

    protected boolean isEnableScroll(){
        return false;
    }
}
