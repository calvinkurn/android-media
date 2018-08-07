package com.tokopedia.home.account.presentation.view;

import android.app.Dialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.adapter.MenuGridAdapter;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.util.MenuGridSpacingDecoration;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvinatin on 01/08/18.
 */

public class SeeAllView extends BottomSheets{

    private RecyclerView holder;
    private MenuGridAdapter adapter;
    private AccountItemListener listener;

    @Override
    public int getLayoutResourceId() {
        return R.layout.view_see_all;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        getBottomSheetBehavior().setPeekHeight(screenHeight/2);
    }

    @Override
    public void initView(View view) {
        holder = view.findViewById(R.id.holder_all_item);

        List<MenuGridItemViewModel> list = createItems();

        adapter = new MenuGridAdapter(listener);
        holder.setAdapter(adapter);
        holder.setLayoutManager(
                new GridLayoutManager(view.getContext(),
                        4,
                        LinearLayoutManager.VERTICAL,
                        false));
        holder.addItemDecoration(
                new MenuGridSpacingDecoration(4,0,false));

        adapter.setNewData(list);
    }

    @Override
    protected String title() {
        return getString(R.string.title_menu_other_transaction);
    }

    public void setListener(AccountItemListener listener){
        this.listener = listener;
    }

    private List<MenuGridItemViewModel> createItems(){
        List<MenuGridItemViewModel> list = new ArrayList<>();
        MenuGridItemViewModel gridItem = new MenuGridItemViewModel(
                R.drawable.ic_top_up_bill,
                getContext().getString(R.string.title_menu_top_up_bill),
                ApplinkConst.DIGITAL_ORDER,
                0
        );
        list.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_train,
                getContext().getString(R.string.title_menu_train),
                AccountConstants.Navigation.TRAIN_ORDER_LIST,
                0
        );
        list.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_deals,
                getContext().getString(R.string.title_menu_deals),
                ApplinkConst.DEALS_ORDER,
                0
        );
        list.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_flight,
                getContext().getString(R.string.title_menu_flight),
                ApplinkConst.FLIGHT_ORDER,
                0
        );
        list.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_reksa_dana,
                getContext().getString(R.string.title_menu_reksadana),
                String.format("%s?url=%s",
                        ApplinkConst.WEBVIEW,
                        AccountConstants.Url.REKSA_DANA_URL),
                0
        );
        list.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_event,
                getContext().getString(R.string.title_menu_event),
                ApplinkConst.EVENTS,
                0
        );
        list.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_emas,
                getContext().getString(R.string.title_menu_gold),
                String.format("%s?url=%s",
                        ApplinkConst.WEBVIEW,
                        AccountConstants.Url.EMAS_URL),
                0
        );
        list.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_zakat,
                getContext().getString(R.string.title_menu_zakat),
                String.format("%s?url=%s",
                        ApplinkConst.WEBVIEW,
                        AccountConstants.Url.Pulsa.ZAKAT_URL),
                0
        );
        list.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_donation,
                getContext().getString(R.string.title_menu_donation),
                String.format("%s?%s",
                        ApplinkConst.DIGITAL_PRODUCT,
                        "category_id=12"),
                0
        );
        list.add(gridItem);

        return list;
    }
}
